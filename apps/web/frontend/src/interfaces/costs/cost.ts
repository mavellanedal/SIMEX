import type { SendType } from '@interfaces/costs/sendType';
import type { CurrencyType } from '@interfaces/costs/currencyType';
import type { CostType } from '@interfaces/costs/costType';

export interface Cost {
  id: number;
  costType: CostType;
  currencyType: CurrencyType;
  sendType: SendType;
  cost: number;
  costAmount: number;
  sale: number;
  saleAmount: number;
}
