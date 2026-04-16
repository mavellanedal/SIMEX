using System.Security.Claims;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Simex.Dtos;
using Simex.Models;

namespace Simex.Controllers;

[ApiController]
[Route("api/operations")]
[Authorize]
public class OperationsController : ControllerBase
{
    private readonly Simex04Context _context;

    public OperationsController(Simex04Context context)
    {
        _context = context;
    }

    [HttpGet("user-operations")]
    public async Task<ActionResult<IEnumerable<OperationDto>>> GetUserOperations()
    {
        var userIdClaim = User.FindFirstValue(ClaimTypes.NameIdentifier);
        if (!int.TryParse(userIdClaim, out var userId)) return Unauthorized(new { message = "Token invalido." });

        var user = await _context.Users.AsNoTracking().FirstOrDefaultAsync(u => u.Id == userId);
        if (user == null || user.CompanyId == null) return Ok(new List<OperationDto>());

        var operations = await _context.Operations
            .AsNoTracking()
            .Include(op => op.OriginPort)
            .Include(op => op.DestinationPort)
            .Where(op => op.NavieraId == user.CompanyId)
            .ToListAsync();

        var operationsDto = operations.Select(op => new OperationDto
        {
            Id = op.Id,
            OrderReference = op.OrderReference,
            OriginPortName = op.OriginPort?.Name ?? "Puerto Desconocido",
            DestinationPortName = op.DestinationPort?.Name ?? "Puerto Desconocido",
            TotalCost = op.TotalCost,
            Etd = op.Etd,
            Eta = op.Eta,
            IncotermId = op.IncotermId,
            PiecesNumber = op.PiecesNumber,
            Kilograms = op.Kilograms
        }).ToList();

        return Ok(operationsDto);
    }

    [HttpGet("recent")]
    public async Task<ActionResult<IEnumerable<OperationDto>>> GetRecentOperations()
    {
        var userIdClaim = User.FindFirstValue(ClaimTypes.NameIdentifier);
        if (!int.TryParse(userIdClaim, out var userId)) return Unauthorized(new { message = "Token inválido." });

        var user = await _context.Users.AsNoTracking().FirstOrDefaultAsync(u => u.Id == userId);
        if (user == null || user.CompanyId == null) return Ok(new List<OperationDto>());

        DateTime haceSieteDias = DateTime.Now.AddDays(-7);
        DateTime hoy = DateTime.Now;

        var operations = await _context.Operations
            .AsNoTracking()
            .Include(op => op.OriginPort)
            .Include(op => op.DestinationPort)
            .Where(op => op.NavieraId == user.CompanyId && op.Etd >= haceSieteDias && op.Etd <= hoy)
            .OrderByDescending(op => op.Etd)
            .ToListAsync();

        var operationsDto = operations.Select(op => new OperationDto
        {
            Id = op.Id,
            OrderReference = op.OrderReference,
            OriginPortName = op.OriginPort?.Name ?? "Puerto Desconocido",
            DestinationPortName = op.DestinationPort?.Name ?? "Puerto Desconocido",
            TotalCost = op.TotalCost,
            Etd = op.Etd,
            Eta = op.Eta,
            IncotermId = op.IncotermId,
            PiecesNumber = op.PiecesNumber,
            Kilograms = op.Kilograms
        }).ToList();

        return Ok(operationsDto);
    }
}