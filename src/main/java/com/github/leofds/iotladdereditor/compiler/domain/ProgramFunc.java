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
package com.github.leofds.iotladdereditor.compiler.domain;

public enum ProgramFunc {

	INIT("init"),
	INIT_CONTEXT("initContext"),
	INPUT("readInputs"),
	OUTPUT("writeOutputs"),
	UPDATE("refreshTime64bit"),
	GETTIME("getTime"),
	SCANTIME("scanTime"),
	RUNG("rung"),
	TIMEOUT("isTimeout"),
	PUBLISH("publish");

	public String value;
	
	private ProgramFunc(String value){
		this.value = value;
	}
}
