using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Simex.Dtos;
using Simex.Models;
using Simex.Services;

namespace Simex.Controllers;

[ApiController]
[Route("api/operations")]
[Authorize]
public class OperationsController : ControllerBase
{
    private readonly Simex04Context _context;
    private readonly IOperationTrackingService _operationTrackingService;

    public OperationsController(Simex04Context context, IOperationTrackingService operationTrackingService)
    {
        _context = context;
        _operationTrackingService = operationTrackingService;
    }

    [HttpGet("user-operations")]
    public async Task<ActionResult<IEnumerable<OperationDto>>> GetUserOperations()
    {
        var userIdClaim = User.FindFirstValue(ClaimTypes.NameIdentifier);
        if (!int.TryParse(userIdClaim, out var userId)) return Unauthorized(new { message = "Token invalido." });

        var user = await _context.Users.AsNoTracking().FirstOrDefaultAsync(u => u.Id == userId);
        if (user == null || user.CompanyId == null) return Ok(new List<OperationDto>());

        var operationsDto = await _context.Operations
        .AsNoTracking()
        .Where(op => op.NavieraId == user.CompanyId)
        .Select(op => new OperationDto
        {
            Id = op.Id,
            OrderReference = op.OrderReference,
            OriginPortName = op.OriginPort.Name ?? "Puerto Desconocido",
            DestinationPortName = op.DestinationPort.Name ?? "Puerto Desconocido",
            TotalCost = op.TotalCost,
            Etd = op.Etd,
            Eta = op.Eta,
            IncotermCode = op.Incoterm != null ? (op.Incoterm.IncotermType.Code ?? "N/A") : "N/A",
            PiecesNumber = op.PiecesNumber,
            Kilograms = op.Kilograms,
            StatusName = op.OperationStateHistories
                .OrderByDescending(osh => osh.Id)
                .Select(osh => osh.OperationState != null ? osh.OperationState.Name : null)
                .FirstOrDefault() ?? "Sin estado",
            TrackingFlowId = op.TrackingFlowId,
            TrackingFlowName = op.TrackingFlow != null ? op.TrackingFlow.Name : null,
            CurrentTrackingFlowStepId = op.CurrentTrackingFlowStepId,
            CurrentTrackingStepName = op.CurrentTrackingFlowStep != null ? op.CurrentTrackingFlowStep.Name : null,
            CurrentTrackingStepOrder = op.CurrentTrackingFlowStep != null ? op.CurrentTrackingFlowStep.OrderNum : null,
            CurrentTrackingStepUiPercent = op.CurrentTrackingFlowStep != null ? op.CurrentTrackingFlowStep.UiPercent : null,
            CurrentTrackingStepArrivedAt = op.CurrentTrackingStepArrivedAt
        }).ToListAsync();
        return Ok(operationsDto);
    }

    [HttpGet("recent")]
    public async Task<ActionResult<IEnumerable<OperationDto>>> GetRecentOperations()
    {
        var userIdClaim = User.FindFirstValue(ClaimTypes.NameIdentifier);
        if (!int.TryParse(userIdClaim, out var userId))
            return Unauthorized(new { message = "Token invalido." });

        var user = await _context.Users.AsNoTracking().FirstOrDefaultAsync(u => u.Id == userId);
        if (user == null || user.CompanyId == null)
            return Ok(new List<OperationDto>());

        DateTime haceSieteDias = DateTime.Today.AddDays(-7);
        DateTime hoy = DateTime.Today.AddDays(1).AddTicks(-1);

        var operationsDto = await _context.Operations
            .AsNoTracking()
            .Where(op => op.NavieraId == user.CompanyId && op.Etd >= haceSieteDias && op.Etd <= hoy)
            .OrderByDescending(op => op.Etd)
            .Select(op => new OperationDto
            {
                Id = op.Id,
                OrderReference = op.OrderReference,
                OriginPortName = op.OriginPort.Name ?? "Puerto Desconocido",
                DestinationPortName = op.DestinationPort.Name ?? "Puerto Desconocido",
                TotalCost = op.TotalCost,
                Etd = op.Etd,
                Eta = op.Eta,
                IncotermCode = op.Incoterm != null ? (op.Incoterm.IncotermType.Code ?? "N/A") : "N/A",
                PiecesNumber = op.PiecesNumber,
                Kilograms = op.Kilograms,
                StatusName = op.OperationStateHistories
                    .OrderByDescending(osh => osh.Id)
                    .Select(osh => osh.OperationState != null ? osh.OperationState.Name : null)
                    .FirstOrDefault() ?? "Sin Estado",
                TrackingFlowId = op.TrackingFlowId,
                TrackingFlowName = op.TrackingFlow != null ? op.TrackingFlow.Name : null,
                CurrentTrackingFlowStepId = op.CurrentTrackingFlowStepId,
                CurrentTrackingStepName = op.CurrentTrackingFlowStep != null ? op.CurrentTrackingFlowStep.Name : null,
                CurrentTrackingStepOrder = op.CurrentTrackingFlowStep != null ? op.CurrentTrackingFlowStep.OrderNum : null,
                CurrentTrackingStepUiPercent = op.CurrentTrackingFlowStep != null ? op.CurrentTrackingFlowStep.UiPercent : null,
                CurrentTrackingStepArrivedAt = op.CurrentTrackingStepArrivedAt
            })
            .ToListAsync();
        return Ok(operationsDto);
    }

