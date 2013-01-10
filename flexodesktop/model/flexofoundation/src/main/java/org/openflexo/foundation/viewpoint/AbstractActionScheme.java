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

import org.openflexo.antar.binding.BindingModel;
import org.openflexo.antar.binding.BindingVariable;
import org.openflexo.antar.binding.DataBinding;
import org.openflexo.antar.binding.DataBinding.BindingDefinitionType;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.foundation.view.EditionPatternReference;
import org.openflexo.foundation.viewpoint.ViewPoint.ViewPointBuilder;

public abstract class AbstractActionScheme extends EditionScheme {

	private DataBinding<Boolean> conditional;

	public AbstractActionScheme(ViewPointBuilder builder) {
		super(builder);
	}

	public DataBinding<Boolean> getConditional() {
		if (conditional == null) {
			conditional = new DataBinding<Boolean>(this, Boolean.class, DataBinding.BindingDefinitionType.GET);
			conditional.setBindingName("conditional");
		}
		return conditional;
	}

	public void setConditional(DataBinding<Boolean> conditional) {
		if (conditional != null) {
			conditional.setOwner(this);
			conditional.setDeclaredType(Boolean.class);
			conditional.setBindingDefinitionType(DataBinding.BindingDefinitionType.GET);
			conditional.setBindingName("conditional");
		}
		this.conditional = conditional;
	}

	public boolean evaluateCondition(EditionPatternReference editionPatternReference) {
		if (getConditional().isValid()) {
			try {
				return getConditional().getBindingValue(editionPatternReference);
			} catch (TypeMismatchException e) {
				e.printStackTrace();
			} catch (NullReferenceException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	protected void appendContextualBindingVariables(BindingModel bindingModel) {
		super.appendContextualBindingVariables(bindingModel);
		/*bindingModel.addToBindingVariables(new EditionPatternPathElement<AbstractActionScheme>(EditionScheme.THIS, getEditionPattern(),
				this));*/
		bindingModel.addToBindingVariables(new BindingVariable(EditionScheme.THIS, getEditionPattern()));
	}

	@Override
	public void setEditionPattern(EditionPattern editionPattern) {
		super.setEditionPattern(editionPattern);
		updateBindingModels();
	}

}
