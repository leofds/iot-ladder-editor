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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import com.github.leofds.iotladdereditor.compiler.domain.GenContext;
import com.github.leofds.iotladdereditor.compiler.domain.Kind;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.domain.Symbol;
import com.github.leofds.iotladdereditor.compiler.domain.SymbolTable;
import com.github.leofds.iotladdereditor.compiler.generator.factory.QuadrupleFactory;
import com.github.leofds.iotladdereditor.device.DeviceMemory;

public class NegativeTransitionCoil extends CoilInstruction{

	private static final long serialVersionUID = 1L;
	
	private DeviceMemory lastValue = new DeviceMemory();

	public NegativeTransitionCoil() {
		super();
		setLabel("(N)");
	}

	@Override
	public void setMemory(DeviceMemory memory) {
		lastValue = new DeviceMemory("LAST_"+memory.getName().split("[:]")[0], memory.getType());
		super.setMemory(memory);
	}
	
	public DeviceMemory getLastValue() {
		return lastValue;
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		g2d.setColor(new Color(0, 0, 255));
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(new Line2D.Double(0, blockHeight/2, blockWidth/4, blockHeight/2));
		g2d.draw(new Line2D.Double(blockWidth-blockWidth/4, blockHeight/2, blockWidth, blockHeight/2));
		g2d.draw(new Arc2D.Double(blockWidth/4, blockHeight/3, blockWidth/4, blockHeight/3, 110, 140, Arc2D.OPEN));
		g2d.draw(new Arc2D.Double(blockWidth/2, blockHeight/3, blockWidth/4, blockHeight/3, 290, 140, Arc2D.OPEN));
		g2d.setColor(new Color(0, 0, 255));
		g2d.setFont(new Font("Arial", Font.PLAIN, 14));
		int len = g2d.getFontMetrics().stringWidth("N");
		g2d.drawString("N", (blockWidth-len)/2, blockHeight/1.7f);
	}

	@Override
	public List<Quadruple> generateIR(GenContext context) {
		lastValue = new DeviceMemory("LAST_"+getMemory().getName().split("[:]")[0], getMemory().getType());
		
		SymbolTable symbolTable = context.getSymbolTable();
		Symbol comment 		= symbolTable.addLiteral(getLabel()+" ("+getMemory()+")");
		Symbol lastValue 	= symbolTable.add(new Symbol(getLastValue().getName(), Kind.VARIABLE, getLastValue().getType(), GenContext.GLOBAL_SCOPE));
		Symbol value 		= symbolTable.add(new Symbol(getMemory().getName(), Kind.VARIABLE, getMemory().getType(), GenContext.GLOBAL_SCOPE));
		Symbol label 		= symbolTable.addLabel(context.genLabel(), context.getScope()); 
		Symbol status		= symbolTable.addBoolVar(context.getCurrentStatus(), context.getScope());
		Symbol one			= symbolTable.addIntConst("1");
		Symbol zero			= symbolTable.addIntConst("0");
		
		List<Quadruple> quadruples = new ArrayList<Quadruple>();
		quadruples.add( QuadrupleFactory.createComment( comment ) );
		quadruples.add( QuadrupleFactory.createAssignment( zero , value ) );
		quadruples.add( QuadrupleFactory.createIfFalse( lastValue , label ) );
		quadruples.add( QuadrupleFactory.createIf( status , label ) );
		quadruples.add( QuadrupleFactory.createAssignment( one , value ) );
		quadruples.add( QuadrupleFactory.createLabel( label ) );
		quadruples.add( QuadrupleFactory.createAssignment( status , lastValue ) );
		return quadruples;
	}
	
	@Override
	public String toString() {
		return "instruction=(N), " + super.toString();
	}
}
