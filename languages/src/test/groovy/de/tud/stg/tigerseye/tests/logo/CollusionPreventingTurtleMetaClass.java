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

import groovy.lang.ExpandoMetaClass;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import org.javalogo.Turtle;
 
import de.tud.stg.tigerseye.lang.logo.ConcurrentLogo;

public class CollusionPreventingTurtleMetaClass extends TurtleMetaClass {

	protected ConcurrentLogo interpreter;
	
	public CollusionPreventingTurtleMetaClass(Class theClass, ConcurrentLogo interpreter) {
		super(theClass,interpreter);
		this.interpreter= interpreter;
	}
	
    public Object invokeMethod(Class sender, Object object, String methodName, Object[] originalArguments, boolean isCallToSuper, boolean fromInsideClass) {
    	Turtle turtle = (Turtle)object;
    	if ((methodName.equals("forward") || (methodName.equals("backward")))    
    			&& !turtle.getName().equals("collusion")) {
        	System.out.println("CPMC.prevent("+methodName+")=");
        	preventTurtleFacingACollusion(turtle);
    	}
		Object result = super.invokeMethod(sender, object, methodName, originalArguments, isCallToSuper, fromInsideClass);
		return result;
    }
    
    public void preventTurtleFacingACollusion(Turtle turtle) {
        if ((turtle.getPosition().getX() == 0) && (turtle.getPosition().getX() == 0)) return; //do no prevent collusion at home position
        
        if (turtle.getName().equals("collusion")) return;

        List<Turtle> turtles = interpreter.getTurtles();
        synchronized (this) {
	        Iterator<Turtle> it = turtles.iterator();
		    System.out.println("PREVENTING: sense all other turtles before moving");
	        while (it.hasNext()) {
	        	Turtle other = it.next();
	        	        	
	        	if (turtle == other) continue; //a turtle has no collusion with itself
			    System.out.println("PREVENTING: 1");
	        	if (other.getName().equals("collusion")) continue; //no collusion with the turtles that paint the cross at a collusion point
			    System.out.println("PREVENTING: 2");
	        	if (!turtle.isPenDown() || !other.isPenDown()) continue; //if one of the turtles has the pen up, it is no collusion
			    System.out.println("PREVENTING: 3");
	        	
	        	double distanceX = turtle.getPosition().getX() - other.getPosition().getX();
	        	double distanceY = turtle.getPosition().getY() - other.getPosition().getY();
	        	
	        	double distanceXY = Math.sqrt( distanceX * distanceX + distanceY * distanceY);
	        	
			    System.out.println("PREVENTING: 4");
	        	if (distanceXY < 10) {
	        		try {
	        		    System.out.println("PREVENTING: possible collusion between turtle="+turtle.getName()+
	        		    		" other="+other.getName()+
	        		    		" distance="+distanceXY+
	        		    		" thread="+interpreter.getThread(other).getId());
	        		    if (turtle.isPenDown()) { 
	        		        turtle.penUp();
	        		    	Thread.currentThread().sleep(100);
	        		    	turtle.penDown();
	        		    }
	        		} catch (Exception e) {
	        			//ignore
	        			 System.out.println("PREVENTING exception occurs");
	        		}
	        	} else {
        		    /*System.out.println("PREVENTING: no collusion because of save distance turtle="+turtle.getName()+
        		    		" other="+other.getName()+
        		    		" distance="+distanceXY+
        		    		" thread="+interpreter.getThread(other).getId());*/
	        	}
	        }
        }
    }

}
