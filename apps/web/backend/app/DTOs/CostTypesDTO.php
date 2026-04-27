<?php

namespace App\DTOs;

use App\Models\CostTypes;

readonly class CostTypeDTO
{
    public function __construct(
        public int $id,
        public ?string $name,
        public ?string $description
    ) {}

    public static function fromModel(?CostTypes $costType): ?self
    {
        if (! $costType) {
            return null;
        }

        return new self(
            id: $costType->ID ?? 0,
            name: $costType->NAME ?? null,
            description: $costType->DESCRIPTION ?? null
        );
    }
}
