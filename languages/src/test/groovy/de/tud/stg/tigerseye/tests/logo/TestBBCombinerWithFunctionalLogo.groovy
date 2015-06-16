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
package de.tud.stg.tigerseye.tests.logo;

import de.tud.stg.tigerseye.lang.logo.UCBLogo; 
import de.tud.stg.tigerseye.lang.logo.SimpleLogo; 
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
public class TestBBCombinerWithFunctionalLogo extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
	}

	void testBBCombinerFunctionalLogo() {
		DSL dsl = new BlackBoxCombiner(new UCBLogo(),new Functional());
	    
		Closure mixedKeywordsProgram = {
			define(name:"square") { length -> 
			    repeat (4) {
				    forward length 
					right 90 
				}
			}

			turtle(name:"Square", color:red) { 
			    setpencolor blue
				apply("square")(50) 
				right 180
				setpencolor green
				apply("square")(100) 
			}
	    }
		
		mixedKeywordsProgram.delegate = dsl;

		mixedKeywordsProgram.call();

		//Thread.currentThread().sleep(3000);
	}
	

	
	
}
