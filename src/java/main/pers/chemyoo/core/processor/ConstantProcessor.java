package pers.chemyoo.core.processor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

import pers.chemyoo.core.annotations.AutoConstant;
import pers.chemyoo.core.logger.LogWriter;
import pers.chemyoo.core.system.ConstantGenerator;
import pers.chemyoo.core.system.InitSystemConfig;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({ "pers.chemyoo.core.annotations.AutoConstant" })
public class ConstantProcessor extends AbstractProcessor {
	
	private Properties props = InitSystemConfig.getInstance();

	@Override
	@SuppressWarnings("deprecation")
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getElementsAnnotatedWith(AutoConstant.class)) {
			if (element.getKind() == ElementKind.CLASS) {
				List<Element> list = Lists.newArrayList();
				for (Element enclosedElement : element.getEnclosedElements()) {
					this.addField(list, enclosedElement);
				}
				OutputStream out = null;
				try {
					String name = element.getSimpleName().toString();
					ConstantGenerator generator = new ConstantGenerator(name);
					String clazzString = generator.buildClassString(list);
					JavaFileObject source = processingEnv.getFiler().createSourceFile(generator.getFullClassName());
					out = source.openOutputStream();
					this.transEcoding(out, clazzString);
					processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("当前系统编码：%s", Charset.defaultCharset().displayName()));
					processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("%s file was created.", generator.getClassName()));
				} catch (Exception e) {
					LogWriter.error(e.getMessage(), e);
				} finally {
					IOUtils.closeQuietly(out);
				}
			}
		}
		return false;
	}
	
	private void transEcoding(OutputStream out, String text) throws IOException {
		Charset defaultCharset = Charset.defaultCharset();
		String usedEcoding = props.getProperty("used.file.encoding", defaultCharset.displayName());
		/**
		String transEcode = props.getProperty("trans.file.encoding", defaultCharset.displayName());
		LogWriter.info(ConstantProcessor.class, "defaultCharset: %s.", defaultCharset.displayName());
		LogWriter.info(ConstantProcessor.class, "current used charset: %s.", usedEcode);
		LogWriter.info(ConstantProcessor.class, "trans to charset: %s.", transEcode);
		LogWriter.info("used charset:" + usedEcode + ", transEcode:" + transEcode);
		*/
		
		// 读取字符串编码，此处使用UTF-8编码，所以返回一定是UTF-8
//		InputStream input = new ByteArrayInputStream(encodingByte);
//		String charset = CpdetectorUtils.getIOEncode(input);
		String charset = "UTF-8";
		// 编译项目使用的编码是否是GBK,GB2312
		if("GBK,GB2312".contains(usedEcoding.toUpperCase())) {
			out.write(new String(text.getBytes(charset), "GBK").getBytes());
		} else {
			out.write(text.getBytes(charset));
		}
	}
	
	private void addField(List<Element> list, Element enclosedElement) {
		if (enclosedElement.getKind() == ElementKind.FIELD) {
			Set<Modifier> modifiers = enclosedElement.getModifiers();
			if (!modifiers.contains(Modifier.STATIC) && !modifiers.contains(Modifier.FINAL)) {
				list.add(enclosedElement);
			}
		}
	}

	// -------------------------------------------NOTE------------------------------------------------------------
	/** if (element.getKind() == ElementKind.CLASS) 
	 for (Element enclosedElement : element.getEnclosedElements()) {
	 if (enclosedElement.getKind() == ElementKind.FIELD) 
	 Set<Modifier> modifiers = enclosedElement.getModifiers();
	 StringBuilder sb = new StringBuilder();
	 if (modifiers.contains(Modifier.PRIVATE)) 
	 sb.append("private ");
	 } else if (modifiers.contains(Modifier.PROTECTED)) 
	 sb.append("protected ");
	 } else if (modifiers.contains(Modifier.PUBLIC)) 
	 sb.append("public ");
	 }
	 if (modifiers.contains(Modifier.STATIC))
	 sb.append("static ");
	 if (modifiers.contains(Modifier.FINAL))
	 sb.append("final ");
	 sb.append(enclosedElement.asType()).append("").append(enclosedElement.getSimpleName());
	 }
	 }
	 }*/

}
