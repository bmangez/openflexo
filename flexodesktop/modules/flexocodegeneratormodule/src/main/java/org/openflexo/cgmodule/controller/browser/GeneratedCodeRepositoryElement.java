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
package org.openflexo.cgmodule.controller.browser;

import org.openflexo.components.browser.BrowserElement;
import org.openflexo.components.browser.BrowserElementType;
import org.openflexo.components.browser.ProjectBrowser;
import org.openflexo.foundation.DataModification;
import org.openflexo.foundation.FlexoObservable;
import org.openflexo.foundation.cg.CGSymbolicDirectory;
import org.openflexo.foundation.cg.GenerationRepository;
import org.openflexo.foundation.cg.dm.CGStructureRefreshed;
import org.openflexo.foundation.cg.dm.LongOperationStarted;
import org.openflexo.foundation.cg.dm.LongOperationStopped;

public class GeneratedCodeRepositoryElement extends GCBrowserElement {
	public GeneratedCodeRepositoryElement(GenerationRepository repository, ProjectBrowser browser, BrowserElement parent) {
		super(repository, BrowserElementType.GENERATED_CODE_REPOSITORY, browser, parent);
	}

	@Override
	protected void buildChildrenVector() {
		if (getRepository().isEnabled()) {
			for (CGSymbolicDirectory dir : getRepository().getSymbolicDirectories().values()) {
				addToChilds(dir);
			}
		}
	}

	@Override
	public String getName() {
		return getRepository().getDisplayName();
	}

	public GenerationRepository getRepository() {
		return (GenerationRepository) getObject();
	}

	/**
	 * Overrides update
	 * 
	 * @see org.openflexo.cgmodule.controller.browser.GCBrowserElement#update(org.openflexo.foundation.FlexoObservable,
	 *      org.openflexo.foundation.DataModification)
	 */
	@Override
	public void update(FlexoObservable observable, DataModification dataModification) {
		if (dataModification instanceof LongOperationStarted) {
			_browser.setHoldStructure();
			return;
		} else if (dataModification instanceof LongOperationStopped) {
			_browser.resetHoldStructure();
			refreshWhenPossible();
			return;
		}

		if (dataModification instanceof CGStructureRefreshed) {
			refreshWhenPossible();
			return;
		}
		super.update(observable, dataModification);
	}
}
