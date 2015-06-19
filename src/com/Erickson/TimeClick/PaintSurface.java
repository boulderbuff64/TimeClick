package com.Erickson.TimeClick;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class PaintSurface extends SurfaceView implements Runnable, OnTouchListener {

	SurfaceHolder mHolder; // Surface holder allows to control and monitor the surface
	Thread mThread; // A thread where the painting activities are taking place
	boolean mFlag = false; // A flag which controls the start and stop of the repainting of the SurfaceView
	static Canvas canvas; // create the canvas to edit

	Paint mPaint = new Paint();
	Paint textPaint = new Paint();
	Paint TutTextPaint = new Paint();
	Paint tipTextPaint = new Paint();
	Paint glowPaint = new Paint();

	float Xbegin; // X coord of 1st touch point
	float Ybegin; // Y coord of 1st touch point
	float mX; // X coord while touching
	float mY; // Y coord while touching
	float Xend; // X coord of last touch point
	float Yend; // Y coord of last touch point
	int freeze = 0;
	static int ScreenX = 700;
	static int ScreenY = 1200;
	int release = 0;
	static double score = 1;
	static double bestScore = 1;
	static long lastScore = 0;
	static int first = 1;
	static int touchCount = 0;
	static boolean displayTutorial = false;
	int timer = 0;
	int tip_timer = 0;
	int tipIndex = 0;
	static int show_tips = 1;
	int last_tipIndex = 2;
	int screenSide = ScreenY / 3;
	Calendar calendar;
	Bitmap freePic;
	Bitmap x10Pic;

	static int tip_count = 40;
	static String[] tips = new String[tip_count];
	//static int LONG_PRESS_TIME = 500; // Long press time to move the sun
	static String printScore = "$1";

	//static int Bcount = 3;  
	Business[] Bus = new Business[8];
	static int[] worth = new int[8];
	static int[] upgrade = new int[8];
	static boolean[] tap = new boolean[8];
	static boolean[] tap2 = new boolean[8];
	static boolean loading = false;
	static int[] lastTap = new int[10];
	static long tapTime = 0;
	static long adTime = 0;
	static long adTimeLength = 120000;
	int adBonus = 0;
	static int bonusMulti = 1;
	int[] animate = new int[10];
	int[] triggered = new int[10];
	static int animate_pos = 0;
	int animateTime = 60;
	int free_pos = 0;
	int unlock_pos = 0;
	float[][] random = new float[5][5];
	static long[] startTime = new long[8];
	Bitmap lemonPic = BitmapFactory.decodeResource(getResources(), R.drawable.lemon);
	Bitmap moneyBG = BitmapFactory.decodeResource(getResources(), R.drawable.money_texture_sm2);

	public PaintSurface(Context context, AttributeSet attrs) {
		super(context, attrs);
		mHolder = getHolder(); // Getting the holder
		mX = 1; // Initializing the X position
		mY = 1; // Initializing the Y position

		// Setting the color for the paint object
		mPaint.setColor(Color.GREEN);
		//collPaint.setAlpha(64);

		// Text Color
		textPaint = new Paint();
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(30);
		textPaint.setTextAlign(Align.CENTER);

		TutTextPaint.setColor(Color.WHITE);
		TutTextPaint.setTextSize(40);
		TutTextPaint.setTextAlign(Align.CENTER);

		tipTextPaint.setColor(Color.WHITE);
		tipTextPaint.setTextSize(25);
		tipTextPaint.setTextAlign(Align.CENTER);

		glowPaint.setColor(Color.YELLOW);
		glowPaint.setAlpha(64);

	}

	public void resume() {
		mThread = new Thread(this); // Instantiating the thread
		mFlag = true; // setting the mFlag to true for start repainting
		mThread.start(); // Start repaint the SurfaceView
	}

	public void pause() {
		mFlag = false;
	}

	// Counter for long press
	final Handler _handler = new Handler();
	Runnable _longPressed = new Runnable() {
		public void run() {
			//moveSun = true;
		}
	};

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//_handler.postDelayed(_longPressed, LONG_PRESS_TIME);
			Xbegin = event.getX();
			mX = event.getX();
			Ybegin = event.getY();
			mY = event.getY();
			freeze = 1;
			touchCount++;
			v.performClick();
			break;
		case MotionEvent.ACTION_MOVE:
			_handler.removeCallbacks(_longPressed);
			mX = event.getX();
			mY = event.getY();
			freeze = 1;
			break;
		case MotionEvent.ACTION_UP:
			_handler.removeCallbacks(_longPressed);
			Xend = event.getX();
			Yend = event.getY();
			freeze = 0;
			v.performClick();
			break;
		}
		return true;
	}

	@Override
	public void run() {
		while (mFlag) {
			// Check whether the object holds a valid surface
			if (!mHolder.getSurface().isValid())
				continue;
			Canvas canvas = mHolder.lockCanvas(); // Start editing the surface
			ScreenY = canvas.getHeight();
			ScreenX = canvas.getWidth();
			calendar = Calendar.getInstance();
			long currentTime = calendar.getTimeInMillis();

			if (first == 1) {
				canvas.drawARGB(255, 0, 0, 0); //background

				loadTips();
				textPaint.setTextSize(ScreenX / 20);
				first = 0;
				for (int i = 0; i < Bus.length; i++) {
					tap[i] = false;
					tap2[i] = false;
					Bus[i] = new Business(1000 * (Math.pow(3, i + 1)));
					Bus[i].setColor((127 * i + 254) % 255, (86 * i + 170) % 255, (192 * i) % 255);
				}
				for (int i = 0; i < animate.length; i++) {
					animate[i] = 200;
				}

				Bus[0].setColor(250, 250, 0);
				Bus[1].setColor(255, 125, 0);
				Bus[2].setColor(150, 100, 0);
				Bus[0].name = "Lemonade";
				Bus[1].name = "Eday";
				Bus[2].name = "Taco Stand";
				Bus[3].name = "Restaurant";
				Bus[4].name = "Apartments";
				Bus[5].name = "Baseball Team";
				//Bus[6].name = "Oil Rig";
				Bus[6].name = "Tech Company";
				Bus[7].name = "Bank";
				Bus[0].worth = 1;
				Bus[1].worth = 5;
				Bus[2].worth = 25;
				Bus[3].worth = 100;
				Bus[4].worth = 500;
				Bus[5].worth = 2000;
				Bus[6].worth = 10000;
				Bus[7].worth = 50000;
				Bus[0].pic = BitmapFactory.decodeResource(getResources(), R.drawable.lemon);
				Bus[1].pic = BitmapFactory.decodeResource(getResources(), R.drawable.eday);
				Bus[2].pic = BitmapFactory.decodeResource(getResources(), R.drawable.taco);
				Bus[3].pic = BitmapFactory.decodeResource(getResources(), R.drawable.plate);
				Bus[4].pic = BitmapFactory.decodeResource(getResources(), R.drawable.apartment_sm);
				Bus[5].pic = BitmapFactory.decodeResource(getResources(), R.drawable.baseball_sm);
				Bus[6].pic = BitmapFactory.decodeResource(getResources(), R.drawable.processor);
				Bus[7].pic = BitmapFactory.decodeResource(getResources(), R.drawable.bank);
				freePic = BitmapFactory.decodeResource(getResources(), R.drawable.free);
				x10Pic = BitmapFactory.decodeResource(getResources(), R.drawable.free);
				for (int i = 0; i < Bus.length; i++) {
					triggered[i] = 0;
					int Y = ScreenY * (1 + i) / (Bus.length + 1);
					int H = ScreenY / Bus.length * 3 / 4;
					//Bus[i].rect = new Rect(ScreenX / 20, Y, ScreenX * 3 / 20, Y + H);
					//Bus[i].rect = new Rect(ScreenX / 10, Y, ScreenX / 5, Y + H); ScreenX / 10
					Bus[i].rect = new Rect(ScreenX / 10, Y, ScreenX / 10 + H, Y + H);
					Bus[i].pricePlaceX = ScreenX / 20;
					Bus[i].pricePlaceY = Y + H / 2;
					worth[i] = Bus[i].worth;
					upgrade[i] = worth[i] * 10;
				}
				bestScore = score;
				if (!loading) {
					//Log.d("Not Loading", ""+startTime[i]);
					for (int i = 0; i < 8; i++) {
						startTime[i] = 0;
					}
				}
			}

			if (loading) {
				for (int i = 0; i < 8; i++) {
					Bus[i].begin(startTime[i]);
					Log.d("Loading", "progressTime = " + i + "  " + startTime[i]);
				}
				loading = false;
			} else {
				for (int i = 0; i < 8; i++) {
					startTime[i] = Bus[i].start;
				}
			}

			//Background
			canvas.drawARGB(256, 0, 0, 0);
			Rect fullBackground = new Rect(0, 0, ScreenX, ScreenY);
			//canvas.drawBitmap(lemonPic, null, fullBackground, mPaint);
			canvas.drawBitmap(moneyBG, null, fullBackground, mPaint);

			if (bestScore < score) {
				bestScore = score;
			}

			//Update worth of each company
			for (int i = 0; i < Bus.length; i++) {
				Bus[i].worth = worth[i];
			}

			for (int i = 0; i < tap.length; i++) {
				if (tap[i] == true) {
					tap[i] = false;
					if (Bus[i].progression == 0) {
						if (Math.random() < 0.95) { //variable reward (sometimes don't charge)
							score = score - Bus[i].worth;
						} else {
							free_pos = animate_pos;
							animate[4] = 0;
							Log.d("free", "free " + lastTap[animate_pos]);
						}
						Bus[i].begin(tapTime);
						animate[animate_pos] = 0;

						for (int j = 0; j < 3; j++) {
							if (animate[j] == 0) {
								for (int k = 0; k < 4; k++) {
									random[j][k] = (float) ((Math.random() - 0.5) * ScreenX / 80);
								}
							}
						}
					}
					animate_pos++;
					animate_pos = animate_pos % 3;
					if (animate_pos >= 3) {
						animate_pos = 0;
					}
				}
			}

			//Bonus after watching an ad
			if ((currentTime - adTime) < adTimeLength) {
				adBonus = 1;
			} else {
				adBonus = 0;
				bonusMulti = 1;
			}

			for (int i = 0; i < Bus.length; i++) {
				Bus[i].update(currentTime, bonusMulti);
				if (Bus[i].scored) {
					//score = score + 2 * Bus[i].worth + adBonus * (bonusMulti - 1) * Bus[i].worth; //Add money to score 
					score = score + 2 * Bus[i].worth;
					Bus[i].scored = false;
					//Log.d("Bus", "scoring " + Bus[i].worth);
				}
				int Y = ScreenY * (1 + i) / (Bus.length + 1);
				int H = ScreenY / Bus.length * 3 / 4;
				//canvas.drawRect(0, Y, ScreenX / 5, Y + H, Bus[i].Paint); //left button  
				//canvas.drawRect(ScreenX * 4 / 5, Y, ScreenX, Y + H, mPaint); //right button
				//canvas.drawRect(0, Y, ScreenX / 5 + Bus[i].progression * ScreenX * 6 / 10, Y + H, Bus[i].Paint); //Progress Bar
				canvas.drawRect(0, Y, Bus[i].progression * ScreenX * 8 / 10, Y + H, Bus[i].Paint); //Progress Bar
				canvas.drawCircle(Bus[i].pricePlaceX, Bus[i].pricePlaceY, ScreenX / 20, Bus[i].Paint); //name background

				//Rect R1 = new Rect(ScreenX / 20, ScreenY / (Bus.length + 1), ScreenX * 3 / 20, ScreenY / (Bus.length + 1) + H);
				//Rect R2 = new Rect(ScreenX / 20, ScreenY *3 / (Bus.length + 1), ScreenX * 3 / 20, ScreenY *3 / (Bus.length + 1) + H);
				if (bestScore >= Bus[i].worth) {
					canvas.drawBitmap(Bus[i].pic, null, Bus[i].rect, mPaint);

					canvas.drawText(String.valueOf((int) (Bus[i].progression * 100)) + "%", ScreenX * 2 / 3, Y + H - 5, textPaint); //Percentage
					//canvas.drawText(Bus[i].name, Bus[i].pricePlaceX, Bus[i].pricePlaceY, textPaint); //Business Name
					canvas.drawText(Bus[i].name, ScreenX * 3 / 7, Y + H / 2, textPaint); //Business Name
					if (triggered[i] == 0) {
						Log.d("Unlock", "triggered=" + triggered[i] + "  bestScore=" + bestScore + "   worth=" + Bus[i].worth);
						triggered[i] = 1;
						Log.d("Unlock", "triggered=" + triggered[i]);
						animate[5] = 0;
						unlock_pos = animate_pos;
					}
				}

				int X = ScreenX / 10;
				if (animate[0] < animateTime & i == lastTap[0]) {
					canvas.drawCircle(X + animate[0] / 2 * random[0][0], Y + H / 2, animate[0] / 2, textPaint);
					canvas.drawCircle(X + animate[0] / 2 * random[0][1], Y + H / 2, animate[0] / 2, textPaint);
					canvas.drawCircle(X, Y + animate[0] / 2 * random[0][2] + H / 2, animate[0] / 2, textPaint);
					canvas.drawCircle(X, Y - animate[0] / 2 * random[0][3] + H / 2, animate[0] / 2, textPaint);
					canvas.drawCircle(X + animate[0] / 2 * random[0][0], Y + animate[0] / 2 * random[0][0] + H / 2, animate[0] / 2, Bus[4].Paint);
					canvas.drawCircle(X + animate[0] / 2 * random[0][1], Y + animate[0] / 2 * random[0][1] + H / 2, animate[0] / 2, Bus[5].Paint);
					canvas.drawCircle(X + animate[0] / 2 * random[0][2], Y + animate[0] / 2 * random[0][2] + H / 2, animate[0] / 2, Bus[6].Paint);
					canvas.drawCircle(X + animate[0] / 2 * random[0][3], Y + animate[0] / 2 * random[0][3] + H / 2, animate[0] / 2, Bus[7].Paint);
				}
				if (animate[1] < animateTime & i == lastTap[1]) {
					canvas.drawCircle(X + animate[1] / 2 * random[1][0], Y + H / 2, animate[1] / 2, textPaint);
					canvas.drawCircle(X + animate[1] / 2 * random[1][1], Y + H / 2, animate[1] / 2, textPaint);
					canvas.drawCircle(X, Y + animate[1] / 2 * random[1][2] + H / 2, animate[1] / 2, textPaint);
					canvas.drawCircle(X, Y - animate[1] / 2 * random[1][3] + H / 2, animate[1] / 2, textPaint);
					canvas.drawCircle(X + animate[1] / 2 * random[1][0], Y + animate[1] / 2 * random[1][0] + H / 2, animate[1] / 2, Bus[4].Paint);
					canvas.drawCircle(X + animate[1] / 2 * random[1][1], Y + animate[1] / 2 * random[1][1] + H / 2, animate[1] / 2, Bus[5].Paint);
					canvas.drawCircle(X + animate[1] / 2 * random[1][2], Y + animate[1] / 2 * random[1][2] + H / 2, animate[1] / 2, Bus[6].Paint);
					canvas.drawCircle(X + animate[1] / 2 * random[1][3], Y + animate[1] / 2 * random[1][3] + H / 2, animate[1] / 2, Bus[7].Paint);
				}
				if (animate[2] < animateTime & i == lastTap[2]) {
					//Log.d("Draw", "an2 " + lastTap[animate_pos]);
					canvas.drawCircle(X + animate[2] / 2 * random[2][0], Y + H / 2, animate[2] / 2, textPaint);
					canvas.drawCircle(X + animate[2] / 2 * random[2][1], Y + H / 2, animate[2] / 2, textPaint);
					canvas.drawCircle(X, Y + animate[2] / 2 * random[2][2] + H / 2, animate[2] / 2, textPaint);
					canvas.drawCircle(X, Y - animate[2] / 2 * random[2][3] + H / 2, animate[2] / 2, textPaint);
					canvas.drawCircle(X + animate[2] / 2 * random[2][0], Y + animate[2] / 2 * random[2][0] + H / 2, animate[2] / 2, Bus[4].Paint);
					canvas.drawCircle(X + animate[2] / 2 * random[2][1], Y + animate[2] / 2 * random[2][1] + H / 2, animate[2] / 2, Bus[5].Paint);
					canvas.drawCircle(X + animate[2] / 2 * random[2][2], Y + animate[2] / 2 * random[2][2] + H / 2, animate[2] / 2, Bus[6].Paint);
					canvas.drawCircle(X + animate[2] / 2 * random[2][3], Y + animate[2] / 2 * random[2][3] + H / 2, animate[2] / 2, Bus[7].Paint);
				}
				if (animate[4] < animateTime & i == lastTap[free_pos]) { //Draw free image
					//Log.d("Draw", "free " + lastTap[free_pos]);
					Rect R1 = new Rect(ScreenX / 2 - animate[4] * 3, Y, ScreenX / 2 + animate[4] * 3, Y + +animate[4] * 4);
					canvas.drawBitmap(freePic, null, R1, mPaint);
				}
				if (animate[5] < animateTime * 2 & triggered[i] == 1) { //Draw unlock image
					//Log.d("Draw", "Unlock " + lastTap[unlock_pos] + " " + animate[5]);
					Rect R1 = new Rect(ScreenX / 2 - animate[5] * 2, (int) (Y - animate[5] * 1.5), ScreenX / 2 + animate[5] * 2,
							(int) (Y + animate[5] * 2.5));
					canvas.drawBitmap(Bus[i].pic, null, R1, mPaint);

					if (animate[5] > (animateTime * 2 - 3)) {
						Log.d("Draw", "Finish Unlock ");
						triggered[i] = 2;
					}
				}
			}

			//Print Score
			canvas.drawRect(0, 0, ScreenX, ScreenY / 10, mPaint);
			canvas.drawText(printScore, ScreenX / 2, ScreenY / 20, textPaint);
			printScore = convertScore(score);

			// *** Draw Ad animation *** //
			if (adBonus == 1) {
				float adProgress = (float) (currentTime - adTime) / adTimeLength;
				//Log.d("adProgress", "" + adProgress);
				canvas.drawCircle(ScreenX * 9 / 10, ScreenY / 20, ScreenY / 20, Bus[3].Paint);
				canvas.drawRect(ScreenX * 8 / 10, 0, ScreenX, (float) (adProgress / 10 * ScreenY), mPaint); //Progress Bar
				canvas.drawText("x" + bonusMulti, ScreenX * 9 / 10, ScreenY / 20, textPaint);
			} else {
				for (int i = 0; i <= 15; i++) {
					//canvas.drawCircle(ScreenX * 5 / 10, ScreenY / 20, ScreenY / 20 + ScreenX / 200 * i, glowPaint);
					glowPaint.setAlpha(40);
					canvas.drawCircle(ScreenX * 9 / 10, ScreenY / 20,
							ScreenY / 20 + (int) (ScreenX / 150 * i * Math.sin((float) animate[6] / 18)), glowPaint);
				}
				canvas.drawRect(ScreenX * 8 / 10, 0, ScreenX * 9 / 10, ScreenY / 10 + ScreenX / 50, glowPaint);
			}
			if (adTime == -1) {
				canvas.drawText("Loading", ScreenX * 9 / 10, ScreenY / 25, textPaint);
			}

			// *** Draw x10 bonus animation *** //
			if (bonusMulti == 10) {
				if (triggered[8] == 0) {
					animate[8] = 0;
					triggered[8] = 1;
				}
			} else {
				triggered[8] = 0;
			}
			if (animate[7] < animateTime * 3) {
				Rect R1 = new Rect(ScreenX / 2 - animate[7] * 2, ScreenY / 3 - animate[7] * 2, ScreenX / 2 - animate[7] * 2, ScreenY / 3
						- animate[7] * 2);
				canvas.drawBitmap(x10Pic, null, R1, mPaint); //replace with x10 pic
			}

			for (int i = 0; i < animate.length; i++) {
				animate[i]++;
			}
			mHolder.unlockCanvasAndPost(canvas); // Finish editing the canvas and show to the user
		}
	}

	public static String reset() {
		first = 1;
		String resetScore = String.valueOf((int) score);
		score = 10;
		Log.d("reset()", "reset");

		return resetScore;
	}

	// Set the score
	public static String convertScore(double score) {
		if (score < 1000) {
			return "$" + String.valueOf((int) score);
		} else if (score < 1000000) {
			return "$" + String.valueOf((int) (score / 1000)) + "." + String.valueOf((int) ((score % 1000) / 10)) + "K";
		} else if (score < 1000000000) {
			return "$" + String.valueOf((int) (score / 1000000)) + "." + String.valueOf((int) ((score % 1000000) / 10000)) + "M";
		} else if (score < Math.pow(10, 12)) {
			return "$" + String.valueOf((int) (score / 1000000000)) + "." + String.valueOf((int) ((score % 1000000000) / 10000000)) + "G";
		}
		return "Trillionaire!";
	}

	public static void loadTips() {
		for (int i = 0; i < tip_count; i++) {
			tips[i] = " ";
		}
		int j = 0;
		tips[j++] = "";
		tips[j++] = "Bigger planets have stronger gravity";
		tips[j++] = "Get Planet Sim Pro to help indie developers out";
		tips[j++] = "";
	}

	public static void Bonus(long time, boolean loadAd) {
		Log.d("Button", String.valueOf(time));
		if (loadAd) {
			adTime = time;
			if (Math.random() * 10 > 9) { //set bonus multiplier 
				bonusMulti = 10;
			} else {
				bonusMulti = (int) (2 + Math.random() * 2);
			}
		} else {
			adTime = -1;
			bonusMulti = 1;
		}
	}

	public static void Button(int b, long time) {
		Log.d("Button", String.valueOf(b) + " " + String.valueOf(tapTime));
		if (score >= (int) (worth[b])) {
			tap[b] = true;
			tapTime = time;
		}

		//animate_pos++;
		//animate_pos = animate_pos % 3;
		lastTap[animate_pos] = b;
	}

	public static void Button2(int b, int Cost, int Upgrade) {
		Log.d("Button", String.valueOf(b) + " " + String.valueOf(tapTime));

		if (score >= (int) (upgrade[b])) {
			score = score - Upgrade;
			worth[b] = Cost * 3;
			upgrade[b] = Upgrade * 4;
			tap2[b] = true;
		}
	}

	public static void DisplayTutorialTrue() {
		displayTutorial = true;
	}

	public static void DisplayTutorialfalse() {
		displayTutorial = false;
	}

	public static String reportScore() {
		//return printScore;
		return String.valueOf((int) score);
	}

	public static String reportTouchCount() {
		return String.valueOf(touchCount);
	}

	public static int toggleTips() {
		if (show_tips == 1) {
			show_tips = 0;
		} else {
			show_tips = 1;
		}
		return show_tips;
	}

	public static String reportDimensions() {
		return " X=" + String.valueOf(ScreenX) + " Y=" + String.valueOf(ScreenY);
	}

	public static void setScore(int a) {
		score = a;
	}

	public static String saveStart() {
		String start = "";
		for (int i = 0; i < 8; i++) {
			start = start + startTime[i] + ",";
			Log.d("startTime", "" + startTime[i]);
		}
		return start;
	}

	public static void setStart(String input) {
		Log.d("setStart input", input);
		String[] splits = input.split(",");
		Log.d("setStart split0", splits[0]);
		for (int i = 0; i < 8; i++) {
			startTime[i] = Long.parseLong(splits[i]);
			Log.d("startTime", "" + startTime[i]);
		}
		//Log.d("startTime", "" + startTime[0]);
		loading = true;
	}

}
