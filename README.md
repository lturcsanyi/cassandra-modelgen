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
      <annotationProcessor>com.github.laci009.cassandra.modelgen.CassandraModelProcessor</annotationProcessor>
    </annotationProcessors>
  </configuration>
</plugin>
...
<dependency>
  <groupId>com.github.laci009</groupId>
  <artifactId>cassandra-modelgen</artifactId>
  <version>1.0.0</version>
</dependency>

```

After compile you can use generated metamodel classes with _ postfix to create queries.
In @Accessor annotated class:
```java
    @Query("SELECT * FROM " + UserData_._table + " " +
    "WHERE " + UserDat_.userId + " = :userId")
    Result<UserData> findUserById(String userId);
```
