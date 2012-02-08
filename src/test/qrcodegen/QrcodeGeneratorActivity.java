package test.qrcodegen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class QrcodeGeneratorActivity extends Activity implements OnClickListener {
    Button create;
    EditText text1;

    
    // right now we are making Version 3
    final static int VERSION = 3;
   // final static int VERSION_BYTE = 26;
    //final static int VERSION_BYTE = 44;
    final static int VERSION_BYTE = 70;
    
    final static String MODE_NUMERIC = "0001"; // "0001"
    final static String MODE_ALPHA_NUMERIC = "0010"; //"0010";
    final static String MODE_8BIT_BYTE = "0100"; //"0100";
    final static String MODE_KANJI = "1000"; //"1000";
    
    final static byte ERROR_CORRECTING_LEVEL_L = 1;
    final static byte ERROR_CORRECTING_LEVEL_M = 0;
    final static byte ERROR_CORRECTING_LEVEL_Q = 3;
    final static byte ERROR_CORRECTING_LEVEL_H = 2;

    final static int NUMETIC_MAX_BIT = 10;
    final static int ALPHA_NUMERIC_MAX_BIT = 9;
    final static int EIGHT_BIT_MAX_BIT = 8;
    
    final static int BYTE_SIZE = 8;
    
    static byte levelMode = 0;
    
    private int maxBit = 0;
    
    final static int STANDINTEGER = 32768;
    
    final static String FILLOUT_FIRST = "11101100";
    final static String FILLOUT_SECOND = "00010001";
    
    private static int maxNumber = 0;
    
    private Integer[] gxValue;
    
    private Integer[] orginalValue;
    private Integer[] lastValue;
    private Integer[] keepingValue;
    private Integer[] finalValue;
    
    private int numeticMaxCharacter = 0;
    private int alphaNumericMaxCharater = 0;
    private int eightBitMaxCharacter = 0;
    
    private int dataCodeWords;
    private int ecCodeWords;
    
    private int fullByte = 0;
    private int halfByte = 0;
    private int oneByte = 0;

    private String inputString = ""; 
    private String modeString = MODE_ALPHA_NUMERIC; // alpha-numeric 77 numeric 127

    private String keepString = "";
 
    
    private Integer[] keepNumber;
    
    static int Number = 0;
    static boolean isItFirst = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        create = (Button)findViewById(R.id.create);
        text1 = (EditText)findViewById(R.id.editText1);
        
//        //byte level = ERROR_CORRECTING_LEVEL_H;
//        levelMode = ERROR_CORRECTING_LEVEL_L;
//        // initial all variable according to error correcting level
//        initVariable(levelMode);
//        // initial our error code 
//        initErrorCode();
//        // initial other variable accoring to Mode 
//        initMode(modeString);
//        
//        Number = 0; 
//                
//		if ( isItFirst )
//		{
//        	baseMode(modeString);
//		}
//		
//		isItFirst = false;

        create.setOnClickListener(this);
    }
    
    
    private void initMode(String str) {
    	
		if ( str.compareToIgnoreCase(MODE_NUMERIC) == 0)
		{
			keepNumber = new Integer[numeticMaxCharacter];
			maxBit = NUMETIC_MAX_BIT;
			fullByte = 10;
			halfByte = 7;
			oneByte = 4;
		}
		else if (str.compareToIgnoreCase(MODE_ALPHA_NUMERIC) == 0)
		{
			keepNumber = new Integer[alphaNumericMaxCharater];
			maxBit = ALPHA_NUMERIC_MAX_BIT;
			fullByte = 11;
			halfByte = 6;
		
		}
		else if (str.compareToIgnoreCase(MODE_8BIT_BYTE) == 0)
		{
			keepNumber = new Integer[eightBitMaxCharacter];
			maxBit = EIGHT_BIT_MAX_BIT;
		}
	}


	private void initErrorCode() {
   
        orginalValue = new Integer [VERSION_BYTE];
        keepingValue = new Integer [VERSION_BYTE];
        finalValue = new Integer [VERSION_BYTE];
        lastValue = new Integer [VERSION_BYTE];
        for ( int i = 0 ; i < VERSION_BYTE ; i++)
        {
        	orginalValue[i] = 0;
        	keepingValue[i] = 0;
        	finalValue[i] = 0;
        	lastValue[i] = 0;
        }
	}


	private void baseMode(String mode)
    {
    	keepingString(mode);
    	
    	int keepingNumber = STANDINTEGER | inputString.length();
     	
    	String tempBinary = Integer.toBinaryString(keepingNumber);
    	   	
    	int sub = tempBinary.length() - maxBit;
	
    	String finaltemp = tempBinary.substring(sub);

    	keepingString(finaltemp);
    
    	dataToNumber(mode);
    }
    
    private void dataToNumber(String mode)
    {
    	
    	if ( mode.compareToIgnoreCase(MODE_ALPHA_NUMERIC) == 0)
    	{
    		AlphaNumeric();	
    	}
    	else if ( mode.compareToIgnoreCase(MODE_NUMERIC) == 0)
    	{
    		Numeric();
    	}
    	
    	keepingString("0000");
    	
    	int xx = (BYTE_SIZE - (keepString.length() % BYTE_SIZE));
    	
    	fillOutStringWithZero(xx);

    	int numberTemp = dataCodeWords - (keepString.length()/BYTE_SIZE);
    	
    	
    	for ( int z = 0 ; z < numberTemp ; z++)
    	{
    		if ( z % 2 == 0 )
    			keepingString(FILLOUT_FIRST);
    		else
    			keepingString(FILLOUT_SECOND);
    	}    
   	
    	changeStringToNumber(keepString);
    }
   
    private void Numeric() {
    	
    	String tempString = "";
    	int tempNum = 0;
    	
    	int whatNumber = inputString.length() / 3;
    	int remainer = inputString.length() % 3;
    	
    	for ( int i = 0 ; i < whatNumber; i++)
    	{
    		tempNum = Integer.parseInt(inputString.substring(i*3, (i*3)+3));

    		tempString = Integer.toBinaryString(tempNum);

			if (tempString.length() <= fullByte )
			{	
				int s = fullByte - tempString.length();		
				fillOutStringWithZero(s);
				keepingString(tempString);
			}
    	}
    	
    	if ( remainer == 1 )
    	{
    		tempNum = Integer.parseInt(inputString.substring(3*(whatNumber),3*(whatNumber ) + remainer));

    		tempString = Integer.toBinaryString(tempNum);

			if (tempString.length() <= oneByte )
			{	
				int s = oneByte - tempString.length();		
				fillOutStringWithZero(s);
				keepingString(tempString);
			}
    	}
    	else if ( remainer ==2 )
    	{
    		tempNum = Integer.parseInt(inputString.substring(3*(whatNumber ),3*(whatNumber ) + remainer));

    		tempString = Integer.toBinaryString(tempNum);

			if (tempString.length() <= halfByte )
			{	
				int s = halfByte - tempString.length();		
				fillOutStringWithZero(s);
				keepingString(tempString);
			}	
    	}	
	}


	private void AlphaNumeric() {
    	
    	String tempString = "";
    	int tempNum = 0;
    	int number = 0;
    	
       	for ( int i = 0 ; i < inputString.length(); i++)
    	{
			tempNum = convertCharToByte(inputString.substring(i, i+1));
			if ( tempNum == -1)
				return;
			
    		if ( i % 2 == 0 ) 
    		{
    			// this is the last thing.
    			if ( i + 1 >= inputString.length() )
    			{
    				number = tempNum; 	
    				tempString = Integer.toBinaryString(number);
    				
        			if (tempString.length() <= halfByte )
        			{	
        				int s = halfByte - tempString.length();		
        				fillOutStringWithZero(s);
        				keepingString(tempString);
        			}
    			}
    			else 
    			{
    				number = tempNum * 45; 	
    			}
    			
    		}
    		else
    		{
    			number += tempNum;

    			tempString = Integer.toBinaryString(number);

    			number = 0;
    					
    			if (tempString.length() <= fullByte )
    			{	
    				int s = fullByte - tempString.length();		
    				fillOutStringWithZero(s);
    				keepingString(tempString);
    			}
    		}
    	}	
	}


	private void changeStringToNumber(String value)
    {
    	int temp = value.length() / BYTE_SIZE;
    	String tempString = "";
    	int tempNum = 0;
    	for (int i = 0 ; i < temp; i++ )
    	{
    		tempString = value.substring(i*BYTE_SIZE, (i*BYTE_SIZE)+BYTE_SIZE);

    		tempNum = binaryToDecimal(tempString);

    		keepNumber[i] = tempNum;

    		orginalValue[i] = tempNum;
    		lastValue[i] = tempNum;
    	}
    	
    	makingGX();
    }
    
    private void makingGX()
    {
    	int alpha = convertIntegerToAlpha(orginalValue[Number]);
    	for ( int i = 0 ; i < gxValue.length; i++)
    	{
    		int numTemp = gxValue[i] + alpha;
    		if ( numTemp >= 255 ) numTemp -= 255;
    		
    		int num = convertAlphaToInteger(numTemp);

    		keepingValue[i+Number] = num;
    	}	
    	exclusiveLogicalSum();
    }
    
    private void exclusiveLogicalSum()
    {		
    	for ( int i = Number ; i < VERSION_BYTE; i++)
    	{
    		finalValue[i] = orginalValue[i] ^ keepingValue[i];  	
    		orginalValue[i] = finalValue[i];
    	}

		if (Number >= maxNumber)
		{
			for ( int j = 0 ; j < VERSION_BYTE; j++)
			{		
				lastValue[j] += orginalValue[j];
			}
			
			storingQrData();	
			return;
		}		
    	Number++;
    	makingGX();
    }
    
    
    private void storingQrData() {
	
    	for ( int i = 0 ; i < VERSION_BYTE; i++)
		{
			String temp = Integer.toBinaryString(lastValue[i]);
			String tempStr = "";
			if ( temp.length() < BYTE_SIZE )
			{
				int s = BYTE_SIZE - temp.length();
				for ( int j = 0 ; j < s; j++)
				{
					tempStr += "0";
				}
			}
			tempStr += temp;
			QrData.QrStringdata += tempStr;
		}
    	
	}


	private void fillOutStringWithZero(int num)
    {
    	for ( int i = 0 ; i < num; i++)
    	{
    		keepingString("0");
    	}
    }
    
    private void keepingString (String str)
    {
    	if (str.length() == 0) return;
    	keepString += str;
    }
    


	public void initVariable (byte level)
    {
    	switch(level)
    	{
    	case ERROR_CORRECTING_LEVEL_L :
    		dataCodeWords = 55;
    		ecCodeWords = 15;
    		numeticMaxCharacter = 127;
    		alphaNumericMaxCharater = 77;
    		eightBitMaxCharacter = 53;
    		maxNumber = 54;
    		gxValue = new Integer [] {0,8,183,61,91,202,37,51,58,58,237,140,124,5,99,105};
    		break;
    	case ERROR_CORRECTING_LEVEL_M :
    		dataCodeWords = 44;
    		ecCodeWords = 26;
    		numeticMaxCharacter = 101;
    		alphaNumericMaxCharater = 61;
    		eightBitMaxCharacter = 42;
    		maxNumber = 43;
    		gxValue = new Integer [] {0,173,125,158,2,103,182,118,17,145,201,111,28,165,53,161,21,245,142,13,102,48,227,153,145,218,70}; // 26
    		break;
    	case ERROR_CORRECTING_LEVEL_Q :
    		dataCodeWords = 34;
    		ecCodeWords = 36;
    		numeticMaxCharacter = 77;
    		alphaNumericMaxCharater = 47;
    		eightBitMaxCharacter = 32;
    		maxNumber = 33;
    		gxValue = new Integer [] {0,200,183,98,16,172,31,246,234,60,152,115,0,167,152,113,248,238,107,18,63,218,37,87,210,105,177,120,74,121,196,117,251,113,233,30,120}; // 36
    		break;
    	case ERROR_CORRECTING_LEVEL_H :
    		dataCodeWords = 26;
    		ecCodeWords = 44;
    		numeticMaxCharacter = 58;
    		alphaNumericMaxCharater = 35;
    		eightBitMaxCharacter = 24;
    		maxNumber = 25;
    		gxValue = new Integer [] {0,190,7,61,121,71,246,69,55,168,188,89,243,191,25,72,123,9,145,14,247,1,238,44,78,143,62,224,126,118,114,68,163,52,194,217,147,204,169,37,130,113,102,73,181}; // 44
    		break;
    	default:
    		break;  		
    	}
    	
    }
    
    public int convertCharToByte(String str)
    {
    	if ( str.compareToIgnoreCase("a") == 0) return 10;
    	else if (str.compareToIgnoreCase("b") == 0) return 11;
    	else if (str.compareToIgnoreCase("c") == 0) return 12;
    	else if (str.compareToIgnoreCase("d") == 0) return 13;
    	else if (str.compareToIgnoreCase("e") == 0) return 14;
    	else if (str.compareToIgnoreCase("f") == 0) return 15;
    	else if (str.compareToIgnoreCase("g") == 0) return 16;
    	else if (str.compareToIgnoreCase("h") == 0) return 17;
    	else if (str.compareToIgnoreCase("i") == 0) return 18;
    	else if (str.compareToIgnoreCase("j") == 0) return 19;
    	else if (str.compareToIgnoreCase("k") == 0) return 20;
    	else if (str.compareToIgnoreCase("l") == 0) return 21;
    	else if (str.compareToIgnoreCase("m") == 0) return 22;
    	else if (str.compareToIgnoreCase("n") == 0) return 23;
    	else if (str.compareToIgnoreCase("o") == 0) return 24;
    	else if (str.compareToIgnoreCase("p") == 0) return 25;
    	else if (str.compareToIgnoreCase("q") == 0) return 26;
    	else if (str.compareToIgnoreCase("r") == 0) return 27;
    	else if (str.compareToIgnoreCase("s") == 0) return 28;
    	else if (str.compareToIgnoreCase("t") == 0) return 29;
    	else if (str.compareToIgnoreCase("u") == 0) return 30;
    	else if (str.compareToIgnoreCase("v") == 0) return 31;
    	else if (str.compareToIgnoreCase("w") == 0) return 32;
    	else if (str.compareToIgnoreCase("x") == 0) return 33;
    	else if (str.compareToIgnoreCase("y") == 0) return 34;
    	else if (str.compareToIgnoreCase("z") == 0) return 35;
    	else if (str.compareToIgnoreCase(" ") == 0) return 36;
    	else if (str.compareToIgnoreCase("$") == 0) return 37;
    	else if (str.compareToIgnoreCase("%") == 0) return 38;
    	else if (str.compareToIgnoreCase("*") == 0) return 39;
    	else if (str.compareToIgnoreCase("+") == 0) return 40;
    	else if (str.compareToIgnoreCase("-") == 0) return 41;
    	else if (str.compareToIgnoreCase(".") == 0) return 42;
    	else if (str.compareToIgnoreCase("/") == 0) return 43;
    	else if (str.compareToIgnoreCase(":") == 0) return 44;
    	else if (str.compareToIgnoreCase("0") == 0) return 0;
    	else if (str.compareToIgnoreCase("1") == 0) return 1;
    	else if (str.compareToIgnoreCase("2") == 0) return 2;
    	else if (str.compareToIgnoreCase("3") == 0) return 3;
    	else if (str.compareToIgnoreCase("4") == 0) return 4;
    	else if (str.compareToIgnoreCase("5") == 0) return 5;
    	else if (str.compareToIgnoreCase("6") == 0) return 6;
    	else if (str.compareToIgnoreCase("7") == 0) return 7;
    	else if (str.compareToIgnoreCase("8") == 0) return 8;
    	else if (str.compareToIgnoreCase("9") == 0) return 9;
		
    	return -1;
    	
    	
    }
    
    public int convertIntegerToAlpha(int number)
    {
    	switch (number)
    	{
    	case 1:	return 0; 	case 2: return 1;	case 3: return 25;	case 4: return 2;	case 5:return 50;
    	case 6: return 26;	case 7: return 198;	case 8: return 3;	case 9: return 223;	case 10: return 51;
    	case 11: return 238;case 12: return 27; case 13: return 104;case 14: return 199;case 15: return 75;
    	case 16: return 4;	case 17: return 100;case 18: return 224;case 19: return 14; case 20: return 52;
    	case 21: return 141;case 22: return 239;case 23: return 129;case 24: return 28;	case 25: return 193;
    	case 26: return 105;case 27: return 248;case 28: return 200;case 29: return 8;	case 30: return 76;
    	case 31: return 113;case 32: return 5;	case 33: return 138;case 34: return 101;case 35: return 47;
    	case 36: return 225;case 37: return 36;	case 38: return 15;	case 39: return 33;	case 40: return 53;
    	case 41: return 147;case 42: return 142;case 43: return 218;case 44: return 240;case 45: return 18;
    	case 46: return 130;case 47: return 69; case 48: return 29;	case 49: return 181;case 50: return 194;
    	case 51: return 125;case 52: return 106;case 53: return 39;	case 54: return 249;case 55: return 185;
    	case 56: return 201;case 57: return 154;case 58: return 9;	case 59: return 120;case 60: return 77;
    	case 61: return 228;case 62: return 114;case 63: return 166;case 64: return 6;	case 65: return 191;
    	case 66: return 139;case 67: return 98;	case 68: return 102;case 69: return 221;case 70: return 48;
    	case 71: return 253;case 72: return 226;case 73: return 152;case 74: return 37;	case 75: return 179;
    	case 76: return 16; case 77: return 145;case 78: return 34; case 79: return 136;case 80: return 54;
    	case 81: return 208;case 82: return 148;case 83: return 206;case 84: return 143;case 85: return 150;
    	case 86: return 219;case 87: return 189;case 88: return 241;case 89: return 210;case 90: return 19;
    	case 91: return 92; case 92: return 131;case 93: return 56; case 94: return 70; case 95: return 64;
    	case 96: return 30; case 97: return 66; case 98: return 182;case 99: return 163;case 100:return 195;
    	case 101:return 72; case 102:return 126;case 103:return 110;case 104:return 107;case 105:return 58;
    	case 106:return 40; case 107:return 84; case 108:return 250;case 109:return 133;case 110:return 186;
    	case 111:return 61;	case 112:return 202;case 113:return 94;	case 114:return 155;case 115:return 159;
    	case 116:return 10; case 117:return 21; case 118:return 121;case 119:return 43;	case 120:return 78;
    	case 121:return 212;case 122:return 229;case 123:return 172;case 124:return 115;case 125:return 243;
    	case 126:return 167;case 127:return 87; case 128:return 7;	case 129:return 112;case 130:return 192;
    	case 131:return 247;case 132:return 140;case 133:return 128;case 134:return 99; case 135:return 13;
    	case 136:return 103;case 137:return 74; case 138:return 222;case 139:return 237;case 140:return 49;
    	case 141:return 197;case 142:return 254;case 143:return 24; case 144:return 227;case 145:return 165;
    	case 146:return 153;case 147:return 119;case 148:return 38; case 149:return 184;case 150:return 180;
    	case 151:return 124;case 152:return 17; case 153:return 68; case 154:return 146;case 155:return 217;
    	case 156:return 35; case 157:return 32; case 158:return 137;case 159:return 46; case 160:return 55;
    	case 161:return 63; case 162:return 209;case 163:return 91; case 164:return 149;case 165:return 188;
    	case 166:return 207;case 167:return 205;case 168:return 144;case 169:return 135;case 170:return 151;
    	case 171:return 178;case 172:return 220;case 173:return 252;case 174:return 190;case 175:return 97;
    	case 176:return 242;case 177:return 86; case 178:return 211;case 179:return 171;case 180:return 20;
    	case 181:return 42; case 182:return 93; case 183:return 158;case 184:return 132;case 185:return 60;
    	case 186:return 57; case 187:return 83; case 188:return 71; case 189:return 109;case 190:return 65;
    	case 191:return 162;case 192:return 31; case 193:return 45; case 194:return 67; case 195:return 216;
    	case 196:return 183;case 197:return 123;case 198:return 164;case 199:return 118;case 200:return 196;
    	case 201:return 23; case 202:return 73; case 203:return 236;case 204:return 127;case 205:return 12;
    	case 206:return 111;case 207:return 246;case 208:return 108;case 209:return 161;case 210:return 59;
    	case 211:return 82; case 212:return 41; case 213:return 157;case 214:return 85; case 215:return 170;
    	case 216:return 251;case 217:return 96; case 218:return 134;case 219:return 177;case 220:return 187;
    	case 221:return 204;case 222:return 62; case 223:return 90; case 224:return 203;case 225:return 89;
    	case 226:return 95; case 227:return 176;case 228:return 156;case 229:return 169;case 230:return 160;
    	case 231:return 81; case 232:return 11; case 233:return 245;case 234:return 22; case 235:return 235;
    	case 236:return 122;case 237:return 117;case 238:return 44; case 239:return 215;case 240:return 79;
    	case 241:return 174;case 242:return 213;case 243:return 233;case 244:return 230;case 245:return 231;
    	case 246:return 173;case 247:return 232;case 248:return 116;case 249:return 214;case 250:return 244;
    	case 251:return 234;case 252:return 168;case 253:return 80; case 254:return 88; case 255:return 175;
    	}
		return number;
    }
    
    public int convertAlphaToInteger(int number)
    {
    	switch (number)
    	{
    	case 0: return 1;
    	case 1:	return 2; 	case 2: return 4;	case 3: return 8;	case 4: return 16;	case 5:return 32;
    	case 6: return 64;	case 7: return 128;	case 8: return 29;	case 9: return 58;	case 10: return 116;
    	case 11: return 232;case 12: return 205; case 13: return 135;case 14: return 19;case 15: return 38;
    	case 16: return 76;	case 17: return 152;case 18: return 45;case 19: return 90; case 20: return 180;
    	case 21: return 117;case 22: return 234;case 23: return 201;case 24: return 143;case 25: return 3;
    	case 26: return 6;case 27: return 12;case 28: return 24;case 29: return 48;	case 30: return 96;
    	case 31: return 192;case 32: return 157;case 33: return 39;case 34: return 78;case 35: return 156;
    	case 36: return 37;case 37: return 74;	case 38: return 148;case 39: return 53;	case 40: return 106;
    	case 41: return 212;case 42: return 181;case 43: return 119;case 44: return 238;case 45: return 193;
    	case 46: return 159;case 47: return 35; case 48: return 70;	case 49: return 140;case 50: return 5;
    	case 51: return 10;case 52: return 20;case 53: return 40;	case 54: return 80;case 55: return 160;
    	case 56: return 93;case 57: return 186;case 58: return 105;	case 59: return 210;case 60: return 185;
    	case 61: return 111;case 62: return 222;case 63: return 161;case 64: return 95;	case 65: return 190;
    	case 66: return 97;case 67: return 194;	case 68: return 153;case 69: return 47;case 70: return 94;
    	case 71: return 188;case 72: return 101;case 73: return 202;case 74: return 137;case 75: return 15;
    	case 76: return 30; case 77: return 60;case 78: return 120; case 79: return 240;case 80: return 253;
    	case 81: return 231;case 82: return 211;case 83: return 187;case 84: return 107;case 85: return 214;
    	case 86: return 177;case 87: return 127;case 88: return 254;case 89: return 225;case 90: return 223;
    	case 91: return 163; case 92: return 91;case 93: return 182; case 94: return 113; case 95: return 226;
    	case 96: return 217; case 97: return 175; case 98: return 67;case 99: return 134;case 100:return 17;
    	case 101:return 34; case 102:return 68;case 103:return 136;case 104:return 13;case 105:return 26;
    	case 106:return 52; case 107:return 104; case 108:return 208;case 109:return 189;case 110:return 103;
    	case 111:return 206;case 112:return 129;case 113:return 31;	case 114:return 62;case 115:return 124;
    	case 116:return 248; case 117:return 237; case 118:return 199;case 119:return 147;	case 120:return 59;
    	case 121:return 118;case 122:return 236;case 123:return 197;case 124:return 151;case 125:return 51;
    	case 126:return 102;case 127:return 204; case 128:return 133;	case 129:return 23;case 130:return 46;
    	case 131:return 92;case 132:return 184;case 133:return 109;case 134:return 218; case 135:return 169;
    	case 136:return 79;case 137:return 158; case 138:return 33;case 139:return 66;case 140:return 132;
    	case 141:return 21;case 142:return 42;case 143:return 84; case 144:return 168;case 145:return 77;
    	case 146:return 154;case 147:return 41;case 148:return 82; case 149:return 164;case 150:return 85;
    	case 151:return 170;case 152:return 73; case 153:return 146; case 154:return 57;case 155:return 114;
    	case 156:return 228; case 157:return 213; case 158:return 183;case 159:return 115; case 160:return 230;
    	case 161:return 209; case 162:return 191;case 163:return 99; case 164:return 198;case 165:return 145;
    	case 166:return 63;case 167:return 126;case 168:return 252;case 169:return 229;case 170:return 215;
    	case 171:return 179;case 172:return 123;case 173:return 246;case 174:return 241;case 175:return 255;
    	case 176:return 227;case 177:return 219; case 178:return 171;case 179:return 75;case 180:return 150;
    	case 181:return 49; case 182:return 98; case 183:return 196;case 184:return 149;case 185:return 55;
    	case 186:return 110; case 187:return 220; case 188:return 165; case 189:return 87;case 190:return 174;
    	case 191:return 65;case 192:return 130; case 193:return 25; case 194:return 50; case 195:return 100;
    	case 196:return 200;case 197:return 141;case 198:return 7;case 199:return 14;case 200:return 28;
    	case 201:return 56; case 202:return 112; case 203:return 224;case 204:return 221;case 205:return 167;
    	case 206:return 83;case 207:return 166;case 208:return 81;case 209:return 162;case 210:return 89;
    	case 211:return 178; case 212:return 121; case 213:return 242;case 214:return 249; case 215:return 239;
    	case 216:return 195;case 217:return 155; case 218:return 43;case 219:return 86;case 220:return 172;
    	case 221:return 69;case 222:return 138; case 223:return 9; case 224:return 18;case 225:return 36;
    	case 226:return 72; case 227:return 144;case 228:return 61;case 229:return 122;case 230:return 244;
    	case 231:return 245; case 232:return 247; case 233:return 243;case 234:return 251; case 235:return 235;
    	case 236:return 203;case 237:return 139;case 238:return 11; case 239:return 22;case 240:return 44;
    	case 241:return 88;case 242:return 176;case 243:return 125;case 244:return 250;case 245:return 233;
    	case 246:return 207;case 247:return 131;case 248:return 27;case 249:return 54;case 250:return 108;
    	case 251:return 216;case 252:return 173;case 253:return 71; case 254:return 142; case 255:return 1;
    	}
		return number;
    }
    
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.create) {
			inputString = text1.getText().toString();

			
			QrData.QrStringdata = "";
			// byte level = ERROR_CORRECTING_LEVEL_H;
			levelMode = ERROR_CORRECTING_LEVEL_L;
			// initial all variable according to error correcting level
			initVariable(levelMode);
			if (inputString.length() > alphaNumericMaxCharater)
			{
				Toast.makeText(this, "No more than 77 characters", Toast.LENGTH_LONG).show();
				return;
			}
			// initial our error code
			initErrorCode();
			// initial other variable accoring to Mode
			initMode(modeString);

			Number = 0;

			baseMode(modeString);

			isItFirst = false;

			
			
			Intent i = new Intent().setClass(QrcodeGeneratorActivity.this,
					QrcodeData.class);
			startActivity(i);
			// overridePendingTransition(0,0);
			QrcodeGeneratorActivity.this.finish();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ( keyCode == KeyEvent.KEYCODE_BACK )
		{
			Intent startMain = new Intent(Intent.ACTION_MAIN);
			startMain.addCategory(Intent.CATEGORY_HOME);
			startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(startMain);
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public int binaryToDecimal(String s) {
		
		if (Integer.parseInt(s) == 0) { 
			return 0; 
		} else {
			int number = Integer.parseInt(s);
			int lastDigit = number % 10;
			int remainder = number / 10;
			return lastDigit + 2 * binaryToDecimal(String.valueOf(remainder));
		}
	}
}