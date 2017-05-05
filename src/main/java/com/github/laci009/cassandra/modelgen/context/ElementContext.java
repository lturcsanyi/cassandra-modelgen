package com.github.laci009.cassandra.modelgen.context;

import java.util.List;
import java.util.Optional;

/**
 * Created by laci009 on 2017.05.02.
 */
public class ElementContext {

    private final String packageName;
    private final String className;
    private final Optional<String> tableName;
    private final String qualifiedName;
    private final List<Field> fields;

    private ElementContext(String packageName, String className, Optional<String> tableName, String qualifiedName,
            List<Field> fields) {
        this.packageName = packageName;
        this.className = className;
        this.tableName = tableName;
        this.qualifiedName = qualifiedName;
        this.fields = fields;
    }

    public static ElementContext of(String packageName, String className, Optional<String> tableName, String qualifiedName,
            List<Field> fields) {
        return new ElementContext(packageName, className, tableName, qualifiedName, fields);
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public Optional<String> getTableName() {
        return tableName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public List<Field> getFields() {
        return fields;
    }
}
