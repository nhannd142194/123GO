<?php
/**
 * Created by PhpStorm.
 * User: thien
 * Date: 12/15/2016
 * Time: 8:45 PM
 */

return [
    'food_rules' => [
        'name' => 'required',
        'price' => 'required|numeric',
        'discount' => 'required|numeric|min:0|max:100',
        'description' => 'required',
        'counter_id' => 'required|numeric',
    ],
    'counter_rules' => [
        'owner_id' => 'required|numeric',
        'name' => 'required',
        'address' => 'required',
        'phone' => 'required|numeric',
        'description' => 'required',
    ]
];