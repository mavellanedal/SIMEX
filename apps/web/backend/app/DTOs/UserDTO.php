<?php

namespace App\DTOs;

use App\Models\User;

readonly class UserDTO
{
    public function __construct(
        public int $id,
        public ?string $email,
        public ?string $firstName,
        public ?string $lastName,
        public ?string $username,
        public ?bool $active,
        public ?string $identificationCardPath,
        public ?RoleDTO $role,
        public ?CompanyDTO $company
    ) {}

    public static function fromModel(?User $user): ?self
    {
        if (! $user) {
            return null;
        }

        return new self(
            id: $user->ID ?? ($user->id ?? 0),
            email: $user->EMAIL ?? ($user->email ?? null),
            firstName: $user->FIRST_NAME ?? ($user->first_name ?? null),
            lastName: $user->LAST_NAME ?? ($user->last_name ?? null),
            username: $user->USERNAME ?? ($user->username ?? null),
            active: (bool) ($user->ACTIVE ?? ($user->active ?? false)),
            identificationCardPath: $user->IDENTIFICATION_CARD_PATH ?? ($user->identification_card_path ?? null),
            role: RoleDTO::fromModel($user->role),
            company: CompanyDTO::fromModel($user->company)
        );
    }
}
