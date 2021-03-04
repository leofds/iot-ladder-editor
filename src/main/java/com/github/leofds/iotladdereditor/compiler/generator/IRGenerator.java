/*******************************************************************************
 * Copyright (C) 2021 Leonardo Fernandes
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.leofds.iotladdereditor.compiler.generator;

import java.util.ArrayList;
import java.util.List;

import com.github.leofds.iotladdereditor.compiler.domain.GenContext;
import com.github.leofds.iotladdereditor.compiler.domain.IR;
import com.github.leofds.iotladdereditor.compiler.domain.ProgramFunc;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.domain.Symbol;
import com.github.leofds.iotladdereditor.compiler.domain.SymbolTable;
import com.github.leofds.iotladdereditor.compiler.generator.factory.QuadrupleFactory;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;
import com.github.leofds.iotladdereditor.ladder.rung.Rung;
import com.github.leofds.iotladdereditor.ladder.rung.Rungs;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;

public class IRGenerator {

	private static LadderProgram program;
	private static GenContext genContext;
	private static List<Quadruple> quadruples;
	private static SymbolTable symbolTable;
	
	private static void genStatusCheck(Symbol currentStatus){
		Symbol comment = symbolTable.addLiteral("Parallel status check");
		Symbol endLabel = symbolTable.addLabel(genContext.genLabel(), genContext.getScope());
		Symbol status = symbolTable.addBoolVar(genContext.getCurrentStatus(), genContext.getScope());
		Symbol one = symbolTable.addIntConst("1");
		
		quadruples.add(QuadrupleFactory.createComment( comment ));
		while(!(genContext.decStatus().equals(currentStatus.getName()))){
			Symbol nextLabel = symbolTable.addLabel(genContext.genLabel(), genContext.getScope());
			quadruples.add(QuadrupleFactory.createIfFalse( status, nextLabel ));
			quadruples.add(QuadrupleFactory.createAssignment( one , currentStatus ));
			quadruples.add(QuadrupleFactory.createGoto( endLabel ));
			quadruples.add(QuadrupleFactory.createLabel( nextLabel ));
			status = symbolTable.addIntVar(genContext.getCurrentStatus(), genContext.getScope());
		}
		quadruples.add(QuadrupleFactory.createIfFalse( status, endLabel ));
		quadruples.add(QuadrupleFactory.createAssignment( one, currentStatus ));
		quadruples.add(QuadrupleFactory.createLabel( endLabel ));
	}

	private static void genInstruction(Rung rung,List<LadderInstruction> parallel,LadderInstruction instruction){
		if(instruction != null){
			Symbol currentStatus = symbolTable.addBoolVar(genContext.getCurrentStatus(), genContext.getScope());

			// check for instructions below to save the current status
			if(instruction.getDown() != null){
				parallel.add(instruction.getDown());
				Symbol downStatus = symbolTable.addIntVar( genContext.incStatus() , genContext.getScope() );
				quadruples.add(QuadrupleFactory.createAssignment( currentStatus , downStatus ));
				genContext.decStatus();
			}

			// generate the instruction code
			List<Quadruple> code = instruction.generateIR(genContext);
			if(code != null){
				quadruples.addAll( code );

				// checks how many instructions refer to it as UP
				int ups = rung.getCountReferenceUp(rung.getFirst(), instruction.getNext()); 
				if(ups > 0){
					currentStatus = symbolTable.addIntVar(genContext.getCurrentStatus(), genContext.getScope());//FIXME verificar se necess�rio

					// generates instructions in parallel
					genContext.incStatus();
					genInstruction(rung,parallel,parallel.remove(0));

					// generates the code to check the status after parallel
					genStatusCheck(currentStatus);
					
				}else if(parallel.size() > 0){
					
					// next parallel instruction
					genContext.incStatus();
					genInstruction(rung,parallel,parallel.remove(0));
				}
			}
			// next serial instruction
			genInstruction(rung,parallel,instruction.getNext());					
		}
	}

	public static synchronized IR generate(LadderProgram ladderProgram){
		if(ladderProgram == null){
			throw new IllegalArgumentException("ladderProgram can not be null");
		}
		program = ladderProgram;
		symbolTable = new SymbolTable();
		quadruples = new ArrayList<Quadruple>();
		genContext = new GenContext(symbolTable);
		
		Rungs rungs = program.getRungs();
		for (Rung rung : rungs) {
			genContext.setScope(String.format("%s%03d", ProgramFunc.RUNG.value, rung.getNumber()));
			genContext.clearStatus();
			genInstruction(rung,new ArrayList<LadderInstruction>(),rung.getFirst());
		}
		genInit(rungs);
		genMain();
		genBegin();
		for (Rung rung : rungs) {
			Symbol rungLabel 	= symbolTable.addFunc(String.format("%s%03d", ProgramFunc.RUNG.value, rung.getNumber()),Void.class);
			Symbol zero 		= symbolTable.addIntConst("0");
			quadruples.add( QuadrupleFactory.createCall( rungLabel , zero ) );
		}
		genEnd();
		return new IR(quadruples, symbolTable);
	}
	
	private static void genInit(Rungs rungs){
		Symbol initContext = symbolTable.addFunc( ProgramFunc.INIT_CONTEXT.value ,Void.class);
		quadruples.add(QuadrupleFactory.createLabel( initContext ));
		
		for(Rung rung:rungs){
			for(LadderInstruction instruction: rung){
				List<Quadruple> code = instruction.generateIRInit(genContext);
				if(code != null){
					quadruples.addAll( code );
				}
			}
		}
		quadruples.add(QuadrupleFactory.createReturn());
	}

	private static void genMain(){
		Symbol main = symbolTable.addFunc("main",Void.class);
		Symbol zero = symbolTable.addIntConst("0");
		//		Symbol __init = symbolTable.addFunc("__init",Integer.class);
		//		Symbol iniVar = symbolTable.addIntVar("ini", "main");
		//		Symbol end = symbolTable.addLabel("end","main");
		Symbol initContext = symbolTable.addFunc( ProgramFunc.INIT_CONTEXT.value ,Integer.class);
		
		quadruples.add(QuadrupleFactory.createLabel( main ));
		//		quadruples.add(QuadrupleFact.createCall( __init , zero, iniVar ));
		//		quadruples.add(QuadrupleFact.createIfFalse( iniVar , end ));
		quadruples.add(QuadrupleFactory.createCall( initContext , zero ));
		
	}

	private static void genBegin(){
		Symbol begin = symbolTable.addLabel("begin","main");
		Symbol input = symbolTable.addFunc( ProgramFunc.INPUT.value ,Void.class);
		Symbol zero = symbolTable.addIntConst("0");
		Symbol update = symbolTable.addFunc( ProgramFunc.UPDATE.value ,Void.class);
		quadruples.add(QuadrupleFactory.createLabel( begin ));
		quadruples.add(QuadrupleFactory.createCall( input , zero ));
		quadruples.add(QuadrupleFactory.createCall( update , zero ));
	}

	private static void genEnd(){
		Symbol output = symbolTable.addFunc( ProgramFunc.OUTPUT.value ,Void.class);
		Symbol zero = symbolTable.addIntConst("0");
		Symbol begin = symbolTable.addLabel("begin","main");
		quadruples.add(QuadrupleFactory.createCall( output , zero ));
		quadruples.add(QuadrupleFactory.createGoto( begin ));
	}
}

