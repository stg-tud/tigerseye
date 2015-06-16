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
 * This version of Logo defines shortcut keywords.
 */
public class ConciseLogoStandAlone extends SimpleLogo 
                         implements IConciseLogoStandAlone {
	 
	public ConciseLogoStandAlone() {
		super()
	}
	 
	/* Literals */

	/* Operations */
	public void fd(int n) { bodyDelegate.forward(n); }
	public void bd(int n) { bodyDelegate.backward(n); }
	public void rt(int n) { bodyDelegate.right(n);	}
	public void lt(int n) { bodyDelegate.left(n); }

//	public void ts() { textscreen(); }	
//	public void fs() { fullscreen(); }
//	public void cs() { cleanscreen(); }
//	
//	public void ht() { hideturtle(); }
//	public void st() { showturtle(); }
//	
//	public void setpc(int n) { setpencolor(n); }
//	public void pu() { penup(); }
//	public void pd() { pendown(); }
	
	/* Abstraction Operators */
}