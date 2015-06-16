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
package de.tud.stg.tigerseye.lang.logo.analysis

import java.util.List;
import java.util.Map;

import de.tud.stg.tigerseye.DSL;
import de.tud.stg.tigerseye.LinearizingCombiner;

public class ExpressionCounterCombiner extends LinearizingCombiner implements IExpressionCounter {

	public ExpressionCounterCombiner(IExpressionCounter... dslDefinitions) {
		super(dslDefinitions);
	}

	public ExpressionCounterCombiner(IExpressionCounter dsl1, IExpressionCounter dsl2) {
		super(dsl1,dsl2);
	}

	@Override
	public int getExpressionCounter() {
//		println "Combiner: "+dslDefinitions.inject (0) { int partSum, IExpressionCounter ec -> 
//		    return partSum + ec.getExpressionCounter() 
//		};
		return dslDefinitions.inject (0) { int partSum, IExpressionCounter ec ->
		    //println "Combiner: $partSum + ${ec.getExpressionCounter()}"
		    return partSum + ec.getExpressionCounter() 
		};
	}

	@Override
	public void reset() {
		dslDefinitions.each { IExpressionCounter ec -> 
		   ec.reset() 
		};
	}
	
//	public Object methodMissing(String name, Object args) {
//		println("ExpressionCounterCombiner: methodMissing "+name+" params="+args);
//	    return super.methodMissing(name, args);
//	}
//
//	public void propertyMissing(String name, Object args) {
//		println("ExpressionCounterCombiner: propertyMissing "+name+" params="+args);
//	    super.propertyMissing(name, args);
//	}
//
//	public Object propertyMissing(String name) {
//	    println("ExpressionCounterCombiner: propertyMissing "+name);
//		return super.methodMissing(name);
//    }
}
