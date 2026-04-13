<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class ContainerTypes extends Model
{
  protected $table = 'CONTAINER_TYPES';
  protected $primaryKey = 'ID';
  public $timestamps = false;

  protected $fillable = [
    'ID',
    'TYPE'
  ];
}
