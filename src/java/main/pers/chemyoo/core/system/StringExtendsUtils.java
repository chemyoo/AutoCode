package pers.chemyoo.core.system;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class StringExtendsUtils extends StringUtils {

	private StringExtendsUtils() throws NoSuchMethodException {
		throw new NoSuchMethodException("StringExtendsUtils can not Instanse");
	}

	/**
	 * 去掉括号里面的内容，不使用正则，如果要求性能使用此方法
	 * 
	 * @param context
	 * @return
	 */
	public static String clearBracket(String context) {
		if (StringUtils.isEmpty(context)) {
			return context;
		}
		char[] array = context.toCharArray();
		StringBuilder sb = new StringBuilder();
		boolean end = true;
		for (char c : array) {
			if (c == '(' || c == '（') {
				end = false;
			} else if ((c == ')' || c == '）') && !end) {
				end = true;
				continue;
			}
			if (end) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String repalceBracket(String context) {
		if (StringUtils.isEmpty(context)) {
			return context;
		}
		char[] array = context.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char c : array) {
			if (c == '(') {
				sb.append('（');
			} else if (c == ')') {
				sb.append('）');
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	public static String removeSymbol(String context, char... symbols) {
		if (StringUtils.isEmpty(context)) {
			return context;
		}
		char[] array = context.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (char c : array) {
			if (!ArrayUtils.contains(symbols, c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param context
	 * @param symbols
	 *            <p>
	 *            例如： <div>repalceSymbol("abc", "ac") = b;</div>
	 *            <div>repalceSymbol("abc defg", "a cg") = bdef;</div>
	 *            </p>
	 * @return
	 */
	public static String removeSymbol(String context, String symbols) {
		if (StringUtils.isEmpty(context) || StringUtils.isBlank(symbols)) {
			return context;
		}
		char[] symbolsArray = symbols.toCharArray();
		return removeSymbol(context, symbolsArray);
	}

	public static String replace(String context, String orginSymbol, String targetSymbol) {
		if (StringUtils.isEmpty(context) || StringUtils.isBlank(orginSymbol)) {
			return context;
		}
		if (targetSymbol == null) {
			throw new IllegalArgumentException("targetSymbol can not be null.");
		}
		int charlen = orginSymbol.length();
		char[] contextArray = context.toCharArray();
		StringBuilder res = new StringBuilder();
		for (int i = 0, len = contextArray.length; i < len;) {
			StringBuilder sb = new StringBuilder();
			sb.append(contextArray[i]);
			for (int j = i + 1; j < i + charlen && j < len; j++) {
				sb.append(contextArray[j]);
			}
			if (orginSymbol.contentEquals(sb)) {
				res.append(targetSymbol);
				i += charlen;
			} else {
				res.append(contextArray[i]);
				i ++;
			}
		}
		return res.toString();
	}
}
