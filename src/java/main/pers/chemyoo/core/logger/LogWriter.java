package pers.chemyoo.core.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;
import java.util.Calendar;

public class LogWriter {
	private LogWriter() {};
	
	private static String path = System.getProperty("user.dir");
	
	public static void write(String text) {
		try (FileWriter writer = new FileWriter(path + File.separator + "errorLog.log", true)) {
			ZoneOffset offset = ZoneOffset.ofHours(8);
			writer.write(Calendar.getInstance().getTime().toInstant().atOffset(offset) + " -> ");
			writer.write(text + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void info(String text) {
		write("info:" + text);
	}
	
	public static void error(String text) {
		write("error:" + text);
	}
	
}
