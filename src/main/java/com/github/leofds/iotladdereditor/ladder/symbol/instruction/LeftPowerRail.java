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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.application.ProjectContainer;
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
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.view.DialogScreen;

public class LeftPowerRail extends LadderInstruction{
	
	private static final long serialVersionUID = 1L;
	private Integer number;
	
	public LeftPowerRail() {
		super(1,1,0,0,null);
	}
	
	public LeftPowerRail(int row,int col) {
		super(1,1,row,col,null);
		setPosition(row, col);
	}
	
	public LeftPowerRail(int row,int col,int number) {
		super(1,1,row,col,null);
		this.number = number;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		g2d.setColor(new Color(0, 0, 255));
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(new Line2D.Double(blockWidth/2, 0, blockWidth/2, blockHeight));
		g2d.draw(new Line2D.Double(blockWidth/2, blockHeight/2, blockWidth, blockHeight/2));
		if(number!=null){
			g2d.setFont(new Font("Arial", Font.PLAIN, 12));
			g2d.drawString(String.format("%03d", number), 5, blockHeight/2+5);
		}
	}

	@Override
	public List<Quadruple> generateIR(GenContext context) {
		SymbolTable symbolTable = context.getSymbolTable();
		Symbol label = symbolTable.addFunc( String.format("%s%03d", ProgramFunc.RUNG.value, number), Void.class);
		Symbol one = symbolTable.addIntConst("1");
		Symbol currentStatus = symbolTable.addIntVar(context.getCurrentStatus(), context.getScope());
		
		List<Quadruple> quadruples = new ArrayList<Quadruple>();
		quadruples.add( QuadrupleFactory.createLabel( label ) );
		quadruples.add( QuadrupleFactory.createAssignment( one , currentStatus ) );
		return quadruples;
	}
	
	@Override
	public List<JMenuItem> getMenuItens(final ProjectContainer project){
		List<JMenuItem> itens = new ArrayList<JMenuItem>();
		JMenuItem remove = new JMenuItem(Strings.remove());
		itens.add(remove);
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Mediator me = Mediator.getInstance();
				me.setChangedProgram();
				project.getLadderProgram().getRungs().delete(LeftPowerRail.this);
				me.updateProjectAndViews();
			}
		});
		return itens;
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
