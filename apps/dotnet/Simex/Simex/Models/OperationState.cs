using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class OperationState
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string? Description { get; set; }

    public virtual ICollection<OperationStateHistory> OperationStateHistories { get; set; } = new List<OperationStateHistory>();
}
