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
package org.openflexo.foundation.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InvalidNameException;

import org.openflexo.foundation.Inspectors;
import org.openflexo.foundation.ontology.EditionPatternInstance;
import org.openflexo.foundation.ontology.EditionPatternReference;
import org.openflexo.foundation.ontology.dm.ShemaDeleted;
import org.openflexo.foundation.rm.DuplicateResourceException;
import org.openflexo.foundation.rm.FlexoOEShemaResource;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.rm.FlexoResource;
import org.openflexo.foundation.rm.FlexoXMLStorageResource;
import org.openflexo.foundation.rm.SaveResourceException;
import org.openflexo.foundation.rm.XMLStorageResourceData;
import org.openflexo.foundation.viewpoint.EditionPattern;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.xml.VEShemaBuilder;
import org.openflexo.xmlcode.XMLMapping;

public class View extends ViewObject implements XMLStorageResourceData {

	private static final Logger logger = Logger.getLogger(View.class.getPackage().getName());

	private FlexoProject _project;
	private FlexoOEShemaResource _resource;
	private ViewDefinition _shemaDefinition;
	private ViewPoint _calc;

	/**
	 * Constructor invoked during deserialization
	 * 
	 * @param componentDefinition
	 */
	public View(VEShemaBuilder builder) {
		this(builder.shemaDefinition, builder.getProject());
		builder.shema = this;
		initializeDeserialization(builder);
	}

	/**
	 * Default constructor for OEShema
	 * 
	 * @param shemaDefinition
	 */
	public View(ViewDefinition shemaDefinition, FlexoProject project) {
		super(project);
		logger.info("Created new shema with project " + project);
		_project = project;
		_shemaDefinition = shemaDefinition;
		setShema(this);
		if (getCalc() != null) {
			getCalc().loadWhenUnloaded();
		}
	}

	public Collection<EditionPatternInstance> getEPInstances(String epName) {
		System.out.println("Search " + epName);
		EditionPattern ep = getCalc().getEditionPattern(epName);
		System.out.println("Found " + ep);
		return getEPInstances(ep);
	}

	public Collection<EditionPatternInstance> getEPInstances(EditionPattern ep) {
		Collection<ViewShape> shapes = getChildrenOfType(ViewShape.class);
		Collection<ViewConnector> connectors = getChildrenOfType(ViewConnector.class);
		Collection<EditionPatternInstance> epis = new LinkedHashSet<EditionPatternInstance>();
		for (ViewShape shape : shapes) {
			EditionPatternReference epr = shape.getEditionPatternReference();
			if (epr == null) {
				continue;
			}
			if (/*epr.isPrimaryRole() && */epr.getEditionPattern() == ep) {
				epis.add(epr.getEditionPatternInstance());
			}
		}
		for (ViewConnector conn : connectors) {
			EditionPatternReference epr = conn.getEditionPatternReference();
			if (epr == null) {
				continue;
			}
			if (/*epr.isPrimaryRole() && */epr.getEditionPattern() == ep) {
				epis.add(epr.getEditionPatternInstance());
			}
		}
		System.out.println("Return " + epis);
		return epis;
	}

	public List<EditionPatternInstance> getEPInstancesWithPropertyEqualsTo(String epName, String epProperty, Object value) {
		List<EditionPatternInstance> returned = new ArrayList<EditionPatternInstance>();
		Collection<EditionPatternInstance> epis = getEPInstances(epName);
		for (EditionPatternInstance epi : epis) {
			Object evaluate = epi.evaluate(epProperty);
			if (value == null && evaluate == value || value != null && value.equals(evaluate)) {
				returned.add(epi);
			}
		}
		return returned;
	}

	public ViewDefinition getShemaDefinition() {
		return _shemaDefinition;
	}

	@Override
	public FlexoOEShemaResource getFlexoResource() {
		return _resource;
	}

