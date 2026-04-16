<?php

namespace App\DTOs;
use App\Models\OperationStateHistory;

readonly class OperationStateHistoryDTO {
  public function __construct(
    public int $id,
    public \Carbon\Carbon|string|null $date,
    public ?OperationStateDTO $operationState,
    public ?OperationDTO $operation
  )
  {}

  public static function fromModel(?OperationStateHistory $operationStateHistory): ?self {
    if (!$operationStateHistory) {
      return null;
    }

    return new self (
      id: $operationStateHistory->ID ?? 0,
      date: $operationStateHistory->DATE ?? null,
      operationState: OperationStateDTO::fromModel($operationStateHistory->operationState),
      operation: OperationDTO::fromModel($operationStateHistory->operation)
    );
  }
}
