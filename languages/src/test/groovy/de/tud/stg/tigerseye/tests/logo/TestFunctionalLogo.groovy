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
import junit.framework.Assert;

import de.tud.stg.tigerseye.lang.logo.*;

import java.awt.Color;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestFunctionalLogo extends TestCase {
	
	def interpreter;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
		interpreter = new FunctionalLogo();
	}

	@Test
	void testFunAndApp() {
		Closure choreography = {
			turtle (name:"Fun&App",color:green) {
				fun("square") {
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
				}
				app("square")();				
			}
		}
		choreography.delegate = interpreter;
		choreography.call();
	}
	
	@Test
	void testFunAndApp2() {
		Closure choreography = {
			turtle (name:"Octagon",color:green) {
				fun("polygon") { int length, int edges ->
		            int angle = (int)(360 / edges)
		            repeat (edges) {
				      forward length
		              right angle
		            }
			    }
				app("polygon")(50,8);				
			}
		}
		choreography.delegate = interpreter;
		choreography.call();
		Thread.currentThread().sleep(1000);
	}
	
	@Test
	void testMetaApplication() {
		Closure choreography = {
			turtle (name:"MetaApplication",color:green) {
				fun("square") {
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
				}
				square();				
			}
		}
		choreography.delegate = interpreter;
		choreography.call();
	}	
	
	@Test
	void testParameterApplication() {
		Closure choreography = {
			turtle (name:"Test",color:red) { 
				
				fun("square") { int length ->
			        forward length
			        right 90
			        forward length
			        right 90
			        forward length
			        right 90
			        forward length
			        right 90
				}
				
				square 50
				fd 1
				
				setpc green
				square 100				
				fd 1

				setpc blue
				square 150		
			}
		}
		choreography.delegate = interpreter;
		choreography.call();
	}
	
	@Test
	void testRecursive() {
		int paintcolor = Color.YELLOW.value;
		
		
		Closure choreography = {
			turtle (name:"Recursion",color:paintcolor) { 
				
				fun("adjustcolor") { int i ->
  				    Color defaultColor = Color.YELLOW;
  				    int maxVariation = 30;
			        int red = Math.max(0,defaultColor.getRed()-3*(maxVariation - i));
			        int green = Math.max(0,defaultColor.getGreen()-3*(maxVariation - i));
			        int blue = Math.max(0,defaultColor.getBlue()-3*(maxVariation - i));

			        setpc(new Color(red,green,blue).value);
				}
				
				fun("filledsquare") { int length ->
			        if (length == 0) return;
			        
			        forward length
			        right 90
			        forward length
			        right 90
			        forward length
			        right 90
			        forward length
			        right 90
			        
			        //move one step to the inside of the square 
			        forward 1
			        right 90
			        forward 1
			        left 90
			        
			        adjustcolor(length)

			        //draw a square inside this square 
			        filledsquare(length-2)
				}
				
				filledsquare 30	
			}
		}
		choreography.delegate = interpreter;
		choreography.call();
	}	
	
}