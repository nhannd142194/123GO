<?php

namespace App\Http\Controllers\Api\v1;

use App\Models\Article;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\File;

class ArticlesController extends BaseController
{
    protected $articles;

    public function index(Request $request)
    {
        $limit = (int)str_replace(' ', '', $request->input('limit'));
        $articles = Article::orderBy('created_at', 'desc')->limit($limit)->get();
        if(count($articles) < $limit){
            $last_page = "true";
        }
        else{
            $last_page = "false";
        }

        foreach ($articles as $a){
            if ($a->image != null) {
                $path = public_path() . "/img/news/" . $a->image;
            } else {
                $path = public_path() . "/img/news/default-news.jpg";
            }

            if(!File::exists($path)){
                $path = public_path() . "/img/news/default-news.jpg";
            }
            $data = file_get_contents($path);
            $a->image = base64_encode($data);
        }

        return response()->json(['status' => 'ok', 'msg' => 'success', 'news' => $articles, 'last_page' => $last_page]);
    }

    public function get_detail(Request $request)
    {
        $news_id = $request->input('news_id');
        $news = Article::where("id", "=", $news_id)->first();

        $news->image = $this->get_encode_file($news->image, "news");

        return response()->json(['status' => 'ok', 'msg' => 'success', 'news' => $news]);
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
