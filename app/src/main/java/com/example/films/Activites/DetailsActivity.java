package com.example.films.Activites;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.films.Model.Movie;
import com.example.films.R;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {
    private Movie movie;
    private TextView movieTitleID_details,movieRatingID_details,movieReleaseID_details,movieOverview,movieRuntime,movieRevenue,movieBudget;
    private ImageView movieImageID_details;
    private RequestQueue queue;
    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        queue= Volley.newRequestQueue(this);

        movie=(Movie) getIntent().getSerializableExtra("movie"); // récupérer tous les éléments
        movieId=movie.getId();
        movieTitleID_details=findViewById(R.id.movieTitleID_details);
        movieRatingID_details=findViewById(R.id.movieRatingID_details);
        movieReleaseID_details=findViewById(R.id.movieReleaseID_details);
        movieImageID_details=findViewById(R.id.movieImageID_details);
        movieOverview=findViewById(R.id.movieOverview);
        movieRuntime=findViewById(R.id.movieRuntime);
        movieRevenue=findViewById(R.id.movieRevenue);
        movieBudget=findViewById(R.id.movieBudget);

        getMovieDetails(movieId);
    }

    private void getMovieDetails(String id)
    {
        String myUrl="https://advanced-movie-search.p.rapidapi.com/movies/getdetails?movie_id="+id;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    movieTitleID_details.setText(response.getString("title"));
                    movieReleaseID_details.setText("Release date: " + response.getString("release_date"));
                    movieRatingID_details.setText("Rating: " + response.getString("vote_average"));
                    movieOverview.setText("Overview: " + response.getString("overview"));
                    movieRuntime.setText("Runtime: " + response.getString("runtime") + " minutes");
                    movieRevenue.setText("Revenue: " + response.getString("revenue") + " $");
                    movieBudget.setText("Budget: " + response.getString("budget") + " $");

                    Picasso.get()
                            .load(response.getString("backdrop_path"))
                            .fit()
                            .into(movieImageID_details);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", "Err" + error);
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("x-rapidapi-host", "advanced-movie-search.p.rapidapi.com");
                params.put("x-rapidapi-key", "1858d533d7mshd70c6a61960e056p1cd534jsnb4575c0073b2");
                return params;
            }
        };

        ;
        queue.add(jsonObjectRequest);
    }
}