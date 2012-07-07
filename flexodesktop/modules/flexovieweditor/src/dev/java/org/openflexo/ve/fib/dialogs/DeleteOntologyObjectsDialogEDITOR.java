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
package org.openflexo.ve.fib.dialogs;

import java.io.File;
import java.util.Vector;

import org.openflexo.fib.editor.FIBAbstractEditor;
import org.openflexo.foundation.FlexoResourceCenter;
import org.openflexo.foundation.ontology.FlexoOntology;
import org.openflexo.foundation.ontology.OntologyLibrary;
import org.openflexo.foundation.ontology.OntologyObject;
import org.openflexo.foundation.ontology.action.DeleteOntologyObjects;
import org.openflexo.module.FlexoResourceCenterService;
import org.openflexo.ve.VECst;

public class DeleteOntologyObjectsDialogEDITOR {

	public static void main(String[] args) {
		FIBAbstractEditor editor = new FIBAbstractEditor() {
			@Override
			public Object[] getData() {
				String URI = "http://www.agilebirds.com/openflexo/ontologies/FlexoMethodology/FLXOrganizationalStructure.owl";
				FlexoResourceCenter resourceCenter = getFlexoResourceCenterService().getFlexoResourceCenter();
				OntologyLibrary ontologyLibrary = resourceCenter.retrieveBaseOntologyLibrary();
				FlexoOntology ontology = ontologyLibrary.getOntology(URI);
				ontology.loadWhenUnloaded();
				Vector<OntologyObject> selection = new Vector<OntologyObject>();
<<<<<<< HEAD
				selection.add(ontology.getOntologyObject(URI + "#Actor"));
				selection.add(ontology.getOntologyObject(URI + "#Mission"));
				selection.add(ontology.getOntologyObject(URI + "#hasMission"));
				selection.add(ontology.getOntologyObject(URI + "#description"));
=======
				selection.add(ontologyLibrary.getOntologyObject(URI + "#Actor"));
				selection.add(ontologyLibrary.getOntologyObject(URI + "#Mission"));
				selection.add(ontologyLibrary.getOntologyObject(URI + "#hasMission"));
				selection.add(ontologyLibrary.getOntologyObject(URI + "#description"));
>>>>>>> 1f9a24abb9a829f39257df504d397f3c69c28e6b
				DeleteOntologyObjects action = DeleteOntologyObjects.actionType.makeNewAction(null, selection, null);
				return makeArray(action);
			}

			@Override
			public File getFIBFile() {
				return VECst.DELETE_ONTOLOGY_OBJECTS_DIALOG_FIB;
			}
		};
		editor.launch();
	}

<<<<<<< HEAD
	private static ModuleLoader getModuleLoader() {
		return ModuleLoader.instance();
	}

	private static FlexoResourceCenterService getFlexoResourceCenterService() {
		return FlexoResourceCenterService.instance();
=======
	private static FlexoResourceCenterService getFlexoResourceCenterService() {
		return FlexoResourceCenterService.getInstance();
>>>>>>> 1f9a24abb9a829f39257df504d397f3c69c28e6b
	}
}
