package pers.chemyoo.core.processor;

import java.io.OutputStream;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.apache.commons.io.IOUtils;

import pers.chemyoo.core.annotations.AutoService;
import pers.chemyoo.core.logger.LogWriter;
import pers.chemyoo.core.system.EncodingUtils;
import pers.chemyoo.core.system.ServiceGenerator;

// 通过注解生成文件
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"pers.chemyoo.core.annotations.AutoService"})
public class ServiceProcessor extends AbstractProcessor {
	
	@Override
	@SuppressWarnings("deprecation")
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for(Element element : roundEnv.getElementsAnnotatedWith(AutoService.class)) {
			String name = element.getSimpleName().toString();
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "element name: " + name);
            OutputStream out = null;
            try {
            	ServiceGenerator generator = new ServiceGenerator(name);
				JavaFileObject source = processingEnv.getFiler().createSourceFile(generator.getFullClassName());
				out = source.openOutputStream();
				EncodingUtils.transEcoding(out, generator.buildClass());
			} catch (Exception e) {
				LogWriter.error(e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(out);
			}
        }
		return false;
	}

}
