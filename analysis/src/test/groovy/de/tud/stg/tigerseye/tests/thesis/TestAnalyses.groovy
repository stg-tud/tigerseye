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
import static junit.framework.Assert.*;

import de.tud.stg.tigerseye.lang.logo.*;
import de.tud.stg.tigerseye.lang.logo.analysis.CleanCodeAnalysisFunctional;
import de.tud.stg.tigerseye.lang.logo.analysis.CleanCodeAnalysisCompleteLogo;
import de.tud.stg.tigerseye.lang.logo.analysis.CleanCodeAnalysisFunctionalLogo;
import de.tud.stg.tigerseye.lang.logo.analysis.ExpressionCounterCombiner 
import de.tud.stg.tigerseye.lang.logo.analysis.ExpressionCounterFunctionalLogo;
import de.tud.stg.tigerseye.lang.logo.analysis.ExpressionCounterCompleteLogo;
import de.tud.stg.tigerseye.lang.logo.analysis.IExpressionCounter;
import de.tud.stg.tigerseye.exceptions.AnalysisException;
import de.tud.stg.tigerseye.LinearizingCombiner;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestAnalyses extends TestCase {
	
	def DEBUG = false;
	
	def profiler;
	def exprCntr;
	
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@Before
//	void setUp() throws Exception{
//		profiler = new TimeEstimationFunctionalLogo();
//	}
//	
//	@Test
//	void testSingleCommands() {
//		Closure choreography;
//		
//		choreography = { turtle ([:]) { forward 50 } }
//		choreography.delegate = profiler;
//		choreography.call();
//		assertEquals(100,profiler.getProgramDuration());
//		
//		profiler.reset();
//		choreography = { turtle ([:]) { backward 50 } }
//		choreography.delegate = profiler;
//		choreography.call();
//		assertEquals(100,profiler.getProgramDuration());
//		
//		profiler.reset();
//		choreography = { turtle ([:]) { right 50 } }
//		choreography.delegate = profiler;
//		assertEquals(0,profiler.getProgramDuration());
//		choreography.call();
//		assertEquals(50,profiler.getProgramDuration());
//		
//		profiler.reset();
//		choreography = { turtle ([:]) { left 50 } }
//		choreography.delegate = profiler;
//		choreography.call();
//		assertEquals(50,profiler.getProgramDuration());
//	}
//	
//	
//	@Test
//	void testProfilingSquare() {
//		Closure choreography = {
//			turtle (name:"Square",color:green) {
//			    forward 50
//			    right 90
//			    forward 50
//			    right 90
//			    forward 50
//			    right 90
//			    forward 50
//			    right 90
//			}
//		}
//		choreography.delegate = profiler;
//		choreography.call();
//		
//		println "Duration:"+profiler.getProgramDuration();
//		
////		choreography.delegate = interpreter;
////		choreography.call();
//	}
//
//	@Test
//	void testProfilingFunAndApp() {
//		Closure choreography = {
//			turtle (name:"Fun&App",color:green) {
//				fun("square") {
//			        forward 50
//			        right 90
//			        forward 50
//			        right 90
//			        forward 50
//			        right 90
//			        forward 50
//			        right 90
//				}
//				app("square")();				
//				app("square")();				
//			}
//		}
//		choreography.delegate = profiler;
//		choreography.call();
//		
//		println "Duration:"+profiler.getProgramDuration();
//		
////		choreography.delegate = interpreter;
////		choreography.call();
//	}
//	
//	@Test
//	void testProfileRace() {
//		Closure single = {
//			turtle (name:"SingleTeacher",color:red) { 
//				fun("square") { int length ->
//			        forward length
//			        right 90
//			        forward length
//			        right 90
//			        forward length
//			        right 90
//			        forward length
//			        right 90
//				}
//			}
//			go();
//			
//			turtle (name:"Single",color:black) { 
//  			  forward 100
//			  app("square")(50);
//				
//			  backward 150
//			  app("square")(100);				
//
//			  right 90
//			  forward 50
//			  app("square")(150);
//			}
//			go();
//		}
//		
//		
//		Closure multiple = {
//			turtle (name:"MultipleTeacher",color:red) { 
//				fun("square") { int length ->
//			        forward length
//			        right 90
//			        forward length
//			        right 90
//			        forward length
//			        right 90
//			        forward length
//			        right 90
//				}
//			}
//			go();
//			
//			turtle (name:"MultipleS",color:red) { 
//				if (DEBUG) println "ThreadS"
//	  			forward 100
//	  			app("square")(50)
//			}
//				
//			turtle (name:"MultipleM",color:green) { 
//				if (DEBUG) println "ThreadM"
//	  			forward 100
//				backward 150
//				app("square")(100)				
//			}
//
//			turtle (name:"MultipleL",color:blue) {
//				if (DEBUG) println "ThreadL"
//	  			forward 100
//				backward 150
//				right 90
//				forward 50
//				app("square")(150)		
//			}
//			if (DEBUG) println "before you go go"
//			go();
//		}
//
//		single.delegate = profiler;
//		single.call();
//		long predictedSingleThreadTime = profiler.getProgramDuration();
//		println "Profiler predicted ms for one thread: ${predictedSingleThreadTime}"
//
//        profiler.reset();
//		multiple.delegate = profiler;
//		multiple.call();
//		long predictedMultiThreadTime = profiler.getProgramDuration()
//		println "Profiler predicted ms for multiple threads: $predictedMultiThreadTime}"
//		
//		long startTimeSingle = System.nanoTime();
//		single.delegate = new ConcurrentLogo();;
//		single.call();
//		long endTimeSingle = System.nanoTime();
//		long actualSingleThreadTime = Math.round((endTimeSingle-startTimeSingle)/1000000);
//		println "ms for one thread: ${actualSingleThreadTime}"
//
//		long startTimeMultiple = System.nanoTime();
//		multiple.delegate = new ConcurrentLogo();;
//		multiple.call();
//		long endTimeMultiple = System.nanoTime();
//		long actualMultiThreadTime = Math.round((endTimeMultiple-startTimeMultiple)/1000000);
//		println "ms for multiple threads: ${actualMultiThreadTime}"
//		
//		assert predictedSingleThreadTime >= predictedMultiThreadTime;
//		assert actualSingleThreadTime >= actualMultiThreadTime;
//		assert Math.round(1 - (predictedSingleThreadTime / actualSingleThreadTime)) < 0.2
//		assert Math.round(1 - (predictedMultiThreadTime / actualMultiThreadTime)) < 0.2
//		
//        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
//		interpreter.waitUntilAllThreadsAreFinished()
//        if (DEBUG) println "all finished"
//        //Thread.sleep(1000);
//	}	
	
	@Test
	void testCleanCodeAnalysis() {
		println "-----------------------"
		println "testCleanCodeAnalysis"
		Closure choreography = {
			turtle (name:"Fun&App",color:green) {
				fun("square") {
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
				}
				app("square")();				
			}
		}
		choreography.delegate = new CleanCodeAnalysisFunctionalLogo();
		choreography.call();
	}
	
	@Test
	void testCleanCodeAnalysisComposedNoViolation() {
		println "-----------------------"
		println "testCleanCodeAnalysisComposedNoViolation"
		Closure choreography = {
			turtle (name:"Fun&App",color:green) {
				fun("square") {
					repeat (4) {
			            forward 50
			            right 90
					}
				}
				app("square")();				
			}
		}
		IExpressionCounter exprCntr = new ExpressionCounterFunctionalLogo();
		choreography.delegate = new LinearizingCombiner(new CleanCodeAnalysisCompleteLogo(exprCntr),new CleanCodeAnalysisFunctional(exprCntr));
		choreography.call();
	}
	
	@Test
	void testCleanCodeAnalysisComposedViolationInFunction() {
		println "-----------------------"
		println "testCleanCodeAnalysisComposedViolationInFunction"
		Closure choreography = {
			turtle (name:"Fun&App",color:green) {
				fun("square") {
			        forward 50; right 90; forward 50; right 90; forward 50; right 90; forward 50; right 90;
			        right 45;
			        forward 50; right 90; forward 50; right 90; forward 50; right 90; forward 50; right 90;
				}
				app("square")();				
			}
		}
		IExpressionCounter exprCntr = new ExpressionCounterCombiner(
			new ExpressionCounterCompleteLogo(),
		    new ExpressionCounterFunctionalLogo());
		choreography.delegate = new LinearizingCombiner(new CleanCodeAnalysisCompleteLogo(exprCntr),new CleanCodeAnalysisFunctional(exprCntr));
		try {
  		    choreography.call();
  		    assert false == "The violation of the coding convention has not been detected"
		} catch (AnalysisException ae) {
			//ignore because the violation of the coding convention has been correctly detected
		}
	}

	@Test
	void testCleanCodeAnalysisComposedViolationInRepeat() {
		println "-----------------------"
		println "testCleanCodeAnalysisComposedViolationInRepeat"
		Closure choreography = {
			turtle (name:"Fun&App",color:green) {
				repeat (2) {
 			          forward 50; right 90; forward 50; right 90; forward 50; right 90; forward 50; right 90;
 				      right 45;
 			          forward 50; right 90; forward 50; right 90; forward 50; right 90; forward 50; right 90;
 				      right 45;
				}
			}
		}
		IExpressionCounter exprCntr = new ExpressionCounterCombiner(
				new ExpressionCounterCompleteLogo(),
			    new ExpressionCounterFunctionalLogo());
		choreography.delegate = new LinearizingCombiner(new CleanCodeAnalysisCompleteLogo(exprCntr),new CleanCodeAnalysisFunctional(exprCntr));
		try {
  		    choreography.call();
  		    assert false == "The violation of the coding convention has not been detected"
		} catch (AnalysisException ae) {
			//ignore because the violation of the coding convention has been correctly detected
		}
	}

	@Test
	void testCleanCodeAnalysisComposedViolationInTurtle() {
		println "-----------------------"
		println "testCleanCodeAnalysisComposedViolationInTurtle"
		Closure choreography = {
			turtle (name:"Fun&App",color:green) {
			    forward 50; right 90; forward 50; right 90; forward 50; right 90; forward 50; right 90;
			    right 45;
			    forward 50; right 90; forward 50; right 90; forward 50; right 90; forward 50; right 90;
			}
		}
		IExpressionCounter exprCntr = new ExpressionCounterCombiner(
				new ExpressionCounterCompleteLogo(),
			    new ExpressionCounterFunctionalLogo());
		choreography.delegate = new LinearizingCombiner(new CleanCodeAnalysisCompleteLogo(exprCntr),new CleanCodeAnalysisFunctional(exprCntr));
		try {
  		    choreography.call();
  		    assert false == "The violation of the coding convention has not been detected"
		} catch (AnalysisException ae) {
			//ignore because the violation of the coding convention has been correctly detected
		}
	}

}