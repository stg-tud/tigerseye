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
package de.tud.stg.tigerseye.tests.logo;

import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;
import junit.framework.Assert;
import de.tud.stg.popart.dslsupport.logo.*;

import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestMetaLogo extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
	}

	@Test
	void testMetaLogoRose() {

		def clazz = TurtleDSL.class;
		def reg = InvokerHelper.metaRegistry
		def metaClazz = new ExpandoMetaClass(clazz);
		reg.setMetaClass(clazz,metaClazz);
		metaClazz = reg.getMetaClass(clazz);

		def color = metaClazz.green.&green;
		metaClazz.green = metaClazz.green.red; 
		metaClazz.red = color;

		metaClazz.initialize();

		def dsl = new TurtleDSL();

		dsl.eval(name:"Test2") {

			setpc(green);
		    for (int i in 100..1) {
		    	right i * 2 //right i%20
		    	forward 30 
		    }
		        
		}
	}
}