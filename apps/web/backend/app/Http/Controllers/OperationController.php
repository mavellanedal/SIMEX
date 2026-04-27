<?php

namespace App\Http\Controllers;

use App\DTOs\OperationDTO;
use App\DTOs\OperationListDTO;
use App\Models\Costs;
use App\Models\Notification;
use App\Models\Operations;
use App\Models\OperationStateHistory;
use App\Models\User;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Validation\Rule;

class OperationController extends Controller
{
    public function index(Request $request)
    {
        try {
            $user = $request->user();
            $query = Operations::select(
                'ID', 'ORDER_REFERENCE', 'IMPORTER_ID', 'EXPORTATOR_ID',
                'ORIGIN_PORT_ID', 'DESTINATION_PORT_ID', 'INCOTERM_ID'
            )
                ->with([
                    'importer:ID,NAME',
                    'exportator:ID,NAME',
                    'originPort:ID,NAME',
                    'destinationPort:ID,NAME',
                    'stateHistory',
                    'incoterm' => function ($q) {
                        $q->with('incotermType', 'trackingStep');
                    },
                ]);

            if ($user && $user->ROLE_ID == 2) {
                $query->where(function ($q) use ($user) {
                    $q->where('IMPORTER_ID', $user->COMPANY_ID)
                        ->orWhere('EXPORTATOR_ID', $user->COMPANY_ID)
                        ->orWhere('BUYER_ID', $user->COMPANY_ID)
                        ->orWhere('SELLER_ID', $user->COMPANY_ID);
                });
            }

            $operations = $query->orderBy('ID', 'desc')->get();

            $operationsDTO = $operations->map(function ($operation) {
                return OperationListDTO::fromModel($operation);
            });

            return response()->json([
                'message' => 'Operations retrieved successfully',
                'data' => $operationsDTO,
            ], 200);
        } catch (\Exception $e) {
            return response()->json([
                'message' => 'Failed to retrieve operations',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    private function mapCamelToSnake(Request $request)
    {
        $mappedRequest = [
            'CREATE_USER_ID' => $request->input('createUserId'),
            'ORDER_REFERENCE' => $request->input('orderReference'),
            'IMPORTER_ID' => $request->input('importerId'),
            'PICKUP_DATA' => $request->input('pickupData'),
            'INCOTERM_ID' => $request->input('incotermId'),
            'ETD' => $request->input('etd'),
            'CUSTOMS_AGENT_ID' => $request->input('customsAgentId'),
            'ETA' => $request->input('eta'),
            'EXPORTATOR_ID' => $request->input('exportatorId'),
            'OPERATION_USER_ID' => $request->input('operationUserId'),
            'SALES_USER_ID' => $request->input('salesUserId'),
            'DOCUMENT_USER_ID' => $request->input('documentUserId'),
            'CONTAINER_NUMBER' => $request->input('containerNumber'),
            'CONTAINER_TYPE_ID' => $request->input('containerTypeId'),
            'HS_CODE' => $request->input('hsCode'),
            'PACKAGES_NUMBER' => $request->input('packagesNumber'),
            'PACKAGE_TYPE_ID' => $request->input('packageTypeId'),
            'PACKAGE_SUB_TYPE_ID' => $request->input('packageSubTypeId'),
            'VOLUME' => $request->input('volume'),
            'NET_WEIGHT' => $request->input('netWeight'),
            'KILOGRAMS' => $request->input('kilogram', $request->input('kilograms')),
            'PIECES_NUMBER' => $request->input('piecesNumber'),
            'MBL_NUMBER' => $request->input('mblNumber'),
            'BUYER_ID' => $request->input('buyerId'),
            'SELLER_ID' => $request->input('sellerId'),
            'NAVIERA_ID' => $request->input('navieraId'),
            'CARGO' => $request->input('cargo'),
            'ORIGIN_PORT_ID' => $request->input('originPortId'),
            'DESTINATION_PORT_ID' => $request->input('destinationPortId'),
            'CARGO_DESCRIPTION' => $request->input('cargoDescription'),
            'TOTAL_COST' => $request->input('totalCost'),
            'TOTAL_SALE' => $request->input('totalSale'),
            'PROFIT' => $request->input('profit'),
        ];

        $costs = $request->input('costs');
        if (is_array($costs)) {
            $mappedRequest['COSTS'] = array_map(function ($c) {
                return [
                    'COST_TYPE_ID' => $c['costTypeId'] ?? null,
                    'CURRENCY_TYPE_ID' => $c['currencyTypeId'] ?? null,
                    'SENED_TYPE_ID' => $c['senedTypeId'] ?? $c['sendTypeId'] ?? null,
                    'COST' => $c['cost'] ?? null,
                    'COST_AMOUNT' => $c['costAmount'] ?? null,
                    'SALE' => $c['sale'] ?? null,
                    'SALE_AMOUNT' => $c['saleAmount'] ?? null,
                ];
            }, $costs);
        }

        $request->merge(array_filter($mappedRequest, function ($val) {
            return $val !== null;
        }));
    }

    public function store(Request $request)
    {
        $this->mapCamelToSnake($request);

        $operationValidated = $request->validate([
            'CREATE_USER_ID' => 'required|exists:users,id',
            'ORDER_REFERENCE' => 'required|string|max:255',
            'IMPORTER_ID' => [
                'required',
                Rule::exists('company', 'id')->whereIn('COMPANY_TYPE_ID', [1, 3]),
            ],
            'PICKUP_DATA' => 'required|string',
            'INCOTERM_ID' => 'required|exists:incoterms,id',
            'ETD' => 'required|date',
            'CUSTOMS_AGENT_ID' => 'required|exists:users,id,ROLE_ID,5',
            'ETA' => 'required|date',
            'EXPORTATOR_ID' => [
                'required',
                Rule::exists('company', 'id')->whereIn('COMPANY_TYPE_ID', [2, 3]),
            ],
            'OPERATION_USER_ID' => 'required|exists:users,id',
            'SALES_USER_ID' => 'required|exists:users,id',
            'DOCUMENT_USER_ID' => 'required|exists:users,id',
            'CONTAINER_NUMBER' => 'required|string|max:255',
            'CONTAINER_TYPE_ID' => 'required|exists:container_types,id',
            'HS_CODE' => 'required|string|max:255',
            'PACKAGES_NUMBER' => 'required|integer',
            'PACKAGE_TYPE_ID' => 'required|exists:package_types,id',
            'PACKAGE_SUB_TYPE_ID' => 'required|exists:package_types,id',
            'VOLUME' => 'required|numeric',
            'NET_WEIGHT' => 'required|numeric',
            'KILOGRAMS' => 'required|numeric',
            'PIECES_NUMBER' => 'required|integer',
            'MBL_NUMBER' => 'required|string|max:255',
            'BUYER_ID' => [
                'required',
                Rule::exists('company', 'id')->whereIn('COMPANY_TYPE_ID', [1, 2, 3]),
            ],
            'SELLER_ID' => [
                'required',
                Rule::exists('company', 'id')->whereIn('COMPANY_TYPE_ID', [1, 2, 3]),
            ],
            'NAVIERA_ID' => 'required|exists:company,id,COMPANY_TYPE_ID,4',
            'CARGO' => 'required|string|max:255',
            'ORIGIN_PORT_ID' => 'required|exists:ports,id',
            'DESTINATION_PORT_ID' => 'required|exists:ports,id',
            'CARGO_DESCRIPTION' => 'required|string',
            'TOTAL_COST' => 'required|numeric',
            'TOTAL_SALE' => 'required|numeric',
            'PROFIT' => 'required|numeric',

            'COSTS' => 'nullable|array',
            'COSTS.*.COST_TYPE_ID' => 'required|exists:cost_types,id',
            'COSTS.*.CURRENCY_TYPE_ID' => 'required|exists:currency_types,id',
            'COSTS.*.SENED_TYPE_ID' => 'required|exists:send_types,id',
            'COSTS.*.COST' => 'required|numeric',
            'COSTS.*.COST_AMOUNT' => 'required|integer',
            'COSTS.*.SALE' => 'required|numeric',
            'COSTS.*.SALE_AMOUNT' => 'required|integer',
        ]);

        DB::beginTransaction();

        try {
            $costsArray = $operationValidated['COSTS'] ?? [];
            unset($operationValidated['COSTS']);

            $operation = Operations::create($operationValidated);

            OperationStateHistory::create([
                'OPERATION_ID' => $operation->ID,
                'OPERATION_STATE_ID' => 1,
                'DATE' => Carbon::now(),
            ]);

            if (! empty($costsArray)) {
                $costsData = array_map(function ($cost) use ($operation) {
                    return [
                        'OPERATION_ID' => $operation->ID,
                        'COST_TYPE_ID' => $cost['COST_TYPE_ID'],
                        'CURRENCY_TYPE_ID' => $cost['CURRENCY_TYPE_ID'],
                        'SENED_TYPE_ID' => $cost['SENED_TYPE_ID'],
                        'COST' => $cost['COST'],
                        'COST_AMOUNT' => $cost['COST_AMOUNT'],
                        'SALE' => $cost['SALE'],
                        'SALE_AMOUNT' => $cost['SALE_AMOUNT'],
                    ];
                }, $costsArray);

                Costs::insert($costsData);
            }

            $companiesToNotify = array_filter([$operation->IMPORTER_ID, $operation->EXPORTATOR_ID]);

            $usersToNotify = User::whereIn('COMPANY_ID', $companiesToNotify)
                ->where('ROLE_ID', 2)
                ->get();

            $notificationsData = [];

            foreach ($usersToNotify as $userTarget) {
                $notificationsData[] = [
                    'NAME' => 'Nueva operación asignada',
                    'DESCRIPTION' => 'Accede al panel de operaciones para aceptarla',
                    'USER_ID' => $userTarget->ID,
                    'OPERATION_ID' => $operation->ID,
                    'STATE_ID' => 1,
                    'LOGIC_REMOVE' => 0,
                ];
            }

            if (! empty($notificationsData)) {
                Notification::insert($notificationsData);
            }

            $operation->load([
                'createUser', 'importer', 'incoterm', 'exportator',
                'operationUser', 'salesUser', 'documentUser',
                'containerType', 'packageType', 'packageSubType',
                'buyer', 'seller', 'naviera', 'originPort', 'destinationPort',
            ]);

            DB::commit();

            return response()->json([
                'message' => 'Operation created successfully',
                'operation' => OperationDTO::fromModel($operation),
            ], 201);
        } catch (\Exception $e) {
            DB::rollback();

            return response()->json([
                'message' => 'Failed to create operation',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    public function show($id)
    {
        try {
            $operation = Operations::with([
                'createUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'importer' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'incoterm' => function ($q) {
                    $q->with('incotermType', 'trackingStep');
                },
                'exportator' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'operationUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'salesUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'documentUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'containerType',
                'packageType',
                'packageSubType',
                'buyer' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'seller' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'naviera' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'originPort' => function ($q) {
                    $q->with('city.country', 'city.region');
                },
                'destinationPort' => function ($q) {
                    $q->with('city.country', 'city.region');
                },
            ])->findOrFail($id);

            return response()->json([
                'message' => 'Operation retrieved successfully',
                'data' => OperationDTO::fromModel($operation),
            ], 200);
        } catch (\Exception $e) {
            return response()->json([
                'message' => 'Failed to retrieve operation',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    public function update(Request $request, $id)
    {
        $this->mapCamelToSnake($request);

        $operationValidated = $request->validate([
            'CREATE_USER_ID' => 'required|exists:users,id',
            'ORDER_REFERENCE' => 'required|string|max:255',
            'IMPORTER_ID' => [
                'required',
                Rule::exists('company', 'id')->whereIn('COMPANY_TYPE_ID', [1, 3]),
            ],
            'PICKUP_DATA' => 'required|string',
            'INCOTERM_ID' => 'required|exists:incoterms,id',
            'ETD' => 'required|date',
            'CUSTOMS_AGENT_ID' => 'required|exists:users,id,ROLE_ID,5',
            'ETA' => 'required|date',
            'EXPORTATOR_ID' => [
                'required',
                Rule::exists('company', 'id')->whereIn('COMPANY_TYPE_ID', [2, 3]),
            ],
            'OPERATION_USER_ID' => 'required|exists:users,id',
            'SALES_USER_ID' => 'required|exists:users,id',
            'DOCUMENT_USER_ID' => 'required|exists:users,id',
            'CONTAINER_NUMBER' => 'required|string|max:255',
            'CONTAINER_TYPE_ID' => 'required|exists:container_types,id',
            'HS_CODE' => 'required|string|max:255',
            'PACKAGES_NUMBER' => 'required|integer',
            'PACKAGE_TYPE_ID' => 'required|exists:package_types,id',
            'PACKAGE_SUB_TYPE_ID' => 'required|exists:package_types,id',
            'VOLUME' => 'required|numeric',
            'NET_WEIGHT' => 'required|numeric',
            'KILOGRAMS' => 'required|numeric',
            'PIECES_NUMBER' => 'required|integer',
            'MBL_NUMBER' => 'required|string|max:255',
            'BUYER_ID' => [
                'required',
                Rule::exists('company', 'id')->whereIn('COMPANY_TYPE_ID', [1, 2, 3]),
            ],
            'SELLER_ID' => [
                'required',
                Rule::exists('company', 'id')->whereIn('COMPANY_TYPE_ID', [1, 2, 3]),
            ],
            'NAVIERA_ID' => 'required|exists:company,id,COMPANY_TYPE_ID,4',
            'CARGO' => 'required|string|max:255',
            'ORIGIN_PORT_ID' => 'required|exists:ports,id',
            'DESTINATION_PORT_ID' => 'required|exists:ports,id',
            'CARGO_DESCRIPTION' => 'required|string',
            'TOTAL_COST' => 'required|numeric',
            'TOTAL_SALE' => 'required|numeric',
            'PROFIT' => 'required|numeric',

            'COSTS' => 'nullable|array',
            'COSTS.*.COST_TYPE_ID' => 'required|exists:cost_types,id',
            'COSTS.*.CURRENCY_TYPE_ID' => 'required|exists:currency_types,id',
            'COSTS.*.SENED_TYPE_ID' => 'required|exists:send_types,id',
            'COSTS.*.COST' => 'required|numeric',
            'COSTS.*.COST_AMOUNT' => 'required|integer',
            'COSTS.*.SALE' => 'required|numeric',
            'COSTS.*.SALE_AMOUNT' => 'required|integer',
        ]);

        DB::beginTransaction();

        try {
            $operation = Operations::findOrFail($id);

            $costsArray = $operationValidated['COSTS'] ?? [];
            unset($operationValidated['COSTS']);

            $operation->update($operationValidated);

            Costs::where('OPERATION_ID', $operation->ID)->delete();

            if (! empty($costsArray)) {
                $costsData = array_map(function ($cost) use ($operation) {
                    return [
                        'OPERATION_ID' => $operation->ID,
                        'COST_TYPE_ID' => $cost['COST_TYPE_ID'],
                        'CURRENCY_TYPE_ID' => $cost['CURRENCY_TYPE_ID'],
                        'SENED_TYPE_ID' => $cost['SENED_TYPE_ID'],
                        'COST' => $cost['COST'],
                        'COST_AMOUNT' => $cost['COST_AMOUNT'],
                        'SALE' => $cost['SALE'],
                        'SALE_AMOUNT' => $cost['SALE_AMOUNT'],
                    ];
                }, $costsArray);

                Costs::insert($costsData);
            }

            $operation->load([
                'createUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'importer' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'incoterm' => function ($q) {
                    $q->with('incotermType', 'trackingStep');
                },
                'exportator' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'operationUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'salesUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'documentUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'containerType',
                'packageType',
                'packageSubType',
                'buyer' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'seller' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'naviera' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'originPort' => function ($q) {
                    $q->with('city.country', 'city.region');
                },
                'destinationPort' => function ($q) {
                    $q->with('city.country', 'city.region');
                },
            ]);

            DB::commit();

            return response()->json([
                'message' => 'Operation updated successfully',
                'operation' => OperationDTO::fromModel($operation),
            ], 200);
        } catch (\Exception $e) {
            DB::rollback();

            return response()->json([
                'message' => 'Failed to update operation',
                'error' => $e->getMessage(),
            ], 500);
        }
    }

    public function changeStateOperation(Request $request, $id)
    {

        $request->validate([
            'newState' => 'required|exists:operation_states,ID',
            'observations' => 'nullable|string',
        ]);

        try {
            $operation = Operations::findOrFail($id);

            $lastStateHistory = OperationStateHistory::where('OPERATION_ID', $operation->ID)
                ->orderBy('DATE', 'desc')
                ->first();

            $currentStateId = $lastStateHistory ? $lastStateHistory->OPERATION_STATE_ID : 1;

            if ($currentStateId != 1 && in_array($request->newState, [2, 3])) {
                return response()->json([
                    'message' => 'Only operations in "Created" state can be accepted or rejected',
                ], 400);
            }

            DB::beginTransaction();

            OperationStateHistory::create([
                'OPERATION_ID' => $operation->ID,
                'OPERATION_STATE_ID' => $request->newState,
                'DATE' => Carbon::now(),
                'OBSERVATIONS' => $request->input('observations'),
            ]);

            $operation->load([
                'createUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'importer' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'incoterm' => function ($q) {
                    $q->with('incotermType', 'trackingStep');
                },
                'exportator' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'operationUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'salesUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'documentUser' => function ($q) {
                    $q->with('role', 'company');
                },
                'containerType',
                'packageType',
                'packageSubType',
                'buyer' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'seller' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'naviera' => function ($q) {
                    $q->with('companyType', 'region', 'city');
                },
                'originPort' => function ($q) {
                    $q->with('city.country', 'city.region');
                },
                'destinationPort' => function ($q) {
                    $q->with('city.country', 'city.region');
                },
            ]);

            DB::commit();

            return response()->json([
                'message' => 'Operation accepted successfully',
                'operation' => OperationDTO::fromModel($operation),
            ], 200);
        } catch (\Exception $e) {
            DB::rollback();

            return response()->json([
                'message' => 'Failed to accept operation',
                'error' => $e->getMessage(),
            ], 500);
        }
    }
}
