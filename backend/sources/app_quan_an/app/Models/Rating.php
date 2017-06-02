<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Rating extends Model
{
    protected $table = "rating";

    /**
     * @var array
     */
    protected $fillable = [
        'food_id',
        'user_id',
        'score',
    ];
}
