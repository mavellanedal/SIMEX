<?php

namespace App\DTOs;
use App\Models\PackageTypes;

readonly class PackageTypeDTO {
  public function __construct(
    public int $id,
    public ?string $name,
    public ?string $description = null,
    public ?int $parentId = null,
  )
  {}

  public static function fromModel(?PackageTypes $packageType): ?self
  {
    if (!$packageType) {
      return null;
    }

    return new self(
      id: $packageType->ID ?? 0,
      name: $packageType->NAME ?? null,
      description: $packageType->DESCRIPTION ?? null,
      parentId: $packageType->PARENT_ID ?? null
    );
  }
}
