package pers.chemyoo.core.processor;

import java.io.Writer;
import java.util.List;
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
import javax.tools.JavaFileObject;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;

import pers.chemyoo.core.annotations.AutoConstant;
import pers.chemyoo.core.logger.LogWriter;
import pers.chemyoo.core.system.ConstantGenerator;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({ "pers.chemyoo.core.annotations.AutoConstant" })
public class ConstantProcessor extends AbstractProcessor {

	@Override
	@SuppressWarnings("deprecation")
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (Element element : roundEnv.getElementsAnnotatedWith(AutoConstant.class)) {
			if (element.getKind() == ElementKind.CLASS) {
				List<Element> list = Lists.newArrayList();
				for (Element enclosedElement : element.getEnclosedElements()) {
					this.addField(list, enclosedElement);
				}
				Writer write = null;
				try {
					String name = element.getSimpleName().toString();
					ConstantGenerator generator = new ConstantGenerator(name);
					String clazzString = generator.buildClassString(list);
					JavaFileObject source = processingEnv.getFiler().createSourceFile(generator.getFullClassName());
					write = source.openWriter();
					write.write(new String(clazzString.getBytes("utf-8")));
					write.close();
				} catch (Exception e) {
					LogWriter.error(e.getMessage(), e);
				} finally {
					IOUtils.closeQuietly(write);
				}
			}
		}
		return false;
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
