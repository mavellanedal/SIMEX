using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class Company
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public int? CompanyTypeId { get; set; }

    public string? IconPath { get; set; }

    public string? Email { get; set; }

    public string? PhoneNumber { get; set; }

    public string Nif { get; set; } = null!;

    public int RegionId { get; set; }

    public int CityId { get; set; }

    public string? Address { get; set; }

    public bool Active { get; set; }

    public virtual City City { get; set; } = null!;

    public virtual CompanyType? CompanyType { get; set; }

    public virtual ICollection<Operation> OperationBuyers { get; set; } = new List<Operation>();

    public virtual ICollection<Operation> OperationExportators { get; set; } = new List<Operation>();

    public virtual ICollection<Operation> OperationImporters { get; set; } = new List<Operation>();

    public virtual ICollection<Operation> OperationNavieras { get; set; } = new List<Operation>();

    public virtual ICollection<Operation> OperationSellers { get; set; } = new List<Operation>();

    public virtual Region Region { get; set; } = null!;

    public virtual ICollection<User> Users { get; set; } = new List<User>();
}
