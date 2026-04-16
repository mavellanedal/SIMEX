<?php

namespace App\DTOs;
use App\Models\CompanyType;

readonly class CompanyTypeDTO {
  public function __construct(
    public int $id,
    public string $name,
    public ?string $description = null
  )
  {}

  public static function fromModel(?CompanyType $companyType): ?self
  {
    if (!$companyType) {
      return null;
    }
    return new self(
      id: $companyType->ID ?? 0,
      name: $companyType->NAME ?? null,
      description: $companyType->DESCRIPTION ?? null
    );
  }
}
