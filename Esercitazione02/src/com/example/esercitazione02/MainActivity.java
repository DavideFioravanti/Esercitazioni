package com.example.esercitazione02;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button btnSender = (Button) findViewById(R.id.button1);
		Button btnReceiver = (Button) findViewById(R.id.button2);
		//Connetti();
		btnSender.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,Sender.class);
				startActivity(intent);
			}
		});


		btnReceiver.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,Receiver.class);
				startActivity(intent);
			}
		});
		
		
	}
	
	XMPPConnection connection;
	
	public void Connetti(){
		Runnable runnable =  new Runnable() {
			public void run() {

				ConnectionConfiguration config= new ConnectionConfiguration("ppl.eln.uniroma2.it",5222); // Configurazione per connettersi al server (indirizzo e porta)
		        
		        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled); // Sicurezza disabilitata per le nostre prove
		        
		        connection = new XMPPConnection(config); // Imposto la configurazione nella connessione
		     

		        
		        	try {
						connection.connect();
						connection.login("fioravanti", "qwerty"); //Username e password per accedere
						
						Log.d("INFO","CONNESSO AL SERVER");
						
						connection.getUser();
						
						
						AccountManager am = connection.getAccountManager();
						
						//am.createAccount("fioravanti1", "qwerty");
						Log.d("INFO",""+am.supportsAccountCreation());
						
					} catch (XMPPException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} // Ci connettiamo alla chat
		        
					
		        
			}
		};
		new Thread(runnable).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
