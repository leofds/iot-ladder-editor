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

import java.util.ArrayList;
import java.util.List;

public class SourceCode {

	private List<SourceFile> files = new ArrayList<SourceFile>();
	private SourceFile file;
	
	public void createNewFile(String fname){
		file = new SourceFile(fname);
		files.add(file);
	}
	
	public void add(String code){
		file.addContent(code);
	}
	
	public void addl(String code){
		file.addContent(code + "\r\n");
	}
	
	public void newLine(){
		file.addContent("\r\n");
	}
	
	public List<SourceFile> getFiles(){
		return files;
	}
}
