<?php

namespace App\DTOs;

use App\Models\CurrencyTypes;

readonly class CurrencyTypeDTO
{
    public function __construct(
        public int $id,
        public string $name,
        public string $symbol
    ) {}

    public static function fromModel(?CurrencyTypes $currencyType): ?self
    {
        if (! $currencyType) {
            return null;
        }

        return new self(
            id: $currencyType->ID ?? 0,
            name: $currencyType->NAME ?? null,
            symbol: $currencyType->SYMBOL ?? null
        );
    }
}
