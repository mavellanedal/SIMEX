<?php

namespace App\Http\Controllers;

use App\DTOs\createCompanyDTO;
use App\DTOs\updateCompanyDTO;
use App\DTOs\CompanyDTO;
use App\Models\Company;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;

class CompanyController extends Controller
{


    public function index()
    {
        // Añadimos region.country para que el DTO pueda anidar el país correctamente
        $companies = Company::with(['companyType', 'region.country', 'city'])->get();

        $data = $companies->map(function ($company) {
            return CompanyDTO::fromModel($company);
        });

        return response()->json($data);
    }

    public function store(Request $request)
    {
        $validatedData = $request->validate([
            'NAME' => 'required|string|max:255',
            'EMAIL' => 'required|email|unique:COMPANY,EMAIL',
            'NIF' => 'required|string|max:20|unique:COMPANY,NIF',
            'COMPANY_TYPE_ID' => 'required|integer|exists:COMPANY_TYPES,ID',
            'REGION_ID' => 'required|integer|exists:REGIONS,ID',
            'CITY_ID' => 'required|integer|exists:CITIES,ID',
            'ADDRESS' => 'required|string',
            'ACTIVE' => 'required|boolean',
            'PHONE_NUMBER' => 'nullable|string|max:20',
            'ICON_PATH' => 'nullable|string|max:255',
        ]);

        $company = Company::create($validatedData);
        $company->load(['companyType', 'region.country', 'city']);

        return response()->json(CompanyDTO::fromModel($company), 201);
    }

    public function show(Company $company)
    {
        $company->load(['companyType', 'region.country', 'city']);
        return response()->json(CompanyDTO::fromModel($company));
    }

    public function update(Request $request, Company $company)
    {
        $validatedData = $request->validate([
            'NAME' => 'sometimes|required|string|max:255',
            'EMAIL' => 'sometimes|required|email|unique:COMPANY,EMAIL,' . $company->ID . ',ID',
            'NIF' => 'sometimes|required|string|max:20|unique:COMPANY,NIF,' . $company->ID . ',ID',
            'COMPANY_TYPE_ID' => 'sometimes|required|integer|exists:COMPANY_TYPES,ID',
            'REGION_ID' => 'sometimes|required|integer|exists:REGIONS,ID',
            'CITY_ID' => 'sometimes|required|integer|exists:CITIES,ID',
            'ADDRESS' => 'sometimes|required|string',
            'ACTIVE' => 'sometimes|required|boolean',
            'PHONE_NUMBER' => 'nullable|string|max:20',
            'ICON_PATH' => 'nullable|string|max:255',
        ]);

        $company->update($validatedData);
        $company->load(['companyType', 'region.country', 'city']);

        return response()->json(CompanyDTO::fromModel($company));
    }


    public function destroy(Company $company)
    {
        $company->delete();

        return response()->json(null, 204);
    }

    public function getImportersExportersCompanies()
    {
        $importersExporters = Company::with(['companyType', 'region.country', 'city'])
          ->whereIn('COMPANY_TYPE_ID', [1, 2, 3])
          ->where('ACTIVE', true)
          ->get();

        $data = $importersExporters->map(function ($company) {
            return CompanyDTO::fromModel($company);
        });

        return response()->json($data);
    }

    public function getNavieraCompanies()
    {
        $navieras = Company::with(['companyType', 'region.country', 'city'])
          ->where('COMPANY_TYPE_ID', 4)
          ->where('ACTIVE', true)
          ->get();

        $data = $navieras->map(function ($company) {
            return CompanyDTO::fromModel($company);
        });

        return response()->json($data);
    }
}
