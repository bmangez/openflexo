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
package org.openflexo.doceditor.controller.action;

import java.awt.event.ActionEvent;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.components.AskParametersDialog;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.action.FlexoActionFinalizer;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.action.FlexoExceptionHandler;
import org.openflexo.foundation.cg.action.InvalidLevelException;
import org.openflexo.foundation.dm.ERDiagram;
import org.openflexo.foundation.param.DMEntityParameter;
import org.openflexo.foundation.param.DynamicDropDownParameter;
import org.openflexo.foundation.param.EnumDropDownParameter;
import org.openflexo.foundation.param.OperationComponentParameter;
import org.openflexo.foundation.param.ParameterDefinition;
import org.openflexo.foundation.param.ProcessOrProcessFolderParameter;
import org.openflexo.foundation.param.ProcessOrProcessFolderParameter.ProcessOrProcessFolderSelectingConditional;
import org.openflexo.foundation.param.RoleParameter;
import org.openflexo.foundation.param.TextFieldParameter;
import org.openflexo.foundation.param.ViewParameter;
import org.openflexo.foundation.toc.PredefinedSection;
import org.openflexo.foundation.toc.ProcessSection;
import org.openflexo.foundation.toc.action.DeprecatedAddTOCEntry;
import org.openflexo.foundation.view.ViewDefinition;
import org.openflexo.foundation.wkf.FlexoProcess;
import org.openflexo.foundation.wkf.ProcessFolder;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

public class DeprecatedAddTOCEntryInitializer extends ActionInitializer {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	public DeprecatedAddTOCEntryInitializer(ControllerActionInitializer actionInitializer) {
		super(DeprecatedAddTOCEntry.actionType, actionInitializer);
	}

