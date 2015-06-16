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
package de.tud.stg.tigerseye.lang.logo.analysis;

import groovy.lang.Closure;

import de.tud.stg.tigerseye.*;
import org.javalogo.*;
import java.awt.Color;
import de.tud.stg.tigerseye.lang.logo.*;

/**
 * This version of Logo simulates costs of drawing operation by slowing them down by 100 ms.
 */ 
public class AbstractInterpretationCompleteLogo extends Interpreter implements IUCBLogo {
		 
	public AbstractInterpretationCompleteLogo() {	}
	
	/* Literals */
	public int getBlack() { return 0; }
	public int getBlue() { return 0; }
	public int getRed() { return 0; }
	public int getGreen() { return 0; }
	public int getYellow() { return 0; }
	public int getWhite() { return 0; }
	
	/* Operations */
	
	public void forward(int n) {}
	public void backward(int n) {}
	public void right(int n) {}
	public void left(int n) {}
	
	public void textscreen() {}
	public void fullscreen() {}
	public void home() {}
	public void clean() {}
	public void cleanscreen() {}
	public void hideturtle() {}
	public void showturtle() {}
	public void setpencolor(int n) {}
	public void penup() {}
	public void pendown() {}
	
	public void ts() {}
	public void fs() {}
	public void cs() {}
	public void ht() {}
	public void st() {}
	public void pu() {}
	public void pd() {}
	public void fd(int n) { }
	public void bd(int n) { }
	public void rt(int n) { }
	public void lt(int n) { }

	/* Abstraction Operators */
	public void turtle(HashMap params, Closure body) {
		body.delegate = super.bodyDelegate;
		body.call();
	}	
	
	public void repeat(int _times, Closure body) {
		body.delegate = super.bodyDelegate;
		body.call();		
	}

}