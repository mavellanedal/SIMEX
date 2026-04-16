using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class TicketState
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string? Description { get; set; }

    public virtual ICollection<TicketStateHistory> TicketStateHistories { get; set; } = new List<TicketStateHistory>();
}
