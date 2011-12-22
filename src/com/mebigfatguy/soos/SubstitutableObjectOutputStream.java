/*
 * soos - An object output stream that replaces substitutable objects with an interned version
 * for better stream sizing.
 * 
 * Copyright 2011 MeBigFatGuy.com
 * Copyright 2011 Dave Brosius
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.mebigfatguy.soos;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamConstants;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * format compatible with ObjectOutputStream/ObjectInputStream to provide a more compressed stream by
 * converting substitutable objects written into the stream into a write of one specific interned object.
 * A substitutable object is an object of an Immutable class, such that if you have two objects of that class (a, b)
 * and a.hashCode() == b.hashCode() and a.equals(b) performing a = b or b = a does not change the behavior
 * of your program. Such examples are the Boxed primitives and the String class. You can add your own classes that
 * follow this behavior.
 */
public class SubstitutableObjectOutputStream implements ObjectOutput, ObjectStreamConstants {

	private static Set<Class<?>> substitutableClasses = new HashSet<Class<?>>();
	static {
		addSubstitutableClass(Boolean.class);
		addSubstitutableClass(Byte.class);
		addSubstitutableClass(Character.class);
		addSubstitutableClass(Short.class);
		addSubstitutableClass(Integer.class);
		addSubstitutableClass(Long.class);
		addSubstitutableClass(Float.class);
		addSubstitutableClass(Double.class);
		addSubstitutableClass(String.class);
	}
	
	private final Map<Object, Object> internMap = new HashMap<Object, Object>();
	private final ObjectOutputStream oos;
	
	/**
	 * adds a custom class that follows the rules of Substitutability
	 * @param cls the class to add
	 */
	public static void addSubstitutableClass(Class<?> cls) {
		substitutableClasses.add(cls);
	}
	
	/**
	 * constructs a SubstitutableObjectOutputStream from an underlying stream
	 * @param s the underlying stream
	 * @throws IOException if writing the stream header fails
	 */
	public SubstitutableObjectOutputStream(OutputStream s) throws IOException {
		oos = new ObjectOutputStream(s);
	}
	
	@Override
	public void writeBoolean(boolean v) throws IOException {
		oos.writeBoolean(v);
	}

	@Override
	public void writeByte(int v) throws IOException {
		oos.writeByte(v);
	}

	@Override
	public void writeShort(int v) throws IOException {
		oos.writeShort(v);
	}

	@Override
	public void writeChar(int v) throws IOException {
		oos.writeChar(v);
	}

	@Override
	public void writeInt(int v) throws IOException {
		oos.writeInt(v);
	}

	@Override
	public void writeLong(long v) throws IOException {
		oos.writeLong(v);
	}

	@Override
	public void writeFloat(float v) throws IOException {
		oos.writeFloat(v);
	}

	@Override
	public void writeDouble(double v) throws IOException {
		oos.writeDouble(v);
	}

	@Override
	public void writeBytes(String s) throws IOException {
		oos.writeBytes(s);
	}

	@Override
	public void writeChars(String s) throws IOException {
		oos.writeChars(s);
	}

	@Override
	public void writeUTF(String s) throws IOException {
		oos.writeUTF(s);
	}

	@Override
	public void writeObject(Object obj) throws IOException {
		if ((obj != null) && substitutableClasses.contains(obj.getClass())) {
			Object internedObj = internMap.get(obj);
			if (internedObj == null) {
				internMap.put(obj,  obj);
			} else {
				obj = internedObj;
			}
		}
		
		oos.writeObject(obj);
	}

	@Override
	public void write(int b) throws IOException {
		oos.write(b);
	}

	@Override
	public void write(byte[] b) throws IOException {
		oos.write(b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		oos.write(b, off, len);
	}

	@Override
	public void flush() throws IOException {
		oos.flush();
	}

	public void reset() throws IOException {
		internMap.clear();
		oos.reset();
	}

	@Override
	public void close() throws IOException {
		internMap.clear();
		oos.close();
	}
}
