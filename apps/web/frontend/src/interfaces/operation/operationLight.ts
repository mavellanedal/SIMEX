export interface OperationLight {
  id: number;
  orderReference: string;
  importer: { name: string } | null;
  exporter: { name: string } | null;
  originPort: { name: string } | null;
  destinationPort: { name: string } | null;
  incoterm: {
    incotermType: { name: string } | null
  } | null;
  currentStateId: number | null;
}
