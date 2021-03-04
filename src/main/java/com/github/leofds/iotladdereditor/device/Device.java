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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Device implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private List<Peripheral> peripherals;
	
	public Device(String name) {
		this.name = name;
		this.peripherals = new ArrayList<Peripheral>();
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void addPeripheral(Peripheral peripheral){
		peripherals.add(peripheral);
	}
	
	public List<Peripheral> getPeripherals() {
		return peripherals;
	}
	
	public Peripheral getPeripheralBySymbol(String symbol) {
		for(Peripheral peripheral: peripherals) {
			if(peripheral.getSymbol().equals(symbol)) {
				return peripheral;
			}
		}
		return null;
	}
	
	public void sort() {
		for(Peripheral peripheral: peripherals) {
			Collections.sort(peripheral.getPeripheralItems());
			
		}
	}

	@Override
	public String toString() {
		return name;
	}
	
	public Device clone() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Device) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
	}
	
	public PeripheralIO getPeripheralIOByName(String name) {
		for(Peripheral peripheral: peripherals) {
			for( PeripheralIO peripheralIO : peripheral.getPeripheralItems()) {
				if(peripheralIO.getName().equalsIgnoreCase(name)) {
					return peripheralIO;
				}
			}
		}
		return null;
	}
	
	public boolean removePeripheralIOByName(String name) {
		for(Peripheral peripheral: peripherals) {
			Iterator<PeripheralIO> it = peripheral.getPeripheralItems().iterator();
			while(it.hasNext()) {
				if(it.next().getName().equalsIgnoreCase(name)) {
					it.remove();
					return true;
				}
			}
		}
		return false;
	}
	
	
}
