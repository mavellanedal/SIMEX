<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class IncotermTypes extends Model
{
    protected $table = 'INCOTERM_TYPES';

    protected $primaryKey = 'ID';

    public $timestamps = false;

    protected $fillable = [
        'ID',
        'CODE',
        'NAME',
    ];
}
