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

import de.tud.stg.popart.aspect.extensions.itd.conflicts.FirstActionConflictResolver;


/**
 * @author Tom Dinkelaker
 *
 */
public class TestLanguageSymbiosys extends TestCase {
	
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
	void testIncompatibleTypes() {
		
		//AspectManager.getInstance().setResolvementStrategy(new FirstActionConflictResolver()); //new to disable conflict detection and activate merging
		
        Closure program = {
			
			aspect(name:"AddKeywordToDSLInterpreterWithITD") {
						    	
				introduce_method(is_type(AbstractInterpretationFunctionalLogo), "forward") { float length ->
				    delegate.forward ((int)length);
				}
				
			}
		
			turtle(name:"TestIncompatibleTypes") {
				
				forward 333/2
				rt 90
				forward 333/2
				rt 90
				forward 333/2
				rt 90
				forward 333/2
				rt 90
				
			}
			
		}
        program.delegate = ccc;
        //program.resolveStrategy = Closure.DELEGATE_FIRST;
        program.call();
        
        Thread.sleep(2000);
		
	}
}