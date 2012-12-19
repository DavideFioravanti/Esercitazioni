package com.example.esercitazione02;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

	
	public class Sender extends Activity {

		

		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//setContentView(new MyViewSender(this));
			Boolean enableReceiverWrite= this.getIntent().getBooleanExtra("enableReceiverWrite",true);
			setContentView(new MyViewGlobal(this,true,enableReceiverWrite));
			
						
		}
	

}
