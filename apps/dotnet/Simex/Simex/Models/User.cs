using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class User
{
    public int Id { get; set; }

    public string Email { get; set; } = null!;

    public string Password { get; set; } = null!;

    public string FirstName { get; set; } = null!;

    public string LastName { get; set; } = null!;

    public int RoleId { get; set; }

    public int? CompanyId { get; set; }

    public string? Username { get; set; }

    public bool? Active { get; set; }

    public string? IdentificationCardPath { get; set; }

    public virtual Company? Company { get; set; }

    public virtual ICollection<Document> Documents { get; set; } = new List<Document>();

    public virtual ICollection<Notification> Notifications { get; set; } = new List<Notification>();

    public virtual ICollection<Offer> Offers { get; set; } = new List<Offer>();

    public virtual ICollection<Operation> OperationCreateUsers { get; set; } = new List<Operation>();

    public virtual ICollection<Operation> OperationCustomsAgents { get; set; } = new List<Operation>();

    public virtual ICollection<Operation> OperationDocumentUsers { get; set; } = new List<Operation>();

    public virtual ICollection<Operation> OperationOperationUsers { get; set; } = new List<Operation>();

    public virtual ICollection<Operation> OperationSalesUsers { get; set; } = new List<Operation>();

    public virtual Role Role { get; set; } = null!;

    public virtual ICollection<TicketStateHistory> TicketStateHistories { get; set; } = new List<TicketStateHistory>();

    public virtual ICollection<Ticket> Tickets { get; set; } = new List<Ticket>();

    public virtual ICollection<UserNotificationConfig> UserNotificationConfigs { get; set; } = new List<UserNotificationConfig>();
}
