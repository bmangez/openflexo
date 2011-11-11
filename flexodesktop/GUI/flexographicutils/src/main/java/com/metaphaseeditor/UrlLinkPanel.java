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

/**
 * 
 * @author Rudolf Visagie
 */
public class UrlLinkPanel extends javax.swing.JPanel {

	/** Creates new form UrlLinkPanel */
	public UrlLinkPanel() {
		initComponents();

		linkTargetComboBox.removeAllItems();
		LinkTarget[] linkTargets = LinkTarget.values();
		for (int i = 0; i < linkTargets.length; i++) {
			linkTargetComboBox.addItem(linkTargets[i]);
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
	 * method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		urlTextField = new javax.swing.JTextField();
		urlLabel = new javax.swing.JLabel();
		linkTargetLabel = new javax.swing.JLabel();
		linkTargetComboBox = new javax.swing.JComboBox();

		urlLabel.setText("URL");

		linkTargetLabel.setText("Link Target");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(linkTargetLabel)
										.addComponent(urlLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(urlTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
										.addComponent(linkTargetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(urlLabel)
										.addComponent(urlTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(
								layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(linkTargetLabel)
										.addComponent(linkTargetComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(243, Short.MAX_VALUE)));
	}// </editor-fold>//GEN-END:initComponents

	public String getUrl() {
		return urlTextField.getText();
	}

	public LinkTarget getLinkTarget() {
		return (LinkTarget) linkTargetComboBox.getSelectedItem();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JComboBox linkTargetComboBox;
	private javax.swing.JLabel linkTargetLabel;
	private javax.swing.JLabel urlLabel;
	private javax.swing.JTextField urlTextField;
	// End of variables declaration//GEN-END:variables

}
