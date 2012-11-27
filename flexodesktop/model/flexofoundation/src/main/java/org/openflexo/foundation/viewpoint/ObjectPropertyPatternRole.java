package org.openflexo.foundation.viewpoint;

import org.openflexo.foundation.ontology.IFlexoOntologyClass;
import org.openflexo.foundation.ontology.IFlexoOntologyObjectProperty;
import org.openflexo.foundation.viewpoint.ViewPoint.ViewPointBuilder;

public class ObjectPropertyPatternRole extends PropertyPatternRole {

	private String rangeURI;

	public ObjectPropertyPatternRole(ViewPointBuilder builder) {
		super(builder);
	}

	@Override
	public PatternRoleType getType() {
		return PatternRoleType.ObjectProperty;
	}

	@Override
	public String getPreciseType() {
		if (getParentProperty() != null) {
			return getParentProperty().getName();
		}
		return "";
	}

	@Override
	public Class<?> getAccessedClass() {
		return IFlexoOntologyObjectProperty.class;
	}

	@Override
	public IFlexoOntologyObjectProperty getParentProperty() {
		return (IFlexoOntologyObjectProperty) super.getParentProperty();
	}

	public void setParentProperty(IFlexoOntologyObjectProperty ontologyProperty) {
		super.setParentProperty(ontologyProperty);
	}

	public String _getRangeURI() {
		return rangeURI;
	}

	public void _setRangeURI(String domainURI) {
		this.rangeURI = domainURI;
	}

	public IFlexoOntologyClass getRange() {
		getViewPoint().loadWhenUnloaded();
		return getViewPoint().getViewpointOntology().getClass(_getRangeURI());
	}

	public void setRange(IFlexoOntologyClass c) {
		_setRangeURI(c != null ? c.getURI() : null);
	}

}
