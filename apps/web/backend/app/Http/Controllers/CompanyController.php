<?php

namespace App\Http\Controllers;

use App\DTOs\CreateCompanyDTO;
use App\DTOs\UpdateCompanyDTO;
use App\Models\Company;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;

class CompanyController extends Controller
{
    public function index()
    {
        $companies = Company::with(['companyType', 'region', 'city'])->get();
        return response()->json($companies);
    }

    public function store(Request $request): JsonResponse
    {
        
        $request->validate([
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

        $dto = new CreateCompanyDTO(
            $request->NAME,
            $request->EMAIL,
            $request->NIF,
            $request->COMPANY_TYPE_ID,
            $request->REGION_ID,
            $request->CITY_ID,
            $request->ADDRESS,
            $request->ACTIVE,
            $request->PHONE_NUMBER,
            $request->ICON_PATH
        );

        $company = Company::create([
            'NAME' => $dto->NAME,
            'EMAIL' => $dto->EMAIL,
            'NIF' => $dto->NIF,
            'COMPANY_TYPE_ID' => $dto->COMPANY_TYPE_ID,
            'REGION_ID' => $dto->REGION_ID,
            'CITY_ID' => $dto->CITY_ID,
            'ADDRESS' => $dto->ADDRESS,
            'ACTIVE' => $dto->ACTIVE,
            'PHONE_NUMBER' => $dto->PHONE_NUMBER,
            'ICON_PATH' => $dto->ICON_PATH,
        ]);

        $company->load(['companyType', 'region', 'city']);

        return response()->json($company, 201);
    }

    public function show(Company $company): JsonResponse
    {
        $company->load(['companyType', 'region', 'city']);
        return response()->json($company);
    }

    public function update(Request $request, Company $company): JsonResponse
    {
        $request->validate([
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

        $company->NAME = $request->input('NAME', $company->NAME);
        $company->EMAIL = $request->input('EMAIL', $company->EMAIL);
        $company->NIF = $request->input('NIF', $company->NIF);
        $company->COMPANY_TYPE_ID = $request->input('COMPANY_TYPE_ID', $company->COMPANY_TYPE_ID);
        $company->REGION_ID = $request->input('REGION_ID', $company->REGION_ID);
        $company->CITY_ID = $request->input('CITY_ID', $company->CITY_ID);
        $company->ADDRESS = $request->input('ADDRESS', $company->ADDRESS);
        $company->ACTIVE = $request->input('ACTIVE', $company->ACTIVE);
        $company->PHONE_NUMBER = $request->input('PHONE_NUMBER', $company->PHONE_NUMBER);
        $company->ICON_PATH = $request->input('ICON_PATH', $company->ICON_PATH);
        
        $company->save();

        $company->load(['companyType', 'region', 'city']);

        return response()->json($company);
    }

    public function destroy(Company $company)
    {
        $company->delete();
        return response()->json(null, 204);
    }
}