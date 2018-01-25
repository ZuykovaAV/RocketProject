package com.zuykova.na.rocketproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RocketListActivity extends AppCompatActivity {
    private static final String TAG = "RocketListActivity";

    private RecyclerView mRocketRecyclerView;
    private List<Rocket> mItems = new ArrayList<>();
    private IconDownloader<RocketHolder> mIconDownloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_list);

        mRocketRecyclerView = findViewById(R.id.rocket_recycler_view);
        mRocketRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupAdapter();

        new FetchItemsTask().execute();

        Handler responseHandler = new Handler();
        mIconDownloader = new IconDownloader<>(responseHandler);
        mIconDownloader.setIconDownloaderListener(
                new IconDownloader.IconDownloaderListener<RocketHolder>() {
                    @Override
                    public void onIconDownloaded(RocketHolder rocketHolder, Bitmap bitmap) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        rocketHolder.bindDrawable(drawable);
                    }
            }
        );

        mIconDownloader.start();
        mIconDownloader.getLooper();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mIconDownloader.clearQueue();
        mIconDownloader.quit();
    }

    private void setupAdapter() {
        mRocketRecyclerView.setAdapter(new RocketAdapter(mItems));
    }

    private class RocketHolder extends RecyclerView.ViewHolder
                                                implements View.OnClickListener{
        private Rocket mRocket;

        private TextView mNameTextView;
        private TextView mTimeTextView;
        private ImageView mIconImageView;
        private TextView mDescTextView;

        public RocketHolder(View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.rocket_name);
            mTimeTextView = itemView.findViewById(R.id.rocket_time);
            mIconImageView = itemView.findViewById(R.id.rocket_icon);
            mDescTextView = itemView.findViewById(R.id.rocket_description);
            itemView.setOnClickListener(this);
        }

        public void bind(Rocket item) {
            mRocket = item;
            mNameTextView.setText(item.getRocketName());
            mTimeTextView.setText(unixToStringDate(item.getRocketTime()));
            mDescTextView.setText(item.getRocketDesc());
        }

        private String unixToStringDate(long unix){
            Date date = new Date(unix*1000L); //получаем миллисекунды
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd \nHH:mm:ss z");
            return sdf.format(date);
        }

        public void bindDrawable(Drawable drawable) {
            mIconImageView.setImageDrawable(drawable);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mRocket.getArticleLink()));
            startActivity(intent);
        }
    }

    private class RocketAdapter extends RecyclerView.Adapter<RocketHolder> {
        private List<Rocket> mRockets;

        public RocketAdapter(List<Rocket> rockets) {
            mRockets = rockets;
        }

        @Override
        public RocketHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.list_item_rocket, parent, false);
            return new RocketHolder(view);
        }

        @Override
        public void onBindViewHolder(RocketHolder holder, int position) {
            Rocket rocket = mRockets.get(position);
            holder.bind(rocket);
            mIconDownloader.queueIcon(holder, rocket.getRocketIcon());
        }

        @Override
        public int getItemCount() {
            return mRockets.size();
        }
    }

    private class FetchItemsTask extends AsyncTask<Void,Void,List<Rocket>>{

        @Override
        protected List<Rocket> doInBackground(Void... params) {
            new RocketFetchr().fetchItems();
            return new RocketFetchr().fetchItems();
        }

        @Override
        protected void onPostExecute(List<Rocket> items) {
            mItems = items;
            setupAdapter();
        }
    }
}
