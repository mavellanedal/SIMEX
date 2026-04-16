using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class City
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public int CountryId { get; set; }

    public int? RegionId { get; set; }

    public decimal? Latitude { get; set; }

    public decimal? Longitude { get; set; }

    public int? Altitude { get; set; }

    public virtual ICollection<Airport> Airports { get; set; } = new List<Airport>();

    public virtual ICollection<Carrier> Carriers { get; set; } = new List<Carrier>();

    public virtual ICollection<Company> Companies { get; set; } = new List<Company>();

    public virtual Country Country { get; set; } = null!;

    public virtual ICollection<Port> Ports { get; set; } = new List<Port>();

    public virtual Region? Region { get; set; }

    public virtual ICollection<ShippingLine> ShippingLines { get; set; } = new List<ShippingLine>();
}
