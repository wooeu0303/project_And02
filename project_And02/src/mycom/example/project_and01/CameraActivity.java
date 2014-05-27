
package mycom.example.project_and01;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;




public class CameraActivity extends Activity implements LocationListener,SensorEventListener{
    //카메라 프리뷰
	private MyPreview mPreview;
    Camera mCamera;
    int numberOfCameras;
    int cameraCurrentlyLocked;

    // The first rear facing camera
    int defaultCameraId;
    
    //서버 통신
	Map<String, Map<String, String>> locationMap = new HashMap<String, Map<String, String>>();
    
    //위치정보
	private Location location;
	private LocationManager locMgr;
	private String provider;
	private Location targetLoc;
	AugmentedView mAView;
	
	//센서 정보
	private float[] mGravity = new float[3];
	private float[] mGeomagnetic = new float[3];
	private SensorManager mSensorMgr;
	float azimut;
	float azimut2;
	float azimut3;
    float avg;
    float a;
	private float x1;
	private float y1;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// 기본
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        //서버
        new getBeaconListTask().execute("http://192.168.200.13:8080/app/json/gangnam");

        
        setContentView(R.layout.camera);
        
        mPreview = (MyPreview)findViewById(R.id.surface1);
        mAView=new AugmentedView(this);
        addContentView(mAView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        SurfaceHolder mpHolder = mPreview.getHolder();
        mpHolder.addCallback(mPreview);
        mpHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);        
        
        
        // Find the total number of cameras available
        numberOfCameras = Camera.getNumberOfCameras();

        // Find the ID of the default camera
        CameraInfo cameraInfo = new CameraInfo();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                defaultCameraId = i;
            }
        }
        
        
        
        //위치정보
        locMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		provider = locMgr.getBestProvider(criteria, true);
        location=locMgr.getLastKnownLocation(provider);
        locMgr.requestLocationUpdates(provider, 2, 10, this);
        
        targetLoc=new Location("target");
        targetLoc.setLatitude(37.497942);
        targetLoc.setLongitude(127.027621);
        
        
        //센서정보
         mSensorMgr=(SensorManager)getSystemService(this.SENSOR_SERVICE);
    }

	@Override
	protected void onStop() {
		super.onStop();
		mSensorMgr.unregisterListener(this);
	}


    @Override
    protected void onResume() {
        super.onResume();

        // Open the default i.e. the first rear facing camera.
        if(mCamera==null){
        	mCamera = Camera.open();
        }
        mPreview.setCamera(mCamera);
        cameraCurrentlyLocked = defaultCameraId;
        
        
        
        //센서
        mSensorMgr.registerListener(this,
        		mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorMgr.registerListener(this,
        		mSensorMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);
                
        
        
    }
    

    public Map<String, Map<String, String>> getJsonData(String servUrl){
		
		Map<String, Map<String, String>> locationMap = new HashMap<String, Map<String, String>>();
		
		Map<String, String> placeMap = new HashMap<String, String>();
		
		InputStream is = null;
		String result = "";
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(servUrl);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			
			is = httpEntity.getContent();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			
			String line = null;
			while((line = br.readLine()) != null){
				sb.append(line+"\n");
			}
			
			is.close();
			result = sb.toString();	
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			placeMap.put("lat", new JSONObject(result).getJSONObject("gangnam").getString("lat"));
			placeMap.put("lng", new JSONObject(result).getJSONObject("gangnam").getString("lng"));
			placeMap.put("locationName", new JSONObject(result).getJSONObject("gangnam").getString("locationName"));
			placeMap.put("locationDetail", new JSONObject(result).getJSONObject("gangnam").getString("locationDetail"));
			locationMap.put("gangnam", placeMap);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return locationMap;
		
	}
	
	private class getBeaconListTask extends AsyncTask<String, Void, Void>{		
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onPostExecute(Void result) {

		}

		@Override
		protected Void doInBackground(String... urls) {
			
			Map<String, Map<String, String>> downloadLocationMap = getJsonData(urls[0]);
			
			for(int i=0; i<downloadLocationMap.size(); i++){
				locationMap.putAll(downloadLocationMap);
			}
			return null;
		}
	}
	
	private class MyAdapter extends BaseAdapter{
		
		Context mc;
		
		public MyAdapter(Context context) {
			this.mc = context;
		}

		@Override
		public int getCount() {
			return locationMap.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View root, ViewGroup parent) {		
			return root;
		}
	}
	
	public class AugmentedView extends ImageView {
		
		private int x;
		private int y;
		private int size=40;
		
		public AugmentedView(Context context) {
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			Paint paint=new Paint();
			paint.setColor(Color.WHITE);
            paint.setStyle(Paint.Style.FILL);
			paint.setTextSize(size);
			paint.setStrokeWidth(10);
   
            float angle=40;
            
            if(azimut2-angle/2<0){
            	azimut3=azimut2-angle/2+360;
            }else if(azimut2+angle/2>=360){
            	azimut3=azimut2+angle/2-360;
            }else{
            	azimut3=azimut2;
            }

			if(azimut3>azimut){
				if(azimut3-azimut<angle/2){
					x1=canvas.getWidth()/2+(azimut3-azimut)*canvas.getWidth()/angle;
					y1=150;
					canvas.drawText(locationMap.get("gangnam").get("locationName"), x1, y1, paint);
				}
			}else{
				if(azimut-azimut3<angle/2){
					x1=canvas.getWidth()/2-(azimut-azimut3)*canvas.getWidth()/angle;
					y1=150;
					canvas.drawText(locationMap.get("gangnam").get("locationName"), x1, y1, paint);
				}
			}
			super.onDraw(canvas);
		}
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			x=(int) event.getX();
			y=(int) event.getY();
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				if(x<x1+200 && x>x1 && y<y1 && y>y1-100){
					Intent intent = new Intent(CameraActivity.this, DetailPopUp.class);
					intent.putExtra("detail", locationMap.get("gangnam").get("locationDetail"));
					startActivity(intent);
					return true;
				}
			}
			invalidate();	
			return false;
		}		
	}
	
	//location
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	
	//sensor
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
             mGravity = event.values.clone();
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
             mGeomagnetic = event.values.clone();
		if (mGravity != null && mGeomagnetic != null) {
             float R[] = new float[9];
             float I[] = new float[9];
             boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                             mGeomagnetic);
             if (success) {
                     float orientation[] = new float[3];
                     SensorManager.getOrientation(R, orientation);
                     
                     
                     //avg=avg*(1.0f-0.1f)+orientation[0]*0.1f;
                     azimut = (float)Math.toDegrees(orientation[0]);
                    
                     if(azimut<0)
                             azimut = azimut+360;
             
                     Matrix matrix=new Matrix();
                     mAView.setScaleType(ScaleType.MATRIX);   //required
                     matrix.postRotate(360-azimut, mAView.getWidth()/2, mAView.getHeight()/2);
                     mAView.setImageMatrix(matrix);        
                     azimut2=location.bearingTo(targetLoc);
                     if(azimut2<0){
                    	 azimut2+=360;
                    	 }
             }
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}
}
