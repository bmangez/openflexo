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
package org.openflexo.foundation.viewpoint;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.antar.binding.AbstractBinding;
import org.openflexo.antar.binding.Bindable;
import org.openflexo.antar.binding.BindingDefinition;
import org.openflexo.antar.binding.BindingFactory;
import org.openflexo.antar.binding.BindingModelChanged;
import org.openflexo.foundation.validation.FixProposal;
import org.openflexo.foundation.validation.ValidationError;
import org.openflexo.foundation.validation.ValidationIssue;
import org.openflexo.foundation.validation.ValidationRule;
import org.openflexo.foundation.viewpoint.binding.ViewPointDataBinding;
import org.openflexo.foundation.viewpoint.inspector.InspectorBindingAttribute;

/**
 * Represents an object which is part of the model of a ViewPoint
 * 
 * @author sylvain
 * 
 */
public abstract class ViewPointObject extends ViewPointLibraryObject implements Bindable {

	private static final Logger logger = Logger.getLogger(ViewPointObject.class.getPackage().getName());

	public abstract ViewPoint getViewPoint();

	@Override
	public ViewPointLibrary getViewPointLibrary() {
		if (getViewPoint() != null) {
			return getViewPoint().getViewPointLibrary();
		}
		return null;
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (getViewPoint() != null) {
			getViewPoint().setIsModified();
		}
	}

	public void notifyBindingChanged(ViewPointDataBinding binding) {
		if (getPropertyChangeSupport() != null) {
			if (binding != null && binding.getBindingAttribute() != null) {
				getPropertyChangeSupport().firePropertyChange(binding.getBindingAttribute().toString(), null, binding);
			}
		}
	}

	public void notifyChange(String propertyName, Object oldValue, Object newValue) {
		if (getPropertyChangeSupport() != null) {
			getPropertyChangeSupport().firePropertyChange(propertyName, oldValue, newValue);
		}
	}

	public void notifyChange(InspectorBindingAttribute bindingAttribute, AbstractBinding oldValue, AbstractBinding newValue) {
		if (getPropertyChangeSupport() != null) {
			if (bindingAttribute != null) {
				getPropertyChangeSupport().firePropertyChange(bindingAttribute.toString(), oldValue, newValue);
			}
		}
	}

	@Override
	public BindingFactory getBindingFactory() {
		if (getViewPoint() != null) {
			return getViewPoint().getBindingFactory();
		}
		return null;
	}

	public void notifyBindingModelChanged() {
		getPropertyChangeSupport().firePropertyChange(BindingModelChanged.BINDING_MODEL_CHANGED, null, null);
	}

	public LocalizedDictionary getLocalizedDictionary() {
		return getViewPoint().getLocalizedDictionary();
	}

	@Deprecated
	public ViewPoint getCalc() {
		return getViewPoint();
	}

	@Override
	public String getInspectorName() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Return a vector of all embedded objects on which the validation will be performed
	 * 
	 * @return a Vector of Validable objects
	 */
	/*@Override
	public Vector<Validable> getAllEmbeddedValidableObjects() {
		return new Vector<Validable>(getAllEmbeddedViewPointObjects());
	}*/

	public final Vector<ViewPointObject> getAllEmbeddedViewPointObjects() {
		return null;
	}

	@Override
	public String getFullyQualifiedName() {
		return (getViewPoint() != null ? getViewPoint().getFullyQualifiedName() : "null") + "#" + getClass().getSimpleName();
	}

	public static abstract class BindingMustBeValid<C extends ViewPointObject> extends ValidationRule<BindingMustBeValid<C>, C> {
		public BindingMustBeValid(String ruleName, Class<C> clazz) {
			super(clazz, ruleName);
		}

		public abstract ViewPointDataBinding getBinding(C object);

		public abstract BindingDefinition getBindingDefinition(C object);

		@Override
		public ValidationIssue<BindingMustBeValid<C>, C> applyValidation(C object) {
			if (getBinding(object) != null && getBinding(object).isSet()) {
				if (!getBinding(object).isValid()) {
					logger.info("Binding NOT valid: " + getBinding(object) + " for " + object.getFullyQualifiedName() + ". Reason follows.");
					getBinding(object).getBinding().debugIsBindingValid();
					DeleteBinding<C> deleteBinding = new DeleteBinding<C>(this);
					return new ValidationError(this, object, BindingMustBeValid.this.getNameKey(), deleteBinding);
				}
			}
			return null;
		}

		protected static class DeleteBinding<C extends ViewPointObject> extends FixProposal<BindingMustBeValid<C>, C> {

			private BindingMustBeValid rule;

			public DeleteBinding(BindingMustBeValid rule) {
				super("delete_this_binding");
				this.rule = rule;
			}

			@Override
			protected void fixAction() {
				rule.getBinding(getObject()).setBinding(null);
			}

		}
	}

	public static abstract class BindingIsRequiredAndMustBeValid<C extends ViewPointObject> extends
			ValidationRule<BindingIsRequiredAndMustBeValid<C>, C> {
		public BindingIsRequiredAndMustBeValid(String ruleName, Class<C> clazz) {
			super(clazz, ruleName);
		}

		public abstract ViewPointDataBinding getBinding(C object);

		public abstract BindingDefinition getBindingDefinition(C object);

		@Override
		public ValidationIssue<BindingIsRequiredAndMustBeValid<C>, C> applyValidation(C object) {
			if (getBinding(object) == null || !getBinding(object).isSet()) {
				return new ValidationError(this, object, BindingIsRequiredAndMustBeValid.this.getNameKey());
			} else if (!getBinding(object).isValid()) {
				logger.info("Binding NOT valid: " + getBinding(object) + " for " + object.getFullyQualifiedName() + ". Reason follows.");
				getBinding(object).getBinding().debugIsBindingValid();
				return new ValidationError(this, object, BindingIsRequiredAndMustBeValid.this.getNameKey());
			}
			return null;
		}
	}

}
