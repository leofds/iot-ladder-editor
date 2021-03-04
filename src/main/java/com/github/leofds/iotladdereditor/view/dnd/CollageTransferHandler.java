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
package com.github.leofds.iotladdereditor.view.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.device.PeripheralIO;
import com.github.leofds.iotladdereditor.ladder.symbol.RungView;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;

public class CollageTransferHandler implements Transferable{

	private LadderInstruction instruction;
	private RungView rung;
	private DeviceMemory memory;
	
	public static DataFlavor COLLAGE_INSTRUCTION_FLAVOR;
	public static DataFlavor COLLAGE_RUNG_FLAVOR;
	public static DataFlavor COLLAGE_MEMORY_FLAVOR;
	
	static {
		COLLAGE_INSTRUCTION_FLAVOR = new DataFlavor(LadderInstruction.class,"LadderInstruction");
		COLLAGE_RUNG_FLAVOR = new DataFlavor(RungView.class,"Rung");
		COLLAGE_MEMORY_FLAVOR = new DataFlavor(PeripheralIO.class,"PeripheralItem");
	}
	
	public CollageTransferHandler(LadderInstruction instruction) {
		this.instruction = instruction;
	}
	
	public CollageTransferHandler(RungView rung) {
		this.rung = rung;
	}
	
	public CollageTransferHandler(DeviceMemory memory) {
		this.memory = memory;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { COLLAGE_INSTRUCTION_FLAVOR, COLLAGE_RUNG_FLAVOR, COLLAGE_MEMORY_FLAVOR };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if(flavor.isMimeTypeEqual(COLLAGE_INSTRUCTION_FLAVOR.getMimeType())){
			return true;
		}
		if(flavor.isMimeTypeEqual(COLLAGE_RUNG_FLAVOR.getMimeType())){
			return true;
		}
		if(flavor.isMimeTypeEqual(COLLAGE_MEMORY_FLAVOR.getMimeType())){
			return true;
		}
		return false;
	};

	@Override
	public Object getTransferData(DataFlavor flavor)throws UnsupportedFlavorException, IOException {
		if(flavor.equals(COLLAGE_INSTRUCTION_FLAVOR)){
			return instruction;
		}
		if(flavor.equals(COLLAGE_RUNG_FLAVOR)){
			return rung;
		}
		if(flavor.equals(COLLAGE_MEMORY_FLAVOR)){
			return memory;
		}
		return new UnsupportedFlavorException(flavor);
	}

}
