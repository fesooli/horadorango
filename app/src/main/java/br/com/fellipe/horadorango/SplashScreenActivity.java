package br.com.fellipe.horadorango;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 3500;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startAnimation();
    }

    private void startAnimation() {
        Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.anim);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        if (iv != null) {
            iv.clearAnimation();
            iv.startAnimation(anim);
        }

        final Context context = this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                }
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private FirebaseUser getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null ? user : null;
    }

}
