package com.example.thien.a123go.app;

/**
 * Created by thien on 4/10/2017.
 */

public class AppConfig {
    public static String HOST = "http://10.0.3.2:8001";

    //Login url
    public static String URL_LOGIN = HOST + "/api/auth/login/";

    //Sign up url
    public static String URL_SIGNUP = HOST + "/api/auth/signup";

    //News url
    public static String URL_NEWS = HOST + "/api/article";

    //Latest food, counter
    public static String URL_LATEST_FOOD = HOST + "/api/latest_food";

    //Food of counter
    public static String URL_MENU = HOST + "/api/menu";

    //Create food
    public static String URL_ADD_FOOD = HOST + "/api/food/create";

    //Submit counter
    public static String URL_SUBMIT_COUNTER = HOST + "/api/counter/create";

    //Search food
    public static String URL_SEARCH_FOOD = HOST + "/api/food/search";

    //Get food detail
    public static String URL_FOOD_DETAIL = HOST + "/api/food/get_detail";

    //Get news detail
    public static String URL_NEWS_DETAIL = HOST + "/api/article/get_detail";

    //Get counter detail
    public static String URL_COUNTER_DETAIL = HOST + "/api/counter/get_detail";

    //Rate food
    public static String URL_FOOD_RATING = HOST + "/api/food/rating";

    //Comment food
    public static String URL_FOOD_COMMENT = HOST + "/api/food/comment";
}
