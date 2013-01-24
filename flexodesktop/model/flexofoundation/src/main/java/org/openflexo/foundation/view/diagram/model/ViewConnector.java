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
package org.openflexo.foundation.view.diagram.model;

import java.util.logging.Logger;

import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.foundation.view.diagram.viewpoint.ConnectorPatternRole;
import org.openflexo.foundation.xml.ViewBuilder;

public class ViewConnector extends ViewElement {

	private static final Logger logger = Logger.getLogger(ViewShape.class.getPackage().getName());

	private ViewShape startShape;
	private ViewShape endShape;

	/**
	 * Constructor invoked during deserialization
	 * 
	 * @param componentDefinition
	 */
	public ViewConnector(ViewBuilder builder) {
		this(builder.view);
		initializeDeserialization(builder);
	}

	/**
	 * Default constructor for OEShema
	 * 
	 * @param shemaDefinition
	 */
	public ViewConnector(View shema) {
		super(shema);
	}

	/**
	 * Common constructor for OEShema
	 * 
	 * @param shemaDefinition
	 */
	public ViewConnector(View shema, ViewShape aStartShape, ViewShape anEndShape) {
		super(shema);
		setStartShape(aStartShape);
		setEndShape(anEndShape);
	}

	@Override
	public ConnectorGraphicalRepresentation<ViewConnector> getGraphicalRepresentation() {
		return (ConnectorGraphicalRepresentation<ViewConnector>) super.getGraphicalRepresentation();
	}

	/**
	 * Reset graphical representation to be the one defined in related pattern role
	 */
	@Override
	public void resetGraphicalRepresentation() {
		getGraphicalRepresentation().setsWith(getPatternRole().getGraphicalRepresentation(), GraphicalRepresentation.Parameters.text,
				GraphicalRepresentation.Parameters.isVisible, GraphicalRepresentation.Parameters.absoluteTextX,
				GraphicalRepresentation.Parameters.absoluteTextY);
		refreshGraphicalRepresentation();
	}

	/**
	 * Refresh graphical representation
	 */
	@Override
	public void refreshGraphicalRepresentation() {
		super.refreshGraphicalRepresentation();
		getGraphicalRepresentation().notifyConnectorChanged();
	}

	@Override
	public void delete() {
		if (getParent() != null) {
			getParent().removeFromChilds(this);
		}
		super.delete();
		deleteObservers();
	}

	/* @Override
	 public AddShemaElementAction getEditionAction() 
	 {
	 	return getAddConnectorAction();
	 }
	 
	public AddConnector getAddConnectorAction()
	{
		if (getEditionPattern() != null && getPatternRole() != null)
			return getEditionPattern().getAddConnectorAction(getPatternRole());
		return null;
	}*/

	@Override
	public String getClassNameKey() {
		return "oe_connector";
	}

	@Override
	public String getFullyQualifiedName() {
		return getView().getFullyQualifiedName() + "." + getName();
	}

	public ViewShape getEndShape() {
		return endShape;
	}

	public void setEndShape(ViewShape endShape) {
		this.endShape = endShape;
		endShape.addToIncomingConnectors(this);
	}

	public ViewShape getStartShape() {
		return startShape;
	}

	public void setStartShape(ViewShape startShape) {
		this.startShape = startShape;
		startShape.addToOutgoingConnectors(this);
	}

	@Override
	public boolean isContainedIn(ViewObject o) {
		if (o == this) {
			return true;
		}
		if (getParent() != null && getParent() == o) {
			return true;
		}
		if (getParent() != null) {
			return getParent().isContainedIn(o);
		}
		return false;
	}

	@Override
	public String getDisplayableDescription() {
		return "Connector" + (getEditionPattern() != null ? " representing " + getEditionPattern() : "");
	}

	@Override
	public ConnectorPatternRole getPatternRole() {
		return (ConnectorPatternRole) super.getPatternRole();
	}

}
