<?php
namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class UserNotificationConfig extends Model
{
    protected $table = 'USER_NOTIFICATION_CONFIG';
    protected $primaryKey = 'ID';
    public $timestamps = false;

    protected $fillable = ['USER_ID', 'PUSH', 'EMAIL', 'SMS'];
}
