using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class TrackingFlow
{
    public int Id { get; set; }

    public string Code { get; set; } = null!;

    public string Name { get; set; } = null!;

    public int? TransportTypeId { get; set; }

    public bool Active { get; set; }

    public DateTime CreatedAt { get; set; }

    public DateTime UpdatedAt { get; set; }

    public virtual ICollection<Operation> Operations { get; set; } = new List<Operation>();

    public virtual TransportType? TransportType { get; set; }

    public virtual ICollection<TrackingFlowStep> TrackingFlowSteps { get; set; } = new List<TrackingFlowStep>();
}
