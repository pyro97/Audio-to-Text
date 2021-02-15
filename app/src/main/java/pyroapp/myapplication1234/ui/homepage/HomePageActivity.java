package pyroapp.myapplication1234.ui.homepage;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.google.android.gms.ads.InterstitialAd;

import pyroapp.myapplication1234.R;
import pyroapp.myapplication1234.util.Constants;

public class HomePageActivity extends AppCompatActivity {
    private FloatingActionButton convertButton,copyButton,shareButton;
    private InterstitialAd mInterstitialAd;
    private EditText convertedText;
    private ScheduledExecutorService scheduler;
    private boolean isVisible;
    private static HomePagePresenter mPresenter;
    private Vibrator vibe;
    private static final int RECOGNIZE_PERMISSION_CODE = 200;
    private static final int MS_VIBRATE = 100;
    private static final int DELAY = 30;
    private static final int PERIOD = 45;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new HomePagePresenter(this);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        convertedText = findViewById(R.id.editText2);
        convertButton= findViewById(R.id.convert);
        copyButton= findViewById(R.id.copy);
        shareButton= findViewById(R.id.button3);
        mPresenter.initializeBanner();
        mInterstitialAd = mPresenter.initializeInterstitial();

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault().getDisplayLanguage());
                try {
                    startVibration();
                    startActivityForResult(intent, RECOGNIZE_PERMISSION_CODE);
                } catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(), Constants.ERROR,Toast.LENGTH_SHORT).show();
                }
            }
        });

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVibration();
                if (convertedText.getText().length() > 0) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(Constants.COPIED, convertedText.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), Constants.COPIED, Toast.LENGTH_SHORT).show();
                } else if (convertedText.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), Constants.NO_TEXT, Toast.LENGTH_SHORT).show();
                }
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVibration();
                if (convertedText.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), Constants.NO_TEXT, Toast.LENGTH_SHORT).show();
                } else {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, convertedText.getText().toString());
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        isVisible = true;
        if(scheduler == null){
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (mInterstitialAd.isLoaded() && isVisible) {
                                mInterstitialAd.show();
                            }
                            mInterstitialAd = mPresenter.initializeInterstitial();
                        }
                    });
                }
            }, DELAY, PERIOD, TimeUnit.SECONDS);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        scheduler.shutdownNow();
        scheduler = null;
        isVisible =false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RECOGNIZE_PERMISSION_CODE){
            if(resultCode==RESULT_OK && data!=null){
                ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                convertedText.setText(result.get(0));
            }
        }
    }

    public void startVibration(){
        vibe.vibrate(MS_VIBRATE);
    }
}

