<?php

namespace App\DTOs;
use App\DTOs\CountryDTO;

readonly class RegionDTO {
  public function __construct(
    public int $id,
    public string $name,
    public CountryDTO $country
  )
  {}
}
