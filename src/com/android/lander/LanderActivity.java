package com.android.lander;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
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
	private boolean alienActive = false;
	private int alienX = 0;
	private int alienY = 280;
	private int pantalla = 1;
    private int y= 200; 
    private boolean gameOver = false;
    private float mAccelX = 1;
    private float mAccelY = 0;
    private float mAccelZ = 0;
    private float yItem = 80;
    private float xItem = 150;
    private int itemSize = 0;
    private int alienSize = 0;
    private int xTouch = 0;
    private int yTouch = 0;
    
    private int[] colisionesBot;
    private int[] colisionesTop;
    private int gameSpeed= 1;;
    private final static int SPEED = 1;
    private final static int VIDA = 2;
    private final static float DFUEL = (float) -0.1; //diferencial de la gasolina
    
      

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
        private Paint pFuel;
        private Paint pBFuel;
        private Paint pBBFuel; //back border fuel
        private int barYi = 0;
        private int barYf = 50;
        private float fuel = 100;
        private boolean speedSaved = false;
        private Rect recf = new Rect(0,0,800,480);
        private Rect reca = new Rect(0,0,800,480);
        							 //   5   5     95      45
        private RectF recFuel = new RectF(5,barYi+5,fuel-5,barYf-5);
        
        private RectF BBFuel= new RectF();
        private RectF barra = new RectF(98,0,702,20);
    	private int colorFuel = 0xFF99D9EA;
    	private int colorBFuel = 0xFFFF9900;
    	
        //private RectF re = new RectF(0,barYi,800,barYf);
        private RectF re = new RectF(0,0,100,50);
        private RectF re2 = new RectF(700,0,800,50);
        private long l;
        private Bitmap speed;
        private Bitmap speedItem;
        private Bitmap naveOn;
        private Bitmap naveOff;
        private Nave nave;
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
        private Bitmap alien;
        
        private Bitmap botonGameOver;
        private Bitmap botonGameOverOn;
        private Bitmap botonGameOverNormal;
        
        private Bitmap p2;
        private Bitmap explosion;
        private int pixel;
        private Item vel;
        private Bitmap taptostart;
    	
        public Panel(Context context,Activity activity) {
            super(context);
            getHolder().addCallback(this);
            //getHolder().lockCanvas().setBitmap(fondo);
            mSensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // register our accelerometer so we can receive values.
            // SENSOR_DELAY_GAME is the recommended rate for games
            vel = new Item(speed,1,this);
            mSensorManager.registerListener(mSensorAccelerometer,mAccelerometer , SensorManager.SENSOR_DELAY_UI);
            
            ml = new mainLoop(getHolder(), this);
            
            inicializarP1();
            taptostart = BitmapFactory.decodeResource(getResources(), R.drawable.taptostart);
            nave = new Nave(activity);
            itemSize = speedItem.getHeight();
            alienSize = alien.getHeight();
            //stars2 = BitmapFactory.decodeResource(getResources(), R.drawable.stars2);
            paint = new Paint();
            pFuel = new Paint();
            pBFuel = new Paint();
            pBBFuel = new Paint();
            BBFuel.set(recFuel);
            paint.setTextSize(20);
            paint.setColor(0xFFFFFF00);
            
            pFuel.setColor(colorFuel);
            pBFuel.setColor(colorBFuel);
            pBBFuel.setColor(0xFF1f6877);
            pBBFuel.setStrokeWidth(2);
            pBBFuel.setStyle(Style.STROKE);
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
            xTouch = (int) event.getX();
            yTouch = (int) event.getY();
        	a = event.getAction();
        	//rec.offset(1, 1);
        	
        	//msg[0] = (byte) (_y/(2*cpy));
            return true;
        }
        @Override
        public void onDraw(Canvas canvas) {
        	switch (pantalla){
        	case 1:
        		drawP1(canvas);
        		break;
        	case 2:
        		//drawP2(canvas);
        		break;
        	
        	}
        	
          
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
            vida1.recycle();
        	vida2.recycle();
        	vida3.recycle();
        	vida4.recycle();
        	vida5.recycle();
        	vida6.recycle();
        	vida7.recycle();
        	vida8.recycle();
        	fondo.recycle();
        	speed.recycle();
        	speedItem.recycle();
        	alien.recycle();
        
        	stars.recycle();
        }
        
        // ------------------------------------mis metodos---------------------------------------------------
        public int[] llenarVectorBot(){
        	int[] x = new int[320];
        	int pix = 0;
        	for(int i = 0;i<320;i++){
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
        	int[] x = new int[320];
        	int pix = 0;
        	for(int i = 0;i<320;i++){
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
        
        
        //------------------------ inicializar variables---------------------------------------
        public void inicializarP1(){
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
             alien = BitmapFactory.decodeResource(getResources(), R.drawable.alien);
             //p2 = BitmapFactory.decodeResource(getResources(), R.drawable.pantalla2);
             explosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosion);
             fuel = 100;
             //updateFuel();
           //---------------------------------------
             colisionesBot = llenarVectorBot();
             colisionesTop = llenarVectorTop();
             
             //pixel = fondo.getPixel(20, 200);
             //---------------------------------------
             vida = vida8;
              
        }
        public void recicleP1(){
        	/*
        	vida1.recycle();
        	vida2.recycle();
        	vida3.recycle();
        	vida4.recycle();
        	vida5.recycle();
        	vida6.recycle();
        	vida7.recycle();
        	vida8.recycle();
        	*/
        	fondo.recycle();
        	speed.recycle();
        	speedItem.recycle();
        	alien.recycle();
        	//p2.recycle();
        	stars.recycle();
        }
        public void inicializarP2(){
       	 fondo = BitmapFactory.decodeResource(getResources(), R.drawable.pantalla2);
            stars = BitmapFactory.decodeResource(getResources(), R.drawable.nebula);
            speed = BitmapFactory.decodeResource(getResources(), R.drawable.speed);
            speedItem = BitmapFactory.decodeResource(getResources(), R.drawable.speed_item);
            /*vida1 = BitmapFactory.decodeResource(getResources(), R.drawable.vida1);
            vida2 = BitmapFactory.decodeResource(getResources(), R.drawable.vida2);
            vida3 = BitmapFactory.decodeResource(getResources(), R.drawable.vida3);
            vida4 = BitmapFactory.decodeResource(getResources(), R.drawable.vida4);
            vida5 = BitmapFactory.decodeResource(getResources(), R.drawable.vida5);
            vida6 = BitmapFactory.decodeResource(getResources(), R.drawable.vida6);
            vida7 = BitmapFactory.decodeResource(getResources(), R.drawable.vida7);
            vida8 = BitmapFactory.decodeResource(getResources(), R.drawable.vidafull);
            */
            alien = BitmapFactory.decodeResource(getResources(), R.drawable.alien);
            //p2 = BitmapFactory.decodeResource(getResources(), R.drawable.pantalla2);
            explosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosion_ice);
          //---------------------------------------
            colisionesBot = llenarVectorBot();
            colisionesTop = llenarVectorTop();
            
            //pixel = fondo.getPixel(20, 200);
            //---------------------------------------
            //vida = vida8;
             
       }
       public void recicleP2(){
       	vida1.recycle();
       	vida2.recycle();
       	vida3.recycle();
       	vida4.recycle();
       	vida5.recycle();
       	vida6.recycle();
       	vida7.recycle();
       	vida8.recycle();
       	fondo.recycle();
       	speed.recycle();
       	speedItem.recycle();
       	alien.recycle();
       	p2.recycle();
       	stars.recycle();
       }
       private int wBoton = 0;
       private int hBoton = 0;
       public void inicializarGameOver(){
         	 fondo = BitmapFactory.decodeResource(getResources(), R.drawable.gameover); 
         	 botonGameOverNormal = BitmapFactory.decodeResource(getResources(), R.drawable.botonreplay); 
             botonGameOverOn =  BitmapFactory.decodeResource(getResources(), R.drawable.botonreplayon);
             botonGameOver = botonGameOverNormal;
             wBoton = botonGameOverNormal.getWidth();
             hBoton = botonGameOverNormal.getHeight();
             
              //pixel = fondo.getPixel(20, 200);
              //---------------------------------------
              //vida = vida8;
               
         }
        //----cambiar color de la gasolina-------------
        public void updateFuel(){
        	recFuel.right += DFUEL;
        	
        	//colorFuel += 0x00010000;
        	fuel+=DFUEL;
        	if(fuel>25){
        		if(fuel>90){
        			colorFuel = 0xFF99D9EA;
        		}else if(fuel>80){
        			colorFuel = 0xFF3CFF7B;
        		}else if(fuel>70){
        			colorFuel = 0xFF3CFF3C;
        		}else if(fuel>60){
        			colorFuel = 0xFFA7FF3C;
        		}else if(fuel>50){
        			colorFuel = 0xFFF9FF3C;
        		}else if(fuel>40){
        			colorFuel = 0xFFFFDE3C;
        		}else if(fuel>30){
        			colorFuel = 0xFFFF632C;
        		}else if(fuel>20){
        			colorFuel = 0xFFFF2C2C;
        		}
        		
        		pFuel.setColor(colorFuel);
        	}
        }
        public void drawNoGas(Canvas canvas) {
        	//canvas.drawBitmap(stars,recf, recStars1,null);
        	//canvas.drawBitmap(stars, recf, recStars2, null);
        
        }
        public void drawNoLife(Canvas canvas){
        	
        }
        //dibujar la pantalla 1
        public void drawP1(Canvas canvas){
        	canvas.drawBitmap(stars,recf, recStars1,null);
        	canvas.drawBitmap(stars, recf, recStars2, null);
        	//                       src   dst
        	canvas.drawBitmap(fondo, rec, recf, null);
        	//------------------------ barra -----------------
        	//canvas.drawRoundRect(re,5,5, paint);
//        	canvas.drawRoundRect(re2,5,5, paint);
        	//canvas.drawRect(barra, paint);
        	//---------------------------------------
//        	canvas.drawRoundRect(recFuel, 5,5, pBFuel);
        	canvas.drawRoundRect(recFuel, 5, 5, pFuel);
        	canvas.drawRoundRect(BBFuel, 5, 5, pBBFuel);
        	
        	canvas.rotate(rot,x+ 45,y+50);
        	canvas.drawBitmap(nave.img,x, y, null);
            canvas.rotate(-rot,x+45,y+50);
            if(alienActive)
            	canvas.drawBitmap(alien,alienX, alienY, null);
            
            if(exploTop)
            	canvas.drawBitmap(explosion, x, y-30, null);
            if(exploBot)
            	canvas.drawBitmap(explosion, x, y+nave.height-30, null);
            canvas.drawBitmap(vida,700, barYi, null);
            canvas.drawText((alien.getHeight())+"|", 20, 150,paint);
            if(!speedSaved)
            	canvas.drawBitmap(speedItem, xItem, yItem, null);
            if(!started)
        		canvas.drawBitmap(taptostart,0,0 , null);
            //if(speedSaved)
//            	canvas.drawBitmap(speed, 20, 400, null);
        }
        private int xprueba = 300;
        public void drawGO(Canvas canvas){
        	canvas.drawBitmap(fondo,0, 0, null);
        	canvas.drawBitmap(botonGameOver, xprueba,350, null);
        }
        public void drawP3(Canvas canvas){
        	
        }
       /*
        public void change(boolean onOff){
        	if(onOff){
        	nave.on();
        	return;
        	}
        	nave.off();
        }
        */
        public void item(boolean onOff){
        	speedSaved = onOff; 
        }
        
        //---------------------- items---------
        public void itemSpeed(){
        	gameSpeed = 3;
        }
        public void alien(){
        	alien = explosion;
        	
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
        private boolean alienColision = false;
        private int alienColCount = 0;
        private float cont = 0;
        private float cont2 = 0;
        private int contI = 0;
        private int dir = 1;
        private int posXalien = 10;
        private int alDy = 0;
        private int vida = 8;
        private int alienOnScreen = 950;
        private boolean xAl = true;
        private int contExplo = 0;
        private int xReal = x;
        private int xvector1,xvector2 = 0;
        private int naveHeight;
        private int naveWidth; 
        private int dx = (int) mAccelY;
        private float bdx = dx;
        private Canvas c;
        private boolean _run = false;
        private boolean control = true;
        private boolean primera = true;
        public mainLoop(SurfaceHolder surfaceHolder, Panel panel) {
            _surfaceHolder = surfaceHolder;
           
            _panel = panel;
        }
 
        public void setRunning(boolean run) {
            _run = run;
        }
        private void Logic(){
        	if((started)&&(control)){
        		_panel.taptostart.recycle();
        		control = false;
        	}
        	if(a != MotionEvent.ACTION_UP){
        		started = true;
        		
        		_panel.nave.on();
        		if(cont < 100){
        			cont+=0.8;
        		}
        		_panel.updateFuel();
        		cont2 = 3;
        		y -= 0.1*cont*gameSpeed;
        	
        		//reiniciar background -------------------------
        		/*
        		recStars2.offset(-10, 0);
        		recStars1.offset(-10, 0);
        		*/
        		if(recStars1.right <= 0){
        			recStars1.offset(1600, 0);
        		}
        		if(recStars2.right <= 0){
        			recStars2.offset(1600, 0);
        		}
        		//--------------------------------------------
        	}else{
        		cont = 0;
        		y+= 0.1*cont2*gameSpeed;
        		if(started)
        			cont2+=0.8;
        		_panel.nave.off();
        	}
        	
        	yItem = yItem+dir;
        	contI++;
        	if(contI > 10){
        		dir = dir*(-1);
        		contI = 0;
        	}
        	//****************************************************************************************************
        	//--------------------------- colisiones ---------------------------------------
        	//----items--------
        	if((x+naveWidth>xItem)&&(x<xItem+itemSize)&&(y+naveHeight>yItem)&&(y<yItem+itemSize)){
        		_panel.item(true);
        		_panel.vel.run();
        		
        	}
        	//------------------
        	//-----aliens-------
        	if((x+naveWidth>alienX)&&(x<alienX+alienSize)&&(y+naveHeight>alienY)&&(y<alienY+alienSize)&&(!alienColision)&&(alienActive)){
        		_panel.alien();
        		vida--;
        		_panel.vida(vida);
        		if(x>alienX){
        			x+= 100;
        			xReal +=100;
        		}
        		if(x<=alienX){
        			x -= 100;
        			xReal -=100;
        		}   
        		alienColision = true;
        		alienColCount = 0;
        		
        	}
        	if(alienColCount < 10){
            	alienColCount++;
            	}
            if((alienColCount==10)&&(alienColision)){
           		alienColision = false;
           		alienActive = false;
           		
           	}
        	//-------------------
        	xvector1 = xReal/10;
        	xvector2 = (xReal+naveWidth)/10;
        	
        
        	if((y+naveHeight> colisionesBot[xvector1])||(y+naveHeight>colisionesBot[xvector2])){
        		vida--;
        		_panel.vida(vida);
        		if(pantalla == 1)
        			y -= 30;
        		if(pantalla == 2)
        			y -= 15;
        		
        		cont = 0;
        		cont2 = 0;
        		exploBot = true;
        		contExplo = 0;
        	}
        	if((y< colisionesTop[xvector1+4])){
        		vida--;
        		_panel.vida(vida);
        		if(pantalla == 1)
        			y += 30;
        		if(pantalla == 2)
        			y += 15;
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
        	//***************************************************************************************************
        	//------------------------------mover background al llegar a un punto--------------------------------------------------
        	if((xReal> alienOnScreen)&&(xAl)){
        		alienX = 800;
        		xAl = false;
        		posXalien =(alienOnScreen+ (800-x))/10;
        		alienActive = true;
        		alDy = 1;
        	}
        	
        	
        	if(alienActive){
        		if(alienY<colisionesTop[posXalien])
        			alDy = 1;
        		if(alienY+50> colisionesBot[posXalien])
        			alDy = -1;
        		
        	}
        	alienY += alDy;
        	if(started){
	        	if((x>450)&&(dx > 0 )){
	        		xReal = (dx + xReal);
	        		xItem -= dx;
	        		alienX -= dx;
	        		rec.offset(dx, 0);
	        		recStars2.offset(-(int)bdx, 0);
	        		recStars1.offset(-(int)bdx, 0);
	        	}else if((x<10)&&(dx<0)){
	        		
	        	}else{
	        		
	        		xReal = (dx + xReal);
	        		x= (dx +x);
	        		
	        	}
        	}
        	//-------------------------------------------------------------------------------------------------
        	//---------------------------------- cambio de pantalla -------------------------------------------
        	//-------------------------------------------------------------------------------------------------
        	if(xReal > 3000){
        		reset();
        		pantalla = 2;
        	} 
        	if((vida == 0)||(_panel.fuel <= 0)){
        		gameOver = true;
        	}
        	if(started)
        		rot = (float) (dx*9.0)/gameSpeed;
        	//c.setBitmap(fondo);
            _panel.drawP1(c);
        }  
        private void reset(){
        	if(primera){
        		_panel.recicleP1();
        		_panel.inicializarP2();
        		xReal = 0;
        		x = 0;
        		y = 200;
        		
        		rec = new Rect(0,0,800,480);
        		primera = false;
        	}
        }
        boolean controlGameOver = true;
        boolean apachoBoton = false;
        private void gameOver(){
        	if(controlGameOver){
        		switch(pantalla){
        		case 1:
        			_panel.recicleP1();
        		break;
        		case 2:
        			_panel.recicleP2();
        		break;
        		}
        		_panel.inicializarGameOver();
        		controlGameOver = false;
        	}
        	if((xTouch > 300)&&(xTouch < 300+_panel.wBoton)&&(yTouch>350)&&(yTouch<350+_panel.hBoton)){
        		_panel.botonGameOver = _panel.botonGameOverOn;
        		apachoBoton = true;
        		//_panel.xprueba = 20;
        		//Log.d("gf3","entro al if");
        	}
        	if((a == MotionEvent.ACTION_UP)&&(apachoBoton)){
        		_panel.botonGameOver = _panel.botonGameOverNormal;
        		pantalla = 1;
        		_panel.inicializarP1();
        		gameOver = false;
        		vida = 8;
        		//_panel.vida(8);
        		_panel.speedSaved = false;
        		x = 20;
        		alienActive = true;
        		xReal = 0;
        		rec = new Rect(0,0,800,480);
        		_panel.recFuel = new RectF(5,5,95,45);
        		y = 200;
        		_panel.updateFuel();
        		alienOnScreen = 950;
        		apachoBoton = false;
        		controlGameOver = true;
        	}
        	_panel.drawGO(c);
        	
        }
        	
        @Override
        public void run() {
        	naveHeight = _panel.nave.height;
        	naveWidth = _panel.nave.width;
            while (_run) {
                c = null;
                try {
                	dx = (int)((mAccelY*gameSpeed)/1.5);
                	bdx = (mAccelY/3)*gameSpeed;
                     c = _surfaceHolder.lockCanvas(null);
                    
                    synchronized (_surfaceHolder) {
                    	/*switch(pantalla){
	                    	case 1:	
	                    		p1Logic();
	                    	break;
	                    	case 2:
	                    		p2Logic();
	                    	break;
                    	}
                    	*/
                    	if(gameOver){
                    		gameOver();
                    	}else{
                    		Logic();
                    	}
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