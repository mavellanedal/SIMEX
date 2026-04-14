<?php

namespace App\Http\Controllers;

use App\DTOs\createCompanyDTO;
use App\DTOs\updateCompanyDTO;
use App\Models\Company;
use Illuminate\Http\Request;
use Illuminate\Http\JsonResponse;

class CompanyController extends Controller
{


    public function index()
    {

        $companies = Company::with(['companyType', 'region', 'city'])->get();

        // Recorremos cada empresa que hemos obtenido para añadirle un dato que le falta.
        //    Como la empresa solo tiene REGION_ID, pero el frontend necesita COUNTRY_ID para los desplegables,


        $companies->each(function ($company) {
            $company->COUNTRY_ID = $company->region ? $company->region->COUNTRY_ID : null;
        });

        return response()->json($companies);
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


        //    Le añadimos las relaciones y el COUNTRY_ID
        $company->load(['companyType', 'region', 'city']);
        $company->COUNTRY_ID = $company->region ? $company->region->COUNTRY_ID : null;


        return response()->json($company, 201);

    }


    public function show(Company $company)
    {
        $company->load(['companyType', 'region', 'city']);

        $company->COUNTRY_ID = $company->region ? $company->region->COUNTRY_ID : null;

        return response()->json($company);
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

        $company->load(['companyType', 'region', 'city']);
        $company->COUNTRY_ID = $company->region ? $company->region->COUNTRY_ID : null;

        return response()->json($company);
    }


    public function destroy(Company $company)
    {
        $company->delete();

        return response()->json(null, 204);
    }
}
