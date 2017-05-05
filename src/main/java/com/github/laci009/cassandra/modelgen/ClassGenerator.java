package com.github.laci009.cassandra.modelgen;

import com.github.laci009.cassandra.modelgen.context.ElementContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;

import static javax.tools.Diagnostic.Kind;

/**
 * Created by laci009 on 2017.05.02.
 */
public class ClassGenerator {

    private static final String TEMPLATE_FILE = "metaclass.vm";

    private final ElementContext elementContext;
    private final ProcessingEnvironment processingEnv;


    public ClassGenerator(ElementContext elementContext, ProcessingEnvironment processingEnv) {
        this.elementContext = elementContext;
        this.processingEnv = processingEnv;
    }

    public void generateMetadataClass() {
        try {
            generateClass();
        } catch (ResourceNotFoundException | ParseErrorException | IOException e) {
            processingEnv.getMessager().printMessage(Kind.ERROR, e.getLocalizedMessage());
        }
    }

    private void generateClass() throws IOException {
        VelocityEngine velocityEngine = initVelocityEngine();
        VelocityContext context = initVelocityContext(elementContext);

        Template template = velocityEngine.getTemplate(TEMPLATE_FILE);
        JavaFileObject javaFileObject = processingEnv.getFiler().createSourceFile(elementContext.getQualifiedName());
        processingEnv.getMessager().printMessage(Kind.NOTE, "Creating source file: " + javaFileObject.toUri());

        Writer writer = javaFileObject.openWriter();
        processingEnv.getMessager().printMessage(Kind.NOTE, "Applying velocity template: " + template.getName());
        template.merge(context, writer);
        writer.close();
    }

    private VelocityEngine initVelocityEngine() throws IOException {
        Properties properties = new Properties();
        URL url = this.getClass().getClassLoader().getResource("velocity.properties");
        properties.load(url.openStream());

        VelocityEngine velocityEngine = new VelocityEngine(properties);
        velocityEngine.init();
        return velocityEngine;
    }

    private VelocityContext initVelocityContext(ElementContext elementContext) {
        VelocityContext context = new VelocityContext();
        context.put("packageName", elementContext.getPackageName());
        context.put("className", elementContext.getClassName());
        context.put("fields", elementContext.getFields());
        context.put("tableName", elementContext.getTableName().orElse(null));
        return context;
    }
}
