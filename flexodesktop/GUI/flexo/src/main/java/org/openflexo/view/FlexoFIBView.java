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
package org.openflexo.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.openflexo.fib.FIBLibrary;
import org.openflexo.fib.controller.FIBController;
import org.openflexo.fib.model.FIBComponent;
import org.openflexo.fib.model.listener.FIBMouseClickListener;
import org.openflexo.fib.view.FIBView;
import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.DefaultFlexoEditor;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoEditor.FlexoEditorFactory;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.FlexoObservable;
import org.openflexo.foundation.FlexoResourceCenter;
import org.openflexo.foundation.GraphicalFlexoObserver;
import org.openflexo.foundation.action.FlexoAction;
import org.openflexo.foundation.action.FlexoActionInitializer;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.rm.FlexoResourceManager;
import org.openflexo.foundation.utils.FlexoProgress;
import org.openflexo.foundation.utils.ProjectInitializerException;
import org.openflexo.foundation.utils.ProjectLoadingCancelledException;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.module.FlexoResourceCenterService;
import org.openflexo.toolbox.HasPropertyChangeSupport;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.FlexoFIBController;

/**
 * Please comment this class
 * 
 * @author sguerin
 * 
 */
public class FlexoFIBView<O> extends JPanel implements GraphicalFlexoObserver, HasPropertyChangeSupport, PropertyChangeListener {
	static final Logger logger = Logger.getLogger(FlexoFIBView.class.getPackage().getName());

	private O dataObject;
	private FlexoController controller;
	private FIBView fibView;
	private FlexoFIBController<O> fibController;
	private FIBComponent fibComponent;

	private PropertyChangeSupport pcSupport;

	public FlexoFIBView(O representedObject, FlexoController controller, File fibFile, FlexoProgress progress) {
		this(representedObject, controller, fibFile, false, progress);
	}

	public FlexoFIBView(O representedObject, FlexoController controller, File fibFile, boolean addScrollBar, FlexoProgress progress) {
		this(representedObject, controller, FIBLibrary.instance().retrieveFIBComponent(fibFile), addScrollBar, progress);
	}

	public FlexoFIBView(O representedObject, FlexoController controller, String fibResourcePath, FlexoProgress progress) {
		this(representedObject, controller, fibResourcePath, false, progress);
	}

	public FlexoFIBView(O representedObject, FlexoController controller, String fibResourcePath, boolean addScrollBar,
			FlexoProgress progress) {
		this(representedObject, controller, FIBLibrary.instance().retrieveFIBComponent(fibResourcePath), addScrollBar, progress);
	}

	protected FlexoFIBView(O dataObject, FlexoController controller, FIBComponent fibComponent, boolean addScrollBar, FlexoProgress progress) {
		super(new BorderLayout());
		this.dataObject = dataObject;
		this.controller = controller;
		this.fibComponent = fibComponent;

		if (dataObject instanceof HasPropertyChangeSupport) {
			((HasPropertyChangeSupport) dataObject).getPropertyChangeSupport().addPropertyChangeListener(this);
		} else if (dataObject instanceof FlexoObservable) {
			((FlexoObservable) dataObject).addObserver(this);
		}

		pcSupport = new PropertyChangeSupport(this);

		initializeFIBComponent();

		fibController = createFibController(fibComponent, controller);

		if (progress != null) {
			progress.setProgress(FlexoLocalization.localizedForKey("build_view"));
		}

		fibView = fibController.buildView(fibComponent);

		if (progress != null) {
			progress.setProgress(FlexoLocalization.localizedForKey("init_view"));
		}

		fibController.setDataObject(dataObject);

		if (this instanceof FIBMouseClickListener) {
			fibView.getController().addMouseClickListener((FIBMouseClickListener) this);
		}

		if (addScrollBar) {
			add(new JScrollPane(fibView.getJComponent()), BorderLayout.CENTER);
		} else {
			add(fibView.getJComponent(), BorderLayout.CENTER);
		}

		validate();
		revalidate();

		if (progress != null) {
			progress.hideWindow();
		}
	}

	/**
	 * Create the Fib Controller to be used for this view. Can be overrided to add functionalities to this Fib View.
	 * 
	 * @param fibComponent
	 * @param controller
	 * @return the newly created FlexoFIBController
	 */
	protected FlexoFIBController<O> createFibController(FIBComponent fibComponent, FlexoController controller) {
		FIBController returned = FIBController.instanciateController(fibComponent, FlexoLocalization.getMainLocalizer());
		if (returned instanceof FlexoFIBController) {
			((FlexoFIBController) returned).setFlexoController(controller);
			return (FlexoFIBController) returned;
		} else if (fibComponent.getControllerClass() != null) {
			logger.warning("Controller for component " + fibComponent + " is not an instanceof FlexoFIBController");
		}
		return fibController = new FlexoFIBController<O>(fibComponent, controller);
	}

