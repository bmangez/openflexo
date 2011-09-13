/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo.ced.fib.dialogs;

import java.io.File;

import org.openflexo.ced.CEDCst;
import org.openflexo.fib.editor.FIBAbstractEditor;
import org.openflexo.foundation.FlexoResourceCenter;
import org.openflexo.foundation.ontology.calc.CalcDrawingShape;
import org.openflexo.foundation.ontology.calc.CalcDrawingShema;
import org.openflexo.foundation.ontology.calc.CalcLibrary;
import org.openflexo.foundation.ontology.calc.OntologyCalc;
import org.openflexo.foundation.ontology.calc.action.PushToPalette;
import org.openflexo.module.ModuleLoader;


public class PushToPaletteDialogEDITOR {

	
	public static void main(String[] args)
	{
		FIBAbstractEditor editor = new FIBAbstractEditor() {
			@Override
			public Object[] getData() 
			{
				FlexoResourceCenter resourceCenter = ModuleLoader.getFlexoResourceCenter(true);
				CalcLibrary calcLibrary = resourceCenter.retrieveCalcLibrary();
				OntologyCalc calc1 = calcLibrary.getOntologyCalc("http://www.agilebirds.com/flexo/ontologies/Calcs/BasicOrganizationTreeEditor.owl");
				calc1.loadWhenUnloaded();
				CalcDrawingShema shema = calc1.getShemas().firstElement();
				CalcDrawingShape shape = (CalcDrawingShape)shema.getChilds().firstElement();
				PushToPalette action = PushToPalette.actionType.makeNewAction(shape, null,null);
				return makeArray(action);
			}
			@Override
			public File getFIBFile() {
				return CEDCst.PUSH_TO_PALETTE_DIALOG_FIB;
			}
		};
		editor.launch();
	}
}
