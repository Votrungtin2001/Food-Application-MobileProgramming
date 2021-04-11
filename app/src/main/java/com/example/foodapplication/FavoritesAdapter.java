package com.example.foodapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {
    private List<FavRestaurant> lFavorites;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtFavName, txtFavDistance, txtFavTime;

        public ViewHolder(View favView) {
            super(favView);

            txtFavName = (TextView) favView.findViewById(R.id.txtFavName);
            txtFavDistance = (TextView) favView.findViewById(R.id.txtFavDistance);
            txtFavTime = (TextView) favView.findViewById(R.id.txtFavTime);
        }
    }

    public FavoritesAdapter(List<FavRestaurant> lFavorites) { this.lFavorites = lFavorites; }

    @Override
    public FavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View FavoritesView = inflater.inflate(R.layout.favorites_entry_layout, parent, false);
        return new ViewHolder(FavoritesView);
    }

    @Override
    public void onBindViewHolder(FavoritesAdapter.ViewHolder holder, int pos) {
        FavRestaurant favorite = lFavorites.get(pos);

        holder.txtFavName.setText(favorite.getName());
        holder.txtFavDistance.setText(favorite.getDistance() + "km");
        holder.txtFavTime.setText(favorite.getETA() + "min.");
    }

    @Override
    public int getItemCount() { return lFavorites.size(); }
}

