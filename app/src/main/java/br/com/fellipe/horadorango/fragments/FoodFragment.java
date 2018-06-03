package br.com.fellipe.horadorango.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.fellipe.horadorango.R;
import br.com.fellipe.horadorango.adapters.FoodAdapter;
import br.com.fellipe.horadorango.api.PlacesApi;
import br.com.fellipe.horadorango.model.Place;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FoodAdapter foodAdapter;

    public FoodFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

        final Context context = this.getContext();

        if(foodAdapter == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://horadorango.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            PlacesApi service = retrofit.create(PlacesApi.class);
            final Call<List<Place>> places = service.listPlaces();
            places.enqueue(new Callback<List<Place>>() {
                @Override
                public void onResponse(Call<List<Place>> call, Response<List<Place>> response) {
                    List<Place> pl = response.body();
                    foodAdapter = new FoodAdapter(pl);
                    mLayoutManager = new LinearLayoutManager(context);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(foodAdapter);
                }

                @Override
                public void onFailure(Call<List<Place>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
        }
        return view;
    }

}
