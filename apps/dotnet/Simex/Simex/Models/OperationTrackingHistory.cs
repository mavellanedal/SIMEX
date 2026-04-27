using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class OperationTrackingHistory
{
    public int Id { get; set; }

    public int OperationId { get; set; }

    public int TrackingFlowStepId { get; set; }

    public DateTime ArrivedAt { get; set; }

    public string? Observations { get; set; }

    public int? UserId { get; set; }

    public DateTime CreatedAt { get; set; }

    public virtual Operation Operation { get; set; } = null!;

    public virtual TrackingFlowStep TrackingFlowStep { get; set; } = null!;

    public virtual User? User { get; set; }
}
