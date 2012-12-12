/** Copyright (c) 2012, THALES SYSTEMES AEROPORTES - All Rights Reserved
 * Author : Gilles Besançon
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
 * Contributors :
 *
 */
package org.openflexo.technologyadapter.emf.model.io;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.openflexo.foundation.ontology.IFlexoOntologyPropertyValue;
import org.openflexo.technologyadapter.emf.metamodel.EMFMetaModel;
import org.openflexo.technologyadapter.emf.model.EMFModel;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividual;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualAttributeDataPropertyValue;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualAttributeObjectPropertyValue;
import org.openflexo.technologyadapter.emf.model.EMFObjectIndividualReferenceObjectPropertyValue;

/**
 * EMF Model Converter.
 * 
 * @author gbesancon
 */
public class EMFModelConverter {

	/** Builder. */
	protected EMFModelBuilder builder = new EMFModelBuilder();
	/** Concepts. */
	protected final Map<EObject, EMFObjectIndividual> individuals = new HashMap<EObject, EMFObjectIndividual>();
	/** Property Values. */
	protected final Map<EObject, Map<EStructuralFeature, IFlexoOntologyPropertyValue>> propertyValues = new HashMap<EObject, Map<EStructuralFeature, IFlexoOntologyPropertyValue>>();

	/**
	 * Constructor.
	 */
	public EMFModelConverter() {
	}

	/**
	 * Convert a Resource into a Model with MetaModel support.
	 * 
	 * @param metaModel
	 * @param aResource
	 * @return
	 */
	public EMFModel convertModel(EMFMetaModel metaModel, Resource aResource) {
		EMFModel model = builder.buildModel(metaModel, this, aResource);
		for (EObject aObject : aResource.getContents()) {
			EMFObjectIndividual individual = convertObjectIndividual(model, aObject);
		}
		return model;
	}

	/**
	 * Convert EMF Object to EMF Object Individual.
	 * 
	 * @param model
	 * @param eObject
	 * @return
	 */
	public EMFObjectIndividual convertObjectIndividual(EMFModel model, EObject eObject) {
		EMFObjectIndividual individual = null;
		if (individuals.get(eObject) == null) {
			individual = builder.buildObjectIndividual(model, eObject);
			individuals.put(eObject, individual);

			// Attribute
			for (EAttribute eAttribute : eObject.eClass().getEAllAttributes()) {
				IFlexoOntologyPropertyValue propertyValue = convertObjectIndividualAttribute(model, eObject, eAttribute);
			}
			// Reference
			for (EReference eReference : eObject.eClass().getEAllReferences()) {
				IFlexoOntologyPropertyValue propertyValue = convertObjectIndividualReferenceObjectPropertyValue(model, eObject, eReference);
			}

		} else {
			individual = individuals.get(eObject);
		}
		return individual;
	}

	/**
	 * Convert EMF Attribute into Object Individual Attribute.
	 * 
	 * @param model
	 * @param eObject
	 * @param eAttribute
	 * @return
	 */
	public IFlexoOntologyPropertyValue convertObjectIndividualAttribute(EMFModel model, EObject eObject, EAttribute eAttribute) {
		IFlexoOntologyPropertyValue propertyValue = null;
		if (eAttribute.getEAttributeType().eClass().getClassifierID() == EcorePackage.EDATA_TYPE) {
			propertyValue = convertObjectIndividualAttributeDataPropertyValue(model, eObject, eAttribute);
		} else {
			propertyValue = convertObjectIndividualAttributeObjectPropertyValue(model, eObject, eAttribute);
		}
		return propertyValue;
	}

