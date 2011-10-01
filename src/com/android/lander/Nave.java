package com.android.lander;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Nave {
	Bitmap naveOff;
	Bitmap naveOn;
	Bitmap nave;
	public Nave(Activity activity){
		
		naveOff = BitmapFactory.decodeResource(activity.getApplicationContext().getResources(), R.drawable.nave);
        naveOn = BitmapFactory.decodeResource(activity.getApplicationContext().getResources(), R.drawable.naveon);
        nave = naveOff;
	}

}
