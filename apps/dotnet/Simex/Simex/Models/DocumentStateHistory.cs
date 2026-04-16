using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class DocumentStateHistory
{
    public int Id { get; set; }

    public DateTime Date { get; set; }

    public int StateId { get; set; }

    public int DocumentId { get; set; }

    public virtual Document Document { get; set; } = null!;

    public virtual DocumentState State { get; set; } = null!;
}
