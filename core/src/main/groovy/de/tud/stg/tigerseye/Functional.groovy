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
package de.tud.stg.tigerseye;

import de.tud.stg.tigerseye.exceptions.UndefinedIdentifierException 
import de.tud.stg.tigerseye.*;
import org.javalogo.*;
import groovy.lang.Closure;

/**
 * This version of Logo simulates costs of drawing operation by slowing them down by 100 ms.
 */
public class Functional extends EnvironmentInterpreter implements IFunctional {

	/* Literals */

	/* Operations */
	public Closure apply(String name) {
		if (!environment.containsKey(name)) throw new UndefinedIdentifierException("Undefined function '$name', which is not in env=$environment");
		assert name != null;
		assert !name.isEmpty();
		Closure function = environment.get(name)
		assert function != null;
		if (DEBUG) { 
			println "Functional.apply: applying function $name"
			println "Functional.apply: applying function ${function.delegate}"
			println "Functional.apply: applying function ${function.getResolveStrategy()}"
			if (function.delegate instanceof IEnvironment)
			  println("Functional.apply: applying function ${name} has environment ${function.getDelegate().getEnvironment()}");
		}
		return function;
	}
	
	/* Abstraction Operators */
	public void define(HashMap params, Closure body) {
		String name = params.name; assert name != null; assert !name.isEmpty(); 
		if (DEBUG) System.out.println("Functional.define: defining new function ${name} ...");
		environment.put(name,body);
		if (scopingStrategy == EnvironmentInterpreter.DEFAULT_SCOPING) {
			if (DEBUG) System.out.println("Functional.define: setting scoping strategy default");
		    body.delegate = getEnvironmentBodyDelegate(body);
		    //body.resolveStrategy = Closure.DELEGATE_FIRST;
			if (DEBUG) System.out.println("Functional.define: defined function ${name} has default scope with ${body.delegate}");
		} else if (scopingStrategy == EnvironmentInterpreter.LEXICAL_SCOPING) {
			if (DEBUG) System.out.println("Functional.define: setting scoping strategy lexical");
			body.delegate = getEnvironmentBodyDelegate(body);
		    //body.resolveStrategy = Closure.DELEGATE_FIRST;
			if (DEBUG) System.out.println("Functional.define: defined function ${name} has lexical scope with ${body.delegate}");
			if (DEBUG) System.out.println("Functional.define: defined function ${name} has environment ${body.delegate.__environment}");
		} else if (scopingStrategy == EnvironmentInterpreter.DYNAMIC_SCOPING) {
			if (DEBUG) System.out.println("Functional.define: setting scoping strategy dynamic");
			body.delegate = bodyDelegate;
		    //body.resolveStrategy = Closure.DELEGATE_FIRST;
			if (DEBUG) System.out.println("Functional.define: 1");
			if (DEBUG) System.out.println("Functional.define: defined function ${name} has body ${body.toString()}");
			if (DEBUG) System.out.println("Functional.define: 2");
			if (DEBUG) System.out.println("Functional.define: defined function ${name} has dynamic scope with ${body.getDelegate()}");
			if (DEBUG) System.out.println("Functional.define: defined function ${name} has environment ${body.getDelegate().__globalEnvironment}");
		} else if (scopingStrategy == EnvironmentInterpreter.USERDEFINED_SCOPING) {
			if (DEBUG) System.out.println("Functional.define: setting scoping strategy user-defined");
			//nothing 
		} else throw new RuntimeException("Invalid resolution strategy.")
		if (DEBUG) System.out.println("Functional.define: ... function ${name} defined");
	}
	
	/* Inline Meta Level */
	protected Object methodMissing(String name, Object args) {
		//println "${this} : functions=$functionNamesToClosure"
		if (environment.get(name) != null) {
          apply(name).call(*args);
		} else {
			throw new MissingMethodException(name, this.class, args);
	    }
	}
}