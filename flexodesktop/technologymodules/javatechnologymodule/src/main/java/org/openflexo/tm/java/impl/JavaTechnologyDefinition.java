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
package org.openflexo.tm.java.impl;

import org.openflexo.foundation.sg.implmodel.ImplementationModel;
import org.openflexo.foundation.sg.implmodel.TechnologyModuleDefinition;
import org.openflexo.foundation.sg.implmodel.TechnologyModuleImplementation;
import org.openflexo.foundation.sg.implmodel.exception.TechnologyModuleCompatibilityCheckException;
import org.openflexo.foundation.sg.implmodel.exception.TechnologyModuleInitializationException;
import org.openflexo.sg.formatter.FormatterFactory;
import org.openflexo.tm.java.formatter.JavaFormatter;

/**
 * @author nid
 * 
 */
public class JavaTechnologyDefinition extends TechnologyModuleDefinition {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TechnologyModuleImplementation createNewImplementation(ImplementationModel implementationModel)
			throws TechnologyModuleCompatibilityCheckException {
		JavaImplementation javaImplementation = new JavaImplementation(implementationModel);
		return javaImplementation;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadModule() throws TechnologyModuleInitializationException {
		super.loadModule();
		FormatterFactory.recordFormatter(new JavaFormatter());
	}
}
