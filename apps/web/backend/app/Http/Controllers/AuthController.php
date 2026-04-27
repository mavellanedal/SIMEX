<?php

namespace App\Http\Controllers;

use App\DTOs\LoginDTO;
use App\DTOs\UserDTO;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;

class AuthController extends Controller
{
    public function login(Request $request)
    {
        $request->validate([
            'username' => 'required|string',
            'password' => 'required',
        ]);

        $dto = new LoginDTO($request->username, $request->password);
        $user = User::with('role', 'company')->where('USERNAME', $dto->username)->first();

        if (! $user || ! Hash::check($dto->password, $user->getAuthPassword())) {
            throw ValidationException::withMessages([
                'username' => ['Las credenciales proporcionadas son incorrectas.'],
            ]);
        }

        if (! $user->ACTIVE) {
            throw ValidationException::withMessages([
                'username' => ['Tu cuenta está desactivada. Contacta con un administrador.'],
            ]);
        }

        $token = $user->createToken('auth_token')->plainTextToken;

        return response()->json([
            'message' => 'Inicio de sesión exitoso',
            'access_token' => $token,
            'user' => UserDTO::fromModel($user),
        ]);
    }

    public function logout(Request $request)
    {
        $request->user()->currentAccessToken()->delete();

        return response()->json([
            'message' => 'Cierre de sesión exitoso',
        ]);
    }
}
