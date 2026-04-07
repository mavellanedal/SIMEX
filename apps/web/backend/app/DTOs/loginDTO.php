<?php
  namespace App\DTOs;

  readonly class LoginDTO
  {
      public function __construct(
          public readonly string $username,
          public readonly string $password,
      ) {
      }
  }
