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
package de.tud.stg.tigerseye.lang.logo.exceptions;

/**
 * This exception is thrown if a turtle paints out side the canvas.
 * @author Tom Dinkelaker
 */
public class OutOfCanvasException extends RuntimeException {

	public OutOfCanvasException() {
		super();
	}

	public OutOfCanvasException(String arg0) {
		super(arg0);
	}

	public OutOfCanvasException(Throwable arg0) {
		super(arg0);
	}

	public OutOfCanvasException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
