export interface CreateUser {
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  password?: string;
  roleId: number;
  companyId: number | null | string;
  active: number;
}
