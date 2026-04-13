<?php

namespace App\DTOs;

readonly class RoleDTO {
  public function __construct(
    public int $id,
    public string $name,
    public ?string $description = null,
  )
  {}
}
