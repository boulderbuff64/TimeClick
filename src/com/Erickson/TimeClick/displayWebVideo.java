package com.Erickson.TimeClick;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressLint("NewApi")
public class displayWebVideo extends Activity {
	WebView webView;

	private boolean isNetworkAvailable() {
		boolean isConnected = false;
		ConnectivityManager check = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (check != null) {
			NetworkInfo[] info = check.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						isConnected = true;
					}
				}
			}
		}
		return isConnected;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		WebView webView = (WebView) findViewById(R.id.webView1);
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl("http://goo.gl/jPKbAf"); //Google URL for youtube video
		//webView.loadUrl("http://goo.gl/ncdNmo"); //Google URL for play store page
		WebSettings webSettings = webView.getSettings();
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(false);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default
		if (!isNetworkAvailable()) { // loading offline
			webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}
	}
}