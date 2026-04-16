using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;

namespace Simex.Models;

public partial class Simex04Context : DbContext
{
    public Simex04Context()
    {
    }

    public Simex04Context(DbContextOptions<Simex04Context> options)
        : base(options)
    {
    }

    public virtual DbSet<Airport> Airports { get; set; }

    public virtual DbSet<Cache> Caches { get; set; }

    public virtual DbSet<CacheLock> CacheLocks { get; set; }

    public virtual DbSet<CargoType> CargoTypes { get; set; }

    public virtual DbSet<Carrier> Carriers { get; set; }

    public virtual DbSet<City> Cities { get; set; }

    public virtual DbSet<Company> Companies { get; set; }

    public virtual DbSet<CompanyType> CompanyTypes { get; set; }

    public virtual DbSet<ContainerType> ContainerTypes { get; set; }

    public virtual DbSet<Cost> Costs { get; set; }

    public virtual DbSet<CostType> CostTypes { get; set; }

    public virtual DbSet<Country> Countries { get; set; }

    public virtual DbSet<CurrencyType> CurrencyTypes { get; set; }

    public virtual DbSet<Document> Documents { get; set; }

    public virtual DbSet<DocumentState> DocumentStates { get; set; }

    public virtual DbSet<DocumentStateHistory> DocumentStateHistories { get; set; }

    public virtual DbSet<DocumentType> DocumentTypes { get; set; }

    public virtual DbSet<FailedJob> FailedJobs { get; set; }

    public virtual DbSet<FlowType> FlowTypes { get; set; }

    public virtual DbSet<Incoterm> Incoterms { get; set; }

    public virtual DbSet<IncotermType> IncotermTypes { get; set; }

    public virtual DbSet<Job> Jobs { get; set; }

    public virtual DbSet<JobBatch> JobBatches { get; set; }

    public virtual DbSet<Migration> Migrations { get; set; }

    public virtual DbSet<Notification> Notifications { get; set; }

    public virtual DbSet<NotificationState> NotificationStates { get; set; }

    public virtual DbSet<Offer> Offers { get; set; }

    public virtual DbSet<OfferStatus> OfferStatuses { get; set; }

    public virtual DbSet<Operation> Operations { get; set; }

    public virtual DbSet<OperationState> OperationStates { get; set; }

    public virtual DbSet<OperationStateHistory> OperationStateHistories { get; set; }

    public virtual DbSet<PackageType> PackageTypes { get; set; }

    public virtual DbSet<PasswordResetToken> PasswordResetTokens { get; set; }

    public virtual DbSet<PersonalAccessToken> PersonalAccessTokens { get; set; }

    public virtual DbSet<Port> Ports { get; set; }

    public virtual DbSet<Region> Regions { get; set; }

    public virtual DbSet<Role> Roles { get; set; }

    public virtual DbSet<SendType> SendTypes { get; set; }

    public virtual DbSet<Session> Sessions { get; set; }

    public virtual DbSet<ShippingLine> ShippingLines { get; set; }

    public virtual DbSet<Ticket> Tickets { get; set; }

    public virtual DbSet<TicketState> TicketStates { get; set; }

    public virtual DbSet<TicketStateHistory> TicketStateHistories { get; set; }

    public virtual DbSet<TicketType> TicketTypes { get; set; }

    public virtual DbSet<TrackingStep> TrackingSteps { get; set; }

    public virtual DbSet<TransportType> TransportTypes { get; set; }

    public virtual DbSet<User> Users { get; set; }

    public virtual DbSet<UserNotificationConfig> UserNotificationConfigs { get; set; }

    public virtual DbSet<ValidationType> ValidationTypes { get; set; }

    protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
    {
        if (!optionsBuilder.IsConfigured)
        {
            throw new InvalidOperationException("Simex04Context must be configured through dependency injection.");
        }
    }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Airport>(entity =>
        {
            entity.ToTable("AIRPORTS");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.CityId).HasColumnName("CITY_ID");
            entity.Property(e => e.Code)
                .HasMaxLength(5)
                .IsUnicode(false)
                .IsFixedLength()
                .HasColumnName("CODE");
            entity.Property(e => e.Name)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("NAME");

            entity.HasOne(d => d.City).WithMany(p => p.Airports)
                .HasForeignKey(d => d.CityId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_AIRPORTS_CITIES");
        });

        modelBuilder.Entity<Cache>(entity =>
        {
            entity.HasKey(e => e.Key).HasName("cache_key_primary");

            entity.ToTable("cache");

            entity.HasIndex(e => e.Expiration, "cache_expiration_index");

            entity.Property(e => e.Key)
                .HasMaxLength(255)
                .HasColumnName("key");
            entity.Property(e => e.Expiration).HasColumnName("expiration");
            entity.Property(e => e.Value).HasColumnName("value");
        });

