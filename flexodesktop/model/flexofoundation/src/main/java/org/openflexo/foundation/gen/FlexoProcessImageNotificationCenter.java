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
package org.openflexo.foundation.gen;

import org.openflexo.foundation.AttributeDataModification;
import org.openflexo.foundation.FlexoObservable;

/**
 * This class should be removed as PPM is deprecated.
 *
 */
@Deprecated
public class FlexoProcessImageNotificationCenter extends FlexoObservable {

	private static FlexoProcessImageNotificationCenter instance = new FlexoProcessImageNotificationCenter();

	public static FlexoProcessImageNotificationCenter getInstance() {
		return instance;
	}

	public void notifyNewImage() {
		setChanged();
		notifyObservers(new AttributeDataModification("displaySubProcessImage", null, null));
	}

	@Override
	public String getDeletedProperty() {
		// TODO Auto-generated method stub
		return null;
	}
}
