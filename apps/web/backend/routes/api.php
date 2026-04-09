<?php

use App\Http\Controllers\AuthController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;
use App\Http\Controllers\CatalogController;
use App\Http\Controllers\CompanyController;

Route::post('/login', [AuthController::class, 'login']);

Route::middleware('auth:sanctum')->group(function () {
  Route::post('/logout', [AuthController::class, 'logout']);
  Route::get('/users', [UserController::class, 'index']);
  Route::post('/user', [UserController::class, 'store']);
  Route::put('/user/{id}', [UserController::class, 'update']);

  Route::get('/roles', [CatalogController::class, 'getRoles']);


  Route::get('/companies', [CompanyController::class, 'index']); 
  Route::get('/company/{company}', [CompanyController::class, 'show']);
  Route::post('/company', [CompanyController::class, 'store']); 
  Route::put('/company/{company}', [CompanyController::class, 'update']); 
  Route::delete('/company/{company}', [CompanyController::class, 'destroy']); 
});
