using System;
using System.Collections.Generic;

namespace Simex.Models;

public partial class Operation
{
    public int Id { get; set; }

    public int CreateUserId { get; set; }

    public string OrderReference { get; set; } = null!;

    public int? ImporterId { get; set; }

    public DateTime? PickupData { get; set; }

    public int? IncotermId { get; set; }

    public DateTime? Etd { get; set; }

    public int CustomsAgentId { get; set; }

    public DateTime? Eta { get; set; }

    public int ExportatorId { get; set; }

    public int OperationUserId { get; set; }

    public int SalesUserId { get; set; }

    public int DocumentUserId { get; set; }

    public string ContainerNumber { get; set; } = null!;

    public int? ContainerTypeId { get; set; }

    public string? HsCode { get; set; }

    public int PackagesNumber { get; set; }

    public int PackageTypeId { get; set; }

    public int? PackageSubTypeId { get; set; }

    public decimal? Volume { get; set; }

    public decimal NetWeight { get; set; }

    public decimal Kilograms { get; set; }

    public int? PiecesNumber { get; set; }

    public string MblNumber { get; set; } = null!;

    public int BuyerId { get; set; }

    public int SellerId { get; set; }

    public int NavieraId { get; set; }

    public string Cargo { get; set; } = null!;

    public int OriginPortId { get; set; }

    public int DestinationPortId { get; set; }

    public string? CargoDescription { get; set; }

    public decimal TotalCost { get; set; }

    public decimal TotalSale { get; set; }

    public decimal Profit { get; set; }

    public virtual Company Buyer { get; set; } = null!;

    public virtual ContainerType? ContainerType { get; set; }

    public virtual ICollection<Cost> Costs { get; set; } = new List<Cost>();

    public virtual User CreateUser { get; set; } = null!;

    public virtual User CustomsAgent { get; set; } = null!;

    public virtual Port DestinationPort { get; set; } = null!;

    public virtual User DocumentUser { get; set; } = null!;

    public virtual ICollection<Document> Documents { get; set; } = new List<Document>();

    public virtual Company Exportator { get; set; } = null!;

    public virtual Company? Importer { get; set; }

    public virtual Incoterm? Incoterm { get; set; }

    public virtual Company Naviera { get; set; } = null!;

    public virtual ICollection<Notification> Notifications { get; set; } = new List<Notification>();

    public virtual User OperationUser { get; set; } = null!;

    public virtual Port OriginPort { get; set; } = null!;

    public virtual PackageType? PackageSubType { get; set; }

    public virtual PackageType PackageType { get; set; } = null!;

    public virtual User SalesUser { get; set; } = null!;

    public virtual Company Seller { get; set; } = null!;

    public virtual ICollection<Ticket> Tickets { get; set; } = new List<Ticket>();
}
