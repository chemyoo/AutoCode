package pers.chemyoo.core.system;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import pers.chemyoo.core.logger.LogWriter;

public class InitSystemConfig {
	
	private InitSystemConfig() {
		throw new NoSuchMethodError("InitSystemConfig class can not instant.");
	}
	
	private static VersionMark versionMark = new VersionMark();
	
	private static final String WORK_SPACE = System.getProperty("user.dir");
	private static final File FILE = new File(WORK_SPACE, File.separator + "AutoCodeConfig" + File.separator + "autocode.properties");
	
	static class VersionMark {
		private long version = 0;
	}

	private static Properties props = new Properties();

	public static Properties getInstance() {
		if (props.isEmpty() || isLatest()) {
			init();
		}
		return props;
	}
	
	private static boolean isLatest() {
		if(!FILE.exists()) {
			return true;
		} else {
			return versionMark.version < FILE.lastModified();
		}
	}

	private static void init() {
		try {
			readAsStream();
		} catch (IOException e) {
			LogWriter.error(e.getMessage(), e);
			throw new IllegalAccessError(e.getMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	private static void readAsStream() throws IOException {
		if(!FILE.exists() || !FILE.isFile()) {
			new TipMessage("配置文件：autocode.properties不存在，请将配置文件放到->" + FILE.getParent() 
				+ "文件夹下，现在正在为您生成新的配置文件，请按需更改。");
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("autocode.properties");
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			IOUtils.copy(in, byteArray);
			FileUtils.writeByteArrayToFile(FILE, byteArray.toByteArray());
			IOUtils.closeQuietly(in);
		}
		versionMark.version = FILE.lastModified();
		try (InputStream in = FileUtils.openInputStream(FILE);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in , "UTF-8"));) {
			props.load(bufferedReader);
		} catch (Exception e) {
			LogWriter.error(e.getMessage(), e);
			throw new IllegalAccessError(e.getMessage());
		}
	}
	
	/**public static String readMapperTemplate() {
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
	}*/
	
	public static void main(String[] args) {
//		System.err.println(InitSystemConfig.readMapperTemplate().replaceAll("#\\{[a-zA-Z]+?\\}", "HUB"));
//		System.err.println(InitSystemConfig.readServiceTemplate().replaceAll("#\\{[a-zA-Z]+?\\}", "HUB"));
		System.err.println(System.getProperty("file.encoding"));
		System.err.println(Charset.defaultCharset().displayName());
	}

}
