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
package de.tud.stg.tigerseye.analysis.syntax

import de.tud.stg.tigerseye.Interpreter 
import de.tud.stg.tigerseye.InterpreterCombiner 
import java.lang.reflect.Field 
import java.lang.reflect.Method 
import java.util.Set;

public class ProvidedKeywordsAnalysis {
	
	final boolean DEBUG = true;

	/**
	* When accessing a literal using its accessors, the analysis does not treat this as an operation (default true).
	*/
    public final boolean OPTION_IGNORE_ACCESSOR_KEYWORDS = true;

	Set<String> keywords = new HashSet<String>();

	Set<String> literals = new HashSet<String>();

	Set<String> operations = new HashSet<String>();

	Set<String> abstractions = new HashSet<String>();
	
	public ProvidedKeywordsAnalysis(Class clz) {
		calculateKeywords(clz);
	}
	
	public ProvidedKeywordsAnalysis(Interpreter facade) {
		this.clz = facade.getClass();
		calculateKeywords(clz);
	}

	public ProvidedKeywordsAnalysis(InterpreterCombiner combiner) {
		for (Interpreter i : combiner.dslDefinitions) {
		    this.clz = i.getClass();
		    calculateKeywords(clz);
		}
	}

	private void calculateKeywords(Class clz) {
		calculateKeywordMethods(clz);
		calculateKeywordFields(clz);
	}
	
	private boolean isBlackListName(String name) {
		if (name.equals("bodyDelegate")) return true;
		if (name.equals("DEBUG")) return true;
		if (name.equals("hashCode")) return true;
		if (name.equals("invokeMethod")) return true;
		if (name.equals("notify")) return true;
		if (name.equals("notifyAll")) return true;
		if (name.equals("metaClass")) return true;
		if (name.equals("missingMethod")) return true;
		if (name.equals("toString")) return true;
		if (name.equals("wait")) return true;
		return false;
	}

	private void calculateKeywordMethods(Class clz) {
		Method[] definedMethods = clz.getMethods();
		Method[] ignoredMethods = Object.class.getDeclaredMethods();
		Method[] methods = definedMethods - ignoredMethods
		
	    if (DEBUG) println this.getClass().getName()+":: DSL analyses defined keywords in '$clz' with methods \n scanned: $methods \n ignored: $ignoredMethods";
		for (int i=0; i < methods.length; i++) {
   		    Method method =  methods[i];
	    	String name = method.getName();
			if (DEBUG) println this.getClass().getName()+":: DSL program uses keyword of yet unknown type '$name' public:${!(method.getModifiers() && Method.PUBLIC)}";
			
			if (name.contains("\$")) continue;
			
			if (isBlackListName(name)) continue;
			
	    	if (name.startsWith("get") && name.size() > 3) {
	    		//Special case: a method call starting with "get" may also be mapped to the getter method
	            def firstLetter = name[3]
	        	def remainderLetters = name[4..name.size()-1];
	    	    def keywordName = firstLetter.toLowerCase()+remainderLetters; 
	    	    if (DEBUG) println this.getClass().getName()+":: DSL program uses getter literal keyword '$keywordName'";
	    	    if (DEBUG) println this.getClass().getName()+":: \\-- adapted keyword name firstLetter='$firstLetter' remainderLetters='$remainderLetters' -> '$keywordName'";
	    	    keywords.add(keywordName);
	    	    literals.add(keywordName);
	    	    if (OPTION_IGNORE_ACCESSOR_KEYWORDS) continue;
	    	} else if (name.startsWith("set") && name.size() > 3) {
	    		//Special case: a method call starting with "get" may also be mapped to the getter method
	            def firstLetter = name[3]
	        	def remainderLetters = name[4..name.size()-1];
	    	    def keywordName = firstLetter.toLowerCase()+remainderLetters; 
	    	    if (DEBUG) println this.getClass().getName()+":: DSL program uses setter literal keyword '$keywordName'";
	    	    if (DEBUG) println this.getClass().getName()+":: \\-- adapted keyword name firstLetter='$firstLetter' remainderLetters='$remainderLetters' -> '$keywordName'";
	    	    keywords.add(keywordName);
	    	    literals.add(keywordName);
	    	    if (OPTION_IGNORE_ACCESSOR_KEYWORDS) continue;
	    	} 
			
			if (DEBUG) println this.getClass().getName()+":: DSL program uses keyword '$name' with args=${method.getParameterTypes()} args=${ method.getParameterTypes().length}";
			keywords.add(name);
			  
			Class lastParamClass = null;
			if (method.getParameterTypes().length > 0) {
				lastParamClass = method.getParameterTypes()[method.getParameterTypes().length - 1];
			}
	
			if (DEBUG) println this.getClass().getName()+":: \\-- args[last]="+lastParamClass;
			if (lastParamClass.equals(Closure)) {
				//Special case: nested abstraction
				if (DEBUG) println this.getClass().getName()+":: DSL program uses nested abstraction keyword '$name'";
				abstractions.add(name);
			} else {
				//Default case: operation
				if (DEBUG) println this.getClass().getName()+":: DSL program uses operation keyword '$name'";
				operations.add(name);
			}
			
    	}

	}
	
