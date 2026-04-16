using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class OperationStateHistory
{
    public int Id { get; set; }

    public DateTime Date { get; set; }

    public int? OperationStateId { get; set; }

    public virtual OperationState? OperationState { get; set; }
}
