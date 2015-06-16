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
package de.tud.stg.tigerseye.lang.logo.dspcl;

import de.tud.stg.tigerseye.lang.logo.dsjpm.BackwardJoinPoint;
import de.tud.stg.tigerseye.lang.logo.dsjpm.ForwardJoinPoint;
import de.tud.stg.tigerseye.lang.logo.dsjpm.TurtleMoveJoinPoint;
import de.tud.stg.popart.joinpoints.JoinPoint;
import de.tud.stg.popart.pointcuts.Pointcut;

public class TurtleMovingStepsPCD extends Pointcut {

	public TurtleMovingStepsPCD(int steps) {
		super("pmoving(steps)");
		this.steps = steps;
	}
	private int steps;

	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}
	@Override
	public boolean match(JoinPoint jp) {
		return 
		  ((jp instanceof BackwardJoinPoint) ||
		  (jp instanceof ForwardJoinPoint)) &&
		  (steps == ((TurtleMoveJoinPoint)jp).getSteps());
	}
	
}
