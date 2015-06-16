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

import de.tud.stg.tigerseye.LinearizingCombiner;
import de.tud.stg.tigerseye.lang.logo.CompleteLogo;
import de.tud.stg.popart.dslsupport.logo.FunctionalLogo;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

import de.tud.stg.tigerseye.*;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestPluggableScoping extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception {
	}
	
	@Test
	void testLogoDynamicScopingVerbose() {
		System.out.println("testLogoDynamicScopingVerbose");
	    
		Closure cl = {
			turtle(name:"Teacher", color:red) { 
				define(name:"f1") { 
   				    let("length",50);
				    define(name:"complexShape") { 
				    	assert length == 100 //  <---- With dynamic scoping the lexically enclosed variable binding is used
			    	    fd get("length"); rt 120; fd get("length"); rt 120; fd get("length"); rt 120;
			        }
				}
				f1();
			} 

			turtle(name:"Pupil", color:red) { 
				define(name:"f2") { 
		    	    let("length",100);
		    	    apply("complexShape")();
				}
				f2();
            }
	    }
		EnvironmentInterpreter functional = new Functional();
		functional.scopingStrategy = EnvironmentInterpreter.DYNAMIC_SCOPING;
		cl.delegate = new LinearizingCombiner(functional, new CompleteLogo());
		def result;
		try {
		    result = cl.call();
		} catch (MissingPropertyException mpe) {
			//ignore since correctly 
			throw mpe
		}
	    //Thread.sleep(3000);
	}
	
	@Test
	void testLogoDynamicScopingShort() {
		System.out.println("testLogoDynamicScoping");
	    
		Closure cl = {
			turtle(name:"Teacher", color:red) { 
				define(name:"f1") { 
   				    length=50
				    define(name:"complexShape") { 
				    	assert length == 100 //  <---- With dynamic scoping the lexically enclosed variable binding is used
			    	    fd length; rt 120; fd length; rt 120; fd length; rt 120;
			        }
				}
				f1();
			} 

			turtle(name:"Pupil", color:red) { 
				define(name:"f2") { 
		    	    length=100
		    	    apply("complexShape")();
				}
				f2();
            }
	    }
		EnvironmentInterpreter functional = new Functional();
		functional.scopingStrategy = EnvironmentInterpreter.DYNAMIC_SCOPING;
		cl.delegate = new LinearizingCombiner(functional, new CompleteLogo());
		def result;
		try {
		    result = cl.call();
		} catch (MissingPropertyException mpe) {
			//ignore since correctly 
			throw mpe
		}
	    //Thread.sleep(3000);
	}	
	
	@Test
	void testLogoLexicalScoping() {
		System.out.println("testLogoLexicalScoping");
	    
		Closure cl = {
			turtle(name:"Teacher", color:red) { 
				define(name:"f1") { 
   				    let("length",50);
				    define(name:"complexShape") { 
				    	assert length == 50 //  <---- With lexical scoping the global variable binding is used
			    	    fd length; rt 120; fd length; rt 120; fd length; rt 120;
			        }
				}
				f1();
			} 

			turtle(name:"Pupil", color:red) { 
				define(name:"f2") { 
		    	    let("length",100);
		    	    apply("complexShape")();
				}
				f2();
            }
	    }
		EnvironmentInterpreter functional = new Functional();
		functional.scopingStrategy = EnvironmentInterpreter.LEXICAL_SCOPING;
		cl.delegate = new LinearizingCombiner(functional, new CompleteLogo());
		def result;
		try {
		    result = cl.call();
		} catch (MissingPropertyException mpe) {
			//ignore since correctly 
			throw mpe
		}
	    //Thread.sleep(3000);
	}
	
	
	@Test
	void testImplicitReferences() {
		System.out.println("----");
		System.out.println("testImplicitReferences");
	    
		Closure program = {
			define(name:"print") { str ->
			  println str
			} 

			define(name:"colorToStr") { color ->
			  println color
			} 

			turtle(name:"Turtle1",color:red) {
			  apply("print")(thisTurtle.name) 
			}

			turtle(name:"Turtle2",color:blue) {
			  apply("print")(apply("colorToStr")(color)) 
			}
	    }
		CompleteLogo logo = new CompleteLogo();
		LogoImplicitReference lir = new LogoImplicitReference(logo);
		Functional functional = new Functional();
		program.delegate = new LinearizingCombiner(lir,functional,logo);
		program.call();
	    //Thread.sleep(3000);
	}
			
}
