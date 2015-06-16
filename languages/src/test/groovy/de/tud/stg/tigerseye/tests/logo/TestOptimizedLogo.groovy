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
public class TestOptimizedLogo extends TestCase {
	
	def DEBUG = false;
	
	def interpreter;
	def optimizer;
	def positionTracker;
	
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
		optimizer = new OptimizingLogo();
		positionTracker = new PositionTrackingLogo();
	}
	
	/*
	@Test
	void testNonOptimized() {
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
				
				fun("polygon") { int length, int edges ->
		            int angle = (int)(360 / edges)
		            repeat (edges) {
				      forward length
		              right angle
		            }
  			    }

				fun("flower") { int length, int edges ->
	              int angle = (int)(360 / edges)
	              repeat (edges) {
			        forward length
			        
	                //draw outer figure
	                left ((int)(90-(angle/2))) //adjust for peduncle  
	            	forward ((int)(length/4))  //paint peduncle
	                left ((int)(90-(angle/2))) //adjust for blossom           	
	                polygon (((int)(length/4)), edges) //paint blossom 
	                right ((int)(90-(angle/2)))	    
	            	backward ((int)(length/4))
	                right ((int)(90-(angle/2)))
			        
	                right angle	                
	              }
			    }
			}
			go();
			
			turtle (name:"Single",color:green) { 
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
			}
			go();
		}
		
		long startTimeSingle = System.nanoTime();
		single.delegate = interpreter;
		single.call();
		long endTimeSingle = System.nanoTime();
		if (DEBUG) println "ms for one thread: ${(endTimeSingle-startTimeSingle)/1000000}"
		
        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
		interpreter.waitUntilAllThreadsAreFinished()
        if (DEBUG) println "all finished"
        Thread.sleep(1000);
	}	
	*/		
	
	
	@Test
	void testOptimized() {
		Closure single = flowerProgram.clone();
		
		long startTimeSingle = System.nanoTime();
		single.delegate = optimizer;
		try {
		    single.call();
		} catch (java.util.ConcurrentModificationException cme) {
			//ignore
		}
		long endTimeSingle = System.nanoTime();
		if (DEBUG) println "ms for one thread: ${(endTimeSingle-startTimeSingle)/1000000}"
		
        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
		optimizer.waitUntilAllThreadsAreFinished()
        if (DEBUG) println "all finished"
        Thread.sleep(10);
        
        println "Turtles recruited: "+ optimizer.getTurtles().size();
	}	
	
	/*
	@Test
	void testOptimizedIsFaster() {
		Closure single = flowerProgram;
		
		
		long startTimeSingle = System.nanoTime();
		single.delegate = interpreter;
		single.call();
		long endTimeSingle = System.nanoTime();
		if (DEBUG) println "ms for one thread: ${(endTimeSingle-startTimeSingle)/1000000}"
		
		long startTimeSingleOptimzed = System.nanoTime();
		single.delegate = optimizer;
		single.call();
		long endTimeSingleOptimzed = System.nanoTime();
		if (DEBUG) println "ms for one thread: ${(endTimeSingleOptimzed-startTimeSingleOptimzed)/1000000}"
		
        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
		optimizer.waitUntilAllThreadsAreFinished()
        if (DEBUG) println "all finished"
        Thread.sleep(10);
        
        def durationNonOptimized = (endTimeSingle-startTimeSingle);
        def durationOptimized = (endTimeSingleOptimzed-startTimeSingleOptimzed);
        assertTrue("OptimizedProgram should be faster", durationOptimized < durationNonOptimized);
	}
	*/
}