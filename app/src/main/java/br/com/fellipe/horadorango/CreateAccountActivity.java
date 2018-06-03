package br.com.fellipe.horadorango;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccountActivity extends AppCompatActivity {

    private final Integer FIRST_TIME = 0;

    private FirebaseAuth mAuth;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        mAuth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);
    }

    public void createAccount(View view) {
        final Context context = this;
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Login ou senha inválidos!",
                    Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(context, AddressActivity.class);
                                intent.putExtra("CREATE_ADRESS", FIRST_TIME);
                                intent.putExtra("USERNAME", etEmail.getText().toString());
                                startActivity(intent);
                            } else {
                                task.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(context, "Erro ao criar conta! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
        }
    }
}
