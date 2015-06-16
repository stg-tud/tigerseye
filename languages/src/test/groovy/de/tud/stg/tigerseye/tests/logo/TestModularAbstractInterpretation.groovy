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

import de.tud.stg.popart.aspect.Aspect;
import de.tud.stg.popart.aspect.AspectManager;
import de.tud.stg.popart.aspect.extensions.Booter;
import de.tud.stg.popart.aspect.extensions.itd.ITDCCCombiner;

import de.tud.stg.tigerseye.lang.logo.*;
import de.tud.stg.tigerseye.lang.logo.analysis.AbstractInterpretationFunctionalLogo;
import de.tud.stg.tigerseye.lang.logo.analysis.TotalAbstractInterpretation;
import de.tud.stg.tigerseye.lang.logo.dspcl.LogoPointcutInterpreter;
import de.tud.stg.tigerseye.BlackBoxCombiner;


/**
 * @author Tom Dinkelaker
 *
 */
public class TestModularAbstractInterpretation extends TestCase {
	
	def ccc;
	def dsl;
	def concreteInterpreter;
	def abstractInterpreter;
	
	Closure aspect;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		if (!Booter.aspectSystemInitialized) Booter.initialize();
	    
		concreteInterpreter = new FunctionalLogo();
		abstractInterpreter = new AbstractInterpretationFunctionalLogo();
		
		dsl = abstractInterpreter;
	    def pcl = LogoPointcutInterpreter.getInstance();
	    def advl = abstractInterpreter;

	    ccc = new ITDCCCombiner(new LinkedHashSet([advl,pcl]));
	    
	    AspectManager.instance.unregisterAllAspects();
	    
