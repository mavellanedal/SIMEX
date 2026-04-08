using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class TrackingStep
{
    public int Id { get; set; }

    public int? OrderNum { get; set; }

    public string? Name { get; set; }

    public virtual ICollection<Incoterm> Incoterms { get; set; } = new List<Incoterm>();
}
