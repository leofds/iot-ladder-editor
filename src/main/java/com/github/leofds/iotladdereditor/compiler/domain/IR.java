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

import java.io.StringWriter;
import java.util.List;

public class IR {

	private List<Quadruple> quadruples;
	private SymbolTable symbolTable;
	
	public IR(List<Quadruple> quadruples, SymbolTable symbolTable) {
		this.quadruples = quadruples;
		this.symbolTable = symbolTable;
	}

	public List<Quadruple> getQuadruples() {
		return quadruples;
	}
	
	public void setQuadruples(List<Quadruple> quadruples) {
		this.quadruples = quadruples;
	}
	
	public SymbolTable getSymbolTable() {
		return symbolTable;
	}
	
	public void setSymbolTable(SymbolTable symbolTable) {
		this.symbolTable = symbolTable;
	}

	public String getOutputSymbolTable(){
		StringWriter out = new StringWriter();
		for(Symbol symbol:symbolTable){
			out.append(symbol.toString()+"\r\n");
		}
		return out.getBuffer().toString();
	}
	
	public String getOutputQuadruples(){
		StringWriter out = new StringWriter();
		for(Quadruple quadruple:quadruples){
			out.append(quadruple.toString()+"\r\n");
		}
		return out.getBuffer().toString();
	}

	public String getTextQuadruple(){
		StringWriter out = new StringWriter(); 
		for(Quadruple quad:quadruples){
			if(quad.getOperator() != null){
				switch (quad.getOperator()) {
				case LABEL:
					out.append(String.format("%s:\r\n",quad.getResult().getName()));
					break;
				case GOTO:
					out.append(String.format("  goto %s\r\n",quad.getResult().getName()));
					break;
				case PARAM:
					out.append(String.format("  push %s\r\n",quad.getArgument1().getName()));
					break;
				case CALL:
					if(quad.getResult() != null){
						out.append(String.format("  %s = call %s,%s\r\n",quad.getResult().getName(),quad.getArgument1().getName(),quad.getArgument2().getName()));
					}else{
						out.append(String.format("  call %s,%s\r\n",quad.getArgument1().getName(),quad.getArgument2().getName()));
					}
					break;
				case RETURN:
					if(quad.getArgument1() != null){
						out.append(String.format("  return %s\r\n",quad.getArgument1().getName()));
					}else{
						out.append(String.format("  return\r\n"));
					}
					break;
				case ASSIGNMENT:
					out.append(String.format("  %s = %s\r\n",quad.getResult().getName(),quad.getArgument1().getName()));
					break;
				case IF:
					out.append(String.format("  if %s goto %s\r\n",quad.getArgument1().getName(),quad.getResult().getName()));
					break;
				case IFFALSE:
					out.append(String.format("  ifFalse %s goto %s\r\n",quad.getArgument1().getName(),quad.getResult().getName()));
					break;
				case IFEQ:
					out.append(String.format("  if %s == %s goto %s\r\n",quad.getArgument1().getName(),quad.getArgument2().getName(),quad.getResult().getName()));
					break;
				case IFG:
					out.append(String.format("  if %s > %s goto %s\r\n",quad.getArgument1().getName(),quad.getArgument2().getName(),quad.getResult().getName()));
					break;
				case IFGEQ:
					out.append(String.format("  if %s >= %s goto %s\r\n",quad.getArgument1().getName(),quad.getArgument2().getName(),quad.getResult().getName()));
					break;
				case IFL:
					out.append(String.format("  if %s < %s goto %s\r\n",quad.getArgument1().getName(),quad.getArgument2().getName(),quad.getResult().getName()));
					break;
				case IFLE:
					out.append(String.format("  if %s <= %s goto %s\r\n",quad.getArgument1().getName(),quad.getArgument2().getName(),quad.getResult().getName()));
					break;
				case IFNEQ:
					out.append(String.format("  if %s != %s goto %s\r\n",quad.getArgument1().getName(),quad.getArgument2().getName(),quad.getResult().getName()));
					break;
				case NOT:
					out.append(String.format("  %s = not %s\r\n",quad.getResult().getName(),quad.getArgument1().getName()));
					break;
				case COMMENT:
					out.append(String.format("\r\n  ;****** %s ******\r\n",quad.getArgument1().getName()));
					break;
				case ADD:
					out.append(String.format("  %s = %s + %s\r\n",quad.getResult().getName(),quad.getArgument1().getName(),quad.getArgument2().getName()));
					break;
				case DIV:
					out.append(String.format("  %s = %s / %s\r\n",quad.getResult().getName(),quad.getArgument1().getName(),quad.getArgument2().getName()));
					break;
				case MUL:
					out.append(String.format("  %s = %s * %s\r\n",quad.getResult().getName(),quad.getArgument1().getName(),quad.getArgument2().getName()));
					break;
				case SUB:
					out.append(String.format("  %s = %s - %s\r\n",quad.getResult().getName(),quad.getArgument1().getName(),quad.getArgument2().getName()));
					break;
				case AND:
					out.append(String.format("  %s = %s & %s\r\n",quad.getResult().getName(),quad.getArgument1().getName(),quad.getArgument2().getName()));
					break;
				case OR:
					out.append(String.format("  %s = %s | %s\r\n",quad.getResult().getName(),quad.getArgument1().getName(),quad.getArgument2().getName()));
					break;
				case XOR:
					out.append(String.format("  %s = %s ^ %s\r\n",quad.getResult().getName(),quad.getArgument1().getName(),quad.getArgument2().getName()));
					break;
				default:
					break;
				}
			}else{
				out.append("\r\n");
			}
		}
		return out.getBuffer().toString();
	}
	
	@Override
	public String toString() {
		return getTextQuadruple();
	} 
	
}
