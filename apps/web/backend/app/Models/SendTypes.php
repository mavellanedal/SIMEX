<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class SendTypes extends Model
{
    protected $table = 'SEND_TYPES';

    protected $primaryKey = 'ID';

    public $timestamps = false;

    protected $fillable = [
        'ID',
        'NAME',
        'DESCRIPTION',
    ];
}
