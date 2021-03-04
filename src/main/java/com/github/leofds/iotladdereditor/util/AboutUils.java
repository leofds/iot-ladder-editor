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
import java.io.InputStream;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

public class AboutUils {

	public static Model getProjectModel() {
		try {
			MavenXpp3Reader reader = new MavenXpp3Reader();
			InputStream is = null;
			File projectFile = new File("pom.xml");
			if(projectFile.exists()) {
				is = new FileInputStream(projectFile);
			}else {
				is = AboutUils.class.getClassLoader().getResourceAsStream("META-INF/maven/com.github.leofds/iot_ladder_editor/pom.xml");
			}
			return reader.read(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
