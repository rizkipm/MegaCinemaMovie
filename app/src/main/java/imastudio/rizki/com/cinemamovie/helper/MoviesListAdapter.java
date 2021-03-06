package imastudio.rizki.com.cinemamovie.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import imastudio.rizki.com.cinemamovie.R;


public class MoviesListAdapter extends ArrayAdapter<MovieList> {
    private final String LOG_TAG = MoviesListAdapter.class.getSimpleName();

    public MoviesListAdapter(Context context, int resources) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, resources);
    }


    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {

        MovieList moviesList = getItem(position);


        if ( convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_item, parent, false);

        }
        ImageView posterView = (ImageView) convertView.findViewById(R.id.flavor_image);
      // using the picasso library converting the image into gridview of images
       Log.v(LOG_TAG,"url "+ moviesList.getImageurl());
        Picasso.with(getContext()).load(moviesList.getImageurl()).placeholder(R.drawable.square_placeholder).fit().into(posterView);
        //Glide.with(getContext()).load(moviesList.getImageurl()).crossFade().into(posterView);


        return convertView;
    }
}
