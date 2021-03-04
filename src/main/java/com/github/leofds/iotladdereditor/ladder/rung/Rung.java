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
package com.github.leofds.iotladdereditor.ladder.rung;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.leofds.iotladdereditor.ladder.config.Constants;
import com.github.leofds.iotladdereditor.ladder.symbol.VerticalLink;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.HorizontalLink;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LeftPowerRail;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.RightPowerRail;

public class Rung implements Serializable, Iterable<LadderInstruction>{

	private static final long serialVersionUID = 1L;
	private LadderInstruction first;
	private LeftPowerRail leftPowerRail;	
	private RightPowerRail rigthPowerRail;
	private int number;
	private int row;

	public Rung() {
	}

	public Rung(int number,int row) {
		this.number = number;
		this.row = row;
		this.rigthPowerRail = new RightPowerRail(row, Constants.colsNumber+1);
		this.leftPowerRail = new LeftPowerRail(row, 0, number);
		this.first = leftPowerRail;

		LadderInstruction current = this.leftPowerRail;  
		for(int col=1;col<=Constants.colsNumber;col++){
			LadderInstruction next = new HorizontalLink(row, col);
			current.setNext(next);
			next.setPrevious(current);
			current = next;
		}		
		current.setNext(this.rigthPowerRail);
	}

	public void setRow(int row){
		this.row = row;
		leftPowerRail.setNumber(this.number);
		updateRow(row, leftPowerRail);
	}

	public int getRow() {
		return row;
	}

	public void setNumber(int number){
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public LadderInstruction getFirst() {
		return first;
	}

	public LadderInstruction getLast(){
		LadderInstruction last = first;
		while(!(last.getNext() instanceof RightPowerRail)){
			last = last.getNext();
		}
		return last;
	}

	private void recursivePaint(Graphics2D g2d,LadderInstruction instruction){
		if(instruction != null){
			recursivePaint(g2d, instruction.getDown());
			recursivePaint(g2d, instruction.getNext());
			instruction.draw(g2d);
		}
	}

	private void paint(Graphics2D g2d){

		// background
		int lines = getLines();
		g2d.setColor(new Color(245, 245, 245));
		for(int r=row;r<(row+lines);r++){
			for(int c=0;c<Constants.colsNumber+2;c++){
				g2d.draw(new Rectangle2D.Float(c*Constants.blockWidth, r*Constants.blockHeight, Constants.blockWidth, Constants.blockHeight));
			}
			if(r!=row){
				new VerticalLink(r, 0).draw(g2d);
				new VerticalLink(r, Constants.colsNumber+1).draw(g2d);
			}
		}

		// instructions
		recursivePaint(g2d,leftPowerRail);
	}

	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g.create();
		paint(g2d);
		g2d.dispose();
	}

	// Checks if the instructions are in the same column
	private boolean isSameColumn(LadderInstruction instruction,LadderInstruction up){
		if(instruction != null){
			LadderInstruction li = instruction.getNext() != null ? instruction.getNext() : instruction.getUp();

			if(li instanceof RightPowerRail && up instanceof RightPowerRail){	// end of rung
				return true;
			}
			if(li != null && up != null){
				if(li.getCol() == up.getCol()){
					return true;
				}
			}
		}
		return false;
	}

	// Returns the number of lines between two instructions
	private int getDepth(LadderInstruction instruction,LadderInstruction up){
		if(instruction != null){
			int depth = instruction.getRow()+instruction.getHeight();
			if(!isSameColumn(instruction, up)){
				int size = 0;

				LadderInstruction current = instruction.getNext();
				if(current != null){
					if(current.getDown() != null){
						current = current.getDown();
						do{
							size = getDepth(current, up);
							if(size > depth){
								depth = size;
							}
							current = current.getDown();
						}while(current != null);
					}else{
						size = getDepth(current, up);
						if(size > depth){
							depth = size;
						}
					}
				}else{
					current = instruction.getUp();
					if(current != null){
						size = getDepth(current.getPrevious(), up);
						if(size > depth){
							depth = size;
						}
					}
				}
			}
			return depth;
		}
		return 0;
	}

