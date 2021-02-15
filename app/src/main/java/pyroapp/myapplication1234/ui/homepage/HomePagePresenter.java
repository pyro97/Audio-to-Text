package pyroapp.myapplication1234.ui.homepage;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import pyroapp.myapplication1234.util.Constants;

public class HomePagePresenter implements HomePageContract {
    private Context context;

    HomePagePresenter(Context context){
        this.context = context;
    }


    @Override
    public void initializeBanner() {
        MobileAds.initialize(context, Constants.BANNER);
    }

    @Override
    public InterstitialAd initializeInterstitial() {
        InterstitialAd mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(Constants.INTERSTITIAL);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        return mInterstitialAd;
    }
}
