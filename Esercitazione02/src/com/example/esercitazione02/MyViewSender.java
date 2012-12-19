package com.example.esercitazione02;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class MyViewSender extends View {

	private int x = 100;
	private int y = 100;
	private Bitmap bmp = null;
	private boolean selected = false;
	Paint mPaint;


	public MyViewSender(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);
		path.moveTo(x, y);
		mPaint = new Paint();
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.STROKE);    
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(10);
		mPaint.setColor(Color.BLACK);
		mPaint.setAntiAlias(true);
		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();

		this.context=context;
			connect();
		
	}
	
	private XMPPConnection connection;
	String scritta = "";
	Context context;
	private double width;
	private double height;
	
	

	/**
	 * @param args
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(bmp, x, y, null);

		canvas.drawPath(path, mPaint);
	}
	Path path = new Path();
	int deltax=0;
	int deltay=0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int eventaction = event.getAction();
		int touchx = (int) event.getX();
		int touchy = (int) event.getY();
		
		width = this.getWidth();
		height= this.getHeight();

		switch (eventaction) {
		case MotionEvent.ACTION_DOWN:
			if (touchx > x & touchx < x + bmp.getWidth() & touchy > y
					& touchy < y + bmp.getHeight()) {
				selected = true;
			deltax=touchx-x;
			deltay=touchy-y;
			
			}
			x = touchx-deltax;
			y = touchy-deltay;
			path.moveTo(touchx, touchy);
			invalidate();
			
			double percent_height= touchy/height*100;
			double percent_width= touchx/width*100;
			
			scritta= "DOWN;"+percent_width+";"+percent_height;
			
			send();
			
			break;
		case MotionEvent.ACTION_MOVE:
		
			
			x = touchx-deltax;
			y = touchy-deltay;
			path.lineTo(touchx, touchy);
			invalidate();
			 percent_height= touchy/height*100;
			 percent_width= touchx/width*100;
			
			scritta= "MOVE;"+percent_width+";"+percent_height;
			
			send();
			
			break;
		case MotionEvent.ACTION_UP:
			selected = false;
			 percent_height= touchy/height*100;
			 percent_width= touchx/width*100;
			
			scritta= "UP;"+percent_width+";"+percent_height;
			
			send();
			break;
		default:
			break;
		}
		return true;
	}

	
	public void connect(){
Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
		
		ConnectionConfiguration config= new ConnectionConfiguration("ppl.eln.uniroma2.it",5222); // Configurazione per connettersi al server (indirizzo e porta)
        
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled); // Sicurezza disabilitata per le nostre prove
        
        connection = new XMPPConnection(config); // Imposto la configurazione nella connessione
     
     
		
		
        try {
        	connection.connect(); // Ci connettiamo alla chat
        
			connection.login("fioravanti1", "qwerty"); //Username e password per accedere
			
			Log.d("INFO","CONNESSO AL SERVER");
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			}
			};
			new Thread(runnable).start();

        

	}
	
	public void send(){
Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				 Log.d("XMPPChat","Hai scritto: "+ scritta);
					
					Message msg = new Message(); // Creo il messaggio
					
					msg.setTo("all@broadcast.ppl.eln.uniroma2.it"); // Imposta come destinatario tutti gli utenti presenti sul server
					
					msg.setBody(scritta); // Inserisce il testo della TextView
					
					connection.sendPacket(msg); //Viene spedito il pacchetto
				}
			};
			new Thread(runnable).start();
		}
	
       
	
	
}
