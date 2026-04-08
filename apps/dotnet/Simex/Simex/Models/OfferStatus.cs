using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class OfferStatus
{
    public int Id { get; set; }

    public string Status { get; set; } = null!;

    public virtual ICollection<Offer> Offers { get; set; } = new List<Offer>();
}
