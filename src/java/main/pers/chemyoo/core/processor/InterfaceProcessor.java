package pers.chemyoo.core.processor;

import java.io.Writer;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import pers.chemyoo.core.annotations.AutoInterface;
import pers.chemyoo.core.logger.LogWriter;

// 通过注解生成文件
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"pers.chemyoo.core.annotations.AutoInterface"})
public class InterfaceProcessor extends AbstractProcessor {
	
private Properties props = InitSystemConfig.getInstance();
	
	private static final String SUBFIX = "Service";

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for(Element element : roundEnv.getElementsAnnotatedWith(AutoInterface.class)) {
			String name = element.getSimpleName().toString();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "element name: " + name);
            try {
            	String serviceName = name + SUBFIX;
				JavaFileObject source = processingEnv.getFiler().createSourceFile(props.getProperty("service.path")+ "." + serviceName);
            	Writer write = source.openWriter();
            	write.write(this.classbuilder(name));
				write.close();
			} catch (Exception e) {
				LogWriter.error(e.getMessage());
			}
        }
		return false;
	}
	
	private String classbuilder(String name) {
		return InitSystemConfig.readServiceTemplate().replaceAll("#\\{[a-zA-Z]+?\\}", name);
	}

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		System.err.println("------------------------------");
	}

}
