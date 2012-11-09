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
package org.openflexo.fge.connectors.impl;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.Vector;

import org.openflexo.fge.ConnectorGraphicalRepresentation;
import org.openflexo.fge.FGEUtils;
import org.openflexo.fge.GraphicalRepresentationUtils;
import org.openflexo.fge.connectors.CurvedPolylinConnector;
import org.openflexo.fge.cp.ConnectorAdjustingControlPoint;
import org.openflexo.fge.cp.ControlPoint;
import org.openflexo.fge.geom.FGEGeometricObject.Filling;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.geom.FGERectangle;
import org.openflexo.fge.graphics.FGEConnectorGraphics;

public class CurvedPolylinConnectorImpl extends ConnectorImpl implements CurvedPolylinConnector {

	private FGEPoint p1 = new FGEPoint();
	private FGEPoint p2 = new FGEPoint();
	private Vector<ControlPoint> controlPoints;

	// *******************************************************************************
	// * Constructor *
	// *******************************************************************************

	/**
	 * This constructor should not be used, as it is invoked by PAMELA framework to create objects, as well as during deserialization
	 */
	public CurvedPolylinConnectorImpl() {
		super();
		controlPoints = new Vector<ControlPoint>();
	}

	@Deprecated
	private CurvedPolylinConnectorImpl(ConnectorGraphicalRepresentation<?> graphicalRepresentation) {
		this();
		setGraphicalRepresentation(graphicalRepresentation);
		controlPoints.add(new ConnectorAdjustingControlPoint(graphicalRepresentation, p1));
		controlPoints.add(new ConnectorAdjustingControlPoint(graphicalRepresentation, p2));
	}

	@Override
	public ConnectorType getConnectorType() {
		return ConnectorType.CURVED_POLYLIN;
	}

	@Override
	public List<ControlPoint> getControlAreas() {
		return controlPoints;
	}

	private void updateControlPoints() {
		FGEPoint newP1 = GraphicalRepresentationUtils.convertNormalizedPoint(getStartObject(), new FGEPoint(0.5, 0.5),
				getGraphicalRepresentation());
		FGEPoint newP2 = GraphicalRepresentationUtils.convertNormalizedPoint(getEndObject(), new FGEPoint(0.5, 0.5),
				getGraphicalRepresentation());

		p1.x = newP1.x;
		p1.y = newP1.y;
		p2.x = newP2.x;
		p2.y = newP2.y;
	}

	@Override
	public void drawConnector(FGEConnectorGraphics g) {
		updateControlPoints();

		g.drawLine(p1.x, p1.y, p2.x, p2.y);

	}

	@Override
	public double getStartAngle() {
		return FGEUtils.getSlope(p1, p2);
	}

	@Override
	public double getEndAngle() {
		return FGEUtils.getSlope(p2, p1);
	}

	@Override
	public double distanceToConnector(FGEPoint aPoint, double scale) {
		Point testPoint = getGraphicalRepresentation().convertNormalizedPointToViewCoordinates(aPoint, scale);
		Point point1 = getGraphicalRepresentation().convertNormalizedPointToViewCoordinates(p1, scale);
		Point point2 = getGraphicalRepresentation().convertNormalizedPointToViewCoordinates(p2, scale);
		return Line2D.ptSegDist(point1.x, point1.y, point2.x, point2.y, testPoint.x, testPoint.y);
	}

	@Override
	public void refreshConnector() {
		if (!needsRefresh()) {
			return;
		}

		updateControlPoints();

		super.refreshConnector();

		// firstUpdated = true;

	}

	@Override
	public boolean needsRefresh() {
		// if (!firstUpdated) return true;
		return super.needsRefresh();
	}

	@Override
	public FGEPoint getMiddleSymbolLocation() {
		// TODO Auto-generated method stub
		return new FGEPoint(0.5, 0.5);
	}

	private FGERectangle NORMALIZED_BOUNDS = new FGERectangle(0, 0, 1, 1, Filling.FILLED);

	@Override
	public FGERectangle getConnectorUsedBounds() {
		return NORMALIZED_BOUNDS;
	}

	/**
	 * Return start point, relative to start object
	 * 
	 * @return
	 */
	@Override
	public FGEPoint getStartLocation() {
		return p1;
	}

	/**
	 * Return end point, relative to end object
	 * 
	 * @return
	 */
	@Override
	public FGEPoint getEndLocation() {
		return p2;
	}

}
