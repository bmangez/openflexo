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
package org.openflexo.wkf.controller;

/*
 * FlexoMainController.java
 * Project WorkflowEditor
 *
 * Created by benoit on Mar 8, 2004
 */

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import org.openflexo.fge.DefaultDrawing;
import org.openflexo.fge.GraphicalRepresentation;
import org.openflexo.fge.geom.FGEPoint;
import org.openflexo.fge.view.DrawingView;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.ie.IERegExp;
import org.openflexo.foundation.rm.DuplicateResourceException;
import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.validation.ValidationModel;
import org.openflexo.foundation.wkf.DuplicateRoleException;
import org.openflexo.foundation.wkf.DuplicateStatusException;
import org.openflexo.foundation.wkf.FlexoProcess;
import org.openflexo.foundation.wkf.FlexoWorkflow;
import org.openflexo.foundation.wkf.Role;
import org.openflexo.foundation.wkf.RoleList;
import org.openflexo.foundation.wkf.Status;
import org.openflexo.foundation.wkf.WKFObject;
import org.openflexo.foundation.wkf.node.OperationNode;
import org.openflexo.foundation.wkf.utils.OperationAssociatedWithComponentSuccessfully;
import org.openflexo.inspector.InspectableObject;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.module.FlexoModule;
import org.openflexo.module.UserType;
import org.openflexo.print.PrintManager;
import org.openflexo.print.PrintManagingController;
import org.openflexo.selection.SelectionManager;
import org.openflexo.utils.FlexoSplitPaneLocationSaver;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.FlexoPerspective;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ConsistencyCheckingController;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.InteractiveFlexoEditor;
import org.openflexo.view.controller.SelectionManagingController;
import org.openflexo.view.listener.FlexoKeyEventListener;
import org.openflexo.view.menu.FlexoMenuBar;
import org.openflexo.wkf.WKFPreferences;
import org.openflexo.wkf.controller.action.WKFControllerActionInitializer;
import org.openflexo.wkf.processeditor.ProcessEditorController;
import org.openflexo.wkf.processeditor.ProcessView;
import org.openflexo.wkf.processeditor.gr.EdgeGR;
import org.openflexo.wkf.processeditor.gr.WKFObjectGR;
import org.openflexo.wkf.view.ProcessBrowserView;
import org.openflexo.wkf.view.ProcessBrowserWindow;
import org.openflexo.wkf.view.RoleListBrowserView;
import org.openflexo.wkf.view.WKFMainPane;
import org.openflexo.wkf.view.WorkflowBrowserView;
import org.openflexo.wkf.view.WorkflowBrowserWindow;
import org.openflexo.wkf.view.doc.WKFDocumentationView;
import org.openflexo.wkf.view.listener.WKFKeyEventListener;
import org.openflexo.wkf.view.menu.WKFMenuBar;

/**
 * The main controller for the WKF module
 * 
 * @author benoit, sylvain
 */
