<?php

namespace App\DTOs;
use App\Models\Notification;

readonly class NotificationDTO {
  public function __construct(
    public int $id,
    public string $name,
    public ?string $description,
    public UserDTO $user,
    public OperationDTO $operation,
    public NotificationStateDTO $state,
    public bool $logicRemove,
  )
  {}

  public static function fromModel(?Notification $notification): ?self
  {
    if (!$notification) {
      return null;
    }

    return new self(
      id: $notification->ID ?? 0,
      name: $notification->NAME ?? null,
      description: $notification->DESCRIPTION ?? null,
      state: NotificationStateDTO::fromModel($notification->state),
      user: UserDTO::fromModel($notification->user),
      operation: OperationDTO::fromModel($notification->operation),
      logicRemove: $notification->LOGIC_REMOVE ?? false
    );
  }
}
