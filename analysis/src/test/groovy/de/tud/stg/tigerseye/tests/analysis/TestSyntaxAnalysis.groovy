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
package de.tud.stg.tigerseye.tests.analysis;

import org.junit.Before;
import junit.framework.TestCase;

import de.tud.stg.tigerseye.*;
import de.tud.stg.tigerseye.lang.logo.FunctionalLogo;
import de.tud.stg.tigerseye.analysis.syntax.*;

/**
 * <p>
 * This test does not run with Groovy 1.7, because the MOP does not try to call missingMethod even if defined. 
 * A MissingMethodException is thrown because the delegate object does not define a concrete method.
 * </p>
 * @author Tom Dinkelaker
 */
public class TestSyntaxAnalysis extends TestCase {
	
	final boolean DEBUG = false;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception{
	}

	public void testTestUsedKeywords() {
		System.out.println("\n---\ntestTestUsedKeywords");
		UsedKeywordsAnalysis dsl = new UsedKeywordsAnalysis();
	    Closure cl = { 
	    	// A DSL PROGRAM - BEGIN
	    	
	    	Object x;
	    	x = primitiveLiteral;
    		primitiveLiteral2 = 2;
	    	
	    	def y = getAccessorLiteral1(); //accessing through getter
	    	setAccessorLiteral2(y); //accessing through setter
	    	
	    	operation(x,y);

	    	nestedAbstraction() { //using a block 
	    		def z = primitiveLiteral3;
	    		primitiveLiteral4 = 4;

	    		operation2(x,y);
	    	}
	    	
	    	// A DSL PROGRAM - END
	    }
	    cl.delegate = dsl;
	    cl.call();
	    
	    def DETAILED_PRINT_OUT = true;
	    
	    if (DETAILED_PRINT_OUT) System.out.println("Checking the produced sets");

	    if (DETAILED_PRINT_OUT) System.out.println("keywords(${dsl.getKeywords().size()})="+dsl.getKeywords());
	    assert(dsl.getKeywords().contains("primitiveLiteral"));
	    assert(dsl.getKeywords().contains("primitiveLiteral2"));
	    assert(dsl.getKeywords().contains("accessorLiteral1"));
	    assert(dsl.getKeywords().contains("accessorLiteral2"));
	    assert(dsl.getKeywords().contains("operation"));
	    assert(dsl.getKeywords().contains("nestedAbstraction"));
	    assert(dsl.getKeywords().contains("primitiveLiteral3"));
	    assert(dsl.getKeywords().contains("primitiveLiteral4"));
	    assert(dsl.getKeywords().contains("operation2"));
	    assert(dsl.getKeywords().size()==9);

	    if (DETAILED_PRINT_OUT) System.out.println("literals(${dsl.getLiterals().size()})="+dsl.getLiterals());
	    assert(dsl.getLiterals().contains("primitiveLiteral"));
	    assert(dsl.getLiterals().contains("primitiveLiteral2"));
	    assert(dsl.getLiterals().contains("accessorLiteral1"));
	    assert(dsl.getLiterals().contains("accessorLiteral2"));
	    assert(dsl.getLiterals().contains("primitiveLiteral3"));
	    assert(dsl.getLiterals().contains("primitiveLiteral4"));
	    assert(dsl.getLiterals().size()==6);

	    if (DETAILED_PRINT_OUT) System.out.println("operations(${dsl.getOperations().size()})="+dsl.getOperations());
	    assert(dsl.getOperations().contains("operation"));
	    assert(dsl.getOperations().contains("operation2"));
	    assert(dsl.getOperations().size()==2);

	    if (DETAILED_PRINT_OUT) System.out.println("abstractions(${dsl.getAbstractions().size()})="+dsl.getAbstractions());
	    assert(dsl.getAbstractions().contains("nestedAbstraction"));
	    assert(dsl.getAbstractions().size()==1);
	}
	
	public void testTestWriteToConsoleAnalysis() {
		System.out.println("\n---\ntestTestWriteToConsoleAnalysis");
		de.tud.stg.tigerseye.analysis.syntax.WriteToConsoleAnalysis dsl = new de.tud.stg.tigerseye.analysis.syntax.WriteToConsoleAnalysis(); 
	    Closure cl = { 
	    	// A DSL PROGRAM - BEGIN
	    	
	    	Object x;
	    	x = primitiveLiteral;
    		primitiveLiteral2 = 2;
	    	
	    	def y = getAccessorLiteral1(); //accessing through getter
	    	setAccessorLiteral2(y); //accessing through setter
	    	
	    	operation(x,y);

	    	nestedAbstraction() { //using a block 
	    		def z = primitiveLiteral3;
	    		primitiveLiteral4 = 4;

	    		operation2(x,y);
	    	}
	    	
	    	// A DSL PROGRAM - END
	    }
	    cl.delegate = dsl;
	    cl.call();
	}
	
	public void testSyntaxCheckerWithNoError() {
		System.out.println("\n---\ntestSyntaxChecker");
		Closure cl = {
			// A DSL PROGRAM - BEGIN
			fun("hexagon") { length ->
				turtle(name:"hexagon",color:red) {
					repeat (6) {
						forward length
						right 60
					}
				}
			}
			app("hexagon")(50)
			// A DSL PROGRAM - END
		}
		def syntaxProgramAnalyzer = new UsedKeywordsAnalysis();
		cl.delegate = syntaxProgramAnalyzer;
		cl.call();
		println "Found used keywords: ${syntaxProgramAnalyzer.getKeywords()}";
		
		def syntaxDefinitionAnalyer = new ProvidedKeywordsAnalysis(FunctionalLogo.class);
		println "Found provided keywords: ${syntaxDefinitionAnalyer.getKeywords()}";
		
		println syntaxProgramAnalyzer.getLiterals()-syntaxDefinitionAnalyer.getLiterals();
		println syntaxProgramAnalyzer.getOperations()-syntaxDefinitionAnalyer.getOperations();
		println syntaxProgramAnalyzer.getAbstractions()-syntaxDefinitionAnalyer.getAbstractions();
		
		assert syntaxDefinitionAnalyer.isSyntaxOfProgramCorrect(syntaxProgramAnalyzer);
	}
	
	public void testSyntaxCheckerWithError() {
		System.out.println("\n---\ntestSyntaxCheckerWithError");
		Closure cl = {
			// A DSL PROGRAM - BEGIN
			fun("hexagon") { length ->
				turtle(name:"hexagon",color:red) {
					repeat (6) {
						forward length
						rechts 60 //Fehler!!!
					}
				}
			}
			app("hexagon")(50)
			// A DSL PROGRAM - END
		}
		def syntaxProgramAnalyzer = new UsedKeywordsAnalysis();
		cl.delegate = syntaxProgramAnalyzer;
		cl.call();
		println "Found used keywords: ${syntaxProgramAnalyzer.getKeywords()}";
		
		def syntaxDefinitionAnalyer = new ProvidedKeywordsAnalysis(FunctionalLogo.class);
		println "Found provided keywords: ${syntaxDefinitionAnalyer.getKeywords()}";
		
		println syntaxProgramAnalyzer.getLiterals()-syntaxDefinitionAnalyer.getLiterals();
		println syntaxProgramAnalyzer.getOperations()-syntaxDefinitionAnalyer.getOperations();
		println syntaxProgramAnalyzer.getAbstractions()-syntaxDefinitionAnalyer.getAbstractions();
		
		assert !syntaxDefinitionAnalyer.isSyntaxOfProgramCorrect(syntaxProgramAnalyzer);
	}

	
	
}
