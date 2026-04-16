using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class Port
{
    public int Id { get; set; }

    public string Name { get; set; } = null!;

    public int CityId { get; set; }

    public virtual City City { get; set; } = null!;

    public virtual ICollection<Offer> OfferDestinationPorts { get; set; } = new List<Offer>();

    public virtual ICollection<Offer> OfferOriginPorts { get; set; } = new List<Offer>();

    public virtual ICollection<Operation> OperationDestinationPorts { get; set; } = new List<Operation>();

    public virtual ICollection<Operation> OperationOriginPorts { get; set; } = new List<Operation>();
}
