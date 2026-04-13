<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class PackageTypes extends Model
{
  protected $table = 'PACKAGE_TYPES';
  protected $primaryKey = 'ID';
  public $timestamps = false;

  protected $fillable = [
    'ID',
    'NAME',
    'DESCRIPTION',
    'PARENT_ID'
  ];

  public function parent()
  {
    return $this->belongsTo(PackageTypes::class, 'PARENT_ID', 'ID');
  }
}
