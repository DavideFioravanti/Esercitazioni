package com.example.esercitazione01;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class Esercitazione01 extends Activity {

	private final static String TITOLO_KEY = "titolo";
	private final static String DATA_KEY = "data";
	static RssHandler handler;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
        
        
        
        try {
        
        	//Stringhe di feed xml per fare prove
        	String url ="http://www.tecnophone.it/feed/";
        	//String url ="http://www.nasa.gov/rss/image_of_the_day.rss";
        	//String url ="http://feeds.feedburner.com/Androidiani?format=xml";
        	
        	//Creo ed inizializzo il parser
        	SAXParserFactory factory = SAXParserFactory.newInstance(); 
        	SAXParser parser;
        	parser = factory.newSAXParser();
        	InputStream in = new URL (url).openStream(); //Se non esiste il file crea una eccezione
        	handler=new RssHandler();
        	XMLReader reader = parser.getXMLReader();
        	reader.setContentHandler(handler);
        	reader.parse(new InputSource(in));//Questo è il momento in cui scarica veramente e legge il file
		
        	TextView nomeBlogTxt = (TextView) findViewById(R.id.nomeBlog);
        	nomeBlogTxt.setText(handler.nomeBlog);
	
        } catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        //Uso un adapter così che la lunghezza della listview
        //vari in automatico in base alle notizie presenti nel feed xml
        
    	// Inizializziamo la struttura di oggetti
		List<? extends Map<String,?>> listData = createItems();
		// Definiamo le chiavi da rappresentare
		String[] keys = new String[]{TITOLO_KEY,DATA_KEY};
		// Creiamo l'array delle View associate
		int[] views = new int[]{R.id.rigaTitolo,R.id.rigaData};
		// Creiamo il SimpleAdapter
		SimpleAdapter simpleAdapter = new SimpleAdapter(this,listData,R.layout.riga,keys,views);
		// Otteniamo il riferimento alla ListView
		ListView listView = (ListView) findViewById(R.id.listView1);
		// Associamo l'adapter alla View
		listView.setAdapter(simpleAdapter);

		//Aggiungo un OnItemClickListner() per capire quale notizia è stata cliccata
		listView.setOnItemClickListener(new OnItemClickListener() {

		       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		           Intent intent = new Intent(Esercitazione01.this, Dettaglio.class);
		           //In base a quale notizia è stata cliccata, mando il titolo del blog,
		           //il titolo della notizia, la data, la descrizione e il link
		           //tramite il metodo putExtra() alla nuova activity.
		           intent.putExtra("nomeBlog", handler.nomeBlog);
		           intent.putExtra("titolo", handler.vTitolo.elementAt(position));
		           intent.putExtra("data", handler.vPubDate.elementAt(position));
		           intent.putExtra("descrizione", handler.vDescription.elementAt(position));
		           intent.putExtra("link", handler.vLink.elementAt(position));
		           startActivity(intent);
		    	   
		           //Toast.makeText(Esercitazione01.this,"Cliccato: "+position, Toast.LENGTH_SHORT).show();
		       }
		   });		
    }
  
    
	//Creo la mappa da usare nel simpleAdapter
	private List<? extends Map<String,?>> createItems()  {
		ArrayList<Map<String,Object>> lista = new ArrayList<Map<String,Object>>();
		for(int i=0;i<handler.vTitolo.size();i++){
			Map<String,Object> data = new HashMap<String,Object>();
			data.put(TITOLO_KEY,handler.vTitolo.elementAt(i));
			data.put(DATA_KEY,handler.vPubDate.elementAt(i));
			lista.add(data);
		}
		return lista;
	}
	

}
