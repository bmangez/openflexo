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
package org.openflexo.foundation.ontology;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.foundation.Inspectors;
import org.openflexo.toolbox.StringUtils;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;

public class OntologyClass extends OntologyObject<OntClass> implements Comparable<OntologyClass> {

	private static final Logger logger = Logger.getLogger(OntologyClass.class.getPackage().getName());

	private OntClass ontClass;

	private final Vector<OntologyClass> superClasses;
	@Deprecated
	private final Vector<OntologyClass> subClasses;
	protected Vector<OntologyIndividual> individuals;

	protected OntologyClass(OntClass anOntClass, FlexoOntology ontology) {
		super(anOntClass, ontology);
		superClasses = new Vector<OntologyClass>();
		subClasses = new Vector<OntologyClass>();
		individuals = new Vector<OntologyIndividual>();
		ontClass = anOntClass;
	}

	/**
	 * Init this OntologyClass, given base OntClass
	 */
	protected void init() {
		updateOntologyStatements(ontClass);
		updateSuperClasses(ontClass);
		updateSubClasses(ontClass);
	}

	@Override
	public void delete() {
		getFlexoOntology().removeClass(this);
		getOntResource().remove();
		getFlexoOntology().updateConceptsAndProperties();
		super.delete();
		deleteObservers();
	}

	/**
	 * Update this OntologyClass, given base OntClass
	 */
	@Override
	protected void update() {
		update(ontClass);
	}

	/**
	 * Update this OntologyClass, given base OntClass which is assumed to extends base OntClass
	 * 
	 * @param anOntClass
	 */
	protected void update(OntClass anOntClass) {
		updateOntologyStatements(anOntClass);
		updateSuperClasses(anOntClass);
		updateSubClasses(anOntClass);
		ontClass = anOntClass;
	}

	@Override
	public void setName(String aName) {
		renameURI(aName, ontClass, OntClass.class);
	}

	@Override
	protected void _setOntResource(OntClass r) {
		ontClass = r;
	}

	@Override
	public OntologyClass getOriginalDefinition() {
		return (OntologyClass) super.getOriginalDefinition();
	}

