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
package de.tud.stg.tigerseye.lang.policy;

import de.tud.stg.tigerseye.*;

/**
 * This class defines a DSL environment for defining WS-SecurityPolicy policies.
 */
public class PolicyDSL implements DSL {

	def DEBUG = false; 
	 
	/* Literals */
	
	/**	Implements keyword <code>SAML</code>. */
	public Assertion getSAML() {
    	/*
		if (DEBUG) println "SAML Assertion created";
    	return "<saml><assertion></assertion></saml>";
    	*/
    	return new SAMLAssertion();
    }
	
	/**	Implements keyword <code>Confidentiality</code>. */
	public Assertion getConfidentiality() {
    	return new ConfidentialityAssertion();
    }
	
	/* Operations */
	
	/**	Implements keyword <code>Integrity</code>. */
	public Assertion integrity(SAMLAssertion nestedAssertion) {
    	return new IntegrityAssertion(nestedAssertion);
    }
	
    /** Implements keyword <code>wrapExactlyOnce</code>. */
    public String wrapExactlyOnce(assertion) {
        return "<ExactlyOnce>$assertion</ExactlyOnce>";
    }
    
    /** Implements keyword <code>convertToPolicy</code>. */
    public String convertToPolicy(assertion) {
        return "<Policy>$assertion</Policy>";
    }
    
}
