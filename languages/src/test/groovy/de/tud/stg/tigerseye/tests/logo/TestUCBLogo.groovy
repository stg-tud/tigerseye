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

import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;
import static junit.framework.Assert.*;

import org.javalogo.*;

import de.tud.stg.tigerseye.lang.logo.*;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestUCBLogo extends TestCase {
	
	def DEBUG = false;
	
	def interpreter;
	
	def logoProgram = {
			turtle (name:"Hexagon",color:red) { 
		            repeat (6) {
				      forward 50
		              right 60
		            }
			}        
		}
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
		interpreter = new UCBLogo();
	}
	
	@Test
	void testPolygon() {
		logoProgram.delegate = interpreter;
		logoProgram.call();
		
		Thread.sleep(1000);
	}	

}