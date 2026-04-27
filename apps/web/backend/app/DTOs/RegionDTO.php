<?php

namespace App\DTOs;

use App\Models\Region;

readonly class RegionDTO
{
    public function __construct(
        public int $id,
        public ?string $name,
        public ?CountryDTO $country
    ) {}

    public static function fromModel(?Region $region): ?self
    {
        if (! $region) {
            return null;
        }

        return new self(
            id: $region->ID ?? 0,
            name: $region->NAME ?? null,
            country: CountryDTO::fromModel($region->country)
        );
    }
}
