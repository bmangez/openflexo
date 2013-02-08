package org.openflexo.foundation.rm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Logger;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.resource.FlexoXMLFileResourceImpl;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.foundation.view.View;
import org.openflexo.foundation.view.VirtualModelInstance;
import org.openflexo.foundation.viewpoint.VirtualModel;
import org.openflexo.model.exceptions.ModelDefinitionException;
import org.openflexo.model.factory.ModelFactory;
import org.openflexo.toolbox.StringUtils;

/**
 * Default implementation for {@link VirtualModelInstanceResource}
 * 
 * 
 * @author Sylvain
 * 
 */
public abstract class VirtualModelInstanceResourceImpl<VMI extends VirtualModelInstance<VMI, ?>> extends FlexoXMLFileResourceImpl<VMI>
		implements VirtualModelInstanceResource<VMI> {

	static final Logger logger = Logger.getLogger(VirtualModelInstanceResourceImpl.class.getPackage().getName());

	public static VirtualModelInstanceResource<?> makeVirtualModelInstanceResource(String name, VirtualModel virtualModel, View view) {
		try {
			ModelFactory factory = new ModelFactory(VirtualModelInstanceResource.class);
			VirtualModelInstanceResourceImpl<?> returned = (VirtualModelInstanceResourceImpl<?>) factory
					.newInstance(VirtualModelInstanceResource.class);
			returned.setServiceManager(view.getProject().getServiceManager());
			String baseName = name;
			File xmlFile = new File(view.getResource().getFile().getParentFile(), baseName
					+ VirtualModelInstanceResource.VIRTUAL_MODEL_SUFFIX);
			returned.setProject(view.getProject());
			returned.setName(name);
			returned.setFile(xmlFile);
			returned.setVirtualModelResource(virtualModel.getResource());
			view.getResource().addToContents(returned);
			view.getResource().notifyContentsAdded(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static VirtualModelInstanceResource<?> retrieveVirtualModelInstanceResource(File virtualModelInstanceFile,
			ViewResource viewResource) {
		try {
			ModelFactory factory = new ModelFactory(VirtualModelInstanceResource.class);
			VirtualModelInstanceResourceImpl<?> returned = (VirtualModelInstanceResourceImpl<?>) factory
					.newInstance(VirtualModelInstanceResource.class);
			returned.setServiceManager(viewResource.getProject().getServiceManager());
			String baseName = virtualModelInstanceFile.getName().substring(0,
					virtualModelInstanceFile.getName().length() - VirtualModelInstanceResource.VIRTUAL_MODEL_SUFFIX.length());
			File xmlFile = new File(viewResource.getFile().getParentFile(), baseName + VirtualModelInstanceResource.VIRTUAL_MODEL_SUFFIX);
			returned.setProject(viewResource.getProject());
			returned.setName(baseName);
			returned.setFile(xmlFile);
			VirtualModelInstanceInfo vmiInfo = findVirtualModelInstanceInfo(xmlFile, "VirtualModelInstance");
			if (vmiInfo == null) {
				// Unable to retrieve infos, just abort
				return null;
			}
			if (StringUtils.isNotEmpty(vmiInfo.virtualModelURI)) {
				returned.setVirtualModelResource(viewResource.getViewPoint().getVirtualModelNamed(vmiInfo.virtualModelURI).getResource());
			}
			viewResource.addToContents(returned);
			viewResource.notifyContentsAdded(returned);
			return returned;
		} catch (ModelDefinitionException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public VMI getVirtualModelInstance() {
		try {
			return getResourceData(null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ResourceLoadingCancelledException e) {
			e.printStackTrace();
		} catch (ResourceDependencyLoopException e) {
			e.printStackTrace();
		} catch (FlexoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Class<VMI> getResourceDataClass() {
		return (Class<VMI>) VirtualModelInstance.class;
	}

	@Override
	public String getURI() {
		if (getContainer() != null) {
			return getContainer().getURI() + "/" + getName();
		}
		return null;
	}

	protected static class VirtualModelInstanceInfo {
		public String virtualModelURI;
		public String name;
	}

	protected static VirtualModelInstanceInfo findVirtualModelInstanceInfo(File virtualModelInstanceFile, String searchedRootXMLTag) {
		Document document;
		try {
			logger.fine("Try to find infos for " + virtualModelInstanceFile);

			String baseName = virtualModelInstanceFile.getName().substring(0,
					virtualModelInstanceFile.getName().length() - VirtualModelInstanceResource.VIRTUAL_MODEL_SUFFIX.length());

			if (virtualModelInstanceFile.exists()) {
				document = FlexoXMLFileResourceImpl.readXMLFile(virtualModelInstanceFile);
				Element root = FlexoXMLFileResourceImpl.getElement(document, searchedRootXMLTag);
				if (root != null) {
					VirtualModelInstanceInfo returned = new VirtualModelInstanceInfo();
					returned.name = baseName;
					Iterator<Attribute> it = root.getAttributes().iterator();
					while (it.hasNext()) {
						Attribute at = it.next();
						if (at.getName().equals("virtualModelURI")) {
							logger.fine("Returned " + at.getValue());
							returned.virtualModelURI = at.getValue();
						}
					}
					return returned;
				}
			} else {
				logger.warning("Cannot find file: " + virtualModelInstanceFile.getAbsolutePath());
			}
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.fine("Returned null");
		return null;
	}

}