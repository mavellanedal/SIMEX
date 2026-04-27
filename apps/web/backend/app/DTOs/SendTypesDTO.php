<?php

namespace App\DTOs;

use App\Models\SendTypes;

readonly class SendTypesDTO
{
    public function __construct(
        public int $id,
        public ?string $name,
        public ?string $description
    ) {}

    public static function fromModel(?SendTypes $sendType): ?self
    {
        if (! $sendType) {
            return null;
        }

        return new self(
            id: $sendType->ID ?? 0,
            name: $sendType->NAME ?? null,
            description: $sendType->DESCRIPTION ?? null
        );
    }
}
