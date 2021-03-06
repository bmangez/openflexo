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
package org.openflexo.module.external;

import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.module.ModuleLoadingException;

/**
 * @author gpolet
 * 
 */
public interface IModuleLoader {
	public IModule getActiveModule();

	public ExternalIEModule getIEModuleInstance(FlexoProject project) throws ModuleLoadingException;

	public ExternalDMModule getDMModuleInstance(FlexoProject project) throws ModuleLoadingException;

	public ExternalWKFModule getWKFModuleInstance(FlexoProject project) throws ModuleLoadingException;

	public ExternalCEDModule getCEDModuleInstance(FlexoProject project) throws ModuleLoadingException;

	public ExternalOEModule getOEModuleInstance(FlexoProject project) throws ModuleLoadingException;

	public boolean isWKFLoaded();

}
