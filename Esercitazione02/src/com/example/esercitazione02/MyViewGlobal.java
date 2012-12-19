package com.example.esercitazione02;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyViewGlobal extends View {

	final boolean enableReceiverWrite;
	double width;
	double height;
	double posx;
	double posy;
	double percent_height;
	double percent_width;
	boolean isSender;
	ProgressDialog dialog;
	String[] testo;
	Handler hand;
	Paint mSenderPaint;
	Paint mReceiverPaint;
	XMPPConnection connection;
	String scritta = "";
	Context context;
	Path pathSender = new Path();
	Path pathReceiver = new Path();

	public MyViewGlobal(Context context, boolean isSender,
			boolean enableReceiverWrite) {
		super(context);
		this.isSender = isSender;
		this.context = context;
		this.enableReceiverWrite = enableReceiverWrite;
		// TODO Auto-generated constructor stub

		// Inizializzo i due "pennelli" nero per il sender e rosso per il
		// receiver

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

		hand = new Handler();

		// Avvia la connessione in base alla variabile isSeder
		if (isSender) {
			connect_sender();
		} else
			connect_receiver();

	}

	/**
	 * @param args
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		// Vengono disegnati i path sia del Sender sia del Receiver
		canvas.drawPath(pathSender, mSenderPaint);
		canvas.drawPath(pathReceiver, mReceiverPaint);

		// Serve per ottenere la grandezza della View. Queste misure saranno
		// sate in seguito.
		width = this.getWidth();
		height = this.getHeight();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int eventaction = event.getAction();
		int touchx = (int) event.getX();
		int touchy = (int) event.getY();

		// Se sono false entrambe le variabili isSender e enableReceiverWrite
		// significa che è stata
		// disabilitata la scrittura al Receiver

		if (!enableReceiverWrite & !isSender) {

		} else {

			switch (eventaction) {
			case MotionEvent.ACTION_DOWN:

				// Poichè siamo nel case ACTION_DOWN dobbiamo solo spostare il
				// path e non disegnarlo. Questo serve se vogliamo fare più
				// disegni e non una sola linea continua.
				if (isSender)
					pathSender.moveTo(touchx, touchy);
				else
					pathReceiver.moveTo(touchx, touchy);

				// Aggiorna la grafica
				invalidate();

				// Questi calcoli sotto servono per ottenere la posizione del
				// tocco in percentuale rispetto alla grandezza della view.
				// Tutto ciò serve per evitare problemi se il programma gira su
				// terminali con risoluzioni diverse.
				double percent_height = touchy / height * 100;
				double percent_width = touchx / width * 100;

				// Genera il messaggio da inviare tramite XMPP
				scritta = "DOWN;" + percent_width + ";" + percent_height;

				// Avvia il thread per l'invio dei messaggi
				send();

				break;
			case MotionEvent.ACTION_MOVE:

				// Ovviamente tutto ciò che è stato detto sopra è valido anche
				// in questo caso. L'unica differenza sta nel fatto che ora
				// siamo in ACTION_MOVE quindi il path verrà disegnato fino alle
				// nuove coordinate e non spostato come prima.
				if (isSender)
					pathSender.lineTo(touchx, touchy);
				else
					pathReceiver.lineTo(touchx, touchy);

				invalidate();

				percent_height = touchy / height * 100;
				percent_width = touchx / width * 100;

				scritta = "MOVE;" + percent_width + ";" + percent_height;

				send();

				break;
			case MotionEvent.ACTION_UP:
				// Questo evento può anche non essere preso in considerazione
				// dato che tutti gli spostamenti ed i disegni vengono
				// effettuati tramite ACTION_DOWN e ACTION_MOVE

				percent_height = touchy / height * 100;
				percent_width = touchx / width * 100;

				scritta = "UP;" + percent_width + ";" + percent_height;

				send();
				break;
			default:
				break;
			}
		}
		;
		return true;
	}

	// Tutte le parti che riguardano le connessioni ad internet le ho dovute
	// inserire a priori dentro Thread separati perchè dalla versione 2.3 di
	// android in poi è stato vietato l'uso di connessioni ad internet nel
	// thread principale
	public void connect_receiver() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				// Configurazione per connettersi al server (indirizzo e porta)
				ConnectionConfiguration config = new ConnectionConfiguration(
						"ppl.eln.uniroma2.it", 5222);

				// Sicurezza disabilitata per le nostre prove
				config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

				// Imposto la configurazione nella connessione
				connection = new XMPPConnection(config);

				try {

					// Crea una ProgressDialog per informare l'utente che si sta
					// connettendo al server.
					// Se infatti inviassi messaggi prima della connessione al
					// server verrebbe generato un errore.
					// Molti elementi, come per esempio questa ProgressDialog,
					// per essere creati o modificati devono essere richiamati
					// nel thread principale, per questo uso il metodo post
					// dell'handler per gestirli.
					hand.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							dialog = ProgressDialog.show(context,
									"Esercitazione02",
									"Connessione al server in corso...", true);
							dialog.show();
						}
					});

					// Ci connettiamo alla chat
					connection.connect();

					// Username e password per accedere
					connection.login("fioravanti2", "qwerty");
					Log.d("INFO", "CONNESSO AL SERVER");

					// Una volta connesso chiudo la ProgessDialog
					hand.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});

					connection.addPacketListener(new PacketListener() {

						@Override
						public void processPacket(Packet arg0) {
							// TODO Auto-generated method stub

							// Esegue il casting dell'arg0
							Message msg = (Message) arg0;

							// Prende i vari segmenti del messaggio
							final String to = msg.getTo();
							String body = msg.getBody();

							// Splitta il messaggio per eliminare tutto ciò che
							// c'è dopo la chiocciola
							final String[] from = msg.getFrom().split("@");
							// Log.d("XMPPChat", "Hai ricevuto un messaggio: "
							// + from[0] + " " + to + " " + body);

							// Se i messaggi sono stati inviati da un utente
							// diverso da fioravanti* esce dalla void
							if (!from[0].startsWith("fioravanti"))
								return;

							testo = body.split(";");

							// Log.d("XMP", "Testo0=" + testo[0] + " |Testo1= "
							// + testo[1] + " |Testo2= " + testo[2]);

							// Riesegue i calcoli al contrario per riportare le
							// percentuali in coordinate assolute
							posx = width / 100 * Double.parseDouble(testo[1]);
							posy = height / 100 * Double.parseDouble(testo[2]);

							hand.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									// Controlla che tipo di messaggio è stato
									// ricevuto
									if (testo[0].equals("SENDERDOWN")) {
										pathSender.moveTo((float) posx,
												(float) posy);

									} else if (testo[0].equals("SENDERMOVE")) {
										pathSender.lineTo((float) posx,
												(float) posy);
									} else if (testo[0].equals("SENDERUP")) {
										// pathSender.lineTo((float)posx,(float)posy);
									}
									;

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

	public void connect_sender() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				// Configurazione per connettersi al server (indirizzo e porta)
				ConnectionConfiguration config = new ConnectionConfiguration(
						"ppl.eln.uniroma2.it", 5222);

				// Sicurezza disabilitata per le nostre prove
				config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

				// Imposto la configurazione nella connessione
				connection = new XMPPConnection(config);

				try {

					// Crea una ProgressDialog per informare l'utente che si sta
					// connettendo al server.
					// Se infatti inviassi messaggi prima della connessione al
					// server verrebbe generato un errore.
					hand.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub

							dialog = ProgressDialog.show(context,
									"Esercitazione02",
									"Connessione al server in corso...", true);
							dialog.show();
						}
					});

					// Ci connettiamo alla chat
					connection.connect();

					// Username e password per accedere
					connection.login("fioravanti1", "qwerty");

					Log.d("INFO", "CONNESSO AL SERVER");

					// Una volta connesso chiudo la ProgessDialog
					hand.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});

					connection.addPacketListener(new PacketListener() {

						@Override
						public void processPacket(Packet arg0) {
							// TODO Auto-generated method stub

							// Esegue il casting dell'arg0
							Message msg = (Message) arg0;

							// Prende i vari segmenti del messaggio
							final String to = msg.getTo();
							String body = msg.getBody();

							// Splitta il messaggio per eliminare tutto ciò che
							// c'è dopo la chiocciola
							final String[] from = msg.getFrom().split("@");
							// Log.d("XMPPChat", "Hai ricevuto un messaggio: "
							// + from[0] + " " + to + " " + body);

							// Se i messaggi sono stati inviati da un utente
							// diverso da fioravanti* esce dalla void
							if (!from[0].startsWith("fioravanti"))
								return;

							testo = body.split(";");
							// Log.d("XMP",body.toString());
							// Log.d("XMP", "Testo0=" + testo[0] + " |Testo1= "
							// + testo[1] + " |Testo2= " + testo[2]);

							// Riesegue i calcoli al contrario per riportare le
							// percentuali in coordinate assolute
							posx = width / 100 * Double.parseDouble(testo[1]);
							posy = height / 100 * Double.parseDouble(testo[2]);

							hand.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub

									// Controlla che tipo di messaggio è stato
									// ricevuto
									if (testo[0].equals("RECEIVERDOWN")) {
										pathReceiver.moveTo((float) posx,
												(float) posy);
										Log.d("XMP", "DOWN" + posx);

									} else if (testo[0].equals("RECEIVERMOVE")) {
										pathReceiver.lineTo((float) posx,
												(float) posy);
									} else if (testo[0].equals("RECEIVERUP")) {
										// pathReceiver.lineTo((float)posx,(float)posy);
									}
									;

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

	public void send() {
		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				// Creo il messaggio
				Message msg = new Message();

				// Imposta come destinatario tutti gli utenti presenti sul
				// server
				msg.setTo("all@broadcast.ppl.eln.uniroma2.it");

				// Controllo se sono Sender o Receiver. In base a questo
				// controllo aggiungo una stringa davanti al messaggio per
				// identificare chi manda i messaggi.
				if (isSender)
					scritta = "SENDER" + scritta;
				else
					scritta = "RECEIVER" + scritta;

				Log.d("XMPPChat", "Hai scritto: " + scritta);

				// Inserisce il testo della TextView
				msg.setBody(scritta);

				// Viene spedito il pacchetto
				connection.sendPacket(msg);

				// Il messaggio che invio sono di questo tipo:
				// SENDERMOVE;50;47
				// RECEIVERDOWN;14;65
				//
				// Dove ho usato come delimitatore il carattere ";" per
				// distinguere le parti del messaggio.
				// La prima indica se il SENDER o se il RECEIVER ha eseguito una
				// ACTION_DOWN o una ACTION_MOVE mentre la seconda e terza parte
				// del messaggio indicano le coordinate dell'azione espresse in
				// percentuale rispetto alla View di origine
			}
		};
		new Thread(runnable).start();
	}

}
