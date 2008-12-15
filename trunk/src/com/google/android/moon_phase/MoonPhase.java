package com.google.android.moon_phase;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

public class MoonPhase extends Activity {
  private static final String TAG = "MoonPhase";
  private static String ICICLE_KEY = "MoonPhase";
  
  private MainView mMainView;
    
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);    
    
    setContentView(R.layout.main);
    mMainView = (MainView) findViewById(R.id.main_view);
    
    if (savedInstanceState != null) {
      // We are being restored
      Bundle map = savedInstanceState.getBundle(ICICLE_KEY);
      if (map != null) {        
        Log.i(TAG, "Resotring saved instance state.");
        mMainView.restoreState(map);
      }
    } else {
      mMainView.setNorthernHemi(isNorthernHemi());      
      mMainView.update();
    }
  } 
  
  @Override
  public void onSaveInstanceState(Bundle outState) {
    Log.i(TAG, "Saving instance state.");    
    outState.putBundle(ICICLE_KEY, mMainView.saveState());
  }
  
  private static final int MENUITEM_TODAY_ID = 0;
  private static final int MENUITEM_CHOOSE_DATE_ID = 1;
    
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    
    MenuItem item;    
    item = menu.add(0, MENUITEM_TODAY_ID, 0,
        R.string.today);           
    item.setIcon(android.R.drawable.ic_menu_today);
    item = menu.add(0, MENUITEM_CHOOSE_DATE_ID, 0,
        R.string.choose_date);           
    item.setIcon(android.R.drawable.ic_menu_month);
        
    return true;
  }    
  
  static final int DATE_DIALOG_ID = 1;
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item){
    switch (item.getItemId()) {
      case MENUITEM_TODAY_ID:
        mMainView.setDate(Calendar.getInstance());
        return true;
      case MENUITEM_CHOOSE_DATE_ID:
        showDialog(DATE_DIALOG_ID);        
        return true;        
    }

    return super.onOptionsItemSelected(item);
  }  
  
  @Override
  protected Dialog onCreateDialog(int id) {
    switch (id) {
      case DATE_DIALOG_ID:
        Calendar date = mMainView.getDate();
        return new DatePickerDialog(this,
            mDateSetListener,
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH));
    }
    
    return null;
  }

  @Override
  protected void onPrepareDialog(int id, Dialog dialog) {
    switch (id) {
      case DATE_DIALOG_ID:
        Calendar date = mMainView.getDate();
        ((DatePickerDialog) dialog).updateDate(date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH));
        break;
    }
  }     
 
  private DatePickerDialog.OnDateSetListener mDateSetListener =
    new DatePickerDialog.OnDateSetListener() {
      public void onDateSet(DatePicker view, int year, int monthOfYear,
          int dayOfMonth) {
        Calendar date = Calendar.getInstance();
        date.set(year, monthOfYear, dayOfMonth);
        mMainView.setDate(date);
      }
    };
 
  private boolean isNorthernHemi() {
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