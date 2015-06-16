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

import de.tud.stg.popart.aspect.Aspect 
import de.tud.stg.popart.aspect.AspectManager 
import de.tud.stg.popart.aspect.extensions.itd.ITDCCCombiner 
import de.tud.stg.popart.aspect.CCCombiner 
import de.tud.stg.popart.aspect.extensions.Booter 
import de.tud.stg.tigerseye.lang.logo.dspcl.LogoPointcutInterpreter 

/**
 * @author Tom Dinkelaker
 */
public class TestComposabilityWorking extends TestCase {
	
	DSL interpreter;
	DSL completeLogo;
	
	def dsl;
	def ccc;

	Closure aspect;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		//completeLogo = new UCBLogo();
		interpreter = new BlackBoxCombiner(new UCBLogo(),new Functional());
		
		//aspects
		if (!Booter.aspectSystemInitialized) Booter.initialize();
	    
		dsl = new TracingLogo();
	    def pcl = LogoPointcutInterpreter.getInstance();
	    def advl = dsl;

	    ccc = new CCCombiner(new HashSet([advl,pcl]));
	    
	    AspectManager.instance.unregisterAllAspects();
	    
	    aspect = { HashMap params, Closure body ->
	        Aspect result = ccc.eval(params,body);
	        AspectManager.getInstance().register(result);
	        return result;
	    }
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
	void testScenarioComposingLanguageExtensions() {
		DSL dsl = new LinearizingCombiner(new UCBLogo(),new ConciseLogo());

		Closure program = {
	    	// A DSL PROGRAM - BEGIN
	    	
			turtle(name:"Square", color:red) {
			    repeat (4) {
				    fd 50
				    rt 90
			    }
			}
			
	    	// A DSL PROGRAM - END
	    }
	    
	    program.delegate = dsl;
	    program.call();

	    //Thread.currentThread().sleep(3000);
	}
	
	@Test
	void testScenarioComposingStandAloneLanguages_FunctionalStandAlone() {
		DSL dsl = new Functional();

		Closure program = {
	    	// A DSL PROGRAM - BEGIN
	    	
			define(name:"pow2") { x -> 
			    x * x
            }
			def result = apply("pow2")(10)
			assert result == 100

	    	// A DSL PROGRAM - END
	    }
	    
	    program.delegate = dsl;
	    program.call();

	    //Thread.currentThread().sleep(3000);
	}
	@Test
	void testScenarioComposingStandAloneLanguages() {
        Closure program = {
            define(name:"square") { length -> 
                repeat (4) {
                    forward length 
                    right 90 
                }
            }

            turtle(name:"TwoSquares", color:red) { 
                setpencolor blue
                apply("square")(50) 
                right 180
                setpencolor green
                apply("square")(100) 
            }
        }
		
		program.delegate = interpreter;
		program.call();
        //Thread.sleep(3000);
	}	
	
	@Test
	void testScenarioComposingValueDomains() {
        Closure program = {
		    define(name:"square") { length -> 
			    turtle(name:"square",color:red) {
				    repeat (4) {
				        fd length
                        rt 90 
                    }
                }
            } 

            define(name:"triangle") { length -> 
                turtle(name:"triangle",color:red) {
                    repeat (3) {
                        lt 120 
                        bd length
                    }
                }
            } 

            define(name:"sequence") { first, second, length -> 
                apply(first)(length)
                apply(second)(length)
            } 

            i = 50
            repeat (4) {
                apply("sequence")("square","triangle",i); 
                i = i + 50
            }
        }
		
		program.delegate = interpreter;
		program.call();
        //Thread.sleep(3000);
	}	

	@Test
	void testScenarioCrosscuttingComposition_ScatteredTracing() {
		println "========================================="
		println "Tracing without aspect"
		println "-----------------------------------------"

		DSL dsl = new TracingLogo();
	    dsl.eval {
	    	// A DSL PROGRAM - BEGIN
	        	
	    	fun("polygon") { length, edges ->
		        show "begin of evaluation of function 'polygon'"
		        trace()
		
   		        def i = 1;
		        repeat (edges) {
		            show "begin of "+i+"-th iteration of repeat"
		            trace()
		
		            show "forward "+length
		            forward length
		            trace()
		
		            show "right "+Math.round(360/edges)
		            right ((int)(360/edges))
		            trace()
		
		            show "end of "+i+"-th iteration of repeat"
		            trace()
		            i = i+1
		        } 
		
		        show "end of evaluation of function 'polygon'"
		        trace()
	        }
		
		
		    turtle(name:"Octagon",color:green) {
		        show "begin of evaluation of turtle 'Octagon'"
		        trace()
		
		        app("polygon") (25, 8)
		
		        show "begin of evaluation of turtle 'Octagon'"
		        trace()
		    }
		   
	    	// A DSL PROGRAM - END
	    }

	    println "========================================="
	    //Thread.currentThread().sleep(3000);
	}
	
	@Test
	void testScenarioCrosscuttingComposition_TracingAspect() {

		println "\n\n"
		println "========================================="
		println "Tracing with aspect"
		println "-----------------------------------------"

//		if (!Booter.aspectSystemInitialized) Booter.initialize();
//	    
//		DSL dsl = new TracingLogo();
//	    def pcl = LogoPointcutInterpreter.getInstance();
//	    def advl = dsl;
//
//	    def ccc = new CCCombiner([advl,pcl]);
//	    
//	    AspectManager.instance.unregisterAllAspects();
//	    
//	    def aspect = { HashMap params, Closure body ->
//	        Aspect result = ccc.eval(params,body);
//	        AspectManager.getInstance().register(result);
//	        return result;
//	    }

		ccc.eval {
	    	// A DSL PROGRAM - BEGIN
	        	
	    	this.aspect(name:"TracingAspect") { 
//	    		before (papply()) { show "begin of evaluation of function '$name'"; trace(); }
//
//	    		after (papply()) { show "end of evaluation of function '$name'"; trace(); }
//	    		  
//	    		before (prepeat()) { show "begin of $iteration-th iteration of (*@repeat@*)"; trace(); }
//	    		  
//	    		after (prepeat()) { show "end of $iteration-th iteration of (*@repeat@*)"; trace(); }

	    		before (pmotion()) { 
	    			show "${thisJoinPoint.command} ${thisJoinPoint.args}"; trace(); 
	    		}

	    		after (pmotion()) { 
	    			show "${thisJoinPoint.command} ${thisJoinPoint.args}"; trace(); 
	    		}
	    		  
//	    		before (pturtle()) { show "begin of evaluation of turtle '$name'"; trace(); }
//
//	    		after (pturtle()) { show "end of evaluation of turtle '$name'"; trace(); }
	    	}
		
	    	fun("polygon") { length, edges ->
	    	    repeat (edges) {
	    		    forward length 
	    		    right ((int)(360/edges))
	    		}
	    	}

	    	turtle(name:"Octagon",color:green) {
	    	    app("polygon")(25, 8)
	    	}
	    		
	    	// A DSL PROGRAM - END
	    }
		println "========================================="

	    Thread.currentThread().sleep(3000);
	}
}
