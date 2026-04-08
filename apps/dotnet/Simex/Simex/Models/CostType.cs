using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class CostType
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string? Description { get; set; }

    public virtual ICollection<Cost> Costs { get; set; } = new List<Cost>();
}
