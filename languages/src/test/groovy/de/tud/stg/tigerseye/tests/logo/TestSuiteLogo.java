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
package de.tud.stg.tigerseye.tests.logo;

import junit.framework.TestSuite;

public class TestSuiteLogo extends TestSuite {
	  
	  public static TestSuite suite() throws Exception {
	    
	    TestSuite suite = new TestSuite();
	    
	    suite.addTestSuite(TestAdaptiveOptimizedLogo.class);
	    suite.addTestSuite(TestCollusionDetectionLogo.class);
	    suite.addTestSuite(TestConcurrentLogo.class);
	    suite.addTestSuite(TestFunctionalLogo.class);
	    suite.addTestSuite(TestGroovySupport.class);
	    suite.addTestSuite(TestNonConcurrentLogo.class);
	    suite.addTestSuite(TestNonOptimizedLogo.class);
	    suite.addTestSuite(TestOptimizedLogo.class);
	    suite.addTestSuite(TestPositionTrackingLogo.class);
	    suite.addTestSuite(TestProfilingLogo.class);
	    suite.addTestSuite(TestRose.class);
	    suite.addTestSuite(TestSquare.class);
	    suite.addTestSuite(TestTimedSquare.class);
	    suite.addTestSuite(TestUCBLogo.class);
	    
	    //Thread.sleep(500); //wait before quitting test suite
	    
	    return suite;
	  }
	  
	  public static void main(String[] args) {
		  new TestSquare().testSquare(); 
		  new TestRose().testRose(); 
		  new TestMetaLogo().testMetaLogoRose();
		  new TestTimedSquare().testTimedSquare();
		  new TestFunctionalLogo().testRecursive();
	  }
	  
}