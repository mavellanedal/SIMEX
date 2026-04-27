<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class CompanyType extends Model
{
    protected $table = 'COMPANY_TYPES';

    protected $primaryKey = 'ID';

    public $timestamps = false;

    protected $fillable = [
        'NAME',
        'DESCRIPTION',
    ];
}
