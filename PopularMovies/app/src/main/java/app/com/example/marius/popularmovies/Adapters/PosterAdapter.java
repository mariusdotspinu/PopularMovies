package app.com.example.marius.popularmovies.Adapters;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.com.example.marius.popularmovies.R;

/**
 * Created by Marius on 6/21/2016.
 */
public class PosterAdapter extends ArrayAdapter {
    private Context context;
    private String[] data;
    private LayoutInflater inflater;
    private final String IMAGE_BASE_PATH = "http://image.tmdb.org/t/p/w185/";

    public PosterAdapter(Context context, String[] data) {
        super(context, R.layout.poster_item, data);

        this.context = context;
        this.data = data;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.poster_item, parent, false);

            holder = new ViewHolder();
            holder.imgView = (ImageView) convertView.findViewById(R.id.poster_img);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso
                .with(context)
                .load(getPosterPathFromConventionString(data[position]))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.imgView);

        return convertView;
    }

    @Override
    public String getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return getIdFromConventionString(data[position]);
    }

    private String getPosterPathFromConventionString(String text) {

        int delimiters = 0;
        String resultString = "";
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '^') {
                delimiters++;
            }

            if (delimiters == 3) {
                resultString += text.charAt(i);
            }
            if (delimiters > 3) {
                break;
            }
        }


        return IMAGE_BASE_PATH + resultString.replace("^", "");

    }

    private Long getIdFromConventionString(String text) {
        String id = "";

        for (int i = text.length() - 1; i > 0; i--) {
            if (text.charAt(i) == '^') {
                break;
            } else {
                id += text.charAt(i);
            }
        }

        return Long.parseLong(id);
    }


    static class ViewHolder {
        private ImageView imgView;
    }
}
