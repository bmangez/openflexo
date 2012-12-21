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
package org.openflexo.dg.rm;

import java.io.File;
import java.util.logging.Logger;

import org.openflexo.dg.latex.DGLatexGenerator;
import org.openflexo.foundation.rm.DuplicateResourceException;
import org.openflexo.foundation.rm.FlexoResource;
import org.openflexo.foundation.rm.cg.TextFile;
import org.openflexo.generator.rm.GenerationAvailableFile;
import org.openflexo.logging.FlexoLogger;

public class LatexFile extends TextFile implements GenerationAvailableFile {

	protected static final Logger logger = FlexoLogger.getLogger(LatexFile.class.getPackage().getName());

	public LatexFile(File f, LatexFileResource resource) {
		super(f);
		try {
			setFlexoResource(resource);
		} catch (DuplicateResourceException e) {
			e.printStackTrace();
		}
	}

	public LatexFile() {
		super();
	}

	@Override
	public LatexFileResource getFlexoResource() {
		return (LatexFileResource) super.getFlexoResource();
	}

	@Override
	public DGLatexGenerator getGenerator() {
		return (DGLatexGenerator) getFlexoResource().getGenerator();
	}

	/**
	 * Overrides setFlexoResource
	 * 
	 * @see org.openflexo.foundation.rm.FlexoResourceData#setFlexoResource(org.openflexo.foundation.rm.FlexoResource)
	 */
	@Override
	public void setFlexoResource(FlexoResource resource) throws DuplicateResourceException {

	}

}