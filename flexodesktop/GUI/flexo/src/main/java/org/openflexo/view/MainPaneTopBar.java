package org.openflexo.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.Module;
import org.openflexo.module.ModuleLoader;
import org.openflexo.module.ModuleLoadingException;
import org.openflexo.swing.BarButton;
import org.openflexo.toolbox.PropertyChangeListenerRegistrationManager;
import org.openflexo.toolbox.ToolBox;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.ControllerModel;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.view.controller.model.HistoryLocation;

public class MainPaneTopBar extends JMenuBar {

	private static final java.util.logging.Logger logger = org.openflexo.logging.FlexoLogger.getLogger(MainPaneTopBar.class.getPackage()
			.getName());

	public static interface FlexoModelObjectRenderer {
		public String render(FlexoModelObject object);
	}

	private PropertyChangeListenerRegistrationManager registrationManager;

	private ControllerModel model;

	private JPanel left;
	private JPanel center;
	private JPanel right;

	private JComponent header;

	private JButton leftViewToggle;

	private JButton rightViewToggle;

	private JPanel perspectives;

	protected FlexoModelObjectRenderer renderer;

	private boolean forcePreferredSize;

	private FlexoController controller;

	public class TabHeaderContainer extends JPanel implements PropertyChangeListener {

		private Map<ModuleView<?>, ViewTabHeader> tabHeaders = new HashMap<ModuleView<?>, MainPaneTopBar.ViewTabHeader>();

		private List<ViewTabHeader> headers = new ArrayList<MainPaneTopBar.ViewTabHeader>();

		public TabHeaderContainer() {
			registrationManager.new PropertyChangeListenerRegistration(FlexoController.MODULE_VIEWS, this, controller);
			registrationManager.new PropertyChangeListenerRegistration(ControllerModel.CURRENT_OBJECT, this, model);
			registrationManager.new PropertyChangeListenerRegistration(ControllerModel.CURRENT_PERPSECTIVE, this, model);
			setLayout(new LayoutManager() {

				@Override
				public Dimension preferredLayoutSize(Container parent) {
					synchronized (parent.getTreeLock()) {
						Dimension dim = new Dimension(0, 0);
						int nmembers = parent.getComponentCount();
						for (int i = 0; i < nmembers; i++) {
							Component m = parent.getComponent(i);
							if (m.isVisible()) {
								Dimension d = m.getPreferredSize();
								dim.height = Math.max(dim.height, d.height);
								dim.width += d.width;

							}
						}
						Insets insets = parent.getInsets();
						dim.width += insets.left + insets.right;
						dim.height += insets.top + insets.bottom;
						return dim;
					}
				}

				@Override
				public Dimension minimumLayoutSize(Container parent) {
					return new Dimension(0, 0);
				}

				@Override
				public void layoutContainer(Container target) {
					synchronized (target.getTreeLock()) {
						Insets insets = target.getInsets();
						int maxwidth = target.getWidth() - (insets.left + insets.right);
						int nmembers = target.getComponentCount();
						int x = 0, y = insets.top;
						int rowh = 0, start = 0;

						boolean ltr = target.getComponentOrientation().isLeftToRight();

						for (int i = 0; i < nmembers; i++) {
							Component m = target.getComponent(i);
							if (m.isVisible()) {
								/*Dimension d = m.getPreferredSize();
								m.setSize(d.width, d.height);
								if (x == 0 || x + d.width <= maxwidth) {
									x += d.width;
									rowh = Math.max(rowh, d.height);
								} else {
									rowh = moveComponents(target, insets.left + hgap, y, maxwidth - x, rowh, start, i, ltr, useBaseline,
											ascent, descent);
									x = d.width;
									y += vgap + rowh;
									rowh = d.height;
									start = i;
								}*/
							}
						}
						/*moveComponents(target, insets.left + hgap, y, maxwidth - x, rowh, start, nmembers, ltr, useBaseline, ascent,
								descent);*/
					}
				}

				@Override
				public void addLayoutComponent(String name, Component comp) {

				}

				@Override
				public void removeLayoutComponent(Component comp) {

				}
			});
		}

