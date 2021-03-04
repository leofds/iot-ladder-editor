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

public class Quadruple {

	private Operator operator;
	private Symbol argument1;
	private Symbol argument2;
	private Symbol result;
	
	public Quadruple(Operator operator, Symbol argument1, Symbol argument2, Symbol result) {
		this.operator = operator;
		this.argument1 = argument1;
		this.argument2 = argument2;
		this.result = result;
	}
	
	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Symbol getArgument1() {
		return argument1;
	}
	
	public void setArgument1(Symbol argument1) {
		this.argument1 = argument1;
	}
	
	public Symbol getArgument2() {
		return argument2;
	}
	
	public void setArgument2(Symbol argument2) {
		this.argument2 = argument2;
	}
	
	public Symbol getResult() {
		return result;
	}
	
	public void setResult(Symbol result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "Quadruple [operator=" + operator + ", argument1=" + argument1
				+ ", argument2=" + argument2 + ", result=" + result + "]";
	}
}
