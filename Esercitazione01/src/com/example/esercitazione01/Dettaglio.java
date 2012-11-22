package com.example.esercitazione01;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Dettaglio extends Activity {
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dettaglio); 
        
        
		TextView nomeBlogTxt = (TextView) findViewById(R.id.nomeBlog);
		TextView titoloTxt = (TextView) findViewById(R.id.titolo);
		TextView dataTxt = (TextView) findViewById(R.id.data);
		TextView descrizioneTxt = (TextView) findViewById(R.id.descrizione);
		
		//Prelevo i dati passati dalla prima activity e li inserisco
		//nelli vari controlli di questa pagina.
		nomeBlogTxt.setText(getIntent().getStringExtra("nomeBlog"));
		titoloTxt.setText(getIntent().getStringExtra("titolo"));
		dataTxt.setText(getIntent().getStringExtra("data"));
		//Poichè il testo dell'articolo è formattato come html, uso
		//Html.fromHtml() per convertirlo senza problemi
		descrizioneTxt.setText(Html.fromHtml(getIntent().getStringExtra("descrizione")));

		Button linkBtn = (Button) findViewById(R.id.continua);
		
		linkBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = Dettaglio.this.getIntent().getStringExtra("link");
				Intent i = new Intent(Intent.ACTION_VIEW);
				//Imposto come uri, l'url del link in modo che il cellulare
				//capisca in automatico come apire un collegamento ad internet
				//ed eventualmente lasci all'utente la scelta del browser
				i.setData(Uri.parse(url));
				startActivity(i);
			}	
		}
		);
    }
    
}
