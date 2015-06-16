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
package de.tud.stg.tigerseye.tests.transformation;

import org.junit.Before;
import junit.framework.TestCase;

import de.tud.stg.tigerseye.*;
import de.tud.stg.tigerseye.transformation.syntax.*;
import groovy.lang.Closure 
//import de.tud.stg.tigerseye.analysis.syntax.CaseInsensitiveAmbiguityFreeAnalysis; 

/**
 * @author Tom Dinkelaker
 */
public class TestCaseInsensitiveSyntaxTransformation extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception{
	}

//	public void testUpperCaseSyntaxAnalysisWithAmbiguity() {
//		System.out.println("\n---\ntestUpperCaseSyntaxAnalysisWithAmbiguity");
//		Interpreter dsl = new CaseInsensitiveAmbiguityFreeAnalysis(new UCBLogoWithKeywordCaseAmbiguity());
//	    Closure cl = {
//			TURTLE (name:"Hexagon",color:RED) { 
//		            REPEAT (6) {
//				      FORWARD 50
//		              RIGHT 60
//		            }
//			}        
//		}
//	    cl.delegate = dsl;
//	    try {
//	        cl.call();
//	    	fail("Should not be reached as the case insensitive ambiguity for the 'FORWARD/fOrWaRd' keyword should be detected")
//	    } catch (Exception ae) {
//	    	//igonre 
//	    } 
//	    //Thread.sleep(5000);
//	}
	


	
	
}
