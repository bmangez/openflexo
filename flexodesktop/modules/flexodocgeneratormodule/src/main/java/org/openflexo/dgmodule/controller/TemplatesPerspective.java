/**
 * 
 */
package org.openflexo.dgmodule.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import org.openflexo.FlexoCst;
import org.openflexo.dgmodule.controller.browser.FIBTemplatesBrowser;
import org.openflexo.dgmodule.view.DGTemplateFileModuleView;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.cg.templates.CGTemplate;
import org.openflexo.icon.DGIconLibrary;
import org.openflexo.view.FlexoPerspective;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;

/**
 * This perspective is used to represent all templates defined in the scope of current project<br>
 * At the left is presented a browser allowing to browse into the tempaltes, at the middle are the templates represented and edited.
 * 
 * @author sylvain
 * 
 */
public class TemplatesPerspective extends FlexoPerspective<FlexoModelObject> {

	protected static final Logger logger = Logger.getLogger(TemplatesPerspective.class.getPackage().getName());

	private FIBTemplatesBrowser templatesBrowser;

	private JLabel infoLabel;
	private final DGController dgController;

	/**
	 * @param deController
	 */
	public TemplatesPerspective(DGController dgController) {
		super("templates");
		this.dgController = dgController;
		templatesBrowser = new FIBTemplatesBrowser(dgController.getProject().getGeneratedDoc().getTemplates(), dgController);
		infoLabel = new JLabel("Templates perspective");
		infoLabel.setFont(FlexoCst.SMALL_FONT);
	}

	@Override
	public boolean isAlwaysVisible() {
		return true;
	}

	@Override
	public boolean doesPerspectiveControlLeftView() {
		return true;
	}

	@Override
	public FIBTemplatesBrowser getLeftView() {
		return templatesBrowser;
	}

	/**
	 * Overrides getIcon
	 * 
	 * @see org.openflexo.view.FlexoPerspective#getActiveIcon()
	 */
	@Override
	public ImageIcon getActiveIcon() {
		return DGIconLibrary.DG_TEMPLATES_ACTIVE_ICON;
	}

	/**
	 * Overrides getSelectedIcon
	 * 
	 * @see org.openflexo.view.FlexoPerspective#getSelectedIcon()
	 */
	@Override
	public ImageIcon getSelectedIcon() {
		return DGIconLibrary.DG_TEMPLATES_SELECTED_ICON;
	}

	@Override
	public JComponent getFooter() {
		return infoLabel;
	}

	@Override
	public FlexoModelObject getDefaultObject(FlexoModelObject proposedObject) {
		System.out.println("Proposed object in TemplatesPerspective: " + proposedObject);
		if (proposedObject instanceof CGTemplate) {
			return proposedObject;
		} else {
			return this.dgController.getProject().getGeneratedDoc().getTemplates();
		}
	}

	@Override
	public boolean hasModuleViewForObject(FlexoModelObject object) {
		return object instanceof CGTemplate;
	}

	@Override
	public ModuleView<? extends FlexoModelObject> createModuleViewForObject(FlexoModelObject object, FlexoController controller) {
		if (object instanceof CGTemplate) {
			return new DGTemplateFileModuleView((CGTemplate) object, (DGController) controller);
		}
		if (DGController.logger.isLoggable(Level.INFO)) {
			DGController.logger.info("No module view for object: " + object + " and perspective: " + this);
		}
		return null;
	}

	@Override
	public void notifyModuleViewDisplayed(ModuleView<?> moduleView) {
		super.notifyModuleViewDisplayed(moduleView);
	}

}