export interface CreateUser {
  first_name: string;
  last_name: string;
  email: string;
  username: string;
  password?: string;
  role_id: number;
  company_id: number | null | string;
  active: number;
}
