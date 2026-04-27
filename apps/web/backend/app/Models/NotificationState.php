<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class NotificationState extends Model
{
    protected $table = 'NOTIFICATION_STATE';

    protected $primaryKey = 'ID';

    public $timestamps = false;

    protected $fillable = [
        'NAME',
        'DESCRIPTION',
    ];
}
