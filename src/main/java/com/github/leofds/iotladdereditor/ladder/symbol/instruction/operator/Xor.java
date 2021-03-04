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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator;

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

public class Xor extends OperatorInstruction{

	private static final long serialVersionUID = 1L;

	public Xor() {
		super();
		setLabel("XOR");
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		String valueSrcA = getSourceA().getName().isEmpty() ? "?" : getSourceA().getName();
		String valueSrcB = getSourceB().getName().isEmpty() ? "?" : getSourceB().getName();
		String valueDst = getDestiny().getName().isEmpty() ? "?" : getDestiny().getName();
		
		g2d.setColor(new Color(0, 0, 255));
		g2d.setFont(new Font("Arial", Font.PLAIN, 12));
		int len = g2d.getFontMetrics().stringWidth(getLabel());
		g2d.drawString(getLabel(), (blockWidth*getWidth()-len)/2, blockHeight*getHeight()/4);
		g2d.drawString("Src A", (blockWidth)/4, blockHeight*getHeight()/2f);
		g2d.drawString("Src B", (blockWidth)/4, blockHeight*getHeight()/1.5f);
		g2d.drawString("Dest", (blockWidth)/4, blockHeight*getHeight()/1.2f);
		
		g2d.setColor(new Color(0, 0, 0));
		len = g2d.getFontMetrics().stringWidth(valueSrcA);
		g2d.drawString(valueSrcA, blockWidth*getWidth()-len-15,  blockHeight*getHeight()/2f);
		len = g2d.getFontMetrics().stringWidth(valueSrcB);
		g2d.drawString(valueSrcB, blockWidth*getWidth()-len-15,  blockHeight*getHeight()/1.5f);
		len = g2d.getFontMetrics().stringWidth(valueDst);
		g2d.drawString(valueDst, blockWidth*getWidth()-len-15, blockHeight*getHeight()/1.2f);
	}
	
	@Override
	public List<Quadruple> generateIR(GenContext context) {
		SymbolTable symbolTable = context.getSymbolTable();
		Symbol comment = symbolTable.addLiteral(getLabel());
		Symbol status = symbolTable.addIntVar(context.getCurrentStatus(), context.getScope());
		Symbol label = symbolTable.addLabel(context.genLabel(), context.getScope());

		Symbol sourceA;
		if(getSourceA().getName().charAt(0) > '9'){
			sourceA = symbolTable.add(new Symbol(getSourceA().getName(), Kind.VARIABLE, getSourceA().getType(), GenContext.GLOBAL_SCOPE));
		}else{
			sourceA = symbolTable.add(new Symbol(getSourceA().getName(), Kind.CONSTANT, getSourceA().getType(), null));
		}
		Symbol sourceB;
		if(getSourceB().getName().charAt(0) > '9'){
			sourceB = symbolTable.add(new Symbol(getSourceB().getName(), Kind.VARIABLE, getSourceB().getType(), GenContext.GLOBAL_SCOPE));
		}else{
			sourceB = symbolTable.add(new Symbol(getSourceB().getName(), Kind.CONSTANT, getSourceB().getType(), null));
		}
		Symbol destiny = symbolTable.add(new Symbol(getDestiny().getName(), Kind.VARIABLE, getDestiny().getType(), GenContext.GLOBAL_SCOPE)); 
		
		List<Quadruple> quadruples = new ArrayList<Quadruple>();
		quadruples.add( QuadrupleFactory.createComment( comment ) );
		quadruples.add( QuadrupleFactory.createIfFalse(status, label) );
		quadruples.add( QuadrupleFactory.createXor( sourceA , sourceB , destiny ) );
		quadruples.add( QuadrupleFactory.createLabel(label) );
		return quadruples;
	}
	
	@Override
	public String toString() {
		return "instruction=XOR, " + super.toString();
	}
}
