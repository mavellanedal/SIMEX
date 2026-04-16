import type { Role } from '@interfaces/roles/role';
import type { Company } from '@interfaces/companies/company';

export interface User {
  id: number;
  email: string | null;
  firstName: string | null;
  lastName: string | null;
  username: string | null;
  active: boolean | null;
  identificationCardPath: string | null;
  role: Role | null;
  company: Company | null;
}
