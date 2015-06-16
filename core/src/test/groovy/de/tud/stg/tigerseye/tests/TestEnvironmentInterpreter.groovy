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

import de.tud.stg.tigerseye.LinearizingCombiner;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

import de.tud.stg.tigerseye.*;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestEnvironmentInterpreter extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception {
	}

	@Test
	void testEmptyDSLProgramRunsOnEmptyDSLInterpreter() {
		System.out.println("testEmptyDSLProgram");
		DSL dsl = new EnvironmentInterpreter();
	    
	    Closure cl = { 
	        println(".."+owner);
	        println("..."+this); 
	    }
	    cl.delegate = dsl;
	    cl.call();
	}
	
	@Test
	void testLocal() {
		System.out.println("testLocal");
		DSL dsl = new EnvironmentInterpreter();
	    
		Closure cl = { 
	        x = 1;
	        return x;
	    }
		cl.delegate = dsl;
		def result =  cl.call();
	    
	    assert result == 1
	}
	
	@Test
	void testUndefinedLocal() {
		System.out.println("testUndefinedLocal");
		DSL dsl = new EnvironmentInterpreter();
	    
		Closure cl = {
			x = 1;
	        def result = y; //undefined symbol
	        assert false //should not be reached since undefined symbol access throws and error 
	        return result;
	    }
		cl.delegate = dsl;
		def result;
		try {
		    result = cl.call();
		} catch (MissingPropertyException mpe) {
			//ignore since correctly 
		}
	    
	}

}
