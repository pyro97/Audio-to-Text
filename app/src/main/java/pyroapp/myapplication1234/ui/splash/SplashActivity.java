package pyroapp.myapplication1234.ui.splash;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import pyroapp.myapplication1234.ui.homepage.HomePageActivity;
import pyroapp.myapplication1234.R;

public class SplashActivity extends AppCompatActivity {
        private TextView text;
        private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        text=findViewById(R.id.textView);
        img=findViewById(R.id.imageView);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        text.startAnimation(animation);
        img.startAnimation(animation);
        final Intent intent=new Intent(this, HomePageActivity.class);
        Thread timer=new Thread(){
            public void run(){
                try{
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();
                }
            }
        };
            timer.start();
    }
}
