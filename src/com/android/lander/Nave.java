package com.android.lander;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Nave {
	Bitmap naveOff;
	Bitmap naveOn;
	Bitmap img;
	int height;
	int width;
	public Nave(Activity activity){
		
		naveOff = BitmapFactory.decodeResource(activity.getApplicationContext().getResources(), R.drawable.nave);
        naveOn = BitmapFactory.decodeResource(activity.getApplicationContext().getResources(), R.drawable.naveon);
        img = naveOff;
        height = naveOn.getHeight();
        width = naveOn.getWidth();
	}
	public void off(){
		img = naveOff;
	}
	public void on(){
		img = naveOn;
	}

}
