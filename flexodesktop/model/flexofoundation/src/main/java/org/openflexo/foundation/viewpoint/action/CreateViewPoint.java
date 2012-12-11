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
package org.openflexo.foundation.viewpoint.action;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.IOFlexoException;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.ontology.IFlexoOntology;
import org.openflexo.foundation.resource.RepositoryFolder;
import org.openflexo.foundation.rm.ViewPointResource;
import org.openflexo.foundation.rm.ViewPointResourceImpl;
import org.openflexo.foundation.viewpoint.ViewPoint;
import org.openflexo.foundation.viewpoint.ViewPointLibrary;
import org.openflexo.foundation.viewpoint.ViewPointLibraryObject;
import org.openflexo.foundation.viewpoint.ViewPointObject;
import org.openflexo.toolbox.JavaUtils;
import org.openflexo.toolbox.StringUtils;

public class CreateViewPoint extends FlexoAction<CreateViewPoint, ViewPointLibraryObject, ViewPointObject> {

	private static final Logger logger = Logger.getLogger(CreateViewPoint.class.getPackage().getName());

	public static FlexoActionType<CreateViewPoint, ViewPointLibraryObject, ViewPointObject> actionType = new FlexoActionType<CreateViewPoint, ViewPointLibraryObject, ViewPointObject>(
			"create_view_point", FlexoActionType.newMenu, FlexoActionType.defaultGroup, FlexoActionType.ADD_ACTION_TYPE) {

		/**
		 * Factory method
		 */
		@Override
		public CreateViewPoint makeNewAction(ViewPointLibraryObject focusedObject, Vector<ViewPointObject> globalSelection,
				FlexoEditor editor) {
			return new CreateViewPoint(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(ViewPointLibraryObject object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

		@Override
		public boolean isEnabledForSelection(ViewPointLibraryObject object, Vector<ViewPointObject> globalSelection) {
			return object != null;
		}

	};

	static {
		FlexoModelObject.addActionForClass(CreateViewPoint.actionType, ViewPointLibrary.class);
		// FlexoModelObject.addActionForClass(CreateViewPoint.actionType, ViewPointFolder.class);
	}

	public static enum OntologicalScopeChoices {
		IMPORT_EXISTING_ONTOLOGY, CREATES_NEW_ONTOLOGY
	}

	public OntologicalScopeChoices ontologicalScopeChoice = OntologicalScopeChoices.IMPORT_EXISTING_ONTOLOGY;

	private String _newViewPointName;
	private String _newViewPointURI;
	private String _newViewPointDescription;
	private ViewPoint newViewPoint;

	private RepositoryFolder folder;

	public Vector<IFlexoOntology> importedOntologies = new Vector<IFlexoOntology>();

	// private boolean createsOntology = false;

	CreateViewPoint(ViewPointLibraryObject focusedObject, Vector<ViewPointObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws IOFlexoException {
		logger.info("Create new viewpoint");

		ViewPointLibrary viewPointLibrary = getFocusedObject().getViewPointLibrary();

		File newViewPointDir = getViewPointDir();

		newViewPointDir.mkdirs();

		/*if (ontologicalScopeChoice == OntologicalScopeChoices.CREATES_NEW_ONTOLOGY) {
			buildOntology();
		} else if (ontologicalScopeChoice == OntologicalScopeChoices.IMPORT_EXISTING_ONTOLOGY) {
			try {
				FileUtils.copyFileToDir(getOntologyFile(), newViewPointDir);
			} catch (IOException e) {
				throw new IOFlexoException(e);
			}

			_ontologyFile = new File(newViewPointDir, _ontologyFile.getName());
		}*/

		// Instanciate new ViewPoint
		newViewPoint = ViewPoint.newViewPoint(getBaseName(), getNewViewPointURI(), newViewPointDir, viewPointLibrary);
		newViewPoint.setDescription(getNewViewPointDescription());
		ViewPointResource vpRes = ViewPointResourceImpl.makeViewPointResource(new File(newViewPointDir, getBaseName() + ".viewpoint"),
				viewPointLibrary);
		vpRes.setResourceData(newViewPoint);

		// And register it to the library
		viewPointLibrary.registerViewPoint(vpRes);
	}

	/*private OWLMetaModel buildOntology() {
		_ontologyFile = new File(getViewPointDir(), getBaseName() + ".owl");
		OWLMetaModel newOntology = OWLMetaModel.createNewImportedOntology(getNewViewPointURI(), _ontologyFile, getViewPointFolder()
				.getOntologyLibrary());
		for (IFlexoOntology importedOntology : importedOntologies) {
			try {
				if (importedOntology instanceof OWLOntology) {
					newOntology.importOntology(importedOntology);
				} else {
					logger.warning("Could not import anything else than an OWL ontology");
				}
			} catch (OntologyNotFoundException e) {
				e.printStackTrace();
			}
		}
		try {
			newOntology.save();
		} catch (SaveResourceException e) {
			e.printStackTrace();
		}
		return newOntology;
	}*/

	public String getNewViewPointName() {
		return _newViewPointName;
	}

	public void setNewViewPointName(String newViewPointName) {
		this._newViewPointName = newViewPointName;
	}

	public String getNewViewPointURI() {
		if (StringUtils.isEmpty(_newViewPointURI) /*&& getOntologyFile() != null*/) {
			// return OWLOntology.findOntologyURI(getOntologyFile());
			return "http://to.be.implemented";
		}
		return _newViewPointURI;
	}

	public void setNewViewPointURI(String newViewPointURI) {
		this._newViewPointURI = newViewPointURI;
	}

	public String getNewViewPointDescription() {
		return _newViewPointDescription;
	}

	public void setNewViewPointDescription(String newViewPointDescription) {
		this._newViewPointDescription = newViewPointDescription;
	}

	/*	public File getOntologyFile() {
		return _ontologyFile;
	}

	public void setOntologyFile(File ontologyFile) {
		this._ontologyFile = ontologyFile;
		if (ontologyFile != null) {
			String ontologyURI = OWLOntology.findOntologyURI(getOntologyFile());
			String ontologyName = ToolBox.getJavaClassName(OWLOntology.findOntologyName(getOntologyFile()));
			if (ontologyName == null && ontologyFile != null && ontologyFile.getName().length() > 4) {
				ontologyName = ontologyFile.getName().substring(0, ontologyFile.getName().length() - 4);
			}
			if (StringUtils.isNotEmpty(ontologyURI)) {
				_newViewPointURI = ontologyURI;
			}
			setNewViewPointName(ontologyName);
		}
		}*/

	public RepositoryFolder getViewPointFolder() {
		/*if (folder == null) {
			if (getFocusedObject() instanceof ViewPointFolder) {
				return _ViewPointFolder = (ViewPointFolder) getFocusedObject();
			} else if (getFocusedObject() instanceof ViewPointLibrary) {
				return _ViewPointFolder = ((ViewPointLibrary) getFocusedObject()).getRootFolder();
			}
			return null;
		}*/
		return folder;
	}

	public void setViewPointFolder(RepositoryFolder viewPointFolder) {
		folder = viewPointFolder;
	}

	public boolean isNewViewPointNameValid() {
		if (StringUtils.isEmpty(getNewViewPointName())) {
			errorMessage = "please_supply_valid_view_point_name";
			return false;
		}
		return true;
	}

	public boolean isNewViewPointURIValid() {
		if (StringUtils.isEmpty(getNewViewPointURI())) {
			errorMessage = "please_supply_valid_uri";
			return false;
		}
		try {
			new URL(getNewViewPointURI());
		} catch (MalformedURLException e) {
			errorMessage = "malformed_uri";
			return false;
		}
		if (!getNewViewPointURI().endsWith(".owl")) {
			errorMessage = "malformed_uri";
			return false;
		}
		return true;
	}

	public String errorMessage;

	public boolean isValid() {
		if (!isNewViewPointNameValid()) {
			return false;
		}
		if (!isNewViewPointURIValid()) {
			return false;
		}
		/*if (ontologicalScopeChoice == OntologicalScopeChoices.IMPORT_EXISTING_ONTOLOGY) {
			return getOntologyFile() != null;
		}*/
		return true;
	}

	public ViewPoint getNewViewPoint() {
		return newViewPoint;
	}

	private String getBaseName() {
		return JavaUtils.getClassName(getNewViewPointName());
	}

	private File getViewPointDir() {
		String baseName = getBaseName();
		logger.warning("Not implemented yet");
		// return new File(getViewPointFolder().getExpectedPath(), baseName + ".viewpoint");
		return null;
	}

	/*public void createOntology() {
		createsOntology = true;
		_ontologyFile = new File(getViewPointDir(), getBaseName() + ".owl");
	}*/

	public void addToImportedOntologies(IFlexoOntology ontology) {
		System.out.println("import ontology " + ontology);
		importedOntologies.add(ontology);
	}

	public void removeFromImportedOntologies(IFlexoOntology ontology) {
		System.out.println("remove ontology " + ontology);
		importedOntologies.remove(ontology);
	}
}
