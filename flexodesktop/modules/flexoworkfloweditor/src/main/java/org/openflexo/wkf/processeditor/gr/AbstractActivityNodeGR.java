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
package org.openflexo.wkf.processeditor.gr;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.fge.FGEUtils;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.controller.CustomClickControlAction;
import org.openflexo.fge.controller.DrawingController;
import org.openflexo.fge.controller.MouseClickControl;
import org.openflexo.fge.graphics.TextStyle;
import org.openflexo.fge.shapes.Shape.ShapeType;
import org.openflexo.foundation.wkf.Role;
import org.openflexo.foundation.wkf.action.OpenOperationLevel;
import org.openflexo.foundation.wkf.node.AbstractActivityNode;
import org.openflexo.foundation.wkf.node.SelfExecutableActivityNode;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.wkf.WKFPreferences;
import org.openflexo.wkf.processeditor.ProcessRepresentation;

public abstract class AbstractActivityNodeGR<O extends AbstractActivityNode> extends FlexoNodeGR<O> {

	protected static final Color BG_COLOR = new Color(240, 240, 240);
	private static final Logger logger = Logger.getLogger(AbstractActivityNodeGR.class.getPackage().getName());

	public AbstractActivityNodeGR(O activity, ShapeType shapeType, ProcessRepresentation aDrawing, boolean isInPalet) {
		super(activity, shapeType, aDrawing, isInPalet);
		setLayer(activity.isEmbedded() ? EMBEDDED_ACTIVITY_LAYER : ACTIVITY_LAYER);

		if (!(activity instanceof SelfExecutableActivityNode)) {
			addToMouseClickControls(new PetriGraphOpener(), true);
		}

		updatePropertiesFromWKFPreferences();
	}

	@Override
	public void updatePropertiesFromWKFPreferences() {
		super.updatePropertiesFromWKFPreferences();
		setTextStyle(TextStyle.makeTextStyle(getTextColor(),
				getWorkflow() != null ? getWorkflow().getActivityFont(WKFPreferences.getActivityNodeFont()).getFont() : WKFPreferences
						.getActivityNodeFont().getFont()));
		setIsMultilineAllowed(true);
		setAdjustMinimalWidthToLabelWidth(true);
		setAdjustMinimalHeightToLabelHeight(true);
	}

	@Override
	public Color getTextColor() {
		return FGEUtils.chooseBestColor(getMainBgColor(), Color.BLACK, Color.WHITE);
	}

	@Override
	public Color getMainBgColor() {
		if (getAbstractActivityNode().getRole() != null) {
			// See also org.openflexo.wkf.swleditor.gr.RoleContainerGR.updateColors() and
			// org.openflexo.wkf.roleeditor.RoleGR.getRoleColor() and
			// org.openflexo.components.browser.wkf.RoleElement.buildCustomIcon(Color)
			return getAbstractActivityNode().getRole().getColor() != null ? getAbstractActivityNode().getRole().getColor() : Color.RED;
		}
		return BG_COLOR;
	}

	public O getAbstractActivityNode() {
		return getDrawable();
	}

	public String getSubLabel() {
		if (getAbstractActivityNode() == null) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("node is null in " + this);
			}
			return "";
		}
		Role role = getAbstractActivityNode().getRole();
		if (role != null) {
			return role.getName();
		} else {
			return FlexoLocalization.localizedForKey("no_role");
		}
	}

	public class PetriGraphOpener extends MouseClickControl {

		public PetriGraphOpener() {
			super("Opener", MouseButton.LEFT, 2, new CustomClickControlAction() {
				@Override
				public boolean handleClick(GraphicalRepresentation<?> graphicalRepresentation, DrawingController<?> controller,
						java.awt.event.MouseEvent event) {
					logger.info("Opening Operation petri graph by double-clicking");
					OpenOperationLevel.actionType.makeNewAction(getAbstractActivityNode(), null, getDrawing().getEditor()).doAction();
					// Is now performed by receiving notification
					// getDrawing().updateGraphicalObjectsHierarchy();
					return true;
				}
			}, false, false, false, false);
		}

	}

}