	    aspect = { HashMap params, Closure body ->
	        Aspect result = ccc.eval(params,body);
	        AspectManager.getInstance().register(result);
	        return result;
	    }
	    

	}

	@Test
	void testAddKeywordToDSLInterpreterWithITD() {
		
		int i = 0;

        Closure program = {
			
			aspect(name:"AddKeywordToDSLInterpreterWithITD") {
						    	
				//This ITD adds the keyword method "vorwaerts150" to the DSL interpreter class "TurtleDSL"
				//effectively this chnages the syntax of a language
//				introduce_method(is_type(TestModularAbstractInterpretation), "vorwaerts150") { 
//					delegate.forward(150);
//				}
				
				introduce_method(is_type(AbstractInterpretationFunctionalLogo), "vorwaerts150") { 
					delegate.forward(150);
				}
				
				before(pmotion()) {
					println "motion inc i";
					i++;
				}
			}
		
			turtle(name:"Square") {
			    vorwaerts150()
			    forward 50
			    right 90
			    forward 50
			    right 90
			    vorwaerts150()
			    forward 50
			    right 90
			    forward 50
			    right 90
			}
		}
        program.delegate = ccc;
        program.resolveStrategy = Closure.DELEGATE_FIRST;
        program.call();
		
		assertEquals(10,i);
	}
	
	@Test
	void testConcreteInterpreter() {
		Closure choreography = {
			turtle (name:"Fun&App",color:green) {
				fun("square") {
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
				}
				app("square")();				
			}
		}
		choreography.delegate = ccc;
		choreography.call();
	}
	
	@Test
	void testAbstractInterpreter() {
		Closure choreography = {
			turtle (name:"Fun&App",color:green) {
				fun("square") {
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
				}
				app("square")();				
			}
		}
		choreography.delegate = abstractInterpreter;
		choreography.call();
	}
	
	@Test
	void testAbstractInterpreterWithIntroducedKeyword() {
		Closure choreography = {
			turtle (name:"Fun&App",color:green) {
				fun("square") {
				    vorwaerts150()
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
			        forward 50
			        right 90
				}
				app("square")();				
			}
		}
		choreography.delegate = abstractInterpreter;
		choreography.call();
	}
	
	@Test
	void testKeywordCounterAsAspect() {
		
		int i = 0;

        Closure program = {
			
			aspect(name:"AddKeywordToDSLInterpreterWithITD") {
						    	
				//This ITD adds the keyword method "vorwaerts150" to the DSL interpreter class "TurtleDSL"
				//effectively this chnages the syntax of a language
				introduce_method(is_type(AbstractInterpretationFunctionalLogo), "vorwaerts150") { 
					delegate.forward(150);
				}
				
				before(pmotion()) {
					println "motion inc i";
					i++;
				}
			}
		
			turtle(name:"Square") {
			    vorwaerts150()
			    forward 50
			    right 90
			    forward 50
			    right 90
			    vorwaerts150()
			    forward 50
			    right 90
			    forward 50
			    right 90
			}
		}
        program.delegate = ccc;
        program.call();
		
		assertEquals(10,i);
	}
	
	
	@Test
	void testTotalAnalysis() {
		
		int i = 0;

		Closure aspectProgram = {
				aspect(name:"AddTwoExpressionToTotalAnalysis") {
			    	
					introduce_method(is_type(TotalAbstractInterpretation), "forward") { int n ->
						i++
					}
					
					introduce_method(is_type(TotalAbstractInterpretation), "right") { int n ->
					    i++
				    }
				
				}
		}
		aspectProgram.delegate = ccc;
		aspectProgram.call();		
		
        Closure program = {
			
			turtle(name:"Square") {
			    vorwaerts150()
			    forward 50
			    right 90
			    forward 50
			    right 90
			    vorwaerts150()
			    forward 50
			    right 90
			    forward 50
			    right 90
			}
		}
        
        
        program.delegate = new TotalAbstractInterpretation();
        program.resolveStrategy = Closure.DELEGATE_FIRST;
        program.call();
		
		assertEquals(8,i);
	}	
	
	@Test
	void testModularTotalAnalysis() {
		
		int i = 0;

		Closure aspectProgram = {
				aspect(name:"AnalysisAspect1") {
			    	
					introduce_method(is_type(TotalAbstractInterpretation), "forward") { int n ->
						i++
					}
					
				}
				
				aspect(name:"AnalysisAspect2") {
			    						
					introduce_method(is_type(TotalAbstractInterpretation), "vorwaerts150") { 
					    i++
				    }
				
				}
		}
		aspectProgram.delegate = ccc;
		aspectProgram.call();		
		
        Closure program = {
			
			turtle(name:"Square") {
			    vorwaerts150()
			    forward 50
			    right 90
			    forward 50
			    right 90
			    vorwaerts150()
			    forward 50
			    right 90
			    forward 50
			    right 90
			}
		}
        
        
        program.delegate = new TotalAbstractInterpretation();
        program.resolveStrategy = Closure.DELEGATE_FIRST;
        program.call();
		
		assertEquals(6,i);
	}	
	
	@Test
	void testExclusiveTotalAnalysis() {
		
		int i = 0;

		Closure aspectProgram = {
				aspect(name:"AnalysisAspect1") {
			    	
					introduce_method(is_type(TotalAbstractInterpretation), "forward") { int n ->
						i++
					}
					
				}
				
				aspect(name:"AnalysisAspect2") {
					
					declare_mutex "AnalysisAspect2", "AnalysisAspect1"
			    						
					introduce_method(is_type(TotalAbstractInterpretation), "forward") { int n ->
					    i++
				    }
				
				}
		}
		aspectProgram.delegate = ccc;
		aspectProgram.call();		
		
        Closure program = {
			
			turtle(name:"Square") {
			    vorwaerts150()
			    forward 50
			    right 90
			    forward 50
			    right 90
			    vorwaerts150()
			    forward 50
			    right 90
			    forward 50
			    right 90
			}
		}
        
        
        program.delegate = new TotalAbstractInterpretation();
        program.resolveStrategy = Closure.DELEGATE_FIRST;
        try {
             program.call();
        } catch (Exception ex) {
        	 //ignore since conflict in analysis has been detected
        }
		
		assertEquals(4,i);
	}		
	
