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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.github.leofds.iotladdereditor.compiler.domain.GenContext;
import com.github.leofds.iotladdereditor.compiler.domain.Kind;
import com.github.leofds.iotladdereditor.compiler.domain.ProgramFunc;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.domain.Symbol;
import com.github.leofds.iotladdereditor.compiler.domain.SymbolTable;
import com.github.leofds.iotladdereditor.compiler.generator.factory.QuadrupleFactory;

public class TimerOffDelay extends TimerInstruction{

	private static final long serialVersionUID = 1L;

	public TimerOffDelay() {
		super();
		setLabel("TMR OFF");
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		String name = getMemory().getName().isEmpty() ? "?" : getMemory().getName();
		
		g2d.setColor(new Color(0, 0, 255));
		g2d.setFont(new Font("Arial", Font.PLAIN, 12));
		g2d.drawString("Timer", (blockWidth)/4, blockHeight*getHeight()/2f);
		g2d.drawString("Preset", (blockWidth)/4, blockHeight*getHeight()/1.5f);
		g2d.drawString("Time Base", (blockWidth)/4, blockHeight*getHeight()/1.2f);
		int len = g2d.getFontMetrics().stringWidth(getLabel());
		g2d.drawString(getLabel(), (blockWidth*getWidth()-len)/2, blockHeight*getHeight()/4);
		
		g2d.setColor(new Color(0, 0, 0));
		len = g2d.getFontMetrics().stringWidth(name);
		g2d.drawString(name, blockWidth*getWidth()-len-15,  blockHeight*getHeight()/2f);
		len = g2d.getFontMetrics().stringWidth(""+getPreset());
		g2d.drawString(""+getPreset(), blockWidth*getWidth()-len-15, blockHeight*getHeight()/1.5f);
		len = g2d.getFontMetrics().stringWidth(""+getTimeBase());
		g2d.drawString(""+getTimeBase(), blockWidth*getWidth()-len-15, blockHeight*getHeight()/1.2f);
	}
	
	@Override
	public List<Quadruple> generateIRInit(GenContext context) {
		SymbolTable symbolTable = context.getSymbolTable();
		Symbol comment 		= symbolTable.addLiteral(getLabel()+" ("+getMemory()+")");
		Symbol enableVar	= symbolTable.addBoolVar(getEnableMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol accumVar	= symbolTable.addBoolVar(getAccumMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol presetVar	= symbolTable.addBoolVar(getPresetMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol doneVar	= symbolTable.addBoolVar(getDoneMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol timeVar	= symbolTable.addLongVar(getTimeMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol baseVar	= symbolTable.addLongVar(getTimeBaseMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol pre			= symbolTable.addIntConst(""+(getPreset()));
		Symbol base			= symbolTable.addIntConst(""+getTimeBase());
		Symbol accum		= symbolTable.addIntConst(""+(getAccum()));
		Symbol zero			= symbolTable.addIntConst("0");
		Symbol time			= symbolTable.addFunc(ProgramFunc.GETTIME.value, Long.class);
		
		List<Quadruple> quadruples = new ArrayList<Quadruple>();
		quadruples.add( QuadrupleFactory.createComment( comment ) );
		quadruples.add( QuadrupleFactory.createAssignment(zero, enableVar) );
		quadruples.add( QuadrupleFactory.createAssignment(accum, accumVar) );
		quadruples.add( QuadrupleFactory.createAssignment(pre, presetVar) );
		quadruples.add( QuadrupleFactory.createAssignment(base, baseVar) );
		quadruples.add( QuadrupleFactory.createAssignment(zero, doneVar) );
		quadruples.add( QuadrupleFactory.createCall(time, zero, timeVar) );
		return quadruples;
	}
	
	@Override
	public List<Quadruple> generateIR(GenContext context) {
		SymbolTable symbolTable = context.getSymbolTable();
		Symbol comment 		= symbolTable.addLiteral(getLabel()+" ("+getMemory()+")");
		Symbol value 		= symbolTable.add(new Symbol(getMemory().getName(), Kind.VARIABLE, getMemory().getType(), GenContext.GLOBAL_SCOPE));	//insere o timer na tabela de s�mbolos
		Symbol enableVar	= symbolTable.addBoolVar(getEnableMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol accumVar		= symbolTable.addBoolVar(getAccumMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol accum		= symbolTable.addIntConst(""+getAccum());
		Symbol presetVar	= symbolTable.addBoolVar(getPresetMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol doneVar		= symbolTable.addBoolVar(getDoneMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol timeVar		= symbolTable.addLongVar(getTimeMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol baseVar		= symbolTable.addLongVar(getTimeBaseMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol label1 		= symbolTable.addLabel(context.genLabel(), context.getScope());
		Symbol label2 		= symbolTable.addLabel(context.genLabel(), context.getScope());
		Symbol label3 		= symbolTable.addLabel(context.genLabel(), context.getScope());
		Symbol status		= symbolTable.addIntVar(context.getCurrentStatus(), context.getScope());
		Symbol one			= symbolTable.addIntConst("1");
		Symbol zero			= symbolTable.addIntConst("0");
		Symbol time			= symbolTable.addFunc(ProgramFunc.GETTIME.value, Long.class);
		Symbol temp1		= symbolTable.addLongVar(context.genTemp(), context.getScope());
		Symbol temp2		= symbolTable.addLongVar(context.genTemp(), context.getScope());
		
		
		List<Quadruple> quadruples = new ArrayList<Quadruple>();
		quadruples.add( QuadrupleFactory.createComment( comment ) );
		quadruples.add( QuadrupleFactory.createAssignment(status, enableVar) );
		quadruples.add( QuadrupleFactory.createIf(status, label1) );
		quadruples.add( QuadrupleFactory.createAssignment(zero, doneVar) );
		quadruples.add( QuadrupleFactory.createAssignment(accum, accumVar) );
		quadruples.add( QuadrupleFactory.createCall(time, zero, timeVar) );
		quadruples.add( QuadrupleFactory.createGoto(label2) );
		quadruples.add( QuadrupleFactory.createLabel(label1) );
		quadruples.add( QuadrupleFactory.createIf(doneVar, label2) );
		quadruples.add( QuadrupleFactory.createCall(time, zero, temp1) );
		quadruples.add( QuadrupleFactory.createSub(temp1, timeVar, temp2) );
		quadruples.add( QuadrupleFactory.createIfL(temp2, baseVar, label2) );
		quadruples.add( QuadrupleFactory.createAssignment(temp1, timeVar) );
		quadruples.add( QuadrupleFactory.createAdd(accumVar, one, accumVar) );
		quadruples.add( QuadrupleFactory.createIfL(accumVar, presetVar, label2) );
		quadruples.add( QuadrupleFactory.createAssignment(one, doneVar) );
		quadruples.add( QuadrupleFactory.createLabel(label2) );
		
		quadruples.add( QuadrupleFactory.createIfFalse(doneVar, label3) );
		quadruples.add( QuadrupleFactory.createAssignment(zero, status) );
		quadruples.add( QuadrupleFactory.createLabel(label3) );
		return quadruples;
	}
	
	@Override
	public String toString() {
		return "instruction=TMR OFF, " + super.toString();
	}
}
