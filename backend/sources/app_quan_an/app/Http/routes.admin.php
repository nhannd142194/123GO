<?php

Route::group(['middleware' => config('admin.filter.auth')], function () {
//    Route::resource('students', 'StudentsController', [
//        'except' => 'show',
//        'names' => [
//            'index' => 'admin.students.index',
//            'create' => 'admin.students.create',
//            'store' => 'admin.students.store',
//            'show' => 'admin.students.show',
//            'update' => 'admin.students.update',
//            'edit' => 'admin.students.edit',
//            'destroy' => 'admin.students.destroy',
//        ],
//    ]);
});

Route::get('oauth/google', 'OAuthController@redirectToGoogleProvider')->name('login.google');
Route::get('oauth/google/callback', 'OAuthController@handleGoogleProviderCallback');
Route::get('oauth/facebook', 'OAuthController@redirectToFacebookProvider')->name('login.facebook');
Route::get('oauth/facebook/callback', 'OAuthController@handleFacebookProviderCallback');
Route::get('oauth/twitter', 'OAuthController@redirectToTwitterProvider')->name('login.twitter');
Route::get('oauth/twitter/callback', 'OAuthController@handleTwitterProviderCallback');

