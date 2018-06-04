package br.com.fellipe.horadorango.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.fellipe.horadorango.R;
import br.com.fellipe.horadorango.dao.model.Order;
import br.com.fellipe.horadorango.model.Menu;
import br.com.fellipe.horadorango.model.Place;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fellipe on 29/05/18.
 */

public class ListViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Menu> menuList;
    private Activity activity;
    private Place place;
    private Order order = new Order();

    public ListViewAdapter(List<Menu> menuList, Activity activity, Place place, Order order) {
        this.menuList = menuList;
        this.activity = activity;
        this.place = place;
        this.order = order;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_item, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position %2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#F8F8FF"));
        }
        final ItemHolder itemHolder = (ItemHolder) holder;
        final Menu menu = menuList.get(position);

        order.setPlaceId(place.getId());
        itemHolder.tvItemName.setText(menu.getItem());
        itemHolder.tvItemPrice.setText(menu.getPrice().toString());
        itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.setTotalPrice(order.getTotalPrice() != null ?
                        (order.getTotalPrice() + menu.getPrice()) : menu.getPrice());
                order.getItems().add(menu.getItem());
                Toast.makeText(activity, menu.getItem() + " adicionado ao carrinho", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return menuList.get(i).getId();
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemName)
        TextView tvItemName;

        @BindView(R.id.itemPrice)
        TextView tvItemPrice;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
