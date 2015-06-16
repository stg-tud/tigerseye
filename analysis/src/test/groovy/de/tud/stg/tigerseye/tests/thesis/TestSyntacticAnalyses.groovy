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
import de.tud.stg.tigerseye.lang.logo.*;

/**
 * @author Tom Dinkelaker
 */
public class TestSyntacticAnalyses extends TestCase {
	
	//DSL interpreter;
	//DSL completeLogo;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception {
		//completeLogo = new UCBLogo();
		//interpreter = new BlackBoxCombiner(new UCBLogo(),new Functional());
	}

	/*
	void testTemplate() {
		DSL dsl = new UCBLogo();

		Closure program = {
	    	// A DSL PROGRAM - BEGIN
	    	
	    	assert (keyword1() + keyword2()) == 3

	    	// A DSL PROGRAM - END
	    }
	    
	    program.delegate = dsl;
	    program.call();

	    //Thread.currentThread().sleep(1000);
	}
	*/
	
	void testCustomizableSemanticsMechanism() {
		ExtendedLogoExpressionCounter dsl = new ExtendedLogoExpressionCounter();

		Closure program = {
	    	// A DSL PROGRAM - BEGIN
	    	
	        turtle(name:"A_red_and_a_black_square", color:red) { 
	            forward 50; right 90; forward 50; right 90; forward 50; right 90; forward 50; right 90; 
	            penup(); 
	            right 90; forward 100; left 90; //goto (x=100,y=0) 
	            pendown(); 
	            setpencolor black 
	            forward 50; right 90; forward 50; right 90; forward 50; right 90; forward 50; right 90; 
	        }

	    	// A DSL PROGRAM - END
	    }
	    
	    program.delegate = dsl;
	    program.call();
	    
	    assert dsl.expressionCounter == 25;

	    //Thread.currentThread().sleep(1000);
	}
	

}
