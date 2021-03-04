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

public class PeripheralIO extends DeviceMemory implements Serializable{

	private static final long serialVersionUID = 3470318238269022501L;
	private String pin;
	private String path;
	private IO io;
	
	public PeripheralIO(String name, Class<?> type, String pin, IO io) {
		super(name, type);
		this.pin = pin;
		this.path = null;
		this.io = io;
	}
	
	public PeripheralIO(String name, Class<?> type, String id, String path, IO io) {
		super(name, type);
		this.pin = id;
		this.path = path;
		this.io = io;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public IO getIo() {
		return io;
	}

	public void setIo(IO io) {
		this.io = io;
	}
}
