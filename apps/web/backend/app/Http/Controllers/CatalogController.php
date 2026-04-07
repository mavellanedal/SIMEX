<?php

namespace App\Http\Controllers;

use App\Models\Role;
use App\Models\Company;
use Illuminate\Http\Request;

class CatalogController extends Controller
{
  public function getRoles()
  {
    return response()->json(Role::all());
  }

  public function getCompanies()
  {
    return response()->json(Company::where('ACTIVE', 1)->get());
  }
}
