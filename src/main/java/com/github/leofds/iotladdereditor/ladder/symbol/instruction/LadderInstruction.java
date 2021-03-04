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
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.List;

import javax.swing.JMenuItem;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.application.ProjectContainer;
import com.github.leofds.iotladdereditor.compiler.domain.GenContext;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.exception.SemanticErrorException;
import com.github.leofds.iotladdereditor.compiler.exception.SemanticWarnigException;
import com.github.leofds.iotladdereditor.device.Device;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.ladder.symbol.LadderSymbol;
import com.github.leofds.iotladdereditor.ladder.view.DialogScreen;

public abstract class LadderInstruction extends LadderSymbol implements Comparable<LadderInstruction>{

	private static final long serialVersionUID = 1L;
	private String label; 
	private DeviceMemory memory;
	private LadderInstruction up;
	private LadderInstruction down;
	private LadderInstruction next;
	private LadderInstruction previous;

	public LadderInstruction(int width, int height, int row, int col,DeviceMemory memory) {
		super(width, height, row, col);
		this.memory = memory;
		this.label = "";
	}

	public DeviceMemory getMemory() {
		return memory;
	}

	public void setMemory(DeviceMemory memory) {
		this.memory = memory;
	}

	public LadderInstruction getUp() {
		return up;
	}

	public void setUp(LadderInstruction up) {
		this.up = up;
	}

	public LadderInstruction getNext() {
		return next;
	}

	public void setNext(LadderInstruction next) {
		this.next = next;
	}

	public LadderInstruction getDown() {
		return down;
	}

	public void setDown(LadderInstruction down) {
		this.down = down;
	}

	public LadderInstruction getPrevious() {
		return previous;
	}

	public void setPrevious(LadderInstruction previous) {
		this.previous = previous;
	} 

	public void clearLink(){
		next = null;
		down = null;
		up =  null;
		previous = null;
	}

	public void setLabel(String label){
		this.label = label;
	}
	
	public String getLabel(){
		return label;
	}
	
	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		Graphics2D g = (Graphics2D) g2d.create();
		g.setColor(new Color(0, 0, 255));
		g.setStroke(new BasicStroke(1));
		g.translate(-getX(), -getY());
		if(down != null){
			g.draw(new Line2D.Double(getX(), getY()+blockHeight/2, down.getX(), down.getY()+blockHeight/2));
		}
		if(up != null){
			g.draw(new Line2D.Double(getX()+(blockWidth*getWidth()), getY()+blockHeight/2, up.getX(), up.getY()+blockHeight/2));
		}
		g.dispose();
	}

	@Override
	public String toString() {
		if(memory != null){
			return "memory="+memory.getName();
		}
		return "";
	}

	public void viewInstructionProperty(){
		DialogScreen dialog = getPropertyScreen();
		if(dialog != null){
			beforeShowScreen(dialog);
			dialog.setModal(true);  
			dialog.setResizable(false);
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
			if(dialog.getResult()){
				afterShowScreen(dialog);
				Mediator.getInstance().updateProjectAndViews();
			}
		}
		Mediator.getInstance().unselectInstruction();
	}
	
	public void resetMemory(String name){
		if(getMemory() != null && !getMemory().getName().isEmpty()){
			if(getMemory().getName().equals(name)){
				setMemory(new DeviceMemory());
			}
		}
	}
	
	public abstract void analyze() throws SemanticErrorException , SemanticWarnigException;

	public abstract List<Quadruple> generateIRInit(GenContext context);

	public abstract List<Quadruple> generateIR(GenContext context);

	public abstract boolean addMemory(DeviceMemory memory,int x,int y);

	public abstract List<JMenuItem> getMenuItens(final ProjectContainer project);
	
	public abstract DialogScreen getPropertyScreen();		
	
	public abstract void beforeShowScreen(DialogScreen dialog);
	
	public abstract void afterShowScreen(DialogScreen dialog);
	
	public abstract void updateDevice(Device device);
	
	@Override
	public int compareTo(LadderInstruction o) {
		return  memory.getName().compareTo(o.getMemory().getName());
	}
	
}
