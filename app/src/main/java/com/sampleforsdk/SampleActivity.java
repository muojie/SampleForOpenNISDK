package com.sampleforsdk;
import org.openni.GeneralException;
import org.openni.StatusException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

import com.asus.xtionstartkernel.DepthData;
import com.asus.xtionstartkernel.PermissionCallbacks;
import com.asus.xtionstartkernel.XtionContext;
import com.asus.xtionstartlibs.GlobalOSDService;
import com.asus.xtionstartlibs.HandTracker;
import com.asus.xtionstartlibs.RGBData;
import com.asus.xtionstartlibs.UserTracker;
import com.sampleforsdk.R;

public class SampleActivity extends Activity {
	XtionContext m_xc;
	RGBData m_rgbdata;
	  
	private PermissionCallbacks m_callbacks = new PermissionCallbacks() {

	
		@Override   
		public void onDevicePermissionGranted() {  
			
			try {
				m_rgbdata = new RGBData(m_xc);
			} catch (GeneralException e) {
				e.printStackTrace();
			} 
			  
			new Thread(new Runnable(){

				@Override
				public void run() { 
					
					try {
						m_xc.start();
					} catch (StatusException e1) {
						e1.printStackTrace();
					}
					while(true){ 
						try {
							m_xc.waitforupdate() ;
						} catch (StatusException e) {
							e.printStackTrace();
						}
					
					} 
				
				}
				
			}).start();
			m_rgbdata.videoEncodeRegister("/sdcard/avedioabc");
			
			
			new Thread(new Runnable(){

				@Override
				public void run() { 
					for(int i =0 ;i<500 ;i++){ 
						
					m_rgbdata.videoEncodeFill() ; 
					} 
					m_rgbdata.vedioEndcodeClose() ;  
				}
				
			}).start();
		}
		

		
		@Override
		public void onDevicePermissionDenied() {

		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
	
		try { 
			m_xc = new XtionContext(this, m_callbacks);
		}catch(Exception e) {  
			e.printStackTrace();
		} 
		
		
	}  
 
	  
	@Override
	protected void onDestroy() {
		super.onDestroy();
		m_rgbdata.Close() ;
		m_xc.Close();
	}

}
