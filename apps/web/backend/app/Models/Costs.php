<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Costs extends Model
{
    protected $table = 'COSTS';

    protected $primaryKey = 'ID';

    public $timestamps = false;

    protected $fillable = [
        'ID',
        'OPERATION_ID',
        'COST_TYPE_ID',
        'CURRENCY_TYPE_ID',
        'SENED_TYPE_ID',
        'COST',
        'COST_AMOUNT',
        'SALE',
        'SALE_AMOUNT',
    ];

    public function operation()
    {
        return $this->belongsTo(Operations::class, 'OPERATION_ID', 'ID');
    }

    public function costType()
    {
        return $this->belongsTo(CostTypes::class, 'COST_TYPE_ID', 'ID');
    }

    public function currencyType()
    {
        return $this->belongsTo(CurrencyTypes::class, 'CURRENCY_TYPE_ID', 'ID');
    }

    public function sendType()
    {
        return $this->belongsTo(SendTypes::class, 'SENED_TYPE_ID', 'ID');
    }
}