	public int getCountReferenceUp(LadderInstruction current,LadderInstruction up){
		int count = 0;
		if(current != null){
			count += getCountReferenceUp(current.getDown(), up);
			count += getCountReferenceUp(current.getNext(), up);
			if(current.getUp() != null && current.getUp().equals(up)){
				count++;
			}
		}
		return count;
	}

	private LadderInstruction getUp(LadderInstruction instruction){
		if(instruction != null){
			if(instruction.getUp() != null){
				return instruction.getUp();
			}
			return getUp(instruction.getNext());
		}
		return null;
	}

	private void updateRow(int row,LadderInstruction instruction){
		if(instruction != null){
			updateRow(row, instruction.getNext());
			instruction.setRow(row);
			if(instruction.getDown() != null){
				LadderInstruction up = getUp(instruction.getDown());
				row = getDepth(instruction, up);
				updateRow(row, instruction.getDown());
			}
		}
	}

	private int getLinesRecursive(LadderInstruction instruction){
		int lines = 0;
		if(instruction != null){
			lines = instruction.getRow()+instruction.getHeight();
			int aux = getLinesRecursive(instruction.getDown());
			if(aux > lines){
				lines = aux;
			}
			aux = getLinesRecursive(instruction.getNext());
			if(aux > lines){
				lines = aux;
			}
		}
		return lines;
	}

	public int getLines(){
		return getLinesRecursive(first)-first.getRow();
	}

