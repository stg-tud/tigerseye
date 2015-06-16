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

import groovy.lang.MetaProperty;
import org.codehaus.groovy.runtime.InvokerHelper;
import groovy.lang.MetaMethod;
import groovy.lang.MissingMethodException;
import java.util.List;
import de.tud.stg.tigerseye.Interpreter;
import de.tud.stg.tigerseye.exceptions.StrategyException;

/**
 * This is a meta interpreter that makes the keywords of a language case insensitive.
 * Every (case insensitive) keyword method called on the meta interpreter will call the matching (case sensitive) keyword on the referenced interpreter instance.
 * @author Tom Dinkelaker
 */
public class CaseInsensitiveTransformation extends Interpreter {

	Interpreter interpreter;
	
	public CaseInsensitiveTransformation(Interpreter interpreter) {
		super();
		this.interpreter = interpreter;
	}
	
	private final boolean DEBUG = true;
	
	public Object methodMissing(String name, Object args) {
	    if (DEBUG) println this.getClass().getName()+":: DSL program uses keyword '$name'";
	    List<MetaMethod> methods = interpreter.metaClass.getMethods();
	    List<MetaMethod> matchingMethods = methods.findAll { MetaMethod method ->
	        return method.getName().toUpperCase().equals(name.toUpperCase());
	    }
	    if (matchingMethods.isEmpty()) {
		    if (DEBUG) println this.getClass().getName()+":: DSL case insenstive keyword '$name' not found in ${interpreter}";
            throw new MissingMethodException(name,this.getClass(),args);
	    } else if (matchingMethods.size > 1) {
		    if (DEBUG) println this.getClass().getName()+":: DSL multiple case insenstive keywords found for '$name' in ${interpreter}";
            throw new StrategyException("Multiple case insenstive keywords found for '$name' in ${interpreter} was ${matchingMethods}");
	    } 
        if (DEBUG) println this.getClass().getName()+":: DSL matching case insenstive keywords found for '$name' in ${interpreter}";
        MetaMethod foundMetaMethod = matchingMethods[0];
        return InvokerHelper.invokeMethod(interpreter,foundMetaMethod.getName(), args);
	}
    
    public void propertyMissing(String name, Object value) { 
		if (DEBUG) println this.getClass().getName()+":: DSL program uses literal keyword '$name'";
	    List<MetaProperty> properties = interpreter.metaClass.getProperties();
	    List<MetaProperty> matchingProperties = properties.findAll { MetaProperty _property ->
	        return _property.getName().toUpperCase().equals(name.toUpperCase());
	    }
	    if (matchingProperties.isEmpty()) {
		    if (DEBUG) println this.getClass().getName()+":: DSL case insenstive literal keyword '$name' not found in ${interpreter}";
            throw new MissingPropertyException(name,this.getClass());
	    } else if (matchingProperties.size > 1) {
		    if (DEBUG) println this.getClass().getName()+":: DSL multiple case insenstive literal keywords found for '$name' in ${interpreter}";
            throw new StrategyException("Multiple case insenstive literal keywords found for '$name' in ${interpreter} was ${matchingProperties}");
	    } 
        MetaProperty foundMetaProperty = matchingProperties[0];
        if (DEBUG) println this.getClass().getName()+":: DSL matching case insenstive literal keyword '${foundMetaProperty.getName()}' found for '$name' in ${interpreter}";
        InvokerHelper.setProperty(interpreter, foundMetaProperty.getName() ,value);
    }
    
    public Object propertyMissing(String name) { 
		if (DEBUG) println this.getClass().getName()+":: DSL program uses literal keyword '$name'";
	    List<MetaProperty> properties = interpreter.metaClass.getProperties();
	    List<MetaProperty> matchingProperties = properties.findAll { MetaProperty _property ->
	        return _property.getName().toUpperCase().equals(name.toUpperCase());
	    }
	    if (matchingProperties.isEmpty()) {
		    if (DEBUG) println this.getClass().getName()+":: DSL case insenstive literal keyword '$name' not found in ${interpreter}";
            throw new MissingPropertyException(name,this.getClass());
	    } else if (matchingProperties.size > 1) {
            throw new StrategyException("Multiple case insenstive literal keywords found for '$name' in ${interpreter} was ${matchingProperties}");
	    } 
        MetaProperty foundMetaProperty = matchingProperties[0];
        if (DEBUG) println this.getClass().getName()+":: DSL matching case insenstive literal keyword '${foundMetaProperty.getName()}' found for '$name' in ${interpreter}";
        return InvokerHelper.getProperty(interpreter, foundMetaProperty.getName());
    }

}
