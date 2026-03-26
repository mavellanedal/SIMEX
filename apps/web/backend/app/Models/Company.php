<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Company extends Model
{
  protected $table = 'COMPANIES';
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
    'CITIY_ID',
    'ADDRESS',
    'ACTIVE',
  ];

  public function companyType()
  {
    return $this->belongsTo(CompanyType::class, 'COMPANY_TYPE_ID');
  }
}
