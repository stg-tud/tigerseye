///////////////////////////////////////////////////////////////////////////////
// Copyright 2009-2015, Technische Universitaet Darmstadt (TUD), Germany
//
// The TUD licenses this file to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
///////////////////////////////////////////////////////////////////////////////
package de.tud.stg.tigerseye.tests;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import junit.framework.Assert;
import de.tud.stg.tigerseye.*;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestStructural extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
	}

	
	@Test
	void testStructural() {
		System.out.println("testStructural");
		DSL dsl = new Structural();

        Closure cl = {
        	define(name:"tuple"){ 
        		x = Integer
        		y = Integer
        	}
        		
        	init(name:"tuple",x:3,y:5);
        }
        
        cl.delegate = dsl;
        def result = cl.call();
        println result;
	}
	
	@Test
	void testStructuralErrorMissingSlotInit() {
		System.out.println("testStructuralErrorMissingSlotInit");
		DSL dsl = new Structural();

        Closure cl = {
        	define(name:"tuple"){ 
        		x = Integer
        		y = Integer
        	}
        	
        	try {
        	    init(name:"tuple",x:3);
        	    assert false == "not reachable"
        	} catch (StructuralTypeException ex) {
        		println "Type error caught"
        		//throw ex;
        	}
        }
        
        cl.delegate = dsl;
        def result = cl.call();
        println result;
	}
	
	
	@Test
	void testStructuralErrorInitSlotWithWrongType() {
		System.out.println("testStructuralErrorInitSlotWithWrongType");
		DSL dsl = new Structural();

        Closure cl = {
        	define(name:"tuple"){ 
        		x = Integer
        		y = Integer
        	}
        	
        	try {
        	    init(name:"tuple",x:3,y:"error");
        	    assert false == "not reachable"
        	} catch (StructuralTypeException ex) {
        		println "Type error caught"
        		//throw ex;
        	}
        }
        
        cl.delegate = dsl;
        def result = cl.call();
        println result;
	}
	
	
	
}


