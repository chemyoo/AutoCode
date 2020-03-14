package pers.chemyoo.core.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
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
import javax.tools.JavaFileObject;

import org.apache.commons.io.IOUtils;

import pers.chemyoo.core.annotations.AutoService;
import pers.chemyoo.core.logger.LogWriter;
import pers.chemyoo.core.system.InitSystemConfig;
import pers.chemyoo.core.system.ServiceGenerator;

// 通过注解生成文件
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"pers.chemyoo.core.annotations.AutoService"})
public class ServiceProcessor extends AbstractProcessor {
	
	private Properties props = InitSystemConfig.getInstance();
	
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
				this.transEcoding(out, generator.buildClass());
			} catch (Exception e) {
				LogWriter.error(e.getMessage());
			} finally {
				IOUtils.closeQuietly(out);
			}
        }
		return false;
	}
	
	private void transEcoding(OutputStream out, String text) throws IOException {
		Charset defaultCharset = Charset.defaultCharset();
		String usedEcoding = props.getProperty("used.file.encoding", defaultCharset.displayName());
		String charset = "UTF-8";
		if("GBK,GB2312".contains(usedEcoding.toUpperCase())) {
			out.write(new String(text.getBytes(charset), "GBK").getBytes());
		} else {
			out.write(text.getBytes(charset));
		}
	}

}
