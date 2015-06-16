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
package de.tud.stg.tigerseye.lang.logo.analysis

import de.tud.stg.tigerseye.exceptions.AnalysisException;

class PositiveValueAnalysisCompleteLogo extends
		AbstractInterpretationCompleteLogo {

	public PositiveValueAnalysisCompleteLogo() {
		// TODO Auto-generated constructor stub
	}

	public void forward(int n) { if (n < 0) throw new AnalysisException("Invalid argument to forward command n=$n is negative."); }
	public void backward(int n) { if (n < 0) throw new AnalysisException("Invalid argument to backward command n=$n is negative."); }
	public void right(int a) { if (a < 0) throw new AnalysisException("Invalid argument to right command a=$a is negative."); }
	public void left(int a) { if (a < 0) throw new AnalysisException("Invalid argument to left command a=$a is negative."); }
}
