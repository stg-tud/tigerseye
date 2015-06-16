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

import java.io.File;

import de.tud.stg.tigerseye.tests.analysis.TestSyntaxAnalysis;

import junit.framework.TestSuite;

public class TestThesisScenarios extends TestSuite {
	  
	  public static TestSuite suite() throws Exception {
	    
	    TestSuite suite = new TestSuite();
		//TestSuite suite = de.tud.stg.tigerseye.tests.logo.TestSuiteLogo.suite(); //TODO: Running AOP Tests before combiner test lead to problems (with TestBBCombiner.class, TestEnvironmentInterpreter.class, TestInterpreterCombiner.class)
	    
	    suite.addTestSuite(TestComposability.class);
	    //suite.addTestSuite(TestComposabilityNotWorking.class);
	    suite.addTestSuite(TestComposabilityStandAlone.class);
	    suite.addTestSuite(TestComposabilityWithInteractions.class);
	    //suite.addTestSuite(TestComposabilityWorking.class);
	    suite.addTestSuite(TestExtensibility.class);
	    //suite.addTestSuite(TestOpenCombiners.class);
	    suite.addTestSuite(TestStaticTransformations.class);
	    suite.addTestSuite(TestSyntacticAnalyses.class);
	    
	    return suite;
	  }
	  
	}