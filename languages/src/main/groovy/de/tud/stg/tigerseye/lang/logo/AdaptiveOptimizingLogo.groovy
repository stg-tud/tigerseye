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
package de.tud.stg.tigerseye.lang.logo;

import de.tud.stg.tigerseye.*;
import org.javalogo.*;
import java.awt.Color;

/**
 * This version of Logo simulates costs of drawing operation by slowing them down by 100 ms.
 */
public class AdaptiveOptimizingLogo  extends OptimizingLogo implements IConcurrentLogo {
	 
	def DEBUG = false;
	
	public AdaptiveOptimizingLogo() {
		super();
	}
		
	/* Literals */

	/* Operations */
	public Closure app(String name) {
		assert name != null;
		assert !name.isEmpty();
		Closure function = functionNamesToClosure.get(name)
		assert function != null;
        
        Closure _functionClone = function.clone();
        
        Closure wrappedFunction = { Object... args ->
            Closure functionClone = function.clone();
        
            DSL profiler = new ProfilingLogo(this.getFunctionNamesToClosure());
            functionClone.delegate = profiler;
            functionClone.resolveStrategy = Closure.DELEGATE_FIRST;
            functionClone.call(*args);
            int baseCosts = profiler.getProgramDuration();
			//int baseCosts = 10000;
        
            Thread thread = Thread.currentThread();
		    Turtle perThreadTurtle = threadToTurtle.get(thread);
		    int initCosts = Math.abs(2*perThreadTurtle.position.y) + 90 + 
		    				Math.abs(2*perThreadTurtle.position.x) + 90 + 
		    				Math.abs(headingToAngular(perThreadTurtle.heading));  
		
		    println "AdaptOpt.decision: should optimize function application? baseCosts=$baseCosts initCosts=$initCosts"    		
		    if (baseCosts > initCosts) {
			    println "AdaptOpt.decision: optimizing function"    		
			    Closure optimizedFunction = super.app(name).clone();
			    optimizedFunction.delegate = bodyDelegate;
			    optimizedFunction.resolveStrategy = Closure.DELEGATE_FIRST;
			    return optimizedFunction.call(*args);
		    } else {
			    println "AdaptOpt.decision: not optimizing function"    		
		    	functionClone.delegate = bodyDelegate;
		    	functionClone.resolveStrategy = Closure.DELEGATE_FIRST;
		    	return functionClone.call(*args);
		    }
        }
        
        return wrappedFunction;
	}
	
	/* Abstraction Operators */
	
}