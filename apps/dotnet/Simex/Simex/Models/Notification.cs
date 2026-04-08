using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class Notification
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string? Description { get; set; }

    public int? UserId { get; set; }

    public int? OperationId { get; set; }

    public int? StateId { get; set; }

    public bool? LogicRemove { get; set; }

    public virtual Operation? Operation { get; set; }

    public virtual NotificationState? State { get; set; }

    public virtual User? User { get; set; }
}
