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
public class TestPositionTrackingLogo extends TestCase {
	
	def DEBUG = false;
	
	def interpreter;
	def positionTracker
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
		positionTracker = new PositionTrackingLogo();
	}
	
	/**
	 * Draw the shape (in the closure) using a real turtle and returns the turtle.
	 * @param shapeClosure The closure that contains the shape (as a Logo program).
	 */
	private Turtle getTurtleAfterDrawing(Closure shapeClosure) {
		def cmpInterpreter = new FunctionalLogo();
		shapeClosure.delegate = cmpInterpreter;
		shapeClosure.call();
		def turtle = cmpInterpreter.turtle;
		if (DEBUG) println "expected x : $turtle.position.x";
		if (DEBUG) println "expected y : $turtle.position.y";
		return turtle;
	}

	@Test
	void testPositionTrackingClosedShape() {
		Closure shape = {
			turtle (name:"Figure",color:red) { 
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
		
		shape.delegate = positionTracker;
		shape.call();
		
		if (DEBUG) println "x : $positionTracker.deltaX";
		if (DEBUG) println "y : $positionTracker.deltaY";
		if (DEBUG) println "angle : $positionTracker.deltaAngular";
		assertEquals("wrong x",0.0,positionTracker.deltaX,0.001);
		assertEquals("wrong y",0.0,positionTracker.deltaY,0.001);
		assertEquals("wrong angle",0.0,positionTracker.deltaAngular,0.001);
	}
	
	@Test
	void testPositionTrackingForward() {
		Closure shape = {
			turtle (name:"Command",color:red) { 
		        forward 50
			}
		}
		
		def turtle = getTurtleAfterDrawing(shape);
		
		shape.delegate = positionTracker;
		shape.call();
		
		if (DEBUG) println "x : $positionTracker.deltaX";
		if (DEBUG) println "y : $positionTracker.deltaY";
		if (DEBUG) println "angle : $positionTracker.deltaAngular";
		assertEquals("wrong x",turtle.position.x,positionTracker.deltaX,0.001);
		assertEquals("wrong y",turtle.position.y,positionTracker.deltaY,0.001);
		assertEquals("wrong angle",0.0,positionTracker.deltaAngular,0.001);
	}
	
	@Test
	void testPositionTrackingBackward() {
		Closure shape = {
			turtle (name:"Command",color:red) { 
		        backward 50
			}
		}
		
		def turtle = getTurtleAfterDrawing(shape);
		
		shape.delegate = positionTracker;
		shape.call();
		
		if (DEBUG) println "x : $positionTracker.deltaX";
		if (DEBUG) println "y : $positionTracker.deltaY";
		if (DEBUG) println "angle : $positionTracker.deltaAngular";
		assertEquals("wrong x",turtle.position.x,positionTracker.deltaX,0.001);
		assertEquals("wrong y",turtle.position.y,positionTracker.deltaY,0.001);
		assertEquals("wrong angle",0.0,positionTracker.deltaAngular,0.001);
	}
	
	@Test
	void testPositionTrackingRight() {
		Closure shape = {
			turtle (name:"Command",color:red) { 
		        right 50
			}
		}
		
		def turtle = getTurtleAfterDrawing(shape);
		
		shape.delegate = positionTracker;
		shape.call();
		
		if (DEBUG) println "x : $positionTracker.deltaX";
		if (DEBUG) println "y : $positionTracker.deltaY";
		if (DEBUG) println "angle : $positionTracker.deltaAngular";
		assertEquals("wrong x",turtle.position.x,positionTracker.deltaX,0.001);
		assertEquals("wrong y",turtle.position.y,positionTracker.deltaY,0.001);
		assertEquals("wrong angle",50.0,positionTracker.deltaAngular,0.001);
	}
	
	@Test
	void testPositionTrackingLeft() {
		Closure shape = {
			turtle (name:"Command",color:red) { 
		        left 50
			}
		}
		
		def turtle = getTurtleAfterDrawing(shape);
		
		shape.delegate = positionTracker;
		shape.call();
		
		if (DEBUG) println "x : $positionTracker.deltaX";
		if (DEBUG) println "y : $positionTracker.deltaY";
		if (DEBUG) println "angle : $positionTracker.deltaAngular";
		assertEquals("wrong x",turtle.position.x,positionTracker.deltaX,0.001);
		assertEquals("wrong y",turtle.position.y,positionTracker.deltaY,0.001);
		assertEquals("wrong angle",310.0,positionTracker.deltaAngular,0.001);
	}
		
	@Test
	void testPositionTrackingDiagonal() {
		Closure shape = {
			turtle (name:"Command",color:red) { 
		        right 45
		        forward 100
			}
		}
		
		shape.delegate = positionTracker;
		shape.call();
		
		if (DEBUG) println "x : $positionTracker.deltaX";
		if (DEBUG) println "y : $positionTracker.deltaY";
		if (DEBUG) println "angle : $positionTracker.deltaAngular";
		assertEquals("wrong x",Math.sqrt((100.0*100.0)/2),positionTracker.deltaX,0.001);
		assertEquals("wrong y",Math.sqrt((100.0*100.0)/2),positionTracker.deltaY,0.001);
		assertEquals("wrong angle",45.0,positionTracker.deltaAngular,0.001);
	}
	
	@Test
	void testPositionTrackingFunction() {
		Closure shape = {
	        right 45
			
	        turtle (name:"Command",color:red) {
	            fun("diagonal") { 
		            forward 50
				}

	        }
			
            diagonal()
            diagonal()
			
		}
		
		shape.delegate = positionTracker;
		shape.call();
		
		if (DEBUG) println "x : $positionTracker.deltaX";
		if (DEBUG) println "y : $positionTracker.deltaY";
		if (DEBUG) println "angle : $positionTracker.deltaAngular";
		assertEquals("wrong x",Math.sqrt((100.0*100.0)/2),positionTracker.deltaX,0.001);
		assertEquals("wrong y",Math.sqrt((100.0*100.0)/2),positionTracker.deltaY,0.001);
		assertEquals("wrong angle",45.0,positionTracker.deltaAngular,0.001);
		
	}
	
	@Test
	void testPositionTrackingFunctionWithParam() {
		Closure shape = {
			turtle ([:]) { right 45 }
			
	        turtle (name:"Command",color:red) {
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
			
	        turtle ([:]) { 
	        	square 10
	            square 10
	        }
		}
		
		def turtle = getTurtleAfterDrawing(shape);
		
		shape.delegate = positionTracker;
		shape.call();
		
		if (DEBUG) println "x : $positionTracker.deltaX";
		if (DEBUG) println "y : $positionTracker.deltaY";
		if (DEBUG) println "angle : $positionTracker.deltaAngular";
		if (DEBUG) println "heading : $turtle.heading";
		assertEquals("wrong x",turtle.position.x,positionTracker.deltaX,0.001);
		assertEquals("wrong y",turtle.position.y,positionTracker.deltaY,0.001);
		assertEquals("wrong angle",45.0,positionTracker.deltaAngular,0.001);
	}

//	@Test
//	void testPositionTrackingUnclosedShape() {
//		Closure shape = {
//			
//	        turtle (name:"Command",color:red) {
//		        right 45
//
//		        fun("ushape") { int length ->
//			        forward length
//			        right 90
//			        forward length
//			        right 90
//			        forward length
//			        right 90
//				}
//
//		        ushape 10
//	        }
//		}
//
//		def turtle = getTurtleAfterDrawing(shape);
//		if (DEBUG || true) println "turtle : $turtle";
//		if (DEBUG || true) println "angle : $turtle.angularUnits";
//		if (DEBUG || true) println "heading : $turtle.heading";
//		if (DEBUG || true) println "position : $turtle.position";
//		
//		shape.delegate = positionTracker;
//		shape.call();
//		
//		if (DEBUG) println "x : $positionTracker.deltaX";
//		if (DEBUG) println "y : $positionTracker.deltaY";
//		if (DEBUG) println "angle : $positionTracker.deltaAngular";
//		assertEquals("wrong x",turtle.position.x,positionTracker.deltaX,0.001);
//		assertEquals("wrong y",turtle.position.y,positionTracker.deltaY,0.001);
//		assertEquals("wrong angle",315.0,positionTracker.deltaAngular,0.001);
//	}
	
	@Test
	void testPositionTrackingAnglaur() {
		Closure shape1 = { 
	        turtle (name:"Command") {
		        repeat (1) {
		        	right 45
		        }
	        }
		}
		
		Closure shape2 = { 
		        turtle (name:"Command") {
			        repeat (2) {
			        	right 45
			        }
		        }
			}
		
		Closure shape3 = { 
		        turtle (name:"Command") {
			        repeat (3) {
			        	right 45
			        }
		        }
			}

		def turtle = getTurtleAfterDrawing(shape1);
		if (DEBUG || true) println "turtle : $turtle";
		if (DEBUG || true) println "angle : $turtle.angularUnits";
		if (DEBUG || true) println "heading : $turtle.heading";
		if (DEBUG || true) println "position : $turtle.position";
		
		turtle = getTurtleAfterDrawing(shape2);
		if (DEBUG || true) println "turtle : $turtle";
		if (DEBUG || true) println "angle : $turtle.angularUnits";
		if (DEBUG || true) println "heading : $turtle.heading";
		if (DEBUG || true) println "position : $turtle.position";

		turtle = getTurtleAfterDrawing(shape3);
		if (DEBUG || true) println "turtle : $turtle";
		if (DEBUG || true) println "angle : $turtle.angularUnits";
		if (DEBUG || true) println "heading : $turtle.heading";
		if (DEBUG || true) println "position : $turtle.position";
    }
	
}