package pers.chemyoo.core.system;

import java.util.List;
import java.util.Properties;

import javax.lang.model.element.Element;

import org.apache.commons.lang3.StringUtils;

import pers.chemyoo.core.annotations.Comment;

/**
 * @author Administrator
 */
public class ConstantGenerator {

	private Properties props = InitSystemConfig.getInstance();

	private String packageName;

	private String className;

	private String fullClassName;
	
	private final StringBuilder builder = new StringBuilder();

	/**
	 * class name
	 * @param className
	 */
	public ConstantGenerator(String className) {
		String configPackage = props.getProperty("constant.classpath");
		if (StringUtils.isBlank(configPackage)) {
			throw new IllegalArgumentException("constant.classpath not found in properties.");
		}
		this.className = className + "Constant";
		this.packageName = configPackage;
		this.fullClassName = configPackage + "." + this.className;
	}

	public String buildClassString(List<Element> list) {
		if (list == null) {
			throw new IllegalArgumentException("fields can not be null.");
		}
		this.buildPackage();
		this.buildClassBody(list);
		return builder.toString();
	}
	
	private void buildPackage() {
		builder.append("package").append(AutoCoreConstant.BLANK).append(this.packageName)
			   .append(AutoCoreConstant.SEMICOLON);
		this.twoEmptyLine();
	}
	
	private void buildClassBody(List<Element> list) {
		builder.append("/** \r\n * Constant Class auto generate by AutoConstant Annotation \r\n * @author jianqing.liu \r\n */");
		builder.append(AutoCoreConstant.CRLF).append("public final class ").append(this.className)
			   .append(AutoCoreConstant.BLANK).append("{");
		this.twoEmptyLine();
		this.buildConstructor();
		for (Element field : list) {
			builder.append(AutoCoreConstant.CRLF);
			String comment = "";
			Comment value = field.getAnnotation(Comment.class);
			if (value != null) {
				comment += "\t/** " + value.value() + " */";
			}
			if(StringUtils.isNotBlank(comment)) {
				builder.append(comment).append(AutoCoreConstant.CRLF);
			}
			String fieldName = this.getConstantFieldName(field.getSimpleName().toString());
			builder.append("\tpublic static final String ").append(fieldName);
			builder.append(" = \"").append(field.getSimpleName()).append('"');
			builder.append(AutoCoreConstant.SEMICOLON);
			builder.append(AutoCoreConstant.CRLF);
		}
		builder.append(AutoCoreConstant.CRLF);
		builder.append('}');
	}
	
	private void buildConstructor() {
		builder.append("\tprivate ").append(this.className).append(" () {").append(AutoCoreConstant.CRLF);
		builder.append("\t\tthrow new NoSuchMethodError(\"");
		builder.append(this.className).append(" class can not instant.\");");
		builder.append(AutoCoreConstant.CRLF).append("\t}");
		builder.append(AutoCoreConstant.CRLF);
	}
	
	private StringBuilder twoEmptyLine() {
		return builder.append(AutoCoreConstant.CRLF).append(AutoCoreConstant.CRLF);
	}
	
	public String getConstantFieldName(String fieldName) {
		char[] array = fieldName.toCharArray();
		StringBuilder charBuilder = new StringBuilder();
		for(char c : array) {
			if(c > 'A' && c < 'Z') {
				charBuilder.append("_").append(c);
			} else {
				charBuilder.append(c);
			}
		}
		return charBuilder.toString().toUpperCase();
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
