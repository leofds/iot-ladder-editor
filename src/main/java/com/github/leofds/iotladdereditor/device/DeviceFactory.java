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

public class DeviceFactory {

	public static Device createEsp32(){
		Device device  = new Device("ESP32");
		Peripheral output = new Peripheral("Output", "Q");
		Peripheral input = new Peripheral("Input", "I");

		PeripheralIO pc1out = new PeripheralIO("Q1", Boolean.class, "2", "PIN_Q01", IO.OUTPUT);
		PeripheralIO pc2out = new PeripheralIO("Q2", Boolean.class, "4", "PIN_Q02", IO.OUTPUT);
		PeripheralIO pc3out = new PeripheralIO("Q3", Boolean.class, "12", "PIN_Q03", IO.OUTPUT);
		PeripheralIO pc4out = new PeripheralIO("Q4", Boolean.class, "13", "PIN_Q04", IO.OUTPUT);
		PeripheralIO pc5out = new PeripheralIO("Q5", Boolean.class, "33", "PIN_Q05", IO.OUTPUT);
		PeripheralIO pc6out = new PeripheralIO("Q6", Boolean.class, "25", "PIN_Q06", IO.OUTPUT);
		PeripheralIO pc7out = new PeripheralIO("Q7", Boolean.class, "26", "PIN_Q07", IO.OUTPUT);
		PeripheralIO pc8out = new PeripheralIO("Q8", Boolean.class, "27", "PIN_Q08", IO.OUTPUT);

		PeripheralIO pc1in = new PeripheralIO("I1", Boolean.class, "14", "PIN_I01", IO.INPUT);
		PeripheralIO pc2in = new PeripheralIO("I2", Boolean.class, "16", "PIN_I02", IO.INPUT);
		PeripheralIO pc3in = new PeripheralIO("I3", Boolean.class, "17", "PIN_I03", IO.INPUT);
		PeripheralIO pc4in = new PeripheralIO("I4", Boolean.class, "18", "PIN_I04", IO.INPUT);
		PeripheralIO pc5in = new PeripheralIO("I5", Boolean.class, "19", "PIN_I05", IO.INPUT);
		PeripheralIO pc6in = new PeripheralIO("I6", Boolean.class, "21", "PIN_I06", IO.INPUT);
		PeripheralIO pc7in = new PeripheralIO("I7", Boolean.class, "22", "PIN_I07", IO.INPUT);
		PeripheralIO pc8in = new PeripheralIO("I8", Boolean.class, "23", "PIN_I08", IO.INPUT);

		output.addPeripheralItem(pc1out);
		output.addPeripheralItem(pc2out);
		output.addPeripheralItem(pc3out);
		output.addPeripheralItem(pc4out);
		output.addPeripheralItem(pc5out);
		output.addPeripheralItem(pc6out);
		output.addPeripheralItem(pc7out);
		output.addPeripheralItem(pc8out);
		input.addPeripheralItem(pc1in);
		input.addPeripheralItem(pc2in);
		input.addPeripheralItem(pc3in);
		input.addPeripheralItem(pc4in);
		input.addPeripheralItem(pc5in);
		input.addPeripheralItem(pc6in);
		input.addPeripheralItem(pc7in);
		input.addPeripheralItem(pc8in);

		device.addPeripheral(output);
		device.addPeripheral(input);
		return device;
	}
}
