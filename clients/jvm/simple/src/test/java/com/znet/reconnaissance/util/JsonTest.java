package com.znet.reconnaissance.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

public class JsonTest {

	@Test
	public void testWrite() {
		String json = Json.write(TestObject.create());
		System.out.println("JSON: " + json);
		Assert.assertNotNull(json);
	}
	
	@Test
	public void testReadWrite() {
		SimpleObject source = SimpleObject.create();
		String json = Json.write(source);
		SimpleObject dest = Json.read(json, SimpleObject.class);
		Assert.assertEquals(source, dest);
	}
	

	@SuppressWarnings("boxing")
	protected static class SimpleObject {
		private String name;
		private Integer integerValue;
		private Double doubleValue;
		private Boolean booleanValue;
		private String[] emptyArray;
		private String[] stringArray;
		private SubObject[] objectArray;
		private SubObject subObject;
		
		public static SimpleObject create() {
			SimpleObject object = new SimpleObject();
			object.name = "test";
			object.integerValue = 59823532;
			object.doubleValue = 2352.532;
			object.booleanValue = false;
			object.emptyArray = new String[] { };
			object.stringArray = new String[] { "foo", "bar" };
			object.objectArray = new SubObject[] { SubObject.create() };
			object.subObject = SubObject.create();
			return object;
		}
		
		public SimpleObject() {
			super();
		}
		
		public String getName() { return this.name; }
		public void setName(String name) { this.name = name; }

		public Integer getIntegerValue() { return this.integerValue; }
		public void setIntegerValue(Integer integerValue) { this.integerValue = integerValue; }

		public Double getDoubleValue() { return this.doubleValue; }
		public void setDoubleValue(Double doubleValue) { this.doubleValue = doubleValue; }

		public Boolean getBooleanValue() { return this.booleanValue; }
		public void setBooleanValue(Boolean booleanValue) { this.booleanValue = booleanValue; }

		public String[] getEmptyArray() { return this.emptyArray; }
		public void setEmptyArray(String[] emptyArray) { this.emptyArray = emptyArray; }

		public String[] getStringArray() { return this.stringArray; }
		public void setStringArray(String[] stringArray) { this.stringArray = stringArray; }
		
		public SubObject[] getObjectArray() { return this.objectArray; }
		public void setObjectArray(SubObject[] objectArray) { this.objectArray = objectArray; }

		public SubObject getSubObject() { return this.subObject; }
		public void setSubObject(SubObject subObject) { this.subObject = subObject; }
		
		public boolean equals(Object object) {
			if (object == null) { return false; }
			else if (!(object instanceof SimpleObject)) { return false; }
			
			SimpleObject that = (SimpleObject) object;
			return isEqual(this.name, that.name) &&
				   isEqual(this.integerValue, that.integerValue) &&
				   isEqual(this.doubleValue, that.doubleValue) &&
				   isEqual(this.booleanValue, that.booleanValue) &&
				   isEqual(this.emptyArray, that.emptyArray) &&
				   isEqual(this.stringArray, that.stringArray) &&
				   isEqual(this.objectArray, that.objectArray) &&
				   isEqual(this.subObject, that.subObject);
		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}
	}
	
	@SuppressWarnings("boxing")
	protected static class TestObject {
		private String name;
		private Integer integerValue;
		private Double doubleValue;
		private Boolean booleanValue;
		private String[] emptyArray;
		private Object[] singleArray;
		private Object[] array;
		private String[][] multiArray;
		private String nullable;
		private List<Object> emptyList;
		private List<String> singleList;
		private List<Object> list;
		private Map<Object, Object> emptyMap;
		private Map<Object, Object> singleMap;
		private Map<Object, Object> map;
		private Object subObject;
		
		public TestObject() {
			super();
		}
		
		public static TestObject create() {
			TestObject object = new TestObject();
			object.name = "test";
			object.integerValue = 59823532;
			object.doubleValue = 2352.532;
			object.booleanValue = false;
			object.emptyArray = new String[] { };
			object.singleArray = new Object[] { SubObject.create() };
			object.array = new Object[] { "foo", 2352.32, false, SubObject.create() };
			object.multiArray = new String[][] { { "foo", "bar" }, { "blah" } };
			object.nullable = null;
			object.emptyList = Arrays.asList();
			object.singleList = Arrays.asList("test");
			object.list = Arrays.asList("test", 352, false, SubObject.create());
			object.emptyMap = new HashMap<Object, Object>();
			object.singleMap = map("foo", "bar");
			object.map = map("foo", "bar", "blah", SubObject.create(), "value", 32552.32);
			object.subObject = SubObject.create();
			return object;
		}
		
