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
package org.openflexo.foundation.wkf.action;

import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoModelObject;
import org.openflexo.foundation.action.FlexoActionType;
import org.openflexo.foundation.wkf.FlexoProcess;
import org.openflexo.foundation.wkf.WKFObject;

public class AddProcessMetricsValue extends AddMetricsValue<AddProcessMetricsValue, FlexoProcess> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(AddProcessMetricsValue.class.getPackage().getName());

	public static FlexoActionType<AddProcessMetricsValue, FlexoProcess, WKFObject> actionType = new FlexoActionType<AddProcessMetricsValue, FlexoProcess, WKFObject>(
			"add_metrics_value", FlexoActionType.ADD_ACTION_TYPE) {

		@Override
		protected boolean isEnabledForSelection(FlexoProcess object, Vector<WKFObject> globalSelection) {
			return object != null && !object.isImported();
		}

		@Override
		protected boolean isVisibleForSelection(FlexoProcess object, Vector<WKFObject> globalSelection) {
			return false;
		}

		@Override
		public AddProcessMetricsValue makeNewAction(FlexoProcess focusedObject, Vector<WKFObject> globalSelection, FlexoEditor editor) {
			return new AddProcessMetricsValue(focusedObject, globalSelection, editor);
		}

	};

	static {
		FlexoModelObject.addActionForClass(actionType, FlexoProcess.class);
	}

	AddProcessMetricsValue(FlexoProcess focusedObject, Vector<WKFObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	protected void doAction(Object context) throws FlexoException {
		getFocusedObject().addToMetricsValues(createMetricsValue());
	}
}
