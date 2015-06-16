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

import de.tud.stg.tigerseye.BlackBoxCombiner;
import groovy.lang.Closure;

import de.tud.stg.tigerseye.lang.logo.UCBLogo; 
import de.tud.stg.tigerseye.lang.logo.SimpleLogo; 
import groovy.lang.MissingMethodException;
import de.tud.stg.tigerseye.exceptions.SyntaxConflictException;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestOpenCombiners extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		println("======================================");
		
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
	void testScenarioFunctionWithStructuralWithSemanticConflictWriteln() {
		System.out.println("testScenarioFunctionWithStructuralWithSemanticConflictWriteln");
		DSL functional = new Functional();
		DSL console = new Console(functional)
		DSL functionalConsole = new LinearizingCombiner(functional,console);

        Closure cl = {
            define(name:"dble") { length -> 
                return  length * 2
            }

        	writeln("dble"); 
        }
        cl.delegate = functionalConsole;
        cl.call();
        
		DSL structural = new Structural();
		console = new Console(structural)
		DSL structuralConsole = new LinearizingCombiner(structural,console);

		Closure cl2 = {
            define(name:"tuple"){ 
                x = Integer
            	y = Integer
            }
            		
            writeln("tuple");
        }
        cl2.delegate = structuralConsole;
        cl2.call();
	}	
	
	@Test
	void testScenarioFunctionWithStructuralWithSemanticConflictWriteln2() {
		System.out.println("testScenarioFunctionWithStructuralWithSemanticConflictWriteln2");
		DSL functional = new Functional();
		DSL console = new Console(functional)
		DSL functionalConsole = new LinearizingCombiner(functional,console);

		Closure program = {
		  define(name:"sort") { list -> println list }
		  writeln("sort"); //prints "FUNCTION: name 'sort' paramaters: 1"
		}
		program.delegate = functionalConsole;
		program.call();

		DSL structural = new Structural();
		console = new Console(structural)
		DSL structuralConsole = new LinearizingCombiner(structural,console);

		program = {
		  define(name:"sort"){ kind = String }            		
		  writeln("sort");
		}
		program.delegate = structuralConsole;
		program.call();
	}	
	
	@Test
	void testScenarioFunctionWithStructuralWithSemanticConflict() {
		
		System.out.println("testScenarioFunctionWithStructuralWithSemanticConflict");
		DSL structural = new Structural();

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
	void testScenarioFunctionWithStructuralWithSemanticConflictInOneProgram() {
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
        
        	print "program defun (tupleSquare)"
            defun(name:"tupleSquare"){ old ->
      		  def squaredTuple = tuple(x:0,y:0);
      		  squaredTuple.x = old.x * old.x;
      		  squaredTuple.y = old.y * old.y;
    		  return squaredTuple;
        	}
        		
        	return tupleSquare(t1);        
        }
        cl.delegate = dsl;
        println cl.call();
	}	
	
	@Test
	void testScenarioFunctionWithStructuralWithSemanticError() {
		System.out.println("testScenarioFunctionWithStructuralWithSemanticError");
		DSL structural = new Structural();
		DSL functional = new Functional();
		
		DSL dsl = new MySemanticAdaptationCombiner(functional,structural);

        Closure cl = {
        	make(name:"tuple"){ 
        		x = Integer
        		y = Integer
        	}
        		
        	def t1 = tuple(x:3,y:5);
        
            defun(name:"tuple"){ old -> //no okay to have the same name
      		  def squaredTuple = tuple(x:0,y:0);
      		  squaredTuple.x = old.x * old.x;
      		  squaredTuple.y = old.y * old.y;
    		  return squaredTuple;
        	}
        		
        	return tupleSquare(t1);        
        }
        cl.delegate = dsl;
        try {
            println cl.call();
            assert false == "Did not detect the semantic conflict"
        } catch (SemanticConflictException scex) {
			//Ignore because semantic conflict was detected correctly
        }

        // opposite order 
		structural = new Structural();
		functional = new Functional();
		dsl = new MySemanticAdaptationCombiner(functional,structural);
        cl = {
                defun(name:"tuple"){ old -> //no okay to have the same name
          		  def squaredTuple = tuple(x:0,y:0);
          		  squaredTuple.x = old.x * old.x;
          		  squaredTuple.y = old.y * old.y;
        		  return squaredTuple;
            	}

            	make(name:"tuple"){ 
            		x = Integer
            		y = Integer
            	}
            		
            	def t1 = tuple(x:3,y:5);

            	return tupleSquare(t1);        
            }
            cl.delegate = dsl;
            try {
                println cl.call();
                assert false == "Did not detect the semantic conflict"
            } catch (SemanticConflictException scex) {
    			//Ignore because semantic conflict was detected correctly
        }
        
        
	}	
	
	@Test
	void testScenarioComposingContextSensitiveError() {
		System.out.println("testScenarioComposingContextSensitiveError2");
        Closure program = {
                define(name:"dble") { length -> 
                    return  length * 2 //okay to define a function without Logo keywords
                }
                
                turtle(name:"TwoSquares", color:red) { 
                    setpencolor blue
                    repeat (4) {
                        forward dble(50)
                        right 90 
                    }
                }
        }
    		
        program.delegate = new MyContextSensitiveCombiner(new Functional(),new UCBLogo());
        program.call();

		Closure programWithError = {
            define(name:"square") { length -> 
	            forward length 
	            right 90 
	            forward length 
	            right 90 
	            forward length 
	            right 90 
	            forward length 
	            right 90 
            }

            turtle(name:"TwoSquares", color:red) { 
                setpencolor blue
                apply("square")(50) 
                right 180
                setpencolor green
                apply("square")(100) 
            }
        }
		
        programWithError.delegate = new MyContextSensitiveCombiner(new Functional(),new UCBLogo());
		try {
            programWithError.call();
            assert false == "Did not detect the context sensitive keyword conflict"
		} catch (TransformationException tex) {
			//Ignore because context sensitive keyword conflict was detected correctly
		}
		
		//TODO The repeat keyword is no longer found, although it is not filtered.
		//		Closure programWithError2 = {
		//	            define(name:"square") { length -> 
		//	                repeat (4) {
		//                        forward length
		//                        right 90 
		//                    }
		//	            }
		//
		//	            turtle(name:"TwoSquares", color:red) { 
		//	                setpencolor blue
		//	                apply("square")(50) 
		//	                right 180
		//	                setpencolor green
		//	                apply("square")(100) 
		//	            }
		//	        }
		//			
		//	        programWithError2.delegate = new MyContextSensitiveCombiner(new Functional(),new UCBLogo());
		//			try {
		//	            programWithError2.call();
		//	            assert false == "Did not detect the context sensitive keyword conflict"
		//			} catch (TransformationException tex) {
		//				//Ignore because context sensitive keyword conflict was detected correctly
		//		}

		
        //Thread.sleep(3000);
	}
	

}
