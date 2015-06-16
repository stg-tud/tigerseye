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

import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;
import static junit.framework.Assert.*;

import de.tud.stg.tigerseye.lang.logo.*;

import java.awt.Color;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestProfilingLogo extends TestCase {
	
	def DEBUG = false;
	
	def interpreter;
	def profiler;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
		interpreter = new ConcurrentLogo();
		profiler = new ProfilingLogo();
	}
	
	@Test
	void testSingleCommands() {
		Closure choreography;
		
		choreography = { turtle ([:]) { forward 50 } }
		choreography.delegate = profiler;
		choreography.call();
		assertEquals(100,profiler.getProgramDuration());
		
		profiler.reset();
		choreography = { turtle ([:]) { backward 50 } }
		choreography.delegate = profiler;
		choreography.call();
		assertEquals(100,profiler.getProgramDuration());
		
		profiler.reset();
		choreography = { turtle ([:]) { right 50 } }
		choreography.delegate = profiler;
		assertEquals(0,profiler.getProgramDuration());
		choreography.call();
		assertEquals(50,profiler.getProgramDuration());
		
		profiler.reset();
		choreography = { turtle ([:]) { left 50 } }
		choreography.delegate = profiler;
		choreography.call();
		assertEquals(50,profiler.getProgramDuration());
	}
	
	
	@Test
	void testProfilingSquare() {
		Closure choreography = {
			turtle (name:"Square",color:green) {
			    forward 50
			    right 90
			    forward 50
			    right 90
			    forward 50
			    right 90
			    forward 50
			    right 90
			}
		}
		choreography.delegate = profiler;
		choreography.call();
		
		println "Duration:"+profiler.getProgramDuration();
		
//		choreography.delegate = interpreter;
//		choreography.call();
	}

	@Test
	void testProfilingFunAndApp() {
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
				app("square")();				
			}
		}
		choreography.delegate = profiler;
		choreography.call();
		
		println "Duration:"+profiler.getProgramDuration();
		
//		choreography.delegate = interpreter;
//		choreography.call();
	}
	
	@Test
	void testProfileRace() {
		Closure single = {
			turtle (name:"SingleTeacher",color:red) { 
				fun("square") { int length ->
			        forward length
			        right 90
			        forward length
			        right 90
			        forward length
			        right 90
			        forward length
			        right 90
				}
			}
			go();
			
			turtle (name:"Single",color:black) { 
  			  forward 100
			  square 50;
				
			  backward 150
			  square 100				

			  right 90
			  forward 50
			  square 150
			}
			go();
		}
		
		
		Closure multiple = {
			turtle (name:"MultipleTeacher",color:red) { 
				fun("square") { int length ->
			        forward length
			        right 90
			        forward length
			        right 90
			        forward length
			        right 90
			        forward length
			        right 90
				}
			}
			go();
			
			turtle (name:"MultipleS",color:red) { 
				if (DEBUG) println "ThreadS"
	  			forward 100
				square 50
			}
				
			turtle (name:"MultipleM",color:green) { 
				if (DEBUG) println "ThreadM"
	  			forward 100
				backward 150
				square 100				
			}

			turtle (name:"MultipleL",color:blue) {
				if (DEBUG) println "ThreadL"
	  			forward 100
				backward 150
				right 90
				forward 50
				square 150		
			}
			if (DEBUG) println "before you go go"
			go();
		}

		single.delegate = profiler;
		single.call();
		println "Profiler predicted ms for one thread: ${profiler.getProgramDuration()}"

        profiler.reset();
		multiple.delegate = profiler;
		multiple.call();
		println "Profiler predicted ms for multiple threads: ${profiler.getProgramDuration()}"
		
		long startTimeSingle = System.nanoTime();
		single.delegate = interpreter;
		single.call();
		long endTimeSingle = System.nanoTime();
		println "ms for one thread: ${(endTimeSingle-startTimeSingle)/1000000}"

		long startTimeMultiple = System.nanoTime();
		multiple.delegate = interpreter;
		multiple.call();
		long endTimeMultiple = System.nanoTime();
		println "ms for multiple threads: ${(endTimeMultiple-startTimeMultiple)/1000000}"
		
		
		
        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
		interpreter.waitUntilAllThreadsAreFinished()
        if (DEBUG) println "all finished"
        //Thread.sleep(1000);
	}	
	

}