	/**
	 * Convert EMF Attribute into Object Individual Attribute Data Property Value.
	 * 
	 * @param model
	 * @param eObject
	 * @param eAttribute
	 * @return
	 */
	public EMFObjectIndividualAttributeDataPropertyValue convertObjectIndividualAttributeDataPropertyValue(EMFModel model, EObject eObject,
			EAttribute eAttribute) {
		EMFObjectIndividualAttributeDataPropertyValue propertyValue = null;
		if (propertyValues.get(eObject) == null) {
			propertyValues.put(eObject, new HashMap<EStructuralFeature, IFlexoOntologyPropertyValue>());
		}
		if (propertyValues.get(eObject).get(eAttribute) == null) {
			propertyValue = builder.buildObjectIndividualAttributeDataPropertyValue(model, eObject, eAttribute);
			propertyValues.get(eObject).put(eAttribute, propertyValue);
		} else {
			propertyValue = (EMFObjectIndividualAttributeDataPropertyValue) propertyValues.get(eObject).get(eAttribute);
		}
		return propertyValue;
	}

	/**
	 * Convert EMF Attribute into Object Individual Attribute Object Property Value.
	 * 
	 * @param model
	 * @param eObject
	 * @param eAttribute
	 * @return
	 */
	public EMFObjectIndividualAttributeObjectPropertyValue convertObjectIndividualAttributeObjectPropertyValue(EMFModel model,
			EObject eObject, EAttribute eAttribute) {
		EMFObjectIndividualAttributeObjectPropertyValue propertyValue = null;
		if (propertyValues.get(eObject) == null) {
			propertyValues.put(eObject, new HashMap<EStructuralFeature, IFlexoOntologyPropertyValue>());
		}
		if (propertyValues.get(eObject).get(eAttribute) == null) {
			propertyValue = builder.buildObjectIndividualAttributeObjectPropertyValue(model, eObject, eAttribute);
			propertyValues.get(eObject).put(eAttribute, propertyValue);
		} else {
			propertyValue = (EMFObjectIndividualAttributeObjectPropertyValue) propertyValues.get(eObject).get(eAttribute);
		}
		return propertyValue;
	}

	/**
	 * Convert EMF Reference into Object Individual Reference Object Property Value.
	 * 
	 * @param model
	 * @param eObject
	 * @param eReference
	 * @return
	 */
	public EMFObjectIndividualReferenceObjectPropertyValue convertObjectIndividualReferenceObjectPropertyValue(EMFModel model,
			EObject eObject, EReference eReference) {
		EMFObjectIndividualReferenceObjectPropertyValue propertyValue = null;
		if (propertyValues.get(eObject) == null) {
			propertyValues.put(eObject, new HashMap<EStructuralFeature, IFlexoOntologyPropertyValue>());
		}
		if (propertyValues.get(eObject).get(eReference) == null) {
			propertyValue = builder.buildObjectIndividualReferenceObjectPropertyValue(model, eObject, eReference);
			propertyValues.get(eObject).put(eReference, propertyValue);

			Object value = eObject.eGet(eReference);
			if (value != null) {
				if (eReference.getUpperBound() == 1) {
					if (value instanceof EObject) {
						EMFObjectIndividual individual = convertObjectIndividual(model, (EObject) value);
					}
				} else if (eReference.getUpperBound() == -1) {
					if (value instanceof EList) {
						for (Object aListValue : (EList<Object>) value) {
							if (aListValue instanceof EObject) {
								EMFObjectIndividual individual = convertObjectIndividual(model, (EObject) aListValue);
							}
						}
					}
				}
			}

		} else {
			propertyValue = (EMFObjectIndividualReferenceObjectPropertyValue) propertyValues.get(eObject).get(eReference);
		}
		return propertyValue;
	}

	/**
	 * Getter of individuals.
	 * 
	 * @return the individuals value
	 */
	public Map<EObject, EMFObjectIndividual> getIndividuals() {
		return individuals;
	}

	/**
	 * Getter of propertyValues.
	 * 
	 * @return the propertyValues value
	 */
	public Map<EObject, Map<EStructuralFeature, IFlexoOntologyPropertyValue>> getPropertyValues() {
		return propertyValues;
	}
}