package mycom.example.project_and01;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class MyPreview extends SurfaceView implements SurfaceHolder.Callback{
	
	Context mc;
	SurfaceHolder mHolder;
	Camera camera;
	
	public MyPreview(Context context) {
		super(context);
		mc=context;
		mHolder=getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
	//==Ã³¸®±â
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera=Camera.open();
		try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {
			camera.release();
			camera=null;
			//e.printStackTrace();
		}
		
		camera.setDisplayOrientation(90); 
		
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Camera.Parameters params=camera.getParameters();
		params.setPreviewSize(width, height);
		//camera.setParameters(params);
		camera.startPreview();
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
		camera=null;
	}

	
}



























