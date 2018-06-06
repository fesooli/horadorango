package br.com.fellipe.horadorango.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

import br.com.fellipe.horadorango.AddressActivity;
import br.com.fellipe.horadorango.BuildConfig;
import br.com.fellipe.horadorango.LoginActivity;
import br.com.fellipe.horadorango.R;

public class AboutFragment extends Fragment {

    private FirebaseAuth mAuth;
    private Button btLogout;
    private Button btUpdateAddress;

    public AboutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        mAuth = FirebaseAuth.getInstance();

        btLogout = (Button) view.findViewById(R.id.logout);
        btUpdateAddress = (Button) view.findViewById(R.id.updateAddress);

        btUpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddressActivity.class);
                intent.putExtra("CREATE_ADRESS", 1);
                startActivity(intent);
            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                FirebaseUser user = getCurrentUser();

                if(user == null) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    private FirebaseUser getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user : null;
    }
}
