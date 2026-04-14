import type { Role } from '@interfaces/roles/role';
import type { Company } from '@interfaces/companies/company';

export interface User {
  id: number;
  email: string;
  nombre: string;
  first_name: string;
  last_name: string;
  username: string;
  active: boolean | number;
  rol: Role;
  company: Company | null;
}
