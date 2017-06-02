<?php
/**
 * Created by PhpStorm.
 * User: thien
 * Date: 4/9/2017
 * Time: 8:39 PM
 */
namespace App\Validation\Auth;

use Dingo\Api\Http\FormRequest;
use Illuminate\Support\Facades\Config;

class LoginRequest extends FormRequest
{
    public function rules()
    {
        return Config::get('auth_jwt.login.validation_rules');
    }
    public function authorize()
    {
        return true;
    }
}