	@Override
	public FlexoXMLStorageResource getFlexoXMLFileResource() {
		return getFlexoResource();
	}

	@Override
	public void setFlexoResource(FlexoResource resource) throws DuplicateResourceException {
		_resource = (FlexoOEShemaResource) resource;
	}

	@Override
	public void save() throws SaveResourceException {
		getFlexoResource().saveResourceData();
	}

	@Override
	public FlexoProject getProject() {
		return _project;
	}

	@Override
	public void setProject(FlexoProject aProject) {
		_project = aProject;
	}

	@Override
	public String getClassNameKey() {
		return "oe_shema";
	}

	@Override
	public String getName() {
		if (getShemaDefinition() != null) {
			return getShemaDefinition().getName();
		}
		return null;
	}

	// TODO: big issue with renaming, don't call this !!!
	@Override
	public void setName(String name) throws DuplicateResourceException, InvalidNameException {
		if (getShemaDefinition() != null) {
			getShemaDefinition().setName(name);
		}
	}

	public String getTitle() {
		if (getShemaDefinition() != null) {
			return getShemaDefinition().getTitle();
		}
		return null;
	}

	public void setTitle(String title) throws DuplicateResourceException, InvalidNameException {
		if (getShemaDefinition() != null) {
			getShemaDefinition().setTitle(title);
		}
	}

	@Override
	public String getDescription() {
		if (isSerializing()) {
			return null;
		}
		if (getShemaDefinition() != null) {
			return getShemaDefinition().getDescription();
		}
		return super.getDescription();
	}

	@Override
	public void setDescription(String description) {
		if (getShemaDefinition() != null) {
			getShemaDefinition().setDescription(description);
		}
		super.setDescription(description);
	}

	@Override
	public Map<String, String> getSpecificDescriptions() {
		if (isSerializing()) {
			return null;
		}
		if (getShemaDefinition() != null) {
			return getShemaDefinition().getSpecificDescriptions();
		}
		return super.getSpecificDescriptions();
	}

	@Override
	public boolean getHasSpecificDescriptions() {
		if (getShemaDefinition() != null) {
			return getShemaDefinition().getHasSpecificDescriptions();
		}
		return super.getHasSpecificDescriptions();
	}

	@Override
	public String getFullyQualifiedName() {
		return getProject().getFullyQualifiedName() + "." + getShemaDefinition().getName();
	}

	@Override
	public XMLMapping getXMLMapping() {
		return getProject().getXmlMappings().getShemaMapping();
	}

	@Override
	public String getInspectorName() {
		return Inspectors.VE.OE_SHEMA_INSPECTOR;
	}

	public ViewPoint getViewPoint() {
		return getCalc();
	}

	public ViewPoint getCalc() {
		if (getShemaDefinition() != null) {
			return getShemaDefinition().getCalc();
		}
		return null;
	}

	@Override
	public boolean isContainedIn(ViewObject o) {
		return o == this;
	}

	@Override
	public String getDisplayableDescription() {
		return "Shema " + getName() + (getCalc() != null ? " (calc " + getCalc().getName() + ")" : "");
	}

	/**
	 * @return
	 */
	public static final String getTypeName() {
		return "SHEMA";
	}

	@Override
	public String toString() {
		return "View[name=" + getName() + "/viewpoint=" + getCalc().getName() + "/hash=" + Integer.toHexString(hashCode()) + "]";
	}

	// ==========================================================================
	// ================================= Delete ===============================
	// ==========================================================================

	@Override
	public final void delete() {
		// tests on this deleted object
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("delete: View " + getName());
		}
		if (getFlexoResource() != null) {
			getFlexoResource().delete();
		}

		if (getShemaDefinition() != null) {
			getShemaDefinition().delete();
		} else {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("View " + getName() + " has no ViewDefinition associated!");
			}
		}

		super.delete();

		setChanged();
		notifyObservers(new ShemaDeleted(this.getShemaDefinition()));
		deleteObservers();
	}

}