    [HttpPost("{operationId:int}/reject")]
    public async Task<IActionResult> RejectOperation(int operationId, [FromBody] RejectOperationRequestDto request)
    {
        IActionResult result;
        var userIdClaim = User.FindFirstValue(ClaimTypes.NameIdentifier);

        if (!int.TryParse(userIdClaim, out var userId))
        {
            result = Unauthorized(new { message = "Token invalido." });
        }
        else if (string.IsNullOrWhiteSpace(request.Reason))
        {
            result = BadRequest(new { message = "El motivo del rechazo es obligatorio." });
        }
        else
        {
            var user = await _context.Users
                .AsNoTracking()
                .FirstOrDefaultAsync(u => u.Id == userId);

            if (user == null || user.CompanyId == null)
            {
                result = NotFound(new { message = "Usuario no encontrado o sin empresa asociada." });
            }
            else
            {
                var operation = await _context.Operations
                    .AsNoTracking()
                    .FirstOrDefaultAsync(op => op.Id == operationId && op.NavieraId == user.CompanyId);

                if (operation == null)
                {
                    result = NotFound(new { message = "Operacion no encontrada." });
                }
                else
                {
                    var rejectedState = await _context.OperationStates
                        .AsNoTracking()
                        .FirstOrDefaultAsync(state => state.Name == "Rechazada");

                    if (rejectedState == null)
                    {
                        result = StatusCode(500, new { message = "No existe un estado de rechazo configurado para operaciones." });
                    }
                    else
                    {
                        var rejectionReason = request.Reason.Trim();

                        _context.OperationStateHistories.Add(new OperationStateHistory
                        {
                            OperationId = operationId,
                            OperationStateId = rejectedState.Id,
                            Observations = rejectionReason,
                            Date = DateTime.Now
                        });

                        await _context.SaveChangesAsync();

                        result = Ok(new
                        {
                            message = "Operacion rechazada correctamente.",
                            operationId,
                            operationStateId = rejectedState.Id,
                            rejectionReason
                        });
                    }
                }
            }
        }

        return result;
    }

    [HttpPost("{operationId:int}/accept")]
    public async Task<IActionResult> AcceptOperation(int operationId)
    {
        IActionResult result;
        var userIdClaim = User.FindFirstValue(ClaimTypes.NameIdentifier);

        if (!int.TryParse(userIdClaim, out var userId))
        {
            result = Unauthorized(new { message = "Token invalido." });
        }
        else
        {
            var user = await _context.Users
                .AsNoTracking()
                .FirstOrDefaultAsync(u => u.Id == userId);

            if (user == null || user.CompanyId == null)
            {
                result = NotFound(new { message = "Usuario no encontrado o sin empresa asociada." });
            }
            else
            {
                var operation = await _context.Operations
                    .AsNoTracking()
                    .FirstOrDefaultAsync(op => op.Id == operationId && op.NavieraId == user.CompanyId);

                if (operation == null)
                {
                    result = NotFound(new { message = "Operacion no encontrada." });
                }
                else
                {
                    var acceptedState = await _context.OperationStates
                        .AsNoTracking()
                        .FirstOrDefaultAsync(state => state.Name == "Aceptada");

                    if (acceptedState == null)
                    {
                        result = StatusCode(500, new { message = "No existe un estado de aceptacion configurado para operaciones." });
                    }
                    else
                    {
                        _context.OperationStateHistories.Add(new OperationStateHistory
                        {
                            OperationId = operationId,
                            OperationStateId = acceptedState.Id,
                            Observations = null,
                            Date = DateTime.Now
                        });

                        await _context.SaveChangesAsync();

                        result = Ok(new
                        {
                            message = "Operacion aceptada correctamente.",
                            operationId,
                            operationStateId = acceptedState.Id
                        });
                    }
                }
            }
        }

        return result;
    }

    [HttpPut("{operationId:int}/tracking/current-step")]
    public async Task<IActionResult> UpdateCurrentTrackingStep(int operationId, [FromBody] UpdateOperationTrackingStepRequestDto request)
    {
        IActionResult result;
        var userIdClaim = User.FindFirstValue(ClaimTypes.NameIdentifier);

        if (!int.TryParse(userIdClaim, out var userId))
        {
            result = Unauthorized(new { message = "Token invalido." });
        }
        else
        {
            var user = await _context.Users
                .AsNoTracking()
                .FirstOrDefaultAsync(u => u.Id == userId);

            if (user == null || user.CompanyId == null)
            {
                result = NotFound(new { message = "Usuario no encontrado o sin empresa asociada." });
            }
            else
            {
                var operation = await _context.Operations
                    .FirstOrDefaultAsync(op => op.Id == operationId && op.NavieraId == user.CompanyId);

                if (operation == null)
                {
                    result = NotFound(new { message = "Operacion no encontrada." });
                }
                else
                {
                    var updateResult = await _operationTrackingService.UpdateCurrentStepAsync(
                        operation,
                        request.TrackingFlowStepId,
                        request.ArrivedAt,
                        request.Observations,
                        userId,
                        HttpContext.RequestAborted
                    );

                    if (!updateResult.IsValid)
                    {
                        result = BadRequest(new { message = updateResult.ErrorMessage });
                    }
                    else
                    {
                        var currentStep = await _context.TrackingFlowSteps
                            .AsNoTracking()
                            .Where(step => step.Id == operation.CurrentTrackingFlowStepId)
                            .Select(step => new
                            {
                                step.Id,
                                step.Name,
                                step.OrderNum,
                                step.TrackingFlowId
                            })
                            .FirstOrDefaultAsync();

                        result = Ok(new
                        {
                            message = "Tracking actualizado correctamente.",
                            operationId = operation.Id,
                            trackingFlowId = operation.TrackingFlowId,
                            currentTrackingStepId = operation.CurrentTrackingFlowStepId,
                            currentTrackingStepName = currentStep?.Name,
                            currentTrackingStepOrder = currentStep?.OrderNum,
                            currentTrackingStepArrivedAt = operation.CurrentTrackingStepArrivedAt
                        });
                    }
                }
            }
        }

        return result;
    }
}
