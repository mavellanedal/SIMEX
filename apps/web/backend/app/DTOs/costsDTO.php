<?php

namespace App\DTOs;
use App\Models\Costs;

readonly class CostDTO {
  public function __construct(
    public int $id,
    public ?CostTypeDTO $costType,
    public ?CurrencyTypeDTO $currencyType,
    public ?SendTypeDTO $sendType,
    public ?float $cost,
    public ?int $costAmount,
    public ?float $sale,
    public ?int $saleAmount
  )
  {}

  public static function fromModel(?Costs $cost): ?self {
    if (!$cost) {
      return null;
    }

    return new self (
      id: $cost->ID ?? 0,
      costType: CostTypeDTO::fromModel($cost->costType),
      currencyType: CurrencyTypeDTO::fromModel($cost->currencyType),
      sendType: SendTypeDTO::fromModel($cost->sendType),
      cost: $cost->COST !== null ? (float) $cost->COST : null,
      costAmount: $cost->COST_AMOUNT !== null ? (int) $cost->COST_AMOUNT : null,
      sale: $cost->SALE !== null ? (float) $cost->SALE : null,
      saleAmount: $cost->SALE_AMOUNT !== null ? (int) $cost->SALE_AMOUNT : null
    );
  }
}
