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
package de.tud.stg.tigerseye.tests;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import junit.framework.Assert;
import de.tud.stg.tigerseye.*;
import de.tud.stg.tigerseye.exceptions.UndefinedIdentifierException;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestFunctional extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
	}

	
	@Test
	void testFunctional() {
		System.out.println("-----------------------");
		System.out.println("testFunctional");
		DSL dsl = new Functional();

        Closure cl = {
        	define(name:"square"){ x ->
        		x * x
        	}
        		
        	return apply("square")(5);
        }
        
        cl.delegate = dsl;
        def result = cl.call();
        assert result == 25;
	}
	
	@Test
	void testUndefinedFunction() {
		System.out.println("-----------------------");
		System.out.println("testFunctional");
		DSL dsl = new Functional();

        Closure cl = {
        	define(name:"square"){ x ->
        		x * x
        	}
        		
        	return apply("squareUndefined")(5);
        }
        cl.delegate = dsl;
        try {
            def result = cl.call();
            assert false == "Not reachable, did not caught undefined function error in program correctly"
        } catch (UndefinedIdentifierException uie) {
        	//Ignore, since caught error in program correctly
        }
	}
	
	@Test
	void testShortcutApplication() {
		System.out.println("-----------------------");
		System.out.println("testShortcutApplication");
		DSL dsl = new Functional();

        Closure cl = {
        	define(name:"square"){ x ->
        		x * x
        	}
        		
        	return square(10);
        }
        
        cl.delegate = dsl;
        def result = cl.call();
        assert result == 100;
	}	
	
	@Test
	void testLexicalScoping() {
		System.out.println("-----------------------");
		System.out.println("testLexicalScoping");
		DSL dsl = new Functional();

        Closure cl = {
            y = 2
        		
            define(name:"powerOfY"){ x ->
                result = 1;
                factor = x;
                y.times { 
                	result = result * factor
                }
                return result;
        	}        		
            assert powerOfY(10) == 100; 
            
            y = 3
            assert powerOfY(3) == 27; 
        }
        
        cl.delegate = dsl;
        def result = cl.call();
        
	}		
	
	@Test
	void testLexicalScoping2WithGetLet() {
		System.out.println("-----------------------");
		System.out.println("testLexicalScoping2WithGetLet");
		DSL dsl = new Functional();

        Closure cl = {
            let("y",2)
        		
            define(name:"powerOfY"){ x ->
                result = 1;
                factor = x;
                get("y").times { 
                	result = result * factor
                }
                return result;
        	}        		
            assert powerOfY(10) == 100; 
            
            y = 3
            assert powerOfY(3) == 27; 
        }
        
        cl.delegate = dsl;
        def result = cl.call();
        
	}	
	
	@Test
	void testLexicalScoping3WithoutGet() {
		System.out.println("-----------------------");
		System.out.println("testLexicalScoping2WithoutGet");
		DSL dsl = new Functional();

        Closure cl = {
            let("y",2);
        		
            define(name:"powerOfY"){ x ->
                result = 1;
                factor = x;
                y.times { 
                	result = result * factor
                }
                return result;
        	}        		
            assert powerOfY(10) == 100; 
            
            y = 3
            assert powerOfY(3) == 27; 
        }
        
        cl.delegate = dsl;
        def result = cl.call();
	}	
	
// Self-made lexical scoping does not work yet
//	@Test
//	void testLexicalScopingStrategy() {
//		//cf. http://en.wikipedia.org/wiki/Scope_%28programming%29 
//		System.out.println("testLexicalScopingStrategy");
//		Functional dsl = new Functional();
//        dsl.resolveStrategy = EnvironmentInterpreter.LEXICAL_SCOPING;
//        
//        Closure cl = {
//        	let("x",0);
//        	
//        	define("f") { 
//        		//println "return get('x')=${get('x')}"; 
//        	    return get("x"); 
//        	}
//        	
//        	define("g") {
//        	    //println "let x=1"; 
//        	    let("x",1); 
//        	    //println "return f() = ${f()}"; 
//        	    return f(); 
//        	}
//            assert apply("g").call() == 0; 
//        }
//        
//        cl.delegate = dsl;
//        def result = cl.call();
//	}	
	
	@Test
	void testDynamicScoping() {
		//cf. http://en.wikipedia.org/wiki/Scope_%28programming%29 
		System.out.println("-----------------------");
		System.out.println("testDynamicScoping");
		Functional dsl = new Functional();
        dsl.scopingStrategy = EnvironmentInterpreter.DYNAMIC_SCOPING;

        Closure cl = {
        	let("x",0);
        	
        	define(name:"f") { 
        		//println "return get('x')=${get('x')}"; 
        	    return get("x"); 
        	}
        	
        	define(name:"g") {
        	    //println "let x=1"; 
        	    let("x",1); 
        	    //println "return f() = ${f()}"; 
        	    return f(); 
        	}
            assert apply("g").call() == 1; 
        }
        
        cl.delegate = dsl;
        def result = cl.call();
	}		
	
	@Test
	void testGroovyHasLexicalScoping() {
		//cf. http://en.wikipedia.org/wiki/Scope_%28programming%29 
		System.out.println("-----------------------");
		System.out.println("testGroovyHasLexicalScoping");
		DSL dsl = new Functional();

        Closure cl = {
        	def scopedObject = new TestGroovyScoping();
            assert scopedObject.f() == 0; 
            assert scopedObject.g() == 0; 
        }
        
        cl.delegate = dsl;
        def result = cl.call();
	}	
	
//	@Test
//	void testDynamicScoping() {
//		System.out.println("testDynamicScoping");
//		DSL dsl = new Functional();
//
//        Closure cl = {
//            y = 2
//        		
//            define("powerOfY"){ x ->
//                result = 1;
//                factor = x;
//                y.times { 
//                	result = result * factor
//                }
//                return result;
//        	}        		
//            assert powerOfY(10) == 100; 
//            
//            y = 3
//            assert powerOfY(3) == 27; 
//        }
//        
//        cl.delegate = dsl;
//        def result = cl.call();
//        
//	}	
}

class TestGroovyScoping {
    public int x = 0;
    public int f() { return x; }
    public int g() { 
      int x = 1; 
      return f(); 
    }
}
