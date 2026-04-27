<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\BelongsTo;

class City extends Model
{
    protected $table = 'CITIES';

    protected $primaryKey = 'ID';

    public $timestamps = false;

    protected $fillable = [
        'NAME',
        'COUNTRY_ID',
        'REGION_ID',
        'LATITUDE',
        'LONGITUDE',
        'ALTITUDE',
    ];

    public function country(): BelongsTo
    {
        return $this->belongsTo(Country::class, 'COUNTRY_ID', 'ID');
    }

    public function region(): BelongsTo
    {
        return $this->belongsTo(Region::class, 'REGION_ID', 'ID');
    }
}
