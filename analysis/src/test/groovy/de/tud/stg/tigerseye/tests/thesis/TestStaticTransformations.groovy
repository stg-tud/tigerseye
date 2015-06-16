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
 *
 */
public class TestStaticTransformations extends TestCase {
	
	DSL interpreter;
	DSL completeLogo;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception {
		//completeLogo = new UCBLogo();
		interpreter = new BlackBoxCombiner(new UCBLogo(),new Functional());
	}

	/*
	@Test
	void testTemplate() {
		DSL dsl = new UCBLogo();
	    dsl.eval {
	    	// A DSL PROGRAM - BEGIN
	    	
	    	assert (keyword1() + keyword2()) == 3

	    	// A DSL PROGRAM - END
	    }

	    //Thread.currentThread().sleep(1000);
	}
	*/
	
	@Test
	void testScenarioFlexibleNameTransfromationProblem() {
        Closure program = {
            define(name:"polygon") { edges, length ->
                repeat (edges) {
                    forward length 
                    right ((int)(360/edges))
                }
            }

            turtle(name:"Octagon",color:green) {
                polygon (8, 25) //will paint a green octagon with edge size 25
            } 
        }
		
		program.delegate = interpreter;
		program.call();
	}	
	
	@Test
	void testOptimizedIsFaster() {
		def interpreter = new ConcurrentLogo();
		def optimizer = new OptimizingLogo();

		Closure single = {
			turtle (name:" ",color:red) { 
				fun("polygon") { int length, int edges ->
		            int angle = (int)(360 / edges)
		            repeat (edges) {
				      forward length
		              right angle
		            }
			    }
				
				fun("flower") { int length, int edges ->
	              int angle = (int)(360 / edges)
	              //if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower start"
	              repeat (edges) {
	            	//if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.repeat start"
			        forward length
			        
	                //draw outer figure
	                left ((int)(90-(angle/2))) //adjust for peduncle  
	            	forward ((int)(length/2))  //paint peduncle
	                left ((int)(90-(angle/2))) //adjust for blossom           	
	                //if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.polygon calling"
	                polygon (((int)(length/2)), edges) //paint blossom 
	                //if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.polygon returning"
	                right ((int)(90-(angle/2)))	    
	            	backward ((int)(length/2))
	                right ((int)(90-(angle/2)))
			        
	                right angle	                
	                //if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.repeat start"
	              }
	              //if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower end"
			    }
			}
			go();
			
			turtle (name:"IceFlower",color:blue+green) {
			  right 45;
			  flower 50, 4
			}
			go();
		}

		long startTimeSingle = System.nanoTime();
		single.delegate = interpreter;
		single.call();
		long endTimeSingle = System.nanoTime();
		println "ms for one thread: ${(endTimeSingle-startTimeSingle)/1000000}"
		
		long startTimeSingleOptimzed = System.nanoTime();
		single.delegate = optimizer;
		single.call();
		long endTimeSingleOptimzed = System.nanoTime();
		println "ms for one thread: ${(endTimeSingleOptimzed-startTimeSingleOptimzed)/1000000}"
		
	    //must sleep until all threads have been finished because otherwise the JUnit test would end too early
		optimizer.waitUntilAllThreadsAreFinished()
	    println "all finished"
	    Thread.sleep(10);
	    
	    def durationNonOptimized = (endTimeSingle-startTimeSingle);
	    def durationOptimized = (endTimeSingleOptimzed-startTimeSingleOptimzed);
		println "speed up : ${durationNonOptimized / durationOptimized}"
	    assertTrue("OptimizedProgram should be faster", durationOptimized < durationNonOptimized);

	}
	
	@Test
	void testIllustrationInThesis() {
		def interpreter = new ConcurrentLogo();
		def optimizer = new OptimizingLogo();

		Closure single = {
			long startTimeOverall = System.nanoTime();
			int count = 0;

			turtle (name:" ",color:red) { 
				fun("polygon") { int length, int edges ->
 				    println ("before polygon ${++count}: "+((System.nanoTime()-startTimeOverall)/1000000000));
				    int angle = (int)(360 / edges)
		            repeat (edges) {
				      forward length
		              right angle
		            }
 				    println ("after polygon ${count}: "+((System.nanoTime()-startTimeOverall)/1000000000));
			    }			
			}
			go();
			
			turtle (name:"IceFlower",color:blue+green) {
	          int edges = 4
	          int length = 50
			  int angle = (int)(360 / edges)

			  right 45;
	          repeat (edges) {
			    forward length
	            left ((int)(90-(angle/2))) //adjust for peduncle  
	            forward ((int)(length/2))  //paint peduncle
	            left ((int)(90-(angle/2))) //adjust for blossom           	
	            //if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.polygon calling"
				println ("IceFlower before polygon: "+((System.nanoTime()-startTimeOverall)/1000000000));
	            polygon (((int)(length/2)), edges) //paint blossom 
				println ("IceFlower after polygon: "+((System.nanoTime()-startTimeOverall)/1000000000));
	            //if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.polygon returning"
	            right ((int)(90-(angle/2)))	    
	            backward ((int)(length/2))
	            right ((int)(90-(angle/2)))
			        
	            right angle	                
	            //if (DEBUG) println ">>>T${Thread.currentThread().getId()}:flower.repeat start"
	          }
			}
			go();
		}
		
		long startTimeSingle = System.nanoTime();
		single.delegate = interpreter;
		single.call();
		long endTimeSingle = System.nanoTime();
		println "ms for one thread: ${(endTimeSingle-startTimeSingle)/1000000}"
		
		long startTimeSingleOptimzed = System.nanoTime();
		single.delegate = optimizer;
		single.call();
	    //must sleep until all threads have been finished because otherwise the JUnit test would end too early
		optimizer.waitUntilAllThreadsAreFinished()
		long endTimeSingleOptimzed = System.nanoTime();
		println "ms for multiple thread: ${(endTimeSingleOptimzed-startTimeSingleOptimzed)/1000000}"

		println "all finished"
	    Thread.sleep(10);
	    
	    def durationNonOptimized = (endTimeSingle-startTimeSingle);
	    def durationOptimized = (endTimeSingleOptimzed-startTimeSingleOptimzed);
		println "speed up : ${durationNonOptimized / durationOptimized}"
	    assertTrue("OptimizedProgram should be faster", durationOptimized < durationNonOptimized);
		
        Thread.sleep(5000);

	}
}
