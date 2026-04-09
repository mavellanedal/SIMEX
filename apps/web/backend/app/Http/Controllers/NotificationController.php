<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Notification;

class NotificationController extends Controller
{
  public function index(Request $request)
  {
    $user = $request->user();

    $notifications = Notification::with('state')
      ->where('USER_ID', $user->ID)
      // Agrupamos la condición para que sea: AND (LOGIC_REMOVE IS NULL OR LOGIC_REMOVE = 0)
      ->where(function ($query) {
          $query->whereNull('LOGIC_REMOVE')
                ->orWhere('LOGIC_REMOVE', 0);
      })
      ->orderBy('ID', 'DESC')
      ->get();

    return response()->json($notifications);
  }

  public function markAsRead($id, Request $request)
  {
      $notification = Notification::where('ID', $id)
        ->where('USER_ID', $request->user()->ID)
        ->firstOrFail();

      $notification->STATE_ID = 2;
      $notification->save();

      return response()->json(['message' => 'Notificación marcada como leída']);
  }

  public function destroy($id, Request $request)
  {
      $notification = Notification::where('ID', $id)
        ->where('USER_ID', $request->user()->ID)
        ->firstOrFail();

      $notification->LOGIC_REMOVE = 1;
      $notification->save();

      return response()->json(['message' => 'Notificación eliminada']);
  }
}
