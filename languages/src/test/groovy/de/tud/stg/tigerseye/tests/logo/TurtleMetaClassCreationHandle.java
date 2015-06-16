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
package de.tud.stg.tigerseye.tests.logo;

import groovy.lang.MetaClass;
import groovy.lang.MetaClassRegistry;
import groovy.lang.MetaClassRegistry.MetaClassCreationHandle;

import org.javalogo.Turtle;

import de.tud.stg.tigerseye.lang.logo.ConcurrentLogo;

public class TurtleMetaClassCreationHandle extends MetaClassCreationHandle {
	
	public enum Strategy {
	    DETECT, PREVENT
	}
	
	protected Strategy strategy;
	
	protected ConcurrentLogo interpreter;
	
	public TurtleMetaClassCreationHandle(ConcurrentLogo interpreter, Strategy strategy) {
		this.interpreter = interpreter;
		this.strategy = strategy;
	}
	
	@Override
	protected MetaClass createNormalMetaClass(@SuppressWarnings("unchecked") Class theClass, MetaClassRegistry registry) {
		//System.out.println("Creating meta class for "+theClass);
		if (theClass.equals(Turtle.class)) {
			//System.out.println("Turtle class detected "+theClass);
			if (strategy == Strategy.DETECT) {
			    return new TurtleMetaClass(Turtle.class,interpreter);
			} else if (strategy == Strategy.PREVENT) {
				//System.out.println("PREVENTING: installing meta class "+theClass);
			    return new CollusionPreventingTurtleMetaClass(Turtle.class,interpreter);
			} else {
				throw new RuntimeException("invalid strategy.");
			}
		} else { 
			//System.out.println("Creating normal meta class "+theClass);
		    MetaClass normalMetaClass = super.createNormalMetaClass(theClass, registry);
		    return normalMetaClass;
		}
	}

}
