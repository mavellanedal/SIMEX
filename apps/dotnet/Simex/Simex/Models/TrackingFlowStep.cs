using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class TrackingFlowStep
{
    public int Id { get; set; }

    public int TrackingFlowId { get; set; }

    public string StepKey { get; set; } = null!;

    public string Name { get; set; } = null!;

    public int OrderNum { get; set; }

    public int? UiPercent { get; set; }

    public bool Active { get; set; }

    public DateTime CreatedAt { get; set; }

    public DateTime UpdatedAt { get; set; }

    public virtual ICollection<OperationTrackingHistory> OperationTrackingHistories { get; set; } = new List<OperationTrackingHistory>();

    public virtual ICollection<Operation> Operations { get; set; } = new List<Operation>();

    public virtual TrackingFlow TrackingFlow { get; set; } = null!;
}
