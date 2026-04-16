using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class CompanyType
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string? Description { get; set; }

    public virtual ICollection<Company> Companies { get; set; } = new List<Company>();
}
