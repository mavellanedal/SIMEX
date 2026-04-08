using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class Airport
{
    public int Id { get; set; }

    public string Code { get; set; } = null!;

    public string? Name { get; set; }

    public int CityId { get; set; }

    public virtual City City { get; set; } = null!;

    public virtual ICollection<Offer> OfferDestinationAirports { get; set; } = new List<Offer>();

    public virtual ICollection<Offer> OfferOriginAirports { get; set; } = new List<Offer>();
}
