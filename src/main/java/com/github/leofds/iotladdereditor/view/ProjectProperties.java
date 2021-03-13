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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.lang3.RandomStringUtils;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.compiler.domain.CodeOptions;
import com.github.leofds.iotladdereditor.device.Device;
import com.github.leofds.iotladdereditor.device.IO;
import com.github.leofds.iotladdereditor.device.Peripheral;
import com.github.leofds.iotladdereditor.device.PeripheralIO;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;
import com.github.leofds.iotladdereditor.ladder.ProgramProperties;
import com.github.leofds.iotladdereditor.util.FileUtils;

public class ProjectProperties extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldSsid;
	private JTextField textFieldPassword;
	private JTextField textFieldCAFile;
	private JTextField textFieldClientCertFile;
	private JTextField textFieldClientPkFile;
	private JTextField textFieldBrokerAddress;
	private JTextField textFieldPubTopic;
	private JTextField textFieldSubTopic;
	private JTextField textFieldBokerPort;
	private JTextField textFieldClientID;
	private JButton btnChooseCaFile;
	private JButton btnChooseClientCert;
	private JButton btnChooseClientPk;
	private JCheckBox checkBoxUseClientCertificate;
	private JCheckBox checkBoxEnableSsl;
	private JTextField textFieldUserUsername;
	private JTextField textFieldUserPassword;
	private JTextField textFieldTelemetrySeconds;
	private JCheckBox checkBoxEnableTelemetry;
	private JCheckBox checkBoxTelemetryMemory;
	private JCheckBox checkBoxTelemetryOutput;
	private JCheckBox checkBoxTelemetryInput;
	private LadderProgram ladderProgram;
	private JComboBox<CodeOptions> comboBox_code;
	private JTable tablePinMapping;
	private Device device;
	
	

	/**
	 * Create the dialog.
	 */
	public ProjectProperties() {

		ladderProgram = Mediator.getInstance().getProject().getLadderProgram();
		device = ladderProgram.getDevice().clone();

		
		setBounds(100, 100, 820, 508);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setPreferredSize(new Dimension(800, 460));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		contentPanel.setLayout(null);


		JButton btnSave = new JButton(Strings.save());
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		btnSave.setBounds(571, 425, 110, 25);
		contentPanel.add(btnSave);

		JButton btnCancel = new JButton(Strings.cancel());
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectProperties.this.dispose();
			}
		});
		btnCancel.setBounds(693, 425, 97, 25);
		contentPanel.add(btnCancel);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 112, 780, 303);
		contentPanel.add(tabbedPane);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab(Strings.mqtt(), null, panel_2, null);
		panel_2.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel(Strings.brokerAddr());
		lblNewLabel_1.setBounds(12, 13, 131, 16);
		panel_2.add(lblNewLabel_1);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);

		textFieldBrokerAddress = new JTextField();
		textFieldBrokerAddress.setBounds(151, 10, 580, 22);
		panel_2.add(textFieldBrokerAddress);
		textFieldBrokerAddress.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel(Strings.brokerPort());
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_5.setBounds(12, 45, 131, 16);
		panel_2.add(lblNewLabel_5);
		
		textFieldBokerPort = new JTextField();
		textFieldBokerPort.setText("0");
		textFieldBokerPort.setBounds(150, 42, 56, 22);
		panel_2.add(textFieldBokerPort);
		textFieldBokerPort.setColumns(10);
		textFieldBokerPort.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
					if(c < '0' || c > '9' || Integer.parseInt(textFieldBokerPort.getText()+""+c) >= 65536) {
						e.consume();
					}
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
		textFieldClientID = new JTextField();
		textFieldClientID.setBounds(151, 74, 478, 22);
		panel_2.add(textFieldClientID);
		textFieldClientID.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel(Strings.clientId());
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_6.setBounds(12, 77, 131, 13);
		panel_2.add(lblNewLabel_6);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(34, 106, 697, 155);
		panel_2.add(tabbedPane_1);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(Color.WHITE);
		tabbedPane_1.addTab(Strings.user(), null, panel_4, null);
		panel_4.setLayout(null);
		
		JLabel lblNewLabel_7 = new JLabel(Strings.username());
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_7.setBounds(10, 42, 115, 13);
		panel_4.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel(Strings.password());
		lblNewLabel_8.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_8.setBounds(10, 70, 115, 13);
		panel_4.add(lblNewLabel_8);
		
		textFieldUserUsername = new JTextField();
		textFieldUserUsername.setBounds(135, 39, 140, 19);
		panel_4.add(textFieldUserUsername);
		textFieldUserUsername.setColumns(10);
		
		textFieldUserPassword = new JTextField();
		textFieldUserPassword.setBounds(135, 67, 140, 19);
		panel_4.add(textFieldUserPassword);
		textFieldUserPassword.setColumns(10);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(Color.WHITE);
		tabbedPane_1.addTab(Strings.ssl(), null, panel_6, null);
		panel_6.setLayout(null);
		
		checkBoxEnableSsl = new JCheckBox(Strings.enableSsl());
		checkBoxEnableSsl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				enableSsl( checkBoxEnableSsl.isSelected() );
			}
		});
		checkBoxEnableSsl.setBackground(Color.WHITE);
		checkBoxEnableSsl.setBounds(184, 6, 118, 21);
		panel_6.add(checkBoxEnableSsl);
		
				JLabel lblNewLabel = new JLabel(Strings.rootCa());
				lblNewLabel.setBounds(6, 35, 169, 16);
				panel_6.add(lblNewLabel);
				lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				
						JLabel lblNewLabel_2 = new JLabel(Strings.clientCrt());
						lblNewLabel_2.setBounds(6, 66, 169, 16);
						panel_6.add(lblNewLabel_2);
						lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
						
								JLabel lblDevicePrivateKey = new JLabel(Strings.clientPk());
								lblDevicePrivateKey.setBounds(6, 97, 169, 16);
								panel_6.add(lblDevicePrivateKey);
								lblDevicePrivateKey.setHorizontalAlignment(SwingConstants.RIGHT);
								
										textFieldCAFile = new JTextField();
										textFieldCAFile.setBounds(185, 33, 434, 22);
										panel_6.add(textFieldCAFile);
										textFieldCAFile.setColumns(10);
										
												btnChooseCaFile = new JButton("...");
												btnChooseCaFile.setBounds(629, 31, 53, 25);
												panel_6.add(btnChooseCaFile);
												
														textFieldClientCertFile = new JTextField();
														textFieldClientCertFile.setBounds(185, 64, 434, 22);
														panel_6.add(textFieldClientCertFile);
														textFieldClientCertFile.setColumns(10);
														
																btnChooseClientCert = new JButton("...");
																btnChooseClientCert.setBounds(629, 62, 53, 25);
																panel_6.add(btnChooseClientCert);
																
																		textFieldClientPkFile = new JTextField();
																		textFieldClientPkFile.setBounds(185, 95, 434, 22);
																		panel_6.add(textFieldClientPkFile);
																		textFieldClientPkFile.setColumns(10);
																		
																				btnChooseClientPk = new JButton("...");
																				btnChooseClientPk.setBounds(629, 93, 53, 25);
																				panel_6.add(btnChooseClientPk);
																				
																				checkBoxUseClientCertificate = new JCheckBox(Strings.useClientCrt());
																				checkBoxUseClientCertificate.addActionListener(new ActionListener() {
																					public void actionPerformed(ActionEvent e) {
																						useClientCert(checkBoxUseClientCertificate.isSelected());
																					}
																				});
																				checkBoxUseClientCertificate.setBackground(Color.WHITE);
																				checkBoxUseClientCertificate.setBounds(395, 6, 224, 21);
																				panel_6.add(checkBoxUseClientCertificate);
																				btnChooseClientPk.addActionListener(new ActionListener() {
																					public void actionPerformed(ActionEvent e) {
																						JFileChooser fileChooser = new JFileChooser();
																						int returnVal = fileChooser.showOpenDialog(null);
																						fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
																						if (returnVal == JFileChooser.APPROVE_OPTION) {
																							textFieldClientPkFile.setText( fileChooser.getSelectedFile().getAbsolutePath() );
																						}
																					}
																				});
																btnChooseClientCert.addActionListener(new ActionListener() {
																	public void actionPerformed(ActionEvent e) {
																		JFileChooser fileChooser = new JFileChooser();
																		int returnVal = fileChooser.showOpenDialog(null);
																		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
																		if (returnVal == JFileChooser.APPROVE_OPTION) {
																			textFieldClientCertFile.setText( fileChooser.getSelectedFile().getAbsolutePath() );
																		}
																	}
																});
												btnChooseCaFile.addActionListener(new ActionListener() {
													public void actionPerformed(ActionEvent e) {
														JFileChooser fileChooser = new JFileChooser();
														int returnVal = fileChooser.showOpenDialog(null);
														fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
														if (returnVal == JFileChooser.APPROVE_OPTION) {
															textFieldCAFile.setText( fileChooser.getSelectedFile().getAbsolutePath() );
														}
													}
												});
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(Color.WHITE);
		tabbedPane_1.addTab(Strings.topic(), null, panel_5, null);
		panel_5.setLayout(null);
		
				JLabel lblNewLabel_3 = new JLabel(Strings.publishTopic());
				lblNewLabel_3.setBounds(10, 35, 131, 16);
				panel_5.add(lblNewLabel_3);
				lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
				
						JLabel lblNewLabel_4 = new JLabel(Strings.subscribeTopic());
						lblNewLabel_4.setBounds(10, 71, 131, 16);
						panel_5.add(lblNewLabel_4);
						lblNewLabel_4.setHorizontalAlignment(SwingConstants.RIGHT);
						
								textFieldPubTopic = new JTextField();
								textFieldPubTopic.setBounds(151, 33, 387, 22);
								panel_5.add(textFieldPubTopic);
								textFieldPubTopic.setColumns(10);
								
										textFieldSubTopic = new JTextField();
										textFieldSubTopic.setBounds(151, 69, 387, 22);
										panel_5.add(textFieldSubTopic);
										textFieldSubTopic.setColumns(10);
										
										JPanel panel_7 = new JPanel();
										panel_7.setBackground(Color.WHITE);
										tabbedPane_1.addTab( Strings.telemetry() , null, panel_7, null);
										panel_7.setLayout(null);
										
										checkBoxEnableTelemetry = new JCheckBox( Strings.enableTelemetry() );
										checkBoxEnableTelemetry.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												enableTelemetry( checkBoxEnableTelemetry.isSelected() );
											}
										});
										checkBoxEnableTelemetry.setBackground(Color.WHITE);
										checkBoxEnableTelemetry.setBounds(32, 21, 177, 21);
										panel_7.add(checkBoxEnableTelemetry);
										
										JLabel lblNewLabel_9 = new JLabel( Strings.telemetryPublishEvery() );
										lblNewLabel_9.setHorizontalAlignment(SwingConstants.RIGHT);
										lblNewLabel_9.setBounds(215, 24, 120, 16);
										panel_7.add(lblNewLabel_9);
										
										textFieldTelemetrySeconds = new JTextField();
										textFieldTelemetrySeconds.setBounds(345, 21, 51, 19);
										panel_7.add(textFieldTelemetrySeconds);
										textFieldTelemetrySeconds.setColumns(10);
										textFieldTelemetrySeconds.addKeyListener(new KeyListener() {
											
											@Override
											public void keyTyped(KeyEvent e) {
												char c = e.getKeyChar();
												if(c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
													if(c < '0' || c > '9' || Integer.parseInt(textFieldTelemetrySeconds.getText()+""+c) >= 1000000) {
														e.consume();
													}
												}
											}
											
											@Override
											public void keyReleased(KeyEvent e) {
											}
											
											@Override
											public void keyPressed(KeyEvent e) {
											}
										});
										
										JLabel lblNewLabel_10 = new JLabel( Strings.seconds() );
										lblNewLabel_10.setBounds(406, 24, 94, 16);
										panel_7.add(lblNewLabel_10);
										
										checkBoxTelemetryInput = new JCheckBox( Strings.input() );
										checkBoxTelemetryInput.setBackground(Color.WHITE);
										checkBoxTelemetryInput.setBounds(345, 47, 155, 21);
										panel_7.add(checkBoxTelemetryInput);
										
										checkBoxTelemetryOutput = new JCheckBox( Strings.output() );
										checkBoxTelemetryOutput.setBackground(Color.WHITE);
										checkBoxTelemetryOutput.setBounds(345, 70, 155, 21);
										panel_7.add(checkBoxTelemetryOutput);
										
										checkBoxTelemetryMemory = new JCheckBox( Strings.integerFloatMemory() );
										checkBoxTelemetryMemory.setBackground(Color.WHITE);
										checkBoxTelemetryMemory.setBounds(345, 93, 155, 21);
										panel_7.add(checkBoxTelemetryMemory);
										
										JButton btnGenerateClientID = new JButton( Strings.generate() );
										btnGenerateClientID.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent e) {
												textFieldClientID.setText(RandomStringUtils.randomAlphabetic(20));
											}
										});
										btnGenerateClientID.setBounds(639, 74, 92, 21);
										panel_2.add(btnGenerateClientID);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab(Strings.pinMapping(), null, panel_3, null);
		panel_3.setLayout(null);

		tablePinMapping = new JTable();
		tablePinMapping.setBounds(649, 146, 1, 1);
		tablePinMapping.setModel(new PinModel(device));	//FIXME comment WindowBuilder
		tablePinMapping.addMouseListener(new TableMouseListener(tablePinMapping));
		
		JPopupMenu popupMenuPinMapping = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem(Strings.delete());
		deleteItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				deletePin();
			}
		});
		JMenuItem addOutputItem = new JMenuItem(Strings.addOutput());
		addOutputItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addOutput();
			}
		});
		JMenuItem addInputItem = new JMenuItem(Strings.addInput());
		addInputItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				addInput();
			}
		});

		popupMenuPinMapping.add(deleteItem);
		popupMenuPinMapping.add(addOutputItem);
		popupMenuPinMapping.add(addInputItem);

		tablePinMapping.setComponentPopupMenu(popupMenuPinMapping);

		JScrollPane scrollPane = new JScrollPane(tablePinMapping);
		scrollPane.setBounds(199, 26, 359, 220);
		panel_3.add(scrollPane);


		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Strings.codeGenerator(), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 9, 369, 98);
		contentPanel.add(panel);
		panel.setLayout(null);

		comboBox_code = new JComboBox<CodeOptions>();
		comboBox_code.setBounds(72, 40, 229, 21);
		panel.add(comboBox_code);
		comboBox_code.setModel(new DefaultComboBoxModel<CodeOptions>(CodeOptions.values()));
		comboBox_code.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, Strings.wifi(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(391, 9, 399, 98);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblSsid = new JLabel(Strings.ssid());
		lblSsid.setBounds(12, 31, 131, 16);
		panel_1.add(lblSsid);
		lblSsid.setHorizontalAlignment(SwingConstants.RIGHT);

		textFieldSsid = new JTextField();
		textFieldSsid.setBounds(155, 28, 162, 22);
		panel_1.add(textFieldSsid);
		textFieldSsid.setColumns(10);

		JLabel lblPassword = new JLabel(Strings.password());
		lblPassword.setBounds(12, 56, 131, 16);
		panel_1.add(lblPassword);
		lblPassword.setHorizontalAlignment(SwingConstants.RIGHT);

		textFieldPassword = new JTextField();
		textFieldPassword.setBounds(155, 55, 162, 22);
		panel_1.add(textFieldPassword);
		textFieldPassword.setColumns(10);

		loadFields();
	}

	private void loadFields() {
		ProgramProperties properties = ladderProgram.getProperties();
		comboBox_code.setSelectedItem( properties.getCodeOption() );
		textFieldSsid.setText( properties.getWifiSsid() );
		textFieldPassword.setText( properties.getWifiPassword() );
		textFieldBrokerAddress.setText( properties.getBrokerAddress() );
		textFieldBokerPort.setText( ""+properties.getBrokerPort() );
		textFieldClientID.setText( properties.getMqttClientID() );
		textFieldUserUsername.setText( properties.getMqttUsername() );
		textFieldUserPassword.setText( properties.getMqttPassword() );
		checkBoxEnableSsl.setSelected( properties.isEnableSsl() );
		checkBoxUseClientCertificate.setSelected( properties.isUseClientCert() );
		enableSsl( properties.isEnableSsl() );
		useClientCert( properties.isUseClientCert() );
		textFieldCAFile.setText( properties.getMqttCa() );
		textFieldClientCertFile.setText( properties.getMqttClientCert() );
		textFieldClientPkFile.setText( properties.getMqttClientPrivateKey() );
		textFieldPubTopic.setText( properties.getMqttPubTopic() );
		textFieldSubTopic.setText( properties.getMqttSubTopic() );
		textFieldTelemetrySeconds.setText( ""+properties.getTelemetrySeconds() );
		checkBoxEnableTelemetry.setSelected( properties.getEnableTelemetry() );
		checkBoxTelemetryInput.setSelected( properties.getTelemetryPubInput() );
		checkBoxTelemetryOutput.setSelected( properties.getTelemetryPutOutput() );
		checkBoxTelemetryMemory.setSelected( properties.getTelemetryPubMemory() );
		enableTelemetry( checkBoxEnableTelemetry.isSelected() );
	}

	private void save() {
		int dialogResult = JOptionPane.showConfirmDialog(this, Strings.confirmSaveProjectProperties(), Strings.titleSaveProjectProperties(), JOptionPane.YES_NO_OPTION);
		if(dialogResult == 0) {
			CodeOptions codeOpt = (CodeOptions) comboBox_code.getSelectedItem();
			ProgramProperties properties = ladderProgram.getProperties();
			properties.setCodeOption(codeOpt);
			properties.setWifiSsid( textFieldSsid.getText() );
			properties.setWifiPassword( textFieldPassword.getText() );
			properties.setBrokerAddress( textFieldBrokerAddress.getText() );
			try {
				properties.setBrokerPort( Integer.parseInt( textFieldBokerPort.getText() ) );
			} catch (Exception e) {
				properties.setBrokerPort( 1883 );
			}
			properties.setMqttClientID( textFieldClientID.getText() );
			properties.setMqttUsername( textFieldUserUsername.getText() );
			properties.setMqttPassword( textFieldUserPassword.getText() );
			properties.setEnableSsl( checkBoxEnableSsl.isSelected() );
			properties.setUseClientCert( checkBoxUseClientCertificate.isSelected() );
			properties.setMqttCa( textFieldCAFile.getText() );
			properties.setMqttClientCert( textFieldClientCertFile.getText() );
			properties.setMqttClientPrivateKey( textFieldClientPkFile.getText() );
			properties.setMqttPubTopic( textFieldPubTopic.getText() );
			properties.setMqttSubTopic( textFieldSubTopic.getText() );
			properties.setEnableTelemetry( checkBoxEnableTelemetry.isSelected() );
			properties.setTelemetryPubInput( checkBoxTelemetryInput.isSelected() );
			properties.setTelemetryPutOutput( checkBoxTelemetryOutput.isSelected() );
			properties.setTelemetryPubMemory( checkBoxTelemetryMemory.isSelected() );
			String teleSec = textFieldTelemetrySeconds.getText();
			if(teleSec.isEmpty() || teleSec.equals("0")) {
				teleSec = "5";
			}
			properties.setTelemetrySeconds( Integer.parseInt( teleSec ) );

			Mediator me = Mediator.getInstance();
			me.getProject().setChanged(true);
			FileUtils.saveLadderProgram();
			me.clearConsole();
			me.updateDevice(device);
			this.dispose();
		}
	}

	private void deletePin() {
		int column = 0;
		int row = tablePinMapping.getSelectedRow();
		String name = (String) tablePinMapping.getModel().getValueAt(row, column);
		device.removePeripheralIOByName(name);
		tablePinMapping.repaint();
	}
	
	private String getAvaliablePinName(IO io) {
		List<Integer> pinNumbers = new ArrayList<Integer>();
		for(Peripheral periferal: device.getPeripherals()) {
			for(PeripheralIO peripheralIO: periferal.getPeripheralItems()) {
				if(peripheralIO.getIo().equals(io)) {
					pinNumbers.add( Integer.parseInt(peripheralIO.getName().substring(1) ));
				}
			}
		}
		Collections.sort(pinNumbers);
		Integer num;
		for(num=1; num<=pinNumbers.size(); num++) {
			if(!pinNumbers.contains(num)) {
				break;
			}
		}
		switch(io) {
		case INPUT:
			return "I"+num;
		default:
			return "Q"+num;
		}
	}
	
	private PeripheralIO createNewPin(IO io) {
		String pinName = getAvaliablePinName(io);
		PeripheralIO peripheralIO = null;
		try {
			Integer pinNumber = Integer.parseInt( (String) JOptionPane.showInputDialog(null, pinName,Strings.pinNumber(), JOptionPane.INFORMATION_MESSAGE, null, null, null));
			String pinPath = "";
			switch(io) {
			case INPUT:
				pinPath = "PIN_I"+String.format("%02d", pinNumber);
				break;
			default:
				pinPath = "PIN_Q"+String.format("%02d", pinNumber);	
			}
			peripheralIO = new PeripheralIO(pinName, Boolean.class, ""+pinNumber, pinPath, io);
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, Strings.invalidPinNumber());
		}
		return peripheralIO;
	}
	
	private void addInput() {
		PeripheralIO peripheralIO = createNewPin(IO.INPUT);
		if(peripheralIO != null) {
			Peripheral peripheral = device.getPeripheralBySymbol("I");
			if(peripheral != null) {
				peripheral.addPeripheralItem(peripheralIO);
				device.sort();
			}
		}
	}
	
	private void addOutput() {
		PeripheralIO peripheralIO = createNewPin(IO.OUTPUT);
		if(peripheralIO != null) {
			Peripheral peripheral = device.getPeripheralBySymbol("Q");
			if(peripheral != null) {
				peripheral.addPeripheralItem(peripheralIO);
				device.sort();
			}
		}
	}
	
	private void enableSsl(boolean enable) {
		if(enable) {
			checkBoxUseClientCertificate.setEnabled(true);
			btnChooseCaFile.setEnabled(true);
			textFieldCAFile.setEnabled(true);
		}else{
			checkBoxUseClientCertificate.setEnabled(false);
			checkBoxUseClientCertificate.setSelected(false);
			btnChooseCaFile.setEnabled(false);
			btnChooseClientCert.setEnabled(false);
			btnChooseClientPk.setEnabled(false);
			textFieldCAFile.setEnabled(false);
			textFieldClientCertFile.setEnabled(false);
			textFieldClientPkFile.setEnabled(false);
		}
	}
	
	private void useClientCert(boolean use) {
		if(use) {
			btnChooseClientCert.setEnabled(true);
			btnChooseClientPk.setEnabled(true);
			textFieldClientCertFile.setEnabled(true);
			textFieldClientPkFile.setEnabled(true);
		}else{
			btnChooseClientCert.setEnabled(false);
			btnChooseClientPk.setEnabled(false);
			textFieldClientCertFile.setEnabled(false);
			textFieldClientPkFile.setEnabled(false);
		}
	}
	
	private void enableTelemetry(boolean enable) {
		textFieldTelemetrySeconds.setEnabled(enable);
		checkBoxTelemetryInput.setEnabled(enable);
		checkBoxTelemetryOutput.setEnabled(enable);
		checkBoxTelemetryMemory.setEnabled(enable);
	}
	
	class TableMouseListener extends MouseAdapter{
		
		private JTable table;
		
		public TableMouseListener(JTable table) {
			this.table = table;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			Point point = e.getPoint();
			int currentRow = table.rowAtPoint(point);
	        table.setRowSelectionInterval(currentRow, currentRow);
		}
	}

	class PinModel extends AbstractTableModel {

		private static final long serialVersionUID = 1L;
		private String[] columnNames = {Strings.name(),"pin", "type"};
		private Device device;

		public PinModel(Device device) {
			this.device = device;
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		@Override
		public int getRowCount() {
			int count = 0;
			for(Peripheral peripheral: device.getPeripherals()) {
				count += peripheral.getPeripheralItems().size();
			}
			return count;
		}

		@Override
		public int getColumnCount() {
			return columnNames.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			int count = 0;
			for(Peripheral peripheral: device.getPeripherals()) {
				for(PeripheralIO item: peripheral.getPeripheralItems()) {
					if(rowIndex == count) {
						switch(columnIndex) {
						case 0:
							return item.getName();
						case 1:
							return item.getPin();
						case 2:
							return peripheral.getName();
						}
					}
					count++;
				}
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			if(columnIndex == 1) {
				return true;
			}
			return false;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			int count = 0;
			for(Peripheral peripheral: device.getPeripherals()) {
				for(PeripheralIO item: peripheral.getPeripheralItems()) {
					if(rowIndex == count) {
						item.setPin((String) aValue);
					}
					count++;
				}
			}
		}
	}
}