		public String getName() { return this.name; }
		public void setName(String name) { this.name = name; }

		public Integer getIntegerValue() { return this.integerValue; }
		public void setIntegerValue(Integer integerValue) { this.integerValue = integerValue; }

		public Double getDoubleValue() { return this.doubleValue; }
		public void setDoubleValue(Double doubleValue) { this.doubleValue = doubleValue; }

		public Boolean getBooleanValue() { return this.booleanValue; }
		public void setBooleanValue(Boolean booleanValue) { this.booleanValue = booleanValue; }

		public String[] getEmptyArray() { return this.emptyArray; }
		public void setEmptyArray(String[] emptyArray) { this.emptyArray = emptyArray; }

		public Object[] getSingleArray() { return this.singleArray; }
		public void setSingleArray(Object[] singleArray) { this.singleArray = singleArray; }

		public Object[] getArray() { return this.array; }
		public void setArray(Object[] array) { this.array = array; }

		public String[][] getMultiArray() { return this.multiArray; }
		public void setMultiArray(String[][] multiArray) { this.multiArray = multiArray; }

 		public String getNullable() { return this.nullable; }
		public void setNullable(String nullable) { this.nullable = nullable; }

		public List<Object> getEmptyList() { return this.emptyList; }
		public void setEmptyList(List<Object> emptyList) { this.emptyList = emptyList; }

		public List<String> getSingleList() { return this.singleList; }
		public void setSingleList(List<String> singleList) { this.singleList = singleList; }

		public List<Object> getList() { return this.list; }
		public void setList(List<Object> list) { this.list = list; }

		public Map<Object, Object> getEmptyMap() { return this.emptyMap; }
		public void setEmptyMap(Map<Object, Object> emptyMap) { this.emptyMap = emptyMap; }

		public Map<Object, Object> getSingleMap() { return this.singleMap; }
		public void setSingleMap(Map<Object, Object> singleMap) { this.singleMap = singleMap; }

		public Map<Object, Object> getMap() { return this.map; }
		public void setMap(Map<Object, Object> map) { this.map = map; }

		public Object getSubObject() { return this.subObject; }
		public void setSubObject(Object subObject) { this.subObject = subObject; }

		public boolean equals(Object object) {
			if (object == null) { return false; }
			else if (!(object instanceof TestObject)) { return false; }
			
			TestObject that = (TestObject) object;
			return isEqual(this.name, that.name) &&
				   isEqual(this.integerValue, that.integerValue) &&
				   isEqual(this.doubleValue, that.doubleValue) &&
				   isEqual(this.booleanValue, that.booleanValue) &&
				   isEqual(this.emptyArray, that.emptyArray) &&
				   isEqual(this.singleArray, that.singleArray) &&
				   isEqual(this.array, that.array) &&
				   isEqual(this.multiArray, that.multiArray) &&
				   isEqual(this.nullable, that.nullable) &&
				   isEqual(this.emptyList, that.emptyList) &&
				   isEqual(this.singleList, that.singleList) &&
				   isEqual(this.list, that.list) &&
				   isEqual(this.emptyMap, that.emptyMap) &&
				   isEqual(this.singleMap, that.singleMap) &&
				   isEqual(this.map, that.map) &&
				   isEqual(this.subObject, that.subObject);
		}
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}

