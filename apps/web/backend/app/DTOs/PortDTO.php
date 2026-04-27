<?php

namespace App\DTOs;

use App\Models\Ports;

readonly class PortDTO
{
    public function __construct(
        public int $id,
        public ?string $name,
        public ?CityDTO $city
    ) {}

    public static function fromModel(?Ports $port): ?self
    {
        if (! $port) {
            return null;
        }

        return new self(
            id: $port->ID ?? 0,
            name: $port->NAME ?? null,
            city: CityDTO::fromModel($port->city)
        );
    }
}
