/**
 * Metaphase Editor - WYSIWYG HTML Editor Component
 * Copyright (C) 2010  Rudolf Visagie
 * Full text of license can be found in com/metaphaseeditor/LICENSE.txt
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * The author can be contacted at metaphase.editor@gmail.com.
 */

package com.metaphaseeditor;

import org.openflexo.toolbox.ImageIconResource;

/**
 * 
 * @author Rudolf Visagie
 */
public class DivDialog extends javax.swing.JDialog {

	private String divHtml = null;

	/** Creates new form DivDialog */
	public DivDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		setIconImage(new ImageIconResource("Icons/MetaphaseEditor/icons/metaphase16x16.png").getImage());

		setLocationRelativeTo(null);

		languageDirectionComboBox.removeAllItems();
		LanguageDirection[] languageDirections = LanguageDirection.values();
		for (int i = 0; i < languageDirections.length; i++) {
			languageDirectionComboBox.addItem(languageDirections[i]);
		}
	}

	public String showDialog() {
		setVisible(true);
		return divHtml;
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
	 * method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		mainTabbedPane = new javax.swing.JTabbedPane();
		generalPanel = new javax.swing.JPanel();
		stylesheetClassesLabel = new javax.swing.JLabel();
		stylesheetClassesTextField = new javax.swing.JTextField();
		advancedPanel = new javax.swing.JPanel();
		idLabel = new javax.swing.JLabel();
		idTextField = new javax.swing.JTextField();
		languageCodeLabel = new javax.swing.JLabel();
		styleLabel = new javax.swing.JLabel();
		languageCodeTextField = new javax.swing.JTextField();
		styleTextField = new javax.swing.JTextField();
		advisoryTitleLabel = new javax.swing.JLabel();
		advisoryTitleTextField = new javax.swing.JTextField();
		languageDirectionLabel = new javax.swing.JLabel();
		languageDirectionComboBox = new javax.swing.JComboBox();
		okButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Create Div");

		stylesheetClassesLabel.setText("Stylesheet Classes");

		javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
		generalPanel.setLayout(generalPanelLayout);
		generalPanelLayout.setHorizontalGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				generalPanelLayout.createSequentialGroup().addContainerGap().addComponent(stylesheetClassesLabel)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(stylesheetClassesTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
						.addContainerGap()));
		generalPanelLayout.setVerticalGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				generalPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								generalPanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(stylesheetClassesLabel)
										.addComponent(stylesheetClassesTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(126, Short.MAX_VALUE)));

		mainTabbedPane.addTab("General", generalPanel);

		idLabel.setText("ID");

		languageCodeLabel.setText("Language Code");

		styleLabel.setText("Style");

		advisoryTitleLabel.setText("Advisory Title");

		languageDirectionLabel.setText("Language Direction");

		languageDirectionComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "Left to Right", "Right to Left" }));

		javax.swing.GroupLayout advancedPanelLayout = new javax.swing.GroupLayout(advancedPanel);
		advancedPanel.setLayout(advancedPanelLayout);
		advancedPanelLayout.setHorizontalGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						advancedPanelLayout
								.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										advancedPanelLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														advancedPanelLayout
																.createSequentialGroup()
																.addComponent(languageDirectionLabel)
																.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(languageDirectionComboBox,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 85,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGroup(
														advancedPanelLayout
																.createSequentialGroup()
																.addGroup(
																		advancedPanelLayout
																				.createParallelGroup(
																						javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(styleLabel).addComponent(advisoryTitleLabel)
																				.addComponent(idLabel).addComponent(languageCodeLabel))
																.addGap(27, 27, 27)
																.addGroup(
																		advancedPanelLayout
																				.createParallelGroup(
																						javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(languageCodeTextField,
																						javax.swing.GroupLayout.DEFAULT_SIZE, 273,
																						Short.MAX_VALUE)
																				.addComponent(idTextField,
																						javax.swing.GroupLayout.DEFAULT_SIZE, 273,
																						Short.MAX_VALUE)
																				.addComponent(styleTextField,
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						javax.swing.GroupLayout.DEFAULT_SIZE, 273,
																						Short.MAX_VALUE)
																				.addComponent(advisoryTitleTextField,
																						javax.swing.GroupLayout.Alignment.TRAILING,
																						javax.swing.GroupLayout.DEFAULT_SIZE, 273,
																						Short.MAX_VALUE)))).addContainerGap()));
		advancedPanelLayout.setVerticalGroup(advancedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				advancedPanelLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								advancedPanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(idLabel)
										.addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								advancedPanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(styleLabel)
										.addComponent(styleTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								advancedPanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(advisoryTitleTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(advisoryTitleLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								advancedPanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(languageCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(languageCodeLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								advancedPanelLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(languageDirectionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(languageDirectionLabel)).addContainerGap(22, Short.MAX_VALUE)));

		mainTabbedPane.addTab("Advanced", advancedPanel);

		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(mainTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup().addContainerGap(254, Short.MAX_VALUE).addComponent(okButton)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton)
								.addContainerGap()));

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { cancelButton, okButton });

		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(mainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cancelButton)
										.addComponent(okButton)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okButtonActionPerformed
		String id = idTextField.getText();
		String stylesheetClasses = stylesheetClassesTextField.getText();
		String style = styleTextField.getText();
		String advisoryTitle = advisoryTitleTextField.getText();
		String languageCode = languageCodeTextField.getText();
		LanguageDirection languageDirection = (LanguageDirection) languageDirectionComboBox.getSelectedItem();
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div");
		if (id.length() > 0) {
			buffer.append(" id='");
			buffer.append(id);
			buffer.append("'");
		}
		if (stylesheetClasses.length() > 0) {
			buffer.append(" class='");
			buffer.append(stylesheetClasses);
			buffer.append("'");
		}
		if (style.length() > 0) {
			buffer.append(" style='");
			buffer.append(style);
			buffer.append("'");
		}
		if (advisoryTitle.length() > 0) {
			buffer.append(" title='");
			buffer.append(advisoryTitle);
			buffer.append("'");
		}
		if (languageCode.length() > 0) {
			buffer.append(" lang='");
			buffer.append(languageCode);
			buffer.append("'");
		}
		if (languageDirection.getAttrValue() != null) {
			buffer.append(" dir='");
			buffer.append(languageDirection.getAttrValue());
			buffer.append("'");
		}
		buffer.append(">TODO: modify div contents</div>");
		divHtml = buffer.toString();
		setVisible(false);
	}// GEN-LAST:event_okButtonActionPerformed

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelButtonActionPerformed
		setVisible(false);
	}// GEN-LAST:event_cancelButtonActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel advancedPanel;
	private javax.swing.JLabel advisoryTitleLabel;
	private javax.swing.JTextField advisoryTitleTextField;
	private javax.swing.JButton cancelButton;
	private javax.swing.JPanel generalPanel;
	private javax.swing.JLabel idLabel;
	private javax.swing.JTextField idTextField;
	private javax.swing.JLabel languageCodeLabel;
	private javax.swing.JTextField languageCodeTextField;
	private javax.swing.JComboBox languageDirectionComboBox;
	private javax.swing.JLabel languageDirectionLabel;
	private javax.swing.JTabbedPane mainTabbedPane;
	private javax.swing.JButton okButton;
	private javax.swing.JLabel styleLabel;
	private javax.swing.JTextField styleTextField;
	private javax.swing.JLabel stylesheetClassesLabel;
	private javax.swing.JTextField stylesheetClassesTextField;
	// End of variables declaration//GEN-END:variables

}
