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

import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LeftPowerRail;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.count.CountInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.TimerInstruction;

public class Rungs implements Serializable,Iterable<Rung>{

	private static final long serialVersionUID = 1L;
	private List<Rung> rungs = new ArrayList<Rung>();
	
	public Rungs() {
		rungs.add(new Rung(1, 0));		
	}	
	
	public void clear(){
		rungs.clear();
		rungs.add(new Rung(1, 0));
	}
	
	public boolean addInstruction(int x,int y,LadderInstruction instruction){
		for(Rung rung:rungs){
			if(rung.add(x, y, instruction)){
				return true;
			}
		}
		return false;
	}

	public boolean addRung(int x,int y){
		Rung rung = new Rung(0, 0);
		for(int i=0;i<rungs.size();i++){
			Rung r = rungs.get(i);
			if(r.getInstruction(x, y)!=null){
				rungs.add(i+1, rung);
				refresh();
				return true;
			}
		}
		return false;
	}
	
	public boolean addRung(){
		Rung rung = new Rung(0, 0);
		rungs.add(rungs.size(), rung);
		refresh();
		return true;
	}

	public void refresh(){
		int row = 0;
		int count = 1;
		for(Rung rung:rungs){
			rung.setNumber(count++);
			rung.setRow(row);
			row += rung.getLines();
		}
	}

	public int getLines(){
		int lines = 0;
		for(Rung rung:rungs){
			lines += rung.getLines();
		}
		return lines;
	}

	public void draw(Graphics g){
		for(Rung rung:rungs){
			rung.draw(g);
		}
	}

	public LadderInstruction getInstruction(int x,int y){
		for(Rung rung:rungs){
			LadderInstruction instruction = rung.getInstruction(x, y);
			if(instruction != null){
				return instruction;
			}
		}
		return null;
	}

	private void removeRung(int number){
		rungs.remove(number-1);
	}

	public boolean remove(LadderInstruction instruction){
		if(instruction instanceof LeftPowerRail){
			LeftPowerRail leftPowerRail = (LeftPowerRail) instruction;
			removeRung(leftPowerRail.getNumber());
			if(leftPowerRail.getNumber()-1 == rungs.size()){
				rungs.add(new Rung(1, 0));
			}
			return true;
		}else{ 
			for(Rung rung:rungs){
				if(rung.contain(instruction)){
					if(rung.remove(instruction)){
						instruction.clearLink();
						instruction.setPosition(0, 0);
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean delete(LadderInstruction instruction){
		if(remove(instruction)){
			if(instruction instanceof TimerInstruction || instruction instanceof CountInstruction){
				deleteMemory(instruction);
			}
			return true;
		}
		return false;
	}

	private void deleteMemory(LadderInstruction instruction){
		if(instruction instanceof TimerInstruction){
			TimerInstruction timer = (TimerInstruction) instruction;
			unlink(timer.getMemory().getName());
			unlink(timer.getPresetMemory().getName());
			unlink(timer.getAccumMemory().getName());
			unlink(timer.getDoneMemory().getName());
			unlink(timer.getEnableMemory().getName());
			unlink(timer.getTimeBaseMemory().getName());
			unlink(timer.getTimeMemory().getName());
		}else if(instruction instanceof CountInstruction){
			CountInstruction count = (CountInstruction) instruction;
			unlink(count.getMemory().getName());
			unlink(count.getPresetMemory().getName());
			unlink(count.getAccumMemory().getName());
			unlink(count.getDoneMemory().getName());
			unlink(count.getCountMemory().getName());
		}else{
			unlink(instruction.getMemory().getName());
		}
	}

	private void unlink(String name){
		for(Rung rung:rungs){
			for(LadderInstruction instruction:rung){
				instruction.resetMemory(name);
			}
		}
	}
	
	public Rung getRungOfInstruction(LadderInstruction instruction){
		for(Rung rung:rungs){
			if(rung.contain(instruction)){
				return rung;
			}
		}
		return null;
	}
	
	@Override
	public Iterator<Rung> iterator() {
		return new Iterator<Rung>() {

			private int index = 0;

			@Override
			public boolean hasNext() {
				return index < rungs.size();
			}

			@Override
			public Rung next() {
				return rungs.get(index++);
			}

			@Override
			public void remove() {
				rungs.remove(index++);

			}
		};
	}
	
	public List<Rung> getList(){
		return rungs;
	}

}
