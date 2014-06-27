package com.example.myfirstapp;

import java.io.File;
import java.io.FileNotFoundException;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
//import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;



public class MyCamera extends Activity {
	 final int ACTION_VIDEO_CAPTURE = 0;     
	 final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
     String CaptureName="My_Video";
     /* replacing below line with other format for making a file. The new file initializer puts My_Video in the
      * public movie directory of the external storage. It also does not make it private because so far there is
      * seemingly no need for that. The intention with the file is to create an output stream with it and 
      * hopefully write to that output stream with the video obtained */
     /*new*/ File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), CaptureName);
    
     /*original*///private File file = new File (Environment.getExternalStorageDirectory()+"/"+CaptureName);   //   getExternalStorageDirectory()+"/NewFolder"
     //private File file = new File(getApplicationContext().getFilesDir() + "/new_folder/");   //this.getApplicationContext().getFilesDir() + "/"+ CaptureName
 
     
     /*  5/23 adding FileOutputStream   */  
   //  FileOutputStream fileout_stream = null;
   //  OutputStream out_file = new FileOutputStream(file);
 
      Uri OutputfileUri = Uri.fromFile(file);	   	  
	
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		Intent VideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);     // create intent

		 VideoIntent.putExtra( MediaStore.EXTRA_OUTPUT,OutputfileUri );    // put it into another folder
	    VideoIntent.putExtra( MediaStore.EXTRA_VIDEO_QUALITY, 1);
	    startActivityForResult(VideoIntent, ACTION_VIDEO_CAPTURE);  // this function allows you to start activity and get some data back  
	    
    
    }

    
    
    protected void onActivityResult(int RequestCode, int ResultCode, Intent VideoIntent){
Toast.makeText(this,"~~~~~~~~~~~~start onActivityResult~~~~~~~~~~~~~", Toast.LENGTH_LONG).show();     // for testing 
		super.onActivityResult(ResultCode, ResultCode, VideoIntent);		
		switch(RequestCode){
		case 0:      // start videotaping
			switch(ResultCode){      // RESULT cases!!!
				case Activity.RESULT_OK:
					if(file.exists()){
						Toast.makeText(this,"Saved as "+file.getAbsoluteFile(), Toast.LENGTH_LONG).show();    // display message "saved"
					}
					else{
						AlertDialog.Builder alert = new AlertDialog.Builder(this);
						alert.setTitle("Error").setMessage("Returned OK, but not saved.").show();
					}
					break;
				case Activity.RESULT_CANCELED:    // videotaping failed..????
					break;
				default:
					Toast.makeText(this, "unexpected resultcode: ", Toast.LENGTH_LONG).show();  // show message "unexpected"
				break;
			}
		
			default:   // error... should not go there 
				Toast.makeText(this, "unexpected weird thing happened...", Toast.LENGTH_LONG).show(); // show message "unexpected"
			break; 
		}	
		try {
			ExtractVideoFrame(file);
		} catch (IOException e) {
			Toast.makeText(this,"~~~~~~~~~~~~IOexception~~~~~~~~~~~~~", Toast.LENGTH_LONG).show();     // for testing 
			e.printStackTrace();
		}		
Toast.makeText(this,"~~~~~~~~~~~~finish onActivityResult~~~~~~~~~~~~~", Toast.LENGTH_LONG).show();     // for testing (the end end)
		finish();
	}
	

	@SuppressLint("NewApi")
	protected void ExtractVideoFrame(File new_file) throws IOException{
Toast.makeText(this,"~~~~~~~~~~~~start extracting~~~~~~~~~~~~~", Toast.LENGTH_LONG).show();     // for testing 

Toast.makeText(this,"~~file_name: ~~~" + new_file.getName(), Toast.LENGTH_LONG).show();     // for testing 

		MediaMetadataRetriever dataRetriever = new MediaMetadataRetriever();
		dataRetriever.setDataSource(new_file.getAbsolutePath());      // set up input data source
		String mDuration = dataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);    //extract data to get duration time
		
Toast.makeText(this,"~~~~~~~~~~mDuration:" + mDuration, Toast.LENGTH_LONG).show();     // for testing 

		
		
Toast.makeText(this,"~~~~~~~~~~folder path:" + new_file.length(), Toast.LENGTH_LONG).show();     // for testing 

		Bitmap bitmapOriginal = dataRetriever.getFrameAtTime(0);     // set up bitmap for getting height and width and pixel
		int bitmapVideoHeight = bitmapOriginal.getHeight();         // frame's height
		int bitmapVideoWidth = bitmapOriginal.getWidth();          // frame's width
		
//		Log.d(......)   debug use... useless..?????
		
		byte [] FinalByteArray = new byte [200];     // last byte array to export
		
		float factor = 0.5f;       //have no idea whats factor doing for......  original 0.2
		int scaledHeight = (int) ((float)bitmapVideoHeight * factor );   // scaled height 
		int scaledWidth = (int) ((float)bitmapVideoWidth * factor );    // scaled width

		int max = (int) Short.parseShort(mDuration);     // used for condition for FOR loop		
		int quality = 100;     // quality can be changed later.

		for (int i =0; i<3; ++i)   // i < max
		{			
			
			 bitmapOriginal = dataRetriever.getFrameAtTime(i * 100, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);  // update bitmap
			 
			 bitmapVideoHeight = bitmapOriginal.getHeight();    // get updated height
			 bitmapVideoWidth = bitmapOriginal.getWidth();     // get updated width
			 
			 int byteCount = bitmapOriginal.getWidth() * bitmapOriginal.getHeight() * 4;   //set byte count 
			 
	         ByteBuffer tmpByteBuffer = ByteBuffer.allocate(byteCount);       
			 
	         bitmapOriginal.copyPixelsToBuffer(tmpByteBuffer);    // copy pixel 
	         byte [] tmpByteArray = tmpByteBuffer.array();       // temporary array, have to set equal to final array later
	         
	         if ( !Arrays.equals(tmpByteArray, FinalByteArray)){    // if two arrays are not equal to each other

			 File outputFile = new File ( new_file.getAbsolutePath(),"IMG"+(i+1)+".PNG");   // new file to output    !!!  new_file.getAbsolutePath() change to getApplicationContext().getFilesDir() 
if(new_file.isFile())
{
			 Toast.makeText(this,"~~~~~~~~~~~~outputFile path: " + outputFile.getAbsolutePath(), Toast.LENGTH_LONG).show();     // for testing  
}
			 Bitmap bmpScaledSize = Bitmap.createScaledBitmap(bitmapOriginal, scaledWidth, scaledHeight, true);  // new scaled bitmap			 
	
			// OutputStream outputStream = new FileOutputStream(outputFile);   //out stream   Original: (outputFile)  <->outputFile.getAbsolutePath()			
			// outputStream.write(tmpByteArray);
			 //bmpScaledSize.compress(CompressFormat.PNG, quality, outputStream);  
			// outputStream.close();
			 FinalByteArray = tmpByteArray;
			 
	         }	
	                 
		}
		dataRetriever.release();	
Toast.makeText(this,"~~~~~~~~~~~~end extracting~~~~~~~~~~~~~", Toast.LENGTH_LONG).show();     // for testing 


	}
	

    
}

