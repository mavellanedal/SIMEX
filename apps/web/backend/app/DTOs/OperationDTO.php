<?php

namespace App\DTOs;

use App\Models\Operations;

readonly class OperationDTO
{
    public function __construct(
        public int $id,
        public ?UserDTO $createUser,
        public string $orderReference,
        public ?CompanyDTO $importer,
        public ?string $pickUpData,
        public ?IncotermDTO $incoterm,
        public ?string $etd,
        public ?string $eta,
        public ?CompanyDTO $exporter,
        public ?UserDTO $operationUser,
        public ?UserDTO $salesUser,
        public ?UserDTO $documentUser,
        public ?string $containerNumber,
        public ?ContainerTypeDTO $containerType,
        public ?string $hsCode,
        public ?int $packagesNumber,
        public ?PackageTypeDTO $packageType,
        public ?PackageTypeDTO $packageSubtype,
        public ?float $volume,
        public ?float $netWeight,
        public ?float $kilograms,
        public ?int $piecesNumber,
        public ?string $mblNumber,
        public ?CompanyDTO $buyer,
        public ?CompanyDTO $seller,
        public ?CompanyDTO $naviera,
        public ?string $cargo,
        public ?PortDTO $originPort,
        public ?PortDTO $destinationPort,
        public ?string $cargoDescription,
        public ?float $totalCost,
        public ?float $totalSale,
        public ?float $profit
    ) {}

    public static function fromModel(Operations $operation): ?self
    {
        if (! $operation) {
            return null;
        }

        return new self(
            id: $operation->ID ?? 0,
            createUser: UserDTO::fromModel($operation->createUser),
            orderReference: $operation->ORDER_REFERENCE ?? '',
            importer: CompanyDTO::fromModel($operation->importer),
            pickUpData: $operation->PICKUP_DATA,
            incoterm: IncotermDTO::fromModel($operation->incoterm),
            etd: $operation->ETD,
            eta: $operation->ETA,
            exporter: CompanyDTO::fromModel($operation->exportator),
            operationUser: UserDTO::fromModel($operation->operationUser),
            salesUser: UserDTO::fromModel($operation->salesUser),
            documentUser: UserDTO::fromModel($operation->documentUser),
            containerNumber: $operation->CONTAINER_NUMBER,
            containerType: ContainerTypeDTO::fromModel($operation->containerType),
            hsCode: $operation->HS_CODE,
            packagesNumber: $operation->PACKAGES_NUMBER,
            packageType: PackageTypeDTO::fromModel($operation->packageType),
            packageSubtype: PackageTypeDTO::fromModel($operation->packageSubType),
            volume: $operation->VOLUME !== null ? (float) $operation->VOLUME : null,
            netWeight: $operation->NET_WEIGHT !== null ? (float) $operation->NET_WEIGHT : null,
            kilograms: $operation->KILOGRAMS !== null ? (float) $operation->KILOGRAMS : null,
            piecesNumber: $operation->PIECES_NUMBER !== null ? (int) $operation->PIECES_NUMBER : null,
            mblNumber: $operation->MBL_NUMBER,
            buyer: CompanyDTO::fromModel($operation->buyer),
            seller: CompanyDTO::fromModel($operation->seller),
            naviera: CompanyDTO::fromModel($operation->naviera),
            cargo: $operation->CARGO,
            originPort: PortDTO::fromModel($operation->originPort),
            destinationPort: PortDTO::fromModel($operation->destinationPort),
            cargoDescription: $operation->CARGO_DESCRIPTION,
            totalCost: $operation->TOTAL_COST !== null ? (float) $operation->TOTAL_COST : null,
            totalSale: $operation->TOTAL_SALE !== null ? (float) $operation->TOTAL_SALE : null,
            profit: $operation->PROFIT !== null ? (float) $operation->PROFIT : null
        );
    }
}
