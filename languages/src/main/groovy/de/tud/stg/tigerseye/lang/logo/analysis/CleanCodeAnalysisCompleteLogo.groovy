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

import de.tud.stg.tigerseye.*;
import org.javalogo.*;
import de.tud.stg.tigerseye.exceptions.AnalysisException;
import de.tud.stg.tigerseye.lang.logo.IFunctionalLogo 

/**
 * This version of Logo simulates costs of drawing operation by slowing them down by 100 ms.
 */ 
public class CleanCodeAnalysisCompleteLogo extends AbstractInterpretationCompleteLogo {
	
	final boolean DEBUG = false
	
	private DSL exprCounter;
	
	public CleanCodeAnalysisCompleteLogo(DSL exprCounter) {
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
	public void turtle(HashMap params, Closure body) {
		def exprCnt = performExpressionCounting(body);
		if (DEBUG) println "turtle $params.name: "+exprCnt;
		if (exprCnt > 10) throw new AnalysisException("Too much expressions ($exprCnt) in turtle '$params.name'.");
		super.turtle(params, body); 
	}
	
	public void repeat(int n, Closure body) {
		def exprCnt = performExpressionCounting(body);
		if (DEBUG) println "repeat: "+exprCnt;
		if (exprCnt > 10) throw new AnalysisException("Too much expressions ($exprCnt) in repeat.");
		super.repeat(n, body); 
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