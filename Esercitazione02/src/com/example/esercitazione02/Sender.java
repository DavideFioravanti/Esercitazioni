package com.example.esercitazione02;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

	
	public class Sender extends Activity {

		private XMPPConnection connection;
		String scritta = "";

		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//setContentView(new MyViewSender(this));
			setContentView(new MyViewGlobal(this,true));
			
			/*
			ConnectionConfiguration config= new ConnectionConfiguration("ppl.eln.uniroma2.it",5222); // Configurazione per connettersi al server (indirizzo e porta)
	        
	        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled); // Sicurezza disabilitata per le nostre prove
	        
	        connection = new XMPPConnection(config); // Imposto la configurazione nella connessione
	     
	        
	        
	        try {
	        	connection.connect(); // Ci connettiamo alla chat
	        
				connection.login("fioravanti", "qwerty"); //Username e password per accedere
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        connection.addPacketListener(new PacketListener() {
				
				@Override
				public void processPacket(Packet arg0) {
					// TODO Auto-generated method stub
					Message msg = (Message) arg0; // Esegue il casting dell'arg0
					final String to = msg.getTo(); // Prende i vari segmenti del messaggio
					final String body = msg.getBody();
					final String[] from = msg.getFrom().split("@"); // Splitta il messaggio per eliminare tutto ciò che c'è dopo la chiocciola
					Log.d("XMPPChat","Hai ricevuto un messaggio: " + from + " " + to +" " + body);
				
					
					
				}
			}, new MessageTypeFilter(Message.Type.normal));
	        
	        
	        
	        Log.d("XMPPChat","Hai scritto: "+ scritta);
						
			Message msg = new Message(); // Creo il messaggio
			
			msg.setTo("all@broadcast.ppl.eln.uniroma2.it"); // Imposta come destinatario tutti gli utenti presenti sul server
			
			msg.setBody(scritta); // Inserisce il testo della TextView
			
			connection.sendPacket(msg); //Viene spedito il pacchetto
	        */
	        
			
		}
	

}
