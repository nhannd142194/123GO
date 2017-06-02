<?php
/**
 * Created by PhpStorm.
 * User: thien
 * Date: 4/9/2017
 * Time: 8:42 PM
 */

return [
    'sign_up' => [
        'release_token' => env('SIGN_UP_RELEASE_TOKEN'),
        'validation_rules' => [
            'name' => 'required',
            'email' => 'required|email',
            'password' => 'required',
            'user_type' => 'required'
        ]
    ],
    'login' => [
        'validation_rules' => [
            'email' => 'required|email',
            'password' => 'required'
        ]
    ],
    'forgot_password' => [
        'validation_rules' => [
            'email' => 'required|email'
        ]
    ],
    'reset_password' => [
        'release_token' => env('PASSWORD_RESET_RELEASE_TOKEN', false),
        'validation_rules' => [
            'token' => 'required',
            'email' => 'required|email',
            'password' => 'required|confirmed'
        ]
    ]
];