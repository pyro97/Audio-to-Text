package pyroapp.myapplication1234;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.MobileAds;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton button,button1,button3,rate;
    Button button2,button5;
    private InterstitialAd mInterstitialAd;
    String c;
    EditText t;
    private ScheduledExecutorService scheduler;
    private boolean isVisible;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        t=(EditText) findViewById(R.id.editText2);
        MobileAds.initialize(this, "ca-app-pub-9751551150368721~8995780164");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9751551150368721/3787685515");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        button= findViewById(R.id.convert); //conversione audio-testo
        button1= findViewById(R.id.copy);  // copia
        button3= findViewById(R.id.button3); //condividi testo fuori dall'app




    //pulsante che traduce voce in testo
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault().getDisplayLanguage());
                try {

                    vibe.vibrate(100);
                    startActivityForResult(intent, 200);
                }
                   catch (ActivityNotFoundException a){
                    Toast.makeText(getApplicationContext(),"ERROR",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //pulsante che permette di copiare testo presente in EditText t
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vibe.vibrate(100);
                    if (t.getText().length() > 0) {

                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copiato", t.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getApplicationContext(), "Copied", Toast.LENGTH_SHORT).show();

                    } else if (t.getText().length() == 0) {

                        Toast.makeText(getApplicationContext(), "No Text", Toast.LENGTH_SHORT).show();

                    }

            }
        });

        //pulsante che permette di condividere testo presente in EditText t
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                vibe.vibrate(100);
                if (t.getText().length() == 0) {

                    Toast.makeText(getApplicationContext(), "No Text", Toast.LENGTH_SHORT).show();

                }
                    else {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, t.getText().toString());
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
                            mInterstitialAd = new InterstitialAd(MainActivity.this);
                            mInterstitialAd.setAdUnitId("ca-app-pub-9751551150368721/3787685515");
                            mInterstitialAd.loadAd(new AdRequest.Builder().build());
                        }
                    });
                }
            }, 30, 45, TimeUnit.SECONDS);

        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        scheduler.shutdownNow();
        scheduler = null;
        isVisible =false;
    }



// funzione che converte audio memorizzato in testo e assegnarlo all'EditText
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==200){
            if(resultCode==RESULT_OK && data!=null){


                ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                t.setText(result.get(0));

            }
        }
    }


}

