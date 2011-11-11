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
package org.openflexo.sgmodule.controller.action;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.openflexo.icon.GeneratorIconLibrary;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;

import org.openflexo.components.AskParametersDialog;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.cg.CGFile;
import org.openflexo.foundation.cg.GenerationRepository;
import org.openflexo.foundation.cg.templates.CGTemplates;
import org.openflexo.foundation.cg.templates.CustomCGTemplateRepository;
import org.openflexo.foundation.cg.templates.action.AddCustomTemplateRepository;
import org.openflexo.foundation.cg.templates.action.RedefineAllTemplates;
import org.openflexo.foundation.cg.utils.TemplateRepositoryType;
import org.openflexo.foundation.param.DynamicDropDownParameter;
import org.openflexo.foundation.param.RadioButtonListParameter;
import org.openflexo.foundation.param.TextFieldParameter;
import org.openflexo.foundation.utils.FlexoProjectFile;

public class RedefineAllTemplatesInitializer extends ActionInitializer {

	private static final Logger logger = Logger.getLogger(ControllerActionInitializer.class.getPackage().getName());

	RedefineAllTemplatesInitializer(SGControllerActionInitializer actionInitializer) {
		super(RedefineAllTemplates.actionType, actionInitializer);
	}

	@Override
	protected SGControllerActionInitializer getControllerActionInitializer() {
		return (SGControllerActionInitializer) super.getControllerActionInitializer();
	}

	@Override
	protected FlexoActionInitializer<RedefineAllTemplates> getDefaultInitializer() {
		return new FlexoActionInitializer<RedefineAllTemplates>() {
			@Override
			public boolean run(ActionEvent e, RedefineAllTemplates action) {
				CGTemplates templates = action.getFocusedObject().getTemplates();

				String CREATE_NEW_REPOSITORY = FlexoLocalization.localizedForKey("create_new_repository");
				String CHOOSE_EXISTING_REPOSITORY = FlexoLocalization.localizedForKey("choose_existing_repository");
				String[] locationChoices = { CREATE_NEW_REPOSITORY, CHOOSE_EXISTING_REPOSITORY };
				RadioButtonListParameter<String> repositoryChoiceParam = new RadioButtonListParameter<String>("location", "location",
						(templates.getCustomCodeRepositoriesVector().size() > 0 ? CHOOSE_EXISTING_REPOSITORY : CREATE_NEW_REPOSITORY),
						locationChoices);
				TextFieldParameter newRepositoryNameParam = new TextFieldParameter("name", "custom_template_repository_name",
						templates.getNextGeneratedCodeRepositoryName());
				newRepositoryNameParam.setDepends("location");
				newRepositoryNameParam.setConditional("location=" + '"' + CREATE_NEW_REPOSITORY + '"');
				DynamicDropDownParameter<CustomCGTemplateRepository> customRepositoryParam = new DynamicDropDownParameter<CustomCGTemplateRepository>(
						"customRepository",
						"custom_templates_repository",
						templates.getCustomCodeRepositoriesVector(),
						(getControllerActionInitializer().getSGController().getLastEditedCGRepository() != null ? getControllerActionInitializer()
								.getSGController().getLastEditedCGRepository().getPreferredTemplateRepository()
								: null));
				customRepositoryParam.setFormatter("name");
				customRepositoryParam.setDepends("location");
				customRepositoryParam.setConditional("location=" + '"' + CHOOSE_EXISTING_REPOSITORY + '"');
				/*
				String COMMON = FlexoLocalization.localizedForKey("redefine_in_common_context");
				String SPECIFIC_TARGET = FlexoLocalization.localizedForKey("redefine_for_a_specific_target");
				String[] contextChoices = { COMMON, SPECIFIC_TARGET };
				RadioButtonListParameter<String> contextChoiceParam = new RadioButtonListParameter<String>("context","context",COMMON,contextChoices);
				ChoiceListParameter<CodeType> targetTypeParam = new ChoiceListParameter<CodeType>("target", "target", CodeType.PROTOTYPE);
				targetTypeParam.setDepends("context");
				targetTypeParam.setConditional("context="+'"'+SPECIFIC_TARGET+'"');
				*/
				AskParametersDialog dialog = AskParametersDialog.createAskParametersDialog(getProject(), null,
						FlexoLocalization.localizedForKey("redefine_template_file"),
						FlexoLocalization.localizedForKey("enter_parameters_for_template_file_redefinition"), repositoryChoiceParam,
						newRepositoryNameParam, customRepositoryParam);
				if (dialog.getStatus() == AskParametersDialog.VALIDATE) {
					// CustomCGTemplateRepository repository = null;
					if (repositoryChoiceParam.getValue().equals(CREATE_NEW_REPOSITORY)) {
						AddCustomTemplateRepository addDirectory = AddCustomTemplateRepository.actionType.makeNewAction(templates, null,
								action.getEditor());
						addDirectory.setNewCustomTemplatesRepositoryName(newRepositoryNameParam.getValue());
						addDirectory.setNewCustomTemplatesRepositoryDirectory(new FlexoProjectFile(action.getFocusedObject().getProject(),
								newRepositoryNameParam.getValue()));
						addDirectory.setRepositoryType(TemplateRepositoryType.Code);
						addDirectory.doAction();
						action.setRepository(addDirectory.getNewCustomTemplatesRepository());
					} else if (repositoryChoiceParam.getValue().equals(CHOOSE_EXISTING_REPOSITORY)) {
						action.setRepository(customRepositoryParam.getValue());
					}
					/*
					if (contextChoiceParam.getValue().equals(SPECIFIC_TARGET)) {
						action.setTarget(targetTypeParam.getValue());
					}
					*/
					// If context is CGFile, check if custom template repository is
					// to associate to current repository
					if (action.getContext() instanceof CGFile) {
						GenerationRepository enclosingRepository = ((CGFile) action.getContext()).getRepository();
						if (enclosingRepository.getPreferredTemplateRepository() != action.getRepository()) {
							if (FlexoController.confirm(FlexoLocalization
									.localizedForKey("would_you_like_to_associate_custom_template_repository_to_current_code_repository"))) {
								enclosingRepository.setPreferredTemplateRepository(action.getRepository());
							}
						}
					}

					return true;
				} else {
					return false;
				}

			}
		};
	}

	@Override
	protected Icon getEnabledIcon() {
		return GeneratorIconLibrary.EDIT_ICON;
	}

	@Override
	protected Icon getDisabledIcon() {
		return GeneratorIconLibrary.EDIT_DISABLED_ICON;
	}

}
