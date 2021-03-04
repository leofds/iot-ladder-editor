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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.count;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.github.leofds.iotladdereditor.compiler.domain.GenContext;
import com.github.leofds.iotladdereditor.compiler.domain.Kind;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.domain.Symbol;
import com.github.leofds.iotladdereditor.compiler.domain.SymbolTable;
import com.github.leofds.iotladdereditor.compiler.generator.factory.QuadrupleFactory;

public class CountDown extends CountInstruction{
	
	private static final long serialVersionUID = 1L;

	public CountDown() {
		super();
		setLabel("CNT DOWN");
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		String labelName =getMemory().getName().isEmpty() ? "?" : getMemory().getName();
		
		g2d.setColor(new Color(0, 0, 255));
		g2d.setFont(new Font("Arial", Font.PLAIN, 12));
		g2d.drawString("Counter", (blockWidth)/4, blockHeight*getHeight()/2f);
		g2d.drawString("Preset", (blockWidth)/4, blockHeight*getHeight()/1.5f);
		g2d.drawString("Accum", (blockWidth)/4, blockHeight*getHeight()/1.2f);
		int len = g2d.getFontMetrics().stringWidth(getLabel());
		g2d.drawString(getLabel(), (blockWidth*getWidth()-len)/2, blockHeight*getHeight()/4);
		
		g2d.setColor(new Color(0, 0, 0));
		len = g2d.getFontMetrics().stringWidth(labelName);
		g2d.drawString(labelName, blockWidth*getWidth()-len-15,  blockHeight*getHeight()/2f);
		len = g2d.getFontMetrics().stringWidth(""+getPreset());
		g2d.drawString(""+getPreset(), blockWidth*getWidth()-len-15, blockHeight*getHeight()/1.5f);
		len = g2d.getFontMetrics().stringWidth(""+getAccum());
		g2d.drawString(""+getAccum(), blockWidth*getWidth()-len-15, blockHeight*getHeight()/1.2f);
	}
	
	@Override
	public List<Quadruple> generateIRInit(GenContext context) {
		SymbolTable symbolTable = context.getSymbolTable();
		Symbol comment 		= symbolTable.addLiteral(getLabel()+" ("+getMemory()+")");
		Symbol presetValue	= symbolTable.addBoolVar(getPresetMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol accumValue	= symbolTable.addBoolVar(getAccumMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol doneValue	= symbolTable.addIntConst(""+getDoneMemory().getName());
		Symbol ccValue		= symbolTable.addIntConst(""+getCountMemory().getName());
		Symbol pre			= symbolTable.addIntConst(""+getPreset());
		Symbol acc			= symbolTable.addIntConst(""+getAccum());
		Symbol zero			= symbolTable.addIntConst("0");
		
		List<Quadruple> quadruples = new ArrayList<Quadruple>();
		quadruples.add( QuadrupleFactory.createComment(comment) );
		quadruples.add( QuadrupleFactory.createAssignment(pre, presetValue) );
		quadruples.add( QuadrupleFactory.createAssignment(acc, accumValue) );
		quadruples.add( QuadrupleFactory.createAssignment(zero, doneValue) );
		quadruples.add( QuadrupleFactory.createAssignment(zero, ccValue) );
		return quadruples;
	}

	@Override
	public List<Quadruple> generateIR(GenContext context) {
		SymbolTable symbolTable = context.getSymbolTable();
		Symbol comment 		= symbolTable.addLiteral(getLabel()+" ("+getMemory()+")");
		Symbol value 		= symbolTable.add(new Symbol(getMemory().getName(), Kind.VARIABLE, getMemory().getType(), GenContext.GLOBAL_SCOPE));
		Symbol countValue	= symbolTable.addBoolVar(getCountMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol accumValue	= symbolTable.addBoolVar(getAccumMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol presetValue	= symbolTable.addBoolVar(getPresetMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol doneValue	= symbolTable.addBoolVar(getDoneMemory().getName(), GenContext.GLOBAL_SCOPE);
		Symbol label1 		= symbolTable.addLabel(context.genLabel(), context.getScope());
		Symbol label2 		= symbolTable.addLabel(context.genLabel(), context.getScope());
		Symbol label3 		= symbolTable.addLabel(context.genLabel(), context.getScope());
		Symbol status		= symbolTable.addIntVar(context.getCurrentStatus(), context.getScope());
		Symbol one			= symbolTable.addIntConst("1");
		Symbol zero			= symbolTable.addIntConst("0");
		
		List<Quadruple> quadruples = new ArrayList<Quadruple>();
		quadruples.add( QuadrupleFactory.createComment(comment) );		
		quadruples.add( QuadrupleFactory.createIf(status, label1) );
		quadruples.add( QuadrupleFactory.createAssignment(zero, countValue) );
		quadruples.add( QuadrupleFactory.createGoto(label2) );
		quadruples.add( QuadrupleFactory.createLabel(label1) );
		quadruples.add( QuadrupleFactory.createIf(countValue, label2) );
		quadruples.add( QuadrupleFactory.createSub(accumValue, one, accumValue) );
		quadruples.add( QuadrupleFactory.createAssignment(one, countValue) );
		quadruples.add( QuadrupleFactory.createIfG(accumValue, presetValue, label2) );
		quadruples.add( QuadrupleFactory.createAssignment(one, doneValue) );
		quadruples.add( QuadrupleFactory.createLabel(label2) );
		quadruples.add( QuadrupleFactory.createIfFalse(status, label3) );
		quadruples.add( QuadrupleFactory.createIf(doneValue, label3) );
		quadruples.add( QuadrupleFactory.createAssignment(zero, status) );
		quadruples.add( QuadrupleFactory.createLabel(label3) );
		return quadruples;
	}

	@Override
	public String toString() {
		return "instruction=CNT DOWN, " + super.toString();
	}
}


