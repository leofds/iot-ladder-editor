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
package com.github.leofds.iotladdereditor.compiler;

public class SourceFile {

	private String fname;
	private String content = "";
	
	public SourceFile(String fname) {
		this.fname = fname;
	}

	public String getFname() {
		return fname;
	}

	public String getContent() {
		return content;
	}
	
	public void addContent(String content){
		this.content += content;
	}
	
	@Override
	public String toString() {
		return content;
	}
}
