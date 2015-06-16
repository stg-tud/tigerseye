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
import groovy.lang.GroovyObjectSupport;
import groovy.lang.MetaClass;

/**
 * The Interpreter must be a GroovyObject so its missingMethod implementations
 * etc. will be invoked. So we inherit {@link GroovyObjectSupport}, which makes
 * provides the default implementations for GroovyObjects in Java.
 */
public class Interpreter extends GroovyObjectSupport implements DSL {

	protected final static boolean DEBUG = false;
	
	protected Object bodyDelegate = this; 
	
    @Override
	public void setMetaClass(MetaClass mc) { 
	    super.setMetaClass(mc);
	    bodyDelegate = mc; 
	} 
	
	public void setBodyDelegate(Interpreter bd) { 
	    bodyDelegate = bd; 
	} 
	
	public Object eval(Closure cl) {
		cl.setDelegate(bodyDelegate);
		return cl.call();
	}
	
	public Interpreter add(Interpreter other) {
		return new InterpreterCombiner(this, other, new java.util.HashMap<String,Object>());
	}
}
