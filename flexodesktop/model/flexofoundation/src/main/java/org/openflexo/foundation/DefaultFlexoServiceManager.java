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
package org.openflexo.foundation;

import org.openflexo.foundation.resource.DefaultResourceCenterService;
import org.openflexo.foundation.resource.FlexoResource;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenterService;
import org.openflexo.foundation.rm.FlexoProject.FlexoProjectReferenceLoader;
import org.openflexo.foundation.technologyadapter.DefaultTechnologyAdapterService;
import org.openflexo.foundation.technologyadapter.InformationSpace;
import org.openflexo.foundation.technologyadapter.MetaModelRepository;
import org.openflexo.foundation.technologyadapter.ModelRepository;
import org.openflexo.foundation.technologyadapter.TechnologyAdapter;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.foundation.viewpoint.ViewPointLibrary;
import org.openflexo.foundation.xml.XMLSerializationService;

/**
 * Default implementation of {@link FlexoServiceManager}
 * 
 * 
 * @author sylvain
 * 
 */
public abstract class DefaultFlexoServiceManager extends FlexoServiceManager {

	public DefaultFlexoServiceManager() {
		XMLSerializationService xmlSerializationService = XMLSerializationService.createInstance();
		registerService(xmlSerializationService);
		FlexoProjectReferenceLoader projectReferenceLoader = createProjectReferenceLoader();
		if (projectReferenceLoader != null) {
			registerService(projectReferenceLoader);
		}
		FlexoResourceCenterService resourceCenterService = createResourceCenterService();
		registerService(resourceCenterService);
		TechnologyAdapterService technologyAdapterService = createTechnologyAdapterService(resourceCenterService);
		registerService(technologyAdapterService);
		InformationSpace informationSpace = createInformationSpace();
		registerService(informationSpace);
		ViewPointLibrary viewPointLibrary = createViewPointLibraryService();
		registerService(viewPointLibrary);
	}

	@Override
	protected XMLSerializationService createXMLSerializationService() {
		return XMLSerializationService.createInstance();
	}

	@Override
	protected FlexoResourceCenterService createResourceCenterService() {
		return DefaultResourceCenterService.getNewInstance();
	}

	@Override
	protected TechnologyAdapterService createTechnologyAdapterService(FlexoResourceCenterService resourceCenterService) {
		return DefaultTechnologyAdapterService.getNewInstance(resourceCenterService);
	}

	@Override
	protected ViewPointLibrary createViewPointLibraryService() {
		return new ViewPointLibrary();
	}

	@Override
	protected InformationSpace createInformationSpace() {
		return new InformationSpace();
	}

	public String debug() {
		StringBuffer sb = new StringBuffer();
		sb.append("**********************************************\n");
		sb.append("FLEXO SERVICE MANAGER: " + getClass() + "\n");
		sb.append("**********************************************\n");
		sb.append("Registered services: " + getRegisteredServices().size() + "\n");
		for (FlexoService s : getRegisteredServices()) {
			sb.append("Service: " + s.getClass().getSimpleName() + "\n");
		}
		if (getTechnologyAdapterService() != null) {
			sb.append("**********************************************\n");
			sb.append("Technology Adapter Service: " + getTechnologyAdapterService().getClass().getSimpleName() + " technology adapters: "
					+ getTechnologyAdapterService().getTechnologyAdapters().size() + "\n");
			for (TechnologyAdapter ta : getTechnologyAdapterService().getTechnologyAdapters()) {
				sb.append("> " + ta.getName() + "\n");
			}
		}
		if (getResourceCenterService() != null) {
			sb.append("**********************************************\n");
			sb.append("Resource Center Service: " + getResourceCenterService().getClass().getSimpleName() + " resource centers: "
					+ getResourceCenterService().getResourceCenters().size() + "\n");
			for (FlexoResourceCenter rc : getResourceCenterService().getResourceCenters()) {
				sb.append("> " + rc.getName() + "\n");
			}
		}
		if (getInformationSpace() != null) {
			sb.append("**********************************************\n");
			sb.append("Information Space\n");
			for (TechnologyAdapter<?, ?> ta : getTechnologyAdapterService().getTechnologyAdapters()) {
				for (MetaModelRepository<?, ?, ?, ?> mmRep : getInformationSpace().getAllMetaModelRepositories(ta)) {
					System.out.println("Technology adapter: " + ta + " meta-model repository: " + mmRep + "\n");
					for (FlexoResource<?> r : mmRep.getAllResources()) {
						sb.append("> " + r.getURI() + "\n");
					}
				}
			}
			for (TechnologyAdapter<?, ?> ta : getTechnologyAdapterService().getTechnologyAdapters()) {
				for (ModelRepository<?, ?, ?, ?> mRep : getInformationSpace().getAllModelRepositories(ta)) {
					System.out.println("Technology adapter: " + ta + " model repository: " + mRep);
					for (FlexoResource<?> r : mRep.getAllResources()) {
						System.out.println("> " + r.getURI());
					}
				}
			}
		}
		sb.append("**********************************************");
		return sb.toString();
	}
}