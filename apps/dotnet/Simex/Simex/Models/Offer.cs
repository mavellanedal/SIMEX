using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class Offer
{
    public int Id { get; set; }

    public int TransportTypeId { get; set; }

    public int FlowTypeId { get; set; }

    public int CargoTypeId { get; set; }

    public int IncotermId { get; set; }

    public int ClientId { get; set; }

    public string? Comments { get; set; }

    public int? CommercialAgentId { get; set; }

    public int? CarrierId { get; set; }

    public decimal? GrossWeight { get; set; }

    public decimal? Volume { get; set; }

    public int ValidationTypeId { get; set; }

    public int? OriginPortId { get; set; }

    public int? DestinationPortId { get; set; }

    public int? OriginAirportId { get; set; }

    public int? DestinationAirportId { get; set; }

    public int? ShippingLineId { get; set; }

    public int OfferStatusId { get; set; }

    public int OperatorId { get; set; }

    public DateOnly CreationDate { get; set; }

    public DateOnly? InitialValidityDate { get; set; }

    public DateOnly? FinalValidityDate { get; set; }

    public string? RejectionReason { get; set; }

    public int? ContainerTypeId { get; set; }

    public virtual CargoType CargoType { get; set; } = null!;

    public virtual Carrier? Carrier { get; set; }

    public virtual ContainerType? ContainerType { get; set; }

    public virtual Airport? DestinationAirport { get; set; }

    public virtual Port? DestinationPort { get; set; }

    public virtual FlowType FlowType { get; set; } = null!;

    public virtual Incoterm Incoterm { get; set; } = null!;

    public virtual OfferStatus OfferStatus { get; set; } = null!;

    public virtual User Operator { get; set; } = null!;

    public virtual Airport? OriginAirport { get; set; }

    public virtual Port? OriginPort { get; set; }

    public virtual ShippingLine? ShippingLine { get; set; }

    public virtual TransportType TransportType { get; set; } = null!;

    public virtual ValidationType ValidationType { get; set; } = null!;
}
