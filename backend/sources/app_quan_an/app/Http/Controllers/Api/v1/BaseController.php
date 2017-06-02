<?php

namespace App\Http\Controllers\Api\v1;

use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\Redirect;
use Illuminate\Support\Facades\View;
use Illuminate\Routing\Controller;

class BaseController extends Controller
{
    /**
     * Redirect to a route.
     *
     * @param $route
     * @param array $parameters
     * @param int   $status
     * @param array $headers
     *
     * @return mixed
     */
    public function redirect($route, $parameters = array(), $status = 302, $headers = array())
    {
        return Redirect::route('admin.'.$route, $parameters, $status, $headers);
    }

    /**
     * Get all input data.
     *
     * @return array
     */
    public function inputAll()
    {
        return Input::all();
    }
}
