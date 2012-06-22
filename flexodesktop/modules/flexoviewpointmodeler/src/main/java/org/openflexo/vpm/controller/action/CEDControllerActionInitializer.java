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
package org.openflexo.vpm.controller.action;

import java.util.logging.Logger;

import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.InteractiveFlexoEditor;
import org.openflexo.vpm.controller.VPMController;
import org.openflexo.vpm.controller.VPMSelectionManager;

/**
 * 
 * Action initializing for this module
 * 
 * @author yourname
 */
public class CEDControllerActionInitializer extends ControllerActionInitializer {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public CEDControllerActionInitializer(InteractiveFlexoEditor editor, VPMController controller) {
		super(editor, controller);
	}

	protected VPMController getCEDController() {
		return (VPMController) getController();
	}

	protected VPMSelectionManager getCEDSelectionManager() {
		return getCEDController().getCEDSelectionManager();
	}

	@Override
	public void initializeActions() {
		super.initializeActions();

		new CEDSetPropertyInitializer(this);

		// CalcEditor perspective
		new CreateOntologyCalcInitializer(this);
		new AddEditionPatternInitializer(this);
		new DeleteEditionPatternInitializer(this);
		new DuplicateEditionPatternInitializer(this);

		// CalcDrawing edition
		new CreateCalcDrawingShemaInitializer(this);
		new DeleteCalcDrawingShemaInitializer(this);
		new PushToPaletteInitializer(this);
		new DeclareShapeInEditionPatternInitializer(this);
		new DeclareConnectorInEditionPatternInitializer(this);
		new DeleteCalcShemaElementsInitializer(this);

		// Palette edition
		new CreateCalcPaletteInitializer(this);
		new DeleteCalcPaletteInitializer(this);
		new AddCalcPaletteElementInitializer(this);
		new DeleteCalcPaletteElementInitializer(this);

		// OntologyEditor perspective
		new CreateOntologyClassInitializer(this);
		new CreateOntologyIndividualInitializer(this);
		new CreateObjectPropertyInitializer(this);
		new CreateDataPropertyInitializer(this);
		new DeleteOntologyObjectsInitializer(this);
		new AddAnnotationStatementInitializer(this);
	}

}