	private void calculateKeywordFields(Class clz) {
		Field[] definedFields = clz.getFields();
		Field[] ignoredFields = Object.class.getFields();
		Field[] fields = definedFields - ignoredFields
		
		if (DEBUG) println this.getClass().getName()+":: DSL analyses defined keywords in '$clz' with methods \n scanned: $fields \n ignored: $ignoredFields";
		for (int i=0; i < fields.length; i++) {
			Field field =  fields[i];
			String name = field.getName();
			if (DEBUG) println this.getClass().getName()+":: DSL program uses keyword of yet unknown type '$name' public:${!(field.getModifiers() && Field.PUBLIC)}";
			
			if (name.contains("\$") || name.contains("__")) continue; //TODO: Problem, this will not work when we encode concrete syntax, since not only Groovy uses this prefix for synthesized fields, but also our convention uses "__" for arbitrary many whiteSpaces  
				
			if (isBlackListName(name)) continue;
			
			if (DEBUG) println this.getClass().getName()+":: DSL program uses literal field keyword '$name'";
			keywords.add(name);
			literals.add(name);
		}
	}
	
	
    public Set<String> getKeywords() { 
    	return keywords; 
    }  
    
    public Set<String> getLiterals() { 
    	return literals; 
    }  
    
    public Set<String> getOperations() { 
    	return operations; 
    }
    
    public Set<String> getAbstractions() { 
    	return abstractions; 
    }  
	
	public Set<String> getUnknownKeywords(Set<String> usedKeywords, Set<String> providedKeywords) {
		if (DEBUG) println "used keywords: "+usedKeywords;
		if (DEBUG) println "provided keywords: "+providedKeywords;
		Set<String> unknownKeywords = new HashSet<String>();
		for (String usedKeyword : usedKeywords) {
			if (!providedKeywords.contains(usedKeyword)) {
				if (DEBUG) println "\\-- unknown keyword: "+usedKeyword;
				unknownKeywords.add(usedKeyword);
			}
		}
		if (DEBUG) println "unknown keywords: "+unknownKeywords;
		return unknownKeywords;
	}
	
	public boolean isSyntaxOfProgramCorrect(UsedKeywordsAnalysis syntaxProgramAnalyzer) {
//		if (DEBUG) println "used    literals: "+syntaxProgramAnalyzer.getLiterals();
//		if (DEBUG) println "defined literals: "+this.getLiterals();
//		if (DEBUG) println "unknown literals: "+syntaxProgramAnalyzer.getLiterals()-this.getLiterals();
//		if (DEBUG) println "used    operations: "+syntaxProgramAnalyzer.getOperations();
//		if (DEBUG) println "defined operations: "+this.getOperations();
//		if (DEBUG) println "unknown operations: "+syntaxProgramAnalyzer.getOperations()-this.getOperations();
//		if (DEBUG) println "used    abstractions: "+syntaxProgramAnalyzer.getAbstractions();
//		if (DEBUG) println "defined abstractions: "+this.getAbstractions();
//		if (DEBUG) println "unknown abstractions: "+syntaxProgramAnalyzer.getAbstractions()-this.getAbstractions();
		if (getUnknownKeywords(syntaxProgramAnalyzer.getKeywords(),this.getKeywords()).isEmpty()) return true;
		return false;
	}


}
