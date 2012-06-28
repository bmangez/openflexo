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
package org.openflexo.vpm.view;

import java.util.List;
import java.util.Vector;

import org.openflexo.components.widget.FIBOntologyEditor;
import org.openflexo.foundation.ontology.FlexoOntology;
import org.openflexo.selection.SelectionListener;
import org.openflexo.view.FlexoPerspective;
import org.openflexo.view.SelectionSynchronizedModuleView;
import org.openflexo.vpm.controller.VPMController;

/**
 * This class represent the module view for an ontology.<br>
 * Underlying representation is supported by FIBOntologyEditor implementation.
 * 
 * @author sylvain
 * 
 */
@SuppressWarnings("serial")
public class OntologyView extends FIBOntologyEditor implements SelectionSynchronizedModuleView<FlexoOntology> {

	private FlexoPerspective<? super FlexoOntology> declaredPerspective;

	public OntologyView(FlexoOntology ontology, VPMController controller, FlexoPerspective<? super FlexoOntology> perspective) {
		super(ontology, controller);
		declaredPerspective = perspective;
		controller.manageResource(ontology);
	}

	@Override
	public VPMController getFlexoController() {
		return (VPMController) super.getFlexoController();
	}

	@Override
	public FlexoPerspective<? super FlexoOntology> getPerspective() {
		return declaredPerspective;
	}

	@Override
	public void deleteModuleView() {
		deleteView();
		getFlexoController().removeModuleView(this);
	}

	@Override
	public List<SelectionListener> getSelectionListeners() {
		Vector<SelectionListener> reply = new Vector<SelectionListener>();
		reply.add(this);
		return reply;
	}

	@Override
	public void willHide() {
	}

	@Override
	public void willShow() {
	}

	@Override
	public FlexoOntology getRepresentedObject() {
		return getOntology();
	}

}
