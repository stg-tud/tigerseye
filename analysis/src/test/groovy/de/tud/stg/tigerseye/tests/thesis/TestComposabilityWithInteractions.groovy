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
import de.tud.stg.tigerseye.exceptions.*;

import de.tud.stg.popart.aspect.Aspect 
import de.tud.stg.popart.aspect.AspectManager 
import de.tud.stg.popart.aspect.CCCombiner 
import de.tud.stg.popart.aspect.extensions.Booter 
import de.tud.stg.tigerseye.lang.logo.dspcl.LogoPointcutInterpreter; 


/**
 * @author Tom Dinkelaker
 *
 */
public class TestComposabilityWithInteractions extends TestCase {
	
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
		println("======================================");
		//completeLogo = new UCBLogo();
		interpreter = new BlackBoxCombiner(new UCBLogo(),new Functional());
		
//TODO: Side effect between Combiners and Aspects (Moved to own suite)
//		//aspects
//		if (!Booter.aspectSystemInitialized) Booter.initialize();
//	    
//		dsl = new TracingLogo();
//	    def pcl = LogoPointcutInterpreter.getInstance();
//	    def advl = dsl;
//
//	    ccc = new CCCombiner(new HashSet([advl,pcl]));
//	    
//	    AspectManager.instance.unregisterAllAspects();
//	    
//	    aspect = { HashMap params, Closure body ->
//	        Aspect result = ccc.eval(params,body);
//	        AspectManager.getInstance().register(result);
//	        return result;
//	    }
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
	void testScenarioFunctionWithStructuralWithBBC() {
		System.out.println("testScenarioFunctionWithStructuralWithBBC");
		DSL structral = new Structural();
		DSL functional = new Functional();
		
		try {
		    DSL dsl = new BlackBoxCombiner(functional,structral);
		    assert (false == "BlackBoxCombiner did not detect the conflict between Structural and Functional")
		} catch (SyntaxConflictException ex) {
		    println "BlackBoxCombiner correctly detected the conflict between Structural and Functional "
	    } 

		    
	}
	
	@Test
	void testScenarioFunctionWithStructuralWithInterpeterCombiner() {
		System.out.println("testScenarioFunctionWithStructuralWithInterpeterCombiner");
		DSL structural = new Structural();
		DSL functional = new Functional();
		
		DSL dsl = new InterpreterCombiner(functional,structural);

        Closure cl = {
        	define(name:"tuple"){ 
        		x = Integer
        		y = Integer
        	}
        		
        	init(name:"tuple",x:3,y:5);
        }
        
        cl.delegate = dsl;
        def result;
        try {
            result = cl.call();
		    assert (false == "InterpeterCombiner did not detect the conflict between Structural and Functional")
		} catch (RuntimeException ex) {
			if (SyntaxConflictException.isInstance(ex.getCause())) println "InterpeterCombiner correctly detected the conflict between Structural and Functional "
	    } 
        println result;
	}
	
	@Test
	void testScenarioFunctionWithStructuralWithRenamingCombiner() {
		//this is a correct resolution
		System.out.println("testScenarioFunctionWithStructuralWithRenamingCombiner");
		DSL structural = new Structural();
		DSL functional = new Functional();
		
		DSL dsl = new MyRenamingCombiner(functional,structural);

        Closure cl = {
        	make(name:"tuple"){ 
        		x = Integer
        		y = Integer
        	}
        		
        	init(name:"tuple",x:3,y:5);
        }
        cl.delegate = dsl;
        println cl.call();
        
        
        Closure cl2 = {
            defun(name:"square"){ x ->
        		x * x
        	}
        		
        	return apply("square")(5);        
        }
        cl2.delegate = dsl;
        println cl2.call();
	}

	@Test
	void testScenarioFunctionWithStructuralWithRenamingCombinerInOneProgram() {
		//this is a correct resolution
		System.out.println("testScenarioFunctionWithStructuralWithRenamingCombinerInOneProgram");
		DSL structural = new Structural();
		DSL functional = new Functional();
		
		DSL dsl = new MyRenamingCombiner(functional,structural);

        Closure cl = {
        	make(name:"tuple"){ 
        		x = Integer
        		y = Integer
        	}
        		
        	def t1 = init(name:"tuple",x:3,y:5);
        
            defun(name:"tupleSquare"){ tuple ->
      		  def squaredTuple = init(name:"tuple",x:0,y:0);
      		  squaredTuple.x = tuple.x * tuple.x;
      		  squaredTuple.y = tuple.y * tuple.y;
    		  return squaredTuple;
        	}
        		
        	return apply("tupleSquare")(t1);        
        }
        cl.delegate = dsl;
        println cl.call();
	}
	
