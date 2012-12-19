package com.example.esercitazione01;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


public class RssHandler extends DefaultHandler  {
	
	//Per prendere un elemento bisogna creare una variabile booleana per capire se siamo
	//nell'elemento giusto e una variabile che contiene il dato che ci serve.
	//Bisogna creare tante variabili booleane quanti sono i valori che ci servono
	boolean inTitle=false;
	boolean inItem = false;
	boolean inLink = false;
	boolean inPubDate=false;
	boolean inDescription=false;
	String pubDate=null;
	String nomeBlog=null;
	
	
	//Creo 3 vettori per salvare rispettivamente i titoli delle notizie, la data,
	//la descrizione e il link all'articolo completo
	Vector<String> vTitolo= new Vector<String>();
	Vector<String> vPubDate= new Vector<String>();
	Vector<String> vDescription= new Vector<String>();
	Vector<String> vLink= new Vector<String>();	

	

	//Questo metodo serve per capire quando inizia il file xml 
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		//System.out.println("Inizio Documento!");
	}
	
	//Questo metodo serve per capire quando finisce il file xml 
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
		//System.out.println("Fine Documento!");
	}
	
	//Metodo per capire quando inizia un elemento
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		System.out.println("Inizio Elemento:"+qName);
		

		if(qName.equalsIgnoreCase("title") & inItem){
			//Se questo elemento è un titolo e si trova nel tag "item"
			//vuol dire che si tratta del titolo di una notizia
			inTitle=true;
			//Poichè ci troviamo dentro una notizia creo una riga vuota nel vettore di titoli
			vTitolo.add("");
		}else if(qName.equalsIgnoreCase("title") & !inItem){
			//Se questo elemento è un titolo ma non si trova nel tag "item"
			//vuol dire che si tratta del titolo del blog quindi non devo
			//creare una nuova riga nel vettore contenente i titoli delle notizie
			inTitle=true;
		}else if (qName.equalsIgnoreCase("item")){
			inItem=true;
		}else if (qName.equalsIgnoreCase("pubdate")){
			inPubDate=true;
		}else if (qName.equalsIgnoreCase("description") & inItem){
			//Se questo elemento è una descrizione e si trova nel tag "item"
			//vuol dire che si tratta della descrizione di una notizia
			//altrimenti sarebbe la descrizione del blog
			inDescription=true;
			//Come nel caso precedente inserisco una riga vuota nel vettore delle descrizioni
			vDescription.add("");
		}else if (qName.equalsIgnoreCase("link") & inItem){
			//Se questo elemento è un link e si trova nel tag "item"
			//vuol dire che si tratta del link di una notizia
			//altrimenti sarebbe il link del blog
			inLink=true;
			//Come nel caso precedente inserisco una riga vuota nel vettore delle descrizioni
			vLink.add("");
		}
		

	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		//System.out.println("Fine Elemento:"+qName);
		
		if(qName.equalsIgnoreCase("title") & inItem){
			inTitle=false;
		}else if(qName.equalsIgnoreCase("title") & !inItem){
			inTitle=false;
		}else if (qName.equalsIgnoreCase("pubdate")){
			inPubDate=false;
		}else if (qName.equalsIgnoreCase("item")){
			inItem=false;
		}else if (qName.equalsIgnoreCase("description") & inItem){
			inDescription=false;
		}else if (qName.equalsIgnoreCase("link") & inItem){
			inLink=false;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		
		
		String s=new String(ch,start,length);

		//Il parser aveva evidenti problemi con le stringhe di testo e quindi in caso
		//di caratteri speciali divideva le stringhe considerandole 2 diverse.
		//Per ovviare a questo problema, creo una riga del vettore
		//ogni volta che entro in un tag di cui ho bisogno. Quindi fino a quando
		//non finisce il tag in questione, il programma continua ad accodare il testo
		//nell'ultima riga del vettore.
		
		if (inTitle & inItem){
			vTitolo.set(vTitolo.size()-1,vTitolo.lastElement()+s);	
		}
		else if (inDescription & inItem){
			vDescription.set(vDescription.size()-1,vDescription.lastElement()+s);
		}else if (inLink & inItem){
			vLink.set(vLink.size()-1,vLink.lastElement()+s);
		}
		//Il titolo e la data non sembrano essere interessati da questo problema.
		else if (inTitle & !inItem){
			nomeBlog=s;	
		}
		else if (inPubDate & inItem){
			vPubDate.add(s);
		}

	}
}
