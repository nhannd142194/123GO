<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Article extends Model
{
    /**
     * @var array
     */
    protected $fillable = [
        'type',
        'user_id',
        'category_id',
        'title',
        'slug',
        'body',
        'image',
        'published_at',
    ];
}
