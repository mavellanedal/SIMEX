<?php

namespace App\DTOs;

readonly class CreateCompanyDTO
{
    public function __construct(
        public string $NAME,
        public string $EMAIL,
        public string $NIF,
        public int $COMPANY_TYPE_ID,
        public int $REGION_ID,
        public int $CITY_ID,
        public string $ADDRESS,
        public bool $ACTIVE,
        public ?string $PHONE_NUMBER = null,
        public ?string $ICON_PATH = null
    ) {}
}