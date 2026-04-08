using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class Cost
{
    public int Id { get; set; }

    public int OperationId { get; set; }

    public int CostTypeId { get; set; }

    public int CurrencyTypeId { get; set; }

    public int SenedTypeId { get; set; }

    public decimal Cost1 { get; set; }

    public int CostAmount { get; set; }

    public decimal Sale { get; set; }

    public int SaleAmount { get; set; }

    public virtual CostType CostType { get; set; } = null!;

    public virtual CurrencyType CurrencyType { get; set; } = null!;

    public virtual Operation Operation { get; set; } = null!;

    public virtual SendType SenedType { get; set; } = null!;
}
