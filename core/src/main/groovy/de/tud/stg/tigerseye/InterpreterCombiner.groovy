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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.tud.stg.tigerseye.exceptions.SyntaxConflictException;

public class InterpreterCombiner extends Interpreter implements InternalContextDSL {

	protected final boolean DEBUG = false;

	protected boolean SPEED_UP = false; //Cannot optimize the lookup process, because all members are not enforced to be unique  

	protected Set<DSL> dslDefinitions;
	protected Map<String, Object> internalContext;
	
	// BEGIN:[ OR-2009-07-25: Thread-Safety Modification ]:::::::::::::::::::
	public void setInternalContext(Map<String, Object> context) {
		synchronized (this) {
			this.internalContext = context;
		}
	}

	public Map<String, Object> getInternalContext() {
		synchronized (this) {
			return this.internalContext;
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

	public InterpreterCombiner(DSL... dslDefinitions) {
		this(new java.util.HashSet<DSL>(java.util.Arrays.asList(dslDefinitions)), new java.util.HashMap<String,Object>());
	}

	public InterpreterCombiner(DSL dslDefinition, Map<String, Object> context) {
		this.dslDefinitions = new java.util.HashSet<DSL>();
		this.dslDefinitions.add(dslDefinition);
		this.internalContext = context;
		setCombinerAsBodyDelegateOfAllInterpreters();
	} 

	public InterpreterCombiner(DSL dslDefinition1, DSL dslDefinition2, Map<String, Object> context) {
		this.dslDefinitions = new java.util.HashSet<DSL>();
		this.dslDefinitions.add(dslDefinition1);
		this.dslDefinitions.add(dslDefinition2);
		this.internalContext = context;
		setCombinerAsBodyDelegateOfAllInterpreters();
	} 

	public InterpreterCombiner(DSL dslDefinition1, DSL dslDefinition2) {
		this.dslDefinitions = new java.util.HashSet<DSL>();
		this.dslDefinitions.add(dslDefinition1);
		this.dslDefinitions.add(dslDefinition2);
		this.internalContext =  new java.util.HashMap<String,Object>();
		setCombinerAsBodyDelegateOfAllInterpreters();
	} 

	public InterpreterCombiner(Set<DSL> dslDefinitions) {
		this(dslDefinitions, new java.util.HashMap<String,Object>());
	}

	public InterpreterCombiner(Set<DSL> dslDefinitions, Map<String, Object> context) {
		this.dslDefinitions = dslDefinitions;
		this.internalContext = context;
		setCombinerAsBodyDelegateOfAllInterpreters();
	} 

	public Object eval(Closure dslClosure) {
		dslClosure.setResolveStrategy(Closure.DELEGATE_FIRST); //Closure.DELEGATE_FIRST enables writing into properties defined by DSLs and prevent the creation of a local variable
		dslClosure.setDelegate(this);
		return dslClosure.call();
	}

//Old version
//	public Object methodMissing(String name, Object args) {
//		//Necessary, since methodMissing is required to have (string,object) signature.
//		for(DSL dsl : dslDefinitions){
//			try{
//				return InvokerHelper.invokeMethod(dsl, name, args);
//			} catch(MissingMethodException e){
//				//ignore exception to continue search
////			} catch (Exception e2) {
////				if (DEBUG) System.out.println(this.getClass().getName()+":: error in the implementation of a DSL operation (keyword="+name+",args="+String.valueOf(args)+")");
////				if (DEBUG) System.out.println("--- ERROR IN THE DSL IMPLEMENTATION ---");
////				if (DEBUG) e2.printStackTrace(System.out);
////				if (DEBUG) System.out.println("---------------------------------------");
////				throw new DSLException("Error in the implementation of a DSL operation (keyword=$name,args=$args,dsls=$dslDefinitions).",e2);
//			}
//		}
//		throw new MissingMethodException(name, this.getClass(), (Object[]) args);
//	}
	
	private Class[] classesArrayForObjectArray(Object[] args) {
		Class[] classes = new Class[args.length];
		for (int i=0; i < args.length; i++) {
			if (args[i] != null) {
			    classes[i] = args[i].getClass();
			} else {
				classes[i] = null;
			}
		}
		return classes;
	}
	
	private Object invokeMethodMissingOnFirstDSL(Set<DSL> dsls, String name, Object args) {
        HashMap<DSL,MetaMethod> methods = new HashMap<DSL,MetaMethod>();
		for(DSL dsl : dslDefinitions) {
			if (DEBUG) System.out.println("InterpreterCombiner: try to find 'methodMissing' in "+ dsl);
			MetaMethod method = dsl.getMetaClass().pickMethod("methodMissing",(Class[])[(String.class),(Object[].class)]);
            if (method != null) {
            	methods.put(dsl,method);
            	if (SPEED_UP) break;
            }
		}
		if (methods.keySet().isEmpty()) {
			throw new MissingMethodException(name, this.getClass(), (Object[]) args);
		}
		if (methods.keySet().size()>1) throw new RuntimeException(new SyntaxConflictException("The InterpreterCombiner detected a syntax conflict (non-unique signature 'methodMissing') between the combined DSLs '"+dsls+"' in keyword '"+methods+"'."));
		DSL dsl =(DSL)methods.keySet().iterator().next(); 
		MetaMethod uniqueMethod = (MetaMethod)methods.get(dsl);
		if (DEBUG) System.out.println("InterpreterCombiner: unique method found "+uniqueMethod);
		if (DEBUG) System.out.println("InterpreterCombiner: unique method found "+uniqueMethod.getSignature());
		return uniqueMethod.invoke(dsl,(Object[])[name,args]);
	}
	
	public Object methodMissing(String name, Object args) {
		if (DEBUG) System.out.println("InterpreterCombiner: methodMissing "+name+" params="+args);
        HashMap<DSL,MetaMethod> methods = new HashMap<DSL,MetaMethod>();
		for(DSL dsl : dslDefinitions) {
			MetaMethod method = dsl.getMetaClass().pickMethod(name,classesArrayForObjectArray((Object[])args));
            if (method != null) {
            	methods.put(dsl,method);
            	if (SPEED_UP) break;
            }
		}
		if (DEBUG) System.out.println("InterpreterCombiner: methods found "+methods.size());
		if (methods.keySet().isEmpty()) {
			return invokeMethodMissingOnFirstDSL(dslDefinitions,name,args); //only raises exception if no methodMissing is is defined
		}
		if (methods.keySet().size()>1) throw new RuntimeException(new SyntaxConflictException("The InterpreterCombiner detected a syntax conflict (non-unique signature '"+name+"') between the combined DSLs '"+dslDefinitions+"' in keyword '"+methods+"'."));
		DSL dsl =(DSL)methods.keySet().iterator().next(); 
		MetaMethod uniqueMethod = (MetaMethod)methods.get(dsl);
		if (DEBUG) System.out.println("InterpreterCombiner: unique method found "+uniqueMethod);
		return uniqueMethod.invoke(dsl,(Object[])args);
	}

	public void propertyMissing(String name, Object value) {
		if (DEBUG) System.out.println("InterpreterCombiner: propertyMissing "+name+" "+value);

		// TODO should property write access also check the dslDefinitions properties?

		for(DSL dsl : dslDefinitions) {
			if (dsl instanceof IEnvironment) {
				((IEnvironment)dsl).let(name, value); //TODO Write in all environments, if there are multiple?
			} 
		}
		
		synchronized (this) {
			internalContext.put(name, value); //TODO Should also write into combiner's context
		}
	}

//Old version
//	public Object propertyMissing(String name) { 
//		if (DEBUG) System.out.println("InterpreterCombiner: propertyMissing "+name);
//		for(DSL dsl : dslDefinitions){
//			try{
//				return InvokerHelper.getProperty(dsl, name);
//			}catch(MissingPropertyException e){
//				//ignore exception to continue search.
//			}
//		}
//		if(internalContext.containsKey(name)){
//			return internalContext.get(name);
//		} else {
//			throw new MissingPropertyException("The InterpreterCombiner interpreter was unable to find the requested property "+name+" in "+dslDefinitions, name, this.getClass());
//		}
//	}

	public Object propertyMissing(String name) { 
		if (DEBUG) System.out.println("InterpreterCombiner: propertyMissing "+name);
        HashMap<DSL,MetaProperty> fields = new HashMap<DSL,MetaProperty>();
		for(DSL dsl : dslDefinitions) {
			MetaProperty field = dsl.getMetaClass().getMetaProperty(name);
            if (field != null) { 
            	fields.put(dsl,field);
            	if (SPEED_UP) break;
            }
		}
		if (DEBUG) System.out.println("InterpreterCombiner: fields found "+fields);
		if (fields.keySet().isEmpty()) {
			
			//try to get from an environment
			for(DSL dsl : dslDefinitions) {
				if (dsl instanceof IEnvironment) {
					return ((IEnvironment)dsl).get(name); 
				}
			}
			
			//TODO Maybe check whether any DSL implements a special missingProperty method before accessing the  map
			
			//get from combiner local environment
			if(internalContext.containsKey(name)){
				return internalContext.get(name);
			} else {
				throw new MissingPropertyException("The InterpreterCombiner interpreter was unable to find the requested property '"+name+"' in "+dslDefinitions, name, this.getClass());
			}
		}
		if (fields.keySet().size()>1) throw new RuntimeException(new SyntaxConflictException("The InterpreterCombiner detected a syntax conflict (non-unique field '"+name+"') between the combined DSLs '"+dslDefinitions+"' in keyword '"+fields+"'."));
		DSL dsl =(DSL)fields.keySet().iterator().next(); 
		MetaProperty uniqueField = (MetaProperty)fields.get(dsl);
		if (DEBUG) System.out.println("InterpreterCombiner: unique field found "+uniqueField.getName());
		return uniqueField.getProperty(dsl);
	}
	

}
