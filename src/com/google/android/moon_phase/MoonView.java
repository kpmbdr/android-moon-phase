package com.google.android.moon_phase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoonView extends LinearLayout {

  private static final String TAG = "MoonView";  
  private static final double MOON_PHASE_LENGTH = 29.530588853;
  private static final String DATE_FORMAT = "EEEE, MMMM d";

  private Calendar mCalendar;
  private boolean mIsNorthernHemi;
  private SimpleDateFormat dateFormat;
  
  private TextView mPhaseText;  
  private ImageView mMoonImage;
  private TextView mDateText;  
  
  public Animation nextInAnimation;  
  public Animation nextOutAnimation;  
  public Animation previousInAnimation;  
  public Animation previousOutAnimation;  
    
  public MoonView(Context context, AttributeSet attrs) {
    super(context, attrs);
    
    mCalendar = Calendar.getInstance();
    mIsNorthernHemi = true;    
    dateFormat = new SimpleDateFormat(DATE_FORMAT);    
    
    setOrientation(VERTICAL);

    LayoutParams layoutParams;
    
    mPhaseText = new TextView(context);
    layoutParams = 
      new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    mPhaseText.setGravity(Gravity.CENTER_HORIZONTAL);    
    addView(mPhaseText, layoutParams);
    
    mMoonImage = new ImageView(context);
    layoutParams = 
        new LayoutParams(LayoutParams.FILL_PARENT, 0);
    layoutParams.weight = 1.0f;   
    addView(mMoonImage, layoutParams);
    
    mDateText = new TextView(context);
    layoutParams = 
      new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    mDateText.setGravity(Gravity.CENTER_HORIZONTAL);    
    addView(mDateText, layoutParams);
  }
  
  private int getPhaseText(int phaseValue) {
    if (phaseValue == 0) {
      return R.string.new_moon;
    } else if (phaseValue > 0 && phaseValue < 7) {
      return R.string.waxing_crescent;            
    } else if (phaseValue == 7) {
      return R.string.first_quarter;
    } else if (phaseValue > 7 && phaseValue < 15) {
      return R.string.waxing_gibbous;            
    } else if (phaseValue == 15) {
      return R.string.full_moon;
    } else if (phaseValue > 15 && phaseValue < 23) {
      return R.string.waning_gibbous;            
    } else if (phaseValue == 23) {
      return R.string.third_quarter;      
    } else {
      return R.string.waning_crescent;      
    }
  }

  public void update(Calendar calendar, boolean isNorthernHemi) {
    mCalendar = calendar;
    mIsNorthernHemi = isNorthernHemi;
    
    double phase = computeMoonPhase();        
    Log.i(TAG, "Computed moon phase: " + phase);
    
    int phaseValue = ((int) Math.floor(phase)) % 30;
    Log.i(TAG, "Discrete phase value: " + phaseValue);
    
    mPhaseText.setText(getPhaseText(phaseValue));
    mMoonImage.setImageResource(IMAGE_LOOKUP[phaseValue]);
    mDateText.setText(dateFormat.format(mCalendar.getTime()));
    
    invalidate();
  }

  // Computes moon phase based upon Bradley E. Schaefer's moon phase algorithm.
  private double computeMoonPhase() {    
    int year = mCalendar.get(Calendar.YEAR);
    int month = mCalendar.get(Calendar.MONTH) + 1;
    int day = mCalendar.get(Calendar.DAY_OF_MONTH);
    
   // Convert the year into the format expected by the algorithm.
   double transformedYear = year - Math.floor((12 - month) / 10);
   Log.i(TAG, "transformedYear: " + transformedYear);
  
   // Convert the month into the format expected by the algorithm.
   int transformedMonth = month + 9;
   if (transformedMonth >= 12) {
     transformedMonth = transformedMonth - 12;
   }
   Log.i(TAG, "transformedMonth: " + transformedMonth);
  
   // Logic to compute moon phase as a fraction between 0 and 1
   double term1 = Math.floor(365.25 * (transformedYear + 4712));
   double term2 = Math.floor(30.6 * transformedMonth + 0.5);
   double term3 = Math.floor(Math.floor((transformedYear / 100) + 49) * 0.75) - 38;
   
   double intermediate = term1 + term2 + day + 59;
   if (intermediate > 2299160) {
     intermediate = intermediate - term3;
   }
   Log.i(TAG, "intermediate: " + intermediate);
   
   double normalizedPhase = (intermediate - 2451550.1) / MOON_PHASE_LENGTH;
   normalizedPhase = normalizedPhase - Math.floor(normalizedPhase);
   if (normalizedPhase < 0) {
     normalizedPhase = normalizedPhase + 1;
   }  
   Log.i(TAG, "normalizedPhase: " + normalizedPhase);
   
   // Return the result as a value between 0 and MOON_PHASE_LENGTH
   return normalizedPhase * MOON_PHASE_LENGTH;
  }
  
  private static final int [] IMAGE_LOOKUP = {
    R.drawable.moon0,
    R.drawable.moon1,
    R.drawable.moon2,
    R.drawable.moon3,
    R.drawable.moon4,
    R.drawable.moon5,
    R.drawable.moon6,
    R.drawable.moon7,
    R.drawable.moon8,
    R.drawable.moon9,
    R.drawable.moon10,
    R.drawable.moon11,
    R.drawable.moon12,
    R.drawable.moon13,
    R.drawable.moon14,
    R.drawable.moon15,
    R.drawable.moon16,
    R.drawable.moon17,
    R.drawable.moon18,
    R.drawable.moon19,    
    R.drawable.moon20,
    R.drawable.moon21,
    R.drawable.moon22,
    R.drawable.moon23,
    R.drawable.moon24,
    R.drawable.moon25,
    R.drawable.moon26,
    R.drawable.moon27,
    R.drawable.moon28,
    R.drawable.moon29,
  };
  
}