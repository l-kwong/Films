package com.example.films.Activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.films.Data.MovieRecyclerViewAdapter;
import com.example.films.Model.Movie;
import com.example.films.R;
import com.example.films.Util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue requestQueue;
    String searchTerm="Kong";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this.recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        movieList=new ArrayList<>();
        // chercher les préferences de la dernière recherche
        Prefs prefs=new Prefs(MainActivity.this);
        String search=prefs.getSearch();

        movieList=getMovies(search);
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);

        movieRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recherche, menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setQueryHint("Écrire le nom de la film");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTerm=query;
                getMovies(searchTerm);
                // gestion du clavier
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(getCurrentFocus()!=null)
                {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public List<Movie> getMovies(String searchTerm)
    {
        movieList.clear();
        String myUrl="https://advanced-movie-search.p.rapidapi.com/search/movie?query="+ searchTerm;
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray movieArray = response.getJSONArray("results");
                    for (int i = 0; i < movieArray.length(); i++) {
                        JSONObject movieObj = movieArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setTitle(movieObj.getString("title"));
                        movie.setRelease_date(movieObj.getString("release_date"));
                        movie.setVote_average(movieObj.getString("vote_average"));
                        movie.setId(movieObj.getString("id"));
                        movie.setPoster_path(movieObj.getString("poster_path"));
                        // Log.d("Movies =", movie.getTitle());
                        movieList.add(movie);
                    }
                   movieRecyclerViewAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("Erreur", "Err"+error);
            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params=new HashMap<>();
                params.put("x-rapidapi-host", "advanced-movie-search.p.rapidapi.com");
                params.put("x-rapidapi-key", "1858d533d7mshd70c6a61960e056p1cd534jsnb4575c0073b2");
                return params;
        }};

        ;
        requestQueue.add(jsonObjectRequest);
        return movieList;
    }
}