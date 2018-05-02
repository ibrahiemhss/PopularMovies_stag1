package com.ibrahim.popularmovies_stage1.utilities;

import com.ibrahim.popularmovies_stage1.model.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * Created by *i*brahim on 28/04/18.
 */
public class OpenMoviesUtils {

    public static ArrayList<Movies> getMovies(String moviesJsonStr)

            throws JSONException {
        //  final String TOTAL_ٌُPAGES = "total_pages";
        Movies movies;
        ArrayList<Movies> moviesArrayList = new ArrayList<>();

        /* information. Each Movies info is an element of the "results" array */
        final String RESULTS = "results";

       /*all  childrens  objects in "results" array */
        // final String VOTE_COUNT = "vote_count";
        final String VOTE_AVERAGE = "vote_average";
        final String TITLE = "title";
        final String POSTER_PATHE = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";

        /* String array to hold each ellements String */
        JSONObject forecastJson = new JSONObject(moviesJsonStr);
        //get all data inside JSONObject forecastJson
        JSONArray moviesArrayResults = forecastJson.getJSONArray(RESULTS);
        //get all position of array-->  parsedMoviesData that come from JSONArray-->  moviesArrayResults
        for (int i = 0; i < moviesArrayResults.length(); i++) {
            /* These are the values that will be collected */

            movies = new Movies();
            //    long voteCount;
            long voteAverage;
            String title;
            String posterPath;
            String overview;
            String releasDate;

            /* Get the JSON object representing the results */
            JSONObject objResult = moviesArrayResults.getJSONObject(i);
            /* Get the JSON object representing the--> ...vote_average... from -->JSONObject(results)*/
            voteAverage = objResult.optLong(VOTE_AVERAGE);
            /* Get the JSON object representing the -->...title... from--> JSONObject(results)*/
            title = objResult.optString(TITLE);
            /* Get the JSON object representing the -->...poster_path... from -->JSONObject(results)*/
            posterPath = objResult.optString(POSTER_PATHE);
            /* Get the JSON object representing the -->....overview.... from--> JSONObject(results)*/
            overview = objResult.optString(OVERVIEW);
            /* Get the JSON object representing the -->....release_date.... from--> JSONObject(results)*/
            releasDate = objResult.optString(RELEASE_DATE);
            movies.setVotAverage(voteAverage);
            movies.setTilte(title);
            movies.setPoster_path(posterPath);
            movies.setOverview(overview);
            movies.setRelease_date(releasDate);


            moviesArrayList.add(movies);

        }

        return moviesArrayList;
    }
}
