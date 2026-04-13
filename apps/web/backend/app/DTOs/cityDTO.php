<?php

namespace App\DTOs;
use App\DTOs\CountryDTO;
use App\DTOs\RegionDTO;

readonly class CityDTO {
  public function __construct(
    public int $id,
    public string $name,
    public CountryDTO $country,
    public RegionDTO $region,
    public ?int $latitude,
    public ?int $longitude,
    public ?int $altitude
  )
  {}
}
