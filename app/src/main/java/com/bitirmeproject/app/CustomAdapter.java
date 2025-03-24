package com.bitirmeproject.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    private List<Urun> urunList;
    private Context context;
    private String userkey;
    private String userfullname;
    private String username;

    public CustomAdapter(List<Urun> urunList, Context context) {
        this.urunList = urunList;
        this.context = context;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public void setUserfullname(String userfullname) {
        this.userfullname = userfullname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Urun urun = urunList.get(position);
        
        holder.textViewName.setText(urun.getUrun_adi());
        holder.textViewPrice.setText(urun.getUrun_fiyat() + " TL");
        //holder.textViewLocation.setText(urun.getUrun_lokasyon());

        // Load image using Glide
        String imageUrl = urun.getUrun_fotograf();
        Glide.with(context)
                .load(imageUrl)
                .into(holder.imageView);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            // Handle item click
            // You can navigate to detail page or show more information
        });
    }

    @Override
    public int getItemCount() {
        return urunList.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textViewName;
        private TextView textViewPrice;
        //private TextView textViewLocation;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.urunImage);
            textViewName = itemView.findViewById(R.id.textUrunAdi);
            textViewPrice = itemView.findViewById(R.id.textUrunFiyat);
            //textViewLocation = itemView.findViewById(R.id.recycler_row_location_text);
        }
    }
}