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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Preferences {

	public static String LADDER_FILE_NAME = "ladderFileName";
	public static String DEVICE_TOOLS_PATH = "deviceToolsPath";
	public static String LANG = "lang";
	
	private static Map<String, Object> preferences;
	private static String PREFERENCES_FILE_NAME = "ladder.pref";   
	
	private synchronized static void write(){
		ObjectOutputStream out = null;
		try{
			out = new ObjectOutputStream(new FileOutputStream(new File(PREFERENCES_FILE_NAME)));
			out.writeObject(preferences);
		}catch(Exception e){
			try {
				out.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	private synchronized static void read(){
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(PREFERENCES_FILE_NAME));
			preferences = (Map<String, Object>) in.readObject();
		} catch (Exception e) {
			preferences = new HashMap<String,Object>();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}
	}
	
	public synchronized static void put(String key,Object value){
		if(preferences == null){
			read();
		}
		preferences.put(key, value);
		write();
	}
	
	public synchronized static Object get(String key){
		if(preferences == null){
			read();
		}
		if(!preferences.isEmpty()){
			if(preferences.containsKey(key)){
				return preferences.get(key);
			}
		}
		return null;
	}

}
