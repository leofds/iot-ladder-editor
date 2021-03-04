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
package com.github.leofds.iotladdereditor.compiler.generator.factory;

import com.github.leofds.iotladdereditor.compiler.domain.Operator;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.domain.Symbol;

public class QuadrupleFactory {

	public static Quadruple createReturn(){
		return new Quadruple(Operator.RETURN, null , null, null);
	}
	
	public static Quadruple createLabel(Symbol label){
		return new Quadruple(Operator.LABEL, null , null, label);
	}
	
	public static Quadruple createGoto(Symbol label){
		return new Quadruple(Operator.GOTO, null , null, label);
	}
	
	public static Quadruple createComment(Symbol comment){
		return new Quadruple(Operator.COMMENT, comment , null, null);
	}
	
	public static Quadruple createAssignment(Symbol src,Symbol dst){
		return new Quadruple(Operator.ASSIGNMENT, src , null, dst);
	}
	
	public static Quadruple createIf(Symbol condition,Symbol dst){
		return new Quadruple(Operator.IF, condition , null, dst);
	}
	
	public static Quadruple createIfFalse(Symbol condition,Symbol dst){
		return new Quadruple(Operator.IFFALSE, condition , null, dst);
	}
	
	public static Quadruple createIfEq(Symbol srcA,Symbol srcB,Symbol dst){
		return new Quadruple(Operator.IFEQ, srcA , srcB, dst);
	}
	
	public static Quadruple createIfGEq(Symbol srcA,Symbol srcB,Symbol dst){
		return new Quadruple(Operator.IFGEQ, srcA , srcB, dst);
	}
	
	public static Quadruple createIfG(Symbol srcA,Symbol srcB,Symbol dst){
		return new Quadruple(Operator.IFG, srcA , srcB, dst);
	}
	
	public static Quadruple createIfLE(Symbol srcA,Symbol srcB,Symbol dst){
		return new Quadruple(Operator.IFLE, srcA , srcB, dst);
	}
	
	public static Quadruple createIfL(Symbol srcA,Symbol srcB,Symbol dst){
		return new Quadruple(Operator.IFL, srcA , srcB, dst);
	}
	
	public static Quadruple createNEQ(Symbol srcA,Symbol srcB,Symbol dst){
		return new Quadruple(Operator.IFNEQ, srcA , srcB, dst);
	}
	
	public static Quadruple createCall(Symbol label,Symbol args){
		return new Quadruple(Operator.CALL, label, args, null);
	}
	
	public static Quadruple createNot(Symbol dst,Symbol src){
		return new Quadruple(Operator.NOT, src, null, dst);
	}
	
	public static Quadruple createAdd(Symbol srcA, Symbol srcB,Symbol dst){
		return new Quadruple(Operator.ADD, srcA, srcB, dst);
	}
	
	public static Quadruple createSub(Symbol srcA, Symbol srcB,Symbol dst){
		return new Quadruple(Operator.SUB, srcA, srcB, dst);
	}
	
	public static Quadruple createDiv(Symbol srcA, Symbol srcB,Symbol dst){
		return new Quadruple(Operator.DIV, srcA, srcB, dst);
	}
	
	public static Quadruple createMul(Symbol srcA, Symbol srcB,Symbol dst){
		return new Quadruple(Operator.MUL, srcA, srcB, dst);
	}
	
	public static Quadruple createAnd(Symbol srcA, Symbol srcB,Symbol dst){
		return new Quadruple(Operator.AND, srcA, srcB, dst);
	}
	
	public static Quadruple createOr(Symbol srcA, Symbol srcB,Symbol dst){
		return new Quadruple(Operator.OR, srcA, srcB, dst);
	}
	
	public static Quadruple createXor(Symbol srcA, Symbol srcB,Symbol dst){
		return new Quadruple(Operator.XOR, srcA, srcB, dst);
	}
	
	public static Quadruple createCall(Symbol label,Symbol args,Symbol ret){
		return new Quadruple(Operator.CALL, label, args, ret);
	}
	
	public static Quadruple createParam(Symbol param) {
		return new Quadruple(Operator.PARAM, param, null, null);
	}
}
