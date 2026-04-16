import type { User } from '@interfaces/user/user';
import type { Operation } from '@interfaces/operation/operation';
import type { NotificationState } from './notificationState';

export interface Notification {
  id: number;
  name: string;
  description: string | null;
  user: User;
  operation: Operation;
  state: NotificationState;
  logicRemove: boolean;
}
