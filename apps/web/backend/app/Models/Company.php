<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasOneThrough;
class Company extends Model
{
  protected $table = 'COMPANY';
  protected $primaryKey = 'ID';
  public $timestamps = false;

  protected $fillable = [
    'NAME',
    'EMAIL',
    'PHONE_NUMBER',
    'NIF',
    'ICON_PATH',
    'COMPANY_TYPE_ID',
    'REGION_ID',
    'CITY_ID',
    'ADDRESS',
    'ACTIVE',
  ];


    protected $casts = [
      'ACTIVE' => 'boolean',
  ];

  
  public function companyType()
  {
    return $this->belongsTo(CompanyType::class, 'COMPANY_TYPE_ID', 'ID');
  }

  public function region()
  {
    return $this->belongsTo(Region::class, 'REGION_ID', 'ID');
  }

  public function city()
  {
    return $this->belongsTo(City::class, 'CITY_ID', 'ID');
  }
}