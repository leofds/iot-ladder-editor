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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.maven.model.Model;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.util.AboutUils;

public class About extends JDialog{

	private static final long serialVersionUID = 1L;

	public About() {
		setTitle(Strings.about());

		JPanel panel = new JPanel(null);
		panel.setPreferredSize(new Dimension(600, 450));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabel icon = new JLabel(new ImageIcon(About.class.getResource("/images/PrototypeLadder_48x48.png")));
		icon.setBounds(10, 10, 100, 100);
		panel.add(icon);

		JLabel version = new JLabel();
		version.setHorizontalAlignment(SwingConstants.LEFT);
		version.setBounds(85, 68, 185, 20);
		version.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel.add(version);

		JLabel appName = new JLabel();
		appName.setBounds(85, 45, 250, 30);
		appName.setFont(new Font("Dialog", Font.PLAIN, 20));
		appName.setText(Strings.appName());
		panel.add(appName);

		setContentPane(panel);

		JTextPane textPaneLicense = new JTextPane();
		textPaneLicense.setEditable(false);
		textPaneLicense.setBackground(new Color(240, 240, 240));
		textPaneLicense.setFont(new Font("Dialog", Font.PLAIN, 14));
		textPaneLicense.setBounds(46, 98, 540, 276);
		panel.add(textPaneLicense);

		try {
			Document doc = textPaneLicense.getDocument();
			Model model = AboutUils.getProjectModel();
			if(model != null) {
				version.setText("Version "+model.getVersion());
	
				JLabel lblNewLabel = new JLabel("");
				lblNewLabel.setForeground(Color.BLUE);
				lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
				lblNewLabel.setBounds(46, 384, 540, 23);
				lblNewLabel.setText("<html><a href=\"\">"+model.getUrl()+"</a></html>");
				lblNewLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				lblNewLabel.addMouseListener(new MouseListener() {
	
					@Override
					public void mouseReleased(MouseEvent e) {
					}
	
					@Override
					public void mousePressed(MouseEvent e) {
					}
	
					@Override
					public void mouseExited(MouseEvent e) {
					}
	
					@Override
					public void mouseEntered(MouseEvent e) {
					}
	
					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							Desktop.getDesktop().browse(new URI(model.getUrl()));
						} catch (IOException | URISyntaxException e1) {
							e1.printStackTrace();
						}
					}
				});
				panel.add(lblNewLabel);
			}

			String license = "Copyright (C) 2021  Leonardo Fernandes\n"
					+ "\n"
					+ "This program is free software: you can redistribute it and/or modify\n"
					+ "it under the terms of the GNU General Public License as published by\n"
					+ "the Free Software Foundation, either version 3 of the License, or\n"
					+ "(at your option) any later version.\n"
					+ "\n"
					+ "This program is distributed in the hope that it will be useful,\n"
					+ "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
					+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
					+ "GNU General Public License for more details.\n"
					+ "\n"
					+ "You should have received a copy of the GNU General Public License\n"
					+ "along with this program.  If not, see <https://www.gnu.org/licenses/>.";
			doc.insertString(doc.getLength(), license, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
