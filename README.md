# cassandra-modelgen
Generate static metamodel classes from cassandra driver annotations which allows queries  to be constructed in a strongly-typed manner.

## Usage
```xml
<plugin>
  <artifactId>maven-compiler-plugin</artifactId>
  <version>${maven-compiler-plugin.version}</version>
  <configuration>
    <source>1.8</source>
    <target>1.8</target>
    <annotationProcessors>
      <annotationProcessor>com.laci009.cassandra.modelgen.CassandraModelProcessor</annotationProcessor>
    </annotationProcessors>
  </configuration>
</plugin>

```
