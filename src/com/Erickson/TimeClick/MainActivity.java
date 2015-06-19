package com.Erickson.TimeClick;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.Erickson.TimeClick.MyApp.TrackerName;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class MainActivity extends Activity implements OnClickListener {

	PaintSurface mPaintSurface;
	Draw drawView;
	private InterstitialAd fullAd;
	//private PublisherInterstitialAd mPublisherInterstitialAd;
	AdView adView;
	//AdRequest adRequest;
	private Button[] Button = new Button[8];
	private Button[] Button2 = new Button[8];
	private Button bonus;
	private Button reset;
	private Button buy1;
	int[] buttonCount = new int[8];
	int[] button2Count = new int[8];
	String userID = "1";
	int temp = 1;
	int clickCount = 0;
	int bonusClick = 0;
	int split = 0;
	Calendar calendar;
	int versionCode = 0;
	long firstInstall = 0;
	static int[] cost = new int[8];
	static int[] upgrade = new int[8];
	static final int[] Bcost = {R.id.b1, R.id.b2, R.id.b3, R.id.b4, R.id.b5, R.id.b6, R.id.b7, R.id.b8};
	static final int[] Bupgrade = {R.id.c1, R.id.c2, R.id.c3, R.id.c4, R.id.c5, R.id.c6, R.id.c7, R.id.c8};
	String[] Config = new String[10];

	//private GoogleAnalyticsTracker tracker;

	EasyTracker easyTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		easyTracker = EasyTracker.getInstance(MainActivity.this); //Google Anaytics 
		mPaintSurface = (PaintSurface) findViewById(R.id.paint_surface); // Getting reference to the PaintView R.layout.activity_main
		//adView = (AdView) this.findViewById(R.id.adView);
		mPaintSurface.setOnTouchListener(mPaintSurface);

		//mPublisherInterstitialAd = new PublisherInterstitialAd(this);
		//mPublisherInterstitialAd.setAdUnitId("ca-app-pub-3782358297147464/7635179890");
		//requestNewInterstitial();

		/*
		 * mPublisherInterstitialAd.setAdListener(new AdListener() {
		 * 
		 * @Override public void onAdClosed() { requestNewInterstitial();
		 * //beginPlayingGame(); } });
		 */

		bonus = (Button) findViewById(R.id.bonus);
		reset = (Button) findViewById(R.id.b0);
		buy1 = (Button) findViewById(R.id.buy1);
		bonus.setOnClickListener(this);
		reset.setOnClickListener(this);
		buy1.setOnClickListener(this);

		cost[0] = 1;
		cost[1] = 5;
		cost[2] = 25;
		cost[3] = 100;
		cost[4] = 500;
		cost[5] = 2000;
		cost[6] = 10000;
		cost[7] = 50000;
		for (int i = 0; i < 8; i++) {
			Button[i] = (Button) findViewById(Bcost[i]);
			Button[i].setOnClickListener(this);
			Button2[i] = (Button) findViewById(Bupgrade[i]);
			Button2[i].setOnClickListener(this);
			upgrade[i] = 5 * cost[i];
			Button2[i].setText("$" + printValueDec(upgrade[i]));
		}

		//Log.d("onCreate()", "Buttons2");

		drawView = new Draw(this);
		drawView.setBackgroundColor(Color.BLACK);
		// drawView.setOnTouchListener(drawView); // Setting the touch listener
		// setContentView(drawView);

		try {
			Log.d("onCreate()", "try");
			userID = returnID();
			int splitNum = (int) (2 * Math.random());
			if (splitNum == 0) {
				Log.d("onCreate()", "0");
				split = 1;
				//setContentView(R.layout.activity_main_split); 
			} else {
				Log.d("onCreate()", "1");
				split = 0;
				//setContentView(R.layout.activity_main); 
			}
			Log.d("userID", "done");
		} catch (IOException e) {
			Log.d("onCreate()", "catch");
			split = 0;
			e.printStackTrace();
		}

		// Prepare the Interstitial Ad
		fullAd = new InterstitialAd(MainActivity.this);
		// Insert the Ad Unit ID
		fullAd.setAdUnitId("ca-app-pub-3782358297147464/7635179890");

		// Locate the Banner Ad in activity_main.xml

		// Request for Ads
		AdRequest adRequest = new AdRequest.Builder()
		//adRequest = new AdRequest.Builder()

				// Add a test device to show Test Ads
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("").build();

		// Load ads into Banner Ads
		//adView.loadAd(adRequest);

		// Load ads into Interstitial Ads
		//fullAd.loadAd(adRequest);

		// Prepare an Interstitial Ad Listener
		fullAd.setAdListener(new AdListener() {
			public void onAdLoaded() {
				// Call displayInterstitial() function
				displayInterstitial();
				calendar = Calendar.getInstance();
				long mTime = calendar.getTimeInMillis();
				PaintSurface.Bonus(mTime, true);
			}
		});

		Log.d("onCreate()", "Tracker");
		Tracker t = ((MyApp) getApplication()).getTracker(TrackerName.APP_TRACKER); // Get tracker.
		t.setScreenName("OnCreate"); // Pass a String representing the screen name.
		t.send(new HitBuilders.AppViewBuilder().build()); // Send a screen view.
		calendar = Calendar.getInstance();
		String time = String.valueOf(calendar.getTimeInMillis());

		PaintSurface.reset();
		setConfig();

		try {
			writeFile(userID + " s=" + time, true);
			//writeFile(" s=" + time, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void displayInterstitial() {
		// If Ads are loaded, show Interstitial else show nothing.
		if (fullAd.isLoaded()) {
			fullAd.show();
		}
		Log.d("ad", "displayInterstitial");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.tip_toggle) {
			PaintSurface.toggleTips();
		}
		if (id == R.id.more_planets) {
			//PaintSurface.morePlanets();
		}

		//if (id == R.id.action_settings) {
		//	return true;
		//}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mPaintSurface.resume();
		Log.d("onResume()", "Tracker");
		Tracker t = ((MyApp) getApplication()).getTracker(TrackerName.APP_TRACKER); // Get tracker.
		t.setScreenName("onResume"); // Pass a String representing the screen name.
	}

	@Override
	protected void onPause() {
		super.onPause();
		mPaintSurface.pause();

		double starttime = Double.parseDouble(String.valueOf(calendar.getTimeInMillis()));
		calendar = Calendar.getInstance();
		String time = String.valueOf(calendar.getTimeInMillis());
		double endtime = Double.parseDouble(time);
		double elapsed_time = endtime - starttime;

		Tracker t = ((MyApp) getApplication()).getTracker(TrackerName.APP_TRACKER);

		// Report Analytics
		t.send(new HitBuilders.EventBuilder()
				.setCategory("behavior")
				.setAction("onPauseTC")
				.setLabel(
						"TC Pause " + userID + " Score=" + PaintSurface.reportScore() + " clickCount=" + clickCount + " playTime=" + elapsed_time
								+ " touchCount=" + PaintSurface.reportTouchCount() + PaintSurface.reportDimensions() + " bonusCount=" + bonusClick
								+ " split=" + split + " freeVer=" + Config[1] + " 1stinstall=" + Config[0]).build());
		try {
			writeFileName("score3", PaintSurface.reportScore());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writeFileName("start", PaintSurface.saveStart());
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			String outputCost = "";
			for (int i = 0; i < 8; i++) {
				outputCost = outputCost + cost[i] + ",";
			}
			writeFileName("costs", outputCost);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			String outputUpgrade = "";
			for (int i = 0; i < 8; i++) {
				outputUpgrade = outputUpgrade + upgrade[i] + ",";
			}
			writeFileName("upgrades", outputUpgrade);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		//EasyTracker.getInstance(this).set(Fields.TRACKING_ID, "UA-56888204-1");
		//EasyTracker.getInstance(this).activityStart(this);

		EasyTracker tracker = EasyTracker.getInstance(this);
		tracker.set("&tid", "UA-56888204-7");
	}

	@Override
	protected void onStop() {
		super.onStop();
		EasyTracker.getInstance(MainActivity.this).activityStop(MainActivity.this);
		Tracker t = ((MyApp) getApplication()).getTracker(TrackerName.APP_TRACKER);
		t.send(new HitBuilders.EventBuilder().setCategory("behavior").setAction("onStopTC")
				.setLabel("TC Stop " + userID + " TouchCount=" + clickCount).build());
		//.setLabel("Stop " + " TouchCount=" + PaintSurface.reportTouchCount()).build());

		double starttime = Double.parseDouble(String.valueOf(calendar.getTimeInMillis()));
		calendar = Calendar.getInstance();
		String time = String.valueOf(calendar.getTimeInMillis());
		double endtime = Double.parseDouble(time);
		double elapsed_time = endtime - starttime;
		String file = "";
		try {
			writeFile(" stop=" + time + ",", false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			file = readFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		t.send(new HitBuilders.EventBuilder().setCategory("behavior").setAction("onStopTC").setLabel("Stop READ=" + file).build());

	}

	@Override
	public void onClick(View v) {
		// GoogleAnalytics.getInstance(getActivity().getApplicationContext()).dispatchLocalHits();
		calendar = Calendar.getInstance();
		long mTime = calendar.getTimeInMillis();
		clickCount++;

		Tracker t = ((MyApp) getApplication()).getTracker(TrackerName.APP_TRACKER); // Get tracker.
		Log.d("onClick()", "Buttons");
		if (v == bonus) { //top
			Log.d("Click", "adBonus " + mTime);
			bonusClick++;
			PaintSurface.Bonus(mTime, false);

			AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("").build();
			fullAd.loadAd(adRequest);

			//Intent i = new Intent(getApplicationContext(), MainActivity.class);
			//startActivity(i);

		}
		if (v == buy1) {
			boolean purchased = false;
			// ** Google Billing ** //
			// If paid, set purchase to true

			if (purchased) {
				int buyScore = Integer.valueOf(PaintSurface.reportScore());
				PaintSurface.setScore(2 * buyScore);
			}
			Log.d("onClick()", "buy");
		}
		if (v == reset) {
			Log.d("Click", "reset");
			cost[0] = 1;
			cost[1] = 5;
			cost[2] = 25;
			cost[3] = 100;
			cost[4] = 500;
			cost[5] = 2000;
			cost[6] = 10000;
			cost[7] = 50000;
			for (int i = 0; i < 8; i++) {
				upgrade[i] = 5 * cost[i];
				Button[i].setText("$" + printValue(cost[i]));
				Button2[i].setText("$" + printValueDec(upgrade[i]));
			}

			PaintSurface.reset();
			//launch in-app purchase
		}
		for (int i = 0; i < 8; i++) {
			if (v == Button[i]) {
				Log.d("1onClick()", String.valueOf(i));
				PaintSurface.Button(i, mTime);
				buttonCount[i]++;
			}
			if (v == Button2[i]) {
				Log.d("2onClick()", String.valueOf(i));
				PaintSurface.Button2(i, cost[i], upgrade[i]);
				if (Integer.valueOf(PaintSurface.reportScore()) > upgrade[i]) {
					button2Count[i]++;
					cost[i] = cost[i] * 3;
					upgrade[i] = upgrade[i] * 4;
					Button[i].setText("$" + printValue(cost[i]));
					Button2[i].setText("$" + printValueDec(upgrade[i]));
				}
			}
		}

		// Create a New Intent and start the service
		Intent intentVibrate = new Intent(getApplicationContext(), VibrateService.class);
		startService(intentVibrate);
	}

	private String returnID() throws IOException {
		String FILENAME = "ID.txt";
		String path = String.valueOf(getFilesDir());
		File myFile = new File(path + "/" + FILENAME);
		calendar = Calendar.getInstance();
		String time = String.valueOf(calendar.getTimeInMillis());
		String ID = String.valueOf(time);

		if (myFile.exists()) {
			try {
				InputStream inputStream = openFileInput(FILENAME);
				if (inputStream != null) {
					InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
					BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
					String receiveString = "";
					StringBuilder stringBuilder = new StringBuilder();

					while ((receiveString = bufferedReader.readLine()) != null) {
						stringBuilder.append(receiveString);
					}
					inputStream.close();
					ID = stringBuilder.toString();
				}
			} catch (FileNotFoundException e) {
				Log.e("login activity", "File not found: " + e.toString());
			} catch (IOException e) {
				Log.e("login activity", "Can not read file: " + e.toString());
			}

		} else {
			FileOutputStream fos;
			try {
				fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
				fos.write(ID.getBytes());
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}

		return ID;
	}

	private void writeFile(String input, boolean create) throws IOException {
		String FILENAME = "config.txt";
		String string = input; //"hello world!";

		FileOutputStream fos;
		try {
			if (create) {
				fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			} else {
				fos = openFileOutput(FILENAME, Context.MODE_APPEND);
			}
			fos.write(string.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeFileName(String name, String input) throws IOException {
		String FILENAME = name + ".txt";
		String string = input; //"hello world!";

		FileOutputStream fos;
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(string.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String readFromFile() throws IOException {
		String ret = "";
		try {
			InputStream inputStream = openFileInput("config.txt");

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}
				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e("login activity", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("login activity", "Can not read file: " + e.toString());
		}
		return ret;
	}

	private String readFromFileName(String name) throws IOException {
		String FILENAME = name + ".txt";
		String ret = "-1";
		try {
			InputStream inputStream = openFileInput(FILENAME);

			if (inputStream != null) {
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();

				while ((receiveString = bufferedReader.readLine()) != null) {
					stringBuilder.append(receiveString);
				}
				inputStream.close();
				ret = stringBuilder.toString();
			}
		} catch (FileNotFoundException e) {
			Log.e("login activity", "File not found: " + e.toString());
		} catch (IOException e) {
			Log.e("login activity", "Can not read file: " + e.toString());
		}
		return ret;
	}

	private String printValue(double A) {
		if (A < 1000) {
			return String.valueOf((int) A);
		} else if (A < 1000000) {
			return String.valueOf((int) (A / 1000)) + "K";
		} else if (A < 1000000000) {
			return String.valueOf((int) (A / 1000000)) + "M";
		} else if (A < Math.pow(10, 12)) {
			return String.valueOf((int) (A / 1000000000)) + "G";
		}
		return "ERR";
	}

	private String printValueDec(double A) {
		if (A < 1000) {
			return String.valueOf((int) A);
		} else if (A < 1000000) {
			return String.valueOf((int) (A / 1000)) + "." + String.valueOf((int) ((A % 1000) / 100)) + "K";
		} else if (A < 1000000000) {
			return String.valueOf((int) (A / 1000000)) + "." + String.valueOf((int) ((A % 1000000) / 100000)) + "M";
		} else if (A < Math.pow(10, 12)) {
			return String.valueOf((int) (A / 1000000000)) + "." + String.valueOf((int) ((A % 1000000000) / 100000000)) + "G";
		}
		return "ERR";
	}

	private void setConfig() {
		try {
			versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			firstInstall = getPackageManager().getPackageInfo(getPackageName(), 0).firstInstallTime;
		} catch (NameNotFoundException e) {
			versionCode = 0;
			firstInstall = 0;
		}

		for (int i = 0; i < 10; i++) {
			Config[i] = "";
		}
		Config[0] = String.valueOf(firstInstall);
		Config[1] = String.valueOf(versionCode);
		Config[2] = String.valueOf("1");

		try { //Load score
			String score = readFromFileName("score3");
			Log.d("Config", score);

			if (Integer.parseInt(score) > 0) {
				PaintSurface.setScore(Integer.parseInt(score));
			} else {
				PaintSurface.setScore(1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try { //business settings
			String input;
			String[] splits;
			input = readFromFileName("start");
			Log.d("start", input);
			if (!input.contains("-1")) { //-1 means no info loaded
				PaintSurface.setStart(input);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		try { //Cost settings
			String input = readFromFileName("costs");
			Log.d("set costs", input);
			if (!input.contains("-1")) { //-1 means no info loaded
				String[] splits = input.split(",");
				Log.d("split", splits[0]);
				for (int i = 0; i < 8; i++) {
					cost[i] = Integer.parseInt(splits[i]);
					Button[i].setText("$" + printValue(cost[i]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try { //Upgrade settings
			String input = readFromFileName("upgrades");
			Log.d("set upgrades", input);
			if (!input.contains("-1")) { //-1 means no info loaded
				String[] splits = input.split(",");
				Log.d("split", splits[0]);
				for (int i = 0; i < 8; i++) {
					upgrade[i] = Integer.parseInt(splits[i]);
					Button2[i].setText("$" + printValueDec(upgrade[i]));
					PaintSurface.Button2(i, cost[i], upgrade[i]);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void requestNewInterstitial() {
		PublisherAdRequest adRequest = new PublisherAdRequest.Builder().addTestDevice("YOUR_DEVICE_HASH").build();

		//mPublisherInterstitialAd.loadAd(adRequest);
	}

}