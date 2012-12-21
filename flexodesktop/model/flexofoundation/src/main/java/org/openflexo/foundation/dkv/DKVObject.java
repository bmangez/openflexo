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
package org.openflexo.foundation.dkv;

import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.dkv.dm.DKVDataModification;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.rm.XMLStorageResourceData;
import org.openflexo.foundation.validation.Validable;
import org.openflexo.foundation.validation.ValidationModel;
import org.openflexo.foundation.validation.ValidationReport;

/**
 * @author gpolet
 * 
 */
public abstract class DKVObject extends FlexoModelObject implements Validable {

	protected DKVModel dkvModel;

	protected String name;

	/**
     *
     */
	public DKVObject(DKVModel dl) {
		super(dl.getProject());
		this.dkvModel = dl;
	}

	// Should not be called by other object than DKVModel
	public DKVObject(FlexoProject project) {
		super(project);
	}

	public abstract boolean isDeleteAble();

	/**
	 * Overrides getXMLResourceData
	 * 
	 * @see org.openflexo.foundation.FlexoXMLSerializableObject#getXMLResourceData()
	 */
	@Override
	public XMLStorageResourceData getXMLResourceData() {
		return dkvModel;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) throws DuplicateDKVObjectException {
		String old = this.name;
		this.name = name;
		setChanged();
		notifyObservers(new DKVDataModification("name", old, name));
	}

	public DKVModel getDkvModel() {
		return dkvModel;
	}

	public DKVObject getParent() {
		if (this instanceof Value) {
			return ((Value) this).getKey();
		} else if (this instanceof Key) {
			return ((Key) this).getDomain();
		} else if (this instanceof Domain) {
			return ((Domain) this).getDkvModel().getDomainList();
		}
		return null;
	}

	@Override
	public ValidationModel getDefaultValidationModel() {
		return getProject().getDKVValidationModel();
	}

	@Override
	public boolean isValid() {
		return isValid(getDefaultValidationModel());
	}

	@Override
	public boolean isValid(ValidationModel validationModel) {
		return validationModel.isValid(this);
	}

	@Override
	public ValidationReport validate() {
		return validate(getDefaultValidationModel());
	}

	@Override
	public ValidationReport validate(ValidationModel validationModel) {
		return validationModel.validate(this);
	}

	@Override
	public void validate(ValidationReport report) {
		validate(report, getDefaultValidationModel());
	}

	@Override
	public void validate(ValidationReport report, ValidationModel validationModel) {
		validationModel.validate(this, report);
	}

	/**
	 * Returns fully qualified name for this object
	 * 
	 * @return
	 */
	@Override
	public abstract String getFullyQualifiedName();
}
