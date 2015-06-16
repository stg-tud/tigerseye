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
package de.tud.stg.tigerseye.lang.logo.analysis;

import groovy.lang.Closure;

import de.tud.stg.tigerseye.*;
import org.javalogo.*;
import java.awt.Color;
import de.tud.stg.tigerseye.lang.logo.*;

/**
 * Implements a total abstract analysis for any language in a domain with void semantics.
 * This abstract analysis ignore every expression type.
 * Using the analysis alone does not make sense, 
 * but subclasses that extend this class can easily implement total analyses.
 */
public class AnyAbstractInterpretation extends Interpreter  {
		 
	public AnyAbstractInterpretation() {	}
	
	public Object methodMissing(String name, Object args) {
		//println ""+args.getClass();
	    if (args.length > 0) {
	        def lastParam = args[args.length-1];
	        if (lastParam instanceof Closure) {
	        	def body = (Closure)lastParam.clone();
	        	body.delegate = bodyDelegate;
	        	body.DELEGATE_FIRST;
	        	return body.call(args);
	        }
	    }
	}

	public Object propertyMissing(String name) { 

	}

	public void propertyMissing(String name, Object value) { 

	}
}