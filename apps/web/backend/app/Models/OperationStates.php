<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class OperationStates extends Model
{
    protected $table = 'OPERATION_STATES';

    protected $primaryKey = 'ID';

    public $timestamps = false;

    protected $fillable = [
        'ID',
        'NAME',
        'DESCRIPTION',
    ];
}
