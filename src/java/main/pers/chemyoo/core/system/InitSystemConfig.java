package pers.chemyoo.core.system;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

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
		try {
			readAsStream();
		} catch (IOException e) {
			throw new IllegalAccessError(e.getMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void readAsStream() throws IOException {
		String workSpace = System.getProperty("user.dir");
		File file = new File(workSpace, File.separator + "AutoCodeConfig" + File.separator + "autocode.properties");
		if(!file.exists() || !file.isFile()) {
			new TipMessage(file.getAbsolutePath() + "文件不存在，正在将AutoCode.jar中的/resources/autocode.properties拷贝到"
					+ file.getParent() + "文件夹下。");
			boolean success = file.createNewFile();
			if(success) {
				InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("autocode.properties");
				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
				IOUtils.copy(in, byteArray);
				FileUtils.writeByteArrayToFile(file, byteArray.toByteArray());
				IOUtils.closeQuietly(in);
			}
		}
		try (InputStream in = FileUtils.openInputStream(file);
			 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in , "UTF-8"));) {
			props.load(bufferedReader);
		} catch (Exception e) {
			LogWriter.error(e.getMessage());
			throw new IllegalAccessError(e.getMessage());
		}
	}
	
	public static String readMapperTemplate() {
		return readTemplate("template/mapper.tpl");
	}
	
	public static String readServiceTemplate() {
		return readTemplate("template/service.tpl");
	}
	
	public static String readTemplate(String name) {
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
