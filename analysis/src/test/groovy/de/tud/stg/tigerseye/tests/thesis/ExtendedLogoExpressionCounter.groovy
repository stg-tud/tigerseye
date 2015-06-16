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

import groovy.lang.Closure;

import java.util.HashMap;

import de.tud.stg.tigerseye.Interpreter;
import de.tud.stg.tigerseye.lang.logo.IExtendedLogo;

public class ExtendedLogoExpressionCounter extends Interpreter implements IExtendedLogo {

	private int expressionCounter = 0;
	
	public int getExpressionCounter() {
		return expressionCounter;
	}
	
	public ExtendedLogoExpressionCounter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void clean() {
		expressionCounter++;
	}

	@Override
	public void cleanscreen() {
		expressionCounter++;
	}

	@Override
	public void fullscreen() {
		expressionCounter++;
	}

	@Override
	public void hideturtle() {
		expressionCounter++;
	}

	@Override
	public void home() {
		expressionCounter++;
	}

	@Override
	public void pendown() {
		expressionCounter++;
	}

	@Override
	public void penup() {
		expressionCounter++;
	}

	@Override
	public void setpencolor(int n) {
		expressionCounter++;
	}

	@Override
	public void showturtle() {
		expressionCounter++;
	}

	@Override
	public void textscreen() {
		expressionCounter++;
	}

	@Override
	public void backward(int n) {
		expressionCounter++;
	}

	@Override
	public void forward(int n) {
		expressionCounter++;
	}

	@Override
	public int getBlack() {
		expressionCounter++;
		return 0;
	}

	@Override
	public int getBlue() {
		expressionCounter++;
		return 0;
	}

	@Override
	public int getGreen() {
		expressionCounter++;
		return 0;
	}

	@Override
	public int getRed() {
		expressionCounter++;
		return 0;
	}

	@Override
	public int getWhite() {
		expressionCounter++;
		return 0;
	}

	@Override
	public int getYellow() {
		expressionCounter++;
		return 0;
	}

	@Override
	public void left(int n) {
		expressionCounter++;
	}

	@Override
	public void right(int n) {
		expressionCounter++;
	}

	@Override
	public void turtle(HashMap params, Closure choreography) {
		expressionCounter++;
		choreography.delegate = this.bodyDelegate;
		choreography.call();
	}

}
