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

import com.github.leofds.iotladdereditor.compiler.domain.CodeOptions;
import com.github.leofds.iotladdereditor.compiler.generator.CodeGenerator;
import com.github.leofds.iotladdereditor.compiler.generator.code.Esp32ArduinoCodeGenerator;

public class CodeGeneratorFactory {

	public static CodeGenerator create(CodeOptions option) {
		switch(option) {
		case ESP32_ARDUINO_FREERTOS:
			return new Esp32ArduinoCodeGenerator();
		default:
			return null;
		}
	}
	
}
