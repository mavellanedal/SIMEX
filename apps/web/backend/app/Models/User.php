<?php

namespace App\Models;

// use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use Laravel\Sanctum\HasApiTokens;

class User extends Authenticatable
{
    /** @use HasFactory<UserFactory> */
    use HasApiTokens, Notifiable;

    protected $table = 'USERS';

    public $timestamps = false;

    protected $primaryKey = 'ID';

    /**
     * The attributes that are mass assignable.
     *
     * @var list<string>
     */
    protected $fillable = [
        'EMAIL',
        'PASSWORD',
        'FIRST_NAME',
        'LAST_NAME',
        'ROLE_ID',
        'COMPANY_ID',
        'USERNAME',
        'ACTIVE',
        'IDENTIFICATION_CARD_PATH',
    ];

    /**
     * The attributes that should be hidden for serialization.
     *
     * @var list<string>
     */
    protected $hidden = [
        'password',
        'remember_token',
    ];

    /**
     * Get the attributes that should be cast.
     *
     * @return array<string, string>
     */
    protected function casts(): array
    {
        return [
            'email_verified_at' => 'datetime',
            'password' => 'hashed',
        ];
    }

    public function role()
    {
        return $this->belongsTo(Role::class, 'ROLE_ID', 'ID');
    }

    public function company()
    {
        return $this->belongsTo(Company::class, 'COMPANY_ID', 'ID');
    }

    public function getAuthPassword()
    {
        return $this->PASSWORD;
    }

    public function notifications()
    {
        return $this->hasMany(Notification::class, 'USER_ID', 'ID');
    }
}
