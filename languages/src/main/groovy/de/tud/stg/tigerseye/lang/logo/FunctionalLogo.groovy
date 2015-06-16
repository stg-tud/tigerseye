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
package de.tud.stg.tigerseye.lang.logo;

import de.tud.stg.tigerseye.*;
import org.javalogo.*;
import java.awt.Color;

/**
 * This version of Logo simulates costs of drawing operation by slowing them down by 100 ms.
 */
public class FunctionalLogo  extends TimedLogo implements IFunctionalLogo {
	protected HashMap<String,Closure> functionNamesToClosure;
		 
	public FunctionalLogo() {
		super();
		functionNamesToClosure = new HashMap<String,Closure>();
	}
	
	public HashMap<String,Closure> getFunctionNamesToClosure() {
		return functionNamesToClosure;
	}
	
	public void setFunctionNamesToClosure(HashMap<String,Closure> map) {
		this.functionNamesToClosure = map;
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
		functionNamesToClosure.put(name, body);
	}
	
	/* Inline Meta Level */
	private Object methodMissing(String name, Object args) {
		//println "${this} : functions=$functionNamesToClosure"
		if (functionNamesToClosure.get(name) != null) {
          app(name).call(*args);
		} else {
			throw new MissingMethodException(name, this.class, args);
	    }
	}
}