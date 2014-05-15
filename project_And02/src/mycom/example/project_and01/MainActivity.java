package mycom.example.project_and01;

import android.support.v7.app.ActionBarActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.Window;

public class MainActivity extends ActionBarActivity {

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
         handler.sendEmptyMessageDelayed(0, 1000);
         System.out.println("123");
    }

}
