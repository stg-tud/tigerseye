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
package de.tud.stg.tigerseye.lang.atx;

import de.tud.stg.tigerseye.*;
import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * This class defines a DSL environment for working with control flow abstraction.
 */
public class AtxDSL {

	def DEBUG = false; 
	
	def dslsupport; 
		
	private final String BEGIN_LIST_KEY = "beginList"; 
	private final String COMMIT_LIST_KEY = "commitList"; 
	private final String ABORT_LIST_KEY = "abortList"; 

	private List getClosureList(String key) {
		List list = dslsupport.context[key];
		if (list == null) {
			list = new LinkedList();
			dslsupport.context[key] = list;
		}
		return list;
	}

	private void callAllInClosureList(String key) {
	    getClosureList(key).each { cl ->
            assert cl instanceof Closure;
            cl.delegate = dslsupport
            cl.resolvbeStrategy = Closure.DELEGATE_FIRST;
            cl.call();
        }
    }

	
	/* Literals */	
	
	/* Operations */
    
	//CONTROL FLOW ABSTRATIONS	
	public void begin(Closure beginClosure) {
		getClosureList(BEGIN_LIST_KEY).add(beginClosure);
	}
	
	public void commit(Closure commitClosure) {
		getClosureList(COMMIT_LIST_KEY).add(commitClosure);
	}
	
	public void abort(Closure abortClosure) {
		getClosureList(ABORT_LIST_KEY).add(abortClosure);
	}
	
	public void performBegins() {
		callAllInClosureList(BEGIN_LIST_KEY);
	}

	public void performCommits() {
		callAllInClosureList(COMMIT_LIST_KEY);
	}

	public void performAborts() {
		callAllInClosureList(ABORT_LIST_KEY);
	}
	
	//ATX OPERATIONS
	public void alias(String name, Object obj) {
		println "\\--Define alias $name for $obj"
	}

	public void dep(Object from, String type, Object to) {
		println "\\--Dependency $from $type $to"
	}

	public void terminate(Object obj) {
		println "\\--Terminate $obj"
	}
}

