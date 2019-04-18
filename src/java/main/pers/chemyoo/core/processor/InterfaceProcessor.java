package pers.chemyoo.core.processor;

import java.io.File;
import java.io.IOException;
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

import pers.chemyoo.core.annotations.AutoInterface;

// 通过注解生成文件
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"com.chemyoo.core.annotations.AutoInterface"})
public class InterfaceProcessor extends AbstractProcessor {
	
	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if(!roundEnv.processingOver()){
            for(Element element : roundEnv.getElementsAnnotatedWith(AutoInterface.class)){
            	String path = element.getClass().getClassLoader().getResource("").getPath();
                String name = element.getSimpleName().toString();
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "element name: " + name);
                try {
    				File file = new File(path + "/", name + "Sevrice.java");
    				file.createNewFile();
    			} catch (IOException e) {
    				// ignore
    			}
            }
        }
		return false;
	}

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		System.err.println("------------------------------");
	}

}
