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
import de.tud.stg.tigerseye.analysis.syntax.*;
import de.tud.stg.tigerseye.lang.logo.standalone.IConciseLogoStandAlone; 
import de.tud.stg.popart.dslsupport.logo.IUCBLogo;
import de.tud.stg.tigerseye.lang.logo.UCBLogo;
import de.tud.stg.tigerseye.lang.logo.standalone.IConciseLogoStandAlone;
import de.tud.stg.tigerseye.transformation.syntax.KeywordFilterTransformation;


/**
 * <p>
 * This test does not run with Groovy 1.7, because the MOP does not try to call missingMethod even if defined. 
 * A MissingMethodException is thrown because the delegate object does not define a concrete method.
 * </p>
 * @author Tom Dinkelaker
 */
public class TestKeywordFilterTransformation extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception{
	}

	public void testKeywordFilterTransformation() {
		System.out.println("\n---\ntestKeywordFilterTransformation");
		DSL dsl = new KeywordFilterTransformation(new UCBLogo(), [IConciseLogoStandAlone]);
	    Closure cl = {
			turtle (name:"Hexagon",color:red) { 
                repeat (6) {
				    fd 50
		            rt 60
		        }
			}        
		}
	    cl.delegate = dsl;
	    try {
	        cl.call();
	        assert (false == "should not be reached, otherwise the filter is not correct");
	    } catch (Exception ex) {
	    	//ignore, since filtered correctly 
	    }
	}
	
	public void testKeywordFilterTransformationWithoutConflict() {
		System.out.println("\n---\testKeywordFilterTransformationWithoutConflict");
		DSL dsl = new KeywordFilterTransformation(new UCBLogo(), [IConciseLogoStandAlone]);
	    Closure cl = {
			turtle (name:"Hexagon",color:red) { 
                repeat (6) {
				    forward 50
		            right 60
		        }
			}        
		}
	    cl.delegate = dsl;
	    cl.call();
	}
	

	
	
}
