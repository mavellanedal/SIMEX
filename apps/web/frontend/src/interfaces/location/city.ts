import type { Country } from '@interfaces/location/country';
import type { Region } from '@interfaces/location/region';

export interface City {
  id: number;
  name: string;
  country: Country;
  region: Region;
  latitude: number | null;
  longitude: number | null;
  altitude: number | null;
}
