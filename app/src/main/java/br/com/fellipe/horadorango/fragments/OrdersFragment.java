package br.com.fellipe.horadorango.fragments;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.fellipe.horadorango.R;
import br.com.fellipe.horadorango.adapters.OrderRecycleViewAdapter;
import br.com.fellipe.horadorango.dao.HoraDoRangoDatabase;
import br.com.fellipe.horadorango.dao.model.Order;
import br.com.fellipe.horadorango.dao.model.User;

import static br.com.fellipe.horadorango.dao.HoraDoRangoDatabase.DATABASE_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<Order> orders;
    private FirebaseAuth mAuth;

    public OrdersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle_orders, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.ordersList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAuth = FirebaseAuth.getInstance();
        DatabaseTask databaseTask = new DatabaseTask(this.getContext(), mAuth.getCurrentUser().getEmail());

        try {
            orders = databaseTask.execute().get();
            mRecyclerView.setAdapter(new OrderRecycleViewAdapter(this.getContext(), orders));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public class DatabaseTask extends AsyncTask<Void, Void, List<Order>> {
        private String email;
        private Context context;

        public DatabaseTask(Context context, String email) {
            this.email = email;
            this.context = context;
        }

        @Override
        protected List<Order> doInBackground(Void... voids) {
            HoraDoRangoDatabase database = Room
                    .databaseBuilder(
                            context,
                            HoraDoRangoDatabase.class,
                            DATABASE_NAME).build();
            User user = database.userDAO().findByUsername(email);
            return database.orderDAO().getOrdersByUser(user.getId());
        }
    }
}
