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
public class TestInterpreter extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
	}

	@Test
	void testEmptyDSLProgramRunsOnEmptyDSLInterpreter() {
		System.out.println("testEmptyDSLProgram");
		DSL dsl = new Interpreter();
	    dsl.eval {  };
	    
	    Closure cl = { 
	        println(".."+owner);
	        println("..."+this); 
	    }
	    cl.call();
	}
	
	@Test
	void testEmptyDSLInterpreterHasNoKeywords() {
		System.out.println("testEmptyDSL");
		DSL dsl = new Interpreter();
		try {
		    dsl.eval {
		    	// A DSL PROGRAM - BEGIN
		    	undefinedkeyword 1;
		    	assert(false); //should not be reached
		    	// A DSL PROGRAM - END
		    }
		} catch (Exception e) {
			//ignore exception as this is the correct behavior that an undefined keyword raise an exception
		}
	}
	
	@Test
	void testTestDSLInterpreter() {
		System.out.println("testTestDSLInterpreter");
		DSL dsl = new ExampleDSL();
	    dsl.eval {
	    	// A DSL PROGRAM - BEGIN
	    	
	    	assert(primitiveLiteral==1);
	    	
	    	assert(accessorLiteral==2); //accessing through getter
	    	
	    	accessorLiteral = 3; //accessing through setter
	    	assert(operation(primitiveLiteral,accessorLiteral)==4);
	    	
	    	nestedAbstraction { //using a block 
	    		accessorLiteral++;
	    		accessorLiteral++;
	    		assert(accessorLiteral==5);
	    	}
	    	
	    	//passing name params to block
	    	namedParamsAbstraction(x:3) { y-> 
	    	  assert y == 3
	    	}
	    	
	    	//passing variable params to block
	    	variableParamsAbstraction(1,2,3,{ x,y,z -> 
	    	  assert x+y+z == 6
	    	});

	    	// A DSL PROGRAM - END
	    }
	}
	
	
	

	
	
}
