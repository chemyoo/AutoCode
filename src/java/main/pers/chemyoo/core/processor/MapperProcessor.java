package pers.chemyoo.core.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import pers.chemyoo.core.annotations.AutoMapper;
import pers.chemyoo.core.logger.LogWriter;
import pers.chemyoo.core.system.InitSystemConfig;

// 通过注解生成文件
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"pers.chemyoo.core.annotations.AutoMapper"})
public class MapperProcessor extends AbstractProcessor {
	
	private static final String LINE = "\r\n";
	
	private static final String SUBFIX = "Mapper";

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for(Element element : roundEnv.getElementsAnnotatedWith(AutoMapper.class)) {
			String name = element.getSimpleName().toString();
			String fullName = element.toString();
			LogWriter.error("name:" + name);
			LogWriter.error("fullname:" + fullName);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "element name: " + name);
//            try {
//            	String mapperName = name + SUBFIX;
//            	FileObject fileObject = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, props.getProperty("mapper.path"), mapperName)
//				JavaFileObject source = processingEnv.getFiler().createSourceFile(getParentPackage(fullName) + mapperName);
//            	Writer write = source.openWriter();
//            	write.write(this.classbuilder(name));
//				write.close();
//			} catch (Exception e) {
//				LogWriter.error(e.getMessage());
//			}
        }
		return false;
	}
	
	private String getParentPackage(String fullName) {
		StringBuilder builder = new StringBuilder();
		if(fullName != null && !fullName.isEmpty()) {
			String[] array = fullName.split("\\.");
			for(int i = 0, length = array.length - 2; i < length; i ++) {
				builder.append(array[i]).append(".");
			}
		}
		builder.append(SUBFIX.toLowerCase()).append(".");
		return builder.toString();
	}
	
	private String classbuilder(String name) {
		return InitSystemConfig.readMapperTemplate().replaceAll("#\\{[a-zA-Z]+?\\}", name);
	}
	
	public StringBuilder classbuilder(String className, String beanName, String name) {
		Properties props = InitSystemConfig.getInstance();
		StringBuilder builder = new StringBuilder();
		String pkg = props.getProperty("mapper.path");
		if (pkg == null) {
			throw new RuntimeException("can not find property 'mapper.path' in file which name is config.properties");
		}
		builder.append("package ").append(pkg).append(";").append(LINE).append(LINE);
		List<String> imports = new ArrayList<>();
		String superClass = props.getProperty("mapper.super.class"); 
		if (superClass == null) {
			throw new RuntimeException("can not find property 'mapper.super.class' in file which name is config.properties");
		}
		int indexof = superClass.lastIndexOf('.');
		imports.add(superClass);
		imports.add(className);
		for(String im : imports) {
			builder.append("import ").append(im).append(";").append(LINE);
		}
		String gentherClass = props.getProperty("mapper.genther.class");
		builder.append(LINE);
		builder.append("public interface ").append(beanName).append(" extends ");
		builder.append(superClass.substring(indexof + 1));
		if(gentherClass != null && !gentherClass.isEmpty()) {
			builder.append(gentherClass.replaceAll("^<[A-Z]>$", "<" + name + ">"));
		}
		builder.append(" {").append(LINE);
		builder.append("	").append(LINE).append("}");
		return builder;
	}
	
	public static void main(String[] args) {
		new MapperProcessor().getParentPackage("com.chemyoo.core.ACC");
	}

}