        modelBuilder.Entity<CacheLock>(entity =>
        {
            entity.HasKey(e => e.Key).HasName("cache_locks_key_primary");

            entity.ToTable("cache_locks");

            entity.HasIndex(e => e.Expiration, "cache_locks_expiration_index");

            entity.Property(e => e.Key)
                .HasMaxLength(255)
                .HasColumnName("key");
            entity.Property(e => e.Expiration).HasColumnName("expiration");
            entity.Property(e => e.Owner)
                .HasMaxLength(255)
                .HasColumnName("owner");
        });

        modelBuilder.Entity<CargoType>(entity =>
        {
            entity.ToTable("CARGO_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Type)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("TYPE");
        });

        modelBuilder.Entity<Carrier>(entity =>
        {
            entity.ToTable("CARRIERS");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.CityId).HasColumnName("CITY_ID");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");

            entity.HasOne(d => d.City).WithMany(p => p.Carriers)
                .HasForeignKey(d => d.CityId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_CARRIERS_CITIES");
        });

        modelBuilder.Entity<City>(entity =>
        {
            entity.ToTable("CITIES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Altitude).HasColumnName("ALTITUDE");
            entity.Property(e => e.CountryId).HasColumnName("COUNTRY_ID");
            entity.Property(e => e.Latitude)
                .HasColumnType("decimal(8, 6)")
                .HasColumnName("LATITUDE");
            entity.Property(e => e.Longitude)
                .HasColumnType("decimal(9, 6)")
                .HasColumnName("LONGITUDE");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
            entity.Property(e => e.RegionId).HasColumnName("REGION_ID");

            entity.HasOne(d => d.Country).WithMany(p => p.Cities)
                .HasForeignKey(d => d.CountryId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_CITIES_COUNTRIES");

            entity.HasOne(d => d.Region).WithMany(p => p.Cities)
                .HasForeignKey(d => d.RegionId)
                .HasConstraintName("FK__CITIES__REGION_I__47A6A41B");
        });

        modelBuilder.Entity<Company>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__COMPANY__3214EC27D367D3B5");

            entity.ToTable("COMPANY");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Active).HasColumnName("ACTIVE");
            entity.Property(e => e.Address)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("ADDRESS");
            entity.Property(e => e.CityId).HasColumnName("CITY_ID");
            entity.Property(e => e.CompanyTypeId).HasColumnName("COMPANY_TYPE_ID");
            entity.Property(e => e.Email)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("EMAIL");
            entity.Property(e => e.IconPath)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("ICON_PATH");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("NAME");
            entity.Property(e => e.Nif)
                .HasMaxLength(25)
                .IsUnicode(false)
                .HasColumnName("NIF");
            entity.Property(e => e.PhoneNumber)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("PHONE_NUMBER");
            entity.Property(e => e.RegionId).HasColumnName("REGION_ID");

            entity.HasOne(d => d.City).WithMany(p => p.Companies)
                .HasForeignKey(d => d.CityId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__COMPANY__CITY_ID__269AB60B");

            entity.HasOne(d => d.CompanyType).WithMany(p => p.Companies)
                .HasForeignKey(d => d.CompanyTypeId)
                .HasConstraintName("FK__COMPANY__COMPANY__24B26D99");

            entity.HasOne(d => d.Region).WithMany(p => p.Companies)
                .HasForeignKey(d => d.RegionId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__COMPANY__REGION___25A691D2");
        });

        modelBuilder.Entity<CompanyType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__COMPANY___3214EC27D4D2E3F3");

            entity.ToTable("COMPANY_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<ContainerType>(entity =>
        {
            entity.ToTable("CONTAINER_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Type)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("TYPE");
        });

        modelBuilder.Entity<Cost>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__COSTS__3214EC27491E47FF");

            entity.ToTable("COSTS");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Cost1)
                .HasColumnType("decimal(19, 2)")
                .HasColumnName("COST");
            entity.Property(e => e.CostAmount).HasColumnName("COST_AMOUNT");
            entity.Property(e => e.CostTypeId).HasColumnName("COST_TYPE_ID");
            entity.Property(e => e.CurrencyTypeId).HasColumnName("CURRENCY_TYPE_ID");
            entity.Property(e => e.OperationId).HasColumnName("OPERATION_ID");
            entity.Property(e => e.Sale)
                .HasColumnType("decimal(19, 2)")
                .HasColumnName("SALE");
            entity.Property(e => e.SaleAmount).HasColumnName("SALE_AMOUNT");
            entity.Property(e => e.SenedTypeId).HasColumnName("SENED_TYPE_ID");

            entity.HasOne(d => d.CostType).WithMany(p => p.Costs)
                .HasForeignKey(d => d.CostTypeId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__COSTS__COST_TYPE__4707859D");

            entity.HasOne(d => d.CurrencyType).WithMany(p => p.Costs)
                .HasForeignKey(d => d.CurrencyTypeId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__COSTS__CURRENCY___47FBA9D6");

            entity.HasOne(d => d.Operation).WithMany(p => p.Costs)
                .HasForeignKey(d => d.OperationId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__COSTS__OPERATION__46136164");

            entity.HasOne(d => d.SenedType).WithMany(p => p.Costs)
                .HasForeignKey(d => d.SenedTypeId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__COSTS__SENED_TYP__48EFCE0F");
        });

        modelBuilder.Entity<CostType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__COST_TYP__3214EC275A2FFCB4");

            entity.ToTable("COST_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<Country>(entity =>
        {
            entity.ToTable("COUNTRIES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<CurrencyType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__CURRENCY__3214EC271473B3F0");

            entity.ToTable("CURRENCY_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
            entity.Property(e => e.Symbol)
                .HasMaxLength(5)
                .IsUnicode(false)
                .HasColumnName("SYMBOL");
        });

        modelBuilder.Entity<Document>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__DOCUMENT__3214EC273545A46B");

            entity.ToTable("DOCUMENTS");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.ClientId).HasColumnName("CLIENT_ID");
            entity.Property(e => e.DocumentTypeId).HasColumnName("DOCUMENT_TYPE_ID");
            entity.Property(e => e.OperationId).HasColumnName("OPERATION_ID");
            entity.Property(e => e.PathFile)
                .HasMaxLength(512)
                .IsUnicode(false)
                .HasColumnName("PATH_FILE");

            entity.HasOne(d => d.Client).WithMany(p => p.Documents)
                .HasForeignKey(d => d.ClientId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__DOCUMENTS__CLIEN__546180BB");

            entity.HasOne(d => d.DocumentType).WithMany(p => p.Documents)
                .HasForeignKey(d => d.DocumentTypeId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__DOCUMENTS__DOCUM__52793849");

            entity.HasOne(d => d.Operation).WithMany(p => p.Documents)
                .HasForeignKey(d => d.OperationId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__DOCUMENTS__OPERA__536D5C82");
        });

        modelBuilder.Entity<DocumentState>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__DOCUMENT__3214EC27DFED29DA");

            entity.ToTable("DOCUMENT_STATE");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<DocumentStateHistory>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__DOCUMENT__3214EC27542F165F");

            entity.ToTable("DOCUMENT_STATE_HISTORY");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Date)
                .HasColumnType("datetime")
                .HasColumnName("DATE");
            entity.Property(e => e.DocumentId).HasColumnName("DOCUMENT_ID");
            entity.Property(e => e.StateId).HasColumnName("STATE_ID");

            entity.HasOne(d => d.Document).WithMany(p => p.DocumentStateHistories)
                .HasForeignKey(d => d.DocumentId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__DOCUMENT___DOCUM__5832119F");

            entity.HasOne(d => d.State).WithMany(p => p.DocumentStateHistories)
                .HasForeignKey(d => d.StateId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__DOCUMENT___STATE__573DED66");
        });

        modelBuilder.Entity<DocumentType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__DOCUMENT__3214EC277AC65DDE");

            entity.ToTable("DOCUMENT_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<FailedJob>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__failed_j__3213E83FC7AA60EA");

            entity.ToTable("failed_jobs");

            entity.HasIndex(e => e.Uuid, "failed_jobs_uuid_unique").IsUnique();

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Connection).HasColumnName("connection");
            entity.Property(e => e.Exception).HasColumnName("exception");
            entity.Property(e => e.FailedAt)
                .HasDefaultValueSql("(getdate())")
                .HasColumnType("datetime")
                .HasColumnName("failed_at");
            entity.Property(e => e.Payload).HasColumnName("payload");
            entity.Property(e => e.Queue).HasColumnName("queue");
            entity.Property(e => e.Uuid)
                .HasMaxLength(255)
                .HasColumnName("uuid");
        });

        modelBuilder.Entity<FlowType>(entity =>
        {
            entity.ToTable("FLOW_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Type)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("TYPE");
        });

        modelBuilder.Entity<Incoterm>(entity =>
        {
            entity.ToTable("INCOTERMS");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.IncotermTypeId).HasColumnName("INCOTERM_TYPE_ID");
            entity.Property(e => e.TrackingStepId).HasColumnName("TRACKING_STEP_ID");

            entity.HasOne(d => d.IncotermType).WithMany(p => p.Incoterms)
                .HasForeignKey(d => d.IncotermTypeId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_INCOTERMS_INCOTERM_TYPES");

            entity.HasOne(d => d.TrackingStep).WithMany(p => p.Incoterms)
                .HasForeignKey(d => d.TrackingStepId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_INCOTERMS_TRACKING_STEPS");
        });

        modelBuilder.Entity<IncotermType>(entity =>
        {
            entity.ToTable("INCOTERM_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Code)
                .HasMaxLength(5)
                .IsUnicode(false)
                .IsFixedLength()
                .HasColumnName("CODE");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<Job>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__jobs__3213E83F876C2A0F");

            entity.ToTable("jobs");

            entity.HasIndex(e => e.Queue, "jobs_queue_index");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Attempts).HasColumnName("attempts");
            entity.Property(e => e.AvailableAt).HasColumnName("available_at");
            entity.Property(e => e.CreatedAt).HasColumnName("created_at");
            entity.Property(e => e.Payload).HasColumnName("payload");
            entity.Property(e => e.Queue)
                .HasMaxLength(255)
                .HasColumnName("queue");
            entity.Property(e => e.ReservedAt).HasColumnName("reserved_at");
        });

        modelBuilder.Entity<JobBatch>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("job_batches_id_primary");

            entity.ToTable("job_batches");

            entity.Property(e => e.Id)
                .HasMaxLength(255)
                .HasColumnName("id");
            entity.Property(e => e.CancelledAt).HasColumnName("cancelled_at");
            entity.Property(e => e.CreatedAt).HasColumnName("created_at");
            entity.Property(e => e.FailedJobIds).HasColumnName("failed_job_ids");
            entity.Property(e => e.FailedJobs).HasColumnName("failed_jobs");
            entity.Property(e => e.FinishedAt).HasColumnName("finished_at");
            entity.Property(e => e.Name)
                .HasMaxLength(255)
                .HasColumnName("name");
            entity.Property(e => e.Options).HasColumnName("options");
            entity.Property(e => e.PendingJobs).HasColumnName("pending_jobs");
            entity.Property(e => e.TotalJobs).HasColumnName("total_jobs");
        });

        modelBuilder.Entity<Migration>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__migratio__3213E83F08FCDE2E");

            entity.ToTable("migrations");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Batch).HasColumnName("batch");
            entity.Property(e => e.Migration1)
                .HasMaxLength(255)
                .HasColumnName("migration");
        });

        modelBuilder.Entity<Notification>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__NOTIFICA__3214EC273BA522E2");

            entity.ToTable("NOTIFICATION");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.LogicRemove).HasColumnName("LOGIC_REMOVE");
            entity.Property(e => e.Name)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("NAME");
            entity.Property(e => e.OperationId).HasColumnName("OPERATION_ID");
            entity.Property(e => e.StateId).HasColumnName("STATE_ID");
            entity.Property(e => e.UserId).HasColumnName("USER_ID");

            entity.HasOne(d => d.Operation).WithMany(p => p.Notifications)
                .HasForeignKey(d => d.OperationId)
                .HasConstraintName("FK__NOTIFICAT__OPERA__6B44E613");

            entity.HasOne(d => d.State).WithMany(p => p.Notifications)
                .HasForeignKey(d => d.StateId)
                .HasConstraintName("FK__NOTIFICAT__STATE__031C6FA4");

            entity.HasOne(d => d.User).WithMany(p => p.Notifications)
                .HasForeignKey(d => d.UserId)
                .HasConstraintName("FK__NOTIFICAT__USER___6A50C1DA");
        });

        modelBuilder.Entity<NotificationState>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__NOTIFICA__3214EC27741FA2E8");

            entity.ToTable("NOTIFICATION_STATE");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<Offer>(entity =>
        {
            entity.ToTable("OFFERS");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.CargoTypeId).HasColumnName("CARGO_TYPE_ID");
            entity.Property(e => e.CarrierId).HasColumnName("CARRIER_ID");
            entity.Property(e => e.ClientId).HasColumnName("CLIENT_ID");
            entity.Property(e => e.Comments)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("COMMENTS");
            entity.Property(e => e.CommercialAgentId).HasColumnName("COMMERCIAL_AGENT_ID");
            entity.Property(e => e.ContainerTypeId).HasColumnName("CONTAINER_TYPE_ID");
            entity.Property(e => e.CreationDate).HasColumnName("CREATION_DATE");
            entity.Property(e => e.DestinationAirportId).HasColumnName("DESTINATION_AIRPORT_ID");
            entity.Property(e => e.DestinationPortId).HasColumnName("DESTINATION_PORT_ID");
            entity.Property(e => e.FinalValidityDate).HasColumnName("FINAL_VALIDITY_DATE");
            entity.Property(e => e.FlowTypeId).HasColumnName("FLOW_TYPE_ID");
            entity.Property(e => e.GrossWeight)
                .HasColumnType("numeric(18, 2)")
                .HasColumnName("GROSS_WEIGHT");
            entity.Property(e => e.IncotermId).HasColumnName("INCOTERM_ID");
            entity.Property(e => e.InitialValidityDate).HasColumnName("INITIAL_VALIDITY_DATE");
            entity.Property(e => e.OfferStatusId).HasColumnName("OFFER_STATUS_ID");
            entity.Property(e => e.OperatorId).HasColumnName("OPERATOR_ID");
            entity.Property(e => e.OriginAirportId).HasColumnName("ORIGIN_AIRPORT_ID");
            entity.Property(e => e.OriginPortId).HasColumnName("ORIGIN_PORT_ID");
            entity.Property(e => e.RejectionReason)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("REJECTION_REASON");
            entity.Property(e => e.ShippingLineId).HasColumnName("SHIPPING_LINE_ID");
            entity.Property(e => e.TransportTypeId).HasColumnName("TRANSPORT_TYPE_ID");
            entity.Property(e => e.ValidationTypeId).HasColumnName("VALIDATION_TYPE_ID");
            entity.Property(e => e.Volume)
                .HasColumnType("numeric(18, 2)")
                .HasColumnName("VOLUME");

            entity.HasOne(d => d.CargoType).WithMany(p => p.Offers)
                .HasForeignKey(d => d.CargoTypeId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_OFFERS_CARGO_TYPES");

            entity.HasOne(d => d.Carrier).WithMany(p => p.Offers)
                .HasForeignKey(d => d.CarrierId)
                .HasConstraintName("FK_OFFERS_CARRIERS");

            entity.HasOne(d => d.ContainerType).WithMany(p => p.Offers)
                .HasForeignKey(d => d.ContainerTypeId)
                .HasConstraintName("FK_OFFERS_CONTAINER_TYPES");

            entity.HasOne(d => d.DestinationAirport).WithMany(p => p.OfferDestinationAirports)
                .HasForeignKey(d => d.DestinationAirportId)
                .HasConstraintName("FK_OFFERS_DESTINATION_AIRPORTS");

            entity.HasOne(d => d.DestinationPort).WithMany(p => p.OfferDestinationPorts)
                .HasForeignKey(d => d.DestinationPortId)
                .HasConstraintName("FK_OFFERS_DESTINATION_PORTS");

            entity.HasOne(d => d.FlowType).WithMany(p => p.Offers)
                .HasForeignKey(d => d.FlowTypeId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_OFFERS_FLOW_TYPES");

            entity.HasOne(d => d.Incoterm).WithMany(p => p.Offers)
                .HasForeignKey(d => d.IncotermId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_OFFERS_INCOTERMS");

            entity.HasOne(d => d.OfferStatus).WithMany(p => p.Offers)
                .HasForeignKey(d => d.OfferStatusId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_OFFERS_OFFER_STATUSES");

            entity.HasOne(d => d.Operator).WithMany(p => p.Offers)
                .HasForeignKey(d => d.OperatorId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_OFFERS_USERS");

            entity.HasOne(d => d.OriginAirport).WithMany(p => p.OfferOriginAirports)
                .HasForeignKey(d => d.OriginAirportId)
                .HasConstraintName("FK_OFFERS_ORIGIN_AIRPORTS");

            entity.HasOne(d => d.OriginPort).WithMany(p => p.OfferOriginPorts)
                .HasForeignKey(d => d.OriginPortId)
                .HasConstraintName("FK_OFFERS_ORIGIN_PORTS");

            entity.HasOne(d => d.ShippingLine).WithMany(p => p.Offers)
                .HasForeignKey(d => d.ShippingLineId)
                .HasConstraintName("FK_OFFERS_SHIPPING_LINES");

            entity.HasOne(d => d.TransportType).WithMany(p => p.Offers)
                .HasForeignKey(d => d.TransportTypeId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_OFFERS_TRANSPORT_TYPES");

            entity.HasOne(d => d.ValidationType).WithMany(p => p.Offers)
                .HasForeignKey(d => d.ValidationTypeId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_OFFERS_VALIDATION_TYPES");
        });

        modelBuilder.Entity<OfferStatus>(entity =>
        {
            entity.ToTable("OFFER_STATUSES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Status)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("STATUS");
        });

        modelBuilder.Entity<Operation>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__OPERATIO__3214EC276710D3E2");

            entity.ToTable("OPERATIONS");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.BuyerId).HasColumnName("BUYER_ID");
            entity.Property(e => e.Cargo)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("CARGO");
            entity.Property(e => e.CargoDescription)
                .HasMaxLength(512)
                .IsUnicode(false)
                .HasColumnName("CARGO_DESCRIPTION");
            entity.Property(e => e.ContainerNumber)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("CONTAINER_NUMBER");
            entity.Property(e => e.ContainerTypeId).HasColumnName("CONTAINER_TYPE_ID");
            entity.Property(e => e.CreateUserId).HasColumnName("CREATE_USER_ID");
            entity.Property(e => e.CustomsAgentId).HasColumnName("CUSTOMS_AGENT_ID");
            entity.Property(e => e.DestinationPortId).HasColumnName("DESTINATION_PORT_ID");
            entity.Property(e => e.DocumentUserId).HasColumnName("DOCUMENT_USER_ID");
            entity.Property(e => e.Eta)
                .HasColumnType("datetime")
                .HasColumnName("ETA");
            entity.Property(e => e.Etd)
                .HasColumnType("datetime")
                .HasColumnName("ETD");
            entity.Property(e => e.ExportatorId).HasColumnName("EXPORTATOR_ID");
            entity.Property(e => e.HsCode)
                .HasMaxLength(10)
                .IsUnicode(false)
                .HasColumnName("HS_CODE");
            entity.Property(e => e.ImporterId).HasColumnName("IMPORTER_ID");
            entity.Property(e => e.IncotermId).HasColumnName("INCOTERM_ID");
            entity.Property(e => e.Kilograms)
                .HasColumnType("decimal(10, 2)")
                .HasColumnName("KILOGRAMS");
            entity.Property(e => e.MblNumber)
                .HasMaxLength(20)
                .IsUnicode(false)
                .HasColumnName("MBL_NUMBER");
            entity.Property(e => e.NavieraId).HasColumnName("NAVIERA_ID");
            entity.Property(e => e.NetWeight)
                .HasColumnType("decimal(19, 2)")
                .HasColumnName("NET_WEIGHT");
            entity.Property(e => e.OperationUserId).HasColumnName("OPERATION_USER_ID");
            entity.Property(e => e.OrderReference)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("ORDER_REFERENCE");
            entity.Property(e => e.OriginPortId).HasColumnName("ORIGIN_PORT_ID");
            entity.Property(e => e.PackageSubTypeId).HasColumnName("PACKAGE_SUB_TYPE_ID");
            entity.Property(e => e.PackageTypeId).HasColumnName("PACKAGE_TYPE_ID");
            entity.Property(e => e.PackagesNumber).HasColumnName("PACKAGES_NUMBER");
            entity.Property(e => e.PickupData)
                .HasColumnType("datetime")
                .HasColumnName("PICKUP_DATA");
            entity.Property(e => e.PiecesNumber).HasColumnName("PIECES_NUMBER");
            entity.Property(e => e.Profit)
                .HasColumnType("decimal(19, 2)")
                .HasColumnName("PROFIT");
            entity.Property(e => e.SalesUserId).HasColumnName("SALES_USER_ID");
            entity.Property(e => e.SellerId).HasColumnName("SELLER_ID");
            entity.Property(e => e.TotalCost)
                .HasColumnType("decimal(19, 2)")
                .HasColumnName("TOTAL_COST");
            entity.Property(e => e.TotalSale)
                .HasColumnType("decimal(19, 2)")
                .HasColumnName("TOTAL_SALE");
            entity.Property(e => e.Volume)
                .HasColumnType("decimal(10, 3)")
                .HasColumnName("VOLUME");

            entity.HasOne(d => d.Buyer).WithMany(p => p.OperationBuyers)
                .HasForeignKey(d => d.BuyerId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__BUYER__3B95D2F1");

            entity.HasOne(d => d.ContainerType).WithMany(p => p.Operations)
                .HasForeignKey(d => d.ContainerTypeId)
                .HasConstraintName("FK__OPERATION__CONTA__38B96646");

            entity.HasOne(d => d.CreateUser).WithMany(p => p.OperationCreateUsers)
                .HasForeignKey(d => d.CreateUserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__CREAT__3118447E");

            entity.HasOne(d => d.CustomsAgent).WithMany(p => p.OperationCustomsAgents)
                .HasForeignKey(d => d.CustomsAgentId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__CUSTO__33F4B129");

            entity.HasOne(d => d.DestinationPort).WithMany(p => p.OperationDestinationPorts)
                .HasForeignKey(d => d.DestinationPortId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__DESTI__3F6663D5");

            entity.HasOne(d => d.DocumentUser).WithMany(p => p.OperationDocumentUsers)
                .HasForeignKey(d => d.DocumentUserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__DOCUM__37C5420D");

            entity.HasOne(d => d.Exportator).WithMany(p => p.OperationExportators)
                .HasForeignKey(d => d.ExportatorId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__EXPOR__34E8D562");

            entity.HasOne(d => d.Importer).WithMany(p => p.OperationImporters)
                .HasForeignKey(d => d.ImporterId)
                .HasConstraintName("FK__OPERATION__IMPOR__320C68B7");

            entity.HasOne(d => d.Incoterm).WithMany(p => p.Operations)
                .HasForeignKey(d => d.IncotermId)
                .HasConstraintName("FK__OPERATION__INCOR__33008CF0");

            entity.HasOne(d => d.Naviera).WithMany(p => p.OperationNavieras)
                .HasForeignKey(d => d.NavieraId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__NAVIE__3D7E1B63");

            entity.HasOne(d => d.OperationUser).WithMany(p => p.OperationOperationUsers)
                .HasForeignKey(d => d.OperationUserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__OPERA__35DCF99B");

            entity.HasOne(d => d.OriginPort).WithMany(p => p.OperationOriginPorts)
                .HasForeignKey(d => d.OriginPortId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__ORIGI__3E723F9C");

            entity.HasOne(d => d.PackageSubType).WithMany(p => p.OperationPackageSubTypes)
                .HasForeignKey(d => d.PackageSubTypeId)
                .HasConstraintName("FK__OPERATION__PACKA__3AA1AEB8");

            entity.HasOne(d => d.PackageType).WithMany(p => p.OperationPackageTypes)
                .HasForeignKey(d => d.PackageTypeId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__PACKA__39AD8A7F");

            entity.HasOne(d => d.SalesUser).WithMany(p => p.OperationSalesUsers)
                .HasForeignKey(d => d.SalesUserId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__SALES__36D11DD4");

            entity.HasOne(d => d.Seller).WithMany(p => p.OperationSellers)
                .HasForeignKey(d => d.SellerId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__OPERATION__SELLE__3C89F72A");
        });

        modelBuilder.Entity<OperationState>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__OPERATIO__3214EC27C85F17C1");

            entity.ToTable("OPERATION_STATES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<OperationStateHistory>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__OPERATIO__3214EC27C81EFEF7");

            entity.ToTable("OPERATION_STATE_HISTORY");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Date)
                .HasColumnType("datetime")
                .HasColumnName("DATE");
            entity.Property(e => e.OperationStateId).HasColumnName("OPERATION_STATE_ID");

            entity.HasOne(d => d.OperationState).WithMany(p => p.OperationStateHistories)
                .HasForeignKey(d => d.OperationStateId)
                .HasConstraintName("FK__OPERATION__OPERA__4BCC3ABA");
        });

        modelBuilder.Entity<PackageType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__PACKAGE___3214EC274C1D8090");

            entity.ToTable("PACKAGE_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
            entity.Property(e => e.ParentId).HasColumnName("PARENT_ID");

            entity.HasOne(d => d.Parent).WithMany(p => p.InverseParent)
                .HasForeignKey(d => d.ParentId)
                .HasConstraintName("FK__PACKAGE_T__PAREN__2C538F61");
        });

        modelBuilder.Entity<PasswordResetToken>(entity =>
        {
            entity.HasKey(e => e.Email).HasName("password_reset_tokens_email_primary");

            entity.ToTable("password_reset_tokens");

            entity.Property(e => e.Email)
                .HasMaxLength(255)
                .HasColumnName("email");
            entity.Property(e => e.CreatedAt)
                .HasColumnType("datetime")
                .HasColumnName("created_at");
            entity.Property(e => e.Token)
                .HasMaxLength(255)
                .HasColumnName("token");
        });

        modelBuilder.Entity<PersonalAccessToken>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__personal__3213E83FE65C9E7C");

            entity.ToTable("personal_access_tokens");

            entity.HasIndex(e => e.ExpiresAt, "personal_access_tokens_expires_at_index");

            entity.HasIndex(e => e.Token, "personal_access_tokens_token_unique").IsUnique();

            entity.HasIndex(e => new { e.TokenableType, e.TokenableId }, "personal_access_tokens_tokenable_type_tokenable_id_index");

            entity.Property(e => e.Id).HasColumnName("id");
            entity.Property(e => e.Abilities).HasColumnName("abilities");
            entity.Property(e => e.CreatedAt)
                .HasColumnType("datetime")
                .HasColumnName("created_at");
            entity.Property(e => e.ExpiresAt)
                .HasColumnType("datetime")
                .HasColumnName("expires_at");
            entity.Property(e => e.LastUsedAt)
                .HasColumnType("datetime")
                .HasColumnName("last_used_at");
            entity.Property(e => e.Name).HasColumnName("name");
            entity.Property(e => e.Token)
                .HasMaxLength(64)
                .HasColumnName("token");
            entity.Property(e => e.TokenableId).HasColumnName("tokenable_id");
            entity.Property(e => e.TokenableType)
                .HasMaxLength(255)
                .HasColumnName("tokenable_type");
            entity.Property(e => e.UpdatedAt)
                .HasColumnType("datetime")
                .HasColumnName("updated_at");
        });

        modelBuilder.Entity<Port>(entity =>
        {
            entity.ToTable("PORTS");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.CityId).HasColumnName("CITY_ID");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");

            entity.HasOne(d => d.City).WithMany(p => p.Ports)
                .HasForeignKey(d => d.CityId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_PORTS_CITIES");
        });

        modelBuilder.Entity<Region>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__REGIONS__3214EC276BD1DC32");

            entity.ToTable("REGIONS");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.CountryId).HasColumnName("COUNTRY_ID");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("NAME");

            entity.HasOne(d => d.Country).WithMany(p => p.Regions)
                .HasForeignKey(d => d.CountryId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__REGIONS__COUNTRY__46B27FE2");
        });

        modelBuilder.Entity<Role>(entity =>
        {
            entity.ToTable("ROLES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<SendType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__SEND_TYP__3214EC27EEE19EB5");

            entity.ToTable("SEND_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(100)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<Session>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("sessions_id_primary");

            entity.ToTable("sessions");

            entity.HasIndex(e => e.LastActivity, "sessions_last_activity_index");

            entity.HasIndex(e => e.UserId, "sessions_user_id_index");

            entity.Property(e => e.Id)
                .HasMaxLength(255)
                .HasColumnName("id");
            entity.Property(e => e.IpAddress)
                .HasMaxLength(45)
                .HasColumnName("ip_address");
            entity.Property(e => e.LastActivity).HasColumnName("last_activity");
            entity.Property(e => e.Payload).HasColumnName("payload");
            entity.Property(e => e.UserAgent).HasColumnName("user_agent");
            entity.Property(e => e.UserId).HasColumnName("user_id");
        });

        modelBuilder.Entity<ShippingLine>(entity =>
        {
            entity.ToTable("SHIPPING_LINES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.CityId).HasColumnName("CITY_ID");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");

            entity.HasOne(d => d.City).WithMany(p => p.ShippingLines)
                .HasForeignKey(d => d.CityId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_SHIPPING_LINES_CITIES");
        });

        modelBuilder.Entity<Ticket>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__TICKET__3214EC27D98D0450");

            entity.ToTable("TICKET");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.CreateUserId).HasColumnName("CREATE_USER_ID");
            entity.Property(e => e.Description)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("NAME");
            entity.Property(e => e.OperationId).HasColumnName("OPERATION_ID");
            entity.Property(e => e.TicketTypeId).HasColumnName("TICKET_TYPE_ID");

            entity.HasOne(d => d.CreateUser).WithMany(p => p.Tickets)
                .HasForeignKey(d => d.CreateUserId)
                .HasConstraintName("FK__TICKET__CREATE_U__5FD33367");

            entity.HasOne(d => d.Operation).WithMany(p => p.Tickets)
                .HasForeignKey(d => d.OperationId)
                .HasConstraintName("FK__TICKET__OPERATIO__60C757A0");

            entity.HasOne(d => d.TicketType).WithMany(p => p.Tickets)
                .HasForeignKey(d => d.TicketTypeId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__TICKET__TICKET_T__5EDF0F2E");
        });

        modelBuilder.Entity<TicketState>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__TICKET_S__3214EC275577622E");

            entity.ToTable("TICKET_STATE");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<TicketStateHistory>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__TICKET_S__3214EC2794B5CD39");

            entity.ToTable("TICKET_STATE_HISTORY");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.AssignedUserId).HasColumnName("ASSIGNED_USER_ID");
            entity.Property(e => e.Date)
                .HasColumnType("datetime")
                .HasColumnName("DATE");
            entity.Property(e => e.TicketId).HasColumnName("TICKET_ID");
            entity.Property(e => e.TikcetStateId).HasColumnName("TIKCET_STATE_ID");

            entity.HasOne(d => d.AssignedUser).WithMany(p => p.TicketStateHistories)
                .HasForeignKey(d => d.AssignedUserId)
                .HasConstraintName("FK__TICKET_ST__ASSIG__658C0CBD");

            entity.HasOne(d => d.Ticket).WithMany(p => p.TicketStateHistories)
                .HasForeignKey(d => d.TicketId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__TICKET_ST__TICKE__63A3C44B");

            entity.HasOne(d => d.TikcetState).WithMany(p => p.TicketStateHistories)
                .HasForeignKey(d => d.TikcetStateId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__TICKET_ST__TIKCE__6497E884");
        });

        modelBuilder.Entity<TicketType>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__TICKET_T__3214EC274BB0A187");

            entity.ToTable("TICKET_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Description)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("DESCRIPTION");
            entity.Property(e => e.Name)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("NAME");
        });

        modelBuilder.Entity<TrackingStep>(entity =>
        {
            entity.ToTable("TRACKING_STEPS");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("NAME");
            entity.Property(e => e.OrderNum).HasColumnName("ORDER_NUM");
        });

        modelBuilder.Entity<TransportType>(entity =>
        {
            entity.ToTable("TRANSPORT_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Type)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("TYPE");
        });

        modelBuilder.Entity<User>(entity =>
        {
            entity.ToTable("USERS");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Active).HasColumnName("ACTIVE");
            entity.Property(e => e.CompanyId).HasColumnName("COMPANY_ID");
            entity.Property(e => e.Email)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("EMAIL");
            entity.Property(e => e.FirstName)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("FIRST_NAME");
            entity.Property(e => e.IdentificationCardPath)
                .HasMaxLength(255)
                .IsUnicode(false)
                .HasColumnName("IDENTIFICATION_CARD_PATH");
            entity.Property(e => e.LastName)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("LAST_NAME");
            entity.Property(e => e.Password)
                .HasMaxLength(256)
                .IsUnicode(false)
                .HasColumnName("PASSWORD");
            entity.Property(e => e.RoleId).HasColumnName("ROLE_ID");
            entity.Property(e => e.Username)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("USERNAME");

            entity.HasOne(d => d.Company).WithMany(p => p.Users)
                .HasForeignKey(d => d.CompanyId)
                .HasConstraintName("FK__USERS__COMPANY_I__278EDA44");

            entity.HasOne(d => d.Role).WithMany(p => p.Users)
                .HasForeignKey(d => d.RoleId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK_USERS_ROLES");
        });

        modelBuilder.Entity<UserNotificationConfig>(entity =>
        {
            entity.HasKey(e => e.Id).HasName("PK__USER_NOT__3214EC27B948A62C");

            entity.ToTable("USER_NOTIFICATION_CONFIG");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Email).HasColumnName("EMAIL");
            entity.Property(e => e.Push).HasColumnName("PUSH");
            entity.Property(e => e.Sms).HasColumnName("SMS");
            entity.Property(e => e.UserId).HasColumnName("USER_ID");

            entity.HasOne(d => d.User).WithMany(p => p.UserNotificationConfigs)
                .HasForeignKey(d => d.UserId)
                .HasConstraintName("FK__USER_NOTI__USER___6E2152BE");
        });

        modelBuilder.Entity<ValidationType>(entity =>
        {
            entity.ToTable("VALIDATION_TYPES");

            entity.Property(e => e.Id).HasColumnName("ID");
            entity.Property(e => e.Type)
                .HasMaxLength(50)
                .IsUnicode(false)
                .HasColumnName("TYPE");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
