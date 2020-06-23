package pers.chemyoo.core.system;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class EncodingUtils {
	
	private EncodingUtils() {
		throw new NoSuchMethodError("EncodingUtils class can not instant.");
	}
	
	private final static String GBK = "GBK";

	/**
	 * 将原正确编码的字符串src，转化为编码为srcCharset的字符串
	 * 
	 * 前提是：确保原字符串的编码是无损（完整的）. 无需知道原字符串的具体编码，
	 * 转化为目标编码的字符串由java库自动实现，无需自己手动实现。
	 * 
	 * 如果原编码字符串不能转化为目标编码，将会抛出UnsupportedEncodingException
	 * 
	 * @param src
	 * @param srcCharset
	 * @param destCharet
	 * @return 转换后的字符串
	 * @throws UnsupportedEncodingException
	 */
	public static String convertEncodingStr(String src, String destCharet) throws UnsupportedEncodingException {
		byte[] bts = src.getBytes(destCharet);
		return new String(bts, destCharet);
	}

	
	/**
	 * 将编码为srcCharset的字节数组src转化为编码为destCharet的字节数组
	 * 
	 * @param src
	 * @param srcCharset
	 * @param destCharet
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] convertEncodingByteArr(byte[] src, String srcCharset, String destCharet) throws UnsupportedEncodingException{	
		String s = new String(src, srcCharset);
		return s.getBytes(destCharet);
	}


	/**
	 * 将字节数组byteArr转化为2位16进制字符串,每个16进制之间用空格分割
	 * 
	 * @param byteArr
	 * @return
	 */
	public static String byteToHex(byte... byteArr) {
		if (null == byteArr || byteArr.length == 0) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		String tmp = null;

		for (byte b : byteArr) {

			tmp = Integer.toHexString(b);
			// 切记：byte进行运算时，会自动转化为int，否则可能会出错
			if (b >>> 31 == 1) { // 最高位为1，负数
				sb.append(tmp.substring(6));
			} else { // 最高位为0，正数
				if(tmp.length() < 2)
					sb.append('0');
				sb.append(tmp);
			}
			sb.append(' ');
		}
		sb.deleteCharAt(sb.length() - 1); // delete last space
		return sb.toString();
	}
	
    /**
     * 判断是否为ISO-8859-1
     *
     * @return
     */
    public static boolean isISO(String str) {
    	return Charset.forName("ISO-8859-1").newEncoder().canEncode(str);
    }
 
    /**
     * 判断是否为UTF-8
     *
     * @return
     */
    public static boolean isUtf(String str) {
 
    	return Charset.forName("UTF-8").newEncoder().canEncode(str);
    }
 
    public static boolean isUnicode(String str) {
 
    	return Charset.forName("unicode").newEncoder().canEncode(str);
    }
	
    public static boolean isGbk(String str) {
    	return Charset.forName("gbk").newEncoder().canEncode(str);
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException
	{
    	String clazzString = new String("姓名流".getBytes("gbk"), "gbk");
		System.err.println(clazzString);
	}
    
    public static void transEcoding(OutputStream out, String text) throws IOException {
    	Charset defaultCharset = Charset.defaultCharset();
		String usedEcoding = InitSystemConfig.getInstance().getProperty("used.file.encoding", defaultCharset.displayName());
		/**
		String transEcode = props.getProperty("trans.file.encoding", defaultCharset.displayName());
		LogWriter.info(ConstantProcessor.class, "defaultCharset: %s.", defaultCharset.displayName());
		LogWriter.info(ConstantProcessor.class, "current used charset: %s.", usedEcode);
		LogWriter.info(ConstantProcessor.class, "trans to charset: %s.", transEcode);
		
//		if(defaultCharset.displayName().equalsIgnoreCase(usedEcode)) {
//			write.write(new String(text.getBytes(), transEcode));
//		} else {
//			write.write(new String(text.getBytes(usedEcode), transEcode));
//		}
		
//		byte[] encodingByte = text.getBytes();
//		for(int c : encodingByte) {
//			write.write(c);
//		}
//		write.flush();
//		InputStream input = new ByteArrayInputStream(text.getBytes());
//		String charset = CpdetectorUtils.getIOEncode(input);
//		LogWriter.info(ConstantProcessor.class, "input charset: %s.", charset);
		LogWriter.info("used charset:" + usedEcode + ", transEcode:" + transEcode);
		*/
		
		// 读取字符串编码，此处使用UTF-8编码，所以返回一定是UTF-8
//		InputStream input = new ByteArrayInputStream(encodingByte)
//		String charset = CpdetectorUtils.getIOEncode(input);
		String charset = "UTF-8";
		// 编译项目使用的编码是否是GBK,GB2312
		if("GBK,GB2312".contains(usedEcoding.toUpperCase())) {
			out.write(new String(text.getBytes(charset), "GBK").getBytes());
		} else {
			out.write(text.getBytes(charset));
		}
		out.flush();
	}

}

