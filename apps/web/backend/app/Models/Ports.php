<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Ports extends Model
{
  protected $table = 'PORTS';
  protected $primaryKey = 'ID';
  public $timestamps = false;

  protected $fillable = [
    'ID',
    'NAME',
    'CITY_ID'
  ];


  public function city()
  {
    return $this->belongsTo(City::class, 'CITY_ID', 'ID');
  }
}
