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

import groovy.lang.MissingPropertyException;

import java.util.HashMap;

import org.codehaus.groovy.runtime.InvokerHelper;

public class EnvironmentDecorator implements IEnvironment {

	private final static boolean DEBUG = true;

	private final static String UPLINK_KEY = "##enclosingEnv##";
	
	private HashMap<String,Object> __environment;
	private Interpreter __interpreter;
	
	public EnvironmentDecorator(Interpreter interpreter) {
		this.__environment = new HashMap<String,Object>();
		this.__interpreter = interpreter;
	}

	public EnvironmentDecorator(Interpreter interpreter, HashMap<String,Object> enclosingEnvironment) {
		this.__environment = new HashMap<String,Object>();
		__environment.put(UPLINK_KEY, enclosingEnvironment);
		this.__interpreter = interpreter;
	}
	
	public void let(String name, Object value) {
		if (DEBUG) System.out.println("EnvironmentDecorator.let: (before) env="+__environment);
		if (DEBUG) System.out.println("EnvironmentDecorator.let('"+name+"')="+value);
		setProperty(name, value);
		if (DEBUG) System.out.println("EnvironmentDecorator.let: (after) env="+__environment);
	}

	public Object get(String name) {
		if (DEBUG) System.out.println("EnvironmentDecorator.let: env="+__environment);
		if (DEBUG) System.out.println("EnvironmentDecorator.get('"+name+"')="+getProperty(name));
		return getProperty(name);
	}
	
	void setProperty(String name, value) { 
		if (DEBUG) println "setProperty $name"
		//TODO check if the setting must be delegate to interpreter... 
		synchronized (this) {
			__environment.put(name, value);
		}
	}

	 def getProperty(String name) {
		if (DEBUG) println "getProperty $name"
		HashMap<String,Object> env = __environment;
		
		if (name.equals("__environment")) return __environment;
		if (name.equals("__interpreter")) return __interpreter;
		
		while (!env.containsKey(name)) {
			//could not find in the current environment
			if (env.containsKey(UPLINK_KEY)) {
				//continue lookup in enclosing environment
				env = (HashMap<String,Object>)env.get(UPLINK_KEY);
			} else {
				//no more enclosing environment
		        MetaProperty metaProperty = __interpreter.metaClass.getMetaProperty(name)
		        if (metaProperty == null) throw new MissingPropertyException("Property '$name' not found in environment and interpreter. Env=$env",name,__interpreter.getClass());
		        return metaProperty.getProperty(__interpreter)
			}
		}
		return env.get(name);
	}	

	public Object propertyMissing(String name) {
		if (DEBUG) println "propertyMissing $name"
		throw new MissingPropertyException(name,this.getClass());
	}
	 
	public Object invokeMethod(String name, Object args) {
		if (DEBUG) println "invokeMethod '$name' args=$args"
	    MetaMethod metaMethod = __interpreter.metaClass.getMetaMethod(name, *args)
		if (metaMethod == null) throw new MissingMethodException(name,__interpreter.getClass(),args);
	    Object result = metaMethod.invoke(__interpreter, *args)
	    return result 
	}
	
	public HashMap<String,Object> getEnvironment() {
		return __environment;
	}

	
}
