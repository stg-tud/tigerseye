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
package de.tud.stg.tigerseye.lang.logo;

import de.tud.stg.tigerseye.*;
import org.javalogo.*;
import java.awt.Color;

/**
 * This class implements the logo toy language.
 */
public class TurtleDSL extends ConciseLogo {
	/*
	def DEBUG = true; 
	
	private TurtleGraphicsWindow myTurtleGraphicsWindow;
	private Turtle turtle;
	
	public TurtleDSL() {
		myTurtleGraphicsWindow = new TurtleGraphicsWindow();
        myTurtleGraphicsWindow.setTitle("TurtleDSL (based on JavaLogo) "); //Set the windows title
        myTurtleGraphicsWindow.show(); //Display the window
        turtle = new Turtle("Noname",java.awt.Color.BLACK); //Create a turtle
        myTurtleGraphicsWindow.add(turtle); //Put bob in our window so bob has a place to draw
	}
	 
	public Object eval(HashMap map, Closure cl) {
		turtle.setName(map.name);
		cl.delegate = this;
		cl.resolveStrategy = Closure.DELEGATE_FIRST;
		cl.call();
	}
	
	/-* Literals *-/
	public int getBlack() { return Color.BLACK.value; }
	public int getBlue() { return Color.BLUE.value; }
	public int getRed() { return Color.RED.value; }
	public int getGreen() { return Color.GREEN.value; }
	public int getYellow() { return Color.YELLOW.value; }
	public int getWhite() { return Color.WHITE.value; }

	/-* Operations *-/
	public void textscreen()   { throw new IllegalStateException("DSL Operation has not been implemented.") }
	public void ts() { textscreen(); }	
	public void fullscreen() { throw new IllegalStateException("DSL Operation has not been implemented.") }
	public void fs() { fullscreen(); }
	public void home() { turtle.home();	}
	public void clean() { myTurtleGraphicsWindow.clear(); }
	public void cleanscreen() {	clean(); home(); }
	public void cs() { cleanscreen(); }
	
	public void hideturtle() { turtle.hide(); }
	public void ht() { hideturtle(); }
	public void showturtle() { turtle.show(); }
	public void st() { showturtle(); }
	
	public void setpencolor(int n) { turtle.setPenColor(new java.awt.Color(n)); }
	public void setpc(int n) { setpencolor(n); }
	public void penup() { turtle.penUp(); }
	public void pu() { penup(); }
	public void pendown() { turtle.penDown(); } 
	public void pd() { pendown(); }
	
	public void forward(int n) { turtle.forward(n);	}
	public void fd(int n) { forward(n); }
	public void backward(int n) { turtle.backward(n); }
	public void bd(int n) { backward(n); }
	public void right(int n) { turtle.right(n);	}
	public void rt(int n) { right(n);	}
	public void left(int n) { turtle.left(n); }
	public void lt(int n) { left(n); }
	*/
}