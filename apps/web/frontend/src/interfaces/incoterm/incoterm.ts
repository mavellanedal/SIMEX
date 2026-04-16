import type { IncotermType } from '@interfaces/incoterm/incotermType';
import type { TrackingStep } from '@interfaces/incoterm/trackingStep';

export interface Incoterm {
  incotermType: IncotermType | null;
  trackingStep: TrackingStep | null;
}
