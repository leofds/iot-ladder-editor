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
package com.github.leofds.iotladdereditor.compiler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.application.ProjectContainer;
import com.github.leofds.iotladdereditor.compiler.analizer.SemanticAnalyzer;
import com.github.leofds.iotladdereditor.compiler.domain.IR;
import com.github.leofds.iotladdereditor.compiler.generator.CodeGenerator;
import com.github.leofds.iotladdereditor.compiler.generator.IRGenerator;
import com.github.leofds.iotladdereditor.compiler.generator.factory.CodeGeneratorFactory;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.util.FileUtils;

public class Compiler{

	public static boolean build(ProjectContainer project){

		printDate();
		Mediator.getInstance().outputConsoleMessage(Strings.compiling()+"...");

		if(SemanticAnalyzer.analyze(project.getLadderProgram())){
			IR ir = IRGenerator.generate(project.getLadderProgram());
			project.setIr(ir);

			try {
				FileUtils.createFile("out/ladder.ir", ir.getTextQuadruple());
				CodeGenerator codeGenerator = CodeGeneratorFactory.create( project.getLadderProgram().getProperties().getCodeOption() );
				SourceCode sCode = codeGenerator.generate(project);

				for(SourceFile src:sCode.getFiles()){
					FileUtils.createFile(String.format("out/%s", src.getFname()),src.getContent());
				}

				Mediator.getInstance().outputConsoleMessage(Strings.successfullyCompleted());
				project.setCompiled(true);
				return true;
			} catch (IOException e) {
				Mediator.getInstance().outputConsoleMessage(Strings.failToCreateFile());
			}
		}else{
			project.setCompiled(false);
			Mediator.getInstance().outputConsoleMessage(Strings.terminatedWithError());	
		}
		return false;
	}

	private static void printDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = simpleDateFormat.format(new Date());
		Mediator.getInstance().outputConsoleMessage(date);
	}
}
