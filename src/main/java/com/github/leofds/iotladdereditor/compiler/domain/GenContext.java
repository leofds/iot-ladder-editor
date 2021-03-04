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

public class GenContext {

	private int labelCount = 0;
	private int statuscount = 0;
	private int tempCount = 0;
	private String scope;
	private SymbolTable symbolTable;
	public static String GLOBAL_SCOPE = "global";
	
	public GenContext(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	public String genTemp(){
		tempCount++;
		return "_T"+tempCount;
	}

	public String genLabel(){
		labelCount++;
		return "L"+labelCount;
	}

	public String getCurrentLabel(){
		return "L"+labelCount;
	}

	public String getCurrentStatus(){
		return "_S"+statuscount;
	}

	public String incStatus(){
		statuscount++;
		return "_S"+statuscount;
	}

	public String decStatus(){
		if(statuscount != 0){
			statuscount--;
			return "_S"+statuscount;
		}
		return null;
	}

	public void clearStatus(){
		statuscount = 0;
	}
	
	public void setScope(String scope){
		this.scope = scope;
	}

	public String getScope() {
		return scope;
	}

	public SymbolTable getSymbolTable() {
		return symbolTable;
	}
}
