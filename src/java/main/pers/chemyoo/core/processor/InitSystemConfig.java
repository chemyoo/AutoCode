package pers.chemyoo.core.processor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;

import pers.chemyoo.core.logger.LogWriter;

public class InitSystemConfig {
	
	private InitSystemConfig() {};

	private static Properties props = new Properties();

	public static Properties getInstance() {
		if (props.isEmpty()) {
			init();
		}
		return props;
	}

	private static void init() {
		readAsStream();
		for(Object key : props.keySet()) {
			LogWriter.info((String)key + "=" + props.getProperty((String)key));
		}
	}
	
	private static void readAsStream() {
		LogWriter.info("getResourceAsStream...");
		InputStream in = InitSystemConfig.class.getClassLoader().getResourceAsStream("config.properties");
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));) {
			props.load(bufferedReader);
		} catch (Exception e) {
			e.printStackTrace();
			LogWriter.error(e.getMessage());
		}
	}
	
	public static String readMapperTemplateAsStream() {
		return readTemplateAsStream("template/mapper.tpl");
	}
	
	public static String readServiceTemplateAsStream() {
		return readTemplateAsStream("template/service.tpl");
	}
	
	public static String readTemplateAsStream(String name) {
		LogWriter.info("getTemplateAsStream...");
		StringBuilder builder = new StringBuilder();
		InputStream in = InitSystemConfig.class.getClassLoader().getResourceAsStream(name);
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));) {
			if(bufferedReader.ready()) {
				Iterator<String> it = bufferedReader.lines().iterator();
				while(it.hasNext()) {
					builder.append(it.next());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogWriter.error(e.getMessage());
		}
		return builder.toString();
	}

}
