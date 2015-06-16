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

import org.javalogo.*;

import de.tud.stg.tigerseye.lang.logo.*;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestNonOptimizedLogo extends TestCase {
	
	def DEBUG = false;
	
	def interpreter;
	
	def smallFlowerProgram = {
			turtle (name:"Teacher",color:red) { 
				fun("polygon") { int length, int edges ->
				    if (DEBUG) println ">>>polygon start"
		            int angle = (int)(360 / edges)
		            repeat (edges) {
				      forward length
		              right angle
		            }
				    if (DEBUG) println ">>>polygon start"
			    }
				
				fun("flower") { int length, int edges ->
	              int angle = (int)(360 / edges)
	              if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower start"
	              repeat (edges) {
	            	if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.repeat start"
			        forward length
			        
	                //draw outer figure
	                left ((int)(90-(angle/2))) //adjust for peduncle  
	            	forward ((int)(length/4))  //paint peduncle
	                left ((int)(90-(angle/2))) //adjust for blossom           	
	                if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.polygon calling"
	                polygon (((int)(length/4)), edges) //paint blossom 
	                if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.polygon returning"
	                right ((int)(90-(angle/2)))	    
	            	backward ((int)(length/4))
	                right ((int)(90-(angle/2)))
			        
	                right angle	                
	                if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.repeat start"
	              }
	              if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower end"
			    }
			}
			go();
			
			turtle (name:"ToOptimizeSingle",color:green) { 
			  if (DEBUG) println "ToOptimizeSingle.flower calling"
			  flower 50, 6
			  if (DEBUG) println "ToOptimizeSingle.flower returning"
			}
			go();
		}
	
	def flowerProgram = {
			turtle (name:"Teacher",color:red) { 
				fun("polygon") { int length, int edges ->
				    if (DEBUG) println ">>>polygon start"
		            int angle = (int)(360 / edges)
		            repeat (edges) {
				      forward length
		              right angle
		            }
				    if (DEBUG) println ">>>polygon start"
			    }
				
				fun("flower") { int length, int edges ->
	              int angle = (int)(360 / edges)
	              if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower start"
	              repeat (edges) {
	            	if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.repeat start"
			        forward length
			        
	                //draw outer figure
	                left ((int)(90-(angle/2))) //adjust for peduncle  
	            	forward ((int)(length/4))  //paint peduncle
	                left ((int)(90-(angle/2))) //adjust for blossom           	
	                if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.polygon calling"
	                polygon (((int)(length/4)), edges) //paint blossom 
	                if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.polygon returning"
	                right ((int)(90-(angle/2)))	    
	            	backward ((int)(length/4))
	                right ((int)(90-(angle/2)))
			        
	                right angle	                
	                if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.repeat start"
	              }
	              if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower end"
			    }
			}
			go();
			
			turtle (name:"ToOptimizeSingle",color:green) { 
			  if (DEBUG) println "ToOptimizeSingle.flower calling"
			  //flower 50, 6
			  
			  polygon 50, 6

			  pu()
			  forward 100
			  pd()
			  
			  polygon 5, 30

			  pu()
			  backward 300
			  pd()
			  
			  flower 50, 4
			  setpc red
			  flower 50, 6
			  setpc blue
			  flower 50, 8

			  if (DEBUG) println "ToOptimizeSingle.flower returning"
			}
			go();
		}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
		interpreter = new ConcurrentLogo();
	}
	
	@Test
	void testSmallInterpreted() {
		Closure single = smallFlowerProgram;
		
		long startTimeSingle = System.nanoTime();
		single.delegate = interpreter;
		single.call();
		long endTimeSingle = System.nanoTime();
		if (DEBUG) println "ms for one thread: ${(endTimeSingle-startTimeSingle)/1000000}"
		
        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
		interpreter.waitUntilAllThreadsAreFinished()
        if (DEBUG) println "all finished"
        Thread.sleep(10);
	}	
	
	void testBigInterpreted() {
		Closure single = flowerProgram;
		
		long startTimeSingle = System.nanoTime();
		single.delegate = interpreter;
		single.call();
		long endTimeSingle = System.nanoTime();
		if (DEBUG) println "ms for one thread: ${(endTimeSingle-startTimeSingle)/1000000}"
		
        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
		interpreter.waitUntilAllThreadsAreFinished()
        if (DEBUG) println "all finished"
        Thread.sleep(10);
	}	
}