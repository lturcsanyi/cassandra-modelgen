package com.github.laci009.cassandra.modelgen.context;

/**
 * Created by laci009 on 2017.05.02.
 */
public class Field {

	private final String name;
	private final String value;

	private Field(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public static Field of(String name, String value) {
		return new Field(name, value);
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Field field = (Field) o;

		if (name != null ? !name.equals(field.name) : field.name != null) return false;
		if (value != null ? !value.equals(field.value) : field.value != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Field{");
		sb.append("name='").append(name).append('\'');
		sb.append(", value='").append(value).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
