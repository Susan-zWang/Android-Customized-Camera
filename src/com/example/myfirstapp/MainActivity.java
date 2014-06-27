package com.example.myfirstapp;

// do not import android.R
import java.io.IOException;

import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.view.Menu;
import android.app.AlertDialog;
import android.content.Intent;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
         Button camera_button = (Button)findViewById(R.id.camera_button);    // only records 
         camera_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
			startActivity (new Intent(v.getContext(), MyCamera.class));   // goes to MyCamera.java
				// TODO Auto-generated method stub
			}
		}); // Register the onClick listener with the implementation above
        
        Button gallery_button = (Button)findViewById(R.id.gallery_button);    // unexpected exit 
        gallery_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				// TODO Auto-generated method stub
				
			}
		});
        
        Button exit_button = (Button)findViewById(R.id.exit_button);    // exit 
        exit_button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onDestroy();
				finish();
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
