package test.qrcodegen;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;


public class QrcodeData extends Activity {
	
	final private int FONTSIZE = 10;
	final private int START_X = 30;
	final private int START_Y = 30;
	final private int VER2 = 29;
//	final private int VER2 = 25;
//	final private int VER1 = 21;
	
	private byte qrcodeData[][];
	//private String data[] = new String[VER2];
	private int x = VER2 - 1;
	private int y = VER2 - 1;
	
	final static int UP = 1;
	final static int DOWN = 2;
	static int MODE = 0;
	static boolean alreadyLeft = false;
	
	static int number = 0;
	//TextView edit;
	DrawView drawView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.data);
        
        //edit = (TextView)findViewById(R.id.edit);
        
        MODE = UP;
        number = 0;
        x = VER2 - 1;
        y = VER2 - 1;
        alreadyLeft = false;
        initBaseData();
       // printData();
        makingData();
        putErrorCode(QrcodeGeneratorActivity.levelMode);
        printData();
        
        drawView = new DrawView(this);
        drawView.setBackgroundColor(Color.WHITE);
        setContentView(drawView);
      
    }
	
	
	private void makingData() {
		
		
	
		// start with [20][20]
		
		for ( int i = 0 ; i < QrData.QrStringdata.length(); i++)
		{		
			number = i;

			switch (QrData.QrStringdata.charAt(i))
			{
			case '0':
				qrcodeData[x][y] = 0;
				break;
			case '1':
				qrcodeData[x][y] = 1;
				break;
			}	
			
			moveCursor();
			//printData();
		}
		
		reverseData();
	}


	private void moveCursor() {
		
		// if alreadyLeft is false, then cursor will move to left 
		if ( alreadyLeft == false ) 
		{
			y = y - 1;
			alreadyLeft = true;
		}
		else 
		{
			int tempremainer = y % 4;
			int tempnum = y / 4;
			int num = ( VER2 - 8 ) / 4;

			if ( MODE == UP )
			{
				
				if (( x - 1  < 0 ) || ( qrcodeData[x-1][y+1] != 0 ))
				{					
					if ( x == 7 && tempremainer == 3)
					{
						if ( tempnum > 1 && tempnum < num )
						{
							x = 5; y = y + 1;
						}		
					}
					else if ( x == (VER2-4) && y == (VER2-6) ) 
					{
						x =  VER2 - 10; 
						y = VER2 - 5;
					}
					else if ( x == (VER2-9) && y == (VER2-10) ) 
					{
						x = VER2-10; 
						y = VER2-9;
					}
					else if ( y == (VER2-10) && ( x < (VER2-3) && x > (VER2-9)))
					{
						x = x - 1;
						y = VER2-10;
						alreadyLeft = true;
						return;
					}
					else
					{
						
						if ( x == 9 && y == 7 )
						{
							x = 9 ; y = 5;
						}
						else
						{
							y = y - 1;	
						}
						
						MODE = DOWN;
					}
					alreadyLeft = false;
					return;
				}
				else // that spot is empty 
				{
					x = x - 1;
					y = y + 1;
					alreadyLeft = false;
				}
			}
			else if ( MODE == DOWN )
			{
				if (( x + 1  > VER2 -1 ) || ( qrcodeData[x+1][y+1] != 0 ))
				{
					if ( x == 5 && tempremainer == 1)
					{
						if ( tempnum > 1 && tempnum < num )
						{
							x = 7; y = y + 1;
						}
					}
					else if ( x == ( VER2 - 10 ) && y == (VER2-8) ) 
					{
						x = VER2 - 4; 
						y = VER2 - 7;
					}
					else
					{
						if ( x == (VER2 -1) && y == 9 )
						{
							x = VER2 - 9; y = 8;
							MODE = UP;
						}
						else
						{
							MODE = UP;
							y = y - 1;	
						}
					}
					alreadyLeft = false;
					
					return;
				}
				else // that spot is empty 
				{
					x = x + 1;
					y = y + 1;
					alreadyLeft = false;
				}		
			}
		}

	}


	private void reverseData() {
	
		for ( int i = 0 ; i < VER2; i++)
		{
			for ( int j = 0 ; j < VER2 ; j++)
			{
				if ( i < 9 && (j < 9 || j > (VER2-9) ))
					continue;
				else if ( i == 6 )
					continue;
				else if ( j == 6 )
					continue;
				else if ( i > ( VER2-9) && j < 9)
					continue;
				else if ( i > (VER2-10) && i < (VER2-4))
				{
					if ( j < (VER2-4) && j > (VER2-10))
						continue;
				}
				
				
				int num = i + j;
				num = num % 3;
				
				if ( num == 0) // reverse
				{
					if ( qrcodeData[i][j] == 1)
						qrcodeData[i][j] = 0;
					else if ( qrcodeData[i][j] == 0 )
						qrcodeData[i][j] = 1;
				}
			}
			
		}
	}


	public byte[][] getData()
	{
		return qrcodeData;
	}
	
	public void initBaseData()
	{
		
		qrcodeData = new byte[VER2][VER2];
		// initialize qrcodeData with 0
		for ( int i = 0 ; i < VER2; i++)
		{	
			for ( int j = 0 ; j < VER2 ; j++)
				qrcodeData[i][j] = 0;
		}
		
		for ( int i = 0; i < 7; i++)
		{
			qrcodeData[0][i] = 2;
			qrcodeData[6][i] = 2;
			qrcodeData[VER2 - 7][i] = 2;
			qrcodeData[VER2 - 1][i] = 2;
		}
		for ( int i = ( VER2 - 7 ); i < VER2; i++)
		{
			qrcodeData[0][i] = 2;
			qrcodeData[6][i] = 2;
		}	
		
		
		qrcodeData[1][0] = 2; qrcodeData[1][6] = 2; qrcodeData[1][VER2 -7] = 2; qrcodeData[1][VER2 -1] = 2; 
		
		qrcodeData[2][0] = 2; qrcodeData[2][2] = 2; qrcodeData[2][3] = 2; qrcodeData[2][4] = 2; qrcodeData[2][6] = 2; 
		qrcodeData[2][VER2 -7] = 2; qrcodeData[2][VER2 -5] = 2; qrcodeData[2][VER2 -4] = 2; qrcodeData[2][VER2 -3] = 2; qrcodeData[2][VER2 -1] = 2; 
		
		qrcodeData[3][0] = 2; qrcodeData[3][2] = 2; qrcodeData[3][3] = 2; qrcodeData[3][4] = 2; qrcodeData[3][6] = 2; 
		qrcodeData[3][VER2 -7] = 2; qrcodeData[3][VER2 -5] = 2; qrcodeData[3][VER2 -4] = 2; qrcodeData[3][VER2 -3] = 2; qrcodeData[3][VER2 -1] = 2; 
		
		qrcodeData[4][0] = 2; qrcodeData[4][2] = 2; qrcodeData[4][3] = 2; qrcodeData[4][4] = 2; qrcodeData[4][6] = 2; 
		qrcodeData[4][VER2 -7] = 2; qrcodeData[4][VER2 -5] = 2; qrcodeData[4][VER2 -4] = 2; qrcodeData[4][VER2 -3] = 2; qrcodeData[4][VER2 -1] = 2; 
		
		qrcodeData[5][0] = 2; qrcodeData[5][6] = 2; qrcodeData[5][VER2 -7] = 2; qrcodeData[5][VER2 -1] = 2; 
		
		//
		qrcodeData[6][8] = 2; qrcodeData[6][10] = 2; qrcodeData[6][12] = 2; qrcodeData[6][14] = 2; qrcodeData[6][16] = 2; qrcodeData[6][18] = 2; qrcodeData[6][20] = 2;
		
		qrcodeData[8][6] = 2;
		
		qrcodeData[10][6] = 2;
		
		qrcodeData[12][6] = 2;
		
		qrcodeData[14][6] = 2;
		
		qrcodeData[16][6] = 2;		
		
		qrcodeData[18][6] = 2;
		
		qrcodeData[20][6] = 2;					
	
		
		///
		qrcodeData[VER2 -6][0] = 2; qrcodeData[VER2 -6][6] = 2;
		
		qrcodeData[VER2 -5][0] = 2; qrcodeData[VER2 -5][2] = 2; qrcodeData[VER2 -5][3] = 2; qrcodeData[VER2 -5][4] = 2; qrcodeData[VER2 -5][6] = 2; 
		
		qrcodeData[VER2 -4][0] = 2; qrcodeData[VER2 -4][2] = 2; qrcodeData[VER2 -4][3] = 2; qrcodeData[VER2 -4][4] = 2; qrcodeData[VER2 -4][6] = 2; 
		
		qrcodeData[VER2 -3][0] = 2; qrcodeData[VER2 -3][2] = 2; qrcodeData[VER2 -3][3] = 2; qrcodeData[VER2 -3][4] = 2; qrcodeData[VER2 -3][6] = 2; 
		
		qrcodeData[VER2 -2][0] = 2; qrcodeData[VER2 -2][6] = 2;
		
	
		////////////////////////////////
		
		for ( int i = 0 ; i < 6; i++)
		{
			qrcodeData[i][7] = 3; qrcodeData[i][8] = 4; qrcodeData[i][17] = 3; 
		}
		qrcodeData[6][7] = 3; qrcodeData[6][9] = 3; qrcodeData[6][11] = 3; qrcodeData[6][13] = 3; qrcodeData[6][15] = 3; qrcodeData[6][17] = 3; qrcodeData[6][19] = 3;
	
		qrcodeData[7][0] = 3; qrcodeData[7][1] = 3; qrcodeData[7][2] = 3; qrcodeData[7][3] = 3; qrcodeData[7][4] = 3; qrcodeData[7][5] = 3; qrcodeData[7][6] = 3; qrcodeData[7][7] = 3;
		qrcodeData[7][8] = 4;
		
		qrcodeData[7][VER2 -8] = 3; qrcodeData[7][VER2 -7] = 3; qrcodeData[7][VER2 -6] = 3; qrcodeData[7][VER2 -5] = 3; qrcodeData[7][VER2 -4] = 3; qrcodeData[7][VER2 -3] = 3; qrcodeData[7][VER2 -2] = 3; qrcodeData[7][VER2 -1] = 3;
		
		qrcodeData[8][0] = 4; qrcodeData[8][1] = 4; qrcodeData[8][2] = 4; qrcodeData[8][3] = 4; qrcodeData[8][4] = 4; qrcodeData[8][5] = 4; qrcodeData[8][7] = 4; qrcodeData[8][8] = 4;
		qrcodeData[8][VER2 -8] = 4; qrcodeData[8][VER2 -7] = 4; qrcodeData[8][VER2 -6] = 4; qrcodeData[8][VER2 -5] = 4; qrcodeData[8][VER2 -4] = 4; qrcodeData[8][VER2 -3] = 4; qrcodeData[8][VER2 -2] = 4; qrcodeData[8][VER2 -1] = 4;
		
		qrcodeData[9][6] = 3;
		
		qrcodeData[11][6] = 3;
		
		qrcodeData[13][6] = 3;
		
		qrcodeData[15][6] = 3;
		
		qrcodeData[17][6] = 3;
		
		qrcodeData[19][6] = 3;
		
		qrcodeData[VER2 -8][0] = 3; qrcodeData[VER2 -8][1] = 3; qrcodeData[VER2 -8][2] = 3; qrcodeData[VER2 -8][3] = 3; qrcodeData[VER2 -8][4] = 3; qrcodeData[VER2 -8][5] = 3; qrcodeData[VER2 -8][6] = 3; qrcodeData[VER2 -8][7] = 3;
		
		for ( int i = ( VER2 - 7 ); i < VER2; i++)
		{
			qrcodeData[i][7] = 3; 
			qrcodeData[i][8] = 4;
		}	
		
		qrcodeData[VER2 -9][VER2 -9] = 2; qrcodeData[VER2 -9][VER2 -8] = 2; qrcodeData[VER2 -9][VER2 -7] = 2; qrcodeData[VER2 -9][VER2 -6] = 2; qrcodeData[VER2 -9][VER2 -5] = 2;
		
		qrcodeData[VER2 -8][VER2 -9] = 2; qrcodeData[VER2 -8][VER2 -5] = 2;
		
		qrcodeData[VER2 -7][VER2 -9] = 2; qrcodeData[VER2 -7][VER2 -7] = 2; qrcodeData[VER2 -7][VER2 -5] = 2;
		
		qrcodeData[VER2 -6][VER2 -9] = 2; qrcodeData[VER2 -6][VER2 -5] = 2;
		
		qrcodeData[VER2 -5][VER2 -9] = 2; qrcodeData[VER2 -5][VER2 -8] = 2; qrcodeData[VER2 -5][VER2 -7] = 2; qrcodeData[VER2 -5][VER2 -6] = 2; qrcodeData[VER2 -5][VER2 -5] = 2;
		
	}
	
	@Override
	protected void onPause() {
		
		super.onPause();
	}


	private void putErrorCode(byte level) {
		
    	switch(level)
    	{
    	case QrcodeGeneratorActivity.ERROR_CORRECTING_LEVEL_L :
    		// ERROR_CORRECTING_LEVEL_M and Use mask pattern 011
    		qrcodeData[8][VER2-1] = 1; qrcodeData[8][VER2-2] = 0; qrcodeData[8][VER2-3] = 1; qrcodeData[8][VER2-4] = 1; qrcodeData[8][VER2-5] = 1; qrcodeData[8][VER2-6] = 0; qrcodeData[8][VER2-7] = 0; qrcodeData[8][VER2-8] = 1;
    		qrcodeData[VER2-7][8] = 0; qrcodeData[VER2-6][8] = 0; qrcodeData[VER2-5][8] = 0; qrcodeData[VER2-4][8] = 1; qrcodeData[VER2-3][8] = 1; qrcodeData[VER2-2][8] = 1; qrcodeData[VER2-1][8] = 1;
    		
    		qrcodeData[0][8] = 1; qrcodeData[1][8] = 0; qrcodeData[2][8] = 1; qrcodeData[3][8] = 1; qrcodeData[4][8] = 1; qrcodeData[5][8] = 0; qrcodeData[7][8] = 0; qrcodeData[8][8] = 1;
    		qrcodeData[8][7] = 0; qrcodeData[8][5] = 0; qrcodeData[8][4] = 0; qrcodeData[8][3] = 1; qrcodeData[8][2] = 1; qrcodeData[8][1] = 1; qrcodeData[8][0] = 1;

    		break;
    	case QrcodeGeneratorActivity.ERROR_CORRECTING_LEVEL_M :
    		// ERROR_CORRECTING_LEVEL_M and Use mask pattern 011
    		qrcodeData[8][VER2-1] = 1; qrcodeData[8][VER2-2] = 1; qrcodeData[8][VER2-3] = 0; qrcodeData[8][VER2-4] = 1; qrcodeData[8][VER2-5] = 0; qrcodeData[8][VER2-6] = 0; qrcodeData[8][VER2-7] = 1; qrcodeData[8][VER2-8] = 0;
    		qrcodeData[VER2-7][8] = 1; qrcodeData[VER2-6][8] = 1; qrcodeData[VER2-5][8] = 1; qrcodeData[VER2-4][8] = 1; qrcodeData[VER2-3][8] = 1; qrcodeData[VER2-2][8] = 0; qrcodeData[VER2-1][8] = 1;
    		
    		qrcodeData[0][8] = 1; qrcodeData[1][8] = 1; qrcodeData[2][8] = 0; qrcodeData[3][8] = 1; qrcodeData[4][8] = 0; qrcodeData[5][8] = 0; qrcodeData[7][8] = 1; qrcodeData[8][8] = 0;
    		qrcodeData[8][7] = 1; qrcodeData[8][5] = 1; qrcodeData[8][4] = 1; qrcodeData[8][3] = 1; qrcodeData[8][2] = 1; qrcodeData[8][1] = 0; qrcodeData[8][0] = 1;

    		break;
    	case QrcodeGeneratorActivity.ERROR_CORRECTING_LEVEL_Q :
    		// ERROR_CORRECTING_LEVEL_Q and Use mask pattern 011
    		qrcodeData[8][VER2-1] = 0; qrcodeData[8][VER2-2] = 1; qrcodeData[8][VER2-3] = 1; qrcodeData[8][VER2-4] = 0; qrcodeData[8][VER2-5] = 0; qrcodeData[8][VER2-6] = 0; qrcodeData[8][VER2-7] = 0; qrcodeData[8][VER2-8] = 0;
    		qrcodeData[VER2-7][8] = 0; qrcodeData[VER2-6][8] = 1; qrcodeData[VER2-5][8] = 0; qrcodeData[VER2-4][8] = 1; qrcodeData[VER2-3][8] = 1; qrcodeData[VER2-2][8] = 1; qrcodeData[VER2-1][8] = 0;
    		
    		qrcodeData[0][8] = 0; qrcodeData[1][8] = 1; qrcodeData[2][8] = 1; qrcodeData[3][8] = 0; qrcodeData[4][8] = 0; qrcodeData[5][8] = 0; qrcodeData[7][8] = 0; qrcodeData[8][8] = 0;
    		qrcodeData[8][7] = 0; qrcodeData[8][5] = 1; qrcodeData[8][4] = 0; qrcodeData[8][3] = 1; qrcodeData[8][2] = 1; qrcodeData[8][1] = 1; qrcodeData[8][0] = 0;

    		break;
    	case QrcodeGeneratorActivity.ERROR_CORRECTING_LEVEL_H :
    		// ERROR_CORRECTING_LEVEL_H and Use mask pattern 011
    		qrcodeData[8][VER2-1] = 0; qrcodeData[8][VER2-2] = 0; qrcodeData[8][VER2-3] = 0; qrcodeData[8][VER2-4] = 0; qrcodeData[8][VER2-5] = 1; qrcodeData[8][VER2-6] = 0; qrcodeData[8][VER2-7] = 1; qrcodeData[8][VER2-8] = 1;
    		qrcodeData[VER2-7][8] = 1; qrcodeData[VER2-6][8] = 0; qrcodeData[VER2-5][8] = 0; qrcodeData[VER2-4][8] = 1; qrcodeData[VER2-3][8] = 1; qrcodeData[VER2-2][8] = 0; qrcodeData[VER2-1][8] = 0;
    		
    		qrcodeData[0][8] = 0; qrcodeData[1][8] = 0; qrcodeData[2][8] = 0; qrcodeData[3][8] = 0; qrcodeData[4][8] = 1; qrcodeData[5][8] = 0; qrcodeData[7][8] = 1; qrcodeData[8][8] = 1;
    		qrcodeData[8][7] = 1; qrcodeData[8][5] = 0; qrcodeData[8][4] = 0; qrcodeData[8][3] = 1; qrcodeData[8][2] = 1; qrcodeData[8][1] = 0; qrcodeData[8][0] = 0;
    	
    		break;
    	default:
    		break;
    		
    	}
		
	}

		

	public void printData()
	{
		
		for ( int i = 0 ; i < VER2; i++)
		{
			
			for ( int j = 0 ; j < VER2 ; j++)
			{
				if ( qrcodeData[i][j] == 0)
				{
					qrcodeData[i][j] = 0;			
				}
				else if ( qrcodeData[i][j] == 1)
				{
					qrcodeData[i][j] = 1;
		
				}
				else if (qrcodeData[i][j] == 2)
				{
					qrcodeData[i][j] = 1;
	
				}
				else if (qrcodeData[i][j] == 3)
				{
					qrcodeData[i][j] = 0;
	
				}
				else if (qrcodeData[i][j] == 4)
				{
					qrcodeData[i][j] = 0;			
					
				}
			}	
		}	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ( keyCode == KeyEvent.KEYCODE_BACK )
		{
		
			Intent i = new Intent().setClass(QrcodeData.this, QrcodeGeneratorActivity.class);
			startActivity(i);
            //overridePendingTransition(0,0);
			QrcodeData.this.finish();	 
		}
		return super.onKeyDown(keyCode, event);
	}

	class DrawView extends View {
		Paint paintBlack = new Paint();

		public DrawView(Context context) {
			super(context);
			setDrawingCacheEnabled(true);
		}

		@Override
		public void onDraw(Canvas canvas) {

			paintBlack.setStrokeWidth(1);

			for (int i = 0; i < VER2; i++) {
				for (int j = 0; j < VER2; j++) {
					if (qrcodeData[i][j] == 0) {
						paintBlack.setColor(Color.WHITE);
					} else if (qrcodeData[i][j] == 1) {
						paintBlack.setColor(Color.BLACK);
					}
					canvas.drawRect((j * FONTSIZE) + START_X, (i * FONTSIZE)
							+ START_Y, START_X + FONTSIZE + (j * FONTSIZE),
							START_Y + FONTSIZE + (i * FONTSIZE), paintBlack);
				}
			}
			try {
				File root = Environment.getExternalStorageDirectory();
				getDrawingCache().compress(Bitmap.CompressFormat.JPEG, 100,
						new FileOutputStream(new File(root, "sample.jpg")));
			} catch (Exception e) {
				Log.e("Error--------->", e.toString());
			}

		}

	}
}
