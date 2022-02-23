package com.example.films.Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.films.Activites.DetailsActivity;
import com.example.films.Model.Movie;
import com.example.films.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder>
{
    private Context context;
    private List<Movie> movieList;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movieList)
    {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.film, parent, false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerViewAdapter.ViewHolder holder, int position)
    {
        Movie movie=movieList.get(position);
        String posterLink=movie.getPoster_path();
        holder.title.setText(movie.getTitle());
        holder.year.setText("Release date: " +movie.getRelease_date());
        holder.rating.setText("Rating: "+movie.getVote_average());

        Picasso.get()
                .load(posterLink)
                .fit()
                .placeholder(android.R.drawable.ic_btn_speak_now)
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();

    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title,year,rating;
        ImageView poster;

        public ViewHolder(@NonNull View itemView,Context ctx)
        {
            super(itemView);
            context=ctx;
            title=itemView.findViewById(R.id.movieTitleID);
            poster=itemView.findViewById(R.id.movieImageID);
            year=itemView.findViewById(R.id.movieReleaseID);
            rating=itemView.findViewById(R.id.movieRatingID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie=movieList.get(getAdapterPosition());
                    Intent intent=new Intent(context, DetailsActivity.class);
                    intent.putExtra("movie", movie);
                    ctx.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v)
        {

        }
    }

}
