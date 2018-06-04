package br.com.fellipe.horadorango.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.fellipe.horadorango.PlaceActivity;
import br.com.fellipe.horadorango.R;
import br.com.fellipe.horadorango.model.Place;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fellipe on 27/05/18.
 */

public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Place> places;
    private AdapterView.OnItemClickListener clickListener;

    public FoodAdapter(List<Place> places) {
        this.places = places;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_food, parent, false);
        return new FoodHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position %2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#F8F8FF"));
        }

        final FoodHolder foodHolder = (FoodHolder) holder;
        final Place place = places.get(position);
        Picasso.get().load(place.getImage()).into(foodHolder.ivFood);
        foodHolder.tvPlaceName.setText(place.getName());
        foodHolder.tvKindOfFood.setText(place.getKindOfFood());
        foodHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PlaceActivity.class);
                intent.putExtra("PLACE", place);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void setClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    class FoodHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivFood)
        ImageView ivFood;

        @BindView(R.id.placeName)
        TextView tvPlaceName;

        @BindView(R.id.kindOfFood)
        TextView tvKindOfFood;

        public FoodHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}