//	@Test
//	void testSchedulingTotalAnalysis() {
//		
//		int i = 0;
//
//		Closure aspectProgram = {
//				aspect(name:"AnalysisAspect1") {
//			    	
//					//declare_precedence "AnalysisAspect2", "AnalysisAspect1"
//					
//					introduce_method(is_type(TotalAbstractInterpretation), "forward") { int n ->
//						i++
//					}
//					
//				}
//				
//				aspect(name:"AnalysisAspect2") {
//						    						
//					//declare_mutex "AnalysisAspect2", "AnalysisAspect1"
//
//					introduce_method(is_type(TotalAbstractInterpretation), "forward") { int n ->
//					    i*2
//				    }
//				
//				}
//		}
//		aspectProgram.delegate = ccc;
//		aspectProgram.call();		
//		
//        Closure program = {
//			
//			turtle(name:"Square") {
//			    vorwaerts150()
//			    forward 50
//			    right 90
//			    forward 50
//			    right 90
//			    vorwaerts150()
//			    forward 50
//			    right 90
//			    forward 50
//			    right 90
//			}
//		}
//        
//        
//        program.delegate = new TotalAbstractInterpretation();
//        program.resolveStrategy = Closure.DELEGATE_FIRST;
//        program.call();
//		
//		assertEquals(4,i);
//	}	
	
	@Test
	void testContextSensitiveTotalAnalysis() {
		
		boolean myVar = false;
		int i = 0;

		Closure aspectProgram = {
				aspect(name:"AnalysisAspect1") {
			    	
					//declare_precedence "AnalysisAspect2", "AnalysisAspect1"
					
					introduce_method(is_type(TotalAbstractInterpretation), "forward") { int n ->
					    println ":forward:"
					}
					
					before(method_execution("forward")  &
						   if_pcd { myVar } ) {
						//new Exception().printStackTrace();
						println ":fd.advice:"
						i++
					}
					
				}
				
		}
		aspectProgram.delegate = ccc;
		aspectProgram.call();		
		
        Closure program = {
			
        		turtle(name:"Square") {
                  
                  forward 10 
                  Square 20
                  
                  right 90 
                  forward 25
                  Square 15
                  
                  right 180
                  forward 100                
                 
                  
                  forward 10 
                  Square 20
                  
                  right 90 
                  forward 25
                  Square 15
                  
                  right 180
                  forward 100                       
                  forward 10 
                  Square 20
                  
                  right 90 
                  forward 25
                  Square 15
                  
                  right 180
                  forward 100     
  			
  			}
		}
        
        
        program.delegate = new TotalAbstractInterpretation();
        program.resolveStrategy = Closure.DELEGATE_FIRST;
        program.call();
        
        assert i == 0

        myVar = true;
        
        program.delegate = new TotalAbstractInterpretation();
        program.resolveStrategy = Closure.DELEGATE_FIRST;
        program.call();

        assert i == 9
	}	
	
//	@Test
//	void testSchedulingOptimizationTotalAnalysis() {
//		
//		int i = 0;
//
//		Closure aspectProgram = {
//				aspect(name:"AnalysisAspect1") {
//			    	
//					//declare_precedence "AnalysisAspect2", "AnalysisAspect1"
//					
//					introduce_method(is_type(TotalAbstractInterpretation), "forward") { int n ->
//						i++
//					}
//					
//				}
//				
//				aspect(name:"AnalysisAspect2") {
//						    						
//					//declare_mutex "AnalysisAspect2", "AnalysisAspect1"
//
//					introduce_method(is_type(TotalAbstractInterpretation), "forward") { int n ->
//					    i*2
//				    }
//				
//				}
//		}
//		aspectProgram.delegate = ccc;
//		aspectProgram.call();		
//		
//        Closure program = {
//			
//			turtle(name:"Square") {
//                fun("Square") { n ->
//				  forward n
//			      right 90
//			      forward n
//			      right 90
//			      forward n
//			      right 90
//			      forward n
//			      right 90
//                }
//                
//                forward 10 
//                Square 20
//                
//                right 90 
//                forward 25
//                Square 15
//                
//                right 180
//                forward 100                
//               
//                
//			
//			}
//		}
//        
//        
//        program.delegate = new TotalAbstractInterpretation();
//        program.resolveStrategy = Closure.DELEGATE_FIRST;
//        program.call();
//		
//		assertEquals(4,i);
//	}	
}