package mycom.example.project_and01;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MyPreview extends SurfaceView implements SurfaceHolder.Callback{
	Context mc;
	SurfaceHolder mHolder;
	Camera camera;
	
	public MyPreview(Context context) {
		super(context);
		mc=context;
	}
	
	public MyPreview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	//==Ã³¸®±â
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
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
	
	public void setCamera(Camera camera){
		this.camera=camera;
	}
}
