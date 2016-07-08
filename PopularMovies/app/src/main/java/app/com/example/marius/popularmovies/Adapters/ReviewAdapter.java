package app.com.example.marius.popularmovies.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.com.example.marius.popularmovies.R;

/**
 * Created by Marius on 7/1/2016.
 */
public class ReviewAdapter extends ArrayAdapter {

    private Context context;
    private ArrayList<ArrayList<String>> data;
    private LayoutInflater inflater;


    public ReviewAdapter(Context context, ArrayList<ArrayList<String>> data) {
        super(context, R.layout.review_item, data);

        this.context = context;
        this.data = data;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.review_item, parent, false);

            holder = new ViewHolder();
            holder.author = (TextView) convertView.findViewById(R.id.author);
            holder.content = (TextView) convertView.findViewById(R.id.content);

            holder.author.setTextColor(Color.parseColor("#3917ab"));
            holder.content.setTextColor(Color.parseColor("#a7b074"));

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String author = data.get(position).get(0) + " " + context.getString(R.string.author);
        String content = data.get(position).get(1);
        holder.author.setText(author);
        holder.content.setText(content);


        return convertView;
    }

    static class ViewHolder {
        private TextView author, content;
    }
}
