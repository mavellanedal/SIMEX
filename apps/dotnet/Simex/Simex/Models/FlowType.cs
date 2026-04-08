using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class FlowType
{
    public int Id { get; set; }

    public string Type { get; set; } = null!;

    public virtual ICollection<Offer> Offers { get; set; } = new List<Offer>();
}
