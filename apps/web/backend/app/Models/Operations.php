<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use App\Models\Company;
use App\Models\ContainerTypes;
use App\Models\User;
use App\Models\Ports;
use App\Models\Incoterms;
use App\Models\PackageTypes;

class Operations extends Model
{
  protected $table = 'OPERATIONS';
  protected $primaryKey = 'ID';
  public $timestamps = false;



  protected $fillable = [
    'ID',
    'CREATE_USER_ID',
    'ORDER_REFERENCE',
    'IMPORTER_ID',
    'PICKUP_DATA',
    'INCOTERM_ID',
    'ETD',
    'CUSTOMS_AGENT_ID',
    'ETA',
    'EXPORTATOR_ID',
    'OPERATION_USER_ID',
    'SALES_USER_ID',
    'DOCUMENT_USER_ID',
    'CONTAINER_NUMBER',
    'CONTAINER_TYPE_ID',
    'HS_CODE',
    'PACKAGES_NUMBER',
    'PACKAGE_TYPE_ID',
    'PACKAGE_SUB_TYPE_ID',
    'VOLUME',
    'NET_WEIGHT',
    'KILOGRAMS',
    'PIECES_NUMBER',
    'MBL_NUMBER',
    'BUYER_ID',
    'SELLER_ID',
    'NAVIERA_ID',
    'CARGO',
    'ORIGIN_PORT_ID',
    'DESTINATION_PORT_ID',
    'CARGO_DESCRIPTION',
    'TOTAL_COST',
    'TOTAL_SALE',
    'PROFIT'
  ];

  public function buyer()
  {
    return $this->belongsTo(Company::class, 'BUYER_ID', 'ID');
  }

  public function containerType()
  {
    return $this->belongsTo(ContainerTypes::class, 'CONTAINER_TYPE_ID', 'ID');
  }

  public function createUser()
  {
    return $this->belongsTo(User::class, 'CREATE_USER_ID', 'ID');
  }

  public function customsAgent()
  {
    return $this->belongsTo(User::class, 'CUSTOMS_AGENT_ID', 'ID');
  }

  public function destinationPort() {
    return $this->belongsTo(Ports::class, 'DESTINATION_PORT_ID', 'ID');
  }

  public function documentUser()
  {
    return $this->belongsTo(User::class, 'DOCUMENT_USER_ID', 'ID');
  }

  public function exportator()
  {
    return $this->belongsTo(Company::class, 'EXPORTATOR_ID', 'ID');
  }

  public function importer()
  {
    return $this->belongsTo(Company::class, 'IMPORTER_ID', 'ID');
  }

  public function incoterm()
  {
    return $this->belongsTo(Incoterms::class, 'INCOTERM_ID', 'ID');
  }

  public function naviera()
  {
    return $this->belongsTo(Company::class, 'NAVIERA_ID', 'ID');
  }

  public function operationUser()
  {
    return $this->belongsTo(User::class, 'OPERATION_USER_ID', 'ID');
  }

  public function originPort()
  {
    return $this->belongsTo(Ports::class, 'ORIGIN_PORT_ID', 'ID');
  }

  public function packageType()
  {
    return $this->belongsTo(PackageTypes::class, 'PACKAGE_TYPE_ID', 'ID');
  }

  public function packageSubType()
  {
    return $this->belongsTo(PackageTypes::class, 'PACKAGE_SUB_TYPE_ID', 'ID');
  }

  public function salesUser()
  {
    return $this->belongsTo(User::class, 'SALES_USER_ID', 'ID');
  }

  public function seller()
  {
    return $this->belongsTo(Company::class, 'SELLER_ID', 'ID');
  }
}
