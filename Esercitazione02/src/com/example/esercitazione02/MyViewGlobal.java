package com.example.esercitazione02;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class MyViewGlobal extends View {

	
	private int x = 100;
	private int y = 100;
	private Bitmap bmp = null;
	private boolean selected = false;
	Paint mSenderPaint;
	Paint mReceiverPaint;


	public MyViewGlobal(Context context, boolean sender) {
		super(context);
		this.sender=sender;
		// TODO Auto-generated constructor stub
		bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);
		pathSender.moveTo(x, y);
		mSenderPaint = new Paint();
		mSenderPaint.setDither(true);
		mSenderPaint.setStyle(Paint.Style.STROKE);    
		mSenderPaint.setStrokeJoin(Paint.Join.ROUND);
		mSenderPaint.setStrokeCap(Paint.Cap.ROUND);
		mSenderPaint.setStrokeWidth(10);
		mSenderPaint.setColor(Color.BLACK);
		mSenderPaint.setAntiAlias(true);
		
		mReceiverPaint = new Paint();
		mReceiverPaint.setDither(true);
		mReceiverPaint.setStyle(Paint.Style.STROKE);    
		mReceiverPaint.setStrokeJoin(Paint.Join.ROUND);
		mReceiverPaint.setStrokeCap(Paint.Cap.ROUND);
		mReceiverPaint.setStrokeWidth(10);
		mReceiverPaint.setColor(Color.RED);
		mReceiverPaint.setAntiAlias(true);

		
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();

		this.context=context;
		
		hand = new Handler();
		//aggiorna();
		if (sender) {
			connect_sender();
		}	else
		connect_receiver();
		
	}
	
	private XMPPConnection connection;
	String scritta = "";
	Context context;
	private double width;
	private double height;
	String[] testo;
	Handler hand;
	double posx;
	double posy;
	double percent_height;
	double percent_width;
	boolean sender;
	
	
	/**
	 * @param args
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.drawBitmap(bmp, x, y, null);

		canvas.drawPath(pathSender, mSenderPaint);
		canvas.drawPath(pathReceiver, mReceiverPaint);
		
		width = this.getWidth();
		height= this.getHeight();
	}
	Path pathSender = new Path();
	Path pathReceiver = new Path();
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
			
			if (sender)
				pathSender.moveTo(touchx, touchy);
			else 
				pathReceiver.moveTo(touchx, touchy);
			
			invalidate();
			
			double percent_height= touchy/height*100;
			double percent_width= touchx/width*100;
			
			scritta= "DOWN;"+percent_width+";"+percent_height;
			
			send();
			
			break;
		case MotionEvent.ACTION_MOVE:
		
			
			x = touchx-deltax;
			y = touchy-deltay;
			if (sender)
			pathSender.lineTo(touchx, touchy);
			else 
			pathReceiver.lineTo(touchx,touchy);
			
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
	


	public void connect_receiver(){
Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
		
		ConnectionConfiguration config= new ConnectionConfiguration("ppl.eln.uniroma2.it",5222); // Configurazione per connettersi al server (indirizzo e porta)
        
        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled); // Sicurezza disabilitata per le nostre prove
        
        connection = new XMPPConnection(config); // Imposto la configurazione nella connessione
     
     
		
		
        try {
        	connection.connect(); // Ci connettiamo alla chat
        
			connection.login("fioravanti2", "qwerty"); //Username e password per accedere
			
			Log.d("INFO","CONNESSO AL SERVER");
			
			 connection.addPacketListener(new PacketListener() {
					
					@Override
					public void processPacket(Packet arg0) {
						// TODO Auto-generated method stub
						Message msg = (Message) arg0; // Esegue il casting dell'arg0
						final String to = msg.getTo(); // Prende i vari segmenti del messaggio
						 String body = msg.getBody();
						final String[] from = msg.getFrom().split("@"); // Splitta il messaggio per eliminare tutto ciò che c'è dopo la chiocciola
						
						Log.d("XMPPChat","Hai ricevuto un messaggio: " + from + " " + to +" " + body);
						testo=body.split(";");
						//Log.d("XMP",body.toString());
						Log.d("XMP","Testo0="+ testo[0] + " |Testo1= " + testo[1] +" |Testo2= "+testo[2] );
						
						
						 posx = width/100*Double.parseDouble(testo[1]);
						 posy = height/100*Double.parseDouble(testo[2]);
						
						 hand.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (testo[0].equals("SENDERDOWN")){
									pathSender.moveTo((float)posx,(float)posy);
									Log.d("XMP","DOWN" + posx );
									
								}else if (testo[0].equals("SENDERMOVE")){
									pathSender.lineTo((float)posx,(float)posy);
								}else if(testo[0].equals("SENDERUP")){
									pathSender.lineTo((float)posx,(float)posy);
								};
						
							invalidate();
							}
						});
						
						
						
						
					}
				}, new MessageTypeFilter(Message.Type.normal));
			
			
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			}
			};
			new Thread(runnable).start();

        

	}
	

	public void connect_sender(){
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
			
			
			
			 connection.addPacketListener(new PacketListener() {
					
					@Override
					public void processPacket(Packet arg0) {
						// TODO Auto-generated method stub
						Message msg = (Message) arg0; // Esegue il casting dell'arg0
						final String to = msg.getTo(); // Prende i vari segmenti del messaggio
						 String body = msg.getBody();
						final String[] from = msg.getFrom().split("@"); // Splitta il messaggio per eliminare tutto ciò che c'è dopo la chiocciola
						
						Log.d("XMPPChat","Hai ricevuto un messaggio: " + from + " " + to +" " + body);
						testo=body.split(";");
						//Log.d("XMP",body.toString());
						Log.d("XMP","Testo0="+ testo[0] + " |Testo1= " + testo[1] +" |Testo2= "+testo[2] );
						
						
						 posx = width/100*Double.parseDouble(testo[1]);
						 posy = height/100*Double.parseDouble(testo[2]);
						
						 hand.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if (testo[0].equals("RECEIVERDOWN")){
									pathReceiver.moveTo((float)posx,(float)posy);
									Log.d("XMP","DOWN" + posx );
									
								}else if (testo[0].equals("RECEIVERMOVE")){
									pathReceiver.lineTo((float)posx,(float)posy);
								}else if(testo[0].equals("RECEIVERUP")){
									pathReceiver.lineTo((float)posx,(float)posy);
								};
						
							invalidate();
							}
						});
						
						
						
						
					}
				}, new MessageTypeFilter(Message.Type.normal));
			
		
			
			
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
				
				if (sender)scritta="SENDER"+scritta;
				else scritta="RECEIVER"+scritta;
				
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
