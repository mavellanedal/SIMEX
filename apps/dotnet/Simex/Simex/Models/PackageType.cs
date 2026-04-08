using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class PackageType
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string? Description { get; set; }

    public int? ParentId { get; set; }

    public virtual ICollection<PackageType> InverseParent { get; set; } = new List<PackageType>();

    public virtual ICollection<Operation> OperationPackageSubTypes { get; set; } = new List<Operation>();

    public virtual ICollection<Operation> OperationPackageTypes { get; set; } = new List<Operation>();

    public virtual PackageType? Parent { get; set; }
}
