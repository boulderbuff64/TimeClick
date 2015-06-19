package com.Erickson.TimeClick;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.content.Context;
import android.util.Log;

public class Reader {

	Calendar calendar;
	int versionCode = 0;
	long firstInstall = 0;
	String[] Config = new String[10];


	public void create() {

		//start = Start;
		//progression = (float) (0.01);
		//Log.d("Start", String.valueOf(progression));
	}

	private void setConfig() {
		/*try {
			versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			firstInstall = getPackageManager().getPackageInfo(getPackageName(), 0).firstInstallTime;
		} catch (NameNotFoundException e) {
			versionCode = 0;
			firstInstall = 0;
		}*/

		for (int i = 0; i < 10; i++) {
			Config[i] = "";
		}
		Config[0] = String.valueOf(firstInstall);
		Config[1] = String.valueOf(versionCode);
		Config[2] = String.valueOf("1");

		try {
			String score = readFromFileName("score2");
			Log.d("Config", score);

			if (Integer.parseInt(score) > 0) {
				PaintSurface.setScore(Integer.parseInt(score));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	private FileOutputStream openFileOutput(String fILENAME, int modePrivate) {
		// TODO Auto-generated method stub
		return null;
	}

	private InputStream openFileInput(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
