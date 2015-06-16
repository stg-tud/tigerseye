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
import groovy.lang.MetaMethod;
import groovy.lang.MetaProperty;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LinearizingCombiner extends InterpreterCombiner {

	protected final boolean DEBUG = false;

	protected boolean SPEED_UP = false; //Cannot optimize the lookup process, because all members are not enforced to be unique  

//	protected List<DSL> dslDefinitions;
//	protected Map<String, Object> internalContext;

	// BEGIN:[ OR-2009-07-25: Thread-Safety Modification ]:::::::::::::::::::
	public void setInternalContext(Map<String, Object> context) {
		synchronized (this) {
			this.internalContext = context;
		}
	}

	public Map<String, Object> getInternalContext() {
		synchronized (this) {
			return internalContext;
		}
	}
	// END:::[ OR-2009-07-25: Thread-Safety Modification ]:::::::::::::::::::

	//-DINKELAKER-2010-02-16-BEGIN
	private void setCombinerAsBodyDelegateOfAllInterpreters() {
		for(DSL dsl : dslDefinitions){
			if (!(dsl instanceof Interpreter)) continue; //this is necessary for compability to old DSL implementations that do not inherit the Interpreter class
			((Interpreter)dsl).setBodyDelegate(this);
		}
	}
	//-DINKELAKER-2010-02-16-END

	public LinearizingCombiner(DSL... dslDefinitions) {
		//this(java.util.Arrays.asList(dslDefinitions), new java.util.HashMap<String,Object>());
		super(dslDefinitions);
	}

	public LinearizingCombiner(DSL dslDefinition, Map<String, Object> context) {
//		this.dslDefinitions = new java.util.LinkedList<DSL>();
//		this.dslDefinitions.add(dslDefinition);
//		this.internalContext = context;
//		setCombinerAsBodyDelegateOfAllInterpreters();
		super(dslDefinition,context);
	} 

	public LinearizingCombiner(DSL dslDefinition1, DSL dslDefinition2, Map<String, Object> context) {
		super(dslDefinition1,dslDefinition2,context);
	} 

	public LinearizingCombiner(List<DSL> dslDefinitions) {
		super(dslDefinitions);
	}

	public LinearizingCombiner(DSL dslDefinition1, DSL dslDefinition2) {
		super(dslDefinition1,dslDefinition2, new java.util.HashMap<String,Object>());
	} 
	
	public LinearizingCombiner(List<DSL> dslDefinitions, Map<String, Object> context) {
		super(dslDefinitions,context);
		//setCombinerAsBodyDelegateOfAllInterpreters();
	} 

	public Object eval(Closure dslClosure) {
		dslClosure.setResolveStrategy(Closure.DELEGATE_FIRST); //Closure.DELEGATE_FIRST enables writing into properties defined by DSLs and prevent the creation of a local variable
		dslClosure.setDelegate(this);
		return dslClosure.call();
	}
	
	protected Class[] classesArrayForObjectArray(Object[] args) {
		Class[] classes = new Class[args.length];
		for (int i=0; i < args.length; i++) {
			classes[i] = args[i].getClass();
		}
		return classes;
	}
	
	protected Object invokeMethodMissingOnFirstDSL(Set<DSL> dsls, String name, Object args) {
        HashMap<DSL,MetaMethod> methods = new HashMap<DSL,MetaMethod>();
		for(DSL dsl : dslDefinitions) {
			if (DEBUG) System.out.println("LinearizingCombiner: try to find 'methodMissing' in "+ dsl);
			MetaMethod method = ((GroovyObjectSupport)dsl).getMetaClass().pickMethod("methodMissing",(Class[])[String.class, Object[].class]);
            if (method != null) {
            	methods.put(dsl,method);
            	if (SPEED_UP) break;
            }
		}
		if (methods.keySet().isEmpty()) {
			throw new MissingMethodException(name, this.getClass(), (Object[]) args);
		}
		//Ignore syntax conflicts and simply invoke the first interpreter found
		DSL dsl =(DSL)methods.keySet().iterator().next(); 
		MetaMethod uniqueMethod = (MetaMethod)methods.get(dsl);
		if (DEBUG) System.out.println("LinearizingCombiner: unique method found "+uniqueMethod);
		if (DEBUG) System.out.println("LinearizingCombiner: unique method found "+uniqueMethod.getSignature());
		return uniqueMethod.invoke(dsl,(Object[])[name,args] );
	}
	
	protected Object invokePropertyMissingOnFirstDSL(Set<DSL> dsls, String name) {
        HashMap<DSL,MetaMethod> methods = new HashMap<DSL,MetaMethod>();
		for(DSL dsl : dslDefinitions) {
			if (DEBUG) System.out.println("LinearizingCombiner: try to find 'propertyMissing' in "+ dsl);
			MetaMethod method = ((GroovyObjectSupport)dsl).getMetaClass().pickMethod("propertyMissing",(Class[])[String.class]);
            if (method != null) {
            	methods.put(dsl,method);
            	if (SPEED_UP) break;
            }
		}
		if (methods.keySet().isEmpty()) {
			throw new MissingPropertyException(name, this.getClass());
		}
		//Ignore syntax conflicts and simply invoke the first interpreter found
		DSL dsl =(DSL)methods.keySet().iterator().next(); 
		MetaMethod uniqueMethod = (MetaMethod)methods.get(dsl);
		if (DEBUG) System.out.println("LinearizingCombiner: unique 'propertyMissing' method found "+uniqueMethod+" in "+dsl);
		if (DEBUG) System.out.println("LinearizingCombiner: unique 'propertyMissing' method found "+uniqueMethod.getSignature());
		return uniqueMethod.invoke(dsl,(Object[])[name]);
	}
	
	protected void invokePropertyMissingOnFirstDSL(Set<DSL> dsls, String name, Object args) {
        HashMap<DSL,MetaMethod> methods = new HashMap<DSL,MetaMethod>();
		for(DSL dsl : dslDefinitions) {
			if (DEBUG) System.out.println("LinearizingCombiner: try to find 'propertyMissing' in "+ dsl);
			MetaMethod method = ((GroovyObjectSupport)dsl).getMetaClass().pickMethod("propertyMissing",(Class[])[String.class, Object.class]);
            if (method != null) {
            	methods.put(dsl,method);
            	if (SPEED_UP) break;
            }
		}
		if (methods.keySet().isEmpty()) {
			throw new MissingPropertyException(name, this.getClass());
		}
		//Ignore syntax conflicts and simply invoke the first interpreter found
		DSL dsl =(DSL)methods.keySet().iterator().next(); 
		MetaMethod uniqueMethod = (MetaMethod)methods.get(dsl);
		if (DEBUG) System.out.println("LinearizingCombiner: unique 'propertyMissing' method found "+uniqueMethod);
		if (DEBUG) System.out.println("LinearizingCombiner: unique 'propertyMissing' method found "+uniqueMethod.getSignature());
		uniqueMethod.invoke(dsl,(Object[])[name,args] );
	}	
	
	public Object methodMissing(String name, Object args) {
		if (DEBUG) System.out.println("LinearizingCombiner: methodMissing "+name+" params="+args);
        HashMap<DSL,MetaMethod> methods = new HashMap<DSL,MetaMethod>();
		for(DSL dsl : dslDefinitions) {
			if (DEBUG) System.out.println("LinearizingCombiner: dsl="+dsl);
			MetaMethod method = ((GroovyObjectSupport)dsl).getMetaClass().pickMethod(name,classesArrayForObjectArray((Object[])args));
            if (method != null) {
            	methods.put(dsl,method);
            	if (SPEED_UP) break;
            }
		}
		if (DEBUG) System.out.println("LinearizingCombiner: methods found "+methods.size());
		if (methods.keySet().isEmpty()) {
			return invokeMethodMissingOnFirstDSL(new LinkedHashSet(dslDefinitions),name,args); //only raises exception if no methodMissing is is defined
		}
		//Ignore syntax conflicts and simply invoke the first interpreter found
		DSL dsl =(DSL)methods.keySet().iterator().next(); 
		MetaMethod uniqueMethod = (MetaMethod)methods.get(dsl);
		if (DEBUG) System.out.println("LinearizingCombiner: unique method found "+uniqueMethod);
		return uniqueMethod.invoke(dsl,(Object[])args);
	}

	public void propertyMissing(String name, Object value) { 
		if (DEBUG) System.out.println("LinearizingCombiner: propertyMissing "+name+" "+value);

		try {
			//try fining missing property (begin)
			if (DEBUG) System.out.println("LinearizingCombiner: propertyMissing "+name+" params="+value);
	        HashMap<DSL,MetaProperty> properties = new HashMap<DSL,MetaProperty>();
			for(DSL dsl : dslDefinitions) {
				if (DEBUG) System.out.println("LinearizingCombiner: dsl="+dsl);
				MetaProperty property = ((GroovyObjectSupport)dsl).getMetaClass().getMetaProperty(name);
	            if (null != property) {
	            	properties.put(dsl,property);
	            	if (SPEED_UP) break;
	            }
			}
			if (DEBUG) System.out.println("LinearizingCombiner: methods found "+properties.size());
			if (properties.keySet().isEmpty()) {
			    invokePropertyMissingOnFirstDSL(new LinkedHashSet(dslDefinitions),name,value); //only raises exception if no methodMissing is is defined
			}
			//try fining missing property (end)
		} catch (MissingPropertyException mpe) {
			synchronized (this) {
				internalContext.put(name, value);
			}
		}
	}


	public Object propertyMissing(String name) { 
		if (DEBUG) System.out.println("LinearizingCombiner: propertyMissing "+name);
        HashMap<DSL,MetaProperty> fields = new HashMap<DSL,MetaProperty>();
		for(DSL dsl : dslDefinitions) {
			MetaProperty field = ((GroovyObjectSupport)dsl).getMetaClass().getMetaProperty(name);
            if (null != field) { 
            	fields.put(dsl,field);
            	if (SPEED_UP) break;
            }
		}
		if (DEBUG) System.out.println("InterpreterCombiner: fields found "+fields);
		if (fields.keySet().isEmpty()) {
			
			try {
			    return invokePropertyMissingOnFirstDSL(new LinkedHashSet(dslDefinitions),name); //only raises exception if no methodMissing is is defined
			} catch (MissingPropertyException mpe) {
				//ignore and continue searching context
			}

			if(internalContext.containsKey(name)){
				return internalContext.get(name);
			} else {
				throw new MissingPropertyException("The LinearizingCombiner interpreter was unable to find the requested property "+name+" in "+dslDefinitions, name, this.getClass());
			}
		}
		//Ignore syntax conflicts and simply invoke the first interpreter found
		DSL dsl =(DSL)fields.keySet().iterator().next(); 
		MetaProperty uniqueField = (MetaProperty)fields.get(dsl);
		if (DEBUG) System.out.println("LinearizingCombiner: unique field found "+uniqueField.getName());
		return uniqueField.getProperty(dsl);
	}
	

}
