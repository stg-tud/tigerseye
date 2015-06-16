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

import junit.framework.TestCase;
import org.junit.Before; 
import org.junit.Test;

import de.tud.stg.tigerseye.*;
import de.tud.stg.tigerseye.lang.logo.*;
import de.tud.stg.tigerseye.lang.logo.PositionTrackingLogo;

//import de.tud.stg.tigerseye.lang.logo.analysis.PositiveValueAnalysisCompleteLogo;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestSemanticAnalyses extends TestCase {
	
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
	
//	void testCustomizableSemanticsMechanism() {
//		Closure program = {
//	    	// A DSL PROGRAM - BEGIN
//	        fun("hexagon") { length -> 
//                turtle(name:"hexagon",color:red) {
//                    repeat (6) {
//                        forward length 
//                        right 60 
//                    }
//                }
//            } 
//
//            def i = 100
//            app("hexagon")(50-i) 
//	    	// A DSL PROGRAM - END
//	    }
//	    program.delegate = new LinearizingCombiner(
//			new PositiveValueAnalysisCompleteLogo(), new AbstractInterpretationFunctional());;
//	    try {
//		    program.call();
//			assert false == "Did not detect the semantic error correctly"
//	    } catch (AnalysisException ae) {
//		    //ignore since the semantic error was detected correctly
//		}
//	    //Thread.currentThread().sleep(1000);
//	}
	
//	void testCustomizableSemanticsMechanismWithVariables() {
//		Closure program = {
//			// A DSL PROGRAM - BEGIN
//			fun("hexagon") { length ->
//				turtle(name:"hexagon",color:red) {
//					repeat (6) {
//						forward length
//						right 60
//					}
//				}
//			}
//
//			i = 100
//			app("hexagon")(50-i)
//			// A DSL PROGRAM - END
//		}
//		program.delegate = new LinearizingCombiner(
//			new PositiveValueAnalysisCompleteLogo(), new AbstractInterpretationFunctional(), new EnvironmentInterpreter());;
//		try {
//		    program.call();
//			assert false == "Did not detect the semantic error correctly"
//	    } catch (AnalysisException ae) {
//		    //ignore since the semantic error was detected correctly
//		}
//		//Thread.currentThread().sleep(1000);
//	}
//	
//	void testCustomizableSemanticsMechanismWithVariablesAndFunctions() {
//		Closure program = {
//			// A DSL PROGRAM - BEGIN
//			define(name:"hexagon") { length ->
//				turtle(name:"hexagon",color:red) {
//					repeat (6) {
//						forward length
//						right 60
//					}
//				}
//			}
//
//			i = 100
//			apply("hexagon")(50-i)
//			// A DSL PROGRAM - END
//		}
//		EnvironmentInterpreter env = new EnvironmentInterpreter();
//		env.scopingStrategy = EnvironmentInterpreter.DYNAMIC_SCOPING;
//		program.delegate = new LinearizingCombiner(
//			new PositiveValueAnalysisCompleteLogo(), env, new Functional());;
//		try {
//			program.call();
//			assert false == "Did not detect the semantic error correctly"
//		} catch (AnalysisException ae) {
//			//ignore since the semantic error was detected correctly
//		}
//		//Thread.currentThread().sleep(1000);
//	}

	@Test
	void testPaintingStairs() {
		Closure shape = {
			fun("stair") { 
				fd 10;lt 90;fd 10;rt 90;fd 10;lt 90;fd 10;lt 90;fd 10;rt 90;fd 10;lt 90;fd 10;
			}
				
			turtle (name:"Figure",color:red) { 
		        //forward 50
		        //left 90
		        //forward 40
		        //right 90
		        right 135
		        stair()
			}
		}
		
		def painter = new FunctionalLogo() 
		shape.delegate = painter;
		shape.call();
		Thread.sleep(3000);
	}	
	
	
	@Test
	void testPositionTrackingStairs() {
		Closure shape = {
			fun("stair") { 
				fd 2828;lt 90;fd 2828;rt 90;fd 2828;lt 90;fd 2828;lt 90;fd 2828;rt 90;fd 2828;lt 90;fd 2828
			}
				
			turtle (name:"Figure",color:red) { 
//		        forward 5000
//		        left 90
//		        forward 4000
//		        right 90
		        right 135
		        println "\nSTAR\n"
		        stair()
			}
		}
		
		def positionTracker = new PositionTrackingLogo(); 
		shape.delegate = positionTracker;
		shape.call();
		
		
		
		println "x : $positionTracker.deltaX";
		println "y : $positionTracker.deltaY";
		println "angle : $positionTracker.deltaAngular";
//		assertEquals("wrong x",0.0,positionTracker.deltaX,0.001);
//		assertEquals("wrong y",0.0,positionTracker.deltaY,0.001);
//		assertEquals("wrong angle",0.0,positionTracker.deltaAngular,0.001);
	}
}
