<?php

namespace App\DTOs;

readonly class CountryDTO {
  public function __construct(
    public int $id,
    public string $name
  )
  {}
}
