package com.android.lander;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class LanderActivity extends Activity {
	private int a = 1;
	private boolean started = false;
	private boolean exploTop = false;
	private boolean exploBot = false;
    private int y= 200; 
    private float mAccelX = 1;
    private float mAccelY = 0;
    private float mAccelZ = 0;
    private float yItem = 80;
    private float xItem = 200;
    private int itemSize = 0;
    private int naveWidth = 0;
    private int naveHeight = 0;
    private int[] colisionesBot;
    private int[] colisionesTop;
    
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private int x = 200;
    private float rot = 0;
    private Rect rec = new Rect(0,0,800,480);
    private Rect recNave = new Rect(300,300,389,400);
    
    private Rect recStars1 = new Rect(0,0,800,480);
    private Rect recStars2 = new Rect(800,0,1600,480);
    
    private Bitmap fondo;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(new Panel(getApplicationContext(), this));
    }
    @SuppressWarnings("deprecation")
	private SensorEventListener mSensorAccelerometer = new SensorEventListener() {

        // method called whenever new sensor values are reported.
      /*  public void onSensorChanged(int sensor, float[] values) {
            // grab the values required to respond to user movement.
            mAccelX = values[0];
            mAccelY = values[1];
            mAccelZ = values[2];
        }
        
*/
        // reports when the accuracy of sensor has change
        // SENSOR_STATUS_ACCURACY_HIGH = 3
        // SENSOR_STATUS_ACCURACY_LOW = 1
        // SENSOR_STATUS_ACCURACY_MEDIUM = 2
        // SENSOR_STATUS_UNRELIABLE = 0 //calibration required.
        


		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onSensorChanged(SensorEvent event) {
			mAccelX = event.values[0];
            mAccelY = event.values[1];
            mAccelZ = event.values[2];
			
		}
    };

    
    class Panel extends SurfaceView implements SurfaceHolder.Callback {
        private mainLoop ml;
        private Paint paint;
        
        private boolean speedSaved = false;
        private Rect recf = new Rect(0,0,800,480);
        private Rect reca = new Rect(0,0,800,480);
        
        private long l;
        private Bitmap speed;
        private Bitmap speedItem;
        private Bitmap naveOn;
        private Bitmap naveOff;
        private Bitmap nave;
        private Bitmap stars;
        private Bitmap stars2;
        private Bitmap vida;
        private Bitmap vida1;
        private Bitmap vida2;
        private Bitmap vida3;
        private Bitmap vida4;
        private Bitmap vida5;
        private Bitmap vida6;
        private Bitmap vida7;
        private Bitmap vida8;
        private Bitmap explosion;
        private int pixel;
        
        
        public Panel(Context context,Activity activity) {
            super(context);
            getHolder().addCallback(this);
            //getHolder().lockCanvas().setBitmap(fondo);
            mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // register our accelerometer so we can receive values.
            // SENSOR_DELAY_GAME is the recommended rate for games
            
            mSensorManager.registerListener(mSensorAccelerometer,mAccelerometer , SensorManager.SENSOR_DELAY_UI);
            
            ml = new mainLoop(getHolder(), this);
            
            fondo = BitmapFactory.decodeResource(getResources(), R.drawable.back);
            stars = BitmapFactory.decodeResource(getResources(), R.drawable.galaxy);
            speed = BitmapFactory.decodeResource(getResources(), R.drawable.speed);
            speedItem = BitmapFactory.decodeResource(getResources(), R.drawable.speed_item);
            vida1 = BitmapFactory.decodeResource(getResources(), R.drawable.vida1);
            vida2 = BitmapFactory.decodeResource(getResources(), R.drawable.vida2);
            vida3 = BitmapFactory.decodeResource(getResources(), R.drawable.vida3);
            vida4 = BitmapFactory.decodeResource(getResources(), R.drawable.vida4);
            vida5 = BitmapFactory.decodeResource(getResources(), R.drawable.vida5);
            vida6 = BitmapFactory.decodeResource(getResources(), R.drawable.vida6);
            vida7 = BitmapFactory.decodeResource(getResources(), R.drawable.vida7);
            vida8 = BitmapFactory.decodeResource(getResources(), R.drawable.vidafull);
            explosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
            
            vida = vida8;
            
            //---------------------------------------
            colisionesBot = llenarVectorBot();
            colisionesTop = llenarVectorTop();
            
            //pixel = fondo.getPixel(20, 200);
            //---------------------------------------
            nave = naveOff;
            itemSize = speedItem.getHeight();
            //stars2 = BitmapFactory.decodeResource(getResources(), R.drawable.stars2);
            paint = new Paint();
            paint.setTextSize(20);
            paint.setColor(0xFF00FF00);
            naveWidth = naveOn.getWidth();
            naveHeight = naveOn.getHeight();
            //ab.setBitmap(fondo);
            setFocusableInTouchMode(true);
        }
     
        
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event){
        	super.onKeyDown(keyCode, event);
        	switch(keyCode)
            {
            case KeyEvent.KEYCODE_MENU:
            	
            	Log.d("gf3", "se apacho menu");
        		
				return true;
           }
        	return false;
        }
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            //_x = (int) event.getX();
            //_y = (int) event.getY();
        	a = event.getAction();
        	//rec.offset(1, 1);
        	
        	//msg[0] = (byte) (_y/(2*cpy));
            return true;
        }
        @Override
        public void onDraw(Canvas canvas) {
        	
            
        	canvas.drawBitmap(stars,recf, recStars1,null);
        	canvas.drawBitmap(stars, recf, recStars2, null);
        	//                       src   dst
        	canvas.drawBitmap(fondo, rec, recf, null);
        	
        	canvas.rotate(rot,x+ 45,y+50);
        	canvas.drawBitmap(nave,x, y, null);
            canvas.rotate(-rot,x+45,y+50);
            if(exploTop)
            	canvas.drawBitmap(explosion, x, y-50, null);
            if(exploBot)
            	canvas.drawBitmap(explosion, x, y+naveHeight, null);
            canvas.drawBitmap(vida,200, 20, null);
            canvas.drawText(y+"|", 20, 20,paint);
            if(!speedSaved)
            	canvas.drawBitmap(speedItem, xItem, yItem, null);
            if(speedSaved)
            	canvas.drawBitmap(speed, 20, 20, null);
            
        }
     
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // TODO Auto-generated method stub
        }
        //@onOff if true => on
        //		 if false => off
        
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
        	
        	ml.setRunning(true);
            ml.start();
            
           
        }
     
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // simply copied from sample application LunarLander:
            // we have to tell thread to shut down & wait for it to finish, or else
            // it might touch the Surface after we return and explode
            boolean retry = true;
            ml.setRunning(false);
            //threadr.setRunning(false);
            while (retry) {
                try {
                	//threadr.join();
                    ml.join();
                    retry = false;
                } catch (InterruptedException e) {
                    // we will try it again and again...
                }
            }
        }
        
        // mis metodos---------------------------------------------------
        public int[] llenarVectorBot(){
        	int[] x = new int[100];
        	int pix = 0;
        	for(int i = 0;i<100;i++){
        		for(int j = 479;j>0;j--){
        			pix = fondo.getPixel(i*10, j);
        			if(pix == 0){
        				x[i] = j;
        				break;
        			}
        		}
        	}
        	
        	return x;
        }
        public int[] llenarVectorTop(){
        	int[] x = new int[100];
        	int pix = 0;
        	for(int i = 0;i<100;i++){
        		for(int j = 0;j<479;j++){
        			pix = fondo.getPixel(i*10, j);
        			if(pix == 0){
        				x[i] = j;
        				break;
        			}
        		}
        	}
        	
        	return x;
        }
        public void change(boolean onOff){
        	if(onOff){
        	nave = naveOn;
        	return;
        	}
        	nave = naveOff;
        }
        public void item(boolean onOff){
        	speedSaved = onOff; 
        }
        public void vida(int life){
        	switch (life){
        	case 1:
        		vida = vida1;
        		break;
        	case 2:
        		vida = vida2;
        		break;
        	case 3:
        		vida = vida3;
        		break;
        	case 4:
        		vida = vida4;
        		break;
        	case 5:
        		vida = vida5;
        		break;
        	case 6:
        		vida = vida6;
        		break;
        	case 7:
        		vida = vida7;
        		break;
        	
        	}
        
        	
        }
        
    }
 
    class mainLoop extends Thread {
        private SurfaceHolder _surfaceHolder;
        private Panel _panel;
        private boolean _run = false;
 
        public mainLoop(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
           
            _panel = panel;
        }
 
        public void setRunning(boolean run) {
            _run = run;
        }
 
        @Override
        public void run() {
        	float cont = 0;
        	float cont2 = 0;
        	int contI = 0;
        	int dir = 1;
        	int vida = 8;
        	int contExplo = 0;
        	int xReal = x;
        	int xvector1,xvector2 = 0;
        	int dx = (int) mAccelY;
            Canvas c;
            while (_run) {
                c = null;
                try {
                	dx = (int)mAccelY;
                     c = _surfaceHolder.lockCanvas(null);
                    
                    synchronized (_surfaceHolder) {
                    	if(a != MotionEvent.ACTION_UP){
                    		started = true;
                    		_panel.change(true);
                    		if(cont < 180){
                    			cont++;
                    		}
                    		cont2 = 3;
                    		y -= 0.1*cont;
                    	
                    		//mover background -------------------------
                    		/*
                    		recStars2.offset(-10, 0);
                    		recStars1.offset(-10, 0);
                    		if(recStars1.right <= 0){
                    			recStars1.offset(1600, 0);
                    		}
                    		if(recStars2.right <= 0){
                    			recStars2.offset(1600, 0);
                    		}*/
                    		//--------------------------------------------
                    	}else{
                    		cont = 0;
                    		y+= 0.1*cont2;
                    		if(started)
                    			cont2++;
                    		_panel.change(false);
                    	}
                    	
                    	yItem = yItem+dir;
                    	contI++;
                    	if(contI > 10){
                    		dir = dir*(-1);
                    		contI = 0;
                    	}
                    	//--------------------------- colisiones ---------------------------------------
                    	//----items
                    	if((x+naveWidth>xItem)&&(x<xItem+itemSize)&&(y+naveHeight>yItem)&&(y<yItem+itemSize)){
                    		_panel.item(true);
                    	}
                    	//------
                    	xvector1 = xReal/10;
                    	xvector2 = (xReal+naveWidth)/10;
                    	
                    
                    	if((y+naveHeight> colisionesBot[xvector1])||(y+naveHeight>colisionesBot[xvector2])){
                    		vida--;
                    		_panel.vida(vida);
                    		y -= 50;
                    		cont = 0;
                    		cont2 = 0;
                    		exploBot = true;
                    		contExplo = 0;
                    	}
                    	if((y< colisionesTop[xvector1+4])){
                    		vida--;
                    		_panel.vida(vida);
                    		y += 50;
                    		cont = 0;
                    		cont2 = 0;
                    		exploTop = true;
                    		contExplo = 0;
                    	}
                    	if(contExplo < 10){
                    	contExplo++;
                    	}
                    	if(contExplo==10){
                    	exploBot = false;
                    	exploTop = false;
                    	
                    	}
                    	//--------------------------------------------------------------------------------
                    	xReal = (dx + xReal);
                    	if((x>600)&&(dx > 0 )){
                    		rec.offset(dx, 0);
                    		recStars2.offset(-dx, 0);
                    		recStars1.offset(-dx, 0);
                    	}else{
                    		x= (dx +x);
                    	}
                    	rot = (float) (dx*5.0);
                    	//c.setBitmap(fondo);
                        _panel.onDraw(c);
                        
                    }
                } finally {
                    // do| this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) { 
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        } 
    }   
}