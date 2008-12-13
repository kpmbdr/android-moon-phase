package com.google.android.moon_phase;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MoonPhase extends Activity {
  private static final String TAG = "MoonPhase";
  private static String ICICLE_KEY = "MoonPhase";
  
  private Slider mSlider;
    
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);    
    
    setContentView(R.layout.main);
    mSlider = (Slider) findViewById(R.id.slider_view);
    
    if (savedInstanceState != null) {
      // We are being restored
      Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
      if (map != null) {        
        Log.i(TAG, "Resotring saved instance state.");
        mSlider.restoreState(map);
      }
    }    
  } 
  
  @Override
  public void onSaveInstanceState(Bundle outState) {
    Log.i(TAG, "Saving instance state.");    
    outState.putBundle(ICICLE_KEY, mSlider.saveState());
  }
  
  private boolean guessHemisphere() {
    LocationManager locationManager =
      (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    Location location =
        locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    
    if (location != null) {
      return location.getLongitude() > 0;
    }       
    
    return false;
  }
  
}