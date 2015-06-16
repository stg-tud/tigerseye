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


import de.tud.stg.tigerseye.DSL;
import de.tud.stg.tigerseye.IFunctional;
import de.tud.stg.tigerseye.IStructural;
import de.tud.stg.tigerseye.LinearizingCombiner;
import de.tud.stg.tigerseye.exceptions.SemanticConflictException;

/**
 * Special Combiner that resolves the syntax conflict between Functional and Structural.
 * @author Tom Dinkelaker
 *
 */
public class MySemanticAdaptationCombiner extends LinearizingCombiner {
	
	IFunctional functional;
	IStructural structural;
	
	public MySemanticAdaptationCombiner(IFunctional functional, IStructural structural) {
		super(functional,structural);
		this.functional = functional;
		this.structural = structural;
	}
	
	public MySemanticAdaptationCombiner(IFunctional functional, IStructural structural, DSL console) {
		super(functional,structural,console);
		this.functional = functional;
		this.structural = structural;
	}
	
	public void defun(HashMap params, Closure body) {
		assert (params.name != null)
		if (DEBUG) println "MySemanticAdaptationCombiner.defun: adding function '${params.name}'"
		//assert (structural.getEnvironment().get(params.name) == null)
		if (structural.getEnvironment().get(params.name) != null)
		  throw new SemanticConflictException("Function name is not unique '$params.name'.")
		functional.define(params, body);
		if (DEBUG) println "MySemanticAdaptationCombiner.defun: functions = ${functional.getEnvironment()}"
	}

	public void make(HashMap params, Closure body) {
		assert (params.name != null)
		if (DEBUG) println "MySemanticAdaptationCombiner.make: adding type '$params.name'"
		//assert (functional.getEnvironment().get(params.name) == null)
		if (functional.getEnvironment().get(params.name) != null) 
			throw new SemanticConflictException("Data type name is not unique '$params.name'.")
		structural.define(params, body);
		if (DEBUG) println "MySemanticAdaptationCombiner.make: types = ${structural.getEnvironment()}"
	}
	
	/* Inline Meta Level */
	public Object methodMissing(String name, Object args) {
		if (DEBUG) println "MySemanticAdaptationCombiner.methodMissing: name = $name, args = $args"
		if (DEBUG) println "MySemanticAdaptationCombiner.methodMissing: functions = ${functional.getEnvironment()}"
		if (DEBUG) println "MySemanticAdaptationCombiner.methodMissing: types = ${structural.getEnvironment()}"
		if (functional.getEnvironment().get(name) != null) {
			return functional.&methodMissing(name,args);
		} else if (structural.getEnvironment().get(name) != null) {
			return structural.&methodMissing(name,args);
		} else {
			super.methodMissing(name, args);
	    }
	}

}
