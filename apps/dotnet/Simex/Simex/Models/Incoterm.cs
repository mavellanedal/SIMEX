using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class Incoterm
{
    public int Id { get; set; }

    public int IncotermTypeId { get; set; }

    public int TrackingStepId { get; set; }

    public virtual IncotermType IncotermType { get; set; } = null!;

    public virtual ICollection<Offer> Offers { get; set; } = new List<Offer>();

    public virtual ICollection<Operation> Operations { get; set; } = new List<Operation>();

    public virtual TrackingStep TrackingStep { get; set; } = null!;
}