	@Test
	void testScenarioFunctionWithStructuralWithLinearizing() {
		//this is not a correct resolution, see testScenarioFunctionWithStructuralWithRenamingCombiner
		System.out.println("testScenarioFunctionWithStructuralWithLinearizing");
		DSL structural = new Structural();
		DSL functional = new Functional();
		
		DSL dsl = new LinearizingCombiner(functional,structural);

        Closure cl = {
        	define(name:"tuple"){ 
        		x = Integer
        		y = Integer
        	}
        		
        	init(name:"tuple",x:3,y:5);
        }
        
        cl.delegate = dsl;
        def result = cl.call();
        println result;
	}
	
	@Test
	void testScenarioFunctionWithStructuralWithSemanticConflict() {
		
		System.out.println("testScenarioFunctionWithStructuralWithSemanticConflict");
		DSL structural = new Structural();
//		DSL functional = new Functional();
//		
//		DSL dsl = //new MySemanticAdaptationCombiner(functional,structural);

        Closure cl = {
        	define(name:"tuple"){ 
        		x = Integer
        		y = Integer
        	}
        		
        	def t1 = tuple(x:3,y:5);
        
        	return (t1);        
        }
        cl.delegate = structural;
        println cl.call();
	}	
	
	@Test
	void testScenarioFunctionWithStructuralWithSemanticConflictInOneProgramButResolved() {
		//this is a correct resolution
		System.out.println("testScenarioFunctionWithStructuralWithSemanticConflictInOneProgram");
		DSL structural = new Structural();
		DSL functional = new Functional();
		
		DSL dsl = new MySemanticAdaptationCombiner(functional,structural);

        Closure cl = {
        	make(name:"tuple"){ 
        		x = Integer
        		y = Integer
        	}
        		
        	def t1 = tuple(x:3,y:5);
        
            defun(name:"tupleSquare"){ old ->
      		  def squaredTuple = tuple(x:0,y:0);
      		  squaredTuple.x = old.x * old.x;
      		  squaredTuple.y = old.y * old.y;
    		  return squaredTuple;
        	}
        		
        	return apply("tupleSquare")(t1);        
        }
        cl.delegate = dsl;
        println cl.call();
	}	
	
	@Test
	void testScenarioComposingContextSensitiveWithoutError() {
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
		
		program.delegate = new BlackBoxCombiner(new UCBLogo(),new Functional());
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
	
//	@Test
//	void testScenarioCrosscuttingComposition_TracingAspect() {
//
//		println "\n\n"
//		println "========================================="
//		println "Tracing with aspect"
//		println "-----------------------------------------"
//
////		if (!Booter.aspectSystemInitialized) Booter.initialize();
////	    
////		DSL dsl = new TracingLogo();
////	    def pcl = LogoPointcutInterpreter.getInstance();
////	    def advl = dsl;
////
////	    def ccc = new CCCombiner([advl,pcl]);
////	    
////	    AspectManager.instance.unregisterAllAspects();
////	    
////	    def aspect = { HashMap params, Closure body ->
////	        Aspect result = ccc.eval(params,body);
////	        AspectManager.getInstance().register(result);
////	        return result;
////	    }
//
//		ccc.eval {
//	    	// A DSL PROGRAM - BEGIN
//	        	
//	    	this.aspect(name:"TracingAspect") { 
////	    		before (papply()) { show "begin of evaluation of function '$name'"; trace(); }
////
////	    		after (papply()) { show "end of evaluation of function '$name'"; trace(); }
////	    		  
////	    		before (prepeat()) { show "begin of $iteration-th iteration of (*@repeat@*)"; trace(); }
////	    		  
////	    		after (prepeat()) { show "end of $iteration-th iteration of (*@repeat@*)"; trace(); }
//
//	    		before (pmotion()) { 
//	    			show "${thisJoinPoint.command} ${thisJoinPoint.args}"; trace(); 
//	    		}
//
//	    		after (pmotion()) { 
//	    			show "${thisJoinPoint.command} ${thisJoinPoint.args}"; trace(); 
//	    		}
//	    		  
////	    		before (pturtle()) { show "begin of evaluation of turtle '$name'"; trace(); }
////
////	    		after (pturtle()) { show "end of evaluation of turtle '$name'"; trace(); }
//	    	}
//		
//	    	fun("polygon") { length, edges ->
//	    	    repeat (edges) {
//	    		    forward length 
//	    		    right ((int)(360/edges))
//	    		}
//	    	}
//
//	    	turtle(name:"Octagon",color:green) {
//	    	    app("polygon")(25, 8)
//	    	}
//	    		
//	    	// A DSL PROGRAM - END
//	    }
//		println "========================================="
//
//	    Thread.currentThread().sleep(3000);
//	}
}
