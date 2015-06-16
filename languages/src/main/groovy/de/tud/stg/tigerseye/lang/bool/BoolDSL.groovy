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
package de.tud.stg.tigerseye.lang.bool;

import de.tud.stg.tigerseye.*;

/**
 * This class defines a DSL environment for working with int .
 */
public class BoolDSL implements DSL {

	def DEBUG = false; 
		
	/* Literals */
	public Bool T = new T();
	
	public Bool F = new F();
	
	/* Operations */
    
	public void puts(String str, Bool bool) {
		println "$str $bool";
	}
	
	Bool not(Bool bool) {
		return bool.not();
	}
	
}

