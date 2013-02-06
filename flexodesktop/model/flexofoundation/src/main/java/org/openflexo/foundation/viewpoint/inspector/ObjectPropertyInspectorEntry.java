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
package org.openflexo.foundation.viewpoint.inspector;

import java.lang.reflect.InvocationTargetException;

import org.openflexo.antar.binding.BindingEvaluationContext;
import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.binding.DataBinding.BindingDefinitionType;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.foundation.viewpoint.VirtualModel;

/**
 * Represents an inspector entry for an ontology object property
 * 
 * @author sylvain
 * 
 */
public class ObjectPropertyInspectorEntry extends PropertyInspectorEntry {

	private String rangeURI;

	private DataBinding<IFlexoOntologyClass> rangeValue;

	public ObjectPropertyInspectorEntry(VirtualModel.VirtualModelBuilder builder) {
		super(builder);
	}

	@Override
	public Class getDefaultDataClass() {
		return IFlexoOntologyObjectProperty.class;
	}

	@Override
	public String getWidgetName() {
		return "ObjectPropertySelector";
	}

	public String _getRangeURI() {
		return rangeURI;
	}

	public void _setRangeURI(String domainURI) {
		this.rangeURI = domainURI;
	}

	public IFlexoOntologyClass getRange() {
		return getVirtualModel().getOntologyClass(_getRangeURI());
	}

	public void setRange(IFlexoOntologyClass c) {
		_setRangeURI(c != null ? c.getURI() : null);
	}

	public DataBinding<IFlexoOntologyClass> getRangeValue() {
		if (rangeValue == null) {
			rangeValue = new DataBinding<IFlexoOntologyClass>(this, IFlexoOntologyClass.class, BindingDefinitionType.GET);
			rangeValue.setBindingName("rangeValue");
		}
		return rangeValue;
	}

	public void setRangeValue(DataBinding<IFlexoOntologyClass> rangeValue) {
		if (rangeValue != null) {
			rangeValue.setOwner(this);
			rangeValue.setBindingName("rangeValue");
			rangeValue.setDeclaredType(IFlexoOntologyClass.class);
			rangeValue.setBindingDefinitionType(BindingDefinitionType.GET);
		}
		this.rangeValue = rangeValue;
	}

	private boolean isDynamicRangeValueSet = false;

	public boolean getIsDynamicRangeValue() {
		return getRangeValue().isSet() || isDynamicRangeValueSet;
	}

	public void setIsDynamicRangeValue(boolean isDynamic) {
		if (isDynamic) {
			isDynamicRangeValueSet = true;
		} else {
			rangeValue = null;
			isDynamicRangeValueSet = false;
		}
	}

	public IFlexoOntologyClass evaluateRangeValue(BindingEvaluationContext parameterRetriever) {
		if (getRangeValue().isValid()) {
			try {
				return getRangeValue().getBindingValue(parameterRetriever);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
