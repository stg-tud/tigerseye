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
package de.tud.stg.tigerseye.lang.logo.dsjpm;

import de.tud.stg.popart.aspect.AspectManager;
import de.tud.stg.popart.aspect.IProceed 
import de.tud.stg.popart.aspect.extensions.instrumentation.JoinPointInstrumentation

public class TurtleLeftJoinPointInstrumentation extends JoinPointInstrumentation {

	public static final boolean DEBUG = false;

	protected void prolog() {
		if (DEBUG) println("INSTRUMENTATION (MOP): \t prolog LeftJP ${instrumentationContext.args[0]}");
		joinPointContext = new HashMap();
		joinPointContext.thisTurtle = instrumentationContext.receiver;
		joinPointContext.degrees = instrumentationContext.args[0];
		joinPoint = new LeftJoinPoint("", joinPointContext);
		joinPoint.args = instrumentationContext.args;
		joinPointContext.thisJoinPoint = joinPoint;
	}
	
}
