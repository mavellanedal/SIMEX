<?php

namespace App\Http\Controllers;

use App\DTOs\createUserDTO;
use App\DTOs\updateUserDTO;
use App\Http\Resources\UserResource;
use Illuminate\Http\Request;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

class UserController extends Controller
{
  public function index()
  {
    $user = User::with(['role', 'company'])->get();
    return response()->json($user);
  }

  public function store(Request $request)
  {
    $request->validate([
      'first_name' => 'required|string',
      'last_name' => 'required|string',
      'email' => 'required|email|unique:USERS,EMAIL',
      'username' => 'required|string|unique:USERS,USERNAME',
      'password' => 'required|string|min:8',
      'role_id' => 'required|integer|exists:ROLES,ID',
      'company_id' => 'nullable|integer|exists:COMPANY,ID',
      'active' => 'required|boolean',
    ]);

    $dto = new CreateUserDTO(
      $request->first_name,
      $request->last_name,
      $request->email,
      $request->username,
      $request->password,
      $request->role_id,
      $request->company_id,
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
      'user' => new UserResource($user)
    ], 201);
  }

  public function update(Request $request, $id)
  {
    $user = User::findOrFail($id); // si no lo encuentra, lanza un error 404

    $request->validate([
      'first_name' => 'required|string',
      'last_name' => 'required|string',
      'email' => 'required|email|unique:USERS,EMAIL,' . $id . ',ID',
      'username' => 'required|string|unique:USERS,USERNAME,' . $id . ',ID',
      'password' => 'nullable|string|min:8',
      'role_id' => 'required|integer|exists:ROLES,ID',
      'company_id' => 'nullable|integer|exists:COMPANY,ID',
      'active' => 'required|boolean',
    ]);

    $dto = new updateUserDTO(
      $request->first_name,
      $request->last_name,
      $request->email,
      $request->username,
      $request->role_id,
      $request->company_id,
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
      'user' => new UserResource($user)
    ]);
  }
}
