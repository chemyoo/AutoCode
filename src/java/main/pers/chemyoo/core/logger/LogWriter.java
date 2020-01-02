package pers.chemyoo.core.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Calendar;

public class LogWriter {
	private LogWriter() {};
	
	private static String path = System.getProperty("user.dir");
	
	public static void write(String text) {
		LogWriter.writeline(text, true);
	}
	
	public static void writeline(String text, boolean newLine) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String date = format.format(calendar.getTime());
		try (FileWriter writer = new FileWriter(path + File.separator + date + "errorLog.log", true)) {
			ZoneOffset offset = ZoneOffset.ofHours(8);
			writer.write(calendar.getTime().toInstant().atOffset(offset) + " -> ");
			if(newLine) {
				writer.write(text + "\r\n");
			} else {
				writer.write(text);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println(e);
		}
	}
	
	public static void info(String text) {
		LogWriter.write("info: " + text);
	}
	
	public static void info(Class<?> clazz, String formatStr, Object... args) {
		LogWriter.writeline(clazz.getName() + " -> info: ", false);
		if(args != null && args.length > 0) {
			LogWriter.info(String.format(formatStr, args));
		} else {
			LogWriter.info(formatStr);
		}
	}
	
	public static void error(String text) {
		LogWriter.write("error: " + text);
	}
	
	public static void error(Class<?> clazz, String formatStr, Throwable t, Object... args) {
		LogWriter.writeline(clazz.getName() + " -> detail: ", false);
		LogWriter.error(formatStr, t, args);
	}
	
	public static void error(String formatStr, Throwable t, Object... args) {
		if(args != null && args.length > 0) {
			LogWriter.error(String.format(formatStr, args));
		} else {
			LogWriter.error(formatStr);
		}
		if(t != null)
		{
			for(StackTraceElement ele : t.getStackTrace()) {
				LogWriter.write(ele.getFileName() + "." + ele.getMethodName() + "--->" +ele.toString());
			}
		}
	}
	
}
