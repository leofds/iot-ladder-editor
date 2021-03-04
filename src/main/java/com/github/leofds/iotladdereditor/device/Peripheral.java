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
package com.github.leofds.iotladdereditor.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Peripheral implements Serializable{

	private static final long serialVersionUID = 7643582228887821255L;
	private String name;
	private String symbol;
	private List<PeripheralIO> peripheralItems;
	
	public Peripheral(String name, String symbol) {
		this.peripheralItems = new ArrayList<PeripheralIO>();
		this.name = name;
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void addPeripheralItem(PeripheralIO peripheralItem){
		peripheralItems.add(peripheralItem);
	}
	
	public List<PeripheralIO> getPeripheralItems() {
		return peripheralItems;
	}

	@Override
	public String toString() {
		return name;
	}
}
