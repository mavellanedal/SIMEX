<?php
namespace App\DTOs;
use App\Models\ContainerTypes;

readonly class ContainerTypeDTO
{
    public function __construct(
        public readonly int $id,
        public readonly ?string $type,
    ) {
    }

    public static function fromModel(?ContainerTypes $containerType): ?self
    {
        if (!$containerType) {
            return null;
        }

        return new self(
            id: $containerType->ID ?? 0,
            type: $containerType->TYPE ?? null,
        );
    }
}
