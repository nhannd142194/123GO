<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Comment extends Model
{
    protected $table = "comment";

    /**
     * @var array
     */
    protected $fillable = [
        'food_id',
        'user_id',
        'comment',
    ];
}
