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

import de.tud.stg.popart.pointcuts.Pointcut;

public interface ILogoPointcutLanguage {
	
	Pointcut pmotion();
	
	Pointcut pturning();
	
	Pointcut pturning(int degrees);
	
	Pointcut pturning(int minDegrees, int maxDegrees);
	
    Pointcut pleft();	

    Pointcut pleft(int degrees);	

	Pointcut pleft(int minDegrees, int maxDegrees);

	Pointcut pright();	

    Pointcut pright(int degrees);
    
	Pointcut pright(int minDegrees, int maxDegrees);

	Pointcut pmoving();
	
	Pointcut pmoving(int steps);
	
	Pointcut pmoving(int minSteps, int maxSteps);

	Pointcut pforward();

    Pointcut pforward(int steps);
    
	Pointcut pforward(int minSteps, int maxSteps);

	Pointcut pbackward();

    Pointcut pbackward(int steps);
    
    Pointcut pbackward(int minSteps, int maxSteps);
}
