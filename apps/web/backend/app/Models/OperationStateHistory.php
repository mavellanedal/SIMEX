<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class OperationStateHistory extends Model
{
    protected $table = 'OPERATION_STATE_HISTORY';

    protected $primaryKey = 'ID';

    public $timestamps = false;

    protected $fillable = [
        'ID',
        'DATE',
        'OPERATION_STATE_ID',
        'OPERATION_ID',
        'OBSERVATIONS',
    ];

    public function Operation()
    {
        return $this->belongsTo(Operations::class, 'OPERATION_ID');
    }

    public function OperationState()
    {
        return $this->belongsTo(OperationStates::class, 'OPERATION_STATE_ID');
    }
}
