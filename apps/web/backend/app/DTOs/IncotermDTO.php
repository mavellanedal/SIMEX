<?php

namespace App\DTOs;

use App\Models\Incoterms;

readonly class IncotermDTO
{
    public function __construct(
        public ?IncotermTypeDTO $incotermType,
        public ?TrackingStepDTO $trackingStep,
    ) {}

    public static function fromModel(?Incoterms $incoterm): ?self
    {
        if (! $incoterm) {
            return null;
        }

        return new self(
            incotermType: IncotermTypeDTO::fromModel($incoterm->incotermType),
            trackingStep: TrackingStepDTO::fromModel($incoterm->trackingStep),
        );
    }
}