	public FlexoController getFlexoController() {
		return controller;
	}

	@Override
	public void update(FlexoObservable observable, DataModification dataModification) {
		/* if (dataModification instanceof ObjectDeleted) {
		     if (dataModification.oldValue() == getOntologyObject()) {
		         deleteModuleView();
		      }
		 } else if (dataModification.propertyName()!=null && dataModification.propertyName().equals("name")) {
		     getOEController().getFlexoFrame().updateTitle();
		     updateTitlePanel();
		 }*/

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO !
		logger.info("propertyChange in FlexoFIBView: " + evt);
	}

	public O getDataObject() {
		return dataObject;
	}

	public void setDataObject(O object) {
		dataObject = object;
		fibController.setDataObject(object, true);
	}

	public FIBComponent getFIBComponent() {
		return fibComponent;
	}

	public FIBView getFIBView() {
		return fibView;
	}

	public FlexoFIBController<O> getFIBController() {
		return fibController;
	}

	public FIBView getFIBView(String componentName) {
		return fibController.viewForComponent(componentName);
	}

	public void deleteView() {
		if (this instanceof FIBMouseClickListener) {
			fibView.getController().removeMouseClickListener((FIBMouseClickListener) this);
		}
		if (dataObject instanceof HasPropertyChangeSupport) {
			((HasPropertyChangeSupport) dataObject).getPropertyChangeSupport().removePropertyChangeListener(this);
		} else if (dataObject instanceof FlexoObservable) {
			((FlexoObservable) dataObject).deleteObserver(this);
		}
	}

	/**
	 * Returns flag indicating if this view is itself responsible for scroll management When not, Flexo will manage it's own scrollbar for
	 * you
	 * 
	 * @return
	 */
	public boolean isAutoscrolled() {
		return false;
	}

	/**
	 * This method is a hook which is called just before to initialize FIBView and FIBController, and allow to programmatically define,
	 * check or redefine component
	 */
	protected void initializeFIBComponent() {
	}

	@Override
	public PropertyChangeSupport getPropertyChangeSupport() {
		return pcSupport;
	}

	@Override
	public String getDeletedProperty() {
		return null;
	}

	// test purposes
	public static FlexoEditor loadProject(File prjDir) {
		FlexoResourceCenter resourceCenter = getFlexoResourceCenterService().getFlexoResourceCenter();
		FlexoEditor editor = null;
		try {
			editor = FlexoResourceManager.initializeExistingProject(prjDir, EDITOR_FACTORY, resourceCenter);
		} catch (ProjectLoadingCancelledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectInitializerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.getProject().setResourceCenter(getFlexoResourceCenterService().getFlexoResourceCenter());
		if (editor == null) {
			System.exit(-1);
		}
		return editor;
	}

	// test purposes
	private static FlexoResourceCenterService getFlexoResourceCenterService() {
		return FlexoResourceCenterService.instance();
	}

	// test purposes
	protected static final FlexoEditorFactory EDITOR_FACTORY = new FlexoEditorFactory() {
		@Override
		public DefaultFlexoEditor makeFlexoEditor(FlexoProject project) {
			return new FlexoTestEditor(project);
		}
	};

	// test purposes
	public static class FlexoTestEditor extends DefaultFlexoEditor {
		public FlexoTestEditor(FlexoProject project) {
			super(project);
		}

		@Override
		public <A extends FlexoAction<?, T1, T2>, T1 extends FlexoModelObject, T2 extends FlexoModelObject> FlexoActionInitializer<? super A> getInitializerFor(
				FlexoActionType<A, T1, T2> actionType) {
			FlexoActionInitializer<A> init = new FlexoActionInitializer<A>() {

				@Override
				public boolean run(ActionEvent event, A action) {
					boolean reply = action.getActionType().isEnabled(action.getFocusedObject(), action.getGlobalSelection(),
							FlexoTestEditor.this);
					if (!reply) {
						System.err.println("ACTION NOT ENABLED :" + action.getClass() + " on object "
								+ (action.getFocusedObject() != null ? action.getFocusedObject().getClass() : "null focused object"));
					}
					return reply;
				}

			};
			return init;
		}
	}

}
