using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class TicketStateHistory
{
    public int Id { get; set; }

    public DateTime Date { get; set; }

    public int TicketId { get; set; }

    public int TikcetStateId { get; set; }

    public int? AssignedUserId { get; set; }

    public virtual User? AssignedUser { get; set; }

    public virtual Ticket Ticket { get; set; } = null!;

    public virtual TicketState TikcetState { get; set; } = null!;
}
