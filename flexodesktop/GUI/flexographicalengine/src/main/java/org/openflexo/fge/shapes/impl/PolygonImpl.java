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
package org.openflexo.fge.shapes.impl;

import java.util.ArrayList;
import java.util.List;

import org.openflexo.fge.ShapeGraphicalRepresentation;
import org.openflexo.fge.geom.FGEGeometricObject.Filling;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.geom.FGEPolygon;
import org.openflexo.fge.shapes.Polygon;

public class PolygonImpl extends ShapeImpl implements Polygon {

	private FGEPolygon _polygon;

	private List<FGEPoint> points;

	// *******************************************************************************
	// * Constructor *
	// *******************************************************************************

	/**
	 * This constructor should not be used, as it is invoked by PAMELA framework to create objects, as well as during deserialization
	 */
	public PolygonImpl() {
		super();
		this.points = new ArrayList<FGEPoint>();
		updateShape();
	}

	@Deprecated
	private PolygonImpl(ShapeGraphicalRepresentation aGraphicalRepresentation) {
		this();
		setGraphicalRepresentation(aGraphicalRepresentation);
	}

	@Deprecated
	private PolygonImpl(ShapeGraphicalRepresentation aGraphicalRepresentation, List<FGEPoint> points) {
		this();
		setGraphicalRepresentation(aGraphicalRepresentation);
		updateShape();
	}

	@Deprecated
	private PolygonImpl(ShapeGraphicalRepresentation aGraphicalRepresentation, FGEPolygon polygon) {
		this();
		setGraphicalRepresentation(aGraphicalRepresentation);
		for (FGEPoint pt : polygon.getPoints()) {
			points.add(pt);
		}
		updateShape();
	}

	@Deprecated
	private PolygonImpl(ShapeGraphicalRepresentation aGraphicalRepresentation, FGEPoint... points) {
		this();
		setGraphicalRepresentation(aGraphicalRepresentation);
		for (FGEPoint pt : points) {
			this.points.add(pt);
		}
		updateShape();
	}

	@Override
	public List<FGEPoint> getPoints() {
		return points;
	}

	@Override
	public void setPoints(List<FGEPoint> points) {
		if (points != null) {
			this.points = new ArrayList<FGEPoint>(points);
			updateShape();
		} else {
			this.points = null;
		}
	}

	@Override
	public void addToPoints(FGEPoint aPoint) {
		points.add(aPoint);
		updateShape();
	}

	@Override
	public void removeFromPoints(FGEPoint aPoint) {
		points.remove(aPoint);
		updateShape();
	}

	@Override
	public FGEPolygon getShape() {
		return _polygon;
	}

	@Override
	public ShapeType getShapeType() {
		return ShapeType.CUSTOM_POLYGON;
	}

	@Override
	public void updateShape() {
		_polygon = new FGEPolygon(Filling.FILLED, points);
	}

}
