package org.openflexo.technologyadapter.emf.model;

import java.io.File;

import org.openflexo.foundation.TemporaryFlexoModelObject;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.ontology.OntologyLibrary;
import org.openflexo.foundation.rm.SaveResourceException;
import org.openflexo.foundation.technologyadapter.FlexoMetaModel;

public class EMFMetaModel extends TemporaryFlexoModelObject implements FlexoMetaModel, IFlexoOntology {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger.getLogger(EMFMetaModel.class.getPackage()
			.getName());

	public EMFMetaModel(String ontologyURI, File xsdFile, OntologyLibrary library) {
		super();
	}

	@Override
	public boolean isReadOnly() {
		return true;
	}

	@Override
	public void setIsReadOnly(boolean b) {
	}

	@Override
	public void save() throws SaveResourceException {
		logger.warning("EMF Meta Models are not supposed to be saved !!!");
	}

}
