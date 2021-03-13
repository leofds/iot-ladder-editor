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
package com.github.leofds.iotladdereditor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.application.Preferences;
import com.github.leofds.iotladdereditor.application.ProjectContainer;
import com.github.leofds.iotladdereditor.example.Example;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;

public class FileUtils {

	private static String getPName(String fileName){
		String[] s = Paths.get(fileName).getFileName().toString().split("[.]");
		if(s != null){
			return s[0];
		}
		return null;
	}
	
	public static void createFile(String path,String content) throws IOException{
		File file = new File(path);
		file.getParentFile().mkdirs();
		file.createNewFile();
		FileWriter writer = new FileWriter(file);
		writer.write(content);
		writer.flush();
		writer.close();
		Mediator.getInstance().outputConsoleMessage("\t"+path);
	}
	
	private static void writeProgramFile(String fileName){
		Mediator me = Mediator.getInstance();
		ProjectContainer project = me.getProject();
		project.setAbsoletePath(fileName);
		ObjectOutputStream out = null;
		try{
			Mediator.getInstance().unselectInstruction();
			out = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
			out.writeObject(project.getLadderProgram());
			project.setChanged(false);
			project.setSaved(true);
			project.setName(getPName(fileName));
			me.updateProjectAndViews();
		}catch(Exception e){
			e.printStackTrace();
			me.outputDialogMessage( Strings.errorSavingFile() );
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static LadderProgram read(ObjectInputStream in) throws ClassNotFoundException, IOException{
		Object obj = in.readObject();
		if(obj instanceof LadderProgram){
			return (LadderProgram) obj;
		}
		return null;
	}
	
	private static void readProgramFile(String fileName){
		Mediator me = Mediator.getInstance();
		ProjectContainer project = me.getProject();
		project.setAbsoletePath(fileName);
		ObjectInputStream in = null;
		try{
			in = new ObjectInputStream(new FileInputStream(fileName));
			LadderProgram ladderProgram = read(in);
			if(ladderProgram != null) {
				project.setLadderProgram(ladderProgram);
				project.setChanged(false);
				project.setSaved(true);
				project.setName(getPName(fileName));
				me.updateProjectAndViews();
			}else {
				me.outputDialogMessage( Strings.errorReadingFile() );
			}
		}catch(Exception e){
			e.printStackTrace();
			me.outputDialogMessage( Strings.errorOpeningFile() );
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void confirmSave() {
		Mediator me = Mediator.getInstance();
		if(me.getProject() != null) {
			if(me.isChangedProgram()) {
				int ret = JOptionPane.showConfirmDialog(null, Strings.saveCurrentProgram(), Strings.saveChanges(),JOptionPane.YES_NO_OPTION);
				if(ret == JOptionPane.YES_OPTION) {
					saveLadderProgram();
				}
			}
		}
	}
	
	public static void openExample(Example ex, LadderProgram ladderProgram) {
		Mediator me = Mediator.getInstance();
		confirmSave();
		ProjectContainer project = new ProjectContainer();
		project.setLadderProgram(ladderProgram);
		project.setChanged(false);
		project.setSaved(false);
		project.setName(getPName(ex.name));
		project.setAbsoletePath(project.getName());
		me.setProject(project);
		me.updateProjectAndViews();
		me.clearConsole();
		me.setNoCompiled();
	}
	
	public static void openLadderProgram(){
		Mediator me = Mediator.getInstance();
		try {
			confirmSave();
			FileFilter filtroTexto = new FileNameExtensionFilter( Strings.ladderProgramExtension(), "ld");
			JFileChooser fc = new JFileChooser((String) Preferences.get(Preferences.LADDER_FILE_NAME));
			fc.addChoosableFileFilter(filtroTexto);
			fc.setFileFilter(filtroTexto);
			fc.setAcceptAllFileFilterUsed(false);
			int opcao = fc.showOpenDialog(null);
			if (opcao == JFileChooser.APPROVE_OPTION){
				File arq = fc.getSelectedFile();
				int index = arq.getName().lastIndexOf(".");					
				if(index > -1){
					String extencion = arq.getName().substring(index);
					if(extencion.equalsIgnoreCase(".ld")){
						Preferences.put(Preferences.LADDER_FILE_NAME, arq.getAbsolutePath());
						readProgramFile(arq.getAbsolutePath());
						me.updateProjectAndViews();
						me.clearConsole();
						me.setNoCompiled();
					}else{
						me.outputDialogMessage( Strings.invalidFileExtension() );
					}
				}
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			me.outputDialogMessage( Strings.errorOpeningFile() );
		}
	}
	
	public static boolean saveLadderProgram(){
		Mediator me = Mediator.getInstance();
		ProjectContainer project = me.getProject();
		if(project.isChanged()){
			if(project.isSaved()){
				writeProgramFile((String) Preferences.get(Preferences.LADDER_FILE_NAME));
			}else{
				return saveAsLadderProgram();
			}
		}
		return true;
	}
	
	private static boolean checkOverwriteFile(String absolutPath) {
		File file = new File(absolutPath);
		if(file.exists()) {
			int dialogResult = JOptionPane.showConfirmDialog(null, Strings.doYouWantToOverwriteTheFile(), "overwrite", JOptionPane.YES_NO_OPTION);
			if(dialogResult != 0) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean saveAsLadderProgram(){
		Mediator me = Mediator.getInstance();
		try{
			FileFilter filtroTexto = new FileNameExtensionFilter( Strings.ladderProgramExtension(), "ld");
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new File("").getCanonicalFile());
			fc.addChoosableFileFilter(filtroTexto);
			fc.setFileFilter(filtroTexto);
			fc.setAcceptAllFileFilterUsed(false);
			int opcao = fc.showSaveDialog(null);
			if (opcao == JFileChooser.APPROVE_OPTION){
				
				File file = fc.getSelectedFile();
				String absolutPath = fc.getSelectedFile().getAbsolutePath()+".ld";
				
				int index = file.getName().lastIndexOf(".");					
				if(index > -1){
					String extension = file.getName().substring(index);
					if(extension.equalsIgnoreCase(".ld")){
						absolutPath = fc.getSelectedFile().getAbsolutePath();
					}else{
						me.outputDialogMessage(Strings.theExtensionMustBe());
						return false;
					}
				}
				
				if(checkOverwriteFile(absolutPath)) {
					Preferences.put(Preferences.LADDER_FILE_NAME, absolutPath);
					writeProgramFile(absolutPath);
				}
				return true;
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			me.outputDialogMessage( Strings.errorSavingFile() );
		}
		return false;
	}
	
	public static void newLadderProgram(){
		confirmSave();
		Mediator me = Mediator.getInstance();
		me.setProject(new ProjectContainer());
		me.updateProjectAndViews();
		me.clearConsole();
		me.setNoCompiled();
	}
	
	public static void newLadderProgram(ProjectContainer projectContainer) {
		confirmSave();
		Mediator me = Mediator.getInstance();
		me.setProject(projectContainer);;
		me.updateProjectAndViews();
		me.clearConsole();
		me.setNoCompiled();
	}
	
	public static void closeLadderProject() {
		confirmSave();
		Mediator.getInstance().closeProgram();
	}
}
