<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Role extends Model
{
    protected $table = 'ROLES';
    protected $primaryKey = 'ID';
    public $timestamps = false;

    protected $fillable = [
        'NAME',
        'DESCRIPTION',
    ];
}
