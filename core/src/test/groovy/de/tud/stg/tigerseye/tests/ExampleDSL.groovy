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
package de.tud.stg.tigerseye.tests;

import de.tud.stg.tigerseye.Interpreter;


/**
 * An example language embedding that defines every different keyword type.
 * @author Tom Dinkelaker
 *
 */
public class ExampleDSL extends Interpreter {
 
    public int primitiveLiteral = 1;
    
    private int privateLiteral = 2;
    
    public int getAccessorLiteral() {
    	return privateLiteral;
    }
    
    public void setAccessorLiteral(int value) {
    	privateLiteral = value;
    }
    
    public int operation(int x, int y) {
    	return x+y;
    }
    
    public void nestedAbstraction(Closure cl) {
    	cl.delegate = this.bodyDelegate;
    	cl.call();
    }
	
    /**
     * Expects one name parameter of type int with key "x"
     */
    public void namedParamsAbstraction(HashMap map, Closure cl) {
    	cl.delegate = this.bodyDelegate;
    	cl.call(map.x);
    }

    
    public void variableParamsAbstraction(Integer p1, Integer p2, Integer p3, Closure cl) {
    	cl.delegate = this.bodyDelegate;
    	cl.call(p1,p2,p3);
    }
	
}
