using System.ComponentModel.DataAnnotations;

namespace Simex.Dtos;

public class UpdateOperationTrackingStepRequestDto
{
    [Required]
    public int TrackingFlowStepId { get; set; }

    public DateTime? ArrivedAt { get; set; }

    [StringLength(255)]
    public string? Observations { get; set; }
}
