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

import de.tud.stg.tigerseye.IFunctional;
import de.tud.stg.tigerseye.InterpreterCombiner;
import de.tud.stg.tigerseye.IStructural;

/**
 * Special Combiner that resolves the syntax conflict between Functional and Structural.
 * @author Tom Dinkelaker
 *
 */
public class MyRenamingCombiner extends InterpreterCombiner {
	
	IFunctional functional;
	IStructural structural;
	
	public MyRenamingCombiner(IFunctional functional, IStructural structural) {
		super(functional,structural);
		this.functional = functional;
		this.structural = structural;
	}
	
	public void defun(HashMap params, Closure body) {
		functional.define(params, body);
	}

	public void make(HashMap params, Closure body) {
		structural.define(params, body);
	}

}
