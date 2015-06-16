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

import junit.framework.TestCase 
import org.junit.Before 
import org.junit.Test;

import de.tud.stg.tigerseye.lang.logo.UCBLogo; 
import groovy.lang.Closure;
import de.tud.stg.tigerseye.*;

import de.tud.stg.tigerseye.exceptions.TransformationException;


/**
 * @author Tom Dinkelaker
 *
 */
public class TestOpenCombinerContextSensitive extends TestCase {
	
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
	
	
	@Test
	void testScenarioComposingContextSensitiveError2() {
		System.out.println("testScenarioComposingContextSensitiveError2");

		Closure programWithError = {
            define(name:"dble") { length -> 
                return  length * 2 //okay to define a function without Logo keywords
            }
            
            turtle(name:"Triangle", color:red) { 
 			    fd 100; rt 120; fd 100; rt 120; fd 100; rt 120;   
            }            

			define(name:"square") { length -> 
                //...
			    forward length
				right 90 
 			    forward length
				right 90 
 			    forward length
				right 90 
 			    forward length
				right 90 
 			    //...
            }

            turtle(name:"TwoSquares", color:red) { 
                def lenght2 =  apply("dble")(50)
            	apply("square")(lenght2)
            }
        }
		
		//should pass through
        programWithError.delegate = new BlackBoxCombiner(new UCBLogo(),new Functional());
        programWithError.call(); //context-sensitive errror is NOT enforced in this evaluation

        //should detect the error
        programWithError.delegate = new MyContextSensitiveCombiner(new Functional(),new UCBLogo());
		try {
            programWithError.call(); //context-sensitive error IS enforced 
            assert false == "Did not detect the context sensitive keyword conflict"
		} catch (TransformationException tex) {
			//Ignore because context sensitive keyword conflict was detected correctly
		}
		
        //Thread.sleep(3000);
	}
	
	
}
