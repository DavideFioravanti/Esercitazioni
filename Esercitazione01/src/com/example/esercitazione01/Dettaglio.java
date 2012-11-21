package com.example.esercitazione01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class Dettaglio extends Activity {
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettaglio); 
        
        
		TextView nomeBlogTxt = (TextView) findViewById(R.id.nomeBlog);
		TextView titoloTxt = (TextView) findViewById(R.id.titolo);
		TextView dataTxt = (TextView) findViewById(R.id.data);
		TextView descrizioneTxt = (TextView) findViewById(R.id.descrizione);
		
		//Imposto il testo, dell'articolo selezionato
		nomeBlogTxt.setText(getIntent().getStringExtra("nomeBlog"));
		titoloTxt.setText(getIntent().getStringExtra("titolo"));
		dataTxt.setText(getIntent().getStringExtra("data"));
		//Poichè il testo dell'articolo è formattato come html, uso
		//Html.fromHtml() per convertirlo senza problemi
		descrizioneTxt.setText(Html.fromHtml(getIntent().getStringExtra("descrizione")));

    }
    
}
