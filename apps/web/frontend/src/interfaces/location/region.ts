import type { Country } from '@interfaces/location/country';

export interface Region {
  id: number;
  name: string | null;
  country: Country | null;
}
