<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use App\Models\IncotermTypes;
use App\Models\TrackingSteps;

class Incoterms extends Model
{
  protected $table = 'INCOTERMS';
  protected $primaryKey = 'ID';
  public $timestamps = false;

  protected $fillable = [
    'ID',
    'INCOTERM_TYPE_ID',
    'TRACKING_STEP_ID'
  ];

  public function incotermType()
  {
    return $this->belongsTo(IncotermTypes::class, 'INCOTERM_TYPE_ID', 'ID');
  }

  public function trackingStep()
  {
    return $this->belongsTo(TrackingSteps::class, 'TRACKING_STEP_ID', 'ID');
  }
}
