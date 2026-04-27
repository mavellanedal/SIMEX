<?php

namespace App\DTOs;

use App\Models\IncotermTypes;

readonly class IncotermTypeDTO
{
    public function __construct(
        public int $id,
        public ?string $code,
        public ?string $name,
    ) {}

    public static function fromModel(?IncotermTypes $incotermType): ?self
    {
        if (! $incotermType) {
            return null;
        }

        return new self(
            id: $incotermType->ID ?? 0,
            code: $incotermType->CODE ?? null,
            name: $incotermType->NAME ?? null,
        );
    }
}
