package com.example.bartender;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;


public class DrinkListAdapter extends RecyclerView.Adapter<DrinkListAdapter.DrinkViewHolder> {

    private LinkedList<String> mNameList;
    private LinkedList<Drawable> mImageList;
    private Intent mIntent;
    private LayoutInflater mInflator;

    DrinkListAdapter(Context context, LinkedList<String> names, LinkedList<Drawable> images) {
        mInflator = LayoutInflater.from(context);
        mNameList = names;
        mImageList = images;


    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mDrinkView = mInflator.inflate(R.layout.list_item, viewGroup, false);

        return new DrinkViewHolder(mDrinkView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, int i) {
        String mCurrent = mNameList.get(i);
        holder.name.setText(mCurrent);
        if (mImageList.size() > i) {
            Drawable mCurrent2 = mImageList.get(i);
            holder.image.setImageDrawable(mCurrent2);
        }


    }

    @Override
    public int getItemCount() {
        return mNameList.size();
    }

    public class DrinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView name;
        private ImageView image;
        final DrinkListAdapter iAdapter;

        private DrinkViewHolder(View mDrinkView, DrinkListAdapter drinkListAdapter) {
            super(mDrinkView);

            name = mDrinkView.findViewById(R.id.textView);
            image = mDrinkView.findViewById(R.id.imageView);
            this.iAdapter = drinkListAdapter;
            mDrinkView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int pos = getLayoutPosition();

            SingleItemClick(pos, view);
        }
    }

    private void SingleItemClick(int pos, View view) {

        mIntent = new Intent(view.getContext(), DrinkInformation.class);
        mIntent.putExtra("name", mNameList.get(pos));


        view.getContext().startActivity(mIntent);
    }


}

