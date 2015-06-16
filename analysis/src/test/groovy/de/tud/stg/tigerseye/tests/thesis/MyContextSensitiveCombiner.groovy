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
package de.tud.stg.tigerseye.tests.thesis;

import groovy.lang.Closure;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import de.tud.stg.tigerseye.BlackBoxCombiner; 
import de.tud.stg.tigerseye.IFunctional;
import de.tud.stg.tigerseye.Interpreter;
import de.tud.stg.tigerseye.lang.logo.ISimpleLogo;
import de.tud.stg.tigerseye.transformation.syntax.KeywordFilterTransformation;

/**
 * Special Combiner that resolves the syntax conflict between Functional and Structural.
 * @author Tom Dinkelaker
 *
 */
public class MyContextSensitiveCombiner extends BlackBoxCombiner {
	
	IFunctional functional;
	ISimpleLogo logo;
	
	public MyContextSensitiveCombiner(IFunctional functional, ISimpleLogo logo) {
		super(functional,logo);
		this.functional = functional;
		this.logo = logo;
	}
	
	public void define(HashMap params, Closure body) {
		List<Class> ifc = new LinkedList<Class>();
		ifc.add(ISimpleLogo.class);
		functional.define(params, body);
		System.out.println("replace delegate by filtered delegate");
		body.setDelegate(new KeywordFilterTransformation(body.getDelegate(), ifc));
		body.setResolveStrategy(Closure.DELEGATE_ONLY);
	}
	
//	public Closure apply(String name) {
//	    Closure cl = functional.apply(name);
//		System.out.println("function uses delegate "+cl.getDelegate()); 
//		cl.setResolveStrategy(Closure.DELEGATE_ONLY);
//	    return cl;
//	}


}
