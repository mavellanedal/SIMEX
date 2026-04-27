<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class CurrencyTypes extends Model
{
    protected $table = 'CURRENCY_TYPES';

    protected $primaryKey = 'ID';

    public $timestamps = false;

    protected $fillable = [
        'ID',
        'NAME',
        'SYMBOL',
    ];
}
