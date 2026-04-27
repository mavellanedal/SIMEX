<?php

namespace App\DTOs;

use App\Models\Company;

readonly class CompanyDTO
{
    public function __construct(
        public int $id,
        public ?string $name,
        public ?CompanyTypeDTO $companyType,
        public ?string $iconPath,
        public ?string $email,
        public ?string $phoneNumber,
        public ?string $nif,
        public ?RegionDTO $region,
        public ?CityDTO $city,
        public ?string $address,
        public ?bool $active
    ) {}

    public static function fromModel(?Company $company): ?self
    {
        if (! $company) {
            return null;
        }

        return new self(
            id: $company->ID ?? 0,
            name: $company->NAME ?? null,
            companyType: CompanyTypeDTO::fromModel($company->companyType),
            iconPath: $company->ICON_PATH ?? null,
            email: $company->EMAIL ?? null,
            phoneNumber: $company->PHONE_NUMBER ?? null,
            nif: $company->NIF ?? null,
            region: RegionDTO::fromModel($company->region),
            city: CityDTO::fromModel($company->city),
            address: $company->ADDRESS ?? null,
            active: (bool) $company->ACTIVE
        );
    }
}
