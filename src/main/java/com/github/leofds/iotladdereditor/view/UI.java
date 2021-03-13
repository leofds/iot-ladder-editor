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
package com.github.leofds.iotladdereditor.view;

import java.awt.Image;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.util.FileUtils;
import com.github.leofds.iotladdereditor.view.dnd.GhostGlassPane;

public class UI extends JFrame implements WindowListener{

	private static final long serialVersionUID = 1L;

	public void close(){
		dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	}

	public UI() {
		setTitle(Strings.appName());

		List<Image> images = new ArrayList<Image>();
		images.add(new ImageIcon(UI.class.getResource("/images/PrototypeLadder_16x16.png")).getImage());
		images.add(new ImageIcon(UI.class.getResource("/images/PrototypeLadder_24x24.png")).getImage());
		images.add(new ImageIcon(UI.class.getResource("/images/PrototypeLadder_32x32.png")).getImage());
		images.add(new ImageIcon(UI.class.getResource("/images/PrototypeLadder_48x48.png")).getImage());
		images.add(new ImageIcon(UI.class.getResource("/images/PrototypeLadder_64x64.png")).getImage());
		setIconImages(images);
		setGlassPane(new GhostGlassPane());
		addWindowListener(this);
		setFocusable(false);

		JComponent contentPane = new ContentPanel(); 
		contentPane.setOpaque(true);
		setContentPane(contentPane);
		setJMenuBar(new Menu());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		//		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		Mediator.getInstance().setUi(this);
		
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		FileUtils.confirmSave();
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
