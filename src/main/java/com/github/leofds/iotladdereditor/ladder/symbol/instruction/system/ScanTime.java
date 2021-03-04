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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.github.leofds.iotladdereditor.compiler.domain.GenContext;
import com.github.leofds.iotladdereditor.compiler.domain.ProgramFunc;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.domain.Symbol;
import com.github.leofds.iotladdereditor.compiler.domain.SymbolTable;
import com.github.leofds.iotladdereditor.compiler.exception.SemanticErrorException;
import com.github.leofds.iotladdereditor.compiler.exception.SemanticWarnigException;
import com.github.leofds.iotladdereditor.compiler.generator.factory.QuadrupleFactory;
import com.github.leofds.iotladdereditor.device.Device;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.ladder.view.DialogScreen;

public class ScanTime extends SystemInstruction{

	private static final long serialVersionUID = 1L;
	
	private int seconds;
	
	public ScanTime() {
		super(new DeviceMemory("scanTime", null));
		setLabel("SCAN TIME");
		seconds = 1;
	}
	
	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		
		g2d.setColor(new Color(0, 0, 255));
		g2d.setFont(new Font("Arial", Font.PLAIN, 12));
		g2d.drawString("Seconds", (blockWidth)/4, blockHeight*getHeight()/1.5f);
		int len = g2d.getFontMetrics().stringWidth("SYSTEM");
		g2d.drawString("SYSTEM", (blockWidth*getWidth()-len)/2, blockHeight*getHeight()/4);
		len = g2d.getFontMetrics().stringWidth(getLabel());
		g2d.drawString(getLabel(), (blockWidth*getWidth()-len)/2, blockHeight*getHeight()/2.8f);
		
		g2d.setColor(new Color(0, 0, 0));
		len = g2d.getFontMetrics().stringWidth(""+getSeconds());
		g2d.drawString(""+getSeconds(), blockWidth*getWidth()-len-15,  blockHeight*getHeight()/1.5f);
	}

	@Override
	public List<Quadruple> generateIR(GenContext context) {
		SymbolTable symbolTable = context.getSymbolTable();
		Symbol comment 			= symbolTable.addLiteral(getLabel());
		Symbol sysPrintLoops	= symbolTable.addFunc(ProgramFunc.SCANTIME.value, Void.class);
		Symbol zero			    = symbolTable.addIntConst("0");
		
		List<Quadruple> quadruples = new ArrayList<Quadruple>();
		quadruples.add( QuadrupleFactory.createComment(comment) );
		quadruples.add( QuadrupleFactory.createCall(sysPrintLoops, zero) );
		return quadruples;
	}

	@Override
	public void analyze() throws SemanticErrorException, SemanticWarnigException {
	}

	@Override
	public List<Quadruple> generateIRInit(GenContext context) {
		return null;
	}

	@Override
	public boolean addMemory(DeviceMemory memory, int x, int y) {
		return false;
	}

	@Override
	public DialogScreen getPropertyScreen() {
		return null;
	}

	@Override
	public void beforeShowScreen(DialogScreen dialog) {
	}

	@Override
	public void afterShowScreen(DialogScreen dialog) {
	}

	@Override
	public void updateDevice(Device device) {
	}
}
