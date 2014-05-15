package mycom.example.project_and01;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class MainActivity extends Activity {

	@SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        
        Handler handler = new Handler() {
            public void handleMessage(Message msg) {
             super.handleMessage(msg);
             startActivity(new Intent(MainActivity.this, MenuActivity.class));
             finish();
            }
         };
         handler.sendEmptyMessageDelayed(0, 2000);
         
    }

}
