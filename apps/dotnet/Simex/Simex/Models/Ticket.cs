using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class Ticket
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string? Description { get; set; }

    public int TicketTypeId { get; set; }

    public int? CreateUserId { get; set; }

    public int? OperationId { get; set; }

    public virtual User? CreateUser { get; set; }

    public virtual Operation? Operation { get; set; }

    public virtual ICollection<TicketStateHistory> TicketStateHistories { get; set; } = new List<TicketStateHistory>();

    public virtual TicketType TicketType { get; set; } = null!;
}