		@Override
		public void remove(int index) {
			Component c = getComponent(index);
			headers.remove(c);
			super.remove(index);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() == controller) {
				if (evt.getPropertyName().equals(FlexoController.MODULE_VIEWS)) {
					if (evt.getNewValue() != null) {// New Module view
						tabHeaders.put((ModuleView<?>) evt.getNewValue(), new ViewTabHeader((ModuleView<?>) evt.getNewValue()));
					} else if (evt.getOldValue() != null) { // Module view deleted
						ViewTabHeader viewTabHeader = tabHeaders.get(evt.getOldValue());
						if (viewTabHeader != null) {
							viewTabHeader.delete();
						}
					}
				}
			} else if (evt.getSource() == model) {
				if (evt.getPropertyName().equals(ControllerModel.CURRENT_OBJECT)) {
					ModuleView<?> view = controller.moduleViewForObject(model.getCurrentObject());
					if (view != null) {

					}
				}
			}
		}
	}

	private class ViewTabHeader extends JPanel implements PropertyChangeListener, ActionListener {
		private final FlexoModelObject object;

		private JLabel text;
		private JButton close;
		private final ModuleView<?> view;

		public ViewTabHeader(ModuleView<?> view) {
			super(new BorderLayout());
			this.view = view;
			this.object = view.getRepresentedObject();
			text = new JLabel();
			text.setHorizontalTextPosition(JLabel.RIGHT);
			/*close = new BarButton(IconLibrary.CLOSE_ICON);
			close.setRolloverIcon(IconLibrary.CLOSE_HOVER_ICON);*/
			close.addActionListener(this);
			updateText();
			registrationManager.new PropertyChangeListenerRegistration(FlexoModelObject.DELETED_PROPERTY, this, object);
			add(text);
			add(close, BorderLayout.EAST);
			// setBorder(new TabHeaderBorder());
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == close) {
				view.deleteModuleView();
			}
		}

		protected void updateText() {
			text.setText(renderer.render(object));
		}

		public String getLabelText() {
			return text.getText();
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() == object) {
				if (evt.getPropertyName().equals(FlexoModelObject.DELETED_PROPERTY)) {
					delete();
				} else {
					updateText();
				}
			}
		}

		public void delete() {
			JComponent parent = (JComponent) getParent();
			parent.remove(this);
			parent.revalidate();
			registrationManager.removeListener(FlexoModelObject.DELETED_PROPERTY, this, object);
		}
	}

	public MainPaneTopBar(FlexoController controller, FlexoModelObjectRenderer renderer) {
		this.controller = controller;
		this.model = controller.getControllerModel();
		this.renderer = renderer;
		registrationManager = new PropertyChangeListenerRegistrationManager();
		setLayout(new BorderLayout());
		this.forcePreferredSize = ToolBox.getPLATFORM() != ToolBox.MACOS;
		add(left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)), BorderLayout.WEST);
		add(center = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)));
		add(right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)), BorderLayout.EAST);
		left.setOpaque(false);
		center.setOpaque(false);
		right.setOpaque(false);
		initLeftRightViewVisibilityControls();
		initModules();
		initNavigationControls();
		initModuleViewTabHeaders();
		initPerspectives();
	}

	public void delete() {
		registrationManager.delete();
	}

	private void initModules() {
		for (final Module module : model.getModuleLoader().getAvailableModules()) {
			final JButton button = new BarButton(module.getMediumIcon());
			button.setEnabled(true);
			button.setFocusable(false);
			if (forcePreferredSize) {
				button.setPreferredSize(new Dimension(button.getIcon().getIconWidth() + 4, button.getIcon().getIconHeight() + 4));
			}
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						model.getModuleLoader().switchToModule(module);
					} catch (ModuleLoadingException e1) {
						e1.printStackTrace();
						FlexoController.notify(e1.getLocalizedMessage());
					}
				}
			});
			PropertyChangeListener listener = new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					button.setIcon(model.getModuleLoader().isLoaded(module) ? module.getMediumIconWithHover() : module.getMediumIcon());
					button.setSelected(model.getModuleLoader().isActive(module));
				}
			};
			registrationManager.new PropertyChangeListenerRegistration(ModuleLoader.ACTIVE_MODULE, listener, model.getModuleLoader());
			registrationManager.new PropertyChangeListenerRegistration(ModuleLoader.MODULE_ACTIVATED, listener, model.getModuleLoader());
			registrationManager.new PropertyChangeListenerRegistration(ModuleLoader.MODULE_LOADED, listener, model.getModuleLoader());
			registrationManager.new PropertyChangeListenerRegistration(ModuleLoader.MODULE_UNLOADED, listener, model.getModuleLoader());
			left.add(button);
		}
	}

	private void initNavigationControls() {
		final JButton backwardButton = new BarButton(IconLibrary.NAVIGATION_BACKWARD_ICON);
		if (forcePreferredSize) {
			backwardButton.setPreferredSize(new Dimension(24, 24));
		}
		backwardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.historyBack();
			}
		});
		final JButton forwardButton = new BarButton(IconLibrary.NAVIGATION_FORWARD_ICON);
		if (forcePreferredSize) {
			forwardButton.setPreferredSize(new Dimension(24, 24));
		}
		forwardButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.historyForward();
			}
		});
		final JButton upButton = new BarButton(IconLibrary.NAVIGATION_UP_ICON);
		if (forcePreferredSize) {
			upButton.setPreferredSize(new Dimension(24, 24));
		}
		upButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.goUp();
			}
		});
		PropertyChangeListener listener = new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateNavigationControlState(backwardButton, forwardButton, upButton);
			}

		};
		registrationManager.new PropertyChangeListenerRegistration(ControllerModel.CURRENT_LOCATION, listener, model);
		registrationManager.new PropertyChangeListenerRegistration(ControllerModel.CURRENT_EDITOR, listener, model);
		left.add(backwardButton);
		left.add(upButton);
		left.add(forwardButton);
		updateNavigationControlState(backwardButton, forwardButton, upButton);
	}

	protected void updateNavigationControlState(final JButton backwardButton, final JButton forwardButton, final JButton upButton) {
		backwardButton.setEnabled(model.canGoBack());
		forwardButton.setEnabled(model.canGoForward());
		upButton.setEnabled(model.canGoUp());
	}

	private void initModuleViewTabHeaders() {

		for (HistoryLocation hl : model.getPreviousHistory()) {

		}
	}

	private void initPerspectives() {
		right.add(perspectives = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0)), 0);
		perspectives.setOpaque(false);
		registrationManager.new PropertyChangeListenerRegistration(ControllerModel.PERSPECTIVES, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getNewValue() != null) {
					insertPerspective((FlexoPerspective) evt.getNewValue());
				} else {
					if (logger.isLoggable(Level.WARNING)) {
						logger.warning("Perspective removal not supported by top bar.");
					}
				}
			}
		}, model);
		for (final FlexoPerspective p : model.getPerspectives()) {
			insertPerspective(p);
		}
	}

	private void insertPerspective(final FlexoPerspective p) {
		final JButton button = new BarButton(p.getActiveIcon());
		if (forcePreferredSize) {
			int size = Math.max(button.getIcon().getIconWidth() + 8, button.getIcon().getIconHeight() + 4);
			button.setPreferredSize(new Dimension(size, size));
		}
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCurrentPerspective(p);
			}
		});
		registrationManager.new PropertyChangeListenerRegistration(ControllerModel.CURRENT_LOCATION, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateIconForButtonPerspective(button, p);
			}
		}, model);
		updateIconForButtonPerspective(button, p);
		perspectives.add(button);
	}

	protected void updateIconForButtonPerspective(JButton buttonPerspective, FlexoPerspective p) {
		buttonPerspective.setSelected(model.getCurrentPerspective() == p);
	}

	private void initLeftRightViewVisibilityControls() {
		leftViewToggle = getToggleVisibilityButton();
		leftViewToggle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.setLeftViewVisible(!model.isLeftViewVisible());
			}
		});
		left.add(leftViewToggle, 0);
		registrationManager.new PropertyChangeListenerRegistration(ControllerModel.LEFT_VIEW_VISIBLE, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateLeftViewToggleIcon();
			}
		}, model);
		updateLeftViewToggleIcon();
		rightViewToggle = getToggleVisibilityButton();
		rightViewToggle.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.setRightViewVisible(!model.isRightViewVisible());
			}
		});
		right.add(rightViewToggle);
		registrationManager.new PropertyChangeListenerRegistration(ControllerModel.RIGHT_VIEW_VISIBLE, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				updateRightViewToggleIcon();
			}
		}, model);
		updateRightViewToggleIcon();

	}

	protected void updateLeftViewToggleIcon() {
		leftViewToggle.setIcon(model.isLeftViewVisible() ? IconLibrary.TOGGLE_ARROW_BOTTOM_ICON : IconLibrary.TOGGLE_ARROW_TOP_ICON);
		leftViewToggle.setRolloverIcon(model.isLeftViewVisible() ? IconLibrary.TOGGLE_ARROW_BOTTOM_SELECTED_ICON
				: IconLibrary.TOGGLE_ARROW_TOP_SELECTED_ICON);
	}

	protected void updateRightViewToggleIcon() {
		rightViewToggle.setIcon(model.isRightViewVisible() ? IconLibrary.TOGGLE_ARROW_BOTTOM_ICON : IconLibrary.TOGGLE_ARROW_TOP_ICON);
		rightViewToggle.setRolloverIcon(model.isRightViewVisible() ? IconLibrary.TOGGLE_ARROW_BOTTOM_SELECTED_ICON
				: IconLibrary.TOGGLE_ARROW_TOP_SELECTED_ICON);
	}

	private JButton getToggleVisibilityButton() {
		final JButton button = new BarButton(IconLibrary.TOGGLE_ARROW_TOP_ICON);
		button.setRolloverIcon(IconLibrary.TOGGLE_ARROW_TOP_SELECTED_ICON);
		if (forcePreferredSize) {
			button.setPreferredSize(new Dimension(button.getIcon().getIconWidth() + 2, button.getIcon().getIconHeight() + 20));
		}
		return button;
	}

	public void setHeader(JComponent header) {
		if (this.header != header) {
			if (this.header != null) {
				right.remove(this.header);
				right.revalidate();
			}
			this.header = header;
			if (header != null) {
				right.add(header, 0);
				right.revalidate();
			}
		}
	}

	public void setLeftViewToggle(boolean visible) {
		leftViewToggle.setEnabled(visible);
	}

	public void setRightViewToggle(boolean visible) {
		rightViewToggle.setEnabled(visible);
	}

}