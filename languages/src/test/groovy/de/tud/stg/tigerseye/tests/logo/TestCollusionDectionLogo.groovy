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
import de.tud.stg.tigerseye.lang.logo.ConcurrentLogo;
import org.javalogo.*;

import de.tud.stg.tigerseye.lang.logo.*;

import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestCollusionDetectionLogo extends TestCase {
	
	def DEBUG = false;
	
	def optimizer;
	
	def concurrentInterpreter;
	
	def flowerProgram = {
			turtle (name:"Teacher",color:red) { 
				penup();
				
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
	              
	              int i = 0;
	              
	              repeat (edges) {
			        forward length
			        
	                //draw outer figure
	                left ((int)(90-(angle/2))) //adjust for peduncle  
	            	forward ((int)(length/4))  //paint peduncle
	                left ((int)(90-(angle/2))) //adjust for blossom           	
	                
	                if (i % 2 == 0) {
		                polygon (((int)(length/6)), edges) //paint blossom
	                } else {
		                polygon (((int)(length/2)), edges) //paint blossom
	                }
	                i++;
	                
	                right ((int)(90-(angle/2)))	    
	            	backward ((int)(length/4))
	                right ((int)(90-(angle/2)))
			        
	                right angle	                
	              }
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
		optimizer = new OptimizingLogo();
		concurrentInterpreter = new ConcurrentLogo();
	}	
	
//	@Test
//	void testCollusionDetectionPainting() {
//		
//		//println ""+Turtle.metaClass
//		
//		def reg = InvokerHelper.metaRegistry
////		def mc = new TurtleMetaClass(Turtle.class) 
////		mc.initialize();
////		reg.setMetaClass(Turtle.class, mc);
//        
//		reg.setMetaClassCreationHandle(new TurtleMetaClassCreationHandle(optimizer,TurtleMetaClassCreationHandle.Strategy.DETECT));
//
////		println ""+Turtle.metaClass
////		Closure old = Turtle.metaClass.&forward;
////		Turtle.metaClass.forward = { int i -> 
////		    println "<>Forward($i)..."
////		    try {
////		    	println "$old args $old.old.maximumNumberOfParameters"
////		        return old.call(i);
////		    } catch (Exception e) {
////		    	println e;
////		    }
////		}
//
//		Closure single = flowerProgram;
//		
//		long startTimeSingle = System.nanoTime();
//		single.delegate = optimizer;
//		single.call();
//		long endTimeSingle = System.nanoTime();
//		if (DEBUG) println "ms for one thread: ${(endTimeSingle-startTimeSingle)/1000000}"
//		
//        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
//		optimizer.waitUntilAllThreadsAreFinished()
//        if (DEBUG) println "all finished"
//        Thread.sleep(10);
//	}	
	
//	@Test
//	void testFrontalCollusion() {
//		Closure program = {
//			turtle (name:"RosePainter",color:green) {
//				fun("polygon") { int length, int edges ->
//		            int angle = (int)(360 / edges)
//		            repeat (edges) {
//				        forward length
//		                right angle
//		            }
//				}				
//				
//				fun("polygon_at") { int x, int y, int length, int edges ->
//					if (y>0) {
//				      fd y
//					} else {
//					  bd (Math.abs(y))
//					}
//				    rt 90
//				    if (x>0) {
//				      fd x
//				    } else {
//				      bd (Math.abs(x))
//				    }
//				    
//				    polygon length, edges
//				}	
//				
//				fun("polygon_to_the_left") { int length, int edges ->
//		            int angle = (int)(360 / edges)
//		            repeat (edges) {
//				        forward length
//		                left angle
//		            }
//			    }				
//			
//			    fun("polygon_to_the_left_at") { int x, int y, int length, int edges ->
//					if (y>0) {
//				      fd y
//					} else {
//					  bd (Math.abs(y))
//					}
//				    lf 90
//				    if (x>0) {
//				      fd x
//				    } else {
//				      bd (Math.abs(x))
//				    }
//				    
//				    polygon length, edges
//			    }
//			}
//			go();
//			
//			turtle (name:"RosePainter2",color:green) {
//				polygon_at (-30,0,30,4)
//				polygon_to_the_left_at (-10,10,60,6)
//				polygon_at (-30,10,20,8)
//				polygon_to_the_left_at 0,-10,50,4
//			}
//			
//			turtle (name:"RosePainter3",color:green) {
//				polygon_to_the_left_at 5,-5,30,4
//				polygon_at 10,35,60,6
//				polygon_at 35,-15,30,8
//				polygon_to_the_left_at (-5,25,50,4)
//			}
//			
//			turtle (name:"RosePainter4",color:green) {	
//				polygon_at (-10,10,60,4)
//				polygon_to_the_left_at 20,-20,30,6
//				polygon_at (-40,-20,60,8)
//				polygon_to_the_left_at (-10,30,20,4)
//				
//							        
//			}
//			
//			go();
//		}
//		
//		def reg = InvokerHelper.metaRegistry
//		reg.setMetaClassCreationHandle(new TurtleMetaClassCreationHandle(optimizer,TurtleMetaClassCreationHandle.Strategy.DETECT));
//
//		
//		program.delegate = optimizer;
//		//program.call();
//		
//        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
//		optimizer.waitUntilAllThreadsAreFinished()
//        if (DEBUG) println "all finished"
//        Thread.sleep(10);
//	}
//	
	@Test
	void testCollusionPreventingPainting() {
		
		def reg = InvokerHelper.metaRegistry
		reg.setMetaClassCreationHandle(new TurtleMetaClassCreationHandle(optimizer,TurtleMetaClassCreationHandle.Strategy.PREVENT));

		Closure single = flowerProgram;
		
		long startTimeSingle = System.nanoTime();
		single.delegate = optimizer;
		single.call();
		long endTimeSingle = System.nanoTime();
		if (DEBUG) println "ms for one thread: ${(endTimeSingle-startTimeSingle)/1000000}"
		
        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
		optimizer.waitUntilAllThreadsAreFinished()
        if (DEBUG) println "all finished"
        Thread.sleep(10);
	}	
	
	@Test
	void testCollusionPreventingPaintingPerClass() {
		//optimizer.startTurtleDrawingAfterDefinition = false;
		
		//Turtle.metaClass = new CollusionPreventingTurtleMetaClass(Turtle.class,optimizer);

		Closure single = flowerProgram;
		
		long startTimeSingle = System.nanoTime();
		single.delegate = optimizer;
		single.call();
		long endTimeSingle = System.nanoTime();
		if (DEBUG) println "ms for one thread: ${(endTimeSingle-startTimeSingle)/1000000}"
		
        //must sleep until all threads have been finished because otherwise the JUnit test would end too early
		//optimizer.waitUntilAllThreadsAreFinished()
        if (DEBUG) println "all finished"
        Thread.sleep(10);
	}		
	
	
	
}