	private void appendToSuperClasses(OntologyClass superClass) {
		if (getURI().equals(OWL2URIDefinitions.OWL_THING_URI)) {
			return;
		}
		if (superClass == this) {
			return;
		}
		if (superClass.redefinesOriginalDefinition()) {
			if (superClasses.contains(superClass.getOriginalDefinition())) {
				superClasses.remove(superClass.getOriginalDefinition());
			}
			superClass.getOriginalDefinition().subClasses.remove(this);
		}
		if (!superClasses.contains(superClass)) {
			superClasses.add(superClass);
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Add " + superClass.getName() + " as a super class of " + getName());
			}
		}
		if (!superClass.subClasses.contains(this)) {
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Add " + getName() + " as a sub class of " + superClass.getName());
			}
			superClass.subClasses.add(this);
		}
	}

	private void updateSuperClasses(OntClass anOntClass) {
		superClasses.clear();
		if (redefinesOriginalDefinition()) {
			for (OntologyClass c : getOriginalDefinition().getSuperClasses()) {
				if (!c.isThing()) {
					appendToSuperClasses(c);
				}
			}
			// superClasses.addAll(getOriginalDefinition().getSuperClasses());
		}
		// logger.info("updateSuperClasses for " + getURI());

		Iterator it = anOntClass.listSuperClasses(true);
		while (it.hasNext()) {
			OntClass father = (OntClass) it.next();
			OntologyClass fatherClass = getOntology().retrieveOntologyClass(father);// getOntologyLibrary().getClass(father.getURI());
			if (fatherClass != null) {
				appendToSuperClasses(fatherClass);
			}
		}

		// If this class is equivalent to the intersection of some other classes, then all those operand classes are super classes of this
		// class
		if (getEquivalentClass() instanceof OntologyIntersectionClass) {
			for (OntologyClass operand : ((OntologyIntersectionClass) getEquivalentClass()).getOperands()) {
				appendToSuperClasses(operand);
			}
		}

		// If computed ontology is either not RDF, nor RDFS, nor OWL
		// add OWL Thing as parent
		if (getFlexoOntology() != getOntologyLibrary().getRDFOntology() && getFlexoOntology() != getOntologyLibrary().getRDFSOntology()) {
			if (isNamedClass() && !isThing()) {
				OntologyClass THING_CLASS = getOntology().getRootClass();
				appendToSuperClasses(THING_CLASS);
			}
		}
	}

	@Deprecated
	private void updateSubClasses(OntClass anOntClass) {
		subClasses.clear();
		if (redefinesOriginalDefinition()) {
			subClasses.addAll(getOriginalDefinition().getSubClasses());
		}
		// subClasses.clear();
		Iterator it = anOntClass.listSubClasses(true);
		while (it.hasNext()) {
			OntClass child = (OntClass) it.next();
			OntologyClass childClass = getOntology().retrieveOntologyClass(child);// getOntologyLibrary().getClass(child.getURI());
			if (childClass != null) {
				if (!subClasses.contains(childClass)) {
					subClasses.add(childClass);
				}
				if (!childClass.superClasses.contains(this)) {
					childClass.superClasses.add(this);
				}
			}
		}
	}

	@Override
	public String getClassNameKey() {
		return "ontology_class";
	}

	@Override
	public String getFullyQualifiedName() {
		return "OntologyClass:" + getURI();
	}

	public static final Comparator<OntologyClass> COMPARATOR = new Comparator<OntologyClass>() {
		@Override
		public int compare(OntologyClass o1, OntologyClass o2) {
			return Collator.getInstance().compare(o1.getName(), o2.getName());
		}
	};

	@Override
	public String getInspectorName() {
		if (getIsReadOnly()) {
			return Inspectors.VE.ONTOLOGY_CLASS_READ_ONLY_INSPECTOR; // read-only
		} else {
			return Inspectors.VE.ONTOLOGY_CLASS_INSPECTOR;
		}
	}

	@Override
	public int compareTo(OntologyClass o) {
		return COMPARATOR.compare(this, o);
	}

	@Override
	public OntClass getOntResource() {
		return ontClass;
	}

	private static boolean isSuperClassOf(OntClass parentClass, OntClass subClass) {
		if (parentClass == null) {
			return false;
		}
		if (subClass == null) {
			return false;
		}
		if (parentClass.equals(subClass)) {
			return true;
		}
		Iterator it = subClass.listSuperClasses();
		while (it.hasNext()) {
			OntClass p = (OntClass) it.next();
			if (p.equals(parentClass)) {
				return true;
			}
			if (isSuperClassOf(parentClass, p)) {
				return true;
			}
		}
		return false;
	}

	private static boolean isSuperClassOf(OntClass parentClass, Individual individual) {
		if (parentClass == null) {
			return false;
		}
		if (individual == null) {
			return false;
		}
		Iterator it = individual.listOntClasses(false);
		while (it.hasNext()) {
			OntClass p = (OntClass) it.next();
			if (p.equals(parentClass)) {
				return true;
			}
			if (isSuperClassOf(parentClass, p)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isSuperConceptOf(OntologyObject concept) {
		if (OWL2URIDefinitions.OWL_THING_URI.equals(getURI())) {
			return true;
		}
		if (concept instanceof OntologyIndividual) {
			OntologyIndividual ontologyIndividual = (OntologyIndividual) concept;
			// Doesn't work, i dont know why
			// return ontologyIndividual.getIndividual().hasOntClass(ontClass);
			return isSuperClassOf(ontClass, ontologyIndividual.getIndividual());
		}
		if (concept instanceof OntologyClass) {
			OntologyClass ontologyClass = (OntologyClass) concept;
			// Doesn't work, i dont know why
			// return ontologyClass.getOntResource().hasSuperClass(ontClass);
			// return isSuperClassOf(ontClass, ontologyClass.getOntResource());
			return isSuperClassOf(ontologyClass);
		}
		return false;
	}

	public boolean isSuperClassOf(OntologyClass aClass) {
		if (aClass == this) {
			return true;
		}
		if (equalsToConcept(aClass)) {
			return true;
		}
		if (OWL2URIDefinitions.OWL_THING_URI.equals(getURI())) {
			return true;
		}
		for (OntologyClass c : aClass.getSuperClasses()) {
			if (isSuperClassOf(c)) {
				return true;
			}
		}
		return false;
	}

	public Vector<OntologyClass> getSuperClasses() {
		return superClasses;
	}

	public Set<OntologyClass> getAllSuperClasses() {
		Set<OntologyClass> returned = new HashSet<OntologyClass>();
		for (OntologyClass c : getSuperClasses()) {
			returned.add(c);
			returned.addAll(c.getAllSuperClasses());
		}
		return returned;
	}

	public static OntologyClass getMostSpecializedClass(Collection<OntologyClass> someClasses) {

		if (someClasses.size() == 0) {
			return null;
		}
		if (someClasses.size() == 1) {
			return someClasses.iterator().next();
		}
		OntologyClass[] array = someClasses.toArray(new OntologyClass[someClasses.size()]);

		for (int i = 0; i < someClasses.size(); i++) {
			for (int j = i + 1; j < someClasses.size(); j++) {
				OntologyClass c1 = array[i];
				OntologyClass c2 = array[j];
				if (c1.isSuperClassOf(c2)) {
					someClasses.remove(c1);
					return getMostSpecializedClass(someClasses);
				}
				if (c2.isSuperClassOf(c1)) {
					someClasses.remove(c2);
					return getMostSpecializedClass(someClasses);
				}
			}
		}

		// No parent were found, take first item
		logger.warning("Undefined specializing criteria between " + someClasses);
		return someClasses.iterator().next();

	}

	public static OntologyClass getFirstCommonAncestor(OntologyClass c1, OntologyClass c2) {
		Set<OntologyClass> commonAncestors = new HashSet<OntologyClass>();
		Set<OntologyClass> ancestors1 = c1.getAllSuperClasses();
		ancestors1.add(c1);
		Set<OntologyClass> ancestors2 = c2.getAllSuperClasses();
		ancestors2.add(c2);
		for (OntologyClass cl1 : ancestors1) {
			for (OntologyClass cl2 : ancestors2) {
				if (cl1.equalsToConcept(cl2)) {
					commonAncestors.add(cl1);
				}
			}
		}
		return getMostSpecializedClass(commonAncestors);
	}

	@Deprecated
	public Vector<OntologyClass> getSubClasses() {
		return subClasses;
	}

	public Vector<OntologyIndividual> getIndividuals() {
		return individuals;
	}

	/**
	 * Return a vector of Ontology class, as a subset of getSubClasses(), which correspond to all classes necessary to see all classes
	 * belonging to supplied context, which is an ontology
	 * 
	 * @param context
	 * @return
	 */
	public Vector<OntologyClass> getSubClasses(FlexoOntology context) {
		Vector<OntologyClass> returned = new Vector<OntologyClass>();
		for (OntologyClass aClass : getSubClasses()) {
			if (isRequired(aClass, context)) {
				returned.add(aClass);
			}
		}
		return returned;
	}

	private boolean isRequired(OntologyClass aClass, FlexoOntology context) {
		if (aClass.getFlexoOntology() == context) {
			return true;
		}
		for (OntologyClass aSubClass : aClass.getSubClasses()) {
			if (isRequired(aSubClass, context)) {
				return true;
			}
		}
		for (OntologyIndividual anIndividual : aClass.getIndividuals()) {
			if (anIndividual.getFlexoOntology() == context) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getDisplayableDescription() {
		/*String extendsLabel = " extends ";
		boolean isFirst = true;
		for (OntologyClass s : superClasses) {
			extendsLabel += (isFirst ? "" : ",") + s.getName();
			isFirst = false;
		}
		return "Class " + getName() + extendsLabel;*/
		return getName();
	}

	@Override
	public boolean isOntologyClass() {
		return true;
	}

	@Override
	protected void recursivelySearchRangeAndDomains() {
		super.recursivelySearchRangeAndDomains();
		for (OntologyClass aClass : getSuperClasses()) {
			propertiesTakingMySelfAsRange.addAll(aClass.getPropertiesTakingMySelfAsRange());
			propertiesTakingMySelfAsDomain.addAll(aClass.getPropertiesTakingMySelfAsDomain());
		}
		OntologyClass CLASS_CONCEPT = getOntology().getClass(OWL_CLASS_URI);
		// CLASS_CONCEPT is generally non null but can be null when reading RDFS for exampel
		if (CLASS_CONCEPT != null) {
			propertiesTakingMySelfAsRange.addAll(CLASS_CONCEPT.getPropertiesTakingMySelfAsRange());
			propertiesTakingMySelfAsDomain.addAll(CLASS_CONCEPT.getPropertiesTakingMySelfAsDomain());
		}

		/*Vector<OntologyClass> alreadyComputed = new Vector<OntologyClass>();
		if (redefinesOriginalDefinition()) {
			_appendRangeAndDomains(getOriginalDefinition(), alreadyComputed);
		}
		for (OntologyClass aClass : getSuperClasses()) {
			_appendRangeAndDomains(aClass, alreadyComputed);
		}*/
	}

	/*private void _appendRangeAndDomains(OntologyClass superClass, Vector<OntologyClass> alreadyComputed) {
		if (alreadyComputed.contains(superClass)) {
			return;
		}
		alreadyComputed.add(superClass);
		for (OntologyProperty p : superClass.getDeclaredPropertiesTakingMySelfAsDomain()) {
			if (!propertiesTakingMySelfAsDomain.contains(p)) {
				propertiesTakingMySelfAsDomain.add(p);
			}
		}
		for (OntologyProperty p : superClass.getDeclaredPropertiesTakingMySelfAsRange()) {
			if (!propertiesTakingMySelfAsRange.contains(p)) {
				propertiesTakingMySelfAsRange.add(p);
			}
		}
		for (OntologyClass superSuperClass : superClass.getSuperClasses()) {
			_appendRangeAndDomains(superSuperClass, alreadyComputed);
		}
	}*/

	private OntologyClass equivalentClass;
	private List<OntologyClass> equivalentClasses = new ArrayList<OntologyClass>();

	@Override
	public void updateOntologyStatements(OntClass anOntResource) {
		super.updateOntologyStatements(anOntResource);
		equivalentClasses.clear();
		for (OntologyStatement s : getSemanticStatements()) {
			if (s instanceof EquivalentClassStatement) {
				if (((EquivalentClassStatement) s).getEquivalentObject() instanceof OntologyClass) {
					equivalentClass = (OntologyClass) ((EquivalentClassStatement) s).getEquivalentObject();
					equivalentClasses.add(equivalentClass);
				}
			}
		}
	}

	/**
	 * Return equivalent class, asserting there is only one equivalent class statement
	 * 
	 * @return
	 */
	public OntologyClass getEquivalentClass() {
		return equivalentClass;
	}

	/**
	 * Return all restrictions related to supplied property
	 * 
	 * @param property
	 * @return
	 */
	public List<OntologyRestrictionClass> getRestrictions(OntologyProperty property) {
		List<OntologyRestrictionClass> returned = new ArrayList<OntologyRestrictionClass>();
		for (OntologyClass c : getSuperClasses()) {
			if (c instanceof OntologyRestrictionClass) {
				OntologyRestrictionClass r = (OntologyRestrictionClass) c;
				if (r.getProperty() == property) {
					returned.add(r);
				}
			}
		}
		return returned;
	}

	@Override
	public String getHTMLDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");
		sb.append("Class <b>" + getName() + "</b><br>");
		sb.append("<i>" + getURI() + "</i><br>");
		sb.append("<b>Asserted in:</b> " + getOntology().getURI() + "<br>");
		if (redefinesOriginalDefinition()) {
			sb.append("<b>Redefines:</b> " + getOriginalDefinition() + "<br>");
		}
		sb.append("<b>Superclasses:</b>");
		for (OntologyClass c : getSuperClasses()) {
			sb.append(" " + c.getDisplayableDescription());
		}
		sb.append("</html>");
		return sb.toString();
	}

	public boolean isNamedClass() {
		return StringUtils.isNotEmpty(getURI());
	}

	public boolean isThing() {
		return isNamedClass() && getURI().equals(OWL2URIDefinitions.OWL_THING_URI);
	}

}
