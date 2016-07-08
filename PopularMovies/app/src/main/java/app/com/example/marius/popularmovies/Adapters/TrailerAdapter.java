package app.com.example.marius.popularmovies.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.com.example.marius.popularmovies.R;

/**
 * Created by Marius on 7/2/2016.
 */
public class TrailerAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<ArrayList<String>> data;
    private LayoutInflater inflater;

    public TrailerAdapter(Context context, ArrayList<ArrayList<String>> data) {
        super(context, R.layout.trailers_item, data);

        this.context = context;
        this.data = data;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.trailers_item, parent, false);

            holder = new ViewHolder();
            holder.trailerIcon = (ImageView) convertView.findViewById(R.id.trailer_icon);
            holder.trailerNumber = (TextView) convertView.findViewById(R.id.trailer_number);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String content = "Trailer   " + (position + 1);
        holder.trailerNumber.setText(content);
        holder.trailerNumber.setTextColor(Color.parseColor("#9e6c5a"));

        Picasso.with(context)
                .load(R.drawable.play)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(holder.trailerIcon);

        return convertView;
    }

    public String getItemKey(int position) {
        return data.get(position).get(0);
    }

    static class ViewHolder {
        private TextView trailerNumber;
        private ImageView trailerIcon;
    }

}
