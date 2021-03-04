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
package com.github.leofds.iotladdereditor;

import java.awt.EventQueue;
import java.util.Locale;

import javax.swing.UIManager;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.application.Preferences;
import com.github.leofds.iotladdereditor.application.ProjectContainer;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.util.FileUtils;
import com.github.leofds.iotladdereditor.view.UI;

public class IotLadderEditor{

	public static void Initialization(){
		Mediator me = Mediator.getInstance();
		me.setProject(new ProjectContainer());	// Create a blank project
		me.setFileOp(new FileUtils());
	}

	private static void loadLanguage() {
		
		try {
			String lang = (String) Preferences.get( Preferences.LANG );
			if( lang != null && !lang.isEmpty()) {
				String[] l = lang.split("_");
				Locale locale = new Locale( l[0], l[1] );
				Strings.changeLocale( locale );
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		loadLanguage();
		Initialization();
		
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			EventQueue.invokeLater(new Runnable() {

				@Override
				public void run() {
					new UI().setVisible(true);
				}
			});
		}catch (Exception e){
		}
	}
}
