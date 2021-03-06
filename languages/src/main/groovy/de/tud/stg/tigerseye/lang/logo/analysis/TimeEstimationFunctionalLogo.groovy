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
import java.util.HashMap;

import de.tud.stg.popart.dslsupport.logo.AbstractFunctionalLogo 
import de.tud.stg.tigerseye.lang.logo.IFunctionalLogo 


import de.tud.stg.tigerseye.*;
import org.javalogo.*;

/**
 * This version of Logo approximates the costs of drawing operation by simulating the logo program under time constraints.
 */
public class TimeEstimatioFunctionalLogo extends AbstractInterpretationFunctionalLogo implements IFunctionalLogo { 
	
	def DEBUG = false;
	
	public TimeEstimatioFunctionalLogo() {
		super();
	}	
	
	public TimeEstimatioFunctionalLogo(HashMap<String,Closure> enclosedFunctionNamesToClosure) {
		super();
		
	}
	
	protected int programDuration = 0;

	protected int maxTurtleDuration = 0;
	
	public int getProgramDuration() {
		//println "getProgramDuration: programDuration is $programDuration , max is $maxTurtleDuration"
		return programDuration + maxTurtleDuration;
	}
	
	public void reset() { 
		programDuration = 0;
		maxTurtleDuration = 0;
	}
	
	public TimeEstimatioFunctionalLogo createNestedProfiler() {
		DSL profiler = new TimeEstimatioFunctionalLogo(this.getFunctionNamesToClosure()); //the current profiler gets the enclosing profiler of the nested profiler
		
        //make the functions of the enclosing interpreter (i.e., this) availbale in the nested profiler
		if (DEBUG) println "propagating functions from ${this} to $profiler";
        HashMap<String,Closure> fNamesToClosure = new HashMap<String,Closure>();  

        if (DEBUG) println "propagating functions $functionNamesToClosure";
		if (DEBUG) println "propagating keys ${this.functionNamesToClosure.keySet()}";

		Iterator<String> it = null;
		synchronized (functionNamesToClosure.keySet()) {
			Set keys = this.functionNamesToClosure.keySet();
			it = new HashSet(keys).iterator(); 
		}
        
        while (it.hasNext()) {
        	String name = it.next();
        	if (DEBUG) println "nesting function $name";
			Closure fClosure = this.functionNamesToClosure.get(name).clone();
			fClosure.delegate = profiler;
			fClosure.resolveStrategy = Closure.DELEGATE_ONLY;
			fNamesToClosure.put(name,fClosure);
		}
		profiler.functionNamesToClosure = fNamesToClosure;
		
        return profiler;       		
	}
		
	/* Literals */

	/* Operations */
	public void forward(int n) { programDuration += 2*n; if (DEBUG) println "${this} profiling fd $programDuration"; }
	public void backward(int n) { programDuration += 2*n; if (DEBUG) println "${this} profiling bd $programDuration"; }
	public void right(int n) { programDuration += 1*n; if (DEBUG) println "${this} profiling rt $programDuration"; }
	public void left(int n) { programDuration += 1*n; if (DEBUG) println "${this} profiling lt $programDuration"; }

	/**
	 * This method approximates the execution time of a repeat command.
	 * Approximates n time the execution time of the closure.
	 */
	public void repeat(int _times, Closure _choreography) {
		DSL profiler = createNestedProfiler();
		Closure choreography = _choreography.clone();		
    	choreography.delegate = profiler;
    	choreography.resolveStrategy = Closure.DELEGATE_ONLY;
        choreography.call();
		programDuration += _times * profiler.getProgramDuration();

		this.putAllFunctionNamesToClosure(profiler.getFunctionNamesToClosure()); // must copy functions defined in the enclosed turtle structures
	}
	
	/**
	 * This method approximates the execution time of a turtle abstraction oprator.
	 * Approximates the execution time of the closure.
	 * Looks up the execution time of other turtle (only taking into account the slowest turtle).
	 */
	public void turtle(HashMap params, Closure _choreography) {
		if (DEBUG) println "  ${this} profiling abstraction turtle of ${params.name} before duration is $programDuration"
		DSL profiler = createNestedProfiler();
		if (DEBUG) println "  ${profiler} used to profile abstraction turtle"
		Closure choreography = _choreography.clone();		
    	choreography.delegate = profiler;
		choreography.resolveStrategy = Closure.DELEGATE_ONLY;
		if (DEBUG) println "  ${profiler} starts profiling ..."
        choreography.call();
		if (DEBUG) println "  ${profiler} ... ends profiling"
		
		int turtleDuration = profiler.getProgramDuration();
		maxTurtleDuration = Math.max(maxTurtleDuration,turtleDuration);
		if (DEBUG) println "  ${this} profiled duration of ${params.name} is $turtleDuration , max is ${maxTurtleDuration}"
		if (DEBUG) println "  ${this} profiling abstraction turtle of ${params.name} after duration is $programDuration"
		
		this.putAllFunctionNamesToClosure(profiler.getFunctionNamesToClosure()); // must copy functions defined in the enclosed turtle structures
	}
	
	public void go() {
		if (DEBUG) println "${this} profiling go $programDuration"
		//ignore actual thread execution
	}

	public Closure app(String name) {
		if (DEBUG) println "Application of function $name in functions: "+this.functionNamesToClosure.keySet();
		
	    //retrieve function body
	    Closure result = functionNamesToClosure.get(name);
        assert (result != null);
	    
		if (DEBUG) println "${this} profiling clones closure "		
		result = result.clone();
		result.delegate = bodyDelegate;
		result.resolveStrategy = Closure.DELEGATE_ONLY;
		if (DEBUG) println "${this} profiling app $programDuration"
		return result;
	}
	
	/* Abstraction Operators */
	public void fun(String name, Closure body) {
		if (DEBUG) println "${this} profiling abstraction fun $programDuration"
		super.fun(name,body);
		if (DEBUG) println "${this} profiling abstraction fun $programDuration"
	}
	
}