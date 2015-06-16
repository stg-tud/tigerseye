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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.codehaus.groovy.runtime.InvokerHelper;

import de.tud.stg.tigerseye.exceptions.SyntaxConflictException;

import groovy.lang.MetaMethod;
import groovy.lang.MetaProperty;
import groovy.lang.MissingMethodException;
import groovy.lang.MissingPropertyException;

public class BlackBoxCombiner extends InterpreterCombiner {
	
	final boolean DEBUG = false;
	
	protected boolean SPEED_UP = true; //Can optimize the lookup process, because all members are enforced to be unique at creation time, we can skip the uniqueness tests 

	public BlackBoxCombiner(DSL... dslDefinitions) {
		super(dslDefinitions);
		checkAllLanguageFacades(new HashSet(Arrays.asList(dslDefinitions)));
	}

	public BlackBoxCombiner(DSL dslDefinition, Map<String, Object> context) {
		super(dslDefinition, context);
		checkAllLanguageFacades(dslDefinitions);
	}

	public BlackBoxCombiner(DSL dslDefinition1, DSL dslDefinition2,
			Map<String, Object> context) {
		super(dslDefinition1, dslDefinition2, context);
		checkAllLanguageFacades([dslDefinition1,dslDefinition2] as Set);
	}

	public BlackBoxCombiner(DSL dslDefinition1, DSL dslDefinition2) {
		super(dslDefinition1, dslDefinition2);
		checkAllLanguageFacades([dslDefinition1,dslDefinition2] as Set);
	}

	public BlackBoxCombiner(Set<DSL> dslDefinitions) {
		super(dslDefinitions);
		checkAllLanguageFacades(dslDefinitions);
	}

	public BlackBoxCombiner(Set<DSL> dslDefinitions, Map<String, Object> context) {
		super(dslDefinitions, context);
		checkAllLanguageFacades(dslDefinitions);
	}

	public void checkAllLanguageFacades(Set<DSL> dslDefinitions) {
		for (DSL dsl : dslDefinitions) {
			for (DSL other : dslDefinitions) {
				checkLanguageFacadesHaveDisjointExpressionTypes(dsl,other);			 
			}
		}
	}
	
	private List<MetaMethod> filterDefaultMethods(List<MetaMethod> methods) {
		List<MetaMethod> filteredMethods = new LinkedList<MetaMethod>();
		
		Class classOfInterpreter = Interpreter.class;
		Class classOfGroovyObjectSupport = GroovyObjectSupport.class; 
		
		for (MetaMethod m1 : methods) {
			boolean contains = false;
			for (MetaMethod m2 : classOfInterpreter.metaClass.getMethods()) {
			    String sig1 = m1.getSignature();
			    String sig2 = m2.getSignature();
		        if (sig1.equals(sig2)) {
			      //println "Filter: " + m1.toString();
  		          contains = true;
  		          break;
		        }
		    } 
			if (!contains) {
		        //println "Real: " + m1.toString();
				filteredMethods.add(m1);
			}
		}
		
		return filteredMethods;
	}
	
	public void checkLanguageFacadesHaveDisjointExpressionTypes(DSL one, DSL other) {
		if (one == other) return;
		//if (one.equals(other)) return;
		
		for (MetaMethod m1 : filterDefaultMethods(one.metaClass.getMethods())) {
			for (MetaMethod m2 : filterDefaultMethods(other.metaClass.getMethods())) {
			    //println "Cmp: " + m1.getSignature().toString() + " <-> " + m2.getSignature().toString();
			    if (m1.getSignature().equals(m2.getSignature())) {
			    	//Conflict detected
			    	throw new SyntaxConflictException("The BlackBoxCombiner detected a syntax conflict (non-unique signature signature '$m1.signature') between the combined DSLs '$one' and '$other' in keyword '$m1'.");
			    }
			}
		}
	}
	
//// now in InterpreterCombiner	
//	public Object methodMissing(String name, Object args) {
//		if (DEBUG) System.out.println("BlackBoxCombiner: methodMissing "+name+" params="+args);
//        HashMap<DSL,MetaMethod> methods = new HashMap<MetaMethod>();
//		for(DSL dsl : dslDefinitions) {
//			MetaMethod method = dsl.metaClass.pickMethod(name,classesArrayForObjectArray(args))
//            if (method != null) {
//            	methods.put(dsl,method);
//            	if (SPEED_UP) break;
//            }
//		}
//		if (DEBUG) System.out.println("BlackBoxCombiner: methods found ${methods.size()}");
//		if (methods.keySet().isEmpty()) {
//			throw new MissingMethodException(name, this.getClass(), (Object[]) args);
//		}
//		if (methods.keySet().size()>1) throw new SyntaxConflictException("The BlackBoxCombiner detected a syntax conflict (non-unique signature '$method.signature') between the combined DSLs '$dsls' in keyword '$methods'.");
//		DSL dsl =(DSL)methods.keySet().iterator().next(); 
//		MetaMethod uniqueMethod = (MetaMethod)methods.get(dsl);
//		if (DEBUG) System.out.println("BlackBoxCombiner: unique method found $uniqueMethod");
//		return uniqueMethod.invoke(dsl,args);
//	}
//	
//	private Class[] classesArrayForObjectArray(Object[] args) {
//		(Class[]) args.collect { Object obj -> obj.class }
//	}
//
//	public Object propertyMissing(String name) { 
//		if (DEBUG) System.out.println("BlackBoxCombiner: propertyMissing "+name);
//        HashMap<DSL,MetaProperty> fields = new HashMap<MetaProperty>();
//		for(DSL dsl : dslDefinitions) {
//			MetaProperty field = dsl.metaClass.getMetaProperty(name);
//            if (field != null) { 
//            	fields.put(dsl,field);
//            	if (SPEED_UP) break;
//            }
//		}
//		if (DEBUG) System.out.println("BlackBoxCombiner: fields found $fields");
//		if (fields.keySet().isEmpty()) {
//			super.propertyMissing(name);
//		}
//		if (fields.keySet().size()>1) throw new SyntaxConflictException("The BlackBoxCombiner detected a syntax conflict (non-unique field '$name') between the combined DSLs '$dsls' in keyword '$fields'.");
//		DSL dsl =(DSL)fields.keySet().iterator().next(); 
//		MetaProperty uniqueField = (MetaProperty)fields.get(dsl);
//		if (DEBUG) System.out.println("BlackBoxCombiner: unique field found $uniqueField.name");
//		return uniqueField.getProperty(dsl);
//	}
	
}
