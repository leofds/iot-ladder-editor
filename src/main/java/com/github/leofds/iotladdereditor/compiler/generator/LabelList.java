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
package com.github.leofds.iotladdereditor.compiler.generator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LabelList implements Iterable<String>{

	private List<String> labels = new ArrayList<String>();

	public void add(String label){
		labels.add(label);
	}
	
	public void remove(String label){
		for(int i=0;i<labels.size();i++){
			if(labels.get(i).equals(label)){
				labels.remove(i);
			}
		}
	}
	
	public void removeLast(){
		if(labels.size()>0){
			labels.remove(labels.size()-1);
		}
	}
	
	public int count(String label){
		int count = 0;
		for(String l:labels){
			if(l.equals(label)){
				count++;
			}
		}
		return count;
	}
	
	public int size(){
		return labels.size();
	}
	
	@Override
	public Iterator<String> iterator() {
		return labels.iterator();
	}
}
