<?php

namespace App\Http\Controllers\Api\v1;

use App\Models\Article;
use App\Models\Counter;
use App\Models\Food;
use App\Models\Rating;
use App\Models\User;
use App\Models\Comment;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Config;
use Illuminate\Support\Facades\File;
use Intervention\Image\Facades\Image;
use Mockery\CountValidator\Exception;

class FoodsController extends BaseController
{
    public function index(Request $request)
    {
        $food = Food::orderBy('created_at', 'desc')->limit(20)->get();
        $counter = Counter::orderBy('created_at', 'desc')->limit(20)->get();

        foreach ($food as $f){
            $f->image = $this->get_encode_file($f->image, "food");
        }

        foreach ($counter as $c){
            $c->image = $this->get_encode_file($c->image, "counter");
        }

        return response()->json(['status' => 'ok', 'msg' => 'success', 'food' => $food, 'counter' => $counter]);
    }

    public function get(Request $request)
    {
        $counter_id = $request->input('counter_id');
        $counter = Counter::where("id", '=', $counter_id)->first();
        $food = Food::where("counter_id", "=", $counter->id)->get();

        foreach ($food as $f){
            $f->image = $this->get_encode_file($f->image, "food");
        }
        
        return response()->json(['status' => 'ok', 'msg' => 'success', 'food' => $food]);
    }

    public function get_detail(Request $request)
    {
        $food_id = $request->input('food_id');
        $food = Food::where("id", "=", $food_id)->first();

        $food->image = $this->get_encode_file($food->image, "food");

        $rating = Rating::where("food_id", "=", $food_id);
        $food->rating_count = $rating->count();
        $food->rating_score = $rating->avg('score');
        
        if($food->rating_count == null){
            $food->rating_count = 0;
        }

        if($food->rating_score == null){
            $food->rating_score = 0;
        }

        $comments = Comment::where("food_id", "=", $food_id)->get();
        foreach ($comments as $comment){
            $user = User::where("id", "=", $comment->user_id)->first();
            if($user != null){
                $comment->user_name = $user->name;
            }
            else{
                $comment->user_name = '';
            }
        }
        if ($comments == null){
            $comments = [];
        }

        return response()->json(['status' => 'ok', 'msg' => 'success', 'food' => $food, 'comments' => $comments]);
    }

    public function store(Request $request)
    {
        $data = $request->only(['name', 'price', 'discount', 'description', 'counter_id']);
        $validator = \Validator::make($data,  Config::get('custom.food_rules'));

        if ($validator->fails()) {
            return response()->json(['status' => 'error', 'msg' => "Validation failed"]);
        }

        $image_string = $request->get('image', '');
        if($image_string != ''){
            $image = base64_decode($image_string);
            $file_name = "image-".time().".jpg";
            $path = public_path() . "/img/food/" . $file_name;

            file_put_contents($path, $image);
            $data['image'] = $file_name;
        }
        else{
            $data['image'] = "default-food.jpg";
        }

        $data["type"] = 1;

        $food = new Food($data);
        if(!$food->save()) {
            throw new HttpException(500);
        }
        
        return response()->json([
            'status' => 'ok'
        ], 201);
    }

    public function search(Request $request)
    {
        $food_name = $request->input('food_name');
        $food = Food::where("name", "like", "%{$food_name}%")->limit(30)->get();

        foreach ($food as $f){
            $f->image = $this->get_encode_file($f->image, "food");
        }

        return response()->json(['status' => 'ok', 'msg' => 'success', 'food' => $food]);
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

    public function rating(Request $request)
    {
        $user_id = $request->input('user_id');
        $food_id = $request->input('food_id');
        $score = $request->input('score');

        $rating = Rating::where('user_id', '=', $user_id)->where('food_id', '=', $food_id)->first();
        if ($rating === null) {
            $data = $request->only(['user_id', 'food_id', 'score']);
            $rating = new Rating($data);
            if (!$rating->save()) {
                throw new HttpException(500);
            }
        } else {
            $rating->score = $score;
            $rating->save();
        }
        
        return response()->json(['status' => 'ok', 'msg' => 'success']);
    }

    public function comment(Request $request)
    {
        $data = $request->only(['user_id', 'food_id', 'comment']);
        $rating = new Comment($data);
        if (!$rating->save()) {
            throw new HttpException(500);
        }

        return response()->json(['status' => 'ok', 'msg' => 'success']);
    }
}
