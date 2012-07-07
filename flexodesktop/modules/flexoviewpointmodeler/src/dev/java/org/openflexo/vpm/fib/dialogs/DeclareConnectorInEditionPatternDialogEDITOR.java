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
package org.openflexo.vpm.fib.dialogs;

import java.io.File;

import org.openflexo.fib.editor.FIBAbstractEditor;
import org.openflexo.foundation.FlexoResourceCenter;
import org.openflexo.foundation.viewpoint.ExampleDrawingConnector;
import org.openflexo.foundation.viewpoint.ExampleDrawingShema;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPointLibrary;
import org.openflexo.foundation.viewpoint.action.DeclareConnectorInEditionPattern;
import org.openflexo.module.FlexoResourceCenterService;
import org.openflexo.vpm.CEDCst;

public class DeclareConnectorInEditionPatternDialogEDITOR {

	public static void main(String[] args) {
		FIBAbstractEditor editor = new FIBAbstractEditor() {
			@Override
			public Object[] getData() {
				FlexoResourceCenter resourceCenter = getFlexoResourceCenterService().getFlexoResourceCenter(true);
				ViewPointLibrary calcLibrary = resourceCenter.retrieveViewPointLibrary();
				ViewPoint calc1 = calcLibrary.getOntologyCalc("http://www.agilebirds.com/openflexo/ViewPoints/Basic/BasicOntology.owl");
				calc1.loadWhenUnloaded();
				ExampleDrawingShema shema = calc1.getShemas().firstElement();
				ExampleDrawingConnector connector = (ExampleDrawingConnector) shema.getChilds().get(2);
				DeclareConnectorInEditionPattern action = DeclareConnectorInEditionPattern.actionType.makeNewAction(connector, null, null);
				return makeArray(action);
			}

			@Override
			public File getFIBFile() {
				return CEDCst.DECLARE_CONNECTOR_IN_EDITION_PATTERN_DIALOG_FIB;
			}
		};
		editor.launch();
	}

	private static FlexoResourceCenterService getFlexoResourceCenterService() {
		return FlexoResourceCenterService.getInstance();
	}
}
