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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Test;

public class SubstitutableObjectOutputStreamTest {

	@Test
	public void testSOOS() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		SubstitutableObjectOutputStream soos = new SubstitutableObjectOutputStream(baos);
		
		for (int i = 0; i < 10; i++) {
			soos.writeObject(new String("hello"));
			soos.writeObject(new String("world"));
		}
		
		soos.flush();
		soos.close();
		
		byte[] soosData = baos.toByteArray();
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(soosData));
		for (int i = 0; i < 10; i++) {
			Assert.assertEquals("hello", ois.readObject());
			Assert.assertEquals("world", ois.readObject());
		}
		
		baos.reset();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		for (int i = 0; i < 10; i++) {
			soos.writeObject(new String("hello"));
			soos.writeObject(new String("world"));
		}
		
		oos.flush();
		oos.close();
		
		byte[] oosData = baos.toByteArray();
		
		Assert.assertTrue(soosData.length < oosData.length);
	}
}
