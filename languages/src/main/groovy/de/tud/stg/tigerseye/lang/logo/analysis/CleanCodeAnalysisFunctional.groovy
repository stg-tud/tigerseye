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
import de.tud.stg.tigerseye.exceptions.AnalysisException;
import de.tud.stg.tigerseye.lang.logo.IFunctionalLogo 

/**
 * Enforces that every abstraction operator contains at most 10 expressions.
 */ 
public class CleanCodeAnalysisFunctional extends AbstractInterpretationFunctional {

	final boolean DEBUG = false;
	
	private DSL exprCounter;
	
	public CleanCodeAnalysisFunctional(DSL exprCounter) {
		this.exprCounter = exprCounter;
	}
	
	protected int performExpressionCounting(Closure originalBody) {
		Closure body = originalBody.clone();
		exprCounter.reset();
		body.delegate = exprCounter;
		body.resolveStrategy = Closure.DELEGATE_FIRST;
		if (DEBUG) println "CleanCodeAnalysisCompleteLogo: staring express count ${body.getDelegate()}"
		if (DEBUG) println "CleanCodeAnalysisCompleteLogo: staring express count ${exprCounter.getExpressionCounter()}"
		body.call();
		if (DEBUG) println "CleanCodeAnalysisCompleteLogo: end express count ${exprCounter.getExpressionCounter()}"
		if (DEBUG) println "CleanCodeAnalysisCompleteLogo: ${exprCounter.getExpressionCounter()}"
		return exprCounter.getExpressionCounter();
	}
	
	/* Literals */

	/* Operations */
	
	/* Abstraction Operators */
	public void fun(String name, Closure body) {
		def exprCnt = performExpressionCounting(body);
		if (DEBUG) println "fun $name: "+exprCnt;
		if (exprCnt > 10) throw new AnalysisException("Too much expressions ($exprCnt) in function '$name'.");
		super.fun(name,body);
	}
	
//	/* Inline Meta Level */
//	private Object methodMissing(String name, Object args) {
//        def function = app(name); 
//		if (function != null) {
//            function.call(*args);
//		} else {
//			throw new MissingMethodException(name, this.class, args);
//	    }
//	}
	
}