<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo; // <-- AÑADIR ESTA LÍNEA

class Region extends Model
{
    protected $table = 'REGIONS';
    protected $primaryKey = 'ID';
    public $timestamps = false;

    protected $fillable = [
        'NAME',
        'COUNTRY_ID',
    ];

    public function country(): BelongsTo
    {
        return $this->belongsTo(Country::class, 'COUNTRY_ID', 'ID');
    }
}