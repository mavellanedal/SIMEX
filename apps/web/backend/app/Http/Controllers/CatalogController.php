<?php

namespace App\Http\Controllers;

use App\Models\Role;
use App\Models\Company;
use App\Models\Country;
use App\Models\Region;
use App\Models\IncotermTypes;
use App\Models\ContainerTypes;
use App\Models\PackageTypes;
use App\Models\Ports;
use App\Models\SendTypes;

use App\DTOs\RoleDTO;
use App\DTOs\CompanyDTO;
use App\DTOs\CompanyTypeDTO;
use App\DTOs\RegionDTO;
use App\DTOs\CityDTO;
use App\DTOs\CountryDTO;
use App\DTOs\IncotermTypeDTO;
use App\DTOs\ContainerTypeDTO;
use App\DTOs\CurrencyTypeDTO;
use App\DTOs\PackageTypeDTO;
use App\DTOs\PortDTO;
use App\Models\CurrencyTypes;
use \App\DTOs\SendTypesDTO;
use App\Models\City;
use App\Models\CompanyType;
use Illuminate\Http\Request;

class CatalogController extends Controller
{
  public function getRoles()
  {
    return response()->json(Role::all()->map(fn($item) => RoleDTO::fromModel($item)));
  }

  public function getCompanies()
  {
    return response()->json(
      Company::with(['companyType', 'region.country', 'city'])
        ->where('ACTIVE', 1)
        ->get()
        ->map(fn($item) => CompanyDTO::fromModel($item))
    );
  }

  public function getIncoterms()
  {
    return response()->json(IncotermTypes::all()->map(fn($item) => IncotermTypeDTO::fromModel($item)));
  }

  public function getContainerTypes()
  {
    return response()->json(ContainerTypes::all()->map(fn($item) => ContainerTypeDTO::fromModel($item)));
  }

  public function getPackageTypes()
  {
    return response()->json(PackageTypes::where('PARENT_ID', null)->get()->map(fn($item) => PackageTypeDTO::fromModel($item)));
  }

  public function getPackageSubtypes($parentId)
  {
    return response()->json(PackageTypes::where('PARENT_ID', $parentId)->get()->map(fn($item) => PackageTypeDTO::fromModel($item)));
  }

  public function getPorts()
  {
    return response()->json(Ports::with('city')->get()->map(fn($item) => PortDTO::fromModel($item)));
  }

  public function getCurrecyTypes()
  {
    return response()->json(CurrencyTypes::all()->map(fn($itme) => CurrencyTypeDTO::fromModel($itme)));
  }

  public function getSendTypes()
  {
    return response()->json(SendTypes::all()->map(fn($item) => SendTypesDTO::fromModel($item)));
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
