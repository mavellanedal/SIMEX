<?php

use App\Http\Controllers\AuthController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\UserController;
use App\Http\Controllers\CatalogController;
use App\Http\Controllers\CompanyController;
use App\Http\Controllers\NotificationController;
use App\Http\Controllers\OperationController;

Route::post('/login', [AuthController::class, 'login']);

Route::middleware('auth:sanctum')->group(function () {
  Route::post('/logout', [AuthController::class, 'logout']);

  Route::get('/users/getCustomsAgents', [UserController::class, 'getCustomsAgents']);
  Route::get('/users', [UserController::class, 'index']);
  Route::post('/user', [UserController::class, 'store']);
  Route::put('/user/{id}', [UserController::class, 'update']);
  Route::get('/user/{id}', [UserController::class, 'show']);
  Route::post('/users/bulk-delete', [UserController::class, 'bulkDelete']);

  Route::get('/users/customs-agents', [UserController::class, 'getCustomsAgents']);

  Route::get('/roles', [CatalogController::class, 'getRoles']);
  Route::get('/company-types', [CatalogController::class, 'getCompanyTypes']);
  Route::get('/regions', [CatalogController::class, 'getRegions']);
  Route::get('/cities', [CatalogController::class, 'getCities']);
  Route::get('/countries', [CatalogController::class, 'getCountries']);
  Route::get('/incoterms', [CatalogController::class, 'getIncoterms']);
  Route::get('/container-types', [CatalogController::class, 'getContainerTypes']);
  Route::get('/package-types', [CatalogController::class, 'getPackageTypes']);
  Route::get('/package-sub-types/{parentId}', [CatalogController::class, 'getPackageSubtypes']);
  Route::get('/ports', [CatalogController::class, 'getPorts']);
  Route::get('/currency-types', [CatalogController::class, 'getCurrecyTypes']);
  Route::get('/send-types', [CatalogController::class, 'getSendTypes']);

  Route::get('/companies', [CompanyController::class, 'index']);

  Route::get('/company/getImportersExportersCompanies', [CompanyController::class, 'getImportersExportersCompanies']);
  Route::get('/company/getNavieraCompanies', [CompanyController::class, 'getNavieraCompanies']);

  Route::get('/company/{company}', [CompanyController::class, 'show']);
  Route::post('/company', [CompanyController::class, 'store']);
  Route::put('/company/{company}', [CompanyController::class, 'update']);
  Route::delete('/company/{company}', [CompanyController::class, 'destroy']);

  Route::get('/notifications', [NotificationController::class, 'index']);
  Route::put('/notifications/{id}/read', [NotificationController::class, 'markAsRead']);
  Route::delete('/notifications/{id}', [NotificationController::class, 'destroy']);

  Route::get('/operations', [OperationController::class, 'index']);
  Route::post('/operations', [OperationController::class, 'store']);
  Route::get('/operations/{id}', [OperationController::class, 'show']);
  Route::put('/operations/{id}', [OperationController::class, 'update']);
  Route::put('/operations/{id}/state', [OperationController::class, 'changeStateOperation']);
});
