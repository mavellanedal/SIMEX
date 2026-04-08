using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class UserNotificationConfig
{
    public int Id { get; set; }

    public int? UserId { get; set; }

    public bool Push { get; set; }

    public bool Email { get; set; }

    public bool Sms { get; set; }

    public virtual User? User { get; set; }
}
