<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class CostTypes extends Model
{
    protected $table = 'COST_TYPES';

    protected $primaryKey = 'ID';

    public $timestamps = false;

    protected $fillable = [
        'ID',
        'NAME',
        'DESCRIPTION',
    ];
}
