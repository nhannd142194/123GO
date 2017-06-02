<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Food extends Model
{
    protected $table = "food";

    /**
     * @var array
     */
    protected $fillable = [
        'counter_id',
        'type',
        'name',
        'description',
        'price',
        'discount',
        'image',
    ];
}
