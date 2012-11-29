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
package org.openflexo.technologyadapter.owl;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.ontology.OntologyLibrary;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.rm.DuplicateResourceException;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.rm.InvalidFileNameException;
import org.openflexo.foundation.rm.ProjectRestructuration;
import org.openflexo.foundation.technologyadapter.DeclareEditionAction;
import org.openflexo.foundation.technologyadapter.DeclareEditionActions;
import org.openflexo.foundation.technologyadapter.DeclarePatternRole;
import org.openflexo.foundation.technologyadapter.DeclarePatternRoles;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterInitializationException;
import org.openflexo.foundation.utils.FlexoProjectFile;
import org.openflexo.foundation.viewpoint.ClassPatternRole;
import org.openflexo.foundation.viewpoint.DataPropertyPatternRole;
import org.openflexo.foundation.viewpoint.DeleteAction;
import org.openflexo.foundation.viewpoint.IndividualPatternRole;
import org.openflexo.foundation.viewpoint.ObjectPropertyPatternRole;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.technologyadapter.owl.model.OWLMetaModelRepository;
import org.openflexo.technologyadapter.owl.model.OWLModelRepository;
import org.openflexo.technologyadapter.owl.model.OWLOntology;
import org.openflexo.technologyadapter.owl.rm.OWLOntologyResource;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddOWLClass;
import org.openflexo.technologyadapter.owl.viewpoint.editionaction.AddOWLIndividual;

/**
 * This class defines and implements the OWL technology adapter
 * 
 * @author sylvain, luka
 * 
 */
@DeclarePatternRoles({
/** Instances */
@DeclarePatternRole(IndividualPatternRole.class),
/** Classes */
@DeclarePatternRole(ClassPatternRole.class),
/** Data properties */
@DeclarePatternRole(DataPropertyPatternRole.class),
/** Object properties */
@DeclarePatternRole(ObjectPropertyPatternRole.class) })
@DeclareEditionActions({
/** Add instance */
@DeclareEditionAction(AddOWLIndividual.class),
/** Add class */
@DeclareEditionAction(AddOWLClass.class),
/** Add class */
@DeclareEditionAction(DeleteAction.class) })
public class OWLTechnologyAdapter extends TechnologyAdapter<OWLOntology, OWLOntology, OWLModelSlot> {

	private static final Logger logger = Logger.getLogger(OWLTechnologyAdapter.class.getPackage().getName());

	public OWLTechnologyAdapter() throws TechnologyAdapterInitializationException {
	}

	@Override
	public String getName() {
		return "OWL technology adapter";
	}

	@Override
	protected OWLModelSlot createNewModelSlot(ViewPoint viewPoint) {
		return new OWLModelSlot(viewPoint, this);
	}

	/**
	 * Return flag indicating if supplied file represents a valid XSD schema
	 * 
	 * @param aMetaModelFile
	 * @return
	 */
	@Override
	public boolean isValidMetaModelFile(File aMetaModelFile) {
		// TODO: also check that file is valid and maps a valid XSD schema
		return aMetaModelFile.isFile() && aMetaModelFile.getName().endsWith(".owl");
	}

	/**
	 * Retrieve and return URI for supplied meta model file, if supplied file represents a valid XSD meta model
	 * 
	 * @param aMetaModelFile
	 * @return
	 */
	@Override
	public String retrieveMetaModelURI(File aMetaModelFile) {
		return OWLOntology.findOntologyURI(aMetaModelFile);
	}

	/**
	 * Return flag indicating if supplied file represents a valid XML model conform to supplied meta-model
	 * 
	 * @param aModelFile
	 * @param metaModel
	 * @return
	 */
	@Override
	public boolean isValidModelFile(File aModelFile, OWLOntology metaModel) {
		// TODO: also check that file is valid and maps a valid XML model conform to supplied meta-model
		return aModelFile.getName().endsWith(".owl");
	}

	/**
	 * Instantiate new meta model stored in supplied meta model file
	 * 
	 * @param aMetaModelFile
	 * @return
	 */
	@Override
	public OWLMetaModel loadMetaModel(File aMetaModelFile, OntologyLibrary library) {
		return new OWLMetaModel(retrieveMetaModelURI(aMetaModelFile), aMetaModelFile, library);
	}

	@Override
	public OWLOntology createNewModel(FlexoProject project, OWLOntology metaModel) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("createNewOWLModel(), project=" + project);
		}
		logger.info("-------------> Create ontology for " + project.getProjectName());
		File owlFile = ProjectRestructuration.getExpectedProjectOntologyFile(project, project.getProjectName());
		FlexoProjectFile ontologyFile = new FlexoProjectFile(owlFile, project);

		OWLOntology newProjectOntology = createNewOWLModel(project.getURI(), owlFile, project.getProjectOntologyLibrary());
		project.getProjectOntologyLibrary().registerOntology(newProjectOntology);

		OWLOntologyResource ontologyRes;
		try {
			ontologyRes = new OWLOntologyResource(project, newProjectOntology, ontologyFile);
		} catch (InvalidFileNameException e) {
			e.printStackTrace();
			if (logger.isLoggable(Level.SEVERE)) {
				logger.severe("This should not happen: invalid file " + ontologyFile);
			}
			return null;
		} catch (DuplicateResourceException e) {
			e.printStackTrace();
			if (logger.isLoggable(Level.SEVERE)) {
				logger.severe("This should not happen: DuplicateResourceException for " + ontologyFile);
			}
			return null;
		}
		// newDMModel.initializeDefaultRepositories(dmRes);
		try {
			// dmRes.saveResourceData();
			project.registerResource(ontologyRes);
		} catch (Exception e1) {
			// Warns about the exception
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Exception raised: " + e1.getClass().getName() + ". See console for details.");
			}
			e1.printStackTrace();
		}

		try {
			ontologyRes.saveResourceData();
		} catch (Exception e1) {
			// Warns about the exception
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Exception raised: " + e1.getClass().getName() + ". See console for details.");
			}
			e1.printStackTrace();
		}

		return newProjectOntology;
	}

	@Override
	public OWLModelRepository createModelRepository(FlexoResourceCenter resourceCenter) {
		return null;
	}

	@Override
	public OWLMetaModelRepository createMetaModelRepository(FlexoResourceCenter resourceCenter) {
		return null;
	}

}