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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SymbolTable implements Iterable<Symbol>{

	public static String GLOBAL = "global";
	
	private List<Symbol> symbols = new ArrayList<Symbol>();
	
	public Symbol add(Symbol symbol){
		if(!symbols.contains(symbol)){
			symbols.add(symbol);
		}
		return symbol;
	}
	
	public List<Symbol> getSymbols() {
		return symbols;
	}

	public Symbol add(String name,Kind kind,Class<?> type,String scope){
		return add( new Symbol(name, kind, type, scope));
	}
	
	public Symbol addLabel(String label,String scope){
		return add( new Symbol(label, Kind.LABEL, null, scope));
	}
	
	public Symbol addFunc(String label,Class<?> type){
		return add( new Symbol(label, Kind.FUNCTION, type, GLOBAL));
	}
	
	public Symbol addIntConst(String value){
		 return add( new Symbol(value, Kind.CONSTANT, Integer.class, null));
	}
	
	public Symbol addFloatConst(String value){
		 return add( new Symbol(value, Kind.CONSTANT, Float.class, null));
	}
	
	public Symbol addLongConst(String value){
		 return add( new Symbol(value, Kind.CONSTANT, Long.class, null));
	}
	
	public Symbol addLiteral(String literal){
		return add( new Symbol(literal, Kind.CONSTANT, String.class, null) );
	}
	
	public Symbol addBoolVar(String variable,String scope){
		 return add( new Symbol(variable, Kind.VARIABLE, Boolean.class, scope));
	}
	
	public Symbol addIntVar(String variable,String scope){
		 return add( new Symbol(variable, Kind.VARIABLE, Integer.class, scope));
	}
	
	public Symbol addFloatVar(String variable,String scope){
		 return add( new Symbol(variable, Kind.VARIABLE, Float.class, scope));
	}
	
	public Symbol addLongVar(String variable,String scope){
		 return add( new Symbol(variable, Kind.VARIABLE, Long.class, scope));
	}

	@Override
	public Iterator<Symbol> iterator() {
		return symbols.iterator();
	}
}
