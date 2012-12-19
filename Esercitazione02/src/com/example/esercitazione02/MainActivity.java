package com.example.esercitazione02;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

/*
 * Ho creato un solo progetto android che gestisce sia il lato "sender" sia il lato "receiver" dell'esercitazione
 * in modo da poter lavorare su un unico progetto. Tuttavia questo ha richiesto una modifica sostaziale alla classe
 * MyView in modo da poter gestire simultaneamente sia la parte sender sia recever (ora chiamata MyViewGlobal). In realtà allo stato pratico,
 * per come ho strutturato il progetto l'unica differenza tra il sender e il receiver sono i dati di accesso alla chat
 * XMPP e il colore usato per disegnare. Infatti ho deciso di dare la possibilità anche al Receiver di poter disegnare
 * dato che tale modifica avrebbe richiesto poche modifiche. Tuttavia per rispettare le indicazioni dell'esercitazione
 * ho inserito una variabile booleana all'interno della classe MyViewGlobal per poter disattivare la scrittura da parte
 * del receiver.
 */

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button btnSender = (Button) findViewById(R.id.button1);
		Button btnReceiver = (Button) findViewById(R.id.button2);

		btnSender.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, Sender.class);
				startActivity(intent);
			}
		});

		btnReceiver.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(MainActivity.this,
								Receiver.class);
						switch (which) {
						case DialogInterface.BUTTON_POSITIVE:
							intent.putExtra("enableReceiverWrite", true);
							break;

						case DialogInterface.BUTTON_NEGATIVE:
							intent.putExtra("enableReceiverWrite", false);
							break;
						}
						startActivity(intent);
					}
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setMessage("Il receiver può disegnare?")
						.setPositiveButton("Si", dialogClickListener)
						.setNegativeButton("No", dialogClickListener).show();

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
