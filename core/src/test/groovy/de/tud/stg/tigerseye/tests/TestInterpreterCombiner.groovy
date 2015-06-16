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
import junit.framework.TestCase 
import org.junit.Before 

import de.tud.stg.tigerseye.*;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestInterpreterCombiner extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
	}


	void testInterpreterCombinerUndefinedKeyword() {
		DSL composedDsl = new InterpreterCombiner(new ExampleA(),new ExampleB());
		try {
			composedDsl.eval {
		    	// A DSL PROGRAM - BEGIN
		    	
		    	assert (keyword7() + keyword7()) == 2

		    	// A DSL PROGRAM - END
		    }
		    assert false; //should not be reached, if the undefined keyword error was detected correctly
	    } catch (MissingMethodException mme) {
		    //ignore since detected error was correctly
	        println "undefined keyword error detected correctly"
	    }
	}
	
	void testInterpreterCombiner() {
		DSL dsl = new InterpreterCombiner(new ExampleA(),new ExampleB());
	    dsl.eval {
	    	// A DSL PROGRAM - BEGIN
	    	
	    	assert (keyword1() + keyword2()) == 3

	    	// A DSL PROGRAM - END
	    }

	    //Thread.currentThread().sleep(1000);
	}
	

	
}