public class WKFController extends FlexoController implements SelectionManagingController, ConsistencyCheckingController,
		PrintManagingController, PropertyChangeListener {

	private static final Logger logger = Logger.getLogger(WKFController.class.getPackage().getName());

	@Override
	public boolean useNewInspectorScheme() {
		return true;
	}

	@Override
	public boolean useOldInspectorScheme() {
		return false;
	}

	// ======================================================
	// ================== Static variables ==================
	// ======================================================

	public static boolean isDropSuccessFull = false;

	// ======================================================
	// ================= Instance variables =================
	// ======================================================

	// Relative windows
	private WorkflowBrowserWindow _workflowBrowserWindow;

	private ProcessBrowserWindow _processBrowserWindow;

	private WKFSelectionManager _selectionManager;

	// Browsers
	private final ProcessBrowser _processBrowser;
	private final WorkflowBrowser _workflowBrowser;
	private final RoleListBrowser _roleListBrowser;

	// External browser
	private final ProcessBrowser _externalProcessBrowser;

	// public final OldProcessPerspective OLD_PROCESS_EDITOR_PERSPECTIVE;
	public final ProcessPerspective PROCESS_EDITOR_PERSPECTIVE;
	public final SwimmingLanePerspective SWIMMING_LANE_PERSPECTIVE;
	public final RolePerspective ROLE_EDITOR_PERSPECTIVE;
	public final DocumentationPerspective DOCUMENTATION_PERSPECTIVE;

	public final FlexoPerspective WKF_INVADERS = new DocumentationPerspective(this, "wkf_invaders") {
		@Override
		public ModuleView<?> createModuleViewForObject(FlexoModelObject process, FlexoController controller) {
			if (process instanceof FlexoProcess) {
				ProcessEditorController wkfController = new ProcessEditorController((WKFController) controller, (FlexoProcess) process);
			}
			return null;
			// return new WKFInvaders(process,(WKFController)controller);
		}
	};

	protected BufferedImage capturedDraggedNodeImage;

	// ======================================================
	// ========= Define PROCESS_EDITOR perspective ==========
	// ======================================================

	private final JSplitPane _workflowProcessBrowserViews;
	private final WorkflowBrowserView _wkfBrowserView;
	private final ProcessBrowserView _processBrowserView;
	private RoleListBrowserView roleListBrowserView;

	// ==========================================================================
	// ============================= Constructor ===============================
	// ==========================================================================

	/**
	 * Default constructor
	 */
	public WKFController(FlexoModule module) {
		super(module);
		_selectionManager = new WKFSelectionManager(this);

		_processBrowser = new ProcessBrowser(this);
		_externalProcessBrowser = new ProcessBrowser(this);
		_workflowBrowser = new WorkflowBrowser(this);
		_roleListBrowser = new RoleListBrowser(this);

		_wkfBrowserView = new WorkflowBrowserView(this);
		_processBrowserView = new ProcessBrowserView(_processBrowser, this);
		_workflowProcessBrowserViews = new JSplitPane(JSplitPane.VERTICAL_SPLIT, _wkfBrowserView, _processBrowserView);
		_workflowProcessBrowserViews.setBorder(BorderFactory.createEmptyBorder());
		new FlexoSplitPaneLocationSaver(_workflowProcessBrowserViews, "WKFBrowsersSplitPane", 1.0);
		setRoleListBrowserView(new RoleListBrowserView(_roleListBrowser, this));

		addToPerspectives(PROCESS_EDITOR_PERSPECTIVE = new ProcessPerspective(this));
		addToPerspectives(SWIMMING_LANE_PERSPECTIVE = new SwimmingLanePerspective(this));
		addToPerspectives(ROLE_EDITOR_PERSPECTIVE = new RolePerspective(this));
		DOCUMENTATION_PERSPECTIVE = new DocumentationPerspective(this, "documentation");
		if (UserType.isDevelopperRelease() || UserType.isMaintainerRelease()) {
			addToPerspectives(DOCUMENTATION_PERSPECTIVE);
		}
		initWorkflowGraphicalPropertiesFromPrefs(getProject());
		initWithWKFPreferences();
		WKFPreferences.getPreferences().getPropertyChangeSupport().addPropertyChangeListener(this);

	}

	private void initWorkflowGraphicalPropertiesFromPrefs(FlexoProject project) {
		FlexoWorkflow wkf = project.getFlexoWorkflow();
		wkf.getUseTransparency(WKFPreferences.getUseTransparency());
		wkf.getShowShadows(WKFPreferences.getShowShadows());
		wkf.getShowWOName(WKFPreferences.getShowWONameInWKF());
		wkf.getActivityFont(WKFPreferences.getActivityNodeFont());
		wkf.getOperationFont(WKFPreferences.getOperationNodeFont());
		wkf.getActionFont(WKFPreferences.getActionNodeFont());
		wkf.getEventFont(WKFPreferences.getEventNodeFont());
		wkf.getArtefactFont(WKFPreferences.getArtefactFont());
		wkf.getEdgeFont(WKFPreferences.getEdgeFont());
		wkf.getRoleFont(WKFPreferences.getRoleFont());
		wkf.getComponentFont(WKFPreferences.getComponentFont());
		wkf.getConnectorRepresentation(WKFPreferences.getConnectorRepresentation());
	}

	@Override
	protected FlexoKeyEventListener createKeyEventListener() {
		return new WKFKeyEventListener(this);
	}

	@Override
	public ControllerActionInitializer createControllerActionInitializer(InteractiveFlexoEditor editor) {
		return new WKFControllerActionInitializer(editor, this);
	}

	/**
	 * Creates a new instance of MenuBar for the module this controller refers to
	 * 
	 * @return
	 */
	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new WKFMenuBar(this);
	}

	private FlexoControlGraphController _cgController;

	public void showControlGraphViewer() {
		getControlGraphController().getViewer().setVisible(true);
	}

	public void hideControlGraphViewer() {
		getControlGraphController().getViewer().setVisible(false);
	}

	protected FlexoControlGraphController getControlGraphController() {
		if (_cgController == null) {
			_cgController = new FlexoControlGraphController(this);
			getWKFSelectionManager().addObserver(_cgController);
		}
		return _cgController;
	}

	/**
	 *
	 */
	@Override
	public void initInspectors() {
		super.initInspectors();
		if (useOldInspectorScheme()) {
			getWKFSelectionManager().addObserver(getSharedInspectorController());
			getWKFSelectionManager().addObserver(getDocInspectorController());
			notifyShowLeanTabHasChanged();
			showBPEGraphicsInspectors();
		}
	}

	@Override
	public void dispose() {
		if (getWKFSelectionManager() != null) {
			getWKFSelectionManager().deleteObserver(getSharedInspectorController());
			getWKFSelectionManager().deleteObserver(getDocInspectorController());
			_selectionManager = null;
		}
		super.dispose();
	}

	public void loadRelativeWindows() {
		_workflowBrowserWindow = new WorkflowBrowserWindow(getFlexoFrame());
		_workflowBrowserWindow.setVisible(false);
		_processBrowserWindow = new ProcessBrowserWindow(getFlexoFrame());
	}

	@Override
	public ValidationModel getDefaultValidationModel() {
		return getProject().getWKFValidationModel();
	}

	public WorkflowBrowserWindow getWorkflowBrowserWindow() {
		return _workflowBrowserWindow;
	}

	public ProcessBrowserWindow getProcessBrowserWindow() {
		return _processBrowserWindow;
	}

	public void showProcessBrowser() {
		if (getMainPane() != null) {
			getMainPane().showProcessBrowser();
		}
	}

	public void hideProcessBrowser() {
		if (getMainPane() != null) {
			getMainPane().hideProcessBrowser();
		}
	}

	@Override
	protected FlexoMainPane createMainPane() {
		return new WKFMainPane(this);
	}

	@Override
	public WKFMainPane getMainPane() {
		return (WKFMainPane) super.getMainPane();
	}

	@Override
	public SelectionManager getSelectionManager() {
		return getWKFSelectionManager();
	}

	public WKFSelectionManager getWKFSelectionManager() {
		return _selectionManager;
	}

	/**
	 * Select the view representing supplied object, if this view exists. Try all to really display supplied object, even if required view
	 * is not the current displayed view
	 * 
	 * @param object
	 *            the object to focus on
	 */
	@Override
	public void selectAndFocusObject(FlexoModelObject object) {
		if (object instanceof WKFObject) {
			setCurrentFlexoProcess(((WKFObject) object).getProcess());
			getSelectionManager().setSelectedObject(object);
		}
	}

	public void setCurrentFlexoProcess(FlexoProcess process) {
		if (getCurrentPerspective() == PROCESS_EDITOR_PERSPECTIVE || getCurrentPerspective() == SWIMMING_LANE_PERSPECTIVE
				|| getCurrentPerspective() == DOCUMENTATION_PERSPECTIVE) {
			if (process.isImported()) {
				setCurrentImportedProcess(process);
			} else {
				setCurrentEditedObjectAsModuleView(process);
			}
		}
	}

	@Override
	public ModuleView setCurrentEditedObjectAsModuleView(FlexoModelObject object, FlexoPerspective perspective) {
		if (object instanceof FlexoProcess && ((FlexoProcess) object).isImported()) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.warning("Trying to set an imported process as current module view: returning!");
			}
			return null;
		}

		return super.setCurrentEditedObjectAsModuleView(object, perspective);
	}

	public void setCurrentImportedProcess(FlexoProcess subProcess) {
		// TODO Auto-generated method stub
		System.out.println("WKFController.setCurrentImportedProcess : please implement something here !");
	}

	public FlexoProcess getCurrentFlexoProcess() {
		if (getCurrentDisplayedObjectAsModuleView() instanceof FlexoProcess) {
			return (FlexoProcess) getCurrentDisplayedObjectAsModuleView();
		}
		return null;
	}

	public WKFDocumentationView getCurrentWKFDocumentationView() {
		ModuleView currentView = getCurrentModuleView();
		if (currentView instanceof WKFDocumentationView) {
			return (WKFDocumentationView) getCurrentModuleView();
		} else {
			return null;
		}
	}

	// ==========================================================================
	// ============================= Browsers ==================================
	// ==========================================================================

	public ProcessBrowser getProcessBrowser() {
		return _processBrowser;
	}

	public RoleListBrowser getRoleListBrowser() {
		return _roleListBrowser;
	}

	public ProcessBrowser getExternalProcessBrowser() {
		return _externalProcessBrowser;
	}

	public WorkflowBrowser getWorkflowBrowser() {
		return _workflowBrowser;
	}

	/**
	 * @param b
	 */
	public void notifyShowWOName(boolean b) {
		updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	private boolean showMessages = false;

	/**
	 * @param b
	 */
	public void notifyShowMessages(boolean b) {
		showMessages = b;
		// GPO: I commented the code hereunder because it is just not implemented anywhere.
		// updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	private void updateGraphicalRepresentationWithNewWKFPreferenceSettings() {
		for (ModuleView<?> moduleView : getLoadedViews().values()) {
			if (moduleView instanceof DrawingView && ((DrawingView<?>) moduleView).getDrawing() instanceof DefaultDrawing) {
				DefaultDrawing<?> drawing = (DefaultDrawing<?>) ((DrawingView<?>) moduleView).getDrawing();
				Enumeration<GraphicalRepresentation<?>> en = drawing.getAllGraphicalRepresentations();
				while (en.hasMoreElements()) {
					GraphicalRepresentation<?> gr = en.nextElement();
					if (gr instanceof WKFObjectGR<?>) {
						((WKFObjectGR<?>) gr).updatePropertiesFromWKFPreferences();
					} else if (gr instanceof org.openflexo.wkf.swleditor.gr.WKFObjectGR<?>) {
						((org.openflexo.wkf.swleditor.gr.WKFObjectGR<?>) gr).updatePropertiesFromWKFPreferences();
					} else if (gr instanceof EdgeGR<?>) {
						((EdgeGR<?>) gr).updatePropertiesFromWKFPreferences();
					} else if (gr instanceof org.openflexo.wkf.swleditor.gr.EdgeGR<?>) {
						((org.openflexo.wkf.swleditor.gr.EdgeGR<?>) gr).updatePropertiesFromWKFPreferences();
					}
				}
			}
		}
	}

	public void notifyShowShadowChanged() {
		updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	public void notifyUseTransparencyChanged() {
		updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	public void notifyActivityFontChanged() {
		updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	public void notifyOperationFontChanged() {
		updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	public void notifyActionFontChanged() {
		updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	public void notifyEventFontChanged() {
		updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	public void notifyRoleFontChanged() {
		updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	public void notifyComponentFontChanged() {
		updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	public void notifyEdgeFontChanged() {
		updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	public void notifyArtefactFontChanged() {
		updateGraphicalRepresentationWithNewWKFPreferenceSettings();
	}

	@Override
	public boolean handleException(InspectableObject inspectable, String propertyName, Object value, Throwable exception) {
		if (inspectable instanceof Role && exception instanceof DuplicateRoleException) {
			boolean isOK = false;
			while (!isOK) {
				String newRoleName = FlexoController.askForString(FlexoLocalization
						.localizedForKey("sorry_role_already_exists_please_choose_an_other_name"));
				if (newRoleName == null) {
					isOK = true;
				} else {
					try {
						((Role) inspectable).setName(newRoleName);
						isOK = true;
					} catch (DuplicateRoleException e) {
						isOK = false;
					}
				}
			}
			return true;
		}

		if (inspectable instanceof Status && exception instanceof DuplicateStatusException) {
			boolean isOK = false;
			while (!isOK) {
				String newStatusName = FlexoController.askForString(FlexoLocalization
						.localizedForKey("sorry_status_already_exists_please_choose_an_other_name"));
				if (newStatusName == null) {
					isOK = true;
				} else {
					try {
						((Status) inspectable).setName(newStatusName);
						isOK = true;
					} catch (DuplicateStatusException e) {
						isOK = false;
					}
				}
			}
			return true;
		}

		if (inspectable instanceof OperationNode && exception instanceof DuplicateResourceException) {
			if (propertyName.equals("WOComponentName")) {
				boolean isOK = false;
				while (!isOK) {
					String newName = askForStringMatchingPattern(
							FlexoLocalization.localizedForKey("invalid_name_component_already_exists_please_choose_an_other_one"),
							IERegExp.JAVA_CLASS_NAME_PATTERN,
							FlexoLocalization.localizedForKey("must_start_with_a_letter_followed_by_any_letter_or_number"));
					if (newName != null) {
						try {
							((OperationNode) inspectable).setWOComponentName(newName);
							isOK = true;
						} catch (DuplicateResourceException e) {
						} catch (OperationAssociatedWithComponentSuccessfully e) {
							handleException(inspectable, "WOComponentName", value, e);
						}
					} else {
						return true;
					}
				}
				return true;
			}
		} else if (exception instanceof OperationAssociatedWithComponentSuccessfully) {
			WKFExceptionHandler.handleAssociation((OperationAssociatedWithComponentSuccessfully) exception, this);
			return true;
		}
		return super.handleException(inspectable, propertyName, value, exception);
	}

	@Override
	public PrintManager getPrintManager() {
		return PrintManager.getPrintManager();
	}

	// private Rectangle drawnRectangle = new Rectangle();
	// private JComponent previousTarget;

	public boolean showMessages() {
		return showMessages;
	}

	@Override
	public String getWindowTitleforObject(FlexoModelObject object) {
		if (object instanceof FlexoProcess) {
			return ((FlexoProcess) object).getName();
		} else if (object instanceof RoleList) {
			return FlexoLocalization.localizedForKeyWithParams("roles_defined_for_project_($0)", getProject().getName());
		}
		return null;
	}

	private void showBPEGraphicsInspectors() {
		getWKFSelectionManager().setInspectionContext("BPE", true);
		getWKFSelectionManager().removeInspectionContext("SWL");
		getWKFSelectionManager().removeInspectionContext("ROLE_EDITOR");
		if (getInspectorWindow() != null) {
			getInspectorWindow().getContent().refresh();
		}
	}

	private void showRoleEditorGraphicsInspectors() {
		getWKFSelectionManager().setInspectionContext("ROLE_EDITOR", true);
		getWKFSelectionManager().removeInspectionContext("BPE");
		getWKFSelectionManager().removeInspectionContext("SWL");
		if (getInspectorWindow() != null) {
			getInspectorWindow().getContent().refresh();
		}
	}

	private void showSWLGraphicsInspectors() {
		getWKFSelectionManager().setInspectionContext("SWL", true);
		getWKFSelectionManager().removeInspectionContext("BPE");
		getWKFSelectionManager().removeInspectionContext("ROLE_EDITOR");
		if (getInspectorWindow() != null) {
			getInspectorWindow().getContent().refresh();
		}
	}

	@Override
	public void switchToPerspective(FlexoPerspective perspective) {
		super.switchToPerspective(perspective);
		if (perspective == PROCESS_EDITOR_PERSPECTIVE) {
			showBPEGraphicsInspectors();
		}
		if (perspective == SWIMMING_LANE_PERSPECTIVE) {
			showSWLGraphicsInspectors();
		}
		if (perspective == ROLE_EDITOR_PERSPECTIVE) {
			showRoleEditorGraphicsInspectors();
		}
	}

	/*
	 * private static final String WORKFLOW_LEAN_TAB_NAME="Metrics Definition"; private static final String
	 * PROCESS_LEAN_TAB_NAME="Process Metrics"; private static final String ACTIVITY_LEAN_TAB_NAME="Activity Metrics"; private static final
	 * String OPERATION_LEAN_TAB_NAME="Operation Metrics"; private static final String EDGE_LEAN_TAB_NAME="Edge Metrics";
	 */

	public void notifyShowLeanTabHasChanged() {
		if (WKFPreferences.getShowLeanTabs()) {
			getWKFSelectionManager().setInspectionContext("METRICS", true);
			/*
			 * getInspectorWindow().getContent().showTabWithNameInInspectorNamed(WORKFLOW_LEAN_TAB_NAME, Inspectors.WKF.WORKFLOW_INSPECTOR);
			 * getInspectorWindow().getContent().showTabWithNameInInspectorNamed(PROCESS_LEAN_TAB_NAME,
			 * Inspectors.WKF.FLEXO_PROCESS_INSPECTOR);
			 * getInspectorWindow().getContent().showTabWithNameInInspectorNamed(ACTIVITY_LEAN_TAB_NAME,
			 * Inspectors.WKF.ABSTRACT_ACTIVITY_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().showTabWithNameInInspectorNamed(ACTIVITY_LEAN_TAB_NAME,
			 * Inspectors.WKF.BEGIN_ACTIVITY_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().showTabWithNameInInspectorNamed(ACTIVITY_LEAN_TAB_NAME,
			 * Inspectors.WKF.END_ACTIVITY_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().showTabWithNameInInspectorNamed(OPERATION_LEAN_TAB_NAME,
			 * Inspectors.WKF.OPERATION_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().showTabWithNameInInspectorNamed(OPERATION_LEAN_TAB_NAME,
			 * Inspectors.WKF.BEGIN_OPERATION_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().showTabWithNameInInspectorNamed(OPERATION_LEAN_TAB_NAME,
			 * Inspectors.WKF.END_OPERATION_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().showTabWithNameInInspectorNamed(EDGE_LEAN_TAB_NAME,
			 * Inspectors.WKF.POST_CONDITION_INSPECTOR);
			 */
		} else {
			getWKFSelectionManager().removeInspectionContext("METRICS");
			/*
			 * getInspectorWindow().getContent().hideTabWithNameInInspectorNamed(WORKFLOW_LEAN_TAB_NAME, Inspectors.WKF.WORKFLOW_INSPECTOR);
			 * getInspectorWindow().getContent().hideTabWithNameInInspectorNamed(PROCESS_LEAN_TAB_NAME,
			 * Inspectors.WKF.FLEXO_PROCESS_INSPECTOR);
			 * getInspectorWindow().getContent().hideTabWithNameInInspectorNamed(ACTIVITY_LEAN_TAB_NAME,
			 * Inspectors.WKF.ABSTRACT_ACTIVITY_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().hideTabWithNameInInspectorNamed(ACTIVITY_LEAN_TAB_NAME,
			 * Inspectors.WKF.BEGIN_ACTIVITY_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().hideTabWithNameInInspectorNamed(ACTIVITY_LEAN_TAB_NAME,
			 * Inspectors.WKF.END_ACTIVITY_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().hideTabWithNameInInspectorNamed(OPERATION_LEAN_TAB_NAME,
			 * Inspectors.WKF.OPERATION_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().hideTabWithNameInInspectorNamed(OPERATION_LEAN_TAB_NAME,
			 * Inspectors.WKF.BEGIN_OPERATION_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().hideTabWithNameInInspectorNamed(OPERATION_LEAN_TAB_NAME,
			 * Inspectors.WKF.END_OPERATION_NODE_INSPECTOR);
			 * getInspectorWindow().getContent().hideTabWithNameInInspectorNamed(EDGE_LEAN_TAB_NAME,
			 * Inspectors.WKF.POST_CONDITION_INSPECTOR);
			 */
		}
		if (getInspectorWindow() != null) {
			getInspectorWindow().getContent().refresh();
		}
	}

	public void notifyUseSimpleEventPaletteHasChanged() {
		JOptionPane.showMessageDialog(getPreferencesWindow(),
				FlexoLocalization.localizedForKey("You must restart OpenFlexo to enable this change."),
				FlexoLocalization.localizedForKey("Restart required"), JOptionPane.INFORMATION_MESSAGE);
	}

	public void notifyEdgeRepresentationChanged() {
		for (ModuleView<?> view : getLoadedViewsForPerspective(PROCESS_EDITOR_PERSPECTIVE).values()) {
			if (view instanceof ProcessView) {
				((ProcessView) view).refreshConnectors();
			}
		}
	}

	public FGEPoint getLastClickedPoint() {
		if (getCurrentModuleView() instanceof DrawingView) {
			return ((DrawingView<?>) getCurrentModuleView()).getController().getLastClickedPoint();
		}
		return null;
	}

	public void notifyShowGrid(boolean showGrid) {
		for (ModuleView<?> view : getLoadedViewsForPerspective(PROCESS_EDITOR_PERSPECTIVE).values()) {
			if (view instanceof ProcessView) {
				((ProcessView) view).getDrawingGraphicalRepresentation().setShowGrid(WKFPreferences.getShowGrid());
			}
		}
	}

	public JSplitPane getWorkflowProcessBrowserViews() {
		return _workflowProcessBrowserViews;
	}

	public RoleListBrowserView getRoleListBrowserView() {
		return roleListBrowserView;
	}

	public void setRoleListBrowserView(RoleListBrowserView _roleListBrowserView) {
		this.roleListBrowserView = _roleListBrowserView;
	}

	private void initWithWKFPreferences() {
		/*for (GraphicalProperties prop : GraphicalProperties.values()) {
			if (_controller.getFlexoWorkflow().hasGraphicalPropertyForKey(prop.getSerializationName())) {
				switch (prop) {
				case ACTION_FONT:
					setActionNodeFont(_controller.getProject().getFlexoWorkflow().getActionFont());
					break;
				case ACTIVITY_FONT:
					setActivityNodeFont(_controller.getProject().getFlexoWorkflow().getActivityFont());
					break;
				case COMPONENT_FONT:
					setComponentFont(_controller.getProject().getFlexoWorkflow().getComponentFont());
					break;
				case CONNECTOR_REPRESENTATION:
					try {
						setConnectorRepresentation((EdgeRepresentation) _controller.getProject().getFlexoWorkflow()
								.getConnectorRepresentation());
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case EVENT_FONT:
					setEventNodeFont(_controller.getProject().getFlexoWorkflow().getEventFont());
					break;
				case ARTEFACT_FONT:
					setArtefactFont(_controller.getProject().getFlexoWorkflow().getArtefactFont());
					break;
				case EDGE_FONT:
					setEdgeFont(_controller.getProject().getFlexoWorkflow().getEdgeFont());
					break;
				case OPERATION_FONT:
					setOperationNodeFont(_controller.getProject().getFlexoWorkflow().getOperationFont());
					break;
				case ROLE_FONT:
					setRoleFont(_controller.getProject().getFlexoWorkflow().getRoleFont());
					break;
				case SHOW_MESSAGES:
					setShowMessagesInWKF(_controller.getProject().getFlexoWorkflow().getShowMessages());
					break;
				case SHOW_SHADOWS:
					setShowShadows(_controller.getProject().getFlexoWorkflow().getShowShadows());
					break;
				case SHOW_WO_NAME:
					setShowWONameInWKF(_controller.getProject().getFlexoWorkflow().getShowWOName());
					break;
				case USE_TRANSPARENCY:
					setUseTransparency(_controller.getProject().getFlexoWorkflow().getUseTransparency());
					break;
				default:
					break;
				}
			}
		}
		_controller.notifyShowLeanTabHasChanged();
		_controller.notifyShowMessages(getShowMessagesInWKF());*/
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if (propertyName.equals(WKFPreferences.SHOW_WO_NAME_KEY)) {
			notifyShowWOName(WKFPreferences.getShowWONameInWKF());
		} else if (propertyName.equals(WKFPreferences.SHOW_MESSAGES_NAME_KEY)) {
			notifyShowMessages(WKFPreferences.getShowMessagesInWKF());
		} else if (propertyName.equals(WKFPreferences.SHOW_GRID)) {
			notifyShowGrid(WKFPreferences.getShowGrid());
		} else if (propertyName.equals(WKFPreferences.SHOW_SHADOWS)) {
			notifyShowShadowChanged();
		} else if (propertyName.equals(WKFPreferences.SHOW_LEAN_TAB)) {
			notifyShowLeanTabHasChanged();
		} else if (propertyName.equals(WKFPreferences.USE_SIMPLE_EVENT_PALETTE)) {
			notifyUseSimpleEventPaletteHasChanged();
		} else if (propertyName.equals(WKFPreferences.USE_TRANSPARENCY)) {
			notifyUseTransparencyChanged();
		} else if (propertyName.equals(WKFPreferences.ACTIVITY_NODE_FONT_KEY)) {
			notifyActivityFontChanged();
		} else if (propertyName.equals(WKFPreferences.OPERATION_NODE_FONT_KEY)) {
			notifyOperationFontChanged();
		} else if (propertyName.equals(WKFPreferences.ACTION_NODE_FONT_KEY)) {
			notifyActionFontChanged();
		} else if (propertyName.equals(WKFPreferences.EVENT_NODE_FONT_KEY)) {
			notifyEventFontChanged();
		} else if (propertyName.equals(WKFPreferences.ROLE_FONT_KEY)) {
			notifyRoleFontChanged();
		} else if (propertyName.equals(WKFPreferences.EDGE_FONT_KEY)) {
			notifyEdgeFontChanged();
		} else if (propertyName.equals(WKFPreferences.ARTEFACT_FONT_KEY)) {
			notifyArtefactFontChanged();
		} else if (propertyName.equals(WKFPreferences.COMPONENT_FONT_KEY)) {
			notifyComponentFontChanged();
		} else if (propertyName.equals(WKFPreferences.CONNECTOR_REPRESENTATION)) {
			notifyEdgeRepresentationChanged();
		} else if (propertyName.equals(WKFPreferences.CONNECTOR_ADJUSTABILITY)) {
			notifyEdgeRepresentationChanged();
		}

	}
}
