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
package org.openflexo.fge.geomedit.construction;

import java.util.Vector;

import org.openflexo.fge.geom.FGEGeometricObject.Filling;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.geom.FGEPolygon;

public class PolygonWithNPointsConstruction extends PolygonConstruction {

	public Vector<PointConstruction> pointConstructions;

	public PolygonWithNPointsConstruction() {
		super();
		pointConstructions = new Vector<PointConstruction>();
	}

	public PolygonWithNPointsConstruction(Vector<PointConstruction> somePointConstructions) {
		this();
		this.pointConstructions.addAll(somePointConstructions);
	}

	@Override
	protected FGEPolygon computeData() {
		Vector<FGEPoint> pts = new Vector<FGEPoint>();
		for (PointConstruction pc : pointConstructions) {
			pts.add(pc.getData());
		}
		return new FGEPolygon((getIsFilled() ? Filling.FILLED : Filling.NOT_FILLED), pts);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("PolygonWithNPointsConstruction[\n");
		for (PointConstruction pc : pointConstructions) {
			sb.append("> " + pc.toString() + "\n");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public GeometricConstruction[] getDepends() {
		return pointConstructions.toArray(new GeometricConstruction[pointConstructions.size()]);
	}

}
