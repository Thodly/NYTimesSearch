package com.example.thodlydugue.nytimessearch;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by thodlydugue on 7/27/2017.
 */

public class ArticleArrayAdapter extends ArrayAdapter<Article> {

    public ArticleArrayAdapter(Context context, List<Article> articles) {
        super(context, android.R.layout.simple_list_item_1, articles);

    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        //get the data item for position
        Article article = this.getItem(position);
        //check to see if existing view being reused
        //not using a recycled view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_article_result, parent, false);
        }
        //find the image view
        ImageView imageView = convertView.findViewById(R.id.ivImage);
        imageView.setImageResource(0);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvNewsDesk = (TextView) convertView.findViewById(R.id.tvNewsDesk);

        tvTitle.setText(article.getHeadline());
        if(!article.getNewsDesk().equals("null"))
            tvNewsDesk.setText(article.getNewsDesk());

        String thumbnail = article.getThumbNail();
        if (!TextUtils.isEmpty(thumbnail)) {
            Picasso.with(getContext()).load(thumbnail).placeholder(R.drawable.news).into(imageView);
        }else{
            //nothing
        }

        return convertView;

    }
}
