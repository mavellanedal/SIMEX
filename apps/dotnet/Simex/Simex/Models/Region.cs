using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class Region
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public int CountryId { get; set; }

    public virtual ICollection<City> Cities { get; set; } = new List<City>();

    public virtual ICollection<Company> Companies { get; set; } = new List<Company>();

    public virtual Country Country { get; set; } = null!;
}
