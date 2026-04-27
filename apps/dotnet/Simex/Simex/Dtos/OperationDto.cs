namespace Simex.Dtos
{
    public class OperationDto
    {
        public int Id { get; set; }
        public string OrderReference { get; set; } = string.Empty;
        public string OriginPortName { get; set; } = string.Empty;
        public string DestinationPortName { get; set; } = string.Empty;
        public decimal TotalCost { get; set; }
        public DateTime? Etd { get; set; }
        public DateTime? Eta { get; set; }
        public string IncotermCode { get; set; } = string.Empty;
        public int? PiecesNumber { get; set; }
        public decimal Kilograms { get; set; }
        public string StatusName { get; set; } = string.Empty;
        public int? TrackingFlowId { get; set; }
        public string? TrackingFlowName { get; set; }
        public int? CurrentTrackingFlowStepId { get; set; }
        public string? CurrentTrackingStepName { get; set; }
        public int? CurrentTrackingStepOrder { get; set; }
        public int? CurrentTrackingStepUiPercent { get; set; }
        public DateTime? CurrentTrackingStepArrivedAt { get; set; }
    }
}
