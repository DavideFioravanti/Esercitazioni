package com.example.esercitazione02;

import android.app.Activity;
import android.os.Bundle;

public class Receiver extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(new MyViewReceiver(this));
		Boolean enableReceiverWrite= this.getIntent().getBooleanExtra("enableReceiverWrite",true);
		setContentView(new MyViewGlobal(this,false,enableReceiverWrite));
		
		
		
		
	}

}
