package com.android.lander;

import com.android.lander.LanderActivity.Panel;

import android.app.Activity;
import android.graphics.Bitmap;

public class Item {
	Bitmap img;
	Panel panel;
	int id;
	public Item(Bitmap i,int n,Panel p){
		id = n;
		img = i;
		panel = p;
	}
	public void run(){
		switch (id){
		case 1:
			panel.itemSpeed();
			break;
		}
			
		
	}
}
