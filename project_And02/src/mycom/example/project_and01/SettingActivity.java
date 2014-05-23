package mycom.example.project_and01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class SettingActivity extends Activity {
	
	ListView list;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.setting);
			overridePendingTransition(R.anim.leftin, R.anim.leftout);
			
		   //����_�ڷ�
	       Button btn_back = (Button) findViewById(R.id.btn_back);
	       btn_back.setOnClickListener(new OnClickListener(){

	          @Override
	          public void onClick(View v) {
	        	  Intent intent = new Intent(SettingActivity.this, MenuActivity.class);
	              startActivity(intent);
	          }
	       });
	
			list = (ListView)findViewById(R.id.listView);	
			//�迭�� ���� ���ؼ��� adapter�� �ʿ��ϴ�.				
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
			//�̰��� �����, R������ array.settiing, �̸� ���ǵǾ� �ִ°� = adapter 	
	
			this, R.array.setting, android.R.layout.simple_list_item_1);	
			list.setAdapter(adapter);
	
			list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				
			}

				
		});					
	}
}  
	 

