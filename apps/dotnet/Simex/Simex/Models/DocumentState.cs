using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class DocumentState
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public string? Description { get; set; }

    public virtual ICollection<DocumentStateHistory> DocumentStateHistories { get; set; } = new List<DocumentStateHistory>();
}
