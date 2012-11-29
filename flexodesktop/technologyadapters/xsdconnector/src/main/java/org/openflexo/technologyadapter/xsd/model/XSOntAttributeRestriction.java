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
package org.openflexo.technologyadapter.xsd.model;

import org.openflexo.toolbox.StringUtils;

import com.sun.xml.xsom.XSAttributeUse;

public class XSOntAttributeRestriction extends XSOntRestriction {

	private final XSOntDataProperty attributeProperty;
	private final XSAttributeUse attributeUse;

	protected XSOntAttributeRestriction(XSOntology ontology, XSAttributeUse attributeUse) {
		super(ontology);
		String propertyURI = ontology.getFetcher().getUri(attributeUse.getDecl());
		this.attributeProperty = ontology.getDataProperty(propertyURI);
		this.attributeUse = attributeUse;
	}

	@Override
	public boolean isAttributeRestriction() {
		return true;
	}

	public XSAttributeUse getAttributeUse() {
		return attributeUse;
	}

	public XSOntDataProperty getAttributeProperty() {
		return attributeProperty;
	}

	public boolean hasDefaultValue() {
		return StringUtils.isNotEmpty(getDefaultValue());
	}

	public String getDefaultValue() {
		if (attributeUse.getDefaultValue() != null) {
			return attributeUse.getDefaultValue().toString();
		}
		return null;
	}

	public boolean hasFixedValue() {
		return StringUtils.isNotEmpty(getFixedValue());
	}

	public String getFixedValue() {
		if (attributeUse.getFixedValue() != null) {
			return attributeUse.getFixedValue().toString();
		}
		return null;
	}

	public boolean isRequired() {
		return attributeUse.isRequired();
	}

	@Override
	public String getDisplayableDescription() {
		StringBuffer buffer = new StringBuffer("Attribute ");
		buffer.append(attributeUse.getDecl().getName());
		buffer.append(" (").append(attributeProperty.getDataType().toString()).append(") is ");
		if (isRequired()) {
			buffer.append("required");
		} else {
			buffer.append("optional");
		}
		if (hasDefaultValue()) {
			buffer.append(", default: '").append(getDefaultValue()).append("'");
		}
		if (hasFixedValue()) {
			buffer.append(", fixed: '").append(getFixedValue()).append("'");
		}
		return buffer.toString();
	}

	@Override
	public String getClassNameKey() {
		return "XSD_ontology_attribute_restriction";
	}
}