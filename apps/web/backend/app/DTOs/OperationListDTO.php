<?php
namespace App\DTOs;

use App\Models\Operations;

readonly class OperationListDTO
{
    public function __construct(
        public int $id,
        public string $orderReference,
        public ?array $importer,
        public ?array $exporter,
        public ?array $originPort,
        public ?array $destinationPort,
        public ?array $incoterm,
        public ?int $currentStateId
    ) {}

    public static function fromModel(Operations $operation): self
    {
        $latestState = $operation->stateHistory ? $operation->stateHistory->sortByDesc('DATE')->first() : null;

        return new self(
            id: $operation->ID,
            orderReference: $operation->ORDER_REFERENCE ?? '',
            importer: $operation->importer ? ['name' => $operation->importer->NAME] : null,
            exporter: $operation->exportator ? ['name' => $operation->exportator->NAME] : null,
            originPort: $operation->originPort ? ['name' => $operation->originPort->NAME] : null,
            destinationPort: $operation->destinationPort ? ['name' => $operation->destinationPort->NAME] : null,
            incoterm: ($operation->incoterm && $operation->incoterm->incotermType)
                ? ['incotermType' => ['name' => $operation->incoterm->incotermType->NAME]]
                : null,
            currentStateId: $latestState ? $latestState->OPERATION_STATE_ID : 1
        );
    }
}
