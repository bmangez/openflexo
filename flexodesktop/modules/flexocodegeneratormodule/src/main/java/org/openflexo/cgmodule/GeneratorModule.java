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
package org.openflexo.cgmodule;

import java.util.logging.Logger;

import org.openflexo.cgmodule.controller.GeneratorController;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.InspectorGroup;
import org.openflexo.foundation.Inspectors;
import org.openflexo.module.FlexoModule;
import org.openflexo.module.external.ExternalGeneratorModule;
import org.openflexo.view.controller.InteractiveFlexoEditor;

/**
 * Data Model Editor module
 * 
 * @author sguerin
 */
public class GeneratorModule extends FlexoModule implements ExternalGeneratorModule {

	private static final Logger logger = Logger.getLogger(GeneratorModule.class.getPackage().getName());
	private static final InspectorGroup[] inspectorGroups = new InspectorGroup[] { Inspectors.GENERATORS, Inspectors.CG };

	public GeneratorModule(InteractiveFlexoEditor projectEditor) throws Exception {
		super(projectEditor);
		setFlexoController(new GeneratorController(projectEditor, this));
		GeneratorPreferences.init(getGeneratorController());
		if (getProject().getGeneratedCode().getGeneratedRepositories().size() == 0) {
			getGeneratorController().setCurrentEditedObjectAsModuleView(getProject().getGeneratedCode());
			getGeneratorController().selectAndFocusObject(getProject().getGeneratedCode());
		} else {
			getGeneratorController().setCurrentEditedObjectAsModuleView(
					getProject().getGeneratedCode().getGeneratedRepositories().firstElement());
			getGeneratorController().selectAndFocusObject(getProject().getGeneratedCode().getGeneratedRepositories().firstElement());
		}
	}

	@Override
	public InspectorGroup[] getInspectorGroups() {
		return inspectorGroups;
	}

	public GeneratorController getGeneratorController() {
		return (GeneratorController) getFlexoController();
	}

	/**
	 * Overrides getDefaultObjectToSelect
	 * 
	 * @see org.openflexo.module.FlexoModule#getDefaultObjectToSelect()
	 */
	@Override
	public FlexoModelObject getDefaultObjectToSelect() {
		return getProject().getGeneratedCode();
	}

	/**
	 * Overrides moduleWillClose
	 * 
	 * @see org.openflexo.module.FlexoModule#moduleWillClose()
	 */
	@Override
	public void moduleWillClose() {
		super.moduleWillClose();
		getProject().getGeneratedCode().setFactory(null);
		GeneratorPreferences.reset();
	}
}
