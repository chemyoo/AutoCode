package pers.chemyoo.core.system;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class ServiceGenerator {
	
	private Properties props = InitSystemConfig.getInstance();
	private static final String SUBFIX = "Service";

	private String packageName;

	private String className;

	private String fullClassName;
	
	/**
	 * class name
	 * @param className
	 * @throws IOException 
	 */
	public ServiceGenerator(String className) {
		String basePackage = props.getProperty("base.package");
		if (StringUtils.isBlank(basePackage)) {
			throw new IllegalArgumentException("base.package not found in properties.");
		}
		this.className = className + SUBFIX;
		this.packageName = basePackage + ".services";
		this.fullClassName = this.packageName + "." + this.className;
	}
	
	public String buildClass() throws IOException {
		return FileReader.read("service.tpl").replace("#{package}", this.packageName).replace("#{className}", this.fullClassName).replace("#{T}", this.className);
	}

	public String getFullClassName() {
		return fullClassName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getClassName() {
		return className;
	}
}
