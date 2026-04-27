<?php

namespace App\Http\Controllers;

use App\DTOs\NotificationDTO;
use App\Models\Notification;
use Illuminate\Http\Request;

class NotificationController extends Controller
{
    public function index(Request $request)
    {
        $user = $request->user();

        $notifications = Notification::with(['state', 'user', 'operation'])
            ->where('USER_ID', $user->ID)
            ->where(function ($query) {
                $query->whereNull('LOGIC_REMOVE')
                    ->orWhere('LOGIC_REMOVE', 0);
            })
            ->orderBy('ID', 'DESC')
            ->get()
            ->map(fn ($notification) => NotificationDTO::fromModel($notification)); // Usamos el DTO

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
