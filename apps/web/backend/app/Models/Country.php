<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Relations\HasMany;
use Illuminate\Database\Eloquent\Relations\HasManyThrough; 

class Country extends Model
{
    protected $table = 'COUNTRIES';
    protected $primaryKey = 'ID';
    public $timestamps = false;

    protected $fillable = [
        'NAME',
    ];

    public function regions(): HasMany
    {
        return $this->hasMany(Region::class, 'COUNTRY_ID', 'ID');
    }

    
     
    public function cities(): HasManyThrough 
    {
        return $this->hasManyThrough(City::class, Region::class, 'COUNTRY_ID', 'REGION_ID', 'ID', 'ID');
    }
}