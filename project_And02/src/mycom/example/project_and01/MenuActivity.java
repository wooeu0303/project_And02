package mycom.example.project_and01;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MenuActivity extends Activity{
	
	//Field
	//메인화면 사진 저장할 List
	List<Integer> galleryIda=new ArrayList<Integer>();
	//사진 넘겨주는 Flipper 객체
	//private AdapterViewFlipper avf;
	private int mX=0;

    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.menu);

         
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window win=getWindow();
        win.setContentView(R.layout.background);
       
        
        LayoutInflater inflater = (LayoutInflater)getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        		RelativeLayout relative = (RelativeLayout)inflater.inflate(R.layout.menu, null);
 
        		RelativeLayout.LayoutParams paramRelative = new RelativeLayout.LayoutParams(
        		RelativeLayout.LayoutParams.MATCH_PARENT,
        		RelativeLayout.LayoutParams.MATCH_PARENT);
        	win.addContentView(relative, paramRelative);
        */
        
        /*	
        //drawable에 있는 사진 List저장
		for(int i=1;i<=10;i++){
			galleryIda.add(getResources().getIdentifier("kbg"+i, "drawable", this.getPackageName()));
		}
		
		avf=(AdapterViewFlipper)findViewById(R.id.adapterViewFlipper1);
		avf.setOnTouchListener(this);
		avf.setAdapter(new GalleryAdapter(this));
		avf.startFlipping();
    */
		//카메라start
		Button btn_start = (Button) findViewById(R.id.btn_start);
		btn_start.setOnClickListener(new OnClickListener(){
		
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MenuActivity.this, CameraActivity.class);
			    startActivity(intent); 
			}  
		}); 
		
 
		//주변지도
       Button btn_AroundView = (Button) findViewById(R.id.btn_AroundView);
       btn_AroundView.setOnClickListener(new OnClickListener(){

          @Override
          public void onClick(View v) {
        	  Intent intent = new Intent(MenuActivity.this, AroundActivity.class);
              startActivity(intent);
          }
       });  
       
        
	   //홈페이지 연결
	   Button btn_homepage = (Button) findViewById(R.id.btn_homepage);
	   btn_homepage.setOnClickListener(new OnClickListener(){
	
	   @Override
	       public void onClick(View v) {
	          Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/"));
	          startActivity(intent);
	       }     
	   });
	   
	   //설정
       Button setting = (Button) findViewById(R.id.ibtn_setting);
       setting.setOnClickListener(new OnClickListener(){

          @Override
          public void onClick(View v) {
        	  Intent intent = new Intent(MenuActivity.this, SettingActivity.class);
              startActivity(intent);
          }
       });
       
     
   
	   //페이스북 연결
	   Button ibtn_fb = (Button) findViewById(R.id.ibtn_fb);
	   ibtn_fb.setOnClickListener(new OnClickListener(){
	
	     @Override
	     public void onClick(View v) {
	        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/"));
	        startActivity(intent);  
	     } 
	   });
	   
	   //트위터 연결
	   Button ibtn_tw = (Button) findViewById(R.id.ibtn_tw);
	   ibtn_tw.setOnClickListener(new OnClickListener(){
	
	     @Override
	     public void onClick(View v) {
	        Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.twitter.com/"));
	        startActivity(intent);  
	     } 
	   });
    }
}
    
    /*
    //adapter inner class
	public class GalleryAdapter extends BaseAdapter {
		private Context mContext;
		LayoutInflater inflater;
		
		public GalleryAdapter(Context c){
			mContext=c;
			inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return galleryIda.size();
		}
		@Override
		public Object getItem(int position) {
			return position;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView=inflater.inflate(R.layout.imageview,parent,false);
			}
			ImageView imageView=(ImageView)convertView.findViewById(R.id.imageView1);
			imageView.setImageResource(galleryIda.get(position));
			return convertView;
		}
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mX=(int)event.getX();
        } 
		if (event.getAction() == MotionEvent.ACTION_UP) {
            int nTouchPosx=(int)event.getX();
            if(nTouchPosx<mX){
            	avf.showNext();
            }else if(nTouchPosx>mX){
            	avf.showPrevious();
            }
            mX=nTouchPosx;
		}
		return true;
	}
};
*/






























