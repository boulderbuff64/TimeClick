package com.Erickson.TimeClick;

import java.util.HashMap;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class MyApp extends Application {
	
   public enum TrackerName {
       APP_TRACKER,
       GLOBAL_TRACKER,
       E_COMMERCE_TRACKER,
   }

   HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

  /* synchronized Tracker getTracker(TrackerName trackerId,Context context) {
   	 Log.d("getTracker()", "Tracker");
       if (!mTrackers.containsKey(trackerId)) {

           GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
           Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.id.massButton) //massbutton ok? 
                   : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
                   : analytics.newTracker(R.xml.ecommerce_tracker);
           mTrackers.put(trackerId, t);
           Log.d("getTracker()", "setting mTrackers.containsKey(trackerId)");
       }
       else { Log.d("getTracker()", "else"); }
       Log.d("getTracker()", "mTrackers="+String.valueOf(mTrackers.get(trackerId)));
       
       return mTrackers.get(trackerId);
   }*/
   synchronized Tracker getTracker(TrackerName trackerId) {
	    if (!mTrackers.containsKey(trackerId)) {

	      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
	      Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker("UA-56888204-7")
	          : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
	              : analytics.newTracker(R.xml.ecommerce_tracker);
	      mTrackers.put(trackerId, t);

	    }
	    return mTrackers.get(trackerId);
	  }
}
