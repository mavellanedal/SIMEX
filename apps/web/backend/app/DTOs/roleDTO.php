<?php

namespace App\DTOs;
use App\Models\Role;

readonly class RoleDTO {
  public function __construct(
    public int $id,
    public ?string $name,
    public ?string $description
  )
  {}

  public static function fromModel(?Role $role): ?self
  {
    if (!$role) {
      return null;
    }

    return new self(
      id: $role->ID ?? 0,
      name: $role->NAME ?? null,
      description: $role->DESCRIPTION ?? null
    );
  }
}
