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

import groovy.lang.Closure;
import groovy.lang.MissingPropertyException;

import java.util.Map;

import org.codehaus.groovy.runtime.InvokerHelper;

public class EnvironmentInterpreter extends Interpreter implements IEnvironment {

	protected final static boolean DEBUG = true;
	
	private final static String UPLINK_KEY = "##enclosingEnv##";
	
	public static final int USERDEFINED_SCOPING = -1;
	public static final int DEFAULT_SCOPING = 0;
	public static final int LEXICAL_SCOPING = 1;
	public static final int DYNAMIC_SCOPING = 2;
	
	protected int scopingStrategy =  DEFAULT_SCOPING;//DEFAULT_SCOPING;
	
	/**
	 * Parameter strategy is either EnvironmentInterpreter.LEXICAL_SCOPING or EnvironmentInterpreter.DYNAMIC_SCOPING.
	 */
	public void setScopingStrategy(int strategy) {
		scopingStrategy = strategy;
	}
	
	public int getScopingStrategy() {
		return scopingStrategy;
	}
	
		
	protected Map<String,Object> __globalEnvironment;
	
	public EnvironmentInterpreter() {
		this.__globalEnvironment = new HashMap<String,Object>();
	}
	
	public void let(String name, Object value) {
		if (DEBUG) System.out.println("${this.getClass()}.environment ="+__globalEnvironment);
		if (DEBUG) System.out.println("${this.getClass()}.let('"+name+"')="+value);
		__globalEnvironment.put(name, value);
		if (DEBUG) System.out.println("${this.getClass()}.environment ="+__globalEnvironment);
	}

	public Object get(String name) {
		if (DEBUG) System.out.println("${this.getClass()}.get('"+name+"')="+__globalEnvironment.get(name));
		if (DEBUG) System.out.println("${this.getClass()}.environment ="+__globalEnvironment);
		return propertyMissing(name);
	}
	
	public Map<String,Object> getEnvironment() {
		return __globalEnvironment;
	}
	
	public EnvironmentInterpreter(Map<String,Object> environment) {
		this.__globalEnvironment = environment;
	}
	
	public void propertyMissing(String name, Object value) { 
		synchronized (this) {
			this.let(name, value);
		}
	}

	public Object propertyMissing(String name) {
		if (DEBUG) System.out.print("${this.getClass()}.propertyMissing('"+name+"')=");
		HashMap<String,Object> env = __globalEnvironment;
		
		while (!env.containsKey(name)) {
			//could not find in the current environment
			if (env.containsKey(UPLINK_KEY)) {
				//continue lookup in enclosing environment
				if (DEBUG) System.out.println("<--");
				env = (HashMap<String,Object>)env.get(UPLINK_KEY);
			} else {
				if (DEBUG) System.out.println("<--(not defined)");
				//no more enclosing environment
			    throw new MissingPropertyException("The interpreter was unable to find the symbol '"+name+"' in the environment.", name, this.getClass());
				//return super.getProperty(name); //results in endless loop
			}
		}
		if (DEBUG) System.out.println(env.get(name));
		return env.get(name);
	}	
	
	protected EnvironmentDecorator getEnvironmentBodyDelegate(Closure body) {
		def functionOwner = body.owner;
		def functionOwnerDelegate = functionOwner.delegate;
		def enclosingEnvironment = null;
		if (functionOwnerDelegate instanceof EnvironmentDecorator) {
			if (DEBUG) System.out.println("${this.getClass()}.getEnvironmentBodyDelegate: enclosing environment from EnvironmentDecorator");
			EnvironmentDecorator ed = (EnvironmentDecorator)functionOwnerDelegate;
			enclosingEnvironment = ed.__environment;
		}
		if (functionOwnerDelegate instanceof EnvironmentInterpreter) {
			if (DEBUG) System.out.println("${this.getClass()}.getEnvironmentBodyDelegate: enclosing environment from EnvironmentInterpreter");
			EnvironmentInterpreter ei = (EnvironmentInterpreter)functionOwnerDelegate;
			enclosingEnvironment = ei.__globalEnvironment;
		}
		if (LinearizingCombiner.class.isInstance(functionOwnerDelegate)) {
			if (DEBUG) System.out.println("${this.getClass()}.getEnvironmentBodyDelegate: enclosing environment from EnvironmentInterpreter");
			InterpreterCombiner combiner = (InterpreterCombiner)functionOwnerDelegate;
			HashMap env = null;
			for (DSL dsl : combiner.dslDefinitions) { 
			    if (dsl instanceof EnvironmentInterpreter) {
			    	EnvironmentInterpreter ei = (EnvironmentInterpreter)dsl;
			    	env = ei.getEnvironment();
			    	break;
			    }
			}
			enclosingEnvironment = env;
		}
		if (enclosingEnvironment == null) {
			if (DEBUG) System.out.println("${this.getClass()}.getEnvironmentBodyDelegate: create new enironment without enclosing");
   		    return new EnvironmentDecorator(bodyDelegate);
		} else {
			if (DEBUG) System.out.println("${this.getClass()}.getEnvironmentBodyDelegate: create new enironment with enclosing "+enclosingEnvironment);
   		    return new EnvironmentDecorator(bodyDelegate,enclosingEnvironment);
		}
	}	
	
}
