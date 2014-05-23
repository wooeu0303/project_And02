
package mycom.example.project_and01;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class CameraActivity extends Activity {
    private MyPreview mPreview;
    Camera mCamera;
    int numberOfCameras;
    int cameraCurrentlyLocked;

    // The first rear facing camera
    int defaultCameraId;
    
    private ListView listv;
	Map<String, Map<String, String>> locationMap = new HashMap<String, Map<String, String>>();
    
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Hide the window title.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        new getBeaconListTask().execute("http://192.168.200.51:8080/app/json/gangnam");
        
        // Create a RelativeLayout container that will hold a SurfaceView,
        // and set it as the content of our activity.
        
//        mPreview = new MyPreview(this);
//        setContentView(mPreview);
        
        setContentView(R.layout.camera);
        
        mPreview = (MyPreview)findViewById(R.id.surface1);
        AugmentedView mAView=new AugmentedView(this);
        addContentView(mAView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        SurfaceHolder mpHolder = mPreview.getHolder();
        mpHolder.addCallback(mPreview);
        mpHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //mPreview = new MyPreview(this);
        
        
        
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
        
        
        
        
       
		
		//listv = (ListView) findViewById(R.id.list);
        
        
        
        
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
			
//			if(root==null){
//				LayoutInflater inflater
//				= (LayoutInflater) mc.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				root = inflater.inflate(R.layout.list_location, null);
//			}
//			
//			TextView txt_lat = (TextView) root.findViewById(R.id.list_txt_lat);
//			txt_lat.setText(locationMap.get("gangnam").get("lat"));
//			
//			TextView txt_lng = (TextView) root.findViewById(R.id.list_txt_lng);
//			txt_lng.setText(locationMap.get("gangnam").get("lng"));
//			
//			TextView txt_locationName = (TextView) root.findViewById(R.id.list_txt_locationName);
//			txt_locationName.setText(locationMap.get("gangnam").get("locationName"));
//			
//			TextView txt_locationDetail = (TextView) root.findViewById(R.id.list_txt_locationDetail);
//			txt_locationDetail.setText(locationMap.get("gangnam").get("locationDetail"));
			
			return root;
		}
		
	}
	
	private class getBeaconListTask extends AsyncTask<String, Void, Void>{
		
		ProgressDialog dialog = new ProgressDialog(CameraActivity.this);
		
		@Override
		protected void onPreExecute() {
			dialog.setMessage("·ÎµùÁß...");
			dialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss();
			
			//MyAdapter adapter = new MyAdapter(CameraActivity.this);
			//listv.setAdapter(adapter);
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
	
	public class AugmentedView extends View {
		
		private int x;
		private int y;
		private int size=100;
		
		public AugmentedView(Context context) {
			super(context);
		}
		
		@Override
		protected void onDraw(Canvas canvas) {
			Paint paint=new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setTextSize(30);
			canvas.drawText("x : "+x+" Y : "+y,10, 30, paint);
			paint.setColor(Color.RED);
			paint.setTextSize(size);
			paint.setStrokeWidth(10);
			canvas.drawText(locationMap.get("gangnam").get("locationName"), 150, 200, paint);
		//	paint.setColor(Color.BLUE);
		//	paint.setTextSize(50);
		//	canvas.drawText(locationMap.get("gangnam").get("locationDetail"), 50, 500, paint);
			
			super.onDraw(canvas);
		}
		
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			x=(int) event.getX();
			y=(int) event.getY();
			if(event.getAction()==MotionEvent.ACTION_DOWN){
				if(x<350 && x>150 && y<200 && y>100){
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
	
}
