package com.laci009.cassandra.modelgen;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.Table;
import com.laci009.cassandra.modelgen.context.ElementContext;
import com.laci009.cassandra.modelgen.context.Field;
import org.apache.commons.lang.StringUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static javax.tools.Diagnostic.Kind;

/**
 * Created by laci009 on 2017.05.02.
 */
@SupportedAnnotationTypes({
        "com.datastax.driver.mapping.annotations.Table"
})
@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class CassandraModelProcessor extends AbstractProcessor {

    private static final String META_POSTFIX = "_";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        for (TypeElement annotation : annotations) {
            processAnnotation(roundEnvironment, annotation);
        }

        return true;
    }

    private void processAnnotation(RoundEnvironment roundEnv, TypeElement annotation) {
        processingEnv.getMessager().printMessage(Kind.NOTE, "Generate classes annotated with " + annotation.toString());

        final Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
        for (Element element : annotatedElements) {
            processAnnotatedElement(element);
        }
    }

    private void processAnnotatedElement(Element annotatedElement) {
        processingEnv.getMessager().printMessage(Kind.NOTE, "Element found: " + annotatedElement.toString());

        try {
            processElement(annotatedElement);
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Kind.ERROR,
                    "Error while generating metadata: " + e.getMessage(), annotatedElement);
        }
    }

    private void processElement(Element element) throws IOException {
        if (!isClass(element)) {
            return;
        }

        TypeElement classElement = (TypeElement) element;
        processingEnv.getMessager().printMessage(Kind.NOTE, "Annotated class: " + classElement.getQualifiedName(), classElement);

        final ElementContext elementContext = initElementContext(classElement);

        ClassGenerator classGenerator = new ClassGenerator(elementContext, processingEnv);
        classGenerator.generateMetadataClass();
    }

    private boolean isClass(Element element) {
        return ElementKind.CLASS.equals(element.getKind());
    }

    private boolean isField(Element classMember) {
        return ElementKind.FIELD.equals(classMember.getKind());
    }

    private ElementContext initElementContext(TypeElement classElement) {
        PackageElement packageElement = (PackageElement) classElement.getEnclosingElement();
        final String packageName = packageElement.getQualifiedName().toString();
        final String className = classElement.getSimpleName().toString();
        final String qualifiedName = classElement.getQualifiedName().toString() + META_POSTFIX;
        final Optional<String> tableName = getTableName(classElement, className);
        final List<Field> fields = getFields(classElement);
        return ElementContext.of(packageName, className, tableName, qualifiedName, fields);
    }

    private List<Field> getFields(TypeElement classElement) {
        List<Field> fields = new ArrayList<>();
        for (Element classMember : classElement.getEnclosedElements()) {
            if (isField(classMember)) {
                addField(fields, (VariableElement) classMember);
            }
        }

        return fields;
    }

    private void addField(List<Field> fields, VariableElement fieldElement) {
        if (fieldElement.getAnnotation(Column.class) != null) {
            fields.add(addColumnField(fieldElement));
        }
    }

    private Field addColumnField(VariableElement fieldElement) {
        String fieldName = fieldElement.getSimpleName().toString();
        Column columnAnnotation = fieldElement.getAnnotation(Column.class);
        String fieldValue = fieldName;
        if (StringUtils.isNotBlank(columnAnnotation.name())) {
            fieldValue = columnAnnotation.name();
        }

        return Field.of(fieldName, fieldValue);
    }

    private Optional<String> getTableName(TypeElement classElement, String className) {
        Optional<String> tableName = Optional.empty();
        if (classElement.getAnnotation(Table.class) != null) {
            String annotationValue = classElement.getAnnotation(Table.class).name();
            if (StringUtils.isNotBlank(annotationValue)) {
                tableName = Optional.of(annotationValue);
            } else {
                tableName = Optional.of(className);
            }
        }
        return tableName;
    }

}
