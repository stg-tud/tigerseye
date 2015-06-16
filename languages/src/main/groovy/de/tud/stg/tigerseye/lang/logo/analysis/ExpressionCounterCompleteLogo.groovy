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
package de.tud.stg.tigerseye.lang.logo.analysis

import groovy.lang.Closure;

import java.util.HashMap;

class ExpressionCounterCompleteLogo extends AbstractInterpretationCompleteLogo implements IExpressionCounter {

	protected int expressionCount;
	
	public ExpressionCounterCompleteLogo() {
		reset();		
	}
	
	public int getExpressionCounter() {
		return expressionCount;
	}
	
	public void reset() {
		expressionCount = 0;
	}
	
	/* Literals */
	public int getBlack() { expressionCount++; return 0; }
	public int getBlue() { expressionCount++; return 0; }
	public int getRed() { expressionCount++; return 0; }
	public int getGreen() { expressionCount++; return 0; }
	public int getYellow() { expressionCount++; return 0; }
	public int getWhite() { expressionCount++; return 0; }
	
	/* Operations */
	
	public void forward(int n) { expressionCount++; }
	public void backward(int n) { expressionCount++; }
	public void right(int n) { expressionCount++; }
	public void left(int n) { expressionCount++; }
	
	public void textscreen() { expressionCount++; }
	public void fullscreen() { expressionCount++; }
	public void home() { expressionCount++; }
	public void clean() { expressionCount++; }
	public void cleanscreen() { expressionCount++; }
	public void hideturtle() { expressionCount++; }
	public void showturtle() { expressionCount++; }
	public void setpencolor(int n) { expressionCount++; }
	public void penup() { expressionCount++; }
	public void pendown() { expressionCount++; }
	
	public void ts() { expressionCount++; }
	public void fs() { expressionCount++; }
	public void cs() { expressionCount++; }
	public void ht() { expressionCount++; }
	public void st() { expressionCount++; }
	public void pu() { expressionCount++; }
	public void pd() { expressionCount++; }
	public void fd(int n) { expressionCount++; }
	public void bd(int n) { expressionCount++; }
	public void rt(int n) { expressionCount++; }
	public void lt(int n) { expressionCount++; }

	/* Abstraction Operators */
	public void turtle(HashMap params, Closure body) {
		expressionCount++; 
		body.delegate = bodyDelegate;
		body.call();
	}	
	
	public void repeat(int _times, Closure body) {
		expressionCount++; 
		body.delegate = bodyDelegate;
		body.call();		
	}
}
