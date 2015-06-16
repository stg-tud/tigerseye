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

import junit.framework.TestSuite;

public class TestDSLSupport extends TestSuite {
	  
	  public static TestSuite suite() throws Exception {
	    
	    TestSuite suite = new TestSuite();
		//TestSuite suite = de.tud.stg.tigerseye.tests.logo.TestSuiteLogo.suite(); //TODO: Running AOP Tests before combiner test lead to problems (with TestBBCombiner.class, TestEnvironmentInterpreter.class, TestInterpreterCombiner.class)
		
	    suite.addTestSuite(TestBBCombiner.class); 
	    suite.addTestSuite(TestEnvironmentDecorator.class);
	    suite.addTestSuite(TestEnvironmentInterpreter.class); 
	    suite.addTestSuite(TestFunctional.class);
	    suite.addTestSuite(TestInterpreter.class);
	    suite.addTestSuite(TestInterpreterCombiner.class); 
	    suite.addTestSuite(TestStructural.class);

	    return suite;
	  }
	  
	}