package com.ibrahim.popularmovies_stage1.data;

/**
 *
 * Created by ibrahim on 01/05/18.
 *
 */

public class Contract {
    private static final String KEY = "?api_key=";

    //TODO the key of api you have to put your own key
    public static final String API_KEY = KEY+"fa22ceab3172625817f5b2523e53ecd2";
    public static final String BAS_URL = "https://api.themoviedb.org/3/movie/";
    public static final String POPULAR_PART = "popular";
    public static final String TOP_RATED_PART = "top_rated ";

    //keyes of intent
    public static final String EXTRA_TITLE = "extra_title ";
    public static final String EXTRA_URL = "extra_url ";
    public static final String EXTRA_YEAR = "extra_year ";
    public static final String EXTRA_RATE = "extra_rate ";
    public static final String EXTRA_OVERVIEW = "extra_overview ";

    //the url of value of image view
    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String W185 = "w185";
    public static final String W500 = "w500";

}
