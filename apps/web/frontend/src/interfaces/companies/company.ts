import type { CompanyType } from '@interfaces/companies/companyType';
import type { Region } from '@interfaces/location/region';
import type { City } from '@interfaces/location/city';

export interface Company {
  id: number;
  name: string | null;
  companyType: CompanyType | null;
  iconPath: string | null;
  email: string | null;
  phoneNumber: string | null;
  nif: string | null;
  region: Region | null;
  city: City | null;
  address: string | null;
  active: boolean | null;
}
