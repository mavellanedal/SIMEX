<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class TrackingSteps extends Model
{
  protected $table = 'TRACKING_STEPS';
  protected $primaryKey = 'ID';
  public $timestamps = false;

  protected $fillable = [
    'ID',
    'ORDER_NUM',
    'NAME'
  ];
}
