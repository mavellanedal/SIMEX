import type { City } from '@interfaces/location/city';

export interface Port {
  id: number;
  name: string | null;
  city: City | null;
}
