<?php

namespace App\DTOs;

readonly class UpdateUserDTO
{
    public function __construct(
        public string $firstName,
        public string $lastName,
        public string $email,
        public string $username,
        public int $roleId,
        public ?int $companyId,
        public bool $active,
        public ?string $password = null
    ) {}
}
