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
package org.openflexo.technologyadapter.owl.rm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.ontology.dm.OntologyImported;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.rm.DuplicateResourceException;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.rm.FlexoProjectBuilder;
import org.openflexo.foundation.rm.FlexoStorageResource;
import org.openflexo.foundation.rm.InvalidFileNameException;
import org.openflexo.foundation.rm.LoadResourceException;
import org.openflexo.foundation.rm.ResourceType;
import org.openflexo.foundation.rm.SaveResourceException;
import org.openflexo.foundation.rm.SaveResourcePermissionDeniedException;
import org.openflexo.foundation.utils.FlexoProgress;
import org.openflexo.foundation.utils.FlexoProjectFile;
import org.openflexo.foundation.utils.ProjectLoadingHandler;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.model.OntologyLibrary;

/**
 * Represents the resource associated to a {@link OWLOntology}
 * 
 * @author sguerin
 * 
 */
public class OWLOntologyResource extends FlexoStorageResource<OWLOntology> implements FlexoResource<OWLOntology> {

	private static final Logger logger = Logger.getLogger(OWLOntologyResource.class.getPackage().getName());

	private OntologyLibrary ontologyLibrary = null;

	/**
	 * Constructor used for XML Serialization: never try to instanciate resource from this constructor
	 * 
	 * @param builder
	 */
	public OWLOntologyResource(FlexoProjectBuilder builder) {
		this(builder.project);
		builder.notifyResourceLoading(this);
	}

	public OWLOntologyResource(FlexoProject aProject) {
		super(aProject);
	}

	public OntologyLibrary getOntologyLibrary() {
		return ontologyLibrary;
	}

	public void setOntologyLibrary(OntologyLibrary ontologyLibrary) {
		this.ontologyLibrary = ontologyLibrary;
	}

	/*public OWLOntologyResource(FlexoProject aProject, FlexoDMResource dmResource, FlexoProjectFile eoModelFile)
	        throws InvalidFileNameException
	{
	    this(aProject);
	    setResourceFile(eoModelFile);
	    addToSynchronizedResources(dmResource);
	    if (logger.isLoggable(Level.INFO))
	        logger.info("Build new FlexoEOModelResource");
	}*/

	public OWLOntologyResource(FlexoProject aProject, OWLOntology anOntology, FlexoProjectFile ontologyFile)
			throws InvalidFileNameException, DuplicateResourceException {
		this(aProject);
		_resourceData = anOntology;
		anOntology.setFlexoResource(this);
		setResourceFile(ontologyFile);
	}

	@Override
	public ResourceType getResourceType() {
		return ResourceType.PROJECT_ONTOLOGY;
	}

	@Override
	public String getName() {
		return getProject().getProjectName();
	}

	@Override
	public Class getResourceDataClass() {
		return OWLOntology.class;
	}

	@Override
	public void setName(String aName) {
		// Not allowed
	}

	@Override
	public OWLOntology performLoadResourceData(FlexoProgress progress, ProjectLoadingHandler loadingHandler) throws LoadResourceException {

		OWLOntology ontology = new OWLOntology(getURI(), getFile(), getOntologyLibrary());
		getOntologyLibrary().registerOntology(ontology);
		setChanged();
		notifyObservers(new OntologyImported(ontology));

		_resourceData = ontology;

		try {
			_resourceData.setFlexoResource(this);
		} catch (DuplicateResourceException e) {
			e.printStackTrace();
			logger.warning("Should not happen");
		}
		notifyResourceStatusChanged();
		return _resourceData;
	}

	/**
	 * Implements
	 * 
	 * @see org.openflexo.foundation.rm.FlexoResource#saveResourceData()
	 * @see org.openflexo.foundation.rm.FlexoResource#saveResourceData()
	 */
	@Override
	protected void saveResourceData(boolean clearIsModified) throws SaveResourceException {
		if (!hasWritePermission()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Permission denied : " + getFile().getAbsolutePath());
			}
			throw new SaveResourcePermissionDeniedException(this);
		}
		if (_resourceData != null) {
			FileWritingLock lock = willWriteOnDisk();
			_writeToFile();
			hasWrittenOnDisk(lock);
			notifyResourceStatusChanged();
			if (logger.isLoggable(Level.INFO)) {
				logger.info("Succeeding to save Resource " + getResourceIdentifier() + " : " + getFile().getName());
			}
		}
		if (clearIsModified) {
			getResourceData().clearIsModified(false);
		}
	}

	public void _writeToFile() throws SaveResourceException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(getFile());
			_resourceData.getOntModel().write(out, null, _resourceData.getOntologyURI());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SaveResourceException(this);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw new SaveResourceException(this);
			}
		}

		logger.info("Wrote " + getFile());
	}

}