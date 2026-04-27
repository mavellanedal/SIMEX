<?php

namespace App\DTOs;

use App\Models\Country;

readonly class CountryDTO
{
    public function __construct(
        public int $id,
        public ?string $name
    ) {}

    public static function fromModel(?Country $country): ?self
    {
        if (! $country) {
            return null;
        }

        return new self(
            id: $country->ID ?? 0,
            name: $country->NAME ?? null
        );
    }
}
