<?php

$api = app('Dingo\Api\Routing\Router');

$api->version('v1', function ($api) {
    $api->post('auth/login', 'App\Http\Controllers\Api\v1\AuthController@login');
    $api->post('auth/signup', 'App\Http\Controllers\Api\v1\AuthController@signup');

    $api->post('article', 'App\Http\Controllers\Api\v1\ArticlesController@index');
    $api->post('article/get_detail', 'App\Http\Controllers\Api\v1\ArticlesController@get_detail');
    $api->post('latest_food', 'App\Http\Controllers\Api\v1\FoodsController@index');
    $api->post('menu', 'App\Http\Controllers\Api\v1\FoodsController@get');
    $api->post('food/create', 'App\Http\Controllers\Api\v1\FoodsController@store');
    $api->post('food/search', 'App\Http\Controllers\Api\v1\FoodsController@search');
    $api->post('food/get_detail', 'App\Http\Controllers\Api\v1\FoodsController@get_detail');
    $api->post('food/rating', 'App\Http\Controllers\Api\v1\FoodsController@rating');
    $api->post('food/comment', 'App\Http\Controllers\Api\v1\FoodsController@comment');
    $api->post('counter/create', 'App\Http\Controllers\Api\v1\CountersController@store');
    $api->post('counter/get_detail', 'App\Http\Controllers\Api\v1\CountersController@get_detail');
});