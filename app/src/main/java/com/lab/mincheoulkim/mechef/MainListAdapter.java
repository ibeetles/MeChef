package com.lab.mincheoulkim.mechef;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by mincheoulkim on 11/9/15.
 */
public class MainListAdapter  extends RecyclerView.Adapter<MainListAdapter.ViewHolder> {

    Context mContext;

    OnItemClickListener mItemClickListener;

    public MainListAdapter(Context context) {
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout dishHolder;
        public LinearLayout dishNameHolder;
        public TextView dishName;
        public ImageView dishImage;

        public ViewHolder(View itemView) {
            super(itemView);

            dishHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            dishName = (TextView) itemView.findViewById(R.id.dishName);
            dishNameHolder = (LinearLayout) itemView.findViewById(R.id.dishNameHolder);
            dishImage = (ImageView) itemView.findViewById(R.id.dishImage);

            dishHolder.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(itemView, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public int getItemCount() {
        return new DishData().dishList().size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_places, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Dish dish = new DishData().dishList().get(position);
        holder.dishName.setText(dish.name);
        Picasso.with(mContext).load(dish.getImageResourceId(mContext)).into(holder.dishImage);

        // dynamically adapting color to the dish name holder based on the colors of the image
        Bitmap photo = BitmapFactory.decodeResource(mContext.getResources(), dish.getImageResourceId(mContext));

        Palette.generateAsync(photo, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                int bgColor = palette.getMutedColor(mContext.getResources().getColor(android.R.color.black));
                holder.dishNameHolder.setBackgroundColor(bgColor);
            }
        });
    }
}
