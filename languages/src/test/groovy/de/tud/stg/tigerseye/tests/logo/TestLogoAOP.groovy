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

import de.tud.stg.popart.aspect.Aspect;
import de.tud.stg.popart.aspect.AspectManager;
import de.tud.stg.popart.aspect.extensions.Booter;
import de.tud.stg.popart.aspect.CCCombiner;

import de.tud.stg.tigerseye.lang.logo.*;
import de.tud.stg.tigerseye.lang.logo.exceptions.OutOfCanvasException; 
import de.tud.stg.tigerseye.lang.logo.dspcl.LogoPointcutInterpreter;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestLogoAOP extends TestCase {
	
	def dsl;

	Closure aspect;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
	    if (!Booter.aspectSystemInitialized) Booter.initialize();
	    
		dsl = new TurtleDSL();
	    def pcl = LogoPointcutInterpreter.getInstance();
	    def advl = dsl;

	    def ccc = new CCCombiner([advl,pcl]);
	    
	    AspectManager.instance.unregisterAllAspects();
	    
	    aspect = { HashMap params, Closure body ->
	        Aspect result = ccc.eval(params,body);
	        AspectManager.getInstance().register(result);
	        return result;
	    }
	}

	@Test
	void testLogMotions() {
		
		int i = 0;

		dsl.eval(name:"Square") {
			
			aspect(name:"Logger") {
				before(pmotion()) {
					println "motion inc i";
					i++;
				}
			}
		
		    forward 50
		    right 90
		    forward 50
		    right 90
		    forward 50
		    right 90
		    forward 50
		    right 90
		}
		
		assertEquals(8,i);
	}
	
	/**
	 * Tests each pointcut designator;
	 */
	@Test
	void testLogMotionTypes() {
		int motions = 0;
		int moves = 0;
		int turns = 0;
		int forwardMoves = 0; 
		int backwardMoves = 0; 
		int leftTurns = 0; 
		int rightTurns = 0; 
		
		dsl.eval(name:"Square") {

			aspect(name:"LoggerUsingAllPCDs") {
				
				before(pmotion()) {
					println "motion"
					motions++;
				}
				
				before(pmoving()) {
					println "moving";
					moves++;
				}
				
				before(pturning()) {
					println "turning";
					turns++;
				}
				
				before(pforward()) {
					println "forward";
					forwardMoves++;
				}

				before(pbackward()) {
					println "backward";
					backwardMoves++;
				}

				before(pright()) {
					println "right";
					rightTurns++;
				}

				before(pleft()) {
					println "left";
					leftTurns++;
				}

			}
		
			forward 50
			right 90
			forward 50
			right 90
			forward 50
			right 90
			forward 50
			right 90
		    
			backward 25
			left 90
			backward 25
			left 90
			backward 25
			left 90
			backward 25
			left 90
		        
		}

		assertEquals(16,motions);
		assertEquals(8,moves);
		assertEquals(8,turns);
		assertEquals(4,forwardMoves);
		assertEquals(4,backwardMoves);
		assertEquals(4,leftTurns);
		assertEquals(4,rightTurns);
	}
	
	@Test
	void testDomainSpecificConstraints() {
		
		int i = 0;

		try {
			dsl.eval(name:"Square") {
				
				aspect(name:"Logger") {
					after(pmoving()) {
						println "move";
						println "${dsl.canvas}"
						println "$thisJoinPoint.positionX"
						if ((!(thisJoinPoint.positionX in -250..250)) || 
						 (!(thisJoinPoint.positionY in -250..250))) {
							 throw new OutOfCanvasException("Oops, I have painted outsize the canvas ($thisJoinPoint.positionX,$thisJoinPoint.positionY)")
						 }
					}
				}
			
			    forward 50
			    right 90 
			    forward 500
			    right 90
			    forward 50
			    right 90
			    forward 500
			    right 90
			}
			fail("This line should not be reached as it should be detected that the turtle paints out of canvas.");
		} catch (OutOfCanvasException oce) {
			System.out.println("Delected correctly an error in the turtle program: "+oce);
		    //ignore exception as it has correctly been detected that the turtle paints of of the canvas
		} catch (Exception e) {
			fail("Caught another error than OutOfCanvasException: "+e);
		}
		
		//Thread.sleep(1000);
	}	
	
	

}