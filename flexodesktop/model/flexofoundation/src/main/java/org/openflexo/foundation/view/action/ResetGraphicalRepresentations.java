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
package org.openflexo.foundation.view.action;

import java.security.InvalidParameterException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.action.NotImplementedException;
import org.openflexo.foundation.rm.DuplicateResourceException;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.view.diagram.model.View;
import org.openflexo.foundation.view.diagram.model.ViewElement;
import org.openflexo.foundation.view.diagram.model.ViewObject;

/**
 * This action reset all graphical representations found in view to conform to those described in EditionPattern
 * 
 * @author sylvain
 * 
 */
public class ResetGraphicalRepresentations extends FlexoAction<ResetGraphicalRepresentations, ViewObject, ViewObject> {

	private static final Logger logger = Logger.getLogger(ResetGraphicalRepresentations.class.getPackage().getName());

	public static FlexoActionType<ResetGraphicalRepresentations, ViewObject, ViewObject> actionType = new FlexoActionType<ResetGraphicalRepresentations, ViewObject, ViewObject>(
			"reset_graphical_representations", FlexoActionType.editGroup, FlexoActionType.NORMAL_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public ResetGraphicalRepresentations makeNewAction(ViewObject focusedObject, Vector<ViewObject> globalSelection, FlexoEditor editor) {
			return new ResetGraphicalRepresentations(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(ViewObject view, Vector<ViewObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(ViewObject view, Vector<ViewObject> globalSelection) {
			return view instanceof View;
		}

	};

	static {
		FlexoModelObject.addActionForClass(ResetGraphicalRepresentations.actionType, View.class);
	}

	ResetGraphicalRepresentations(ViewObject focusedObject, Vector<ViewObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws DuplicateResourceException, NotImplementedException, InvalidParameterException {
		logger.info("Reset graphical representations for view");

		View view = null;

		if (getFocusedObject() instanceof View) {
			view = (View) getFocusedObject();
		}

		if (view != null) {
			processElement(view);
		}

	}

	private void processElement(ViewObject o) {
		if (o instanceof ViewElement) {
			((ViewElement) o).resetGraphicalRepresentation();
		}
		/*if (o instanceof ViewShape) {
			ViewShape shape = (ViewShape) o;
			if (shape.getPatternRole() != null) {
				((ShapeGraphicalRepresentation) shape.getGraphicalRepresentation()).setsWith((ShapeGraphicalRepresentation) shape
						.getPatternRole().getGraphicalRepresentation(), GraphicalRepresentation.Parameters.text,
						GraphicalRepresentation.Parameters.isVisible, GraphicalRepresentation.Parameters.absoluteTextX,
						GraphicalRepresentation.Parameters.absoluteTextY, ShapeGraphicalRepresentation.Parameters.x,
						ShapeGraphicalRepresentation.Parameters.y, ShapeGraphicalRepresentation.Parameters.width,
						ShapeGraphicalRepresentation.Parameters.height, ShapeGraphicalRepresentation.Parameters.relativeTextX,
						ShapeGraphicalRepresentation.Parameters.relativeTextY);
			}
		} else if (o instanceof ViewConnector) {
			ViewConnector connector = (ViewConnector) o;
			if (connector.getPatternRole() != null) {
				((ConnectorGraphicalRepresentation) connector.getGraphicalRepresentation()).setsWith(
						(ConnectorGraphicalRepresentation) connector.getPatternRole().getGraphicalRepresentation(),
						GraphicalRepresentation.Parameters.text, GraphicalRepresentation.Parameters.isVisible,
						GraphicalRepresentation.Parameters.absoluteTextX, GraphicalRepresentation.Parameters.absoluteTextY);
			}
		}*/
		for (ViewObject child : o.getChilds()) {
			processElement(child);
		}
	}

	public FlexoProject getProject() {
		if (getFocusedObject() != null) {
			return getFocusedObject().getProject();
		}
		return null;
	}

}