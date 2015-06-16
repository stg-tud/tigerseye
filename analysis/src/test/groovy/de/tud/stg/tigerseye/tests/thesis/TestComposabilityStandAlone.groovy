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
package de.tud.stg.tigerseye.tests.thesis;

import org.junit.Test;
import junit.framework.TestCase;
import org.junit.Before; 

import de.tud.stg.tigerseye.*;
import de.tud.stg.tigerseye.lang.logo.standalone.*;
import de.tud.stg.tigerseye.exceptions.*;

import de.tud.stg.popart.aspect.Aspect 
import de.tud.stg.popart.aspect.AspectManager 
import de.tud.stg.popart.aspect.CCCombiner 
import de.tud.stg.popart.aspect.extensions.Booter 
import de.tud.stg.tigerseye.lang.logo.dspcl.LogoPointcutInterpreter; 


/**
 * @author Tom Dinkelaker
 *
 */
public class TestComposabilityStandAlone extends TestCase {
	

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/*
	@Test
	void testTemplate() {
		DSL dsl = new UCBLogo();
//		concise = new ConciseLogoStandAlone();
//		extended = new ExtendedLogoStandAlone()
//		ucb = new UCBLogoStandAlone()
//		completeLogo = null;

		Closure program = {
	    	// A DSL PROGRAM - BEGIN
	    	
			turtle(name:"Square", color:red) {
			    repeat (4) {
				    fd 50
				    rt 90
			    }
			}
			
	    	// A DSL PROGRAM - END
	    }
	    
	    program.delegate = dsl;
	    program.call();
	}
	*/	
	
	@Test
	void testExtendedStandAlone() {
		DSL dsl = new ExtendedLogoStandAlone();

		Closure program = {
	    	// A DSL PROGRAM - BEGIN
	    	
			turtle(name:"rose", color:red) {
				setpencolor(red);
				
			    for (int i in 1..100) {
			    	forward i*2
			    	right 70 
			    }
			}
			
	    	// A DSL PROGRAM - END
	    }
	    
	    program.delegate = dsl;
	    program.call();
	    //Thread.currentThread().sleep(1000);

	}
	
	@Test
	void testConciseStandAlone() {
		DSL dsl = new ConciseLogoStandAlone();

		Closure program = {
	    	// A DSL PROGRAM - BEGIN
	    	
			turtle(name:"squareConcise", color:red) {
				fd 50
				rt 90
				fd 50
				rt 90
				fd 50
				rt 90
				fd 50
				rt 90
			}
			
	    	// A DSL PROGRAM - END
	    }
	    
	    program.delegate = dsl;
	    program.call();
	    //Thread.currentThread().sleep(1000);
	}
	

	@Test
	void testUCBStandAlone() {
		DSL dsl = new UCBLogoStandAlone();

		Closure program = {
	    	// A DSL PROGRAM - BEGIN
	    	
			turtle(name:"squareUCB", color:red) {
				repeat (4) {
					forward 50
					right 90
				}
			}
			
	    	// A DSL PROGRAM - END
	    }
	    
	    program.delegate = dsl;
	    program.call();
	    //Thread.currentThread().sleep(1000);
	}
	

	@Test
	void testCompleteStandAlone() {
		DSL dsl = new LinearizingCombiner(
			new ConciseLogoStandAlone(),
			new ExtendedLogoStandAlone(), //ExtendedLogo must be the first one
			new UCBLogoStandAlone()
		); 

		Closure program = {
	    	// A DSL PROGRAM - BEGIN
	    	
			turtle(name:"squareComplete", color:red) {
				setpencolor(green);
				repeat (4) {
				    fd 50
				    rt 90
				}
			}
			
	    	// A DSL PROGRAM - END
	    }
	    
	    program.delegate = dsl;
	    program.call();
	    Thread.currentThread().sleep(1000);
	}

}
