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
import junit.framework.Assert;

import de.tud.stg.tigerseye.lang.logo.*;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestConcurrentLogo extends TestCase {
	
	def DEBUG = false;
	
	def interpreter;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
		interpreter = new ConcurrentLogo();
	}

	@Test
	void testThreadedSquares() {
		Closure choreography = {
			turtle (name:"ThreadTeacher",color:red) { 
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
			
			turtle (name:"ThreadS",color:red) { 
				if (DEBUG) println "ThreadS"
				square 50
			}
				
			turtle (name:"ThreadM",color:green) { 
				if (DEBUG) println "ThreadM"
				square 100				
			}

			turtle (name:"ThreadL",color:blue) {
				if (DEBUG) println "ThreadL"
				square 150		
			}
			if (DEBUG) println "before you go go"
			go();
		}
		choreography.delegate = interpreter;
		choreography.call();

        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
		interpreter.waitUntilAllThreadsAreFinished()
        if (DEBUG) println "all finished"
        //Thread.sleep(1000);
	}	
	
	/*
	@Test
	void testThreadedRace() {
		Closure single = {
			turtle (name:"ThreadTeacher",color:red) { 
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
			  square 50
				
			  backward 150
			  square 100				

			  right 90
			  forward 50
			  square 150
			}
			go();
		}
		
		
		Closure multiple = {
			turtle (name:"ThreadTeacher",color:red) { 
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
			
			turtle (name:"ThreadS",color:red) { 
				if (DEBUG) println "ThreadS"
	  			forward 100
				square 50
			}
				
			turtle (name:"ThreadM",color:green) { 
				if (DEBUG) println "ThreadM"
	  			forward 100
				backward 150
				square 100				
			}

			turtle (name:"ThreadL",color:blue) {
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
	*/
}