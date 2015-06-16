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

import groovy.lang.Closure;
import groovy.lang.MissingMethodException;
import junit.framework.TestCase 
import org.junit.Before 

import de.tud.stg.tigerseye.*;

import de.tud.stg.tigerseye.exceptions.SyntaxConflictException;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestBBCombiner extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
	}

	
	void testBBCombinerDisjoint() {
		DSL composedDsl = new BlackBoxCombiner(new ExampleA(),new ExampleB());
		composedDsl.eval {
	    	// A DSL PROGRAM - BEGIN
	    	
	    	assert (keyword1() + keyword2()) == 3

	    	// A DSL PROGRAM - END
	    }

	    println "disjoint no conflicts had to be detected"
	    
	    //Thread.currentThread().sleep(1000);
	}
	
	void testBBCombinerWithNoConflictBecauseSameInstance() {
		DSL constituentDsl = new ExampleA();
		DSL composedDsl = new BlackBoxCombiner(constituentDsl,constituentDsl);

	    composedDsl.eval {
	    	// A DSL PROGRAM - BEGIN
	    	
	    	assert (keyword1() + keyword1()) == 2

	    	// A DSL PROGRAM - END
	    }
	    
	    println "disjoint no conflicts had to be detected"
	}
	
	void testBBCombinerWithConflict() {
		DSL composedDsl 
		try {
			composedDsl = new BlackBoxCombiner(new ExampleA(),new ExampleA());
		    assert false; //should not be reached, if the undefined syntax conflict was detected correctly
	    } catch (SyntaxConflictException sce) {
		    //ignore since detected error was correctly
	        println "syntax conflict detected correctly"
	    }
	}
	
	void testBBCombinerWithConflictByInheritance() {
		DSL composedDsl 
		try {
			composedDsl = new BlackBoxCombiner(new ExampleA(),new ExampleC());
		    assert false; //should not be reached, if the undefined syntax conflict was detected correctly
	    } catch (SyntaxConflictException sce) {
		    //ignore since detected error was correctly
	        println "syntax conflict with inherited keyword detected correctly"
	    }
	    
	    //Thread.currentThread().sleep(1000);
	}
	
	
}
