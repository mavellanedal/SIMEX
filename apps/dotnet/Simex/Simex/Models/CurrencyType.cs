using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class CurrencyType
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string Symbol { get; set; } = null!;

    public virtual ICollection<Cost> Costs { get; set; } = new List<Cost>();
}
