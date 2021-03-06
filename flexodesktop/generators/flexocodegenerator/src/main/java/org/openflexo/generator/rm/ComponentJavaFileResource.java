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
package org.openflexo.generator.rm;

import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.FlexoObservable;
import org.openflexo.foundation.FlexoObserver;
import org.openflexo.foundation.ie.ComponentInstance;
import org.openflexo.foundation.ie.cl.ComponentDefinition;
import org.openflexo.foundation.ie.dm.ComponentDeleted;
import org.openflexo.foundation.ie.dm.ComponentNameChanged2;
import org.openflexo.foundation.rm.FlexoDMResource;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.rm.FlexoProjectBuilder;
import org.openflexo.foundation.rm.FlexoResource;
import org.openflexo.foundation.rm.cg.JavaFileResource;
import org.openflexo.generator.FlexoComponentResourceGenerator;
import org.openflexo.generator.TemplateLocator;
import org.openflexo.generator.cg.CGJavaFile;
import org.openflexo.generator.ie.ComponentGenerator;
import org.openflexo.logging.FlexoLogger;

/**
 * @author sylvain
 * 
 */
public abstract class ComponentJavaFileResource<G extends FlexoComponentResourceGenerator> extends JavaFileResource<G, CGJavaFile>
		implements GenerationAvailableFileResource, ComponentFileResource, FlexoObserver {
	protected static final Logger logger = FlexoLogger.getLogger(ComponentJavaFileResource.class.getPackage().getName());
	private boolean isObserverRegistered = false;

	/**
	 * @param builder
	 */
	public ComponentJavaFileResource(FlexoProjectBuilder builder) {
		super(builder);
	}

	/**
	 * @param aProject
	 */
	public ComponentJavaFileResource(FlexoProject aProject) {
		super(aProject);
	}

	@Override
	protected ComponentJavaFile createGeneratedResourceData() {
		return new ComponentJavaFile(getFile(), this);
	}

	@Override
	public ComponentJavaFile getGeneratedResourceData() {
		return (ComponentJavaFile) super.getGeneratedResourceData();
	}

	@Override
	public ComponentDefinition getComponentDefinition() {
		if (getGenerator() != null) {
			return getGenerator().getComponentDefinition();
		}
		return null;
	}

	@Override
	public void registerObserverWhenRequired() {
		if ((!isObserverRegistered) && (getComponentDefinition() != null)) {
			isObserverRegistered = true;
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("*** addObserver " + getFileName() + " for " + getComponentDefinition());
			}
			getComponentDefinition().addObserver(this);
		}
	}

	/**
	 * Return dependancy computing between this resource, and an other resource, asserting that this resource is contained in this
	 * resource's dependant resources
	 * 
	 * @param resource
	 * @param dependancyScheme
	 * @return
	 */
	@Override
	public boolean optimisticallyDependsOf(FlexoResource resource, Date requestDate) {
		if (resource instanceof TemplateLocator) {
			return ((TemplateLocator) resource).needsUpdateForResource(this);
		} else if (resource instanceof FlexoDMResource) {
			FlexoDMResource dmRes = (FlexoDMResource) resource;
			if (dmRes.isLoaded() && getComponentDefinition() != null && getComponentDefinition().getComponentDMEntity() != null) {
				if (!requestDate.before(getComponentDefinition().getComponentDMEntity().getLastUpdate())) {
					if (logger.isLoggable(Level.FINER)) {
						logger.finer("OPTIMIST DEPENDANCY CHECKING for JAVA COMPONENT " + getComponentDefinition().getName());
					}
					return false;
				}
			}
		}
		return super.optimisticallyDependsOf(resource, requestDate);
	}

	/**
	 * Rebuild resource dependancies for this resource
	 */
	@Override
	public void rebuildDependancies() {
		super.rebuildDependancies();

		if (getComponentDefinition() != null) {

			addToDependentResources(getProject().getFlexoDMResource());
			addToDependentResources(getComponentDefinition().getComponentResource());

			if (getComponentDefinition().getWOComponent() != null) {
				for (Enumeration en = getComponentDefinition().getWOComponent().getAllComponentInstances().elements(); en.hasMoreElements();) {
					ComponentInstance ci = (ComponentInstance) en.nextElement();
					if (ci.getComponentDefinition() != null) {
						if (logger.isLoggable(Level.FINE)) {
							logger.fine("Found dependancy between " + this + " and " + ci.getComponentDefinition().getComponentResource());
						}
						addToDependentResources(ci.getComponentDefinition().getComponentResource());
					} else {
						if (logger.isLoggable(Level.WARNING)) {
							logger.warning("Inconsistant data: ComponentInstance refers to an unknown ComponentDefinition "
									+ ci.getComponentName());
						}
					}
				}
			}

		}
	}

	@Override
	public void update(FlexoObservable observable, DataModification dataModification) {
		if (observable == getComponentDefinition()) {
			if (dataModification instanceof ComponentNameChanged2) {
				logger.info("Building new resource after renaming");
				ComponentGenerator generator = (ComponentGenerator) getGenerator();
				setGenerator(null);
				getCGFile().setMarkedForDeletion(true);
				generator.refreshConcernedResources();
				generator.generate(true);
				generator.getRepository().refresh();
				observable.deleteObserver(this);
				isObserverRegistered = false;
			} else if (dataModification instanceof ComponentDeleted) {
				logger.info("Handle component has been deleted");
				setGenerator(null);
				getCGFile().setMarkedForDeletion(true);
				getCGFile().getRepository().refresh();
				observable.deleteObserver(this);
				isObserverRegistered = false;
			}
		}
	}
}
