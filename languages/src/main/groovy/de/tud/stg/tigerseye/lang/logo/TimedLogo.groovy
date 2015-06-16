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
public class TimedLogo  extends UCBLogo implements IUCBLogo {
	 
	public TimedLogo() {
		super();
	}
	
	/* Literals */

	/* Operations */
	public void forward(int n) { Thread.sleep(2*n); super.forward(n); }
	public void backward(int n) { Thread.sleep(2*n); super.backward(n); }
	public void right(int n) { Thread.sleep(1*n); super.right(n);	}
	public void left(int n) { Thread.sleep(1*n); super.left(n); }
	
	/* Abstraction Operators */
}