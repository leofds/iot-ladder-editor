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

import java.util.Observable;
import java.util.Observer;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.compiler.Compiler;
import com.github.leofds.iotladdereditor.view.event.Subject.SubMsg;

public class BuildEvent implements Observer {

	private Subject subject;

	public BuildEvent(Subject subject) {
		subject.addObserver(this);
		this.subject = subject;
	}

	private void build() {
		Mediator me = Mediator.getInstance();
		me.clearConsole();
		Compiler.build(me.getProject());
	}

	@Override
	public void update(Observable o, Object arg) {
		if(subject instanceof Subject && arg instanceof SubMsg){
			if(((SubMsg) arg) == SubMsg.BUILD) {
				build();
			}
		}
	}

}
