<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Counter extends Model
{
    protected $table = "counter";

    /**
     * @var array
     */
    protected $fillable = [
        'owner_id',
        'type',
        'name',
        'address',
        'phone',
        'description',
        'image',
    ];
}