		private static Map<Object, Object> map(Object... kvps) {
			Map<Object, Object> map = new LinkedHashMap<Object, Object>();
			for (int i = 0; i + 1 < kvps.length; i += 2) {
				map.put(kvps[i], kvps[i + 1]);
			}
			
			return map;
		}
	}
	
	protected static class SubObject {
		private String foo;
		private Number number;
		private Class<?> clazz = SubObject.class;
		private DeepObject deep;
		private EmptyObject empty;
		
		public SubObject() {
			super();
		}
		
		@SuppressWarnings("boxing")
		public static SubObject create() {
			SubObject object = new SubObject();
			object.foo = "bar";
			object.number = 325352.35235;
			object.deep = DeepObject.create();
			object.empty = EmptyObject.create();
			return object;
		}

		public String getFoo() { return this.foo; }
		public void setFoo(String foo) { this.foo = foo; }

		public Number getNumber() { return this.number; }
		public void setNumber(Number number) { this.number = number; }

		public Class<?> getClazz() { return this.clazz; }
		public void setClazz(Class<?> clazz) { this.clazz = clazz; }

		public DeepObject getDeep() { return this.deep; }
		public void setDeep(DeepObject deep) { this.deep = deep; }
		
		public EmptyObject getEmpty() { return this.empty; }
		public void setEmpty(EmptyObject empty) { this.empty = empty; }
		
		public boolean equals(Object object) {
			if (object == null) { return false; }
			else if (!(object instanceof SubObject)) { return false; }
			
			SubObject that = (SubObject) object;
			return isEqual(this.foo, that.foo) &&
				   isEqual(this.number, that.number) &&
				   isEqual(this.clazz, that.clazz) &&
				   isEqual(this.deep, that.deep) &&
				   isEqual(this.empty, that.empty);
		}
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}
	}

	protected static class InheritedObject {
		private String inheritedName;
		private int inheritedId;
		
		public InheritedObject() {
			super();
		}

		public static InheritedObject create() {
			InheritedObject object = new InheritedObject();
			object.inheritedName = "inherited";
			object.inheritedId = 9283532;
			return object;
		}
		
		public String getInheritedName() { return this.inheritedName; }
		public void setInheritedName(String inheritedName) { this.inheritedName = inheritedName; }

		public int getInheritedId() { return this.inheritedId; }
		public void setInheritedId(int inheritedId) { this.inheritedId = inheritedId; }
		
		public boolean equals(Object object) {
			if (object == null) { return false; }
			else if (!(object instanceof InheritedObject)) { return false; }
			
			InheritedObject that = (InheritedObject) object;
			return isEqual(this.inheritedName, that.inheritedName) &&
				   isEqual(this.inheritedId, that.inheritedId);
		}
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}
	}
	
	protected static class DeepObject extends InheritedObject {
		private String deepValue;
		
		public DeepObject() {
			super();
		}

		public static DeepObject create() {
			DeepObject object = new DeepObject();
			object.deepValue = "deep";
			return object;
		}
		
		public String getDeepValue() { return this.deepValue; }
		public void setDeepValue(String deepValue) { this.deepValue = deepValue; }
		
		public boolean equals(Object object) {
			if (object == null) { return false; }
			else if (!(object instanceof DeepObject)) { return false; }
			
			DeepObject that = (DeepObject) object;
			return isEqual(this.deepValue, that.deepValue);
		}
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}
	}
	
	protected static class EmptyObject {
		public EmptyObject() { 
			super();
		}
		
		public static EmptyObject create() {
			EmptyObject object = new EmptyObject();
			return object;
		}
		
		public boolean equals(Object object) {
			if (object == null) { return false; }
			else if (!(object instanceof EmptyObject)) { return false; }
			
			// EmptyObject that = (EmptyObject) object;
			return true;
		}
		
		@Override
		public int hashCode() {
			return super.hashCode();
		}
	}
	
	protected static boolean isEqual(int a, int b) {
		return (a == b);
	}
	
	protected static boolean isEqual(double a, double b) {
		return (a == b);
	}
	
	protected static boolean isEqual(boolean a, boolean b) {
		return (a == b);
	}
	
	protected static boolean isEqual(Object a, Object b) {
		if (a == null && b == null) { return true; }
		else if (a != null && b == null) { return false; }
		else if (a == null && b != null) { return false; }
		else if (a.getClass() != b.getClass()) { return false; }
		
		if (a.getClass().isArray()) {
			int alength = Array.getLength(a);
			int blength = Array.getLength(b);
			if (alength != blength) { return false; }
			
			for (int i = 0; i < alength; i++) {
				if (!isEqual(Array.get(a, i), Array.get(b, i))) { return false; }
			}
			
			return true;
		}
		else { return a.equals(b); }
	}
}