	@Override
	protected FlexoActionInitializer<DeprecatedAddTOCEntry> getDefaultInitializer() {
		return new FlexoActionInitializer<DeprecatedAddTOCEntry>() {
			@Override
			public boolean run(ActionEvent e, DeprecatedAddTOCEntry action) {
				final Vector<PredefinedSection.PredefinedSectionType> staticSections = new Vector<PredefinedSection.PredefinedSectionType>();
				for (PredefinedSection.PredefinedSectionType docSection : PredefinedSection.PredefinedSectionType.values()) {
					if (action.getRepository().getTOCEntryWithID(docSection) == null) {
						staticSections.add(docSection);
					}
				}

				Vector<FlexoProcess> processSections = new Vector<FlexoProcess>();
				for (FlexoProcess process : getProject().getAllLocalFlexoProcesses()) {
					if (action.getRepository().getTOCEntryForObject(process) == null) {
						processSections.add(process);
					}
				}

				ParameterDefinition[] def = new ParameterDefinition[10];
				def[0] = new TextFieldParameter("title", "toc_entry_title", null);
				def[1] = new EnumDropDownParameter<SectionTypeList>("sectionType", "sectionType", SectionTypeList.CUSTOM_SECTION,
						SectionTypeList.values()) {
					@Override
					public boolean accept(SectionTypeList value) {
						switch (value) {
						case CUSTOM_SECTION:
							return true;
						case PRE_DEFINED:
							return staticSections.size() > 0;
						case PROCESS:
							return true;
						case VIEW:
							return true;
						case ROLE:
							return true;
						case ENTITY:
							return true;
						case OPERATIONCOMPONENT:
							return true;
						case ER_DIAGRAM:
							return true;
						default:
							break;
						}
						return false;
					}
				};
				def[2] = new EnumDropDownParameter<PredefinedSection.PredefinedSectionType>("section", "section",
						staticSections.size() > 0 ? staticSections.firstElement() : null,
						staticSections.toArray(new PredefinedSection.PredefinedSectionType[staticSections.size()])) {
					@Override
					public boolean accept(PredefinedSection.PredefinedSectionType value) {
						switch (value) {
						case ER_DIAGRAM:
							return false;
						default:
							return true;
						}
					}
				};
				def[2].setDepends("sectionType");
				def[2].setConditional("sectionType=\"" + SectionTypeList.PRE_DEFINED + "\"");
				def[3] = new ProcessOrProcessFolderParameter("process", "process");
				def[3].setDepends("sectionType,section");
				def[3].setConditional("sectionType=\"" + SectionTypeList.PROCESS + "\"");
				((ProcessOrProcessFolderParameter) def[3]).setProcessSelectingConditional(new ProcessOrProcessFolderSelectingConditional() {

					@Override
					public boolean isSelectable(FlexoProcess process) {
						return !process.isImported();
					}

					@Override
					public boolean isSelectable(ProcessFolder folder) {
						return !folder.isImported();
					}

				});

				def[4] = new RoleParameter("role", "role", null);
				def[4].setDepends("sectionType");
				def[4].setConditional("sectionType=\"" + SectionTypeList.ROLE + "\"");

				def[5] = new DMEntityParameter("entity", "entity", null);
				def[5].setDepends("sectionType");
				def[5].setConditional("sectionType=\"" + SectionTypeList.ENTITY + "\"");
				def[6] = new OperationComponentParameter("operationComponent", "operationComponent", null);
				def[6].setDepends("sectionType");
				def[6].setConditional("sectionType=\"" + SectionTypeList.OPERATIONCOMPONENT + "\"");

				def[7] = new EnumDropDownParameter<SectionSubType>("subType", "type", SectionSubType.Doc, SectionSubType.values()) {
					@Override
					public boolean accept(SectionSubType value) {
						switch (value) {
						case Doc:
							return true;
						case RaciMatrix:
							return true;
						case SipocLevel2:
							return true;
						case SipocLevel3:
							return true;
						case OperationTable:
							return true;
						default:
							break;
						}
						return false;
					}
				};
				def[7].setDepends("sectionType");
				def[7].setConditional("sectionType=\"" + SectionTypeList.PROCESS + "\"");

				def[8] = new DynamicDropDownParameter<ERDiagram>("diagram", "diagram", getEditor().getProject().getDataModel()
						.getDiagrams(), null);
				def[8].setDepends("sectionType");
				def[8].setConditional("sectionType=\"" + SectionTypeList.ER_DIAGRAM + "\"");
				def[8].addParameter("format", "name");

				def[9] = new ViewParameter("view", "view", null);
				def[9].setDepends("sectionType,section");
				def[9].setConditional("sectionType=\"" + SectionTypeList.VIEW + "\"");
				((ViewParameter) def[9]).setViewSelectingConditional(new ViewParameter.ViewSelectingConditional() {
					@Override
					public boolean isSelectable(ViewDefinition view) {
						return true;
					}
				});

				AskParametersDialog dialog = AskParametersDialog.createAskParametersDialog(getProject(), getController().getFlexoFrame(),
						FlexoLocalization.localizedForKey("toc_entry_creation"), FlexoLocalization.localizedForKey("enter_new_title"), def);
				if (dialog.getStatus() == AskParametersDialog.VALIDATE) {
					action.setTocEntryTitle((String) def[0].getValue());
					if (def[1].getValue() == SectionTypeList.PRE_DEFINED) {
						if (def[2].getValue() == null) {
							return false;
						}
						action.setSection((PredefinedSection.PredefinedSectionType) def[2].getValue());
						return true;
					}
					if (def[1].getValue() == SectionTypeList.PROCESS) {
						if (def[3].getValue() == null) {
							return false;
						}
						action.setModelObject((FlexoModelObject) def[3].getValue());
						// action.setSection(DocSection.PROCESSES);
						if (def[7].getValue() == null) {
							return false;
						}

						action.setSubType(def[7].getValue().equals(SectionSubType.Doc) ? ProcessSection.ProcessDocSectionSubType.Doc
								: def[7].getValue().equals(SectionSubType.RaciMatrix) ? ProcessSection.ProcessDocSectionSubType.RaciMatrix
										: def[7].getValue().equals(SectionSubType.SipocLevel2) ? ProcessSection.ProcessDocSectionSubType.SIPOCLevel2
												: def[7].getValue().equals(SectionSubType.SipocLevel3) ? ProcessSection.ProcessDocSectionSubType.SIPOCLevel3
														: ProcessSection.ProcessDocSectionSubType.OperationTable);
						return true;
					}
					if (def[1].getValue() == SectionTypeList.VIEW) {
						if (def[9].getValue() == null) {
							return false;
						}
						action.setModelObject(((ViewDefinition) def[9].getValue()).getShema());
						return true;
					}
					if (def[1].getValue() == SectionTypeList.ROLE) {
						if (def[4].getValue() == null) {
							return false;
						}
						action.setModelObject((FlexoModelObject) def[4].getValue());
						return true;
					}
					if (def[1].getValue() == SectionTypeList.ENTITY) {
						if (def[5].getValue() == null) {
							return false;
						}
						action.setModelObject((FlexoModelObject) def[5].getValue());
						return true;
					}
					if (def[1].getValue() == SectionTypeList.OPERATIONCOMPONENT) {
						if (def[6].getValue() == null) {
							return false;
						}
						action.setModelObject((FlexoModelObject) def[6].getValue());
						return true;
					}

					if (def[1].getValue() == SectionTypeList.ER_DIAGRAM) {
						if (def[8].getValue() == null) {
							return false;
						}
						action.setModelObject((ERDiagram) def[8].getValue());
						action.setSection(PredefinedSection.PredefinedSectionType.ER_DIAGRAM);
						return true;
					}
					return true;
				}
				return false;
			}
		};
	}

	enum SectionTypeList {
		CUSTOM_SECTION, PRE_DEFINED, PROCESS, ROLE, ENTITY, OPERATIONCOMPONENT, ER_DIAGRAM, VIEW
	}

	enum SectionSubType {
		Doc, OperationTable, RaciMatrix, SipocLevel2, SipocLevel3
	}

	@Override
	protected FlexoActionFinalizer<DeprecatedAddTOCEntry> getDefaultFinalizer() {
		return new FlexoActionFinalizer<DeprecatedAddTOCEntry>() {
			@Override
			public boolean run(ActionEvent e, DeprecatedAddTOCEntry action) {
				return true;
			}
		};
	}

	@Override
	protected FlexoExceptionHandler<? super DeprecatedAddTOCEntry> getDefaultExceptionHandler() {
		return new FlexoExceptionHandler<DeprecatedAddTOCEntry>() {

			@Override
			public boolean handleException(FlexoException exception, DeprecatedAddTOCEntry action) {
				if (exception instanceof InvalidLevelException) {
					FlexoController.notify(FlexoLocalization.localizedForKey("invalid_level"));
					return true;
				}
				return false;
			}

		};
	}

}
