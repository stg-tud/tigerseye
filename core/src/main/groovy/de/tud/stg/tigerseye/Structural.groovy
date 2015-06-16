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
package de.tud.stg.tigerseye

import java.util.HashMap;
import java.util.Map;

/**
 * Embeds a simple data type system in which each type is defined by a set of slots with associated types.
 * @author Tom Dinkelaker
 *
 */
class Structural extends EnvironmentInterpreter implements IStructural {

	public Structural() {
		// TODO Auto-generated constructor stub
	}
	
	public Object init(HashMap params) {
    	assert params.name != null;
		
		println "${this} : types=$__globalEnvironment"
		println "${this} : params.name=$params.name"
    	HashMap dataType = __globalEnvironment.get(params.name);
		println "${this} : dataType=$dataType"
    	
    	if (dataType == null) throw new StructuralTypeException("The data type '$params.name' undefined.");
    	
    	//Check that the initializer call does initialize all slots
    	Set slotNames = dataType.keySet();
    	slotNames.each { String slotName ->
    		if (!params.keySet().contains(slotName)) throw new StructuralTypeException("The initializer does not provide a value for slot '$slotName'");
    		Class slotType = dataType.get(slotName);
    		Object value = params.get(slotName);
    		if (!(slotType.isInstance(value))) throw new StructuralTypeException("The value for slot '$slotName' does not have type '$slotType'");
    		
    	}

        HashMap object = new HashMap(params);
        object.remove("name");
    	
		return object;
	}	
	
    public void define(HashMap params, Closure body) {
    	assert params.name != null;
    	
    	def builder = new DataTypeBuilder();
    	
    	println "Structural.define evaluates type definition"
    	body.delegate = builder;
    	body.resolveStrategy = Closure.DELEGATE_FIRST;
    	body.call();
    	
    	HashMap definedDataType = builder.getDataType();
    	
    	__globalEnvironment.put(params.name,definedDataType);
    }

	
	/* Inline Meta Level */
	protected Object methodMissing(String name, Object args) {
		println "${this} : types=$__globalEnvironment"
		if (environment.get(name) != null) {
		  HashMap map = new HashMap(args[0]); 
		  map.put("name",name);
          init(map);
		} else {
			throw new MissingMethodException(name, this.class, args);
	    }
	}
}
