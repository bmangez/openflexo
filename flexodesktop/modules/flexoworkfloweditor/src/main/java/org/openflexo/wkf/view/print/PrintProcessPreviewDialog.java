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
package org.openflexo.wkf.view.print;

import java.util.logging.Logger;

import org.openflexo.print.FlexoPrintableComponent;
import org.openflexo.print.PrintPreviewDialog;
import org.openflexo.view.ModuleView;
import org.openflexo.wkf.controller.WKFController;

public class PrintProcessPreviewDialog extends PrintPreviewDialog {

	protected static final Logger logger = Logger.getLogger(PrintProcessPreviewDialog.class.getPackage().getName());

	private boolean isPrintDialogDisposed;

	public PrintProcessPreviewDialog(WKFController controller, FlexoPrintableComponent printableProcessView) {
		super(controller, printableProcessView);
		setIsPrintDialogDispose(false);
	}

	/**
	 *
	 */
	private void setIsPrintDialogDispose(boolean b) {
		isPrintDialogDisposed = b;
	}

	@Override
	public void dispose() {
		isPrintDialogDisposed |= !isVisible();
		super.dispose();
		if (!isPrintDialogDisposed) {
			setIsPrintDialogDispose(true);
			if (getPrintableComponent() instanceof ModuleView)
				((ModuleView) getPrintableComponent()).deleteModuleView();
		}
	}

}
