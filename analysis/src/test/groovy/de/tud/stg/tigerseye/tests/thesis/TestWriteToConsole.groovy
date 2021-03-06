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

import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;
import junit.framework.Assert;
import de.tud.stg.tigerseye.lang.logo.*;

import de.tud.stg.tigerseye.analysis.syntax.WriteToConsoleAnalysis;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestWriteToConsole extends TestCase {
	
	def dsl; 
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
		
		dsl = new WriteToConsoleAnalysis(); 
		
	}

	@Test
	void testSquare() {

		dsl.eval(name:"Square") {
		
		    forward 50
		    right 90
		    forward 50
		    right 90
		    forward 50
		    right 90
		    forward 50
		    right 90
		        
		}
	}
}