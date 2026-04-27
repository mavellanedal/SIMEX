<?php

namespace App\DTOs;

use App\Models\TrackingSteps;

readonly class TrackingStepDTO
{
    public function __construct(
        public int $id,
        public ?string $orderNum,
        public ?string $name,
    ) {}

    public static function fromModel(?TrackingSteps $trackingStep): ?self
    {
        if (! $trackingStep) {
            return null;
        }

        return new self(
            id: $trackingStep->ID ?? 0,
            orderNum: $trackingStep->ORDER_NUM ?? null,
            name: $trackingStep->NAME ?? null,
        );
    }
}
