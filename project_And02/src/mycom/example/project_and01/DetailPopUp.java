package mycom.example.project_and01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class DetailPopUp extends Activity implements OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        
        setContentView(R.layout.detail);
        
        TextView textView = (TextView)findViewById(R.id.Popup);
        Intent intent = getIntent();
        textView.setText(intent.getExtras().get("detail").toString());

 
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
