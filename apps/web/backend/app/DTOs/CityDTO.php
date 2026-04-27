<?php

namespace App\DTOs;

use App\Models\City;

readonly class CityDTO
{
    public function __construct(
        public int $id,
        public ?string $name,
        public ?CountryDTO $country,
        public ?RegionDTO $region,
        public ?float $latitude,
        public ?float $longitude,
        public ?int $altitude
    ) {}

    public static function fromModel(?City $city): ?self
    {
        if (! $city) {
            return null;
        }

        return new self(
            id: $city->ID ?? 0,
            name: $city->NAME ?? null,
            country: CountryDTO::fromModel($city->country),
            region: RegionDTO::fromModel($city->region),
            latitude: $city->LATITUDE !== null ? (float) $city->LATITUDE : null,
            longitude: $city->LONGITUDE !== null ? (float) $city->LONGITUDE : null,
            altitude: $city->ALTITUDE !== null ? (int) $city->ALTITUDE : null
        );
    }
}
