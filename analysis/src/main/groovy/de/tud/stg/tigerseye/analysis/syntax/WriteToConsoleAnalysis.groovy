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

import de.tud.stg.tigerseye.Interpreter;

/**
 * This is an abstract interpreter that can be used to determine all keywords a DSL program uses.
 * @author Tom Dinkelaker
 */
public class WriteToConsoleAnalysis extends Interpreter {

	private String prefix = "DSL program: ";
	
	public WriteToConsoleAnalysis() {
	    super();
	    this.out = System.out;
	}
	
	public WriteToConsoleAnalysis(OutputStream out, String prefix) {
	    super();
		this.prefix = prefix;	
		this.out = out;
	}
	
	public Object methodMissing(String name, Object args) {
    	println("$prefix calls $name($args)");
    }
    
    public void propertyMissing(String name, Object value) {  
    	println("$prefix reads $name sets $value");
    }
    
    public Object propertyMissing(String name) { 
    	println("$prefix writes $name gets");
    }
    
}
    