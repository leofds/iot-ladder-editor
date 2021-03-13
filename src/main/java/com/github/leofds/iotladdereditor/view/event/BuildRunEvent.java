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
package com.github.leofds.iotladdereditor.view.event;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.compiler.Compiler;
import com.github.leofds.iotladdereditor.view.event.Subject.SubMsg;

public class BuildRunEvent implements Observer {

	private Subject subject;

	public BuildRunEvent(Subject subject) {
		subject.addObserver(this);
		this.subject = subject;
	}

	private void build() {
		Mediator me = Mediator.getInstance();
		me.clearConsole();
		Compiler.build(me.getProject());
	}
	
	private void buildRun() {
		Mediator me = Mediator.getInstance();
		build();
		try {
			switch(me.getProject().getLadderProgram().getProperties().getCodeOption()) {
			case ESP32_ARDUINO_FREERTOS:
				Desktop.getDesktop().open(new File("out/plc/plc.ino"));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			me.outputConsoleMessage(e.getMessage());
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(subject instanceof Subject && arg instanceof SubMsg){
			switch((SubMsg) arg) {
			case BUILD:
				build();
				break;
			case BUILD_RUN:
				buildRun();
				break;
			}
		}
	}

}
