<?php

namespace App\DTOs;
use App\Models\Company;
use App\DTOs\CompanyTypeDTO;
use App\DTOs\RegionDTO;
use App\DTOs\CityDTO;


readonly class CompanyDTO {
  public function __construct(
    public int $id,
    public string $name,
    public CompanyTypeDTO $companyType,
    public string $iconPath,
    public string $email,
    public string $phoneNumber,
    public string $nif,
    public RegionDTO $region,
    public CityDTO $city,
    public string $address,
    public bool $active
  )
  {}
}
