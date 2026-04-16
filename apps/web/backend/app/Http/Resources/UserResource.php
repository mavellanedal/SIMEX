<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class UserResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
          'id' => $this->ID,
          'email' => $this->EMAIL,
          'nombre' => $this->FIRST_NAME . ' ' . $this->LAST_NAME,
          'first_name' => $this->FIRST_NAME,
          'last_name' => $this->LAST_NAME,
          'role' => [
            'id' => $this->role->ID,
            'name' => $this->role->NAME,
          ],
          'company' => $this->company ? [
            'id' => $this->company->ID,
            'name' => $this->company->NAME,
          ] : null,
          'username' => $this->USERNAME,
          'active' => $this->ACTIVE,
          'identification_card_path' => $this->IDENTIFICATION_CARD_PATH ? $this->IDENTIFICATION_CARD_PATH : null,
        ];
    }
}
