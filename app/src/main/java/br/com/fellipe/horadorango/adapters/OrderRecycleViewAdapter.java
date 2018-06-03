package br.com.fellipe.horadorango.adapters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.fellipe.horadorango.R;
import br.com.fellipe.horadorango.api.PlacesApi;
import br.com.fellipe.horadorango.dao.HoraDoRangoDatabase;
import br.com.fellipe.horadorango.dao.model.Address;
import br.com.fellipe.horadorango.dao.model.Order;
import br.com.fellipe.horadorango.model.Place;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static br.com.fellipe.horadorango.dao.HoraDoRangoDatabase.DATABASE_NAME;

/**
 * Created by fellipe on 02/06/18.
 */

public class OrderRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Order> orders;

    public OrderRecycleViewAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_middle, parent, false);
        return new OrderHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final OrderHolder orderHolder = (OrderHolder) holder;
        final Order order = orders.get(position);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://horadorango.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PlacesApi service = retrofit.create(PlacesApi.class);
        final Call<Place> placeCall = service.getPlace(order.getPlaceId());
        placeCall.enqueue(new Callback<Place>() {
            @Override
            public void onResponse(Call<Place> call, Response<Place> response) {
                Place place = response.body();
                try {
                    orderHolder.tvPlaceName.setText(place.getName());
                } catch (Exception e) {
                    orderHolder.tvPlaceName.setText("Não Encontrado");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Place> call, Throwable t) {
                t.printStackTrace();
                call.request().url().toString();
            }
        });
        orderHolder.tvOrderPrice.setText("Total: R$" + order.getTotalPrice().toString());

        orderHolder.tvPaymentForm.setText("Forma de Pagamento: " + order.getPaymentForm());
        final DatabaseTask databaseTask = new DatabaseTask(context, order.getDeliveryAddressId());
        try {
            Address address = databaseTask.execute().get();
            orderHolder.tvAddress.setText("Endereço de Entrega: " + address.getStreet() + ", " + address.getNumber().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        for(String item : order.getItems()) {
            String items = orderHolder.tvOrderItems.getText().toString();
            orderHolder.tvOrderItems.setText(items + "1 " + item + "\n");
        }

        orderHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder
                        .setMessage("Deseja Excluir esse registro de compra?")
                        .setTitle("Registro de Compra");
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseTaskRemove databaseTaskRemove = new DatabaseTaskRemove(context, order);
                        try {
                            Long result = databaseTaskRemove.execute().get();
                            if(result == 0L) {
                                orders.remove(order);
                                notifyItemRemoved(position);
                                notifyItemChanged(position, orders.size());
                                Toast.makeText(context, "Registro Excluido com Sucesso", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Erro ao excluir o registro", Toast.LENGTH_SHORT).show();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        return;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return orders.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivOrderImage)
        ImageView ivOrderImage;

        @BindView(R.id.orderPrice)
        TextView tvOrderPrice;

        @BindView(R.id.paymentForm)
        TextView tvPaymentForm;

        @BindView(R.id.orderItems)
        TextView tvOrderItems;

        @BindView(R.id.address)
        TextView tvAddress;

        @BindView(R.id.placeName)
        TextView tvPlaceName;

        public OrderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public class DatabaseTask extends AsyncTask<Void, Void, Address> {
        private Integer addressId;
        private Context context;

        public DatabaseTask(Context context, Integer addressId) {
            this.addressId = addressId;
            this.context = context;
        }

        @Override
        protected Address doInBackground(Void... voids) {
            HoraDoRangoDatabase database = Room
                    .databaseBuilder(
                            context,
                            HoraDoRangoDatabase.class,
                            DATABASE_NAME).build();
            return database.addressDAO().findByAddressId(addressId);
        }
    }

    public class DatabaseTaskRemove extends AsyncTask<Void, Void, Long> {
        private Order order;
        private Context context;

        public DatabaseTaskRemove(Context context, Order order) {
            this.order = order;
            this.context = context;
        }

        @Override
        protected Long doInBackground(Void... voids) {
            HoraDoRangoDatabase database = Room
                    .databaseBuilder(
                            context,
                            HoraDoRangoDatabase.class,
                            DATABASE_NAME).build();
            database.orderDAO().delete(order);
            return 0L;
        }
    }
}
