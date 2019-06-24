package com.oldmonk.newsshots;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdapterNewsCard extends RecyclerView.Adapter<AdapterNewsCard.ViewHolderNewsCard> {

    //create class and parse it before only
    private ArrayList<News> articles;
    Context context;

    public  AdapterNewsCard(ArrayList<News> articles, Context context){
        this.articles = articles;
        this.context = context;
    }

    public static class ViewHolderNewsCard extends RecyclerView.ViewHolder{
        public ImageView ivThumbnail;
        public TextView tvHeading;
        CardView cardView;

        public ViewHolderNewsCard(View view){
            super(view);
            ivThumbnail = (ImageView) view.findViewById(R.id.iv_thumbnail);
            tvHeading = (TextView) view.findViewById(R.id.tv_heading);
            //tvContent = (TextView) view.findViewById(R.id.tv_content);
            cardView = (CardView)view.findViewById(R.id.card_view);
        }
    }
    @NonNull
    @Override
    public AdapterNewsCard.ViewHolderNewsCard onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_news_card, viewGroup,false);

        return new ViewHolderNewsCard(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterNewsCard.ViewHolderNewsCard viewHolder, int i) {
        //viewHolder.tvContent.setText("Heading "+i);
            final News currentArticle = articles.get(i);
            String title = currentArticle.getTitle();
            viewHolder.tvHeading.setText(title);
            String imageUrl = currentArticle.getUrlToImage();
            Glide.with(context)
                    .load(imageUrl)
                    .crossFade()
                    .into(viewHolder.ivThumbnail);

            viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentToWebView = new Intent(context, WebViewActivity.class);
                    intentToWebView.putExtra(AppConfig.JSON_URL_TO_NEWS_SOURCE, currentArticle.getUrlToSource());
                    context.startActivity(intentToWebView);
                }
            });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
