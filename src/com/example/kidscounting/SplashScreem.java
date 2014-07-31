package com.example.kidscounting;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Toast;

public class SplashScreem extends Activity implements Runnable {
	
	private final int DELAY = 3000; //3 segundos

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screem);
		
		Toast.makeText(this, "Aguarde o carregamento do app...", Toast.LENGTH_SHORT).show();
		Handler h = new Handler();
		h.postDelayed(this, DELAY);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screem, menu);
		return true;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, KidsCounting.class));
		finish();
	}

}
