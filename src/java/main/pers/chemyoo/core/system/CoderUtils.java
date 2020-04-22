package pers.chemyoo.core.system;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class CoderUtils
{
	/**
	 * 获取字符串的unicode编码 汉字“木”的Unicode 码点为Ox6728
	 * 
	 * @param s 木
	 * @return \ufeff\u6728 \ufeff控制字符 用来表示「字节次序标记（Byte Order Mark）」不占用宽度 在java中一个char是采用unicode存储的 占用2个字节 比如 汉字木 就是 Ox6728 4bit+4bit+4bit+4bit=2字节
	 */
	public static String stringToUnicode(String s)
	{
		try
		{
			StringBuffer out = new StringBuffer("");
			// 直接获取字符串的unicode二进制
			byte[] bytes = s.getBytes("unicode");
			// 然后将其byte转换成对应的16进制表示即可
			for (int i = 0; i < bytes.length - 1; i += 2)
			{
				out.append("\\u");
				String str = Integer.toHexString(bytes[i + 1] & 0xff);
				for (int j = str.length(); j < 2; j++)
				{
					out.append("0");
				}
				String str1 = Integer.toHexString(bytes[i] & 0xff);
				out.append(str1);
				out.append(str);
			}
			return out.toString();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Unicode转 汉字字符串
	 * 
	 * @param str \u6728
	 * @return '木' 26408
	 */
	public static String unicodeToString(String str)
	{

		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find())
		{
			// group 6728
			String group = matcher.group(2);
			// ch:'木' 26408
			ch = (char) Integer.parseInt(group, 16);
			// group1 \u6728
			String group1 = matcher.group(1);
			str = str.replace(group1, ch + "");
		}
		return str;
	}

	/**
	 * 汉字 转换为对应的 UTF-8编码
	 * 
	 * @param s 木
	 * @return E69CA8
	 */
	public static String convertStringToUTF8(String s)
	{
		if (s == null || s.equals(""))
		{
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try
		{
			char c;
			for (int i = 0; i < s.length(); i++)
			{
				c = s.charAt(i);
				if (c >= 0 && c <= 255)
				{
					sb.append(c);
				}
				else
				{
					byte[] b;
					b = Character.toString(c).getBytes("utf-8");
					for (int j = 0; j < b.length; j++)
					{
						int k = b[j];
						// 转换为unsigned integer 无符号integer
						/*
						 * if (k < 0) k += 256;
						 */
						k = k < 0 ? k + 256 : k;
						// 返回整数参数的字符串表示形式 作为十六进制（base16）中的无符号整数
						// 该值以十六进制（base16）转换为ASCII数字的字符串
						sb.append(Integer.toHexString(k).toUpperCase());

						// url转置形式
						// sb.append("%" +Integer.toHexString(k).toUpperCase());
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * UTF-8编码 转换为对应的 汉字
	 * 
	 * @param s E69CA8
	 * @return 木
	 */
	public static String convertUTF8ToString(String s)
	{
		if (s == null || s.equals(""))
		{
			return null;
		}
		try
		{
			s = s.toUpperCase();
			int total = s.length() / 2;
			// 标识字节长度
			int pos = 0;
			byte[] buffer = new byte[total];
			for (int i = 0; i < total; i++)
			{
				int start = i * 2;
				// 将字符串参数解析为第二个参数指定的基数中的有符号整数。
				buffer[i] = (byte) Integer.parseInt(s.substring(start, start + 2), 16);
				pos++;
			}
			// 通过使用指定的字符集解码指定的字节子阵列来构造一个新的字符串。
			// 新字符串的长度是字符集的函数，因此可能不等于子数组的长度。
			return new String(buffer, 0, pos, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return s;
	}

	public static String unicodeToUtf8(String theString)
	{
		if (StringUtils.isNotBlank(theString))
		{
			StringBuilder outBuffer = new StringBuilder();
			String[] unicodes = theString.split("\\\\");
			for (String unicode : unicodes)
			{
				int value = 0;
				if (unicode.length() != 0)
				{
					char first = unicode.charAt(0);
					if (first == 'u')
					{
						for (int i = 1, len = unicode.length(); i < len; i++)
						{
							char c = unicode.charAt(i);
							switch (c)
							{
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								value = (value << 4) + c - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								value = (value << 4) + 10 + c - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								value = (value << 4) + 10 + c - 'A';
								break;
							default:
								throw new IllegalArgumentException("Malformed   \\uxxxx   encoding.");
							}
						}
						outBuffer.append((char) value);
					}
					else {
						if (first == 't')
						{
							first = '\t';
						}
						else if (first == 'r')
						{
							first = '\r';
						}
						else if (first == 'n')
						{
							first = '\n';
						}
						else if (first == 'f')
						{
							first = '\f';
						}
						outBuffer.append(first);
					}
				}
			}
			return outBuffer.toString();
		}
		return StringUtils.EMPTY;
	}

	public static void main(String[] args)
	{
		String s = "abc单反发达既非中中中";
		String unicode = CoderUtils.stringToUnicode(s);
		String utf = CoderUtils.convertStringToUTF8(s);
		String str = CoderUtils.convertUTF8ToString(utf);
		System.err.println(unicode);
		System.err.println(utf);
		System.err.println(str);
		System.err.println(CoderUtils.unicodeToUtf8(unicode));
	}

}