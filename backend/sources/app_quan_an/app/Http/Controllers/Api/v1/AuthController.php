<?php

namespace App\Http\Controllers\Api\v1;

use App\Models\Counter;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Config;
use Symfony\Component\HttpKernel\Exception\HttpException;
use Tymon\JWTAuth\JWTAuth;
use Tymon\JWTAuth\Exceptions\JWTException;
use App\Models\User;

class AuthController extends BaseController
{
    public function login(Request $request, JWTAuth $JWTAuth)
    {
        $credentials = $request->only(['email', 'password']);

        $validator = \Validator::make($credentials,  Config::get('auth_jwt.login.validation_rules'));

        $is_owner = $request->input('is_owner');
        $is_owner = $is_owner === 'true'? 1: 0;

        if ($validator->fails()) {
            return response()->json(['status' => 'error', 'msg' => "Wrong user name or password"]);
        }

        $credentials["user_type"] = $is_owner;

        try {
            $token = $JWTAuth->attempt($credentials);
            if(!$token) {
                return response()->json(['status' => 'error', 'msg' => "Account not exists"]);
            }
        } catch (JWTException $e) {
            throw new HttpException(500);
        }

        $user = Auth::user();

        $resp = [
            'status' => 'ok',
            'msg' => 'success',
            'token' => $token,
            'id' => $user->id,
            'name' => $user->name,
            'email' => $user->email
        ];

        if($is_owner == 1){
            $counter = Counter::where('owner_id', '=', $user->id)->first();
            if ($counter === null) {
                return response()->json(['status' => 'error', 'msg' => "No counter founded", "user_id" => $user->id]);
            }
            $resp["counter"] = $counter;
        }

        return response()->json($resp);
    }

    public function signUp(Request $request, JWTAuth $JWTAuth)
    {
        $data = $request->all();
        $validator = \Validator::make($data,  Config::get('auth_jwt.sign_up.validation_rules'));

        if ($validator->fails()) {
            return response()->json(['status' => 'error', 'msg' => "validation_failed"]);
        }

        $data['password'] = bcrypt($data['password']);
        $data['user_type'] = $data['user_type'] === 'true'? 1: 0;

        $user = new User($data);
        if(!$user->save()) {
            throw new HttpException(500);
        }
        if(!Config::get('auth_jwt.sign_up.release_token')) {
            return response()->json([
                'status' => 'ok'
            ], 201);
        }
        $token = $JWTAuth->fromUser($user);
        return response()->json([
            'status' => 'ok',
            'token' => $token,
            'id' => $user->id,
            'name' => $user->name,
            'email' => $user->email
        ], 201);
    }
}
