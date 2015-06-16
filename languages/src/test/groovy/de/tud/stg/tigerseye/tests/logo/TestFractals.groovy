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
import de.tud.stg.tigerseye.lang.logo.analysis.AbstractInterpretationFunctionalLogo;
import de.tud.stg.tigerseye.lang.logo.analysis.TotalAbstractInterpretation;
import de.tud.stg.tigerseye.lang.logo.dspcl.LogoPointcutInterpreter;
import de.tud.stg.tigerseye.BlackBoxCombiner;


/**
 * @author Tom Dinkelaker
 *
 */
public class TestFractals extends TestCase {
	
	def ccc;
	def dsl;
	def concreteInterpreter;
	def abstractInterpreter;
	
	Closure aspect;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		if (!Booter.aspectSystemInitialized) Booter.initialize();
	    
		concreteInterpreter = new FunctionalLogo();
		abstractInterpreter = new AbstractInterpretationFunctionalLogo();
		
		dsl = abstractInterpreter;
	    def pcl = LogoPointcutInterpreter.getInstance();
	    def advl = abstractInterpreter;

	    ccc = new ITDCCCombiner(new LinkedHashSet([advl,pcl]));
	    
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

        Closure program = {
			
			aspect(name:"AddKeywordToDSLInterpreterWithITD") {
						    	
				introduce_method(is_type(AbstractInterpretationFunctionalLogo), "vorwaerts150") { 
					delegate.forward(150);
					println "vorwaerts150";
				}
				
			}
		
			turtle(name:"Square") {

				
			    forward 50
			    right 90
			    
			}
		}
        program.delegate = ccc;
        program.resolveStrategy = Closure.DELEGATE_FIRST;
        program.call();
		
	}

	@Test
	void testFractal() {
		//by Lewis Pike from http://www.mathemagic.org/MOBM/tree.html
		
        Closure program = {
			
//			aspect(name:"AddKeywordToDSLInterpreterWithITD") {
//						    	
//				introduce_method(is_type(AbstractInterpretationFunctionalLogo), "vorwaerts150") { 
//					delegate.forward(150);
//					println "vorwaerts150";
//				}
//				
//			}
		
			turtle(name:"Fractal") {
				fun("tree") { int number, int length ->
				  //cs()
				  if (length>256) { print "Please choose a length between 4 and 256"; System.exit(1); }
				  if (length<4) { print "Please choose a length between 4 and 256"; System.exit(1); }
				  if (number<2) { print "Please choose a number between 2 and 4"; System.exit(1); }
				  if (number>4 ) { print "Please choose a number between 2 and 4"; System.exit(1); }
				  //setpensize 3, 3
				  setpencolor (java.awt.Color.BLUE.value)
				  if (number==2) {
					app("tree2") length
				  }
				  else { 
					if (number==3) {
					  app("tree3") length 
					} else 
					  app("tree4") length
				  }
				}
				 
				fun("tree2") { int length ->
				  if (length<2) return
				  lt 45
				  forward length
				  app("tree2") ((int)(length/2))
				  backward length
				  rt 90
				  forward length
				  app("tree2") ((int)(length/2))
				  backward length
				  left 45
				}
				 
				fun("tree3") { int length ->
				  if (length<2) return
				  lt 45
				  forward length
				  app("tree3") ((int)(length/2))
				  backward length
				  rt 45
				  forward length
				  app("tree4") ((int)(length/2))
				  backward length
				  rt 45
				  forward length
				  app("tree3") ((int)(length/2))
				  backward length
				  lt 45
				}
				
				fun("tree4") { int length -> 
				  if (length<2) return
				  lt 45
				  forward length
				  app("tree4") ((int)(length/2))
				  backward length
				  rt 30
				  forward length
				  app("tree4") ((int)(length/2))
				  backward length
				  rt 30
				  forward length
				  app("tree4") ((int)(length/2))
				  backward length
				  rt 30
				  forward length
				  app("tree4") ((int)(length/2))
				  backward length
				  lt 45
				}
			}
			
			println "before"
			app("tree")(2,50);
			println "after"
		}
        program.delegate = concreteInterpreter;
        //program.resolveStrategy = Closure.DELEGATE_FIRST;
        program.call();
        
        Thread.sleep(2000);
		
	}
}