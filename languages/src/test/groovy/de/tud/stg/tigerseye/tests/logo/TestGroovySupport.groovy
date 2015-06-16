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
import static junit.framework.Assert.*;

import de.tud.stg.tigerseye.lang.logo.*;

import org.codehaus.groovy.runtime.InvokerHelper;

/**
 * @author Tom Dinkelaker
 *
 */
public class TestGroovySupport extends TestCase {
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	void setUp() throws Exception{
	}

	/**
	 * Tests whether the delegate of cloned closures is independent from the delegate of original closure.
	 */
	@Test
	void testSingleDelegate() {
        Closure cl1 = {
        	return testit();
        }

        cl1.delegate = new T1();
        cl1.resolveStrategy = Closure.DELEGATE_FIRST;
        def result = cl1.call();
        assertEquals(result,1);
        
        def cl2 = cl1.clone();
        cl2.delegate = new T2(); //setting the cloned closure's delegate should change the delegate of cl1???
        cl2.resolveStrategy = Closure.OWNER_FIRST;
        def result2 = cl2.call();
        assertEquals(result2,2);

        //check if the delegate of cl1 was not changed
        def result3 = cl1.call();
        assertEquals(1,result3); //should not have changed the delegate
        assertEquals(Closure.DELEGATE_FIRST,cl1.getResolveStrategy()); //should not have chnaged the resolveStrategy        
	}
	
	/**
	 * Tests that cloning works correctly.
	 */
	@Test
	void testCloningClosures() {
		Closure cl1 = {
			def cl2 = { return 2; }
			return cl2;
		}
		
		println("cl1.owner="+cl1.getOwner());
		println("cl1.delegate="+cl1.getDelegate());
		println("cl1.resolveStrategy="+cl1.getResolveStrategy());
		
		def cl2 = cl1.call();
		println("cl2.owner="+cl2.getOwner());
		println("cl2.delegate="+cl2.getDelegate());
		println("cl2.resolveStrategy="+cl2.getResolveStrategy());
		
		def cl3 = cl2.clone();
		cl3.delegate = new Object();
		cl3.resolveStrategy = Closure.DELEGATE_FIRST;
		println("cl3.owner="+cl3.getOwner());
		println("cl3.delegate="+cl3.getDelegate());
		println("cl3.resolveStrategy="+cl3.getResolveStrategy());
		
		println("cl2.owner="+cl2.getOwner());
		println("cl2.delegate="+cl2.getDelegate());
		println("cl2.resolveStrategy="+cl2.getResolveStrategy());
	}
}

public class T1 {
	public int testit() {
		return 1;
	}
}

public class T2 {
	public int testit() {
		return 2;
	}
}