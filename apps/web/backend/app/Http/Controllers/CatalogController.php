<?php

namespace App\Http\Controllers;

use App\Models\Role;
use App\Models\Company;
use App\Models\Region;
use App\Models\City;
use App\Models\CompanyType;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;
use App\Models\Country;



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

  public function getCompanyTypes()
    {
        return response()->json(CompanyType::all());
    }

  public function getRegions() 
  {
    return response()->json(Region::all());
  }

  public function getCities() 
  {
    return response()->json(City::all());
  }

  public function getCountries() 
  {
    return response()->json(Country::all());
  }
    

    
}