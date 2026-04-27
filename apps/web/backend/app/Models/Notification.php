<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Notification extends Model
{
    protected $table = 'NOTIFICATION';

    protected $primaryKey = 'ID';

    public $timestamps = false;

    protected $fillable = [
        'NAME',
        'DESCRIPTION',
        'USER_ID',
        'OPERATION_ID',
        'STATE_ID',
        'LOGIC_REMOVE',
    ];

    public function state()
    {
        return $this->belongsTo(NotificationState::class, 'STATE_ID', 'ID');
    }

    public function user()
    {
        return $this->belongsTo(User::class, 'USER_ID', 'ID');
    }
}
