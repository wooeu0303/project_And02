package mycom.example.project_and01;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MenuPagerAdapter extends PagerAdapter{
	
	private LayoutInflater mInflater;
	Context context;
    
    public MenuPagerAdapter(Context context) {
        super();
        this.context=context;
        mInflater = LayoutInflater.from(context);
    }
    
    // PagerAdapter���� ���� View ������ ��ȯ�մϴ�.
   	@Override
	public int getCount() {
		return 10;
	}
   	
    // ViewPager���� ����� View�� ���ϰ� ������ݴϴ�.
	@Override
    public Object instantiateItem(View pager, int position){
//        View v = null;
//        
//        switch(position){
//        case 0:
//            v = mInflater.inflate(R.layout.l1, null);
//            break;
//        case 1:
//            v = mInflater.inflate(R.layout.l2, null);
//            break;
//        case 2:
//            v = mInflater.inflate(R.layout.l3, null);
//            break;
//        }
//        
//        ((ViewPager)pager).addView(v, null);
//        
//        return v;
        
        
        ImageView image = new ImageView(context);
		switch(position){
		case 0:	image.setImageResource(R.drawable.kbg1);	break;
		case 1:	image.setImageResource(R.drawable.kbg2);	break;
		case 2:	image.setImageResource(R.drawable.kbg3);	break;
		case 3:	image.setImageResource(R.drawable.kbg4);	break;
		case 4:	image.setImageResource(R.drawable.kbg5);	break;
		case 5:	image.setImageResource(R.drawable.kbg6);	break;
		case 6:	image.setImageResource(R.drawable.kbg7);	break;
		case 7:	image.setImageResource(R.drawable.kbg8);	break;
		case 8:	image.setImageResource(R.drawable.kbg9);	break;
		case 9:	image.setImageResource(R.drawable.kbg10);	break;
		}
        
        ((ViewPager)pager).addView(image, 0);
		return image;
    }
    //view�� �����մϴ�
	@Override
    public void destroyItem(View pager, int position, Object view) {
        ((ViewPager)pager).removeView((View)view);
    }
    
	// instantiateItem���� ���� ��ü�� �̿��� ������ ���θ� ��ȯ�մϴ�.
	@Override
    public boolean isViewFromObject(View v, Object obj) {
        return v == obj;
    }
}
