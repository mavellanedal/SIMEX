using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class Document
{
    public int Id { get; set; }

    public int DocumentTypeId { get; set; }

    public int OperationId { get; set; }

    public int ClientId { get; set; }

    public string? PathFile { get; set; }

    public virtual User Client { get; set; } = null!;

    public virtual ICollection<DocumentStateHistory> DocumentStateHistories { get; set; } = new List<DocumentStateHistory>();

    public virtual DocumentType DocumentType { get; set; } = null!;

    public virtual Operation Operation { get; set; } = null!;
}
