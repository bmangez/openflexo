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
package org.openflexo.generator.utils;

import java.util.Enumeration;
import java.util.logging.Logger;

import org.apache.velocity.VelocityContext;
import org.openflexo.foundation.rm.cg.JavaFileResource;
import org.openflexo.foundation.wkf.FlexoProcess;
import org.openflexo.generator.ProjectGenerator;
import org.openflexo.logging.FlexoLogger;

/**
 * @author gpolet
 * 
 */
public class ConstantsGenerator extends JavaClassGenerator {
	private static final String TEMPLATE_NAME = "Constants.java.vm";
	private static final Logger logger = FlexoLogger.getLogger(ConstantsGenerator.class.getPackage().getName());

	/**
	 * @param projectGenerator
	 */
	public ConstantsGenerator(ProjectGenerator projectGenerator) {
		super(projectGenerator, projectGenerator.getPrefix() + "Constants", "");
	}

	/**
	 * Overrides getGeneratorLogger
	 * 
	 * @see org.openflexo.generator.CGGenerator#getGeneratorLogger()
	 */
	@Override
	public Logger getGeneratorLogger() {
		return logger;
	}

	@Override
	protected VelocityContext defaultContext() {
		VelocityContext vc = super.defaultContext();
		vc.put("prefix", getPrefix());
		return vc;
	}

	/**
	 * Overrides rebuildDependanciesForResource
	 * 
	 * @see org.openflexo.generator.utils.JavaClassGenerator#rebuildDependanciesForResource(JavaFileResource)
	 */
	@Override
	public void rebuildDependanciesForResource(JavaFileResource resource) {
		resource.addToDependentResources(getProject().getFlexoDKVResource());
		resource.addToDependentResources(getProject().getFlexoWorkflowResource());
		Enumeration<FlexoProcess> en = getProject().getAllLocalFlexoProcesses().elements();
		while (en.hasMoreElements()) {
			resource.addToDependentResources(en.nextElement().getFlexoResource());
		}
	}

	@Override
	public String getTemplateName() {
		return TEMPLATE_NAME;
	}
}
