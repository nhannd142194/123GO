<?php

namespace App\Http\Controllers\Api\v1;

use App\Models\Article;
use App\Models\Counter;
use App\Models\Food;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\File;
use Intervention\Image\Facades\Image;
use Mockery\CountValidator\Exception;

class CountersController extends BaseController
{
    public function get(Request $request)
    {
        $counter_id = $request->input('counter_id');
        $counter = Counter::where("id", '=', $counter_id)->first();

        $counter->image = $this->get_encode_file($counter->image, "counter");

        return response()->json(['status' => 'ok', 'msg' => 'success', 'food' => $counter]);
    }

    public function store(Request $request)
    {
        $data = $request->only(['owner_id', 'name', 'address', 'phone', 'description']);
        $validator = \Validator::make($data,  Config::get('custom.counter_rules'));

        if ($validator->fails()) {
            return response()->json(['status' => 'error', 'msg' => "Validation failed"]);
        }

        $image_string = $request->get('image', '');
        if($image_string != ''){
            $image = base64_decode($image_string);
            $file_name = "image-".time().".jpg";
            $path = public_path() . "/img/counter/" . $file_name;

            file_put_contents($path, $image);
            $data['image'] = $file_name;
        }
        else{
            $data['image'] = "default-counter.jpg";
        }

        $data["type"] = 1;

        Counter::updateOrCreate(['owner_id' => $data['owner_id']], $data);
        
        return response()->json([
            'status' => 'ok'
        ], 201);
    }

    public function get_detail(Request $request)
    {
        $counter_id = $request->input('counter_id');
        $counter = Counter::where("id", "=", $counter_id)->first();

        $counter->image = $this->get_encode_file($counter->image, "counter");

        return response()->json(['status' => 'ok', 'msg' => 'success', 'counter' => $counter]);
    }

    public function get_encode_file($file_name, $type){
        if ($file_name != null) {
            $path = public_path() . "/img/".$type."/" . $file_name;
        } else {
            $path = public_path() . "/img/".$type."/default-".$type.".jpg";
        }

        if(!File::exists($path)){
            $path = public_path() . "/img/".$type."/default-".$type.".jpg";
        }
        $data = file_get_contents($path);
        return base64_encode($data);
    }
}
