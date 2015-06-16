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
package de.tud.stg.tigerseye.lang.logo.standalone;

import de.tud.stg.tigerseye.*;
import org.javalogo.*;
import java.awt.Color;

import de.tud.stg.tigerseye.lang.logo.ISimpleLogo; 
import de.tud.stg.tigerseye.lang.logo.SimpleLogo; 


/**
 * This version of Logo defined all keyword of the Logo language.
 */
public class ExtendedLogoStandAlone extends SimpleLogo 
                          implements IExtendedLogoStandAlone {
	 
	public ExtendedLogoStandAlone() {
		super();
	}
	 
	int getpencolor() { return bodyDelegate.turtle.getPenColor().value; }

	/* Literals */

	/* Operations */
	public void textscreen()   { throw new IllegalStateException("DSL Operation has not been implemented.") }
	public void fullscreen() { throw new IllegalStateException("DSL Operation has not been implemented.") }
	public void home() { bodyDelegate.turtle.home();	}
	public void clean() { bodyDelegate.myTurtleGraphicsWindow.clear(); }
	public void cleanscreen() {	bodyDelegate.clean(); bodyDelegate.home(); }
	
	public void hideturtle() { bodyDelegate.turtle.hide(); }
	public void showturtle() { bodyDelegate.turtle.show(); }
	
	public void setpencolor(int n) { bodyDelegate.turtle.setPenColor(new java.awt.Color(n)); }
	public void penup() { bodyDelegate.turtle.penUp(); }
	public void pendown() { bodyDelegate.turtle.penDown(); } 
	
	/* Abstraction Operators */
}