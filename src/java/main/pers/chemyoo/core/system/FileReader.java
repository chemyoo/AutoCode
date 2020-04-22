package pers.chemyoo.core.system;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.Maps;

public final class FileReader {
	
	private FileReader() {
		throw new AbstractMethodError("FileReader can not instance.");
	}
	
	private static final Map<String, String> MAP_CACHE = Maps.newHashMap();
	private static final Map<String, Long> VERSION_CACHE = Maps.newHashMap();
	
	private static final File TEMPLATES_FOLDER = new File(InitSystemConfig.WORK_SPACE, File.separator + "templates" + File.separator);
	
	private static void createLogFolder(File folder) {
		if(!folder.exists()) {
			folder.mkdirs();
		}
	}
	// TODO 没有从jar中找到模板文件
	public static String read(String fileName) throws IOException {
		Validate.notBlank(fileName, "fileName不能为空");
		File file = new File(TEMPLATES_FOLDER, fileName);
		if(!file.exists()) {
			createLogFolder(TEMPLATES_FOLDER);
			// 文件不存在，就从jar包中拷贝出去
//			TipMessage.showMsg(Thread.currentThread().getContextClassLoader());
			InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(File.separator + "templates" + File.separator + fileName);
			if(input == null) {
				input = Thread.currentThread().getContextClassLoader().getResourceAsStream("templates" + File.separator + fileName);
			}
			Validate.notNull(input, "文件名【%s】的文件不存在", fileName);
			try(OutputStream output = new FileOutputStream(file)){
				IOUtils.copy(input, output);
			} 
		}
		long version = file.lastModified();
		String key = DigestUtils.md5Hex(fileName);
		if(MAP_CACHE.containsKey(key) && VERSION_CACHE.get(key) == version) {
			return MAP_CACHE.get(fileName);
		}
		String encoding = InitSystemConfig.getInstance().getProperty("used.file.encoding", Charset.defaultCharset().displayName());
		MAP_CACHE.put(key, FileUtils.readFileToString(file, encoding));
		VERSION_CACHE.put(key, version);
		return MAP_CACHE.get(key);
	}
	
}
