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
import de.tud.stg.popart.aspect.extensions.itd.ITDCCCombiner;

import de.tud.stg.tigerseye.lang.logo.*;
import de.tud.stg.tigerseye.lang.logo.exceptions.OutOfCanvasException; 
import de.tud.stg.tigerseye.lang.logo.dspcl.LogoPointcutInterpreter;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestInterpreterLevelAspects extends TestCase {
	
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

	    def ccc = new ITDCCCombiner(new LinkedHashSet([advl,pcl]));
	    
	    AspectManager.instance.unregisterAllAspects();
	    
	    aspect = { HashMap params, Closure body ->
	        Aspect result = ccc.eval(params,body);
	        AspectManager.getInstance().register(result);
	        return result;
	    }
	    

	}

	@Test
	void testAddKeywordToDSLInterpreterWithITD() {
		
		int i = 0;

		dsl.eval(name:"Square") {
			
			aspect(name:"AddKeywordToDSLInterpreterWithITD") {
						    	
				//This ITD adds the keyword method "vorwaerts150" to the DSL interpreter class "TurtleDSL"
				//effectively this chnages the syntax of a language
				introduce_method(is_type(TurtleDSL), "vorwaerts150") { 
					delegate.forward(150);
				}
				
				before(pmotion()) {
					println "motion inc i";
					i++;
				}
			}
		
			vorwaerts150()
		    forward 50
		    right 90
		    forward 50
		    right 90
		    vorwaerts150()
		    forward 50
		    right 90
		    forward 50
		    right 90
		}
		
		assertEquals(10,i);
	}
	
	@Test
	void testAddKeywordsWithParameterToDSLInterpreterWithITD() {
		
		int i = 0;

		dsl.eval(name:"Square") {
			
			aspect(name:"AddKeywordsWithParameterToDSLInterpreterWithITD") {
						    	
				//This ITD adds 
				introduce_method(is_type(TurtleDSL), "vorwaerts") { n -> 
					delegate.forward(n);
				}
				
				introduce_method(is_type(TurtleDSL), "rueckwaerts") { n -> 
				    delegate.backward(n);
		        }
				
				introduce_method(is_type(TurtleDSL), "links") { a -> 
				    delegate.left(a);
		        }
				
				introduce_method(is_type(TurtleDSL), "rechts") { a -> 
				    delegate.right(a);
		        }
		
				before(pmotion()) {
					println "motion inc i";
					i++;
				}
			}
		
		    vorwaerts 50
		    rechts 90
		    vorwaerts 50
		    rechts 90
		    vorwaerts 50
		    rechts 90
		    vorwaerts 50
		    rechts 90
		}
		
		assertEquals(8,i);
	}	
	
	@Test
	void testChangeKeywordOfDSLInterpreter() {
		
		dsl.eval(name:"Square") {
			
			def keywordChanger = aspect(name:"ChangeKeywordOfDSLInterpreter",deployed:false) {
				
				around(is_type(TurtleDSL) & method_execution("forward")) { 
					delegate.backward(args[0]);
				}
			}
		
			setpencolor green
		    forward 50
		    right 90
		    forward 50
		    right 90
		    forward 50
		    right 90
		    forward 50
		    right 90

		    keywordChanger.deploy();
		    
			setpencolor red
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
	
	@Test
	void testChangeKeywordOfDSLInterface() {
		
		dsl.eval(name:"Square") {
			
			def keywordChanger = aspect(name:"ChangeKeywordOfDSLInterface",deployed:false) {
				
				around(is_type(IConciseLogo) & method_execution("forward")) { 
					delegate.backward(args[0]);
				}
			}
		
			setpencolor green
		    forward 50
		    right 90
		    forward 50
		    right 90
		    forward 50
		    right 90
		    forward 50
		    right 90

		    keywordChanger.deploy();
		    
			setpencolor red+yellow
		    forward 50
		    right 90
		    forward 50
		    right 90
		    forward 50
		    right 90
		    forward 50
		    right 90
		}

		Thread.sleep(1000);
	}

}