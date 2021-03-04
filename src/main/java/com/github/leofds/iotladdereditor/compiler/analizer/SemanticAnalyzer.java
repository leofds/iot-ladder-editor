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
package com.github.leofds.iotladdereditor.compiler.analizer;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.compiler.exception.SemanticErrorException;
import com.github.leofds.iotladdereditor.compiler.exception.SemanticWarnigException;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;
import com.github.leofds.iotladdereditor.ladder.rung.Rung;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;

public class SemanticAnalyzer {

	private static int verifyInstructionValue(Rung rung,LadderInstruction instruction){
		int errs = 0;
		if(instruction != null){
			errs += verifyInstructionValue(rung,instruction.getDown());
			errs += verifyInstructionValue(rung,instruction.getNext());
			try{
				instruction.analyze();
			}catch(SemanticErrorException e){
				errs++;
				Mediator.getInstance().outputConsoleMessage( Strings.error() +" [rung="+rung.getNumber()+", col="+instruction.getCol()+", "+instruction+"]: "+e.getMessage() );
			}catch(SemanticWarnigException e){
				Mediator.getInstance().outputConsoleMessage( Strings.warning()+" [rung="+rung.getNumber()+", col="+instruction.getCol()+", "+instruction+"]: "+e.getMessage() );
			}
		}
		return errs;
	}

	public static boolean analyze(LadderProgram ladderProgram){
		boolean success = true;
		for(Rung rung:ladderProgram.getRungs()){
			if(verifyInstructionValue(rung,rung.getFirst())!=0){
				success = false;
			}
		}
		return success;
	}
}
