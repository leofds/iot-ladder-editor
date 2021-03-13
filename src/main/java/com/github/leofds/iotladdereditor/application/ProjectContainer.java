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
package com.github.leofds.iotladdereditor.application;

import com.github.leofds.iotladdereditor.compiler.SourceCode;
import com.github.leofds.iotladdereditor.compiler.domain.IR;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;

public class ProjectContainer {

	private LadderProgram ladderProgram;
	private String name;
	private IR ir;
	private SourceCode sourceCode;
	private boolean saved;
	private boolean changed;
	private boolean compiled;
	private String absoletePath;

	public ProjectContainer() {
		ladderProgram = new LadderProgram();
		saved = false;
		changed = false;
		compiled = false;
	}

	public LadderProgram getLadderProgram() {
		return ladderProgram;
	}

	public void setLadderProgram(LadderProgram ladderProgram) {
		this.ladderProgram = ladderProgram;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public IR getIr() {
		return ir;
	}

	public void setIr(IR ir) {
		this.ir = ir;
	}

	public void setCompiled(boolean compiled) {
		this.compiled = compiled;
	}

	public boolean isCompiled() {
		return compiled;
	}

	public SourceCode getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(SourceCode sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getAbsoletePath() {
		return absoletePath;
	}

	public void setAbsoletePath(String absoletePath) {
		this.absoletePath = absoletePath;
	}
}
