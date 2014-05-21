package mycom.example.project_and01;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class AroundActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Create a RelativeLayout container that will hold a SurfaceView,
		// and set it as the content of our activity.
		MoveView moveView = new MoveView(this);
		moveView.setImageResource(R.drawable.rpmap);
		setContentView(moveView);
	}



	public class MoveView extends ImageView implements OnTouchListener{
		
		// These matrices will be used to move and zoom image
		private Matrix matrix = new Matrix();
		private Matrix moveMatrix = new Matrix();
		
		// We can be in one of these 3 states
		private static final int NONE = 0;
		private static final int DRAG = 1;
		private static final int ZOOM = 2;
		private int mode = NONE;
		
		// Remember some things for zooming
		private PointF start = new PointF();
		private PointF mid = new PointF();
		private float oldDist = 1f;
		
		private static final int WIDTH = 0;
		private static final int HEIGHT = 1;
		
		//matrix constants
		private float[] value = new float[9];
		
		private Drawable drawable;
		
		//imageView size
		private int width;
		private int height;
		//image size
		private int imageWidth;
		private int imageHeight;
		//zoomIn image max size
		private int scaledImageWidth;
		private int scaledImageHeight;
		
		//Constructor
		public MoveView(Context context, AttributeSet attrs, int defStyle){
			super(context, attrs, defStyle);
		}
		
		public MoveView(Context context, AttributeSet attrs){
			this(context, attrs, 0);
		}
		
		public MoveView(Context context){
			this(context, null);
			setOnTouchListener(this);
			setScaleType(ScaleType.MATRIX);
		}
		
		//image layout ȭ���ġ
		@Override
		protected void onLayout(boolean changed, int left, int top, int right, int bottom){
			super.onLayout(changed, left, top, right, bottom);
			init();
		}
		
		//image setter
		@Override
		public void setImageBitmap(Bitmap bm){
			super.setImageBitmap(bm);
		}
		
		@Override
		public void setImageDrawable(Drawable drawable){
			super.setImageDrawable(drawable);
		}
		
		@Override
		public void setImageResource(int resId){
			super.setImageResource(resId);
		}
		
		//initialize
		protected void init()
		{
		
			this.matrix.getValues(value); // ��Ʈ���� ��
			// ��ũ��
			width = this.getWidth(); 
			height = this.getHeight();
			drawable = this.getDrawable();
			
			if (drawable == null)  return;
			
			imageWidth = drawable.getIntrinsicWidth(); //���� �̹��� �ʺ�
			imageHeight = drawable.getIntrinsicHeight(); //���� �̹��� ����
			
			if (imageWidth > width || imageHeight > height)	{
				setImageFitOnView();
			}
		
			setCenter();
			matrix.setValues(value);
			setImageMatrix(matrix);
		}
		
		public void initImageReal()	{
			value[0] = 1;
			value[4] = 1;
			value[2] = 0;
			value[5] = 0;
			matrix.setValues(value);
			setImageMatrix(matrix);
		}
		
		public void initImageFit()	{
		
			if (imageWidth > width || imageHeight > height)	{
				setImageFitOnView();
			}
			setCenter();
			value[2] = 0;
			value[5] = 0;
			matrix.setValues(value);
			setImageMatrix(matrix);
		
		}
		
		// �信 �°� �̹��� ������ ����
		private void setImageFitOnView()	{// �̹����� ���ο� ������ ���� ���Ͽ� target ����
			int target = WIDTH;
	//	        if (imageWidth < imageHeight)
	//	          target = HEIGHT;
		
	// �ʺ�� ���� �� ū���� �信 �µ��� value �� ����
			if (target == WIDTH)	{
			value[4] = (float)width / imageWidth;
			value[0] = value[4];
		
	//	        else if (target == HEIGHT)
	//	      {
	//	          value[4] = (float)height / imageHeight;
	//	          value[0] = value[4];
	//	      }
		
			scaledImageWidth = (int) (imageWidth * value[0]);
			scaledImageHeight = (int) (imageHeight * value[4]);
		// �ʺ�(����)�� ��� ������ ����(�ʺ�)�� �亸�� Ŭ ��� �̹��� ũ�⸦ ����(�ʺ�)�� �°� ����
	//	        if (scaledImageWidth > width)
	//	      {
	//	          value[4] = (float) width / imageWidth;
	//	          value[0] = value[4];
	//	      }
	//	        if (scaledImageHeight > height)
	//	        {
	//	          value[4] = (float)height / imageHeight;
	//	          value[0] = value[4];
	//	        }
			}
		}
		
		private void setCenter()	{
			scaledImageWidth = (int) (imageWidth * value[0]);
			scaledImageHeight = (int) (imageHeight * value[4]);
			
			if (scaledImageWidth < width)
			{
			value[2] = (float) (width / 2) - (float) (scaledImageWidth / 2);
			}
			if (scaledImageHeight < height)	{
			value[5] = (float) (height / 2) - (float) (scaledImageHeight / 2);
			}
		}
		
		@Override
		public boolean onTouch(View v, MotionEvent event){
			ImageView view = (ImageView) v;
			switch (event.getAction() & MotionEvent.ACTION_MASK){
				case MotionEvent.ACTION_DOWN:	//first finger down only
					moveMatrix.set(matrix);
					start.set(event.getX(), event.getY());
					mode = DRAG;
					break;
				
				case MotionEvent.ACTION_POINTER_DOWN:	//second finger down
					oldDist = spacing(event);
					if (oldDist > 20f){ // 20f�� �����Ͽ� ���� �ν��ϴ� �� �հ����� �Ÿ����� ������
						moveMatrix.set(matrix);
						midPoint(mid, event);
						mode = ZOOM;
					}
					break;
				case MotionEvent.ACTION_UP:			//first finger lifted
				case MotionEvent.ACTION_POINTER_UP:	//second finger lifted
					mode = NONE;
					if (scaledImageWidth < width){
						fixView();
					}
					break;
				 
				case MotionEvent.ACTION_MOVE:
					if (mode == DRAG) {	//movement of first finger
			
					matrix.set(moveMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
					}else if (mode == ZOOM) {	//pinch zooming
						float newDist = spacing(event);
						if (newDist > 5f)	{
							matrix.set(moveMatrix);
							float scale = newDist / oldDist;
							matrix.postScale(scale, scale, mid.x, mid.y);
						}
					}
					break;
				}
			
				changeMatrixValue(matrix, view);
				// Perform the transformation
				//view.setImageMatrix(matrix);
				return true;	// indicate event was handled
			}
		
		// ȭ�麸�� �۰� ��� ���� �ʵ���
		private void fixView() {
			matrix.getValues(value);
			setImageFitOnView();
			setCenter();
			matrix.setValues(value);
			setImageMatrix(matrix);
			
		}
				
		private void changeMatrixValue(Matrix matrix, ImageView view){
			matrix.getValues(value);
			if (drawable == null)  return;
	
		// �̹����� �ٱ����� ������ �ʵ���.
			if (value[2] < width - scaledImageWidth)   value[2] = width - scaledImageWidth;
			if (value[5] < height - scaledImageHeight)   value[5] = height - scaledImageHeight;
			if (value[2] > 0)   value[2] = 0;
			if (value[5] > 0)   value[5] = 0;
		
		// ����ũ�� 2�� �̻� Ȯ�� ���� �ʵ���
			if (value[0] > 2 || value[4] > 2)	{
			value[0] = 2;
			value[4] = 2;
			}
		
			setCenter();
			matrix.setValues(value);
			setImageMatrix(matrix);
		}
		
		//pinch �� �μհ����� �Ÿ�
		private float spacing(MotionEvent event){
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}
		
		//zoomIn midPoint Ȯ�� | ��� �Ǵ� ��
		private void midPoint(PointF point, MotionEvent event){
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}
	}
}
