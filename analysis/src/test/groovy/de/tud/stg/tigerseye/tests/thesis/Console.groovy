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
package de.tud.stg.tigerseye.tests.thesis

import de.tud.stg.tigerseye.IFunctional;
import de.tud.stg.tigerseye.IStructural;

import de.tud.stg.tigerseye.Interpreter;

class Console extends Interpreter {

	final boolean DEBUG = false;
	
	IFunctional functional;
	IStructural structural; 
	
	public Console(IFunctional functional, IStructural structural) {
		super();
		this.functional = functional;
		this.structural = structural;
	}
	
	public Console(IFunctional functional) {
		super();
		this.functional = functional;
	}
	
	public Console(IStructural structural) {
		super();
		this.structural = structural;
	}
	
	public void writeln(String name) {
		if (DEBUG) println "MySemanticAdaptationCombiner.methodMissing: name = $name"
		if (DEBUG) println "MySemanticAdaptationCombiner.methodMissing: functions = ${functional?.getEnvironment()}"
		if (DEBUG) println "MySemanticAdaptationCombiner.methodMissing: types = ${structural?.getEnvironment()}"
		if (functional?.getEnvironment()?.get(name) != null) {
			println "FUNCTION:"+functional.getEnvironment().get(name)
		} else if (structural?.getEnvironment()?.get(name) != null) {
			println "TYPE:"+structural.getEnvironment().get(name)
		} else {
			println "UNKNOWN OBJECT $name"
	    }
	}
}
