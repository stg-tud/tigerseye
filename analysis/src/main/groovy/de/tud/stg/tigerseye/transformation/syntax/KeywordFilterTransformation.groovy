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
package de.tud.stg.tigerseye.transformation.syntax;

import java.lang.reflect.Method;
import org.codehaus.groovy.runtime.InvokerHelper;
import java.util.List;
import de.tud.stg.tigerseye.Interpreter;
import de.tud.stg.tigerseye.exceptions.TransformationException 

/**
 * This is a meta interpreter that filters certain the keywords from a language .
 * Every keyword method intercepted by the meta interpreter that will check whether the method's signature is contained in a set excluded interfaces.
 * @author Tom Dinkelaker
 */
public class KeywordFilterTransformation extends Interpreter {

	List<Class> languageInterfacesToFilter;
	Object facadeToBeFiltered;
	
	/**
	 * @param facadeToBeFiltered         The facade to be filtered.
	 * @param languageInterfacesToFilter The interface those keyword will be filtered from the facade.
	 */
	public KeywordFilterTransformation(Object facadeToBeFiltered, List<Class> languageInterfacesToFilter) {
		super();
		this.languageInterfacesToFilter = languageInterfacesToFilter;
		this.facadeToBeFiltered = facadeToBeFiltered;
		facadeToBeFiltered.setBodyDelegate(this);
	}
	
	private final boolean DEBUG = false;
	
	private boolean isMethodInFilter(String name, Object args) {
		if (DEBUG) println "isMethodInFilter: filters interfaces = $languageInterfacesToFilter "
		Iterator interfaces = languageInterfacesToFilter.iterator();
		while (interfaces.hasNext()) { 
			Class languageIfc = (Class)interfaces.next();
  		    if (DEBUG) println "isMethodInFilter: filters languageIfc = $languageIfc "
		    Method[] methods = languageIfc.getDeclaredMethods();
			if (DEBUG) println "isMethodInFilter: filters methods = $methods "
		    for (Method method in methods) { 
		        if (DEBUG) println "comparing $name == $method.name "
		        if (method.getName().equals(name)) {
		        	if (DEBUG) println "MATCH"
		        	return true
		        }
		    }
		}
		if (DEBUG) println "NO FILTERED"
		return false;
	}
	
	public Object invokeMethod(String name, Object args) {
		if (DEBUG) println this.getClass().getName()+":: DSL program uses keyword method '$name'";
		if (DEBUG) println this.getClass().getName()+":: languageInterfacesToFilter = '$languageInterfacesToFilter'";
		if (DEBUG) println this.getClass().getName()+":: facadeToBeFiltered = '$facadeToBeFiltered'";
		
		if (isMethodInFilter(name, args)) {
        	if (DEBUG) println "Filter $name"
			//throw new MissingMethodException(name, KeywordFilterTransformation.class, args);
			throw new TransformationException("DSL program uses filtered keyword method '$name'.");        	
		} else {
        	if (DEBUG) println "Not Filter $name"
		}
        
		return InvokerHelper.invokeMethod(facadeToBeFiltered, name, args);
	}
    
    public void setProperty(String name, Object value) { 
		if (DEBUG) println this.getClass().getName()+":: DSL program uses literal keyword '$name'";
        InvokerHelper.setProperty(facadeToBeFiltered, name ,value);
    }
    
    public Object getProperty(String name) { 
		if (DEBUG) println this.getClass().getName()+":: DSL program uses literal keyword '$name'";
        return InvokerHelper.getProperty(facadeToBeFiltered, name);
    }

//	public Object methodMissing(String name, Object args) {
//		if (DEBUG) println this.getClass().getName()+":: DSL program uses keyword method '$name'";
//		
//		if (isMethodInFilter(name, args)) {
//        	if (DEBUG) println "Filter $name"
//			throw new MissingMethodException(name, KeywordFilterTransformation.class, args);
//		} else {
//        	if (DEBUG) println "Not Filter $name"
//		}
//        
//		return InvokerHelper.methodMissing(facadeToBeFiltered, name, args);
//	}
//    
//    public void propertyMissing(String name, Object value) { 
//		if (DEBUG) println this.getClass().getName()+":: DSL program uses literal keyword '$name'";
//        InvokerHelper.propertyMissing(facadeToBeFiltered, name ,value);
//    }
//    
//    public Object propertyMissing(String name) { 
//		if (DEBUG) println this.getClass().getName()+":: DSL program uses literal keyword '$name'";
//        return InvokerHelper.propertyMissing(facadeToBeFiltered, name);
//    }

}
