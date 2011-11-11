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

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.fib.controller.FIBDialog;
import org.openflexo.fib.controller.FIBController.Status;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.viewpoint.action.CreateExampleDrawing;
import org.openflexo.icon.VPMIconLibrary;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.vpm.CEDCst;
import org.openflexo.vpm.controller.CEDController;

public class CreateCalcDrawingShemaInitializer extends ActionInitializer {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	CreateCalcDrawingShemaInitializer(CEDControllerActionInitializer actionInitializer) {
		super(CreateExampleDrawing.actionType, actionInitializer);
	}

	@Override
	protected CEDControllerActionInitializer getControllerActionInitializer() {
		return (CEDControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	public CEDController getController() {
		return (CEDController) super.getController();
	}

	@Override
	protected FlexoActionInitializer<CreateExampleDrawing> getDefaultInitializer() {
		return new FlexoActionInitializer<CreateExampleDrawing>() {
			@Override
			public boolean run(ActionEvent e, CreateExampleDrawing action) {

				FIBDialog dialog = FIBDialog.instanciateComponent(CEDCst.CREATE_EXAMPLE_DRAWING_DIALOG_FIB, action, null, true);
				return (dialog.getStatus() == Status.VALIDATED);
			}
		};
	}

	@Override
	protected FlexoActionFinalizer<CreateExampleDrawing> getDefaultFinalizer() {
		return new FlexoActionFinalizer<CreateExampleDrawing>() {
			@Override
			public boolean run(ActionEvent e, CreateExampleDrawing action) {
				getController().setCurrentEditedObjectAsModuleView(action.getNewShema(), getController().VIEW_POINT_PERSPECTIVE);
				return true;
			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return VPMIconLibrary.EXAMPLE_DIAGRAM_ICON;
	}

}
