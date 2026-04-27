<?php

namespace App\Http\Controllers;

use App\DTOs\createUserDTO;
use App\DTOs\updateUserDTO;
use App\DTOs\UserDTO;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class UserController extends Controller
{
    public function index()
    {
        $users = User::with(['role', 'company'])->get();

        return response()->json(
            $users->map(fn ($item) => UserDTO::fromModel($item))
        );
    }

    public function store(Request $request)
    {
        $request->validate([
            'firstName' => 'required|string',
            'lastName' => 'required|string',
            'email' => 'required|email|unique:USERS,EMAIL',
            'username' => 'required|string|unique:USERS,USERNAME',
            'password' => 'required|string|min:8',
            'roleId' => 'required|integer|exists:ROLES,ID',
            'companyId' => 'nullable|integer|exists:COMPANY,ID',
            'active' => 'required|boolean',
        ]);

        $dto = new createUserDTO(
            $request->firstName,
            $request->lastName,
            $request->email,
            $request->username,
            $request->password,
            $request->roleId,
            $request->companyId,
            $request->active
        );

        $user = User::create([
            'FIRST_NAME' => $dto->firstName,
            'LAST_NAME' => $dto->lastName,
            'EMAIL' => $dto->email,
            'USERNAME' => $dto->username,
            'PASSWORD' => Hash::make($dto->password),
            'ROLE_ID' => $dto->roleId,
            'COMPANY_ID' => $dto->companyId,
            'ACTIVE' => $dto->active,
        ]);

        $user->load(['role', 'company']);

        return response()->json([
            'message' => 'Usuario creado correctamente',
            'user' => UserDTO::fromModel($user),
        ], 201);
    }

    public function update(Request $request, $id)
    {
        $user = User::findOrFail($id);

        $request->validate([
            'firstName' => 'required|string',
            'lastName' => 'required|string',
            'email' => 'required|email|unique:USERS,EMAIL,'.$id.',ID',
            'username' => 'required|string|unique:USERS,USERNAME,'.$id.',ID',
            'password' => 'nullable|string|min:8',
            'roleId' => 'required|integer|exists:ROLES,ID',
            'companyId' => 'nullable|integer|exists:COMPANY,ID',
            'active' => 'required|boolean',
        ]);

        $dto = new updateUserDTO(
            $request->firstName,
            $request->lastName,
            $request->email,
            $request->username,
            $request->roleId,
            $request->companyId,
            $request->active,
            $request->password
        );

        $user->FIRST_NAME = $dto->firstName;
        $user->LAST_NAME = $dto->lastName;
        $user->EMAIL = $dto->email;
        $user->USERNAME = $dto->username;
        $user->ROLE_ID = $dto->roleId;
        $user->COMPANY_ID = $dto->companyId;
        $user->ACTIVE = $dto->active;
        if ($dto->password) {
            $user->PASSWORD = Hash::make($dto->password);
        }
        $user->save();
        $user->load(['role', 'company']);

        return response()->json([
            'message' => 'Usuario actualizado correctamente',
            'user' => UserDTO::fromModel($user),
        ]);
    }

    public function bulkDelete(Request $request)
    {
        $request->validate([
            'ids' => 'required|array|min:1',
            'ids.*' => 'required|integer|exists:USERS,ID',
        ]);

        User::whereIn('ID', $request->ids)->update(['ACTIVE' => 0]);

        return response()->json([
            'message' => count($request->ids).' usuario(s) desactivado(s) correctamente.',
            'processed_ids' => $request->ids,
        ]);
    }

    public function show($id)
    {
        $user = User::with(['role', 'company'])->findOrFail($id);

        return response()->json(UserDTO::fromModel($user));
    }

    public function getCustomsAgents()
    {
        $customsAgents = User::with(['role', 'company'])
            ->whereHas('role', function ($query) {
                $query->where('ID', '5');
            })->get();

        return response()->json(
            $customsAgents->map(fn ($item) => UserDTO::fromModel($item))
        );
    }
}
