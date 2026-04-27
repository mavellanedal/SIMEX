<?php

namespace App\DTOs;

use App\Models\OperationStates;

readonly class OperationStateDTO
{
    public function __construct(
        public int $id,
        public ?string $name,
        public ?string $description
    ) {}

    public static function fromModel(?OperationStates $state): ?self
    {
        if (! $state) {
            return null;
        }

        return new self(
            id: $state->ID ?? 0,
            name: $state->NAME ?? null,
            description: $state->DESCRIPTION ?? null
        );
    }
}
