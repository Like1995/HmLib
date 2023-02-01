package com.like.kotlinkit;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.Charset;

import kotlin.text.Charsets;

/*
 *@author: ZhengHaibo
 *web:     blog.csdn.net/nuptboyzhb
 *mail:    zhb931706659@126.com
 *2012-9-25  Nanjing njupt
 */
public class DataTypeTransform {
	public DataTypeTransform(){
		
	}
	/**
	 *
	 */
	public byte[] ShortToByteArray(short n) {
		byte[] b = new byte[2];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		return b;
	}
	/**
	 * byte
	 */
	public short ByteArrayToShort(byte[] bArr) {
		int n = 0;
		short sum = 0;
		sum += bArr[0] & 0xff;
		n = bArr[1] & 0xff;
		sum += n << 8; 
		return sum;
	}
	/**
	 *
	 */
	public byte[] IntToByteArray(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}
	/**
	 *
	 */
	public int ByteArrayToInt(byte[] bArr) {
		int n = 0;
		int sum = 0;
		sum += bArr[0] & 0xff;
		n = bArr[1] & 0xff;
		sum += n << 8; 
		n = bArr[2] & 0xff;
		sum += n << 16;
		n = bArr[3] & 0xff;
		sum += n << 24;
		return sum;
	}
	/**
	 *
	 */
	public byte[] SIntToByteArray(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		return b;
	}
	public int ByteArrayToSInt(byte[] bArr) {
		
		int n = 0;
		int sum = 0;
		sum += bArr[0] & 0xff;
		n = bArr[1] & 0xff;
		sum += n << 8; 
		return sum;

	}
	/**
	 *
	 */
	public byte[] LongToByteArray(long n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}
	public long ByteArrayToLong(byte[] bArr) {	
		long n = 0;
		long sum = 0;
		sum += bArr[0] & 0xff;
		n = bArr[1] & 0xff;
		sum += n << 8; 
		n = bArr[2] & 0xff;
		sum += n << 16;
		n = bArr[3] & 0xff;
		sum += n << 24;
		return sum;
	}
	/**
	 *
	 */
	public String ByteArraytoPhysicID(byte[] valArr) {
		String str = "";
		for(int i = 0; i< 6; i++)
		{
		   String str1 = String.format("%02X", valArr[i]);
		   str = str + str1;
		}
		return str;
	}
	/**
	 *
	 */
	public String ByteArraytoString(byte[] valArr,int maxLen) {
		String result=null;
		int index = 0;
		while(index < valArr.length && index < maxLen) {
			if(valArr[index] == 0) {
				break;
			}
			index++;
		}
		byte[] temp = new byte[index];
		System.arraycopy(valArr, 0, temp, 0, index);
		try {
			result= new String(temp,Charsets.UTF_8);//"UNICODE");//
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public byte[] StringToByteArray(String str){
		byte[] temp = null;
	    try {
			temp = str.getBytes(Charsets.UTF_8);//"UNICODE");//
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}
	/**
	 * 浮点转换为字节
	 *
	 * @param f
	 * @return
	 */
	public static byte[] FloatToByteArray(float f) {

		// 把float转换为byte[]
		int fbit = Float.floatToIntBits(f);
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (fbit >> (24 - i * 8));
		}

		// 翻转数组
		int len = b.length;
		// 建立一个与源数组元素类型相同的数组
		byte[] dest = new byte[len];
		// 为了防止修改源数组，将源数组拷贝一份副本
		System.arraycopy(b, 0, dest, 0, len);
		byte temp;
		// 将顺位第i个与倒数第i个交换
		for (int i = 0; i < len / 2; ++i) {
			temp = dest[i];
			dest[i] = dest[len - i - 1];
			dest[len - i - 1] = temp;
		}
		return dest;
	}

	/**
	 * 字节转换为浮点
	 *
	 * @param b 字节（至少4个字节）
	 * @param index 开始位置
	 */
	public static float ByteArrayToFloat(byte[] b, int index) {
		int l;
		l = b[index + 0];
		l &= 0xff;
		l |= ((long) b[index + 1] << 8);
		l &= 0xffff;
		l |= ((long) b[index + 2] << 16);
		l &= 0xffffff;
		l |= ((long) b[index + 3] << 24);
		return Float.intBitsToFloat(l);
	}
	public static int getInt(byte[] bytes)
	{
		return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
	}
	public static byte[] getBytes(float data)
	{
		int intBits = Float.floatToIntBits(data);
		return getBytes(intBits);
	}
	public static float getFloat(byte[] bytes)
	{
		return Float.intBitsToFloat(getInt(bytes));
	}
	//字符到字节转换
	public static byte[] charToByte(char ch){
		int temp=(int)ch;
		byte[] b=new byte[2];
		for (int i=b.length-1;i>-1;i--){
			b[i] = new Integer(temp&0xff).byteValue();      //将最高位保存在最低位
			temp = temp >> 8;       //向右移8位
		}
		return b;
	}

	//字节到字符转换
	public static char byteToChar(byte[] b){
		int s=0;
		if(b[0]>0)
			s+=b[0];
		else
			s+=256+b[0];
		s*=256;
		if(b[1]>0)
			s+=b[1];
		else
			s+=256+b[1];
		char ch=(char)s;
		return ch;
	}



	//浮点到字节转换

	public static byte[] DoubleToByteArray(double d){
		byte[] b=new byte[8];
		long l=Double.doubleToLongBits(d);
		for(int i=0;i<b.length;i++){
			b[i]=new Long(l).byteValue();
			l=l>>8;
		}
		return b;
	}

	//字节到浮点转换
	public static double ByteArrayToDouble(byte[] b){
		long l;
		l=b[0];
		l&=0xff;
		l|=((long)b[1]<<8);
		l&=0xffff;
		l|=((long)b[2]<<16);
		l&=0xffffff;
		l|=((long)b[3]<<24);
		l&=0xffffffffl;
		l|=((long)b[4]<<32);
		l&=0xffffffffffl;
		l|=((long)b[5]<<40);
		l&=0xffffffffffffl;
		l|=((long)b[6]<<48);
		l&=0xffffffffffffffl;
		l|=((long)b[7]<<56);
		return Double.longBitsToDouble(l);
	}
	public static double[] asDoubleArray(byte[] input){
		if(null == input ){
			return null;
		}
		DoubleBuffer buffer = ByteBuffer.wrap(input).asDoubleBuffer();
		double[] res = new double[buffer.remaining()];
		buffer.get(res);
		return res;
	}
	public static float[] asFloatArray(byte[] input){
		if(null == input ){
			return null;
		}
		FloatBuffer buffer = ByteBuffer.wrap(input).asFloatBuffer();
		float[] res = new float[buffer.remaining()];
		buffer.get(res);
		return res;
	}
	public static int[] asIntArray(byte[] input){
		if(null == input ){
			return null;
		}
		IntBuffer buffer = ByteBuffer.wrap(input).asIntBuffer();
		int[] res = new int[buffer.remaining()];
		buffer.get(res);
		return res;
	}
	public static long[] asLongArray(byte[] input){
		if(null == input ){
			return null;
		}
		LongBuffer buffer = ByteBuffer.wrap(input).asLongBuffer();
		long[] res = new long[buffer.remaining()];
		buffer.get(res);
		return res;
	}
	public static short[] asShortArray(byte[] input){
		if(null == input ){
			return null;
		}
		ShortBuffer buffer = ByteBuffer.wrap(input).asShortBuffer();
		short[] res = new short[buffer.remaining()];
		buffer.get(res);
		return res;
	}
	/**
	 * {@link DoubleBuffer} TO {@link ByteBuffer}
	 * @param input
	 * @return
	 */
	public static ByteBuffer asByteBuffer(DoubleBuffer input){
		if(null == input ){
			return null;
		}
		ByteBuffer buffer = ByteBuffer.allocate(input.capacity()* (Double.SIZE/8));
		while(input.hasRemaining()){
			buffer.putDouble(input.get());
		}
		return buffer;
	}
	/**
	 * double[] TO byte[]
	 * @param input
	 * @return
	 */
	public static byte[] asByteArray(double[] input){
		if(null == input ){
			return null;
		}
		return asByteBuffer(DoubleBuffer.wrap(input)).array();
	}
	/**
	 * {@link FloatBuffer} TO {@link ByteBuffer}
	 * @param input
	 * @return
	 */
	public static ByteBuffer asByteBuffer(FloatBuffer input){
		if(null == input ){
			return null;
		}
		ByteBuffer buffer = ByteBuffer.allocate(input.capacity()* (Float.SIZE/8));
		while(input.hasRemaining()){
			buffer.putFloat(input.get());
		}
		return buffer;
	}
	/**
	 * float[] TO byte[]
	 * @param input
	 * @return
	 */
	public static byte[] asByteArray(float[] input){
		if(null == input ){
			return null;
		}
		return asByteBuffer(FloatBuffer.wrap(input)).array();
	}
	/**
	 * {@link IntBuffer} TO {@link ByteBuffer}
	 * @param input
	 * @return
	 */
	public static ByteBuffer asByteBuffer(IntBuffer input){
		if(null == input ){
			return null;
		}
		ByteBuffer buffer = ByteBuffer.allocate(input.capacity()* (Integer.SIZE/8));
		while(input.hasRemaining()){
			buffer.putInt(input.get());
		}
		return buffer;
	}
	/**
	 * int[] TO byte[]
	 * @param input
	 * @return
	 */
	public static byte[] asByteArray(int[] input){
		if(null == input ){
			return null;
		}
		return asByteBuffer(IntBuffer.wrap(input)).array();
	}
	/**
	 * {@link LongBuffer} TO {@link ByteBuffer}
	 * @param input
	 * @return
	 */
	public static ByteBuffer asByteBuffer(LongBuffer input){
		if(null == input ){
			return null;
		}
		ByteBuffer buffer = ByteBuffer.allocate(input.capacity()* (Long.SIZE/8));
		while(input.hasRemaining()){
			buffer.putLong(input.get());
		}
		return buffer;
	}
	/**
	 * long[] TO byte[]
	 * @param input
	 * @return
	 */
	public static byte[] asByteArray(long[] input){
		if(null == input ){
			return null;
		}
		return asByteBuffer(LongBuffer.wrap(input)).array();
	}
	/**
	 * {@link ShortBuffer} TO {@link ByteBuffer}
	 * @param input
	 * @return
	 */
	public static ByteBuffer asByteBuffer(ShortBuffer input){
		if(null == input ){
			return null;
		}
		ByteBuffer buffer = ByteBuffer.allocate(input.capacity()* (Short.SIZE/8));
		while(input.hasRemaining()){
			buffer.putShort(input.get());
		}
		return buffer;
	}
	/**
	 * short[] TO byte[]
	 * @param input
	 * @return
	 */
	public static byte[] asByteArray(short[] input){
		if(null == input ){
			return null;
		}
		return asByteBuffer(ShortBuffer.wrap(input)).array();
	}
	public float adjustFontSize(float nFSize){  
	      
	   /* if (ManageClientConServer.nScreenWidth <= 240) {        // 240X320 ��Ļ
	    	
	        return nFSize - 16;//10;  
	  
	    }else if (ManageClientConServer.nScreenWidth <= 320){   // 320X480 ��Ļ   
	    	
	        return nFSize-10;//14;  
	  
	    }else if (ManageClientConServer.nScreenWidth <= 480){   // 480X800 �� 480X854 ��Ļ  
	    	
	        return nFSize -6;//24;  
	  
	    }else if (ManageClientConServer.nScreenWidth <= 540){   // 540X960 ��Ļ   
	    	
	        return nFSize - 4;//26;  
	          
	    }else if(ManageClientConServer.nScreenWidth <= 600){    // 800X1280 ��Ļ   
	    	
	        return nFSize - 3;//30;  640*960  576*1520 440*486 800*1280 768*1024 864*1152  720*1280
	          
	    }else if(ManageClientConServer.nScreenWidth <= 720){    // 800X1280 ��Ļ    
	    	
	        return nFSize - 1;
	        
	    }else if(ManageClientConServer.nScreenWidth <= 800){    // 800X1280 ��Ļ    
	    	
	        return nFSize;//30;   850*1280 900*1600 960*1280 1024*1280  1080*1920
	          
	    }else if(ManageClientConServer.nScreenWidth <= 900){                          // ���� 800X1280  
	    	
	        return nFSize + 5;//32;  
	          
	    }else if(ManageClientConServer.nScreenWidth <= 1080){   
	    	
	        return nFSize + 10;//32;  
	          
	    }else if(ManageClientConServer.nScreenWidth <= 1280){                          // ���� 800X1280  
	    	
	        return nFSize + 18;//32;  
	          
	    }else{
	    	return nFSize + 24;//34;
	    }*/
	   return  nFSize;
	}
//byte[] 转化为char[]
	public char[] getChars (byte[] bytes) {
		Charset cs = Charset.forName ("UTF-8");
		ByteBuffer bb = ByteBuffer.allocate (bytes.length);
		bb.put (bytes);
		bb.flip ();
		CharBuffer cb = cs.decode (bb);
		return cb.array();
	}// char转byte

	//char[] 转化为byte[]
	public byte[] getBytes (char[] chars) {
		Charset cs = Charset.forName ("UTF-8");
		CharBuffer cb = CharBuffer.allocate (chars.length);
        cb.put (chars);
        cb.flip ();
        ByteBuffer bb = cs.encode (cb);
        return bb.array();
	}

	////////////////////////////////////////////////////////////////////////////////////////
	// 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
	static public int isOdd(int num)
	{
		return num & 0x1;
	}
	//-------------------------------------------------------
	static public int HexToInt(String inHex)//Hex字符串转int
	{
		return Integer.parseInt(inHex, 16);
	}
	//-------------------------------------------------------
	static public byte HexToByte(String inHex)//Hex字符串转byte
	{
		return (byte)Integer.parseInt(inHex,16);
	}
	//-------------------------------------------------------
	static public String Byte2Hex(Byte inByte)//1字节转2个Hex字符
	{
		return String.format("%02x", inByte).toUpperCase();
	}
	//-------------------------------------------------------
	static public String ByteArrToHex(byte[] inBytArr)//字节数组转转hex字符串
	{
		StringBuilder strBuilder=new StringBuilder();
		int j=inBytArr.length;
		for (int i = 0; i < j; i++)
		{
			strBuilder.append(Byte2Hex(inBytArr[i]));
			strBuilder.append(" ");
		}
		return strBuilder.toString();
	}
	//-------------------------------------------------------
	static public String ByteArrToHex(byte[] inBytArr,int offset,int byteCount)//字节数组转转hex字符串，可选长度
	{
		StringBuilder strBuilder=new StringBuilder();
		int j=byteCount;
		for (int i = offset; i < j; i++)
		{
			strBuilder.append(Byte2Hex(inBytArr[i]));
		}
		return strBuilder.toString();
	}
	//-------------------------------------------------------
	//转hex字符串转字节数组
	static public byte[] HexToByteArr(String inHex)//hex字符串转字节数组
	{
		int hexlen = inHex.length();
		byte[] result;
		if (isOdd(hexlen)==1)
		{//奇数
			hexlen++;
			result = new byte[(hexlen/2)];
			inHex="0"+inHex;
		}else {//偶数
			result = new byte[(hexlen/2)];
		}
		int j=0;
		for (int i = 0; i < hexlen; i+=2)
		{
			result[j]=HexToByte(inHex.substring(i,i+2));
			j++;
		}
		return result;
	}

	public int ByteToInt(byte data) {
		int n = data;
		n = n & 0xff;
		return n;
	}
}
