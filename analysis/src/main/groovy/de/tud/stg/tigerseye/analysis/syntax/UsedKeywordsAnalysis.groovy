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
package de.tud.stg.tigerseye.analysis.syntax;

import groovy.lang.Closure;
import de.tud.stg.tigerseye.Interpreter;

/**
 * This is an abstract interpreter that can be used to determine all keywords a DSL program uses.
 * @author Tom Dinkelaker
 */
public class UsedKeywordsAnalysis extends Interpreter {

	private final boolean DEBUG = false;
	
	/**
	 * When accessing a literal using its accessors, the analysis does not treat this as an operation (default true).
	 */
	public final boolean OPTION_IGNORE_ACCESSOR_KEYWORDS = true; 
	
	/**
	 * When containing nested abstractions, the analysis also analyses the nested structure (default true).
	 */
	public final boolean OPTION_IGNORE_NESTED_KEYWORDS = false; 
	 
	/**
	 * The set of used keywords by the program.
	 */
	private Set<String> keywords = new HashSet();

	/**
	 * The set of used literal keywords by the program.
	 */
	private Set<String> literals = new HashSet();
	
	/**
	 * The set of used operation keywords by the program.
	 */
	private Set<String> operations = new HashSet();
	
	/**
	 * The set of used abstraction keywords by the program.
	 */
	private Set abstractions = new HashSet();
	
	public Object methodMissing(String name, Object args) {
	    if (DEBUG) println this.getClass().getName()+":: DSL program uses keyword '$name'";
    	if (name.startsWith("get") && name.size() > 3) {
    		//Special case: a method call starting with "get" may also be mapped to the getter method
            def firstLetter = name[3]
        	def remainderLetters = name[4..name.size()-1];
    	    def keywordName = firstLetter.toLowerCase()+remainderLetters; 
    	    if (DEBUG) println this.getClass().getName()+":: DSL program uses getter literal keyword '$keywordName'";
    	    if (DEBUG) println this.getClass().getName()+":: \\-- adapted keyword name firstLetter='$firstLetter' remainderLetters='$remainderLetters' -> '$keywordName'";
    	    keywords.add(keywordName);
    	    literals.add(keywordName);
    	    if (OPTION_IGNORE_ACCESSOR_KEYWORDS) return;
    	}
  		  
    	if (name.startsWith("set") && name.size() > 3) {
    		//Special case: a method call starting with "set" may also be mapped to the setter method
            def firstLetter = name[3]
        	def remainderLetters = name[4..name.size()-1];
    	    def keywordName = firstLetter.toLowerCase()+remainderLetters; 
    	    if (DEBUG) println this.getClass().getName()+":: DSL program uses setter literal keyword '$keywordName'";
    	    if (DEBUG) println this.getClass().getName()+":: \\-- adapted keyword name firstLetter='$firstLetter' remainderLetters='$remainderLetters' -> '$keywordName'";
    	    keywords.add(keywordName);
    	    literals.add(keywordName);
    	    if (OPTION_IGNORE_ACCESSOR_KEYWORDS) return;
    	}
 	
    	//default case: a method called 
    	if (DEBUG) println this.getClass().getName()+":: DSL program uses keyword '$name' with args=${args} args=${args.length}";
    	keywords.add(name);
  		
    	Object lastParam = null;
    	if (DEBUG) println this.getClass().getName()+":: \\-- 1";    	    	
    	if (args.length > 0) {
        	if (DEBUG) println this.getClass().getName()+":: \\-- 2";    	    	
    		lastParam = args[args.length - 1];
        	if (DEBUG) println this.getClass().getName()+":: \\-- 3";    	    	
    	}
    	if (DEBUG) println this.getClass().getName()+":: \\-- 4";    	    	

    	if (DEBUG) println this.getClass().getName()+":: \\-- args[last]="+lastParam;    	    	
    	if (DEBUG) println this.getClass().getName()+":: \\-- args[last].class=${lastParam?.class}";    	    	
    	if (lastParam instanceof Closure) {
            //Special case: nested abstraction 
    		if (DEBUG) println this.getClass().getName()+":: DSL program uses nested abstraction keyword '$name'";
        	abstractions.add(name);    		
        	if (!OPTION_IGNORE_NESTED_KEYWORDS) {
       		    if (DEBUG) println this.getClass().getName()+":: DSL program starts evaluating body of nested abstraction keyword '$name'";
				Closure body = lastParam.clone();
				body.delegate = bodyDelegate;
				body.resolveStrategy = Closure.DELEGATE_FIRST;
				body.call();
       		    if (DEBUG) println this.getClass().getName()+":: DSL program finished evaluation of body of nested abstraction keyword '$name'";
        	}
    	} else {
    		//Default case: operation
    	   	if (DEBUG) println this.getClass().getName()+":: DSL program uses operation keyword '$name'";
        	operations.add(name);
    	}
		return {};
	}
    
    public void propertyMissing(String name, Object value) { 
		if (DEBUG) println this.getClass().getName()+":: DSL program uses literal keyword '$name'";
		keywords.add(name);
	    literals.add(name);
    }
    
    public Object propertyMissing(String name) { 
		if (DEBUG) println this.getClass().getName()+":: DSL program uses literal keyword '$name'";
		keywords.add(name);
	    literals.add(name);
		return {};
    }
    
    public Set<String> getKeywords() { return keywords; }  
    public Set<String> getLiterals() { return literals; }  
    public Set<String> getOperations() { return operations; }  
    public Set<String> getAbstractions() { return abstractions; }  
}
