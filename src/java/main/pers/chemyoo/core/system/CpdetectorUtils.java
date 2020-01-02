package pers.chemyoo.core.system;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;
import pers.chemyoo.core.logger.LogWriter;

public class CpdetectorUtils
{
	//获取文本编码
	private static final String FILE_ENCODE_TYPE = "file";
	//获取文件流编码
	private static final String IO_ENCODE_TYPE = "io";

	private CodepageDetectorProxy getDetector()
	{ 
		/*
		 * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。 cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，
		 * 如ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector。 detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的 字符集编码。
		 * 使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar cpDetector是基于统计学原理的，不保证完全正确。
		 */
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();

		/*
		 * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于 指示是否显示探测过程的详细信息，为false不显示。
		 */
		detector.add(new ParsingDetector(false));
		/*
		 * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码 测定。所以，一般有了这个探测器就可满足大多数项目的要求，
		 * 如果你还不放心，可以 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
		 */
		detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
		// ASCIIDetector用于ASCII编码测定
		detector.add(ASCIIDetector.getInstance());
		// UnicodeDetector用于Unicode家族编码的测定
		detector.add(UnicodeDetector.getInstance());

		return detector;
	}

	/**
	 * 根据"encodeType"获取文本编码或文件流编码
	 */
	public String getFileOrIOEncode(String path, String encodeType)
	{
		CodepageDetectorProxy detector = getDetector();
		File file = new File(path);
		Charset charset = null;
		try
		{
			switch (encodeType)
			{
			case FILE_ENCODE_TYPE:
				charset = detector.detectCodepage(file.toURI().toURL());
				break;
			case IO_ENCODE_TYPE:
				charset = detector.detectCodepage(new BufferedInputStream(new FileInputStream(file)), 128);// 128表示读取128字节来判断文件流的编码,读得越多越精确,但是速度慢
				break;
			default:
				charset = Charset.defaultCharset();
				break;
			}

		}
		catch (IOException e)
		{
			//这里获取编码失败,使用系统默认的编码
			charset = Charset.defaultCharset();
			LogWriter.error(getClass(), e.getMessage(), e);
		}
		return charset.name();
	}

	/**
	public static void main(String[] args) throws IOException
	{

		CpdetectorUtils cu = new CpdetectorUtils();
		String path = "D:\\software\\eclipse\\workspace\\test\\src\\com\\chemyoo\\abc\\constant\\TestModelConstant.java";

		File f = new File(path);
		
		try(InputStream in = new FileInputStream(f))
		{
			byte[] b = new byte[(int) f.length()];// 数据存储的数组
			int len = 0;
			int temp = 0;
			while ((temp = in.read()) != -1)
			{
				// 循环读取数据，未到达流的末尾
				b[len] = (byte) temp;// 将有效数据存储在数组中
				len++;
			}
			String aa = new String(b, 0, len, cu.getFileOrIOEncode(path, FILE_ENCODE_TYPE));

			System.out.println(aa);
			System.out.println("文件编码: " + cu.getFileOrIOEncode(path, FILE_ENCODE_TYPE));
			System.out.println("文件流编码: " + cu.getFileOrIOEncode(path, IO_ENCODE_TYPE));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	} 
	*/
}
