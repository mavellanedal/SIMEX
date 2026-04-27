<?php

namespace App\DTOs;

readonly class CreateUserDTO
{
    public function __construct(
        public string $firstName,
        public string $lastName,
        public string $email,
        public string $username,
        public string $password,
        public int $roleId,
        public ?int $companyId = null,
        public bool $active = true
    ) {}
}
