<?php

use App\Models\User;
use App\DTOs\RoleDTO;
use App\DTOs\CompanyDTO;

readonly class UserDTO {
  public function __construct(
    public int $id,
    public string $email,
    public string $firstName,
    public string $lastName,
    public string $username,
    public bool $active,
    public ?string $identificationCardPath = null,
    public RoleDTO $role,
    public CompanyDTO $company,
  )
  {}
}
