using Microsoft.EntityFrameworkCore;
using Simex.Models;

namespace Simex.Services;

public interface IOperationTrackingService
{
    Task<(bool IsValid, string? ErrorMessage)> UpdateCurrentStepAsync(
        Operation operation,
        int trackingFlowStepId,
        DateTime? arrivedAt,
        string? observations,
        int? userId,
        CancellationToken cancellationToken = default);
}

public class OperationTrackingService : IOperationTrackingService
{
    private readonly Simex04Context _context;

    public OperationTrackingService(Simex04Context context)
    {
        _context = context;
    }

    public async Task<(bool IsValid, string? ErrorMessage)> UpdateCurrentStepAsync(
        Operation operation,
        int trackingFlowStepId,
        DateTime? arrivedAt,
        string? observations,
        int? userId,
        CancellationToken cancellationToken = default)
    {
        var trackingStep = await _context.TrackingFlowSteps
            .AsNoTracking()
            .FirstOrDefaultAsync(step => step.Id == trackingFlowStepId && step.Active, cancellationToken);

        if (trackingStep == null)
        {
            return (false, "El step de tracking indicado no existe o esta inactivo.");
        }

        if (operation.TrackingFlowId == null)
        {
            operation.TrackingFlowId = trackingStep.TrackingFlowId;
        }
        else if (operation.TrackingFlowId != trackingStep.TrackingFlowId)
        {
            return (false, "El step indicado no pertenece al flow de tracking de la operacion.");
        }

        var effectiveArrivedAt = arrivedAt ?? DateTime.Now;

        operation.CurrentTrackingFlowStepId = trackingStep.Id;
        operation.CurrentTrackingStepArrivedAt = effectiveArrivedAt;

        _context.OperationTrackingHistories.Add(new OperationTrackingHistory
        {
            OperationId = operation.Id,
            TrackingFlowStepId = trackingStep.Id,
            ArrivedAt = effectiveArrivedAt,
            Observations = string.IsNullOrWhiteSpace(observations) ? null : observations.Trim(),
            UserId = userId,
            CreatedAt = DateTime.Now
        });

        await _context.SaveChangesAsync(cancellationToken);
        return (true, null);
    }
}
