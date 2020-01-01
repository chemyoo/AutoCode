package pers.chemyoo.core.system;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	}
	
	private static void readAsStream() {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("autocode.properties");
		LogWriter.info(System.getProperty("user.dir"));
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));) {
			props.load(bufferedReader);
		} catch (Exception e) {
			e.printStackTrace();
			LogWriter.error(e.getMessage());
		}
	}
	
	public static String readMapperTemplate() {
		return readTemplate("template/mapper.tpl");
	}
	
	public static String readServiceTemplate() {
		return readTemplate("template/service.tpl");
	}
	
	public static String readTemplate(String name) {
		LogWriter.info("getTemplateAsStream...");
		StringBuilder builder = new StringBuilder();
		InputStream in = InitSystemConfig.class.getClassLoader().getResourceAsStream(name);
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));) {
			if(bufferedReader.ready()) {
				String line = null;
				while((line = bufferedReader.readLine()) != null) {
					builder.append(line).append("\r\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogWriter.error(e.getMessage());
		}
		LogWriter.info(builder.toString());
		return builder.toString();
	}
	
	public static void main(String[] args) {
		System.err.println(InitSystemConfig.readMapperTemplate().replaceAll("#\\{[a-zA-Z]+?\\}", "HUB"));
		System.err.println(InitSystemConfig.readServiceTemplate().replaceAll("#\\{[a-zA-Z]+?\\}", "HUB"));
	}

}
