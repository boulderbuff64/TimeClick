package com.Erickson.TimeClick;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Business {

	String name;
	float progression = 0;
	long progressTime;
	long start = 0;
	double length;
	Paint Paint = new Paint();
	int worth;
	boolean scored = false;
	Bitmap pic; //clickable image  
	Rect rect; //location oo clickable image
	int pricePlaceX;
	int pricePlaceY;

	Business(double Length) {
		length = Length;
		Log.d("Business", "start");
		Paint.setStrokeWidth(5);
	}

	public void setColor(int R, int G, int B) {
		Paint.setColor(Color.rgb(R, G, B));
	}

	public void begin(long Start) {
		start = Start;
		progressTime = Start;
		if (Start > 1) {
			progression = (float) (0.01);
			Log.d("begin", "start "+Start);
		}
	}

	//public float update(long time) {
	public float update(long time, int bonus) {
		if (progression < 0) {
			progression = (float) (0.01);
		}
		if (progression > 0 & progression <= 1) {
			//progression = (float) (0.01 + (time - start + 10) / length);
			progression = progression + (float) (bonus * (float) (time - progressTime) / length);
			Log.d("Update", progression + " " + time + " " + progressTime);
		}
		if (progression > 1) {
			Log.d("Update Prog", name);
			progression = 0;
			scored = true;
			//return 0;
		}
		progressTime = time;
		return progression;
	}
}
