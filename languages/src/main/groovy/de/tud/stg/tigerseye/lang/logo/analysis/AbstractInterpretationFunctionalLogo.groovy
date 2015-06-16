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

import java.util.HashMap;

import de.tud.stg.tigerseye.*;
import org.javalogo.*;
import de.tud.stg.tigerseye.lang.logo.IFunctionalLogo 

/**
 * This version of Logo simulates costs of drawing operation by slowing them down by 100 ms.
 */ 
public class AbstractInterpretationFunctionalLogo extends AbstractInterpretationCompleteLogo implements IFunctionalLogo {
	protected HashMap<String,Closure> functionNamesToClosure;
		 
	public AbstractInterpretationFunctionalLogo() {
		functionNamesToClosure = new HashMap<String,Closure>();
	}
	
	public AbstractInterpretationFunctionalLogo(HashMap<String,Closure> enclosedFunctionNamesToClosure) {
		functionNamesToClosure = new HashMap<String,Closure>();
		putAllFunctionNamesToClosure(enclosedFunctionNamesToClosure);
	}
	
	public HashMap<String,Closure> getFunctionNamesToClosure() {
		return functionNamesToClosure;
	}
	
	public void setFunctionNamesToClosure(HashMap<String,Closure> map) {
		this.functionNamesToClosure = map;
	}
	
	public void putAllFunctionNamesToClosure(HashMap<String,Closure> enclosedFunctionNamesToClosure) {
		synchronized (enclosedFunctionNamesToClosure) {
			enclosedFunctionNamesToClosure.keySet().each { String key ->
		       Closure value = enclosedFunctionNamesToClosure.get(key).clone(); //must clone to allow adjusting the delegate
		       functionNamesToClosure.put(key,value);
		    }
		}
	}
	
	/* Literals */

	/* Operations */
	public Closure app(String name) {
		assert name != null;
		assert !name.isEmpty();
		Closure function = functionNamesToClosure.get(name)
		assert function != null;
		return function;
	}
	
	/* Abstraction Operators */
	public void fun(String name, Closure body) {
		synchronized (functionNamesToClosure) {
		  functionNamesToClosure.put(name, body);
		}
	}
	
//	/* Inline Meta Level */
//	private Object methodMissing(String name, Object args) {
//        def function = app(name); 
//		if (function != null) {
//            function.call(*args);
//		} else {
//			throw new MissingMethodException(name, this.class, args);
//	    }
//	}
	
}