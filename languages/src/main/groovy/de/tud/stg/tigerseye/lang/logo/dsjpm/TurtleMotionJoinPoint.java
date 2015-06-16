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

import java.util.HashMap;

import org.javalogo.Turtle;

import de.tud.stg.popart.joinpoints.JoinPoint;

public abstract class TurtleMotionJoinPoint extends JoinPoint {

	@SuppressWarnings("unchecked")
	public TurtleMotionJoinPoint(String location, HashMap context) {
		super(location, context);
		if (context == null) throw new RuntimeException("context null");
		this.thisTurtle = (Turtle)context.get("thisTurtle");
		this.positionX = Math.round((float)thisTurtle.getPosition().x); 
		this.positionY = Math.round((float)thisTurtle.getPosition().y);
		this.argular = Math.round((float)thisTurtle.getHeading());
		this.color = thisTurtle.getPenColor().getRGB();
	}
	
	protected int color;

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	protected Turtle thisTurtle;

	public Turtle getTurtle() {
		return thisTurtle;
	}

	public void setTurtle(Turtle thisTurtle) {
		this.thisTurtle = thisTurtle;
	}
	
	protected int positionX;

	public int getPositionX() {
		return positionX;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	
	protected int positionY;

	public int getPositionY() {
		return positionY;
	}

	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	
	protected int argular;

	public int getArgular() {
		return argular;
	}

	public void setArgular(int argular) {
		this.argular = argular;
	}
	
	protected String command;
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
	    this.command = command;	
	}
	
	protected Object[] args;
	
	public Object[] getArgs() {
		return args;
	}
	
	public void setArgs(Object[] args) {
		this.args = args;
	}
	
}