	public boolean isUp(LadderInstruction current,LadderInstruction up){
		if(current != null){
			if(current.getUp() != null && current.getUp().equals(up)){
				return true;
			}
			if(isUp(current.getDown(),current)){
				return true;
			}
			if(isUp(current.getNext(),current)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isDown(LadderInstruction current,LadderInstruction down){
		if(current != null){
			if(current.getDown() != null && current.getDown().equals(down)){
				return true;
			}
			if(isDown(current.getDown(),current)){
				return true;
			}
			if(isDown(current.getNext(),current)){
				return true;
			}
		}
		return false;
	}
	
	public boolean isNext(LadderInstruction current,LadderInstruction next){
		if(current != null){
			if(current.getNext() != null && current.getNext().equals(next)){
				return true;
			}
			if(isNext(current.getDown(),current)){
				return true;
			}
			if(isNext(current.getNext(),current)){
				return true;
			}
		}
		return false;
	}

	private void replaceUp(LadderInstruction instruction,LadderInstruction oldUp, LadderInstruction newUp){
		if(instruction != null){
			replaceUp(instruction.getDown(), oldUp, newUp );
			replaceUp(instruction.getNext(), oldUp, newUp);
			if(instruction.getUp() != null){
				if(instruction.getUp().equals(oldUp)){
					instruction.setUp(newUp);
				}
			}
		}
	}

	private boolean insertOverride(LadderInstruction current,LadderInstruction moved){
		LadderInstruction next = current.getNext();
		if(moved.getWidth() > 1){
			if(next instanceof HorizontalLink){
				next = next.getNext();
			}else{
				return false;
			}
		}
		moved.setUp(current.getUp());
		moved.setDown(current.getDown());
		moved.setNext(next);
		moved.setPrevious(current.getPrevious());
		moved.setPosition(current.getRow(), current.getCol());
		if(next != null){
			next.setPrevious(moved);
		}
		current.getPrevious().setNext(moved);
		replaceUp(first, current, moved);
		return true;
	}

	private boolean insertParallel(LadderInstruction current,LadderInstruction moved){
		if(moved.getWidth() == 1 && current.getWidth() == 1){
			if(current.getDown() != null){
				moved.setDown(current.getDown());
			}
			current.setDown(moved);
			if(current.getUp() != null){
				moved.setUp(current.getUp());
			}else{
				moved.setUp(current.getNext());
			}
			moved.setPosition(current.getRow(), current.getCol());
			return true;
		}
		return false;
	}

	private LadderInstruction getRecursiveInstruction(int x,int y,LadderInstruction instruction){
		if(instruction != null){
			if(instruction.contains(x, y)){
				return instruction;
			}
			LadderInstruction other = getRecursiveInstruction(x,y,instruction.getDown()); 
			if(other != null){
				return other;
			}
			other = getRecursiveInstruction(x,y,instruction.getNext()); 
			if(other != null){
				return other;
			}
		}
		return null;
	}


	public LadderInstruction getInstruction(int x,int y){
		return getRecursiveInstruction(x,y,leftPowerRail);
	}

	public boolean add(int x,int y,LadderInstruction instruction){
		LadderInstruction li = getInstruction(x, y);
		if(li != null){
			if(li instanceof HorizontalLink){
				return insertOverride(li, instruction);
			}
			if(!(li instanceof LeftPowerRail) && !(li instanceof RightPowerRail)){
				return insertParallel(li, instruction);
			}
		}
		return false;
	}

	public LadderInstruction getToDown(LadderInstruction down){
		for(LadderInstruction instruction:this){
			if(isDown(instruction, down)){
				return instruction;
			}
		}
		return null;
	}
	
	public LadderInstruction getToNext(LadderInstruction next){
		for(LadderInstruction instruction:this){
			if(isNext(instruction, next)){
				return instruction;
			}
		}
		return null;
	}
	
	
	public List<LadderInstruction> getToUp(LadderInstruction up){
		List<LadderInstruction> ups = new ArrayList<LadderInstruction>();
		for(LadderInstruction instruction:this){
			if(instruction.getUp() != null && instruction.getUp().equals(up)){
				ups.add(instruction);
			}
		}
		return ups;
	}
	
	private boolean removeSingleParallelInstruction(LadderInstruction instruction){
		LadderInstruction toDown = getToDown(instruction);
		if(toDown != null){
			LadderInstruction down = instruction.getDown();
			toDown.setDown(down);
			return true;
		}
		return false;
	}
	
	private boolean removeLastParallelInstruction(LadderInstruction instruction){
		LadderInstruction toNext = getToNext(instruction);
		if(toNext != null){
			LadderInstruction hLink = new HorizontalLink(instruction.getX(),instruction.getY());
			for(LadderInstruction toUp: getToUp(instruction)){
				toUp.setUp(hLink);
			}
			toNext.setNext(hLink);
			hLink.setPrevious(toNext);
			hLink.setDown(instruction.getDown());
			if(instruction.getWidth()>1){
				//TODO
			}
			hLink.setUp(instruction.getUp());
			return true;
		}
		return false;
	}
	
	private boolean removeFirstParallelInstruction(LadderInstruction instruction){
		LadderInstruction toDown = getToDown(instruction);
		if(toDown != null){
			LadderInstruction hLink = new HorizontalLink(instruction.getX(),instruction.getY());
			toDown.setDown(hLink);
			hLink.setDown(instruction.getDown());
			if(instruction.getWidth()>1){
				//TODO
			}
			hLink.setNext(instruction.getNext());
			return true;
		}
		return false;
	}
	
	private boolean removeBaseParallelInstruction(LadderInstruction instruction){
		LadderInstruction toNext = getToNext(instruction);
		if(toNext != null){
			List<LadderInstruction> toUps = getToUp(instruction.getNext());
			if(toUps != null){
				if(toUps.size() > 0){
					for(LadderInstruction toUp: getToUp(instruction)){
						toUp.setUp(instruction.getDown());
					}
					toNext.setNext(instruction.getDown());
					instruction.getDown().setPrevious(toNext);
					LadderInstruction next = instruction.getDown();
					while(next.getNext()!=null){
						next = next.getNext();
					}
					next.setNext(next.getUp());
					next.setUp(null);
					next.getNext().setPrevious(next);
				}else{					
					LadderInstruction hLink = new HorizontalLink(instruction.getRow(),instruction.getCol());
					for(LadderInstruction toUp: getToUp(instruction)){
						toUp.setUp(hLink);
					}
					toNext.setNext(hLink);
					hLink.setPrevious(toNext);
					hLink.setDown(instruction.getDown());
					if(instruction.getWidth()>1){
						//TODO
					}
					hLink.setNext(instruction.getNext());
				}
				return true;
			}
		}
		return false;
	}
	
	private boolean removeSerialInstruction(LadderInstruction instruction){
		LadderInstruction toNext = getToNext(instruction);
		if(toNext != null){
			LadderInstruction hLink = new HorizontalLink(instruction.getRow(),instruction.getCol());
			for(LadderInstruction toUp: getToUp(instruction)){
				toUp.setUp(hLink);
			}
			toNext.setNext(hLink);
			hLink.setPrevious(toNext);
			if(instruction.getWidth() > 1){
				for(int i=1;i<instruction.getWidth();i++){
					LadderInstruction lastHLink = hLink;
					hLink = new HorizontalLink(instruction.getRow(),instruction.getCol()+1);
					lastHLink.setNext(hLink);
					hLink.setPrevious(lastHLink);
				}
			}
			hLink.setNext(instruction.getNext());
			instruction.getNext().setPrevious(hLink);
			return true;
		}
		return false;
	}
	
	public boolean remove(LadderInstruction instruction){
		if(instruction.getUp() != null && getToDown(instruction) != null){
			return removeSingleParallelInstruction(instruction);
		}
		if(instruction.getUp() != null){
			return removeLastParallelInstruction(instruction);
		}
		if(getToDown(instruction) != null){
			return removeFirstParallelInstruction(instruction);
		}
		if(instruction.getDown()!=null){
			return removeBaseParallelInstruction(instruction);
		}
		return removeSerialInstruction(instruction);
	}
	
	public boolean contain(LadderInstruction instruction){
		for(LadderInstruction li:this){
			if(instruction.equals(li)){
				return true;
			}
		}
		return false;
	}
	
	private void readRung(List<LadderInstruction> list,LadderInstruction instruction){
		if(instruction!=null){
			readRung(list, instruction.getDown());
			readRung(list, instruction.getNext());
			list.add(instruction);
		}
	}
	
	private List<LadderInstruction> getInstructions(){
		ArrayList<LadderInstruction> instructions = new ArrayList<LadderInstruction>();
		readRung(instructions, getFirst());
		return instructions;
	}

	@Override
	public Iterator<LadderInstruction> iterator() {
		return new Iterator<LadderInstruction>() {

			private List<LadderInstruction> instructions = getInstructions();
			
			@Override
			public boolean hasNext() {
				if(instructions.isEmpty()){
					return false;
				}
				return true;
			}

			@Override
			public LadderInstruction next() {
				if(instructions.isEmpty()){
					return null;
				}
				return instructions.remove(0);
			}

			@Override
			public void remove() {
			}
		};
	}
	
	private void recursiveUpdateUpIt(LadderInstruction it, LadderInstruction oldIt, LadderInstruction newIt) {
		if(it != null) {
			recursiveUpdateUpIt(it.getNext(),oldIt,newIt);
			recursiveUpdateUpIt(it.getDown(),oldIt,newIt);
			if(it.getUp() != null && it.getUp() == oldIt) {
				it.setUp(newIt);
			}
		}
	}
	
	private void recursiveRemoveEnd(LadderInstruction it) {
		if(it != null) {
			LadderInstruction next = it.getNext();
			if(next != null) {
				HorizontalLink newLink = new HorizontalLink();
				newLink.setNext(next.getNext());
				recursiveUpdateUpIt(first,next,newLink);
			}else{
				recursiveRemoveEnd(it.getNext());
			}
		}
	}
	
	public void removeEnd() {
		recursiveRemoveEnd(first);
	}
}
