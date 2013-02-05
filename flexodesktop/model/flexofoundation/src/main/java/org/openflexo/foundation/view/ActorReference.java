package org.openflexo.foundation.view;

import org.openflexo.foundation.rm.FlexoProject;
import org.openflexo.foundation.rm.XMLStorageResourceData;
import org.openflexo.foundation.technologyadapter.ModelSlot;
import org.openflexo.foundation.viewpoint.PatternRole;
import org.openflexo.toolbox.StringUtils;

public abstract class ActorReference<T> extends VirtualModelInstanceObject {
	private PatternRole<T> patternRole;
	private String patternRoleName;
	private ModelSlot<?, ?> modelSlot;
	private EditionPatternInstance epi;

	protected ActorReference(FlexoProject project) {
		super(project);
	}

	public ModelSlot<?, ?> getModelSlot() {
		return modelSlot;
	}

	public void setModelSlot(ModelSlot<?, ?> modelSlot) {
		this.modelSlot = modelSlot;
	}

	@Override
	public XMLStorageResourceData getXMLResourceData() {
		if (getEditionPatternInstance() != null) {
			return getEditionPatternInstance().getXMLResourceData();
		}
		return null;
	}

	public abstract T retrieveObject();

	public EditionPatternInstance getEditionPatternInstance() {
		return epi;
	}

	public void setEditionPatternInstance(EditionPatternInstance epi) {
		this.epi = epi;
	}

	public PatternRole<T> getPatternRole() {
		if (patternRole == null && epi != null && StringUtils.isNotEmpty(patternRoleName)) {
			patternRole = epi.getEditionPattern().getPatternRole(patternRoleName);
		}
		return patternRole;
	}

	public void setPatternRole(PatternRole<T> patternRole) {
		this.patternRole = patternRole;
	}

	public String getPatternRoleName() {
		return patternRole.getPatternRoleName();
	}

	public void setPatternRoleName(String patternRoleName) {
		this.patternRoleName = patternRoleName;
	}

	@Override
	public VirtualModelInstance<?, ?> getVirtualModelInstance() {
		if (getEditionPatternInstance() != null) {
			return getEditionPatternInstance().getVirtualModelInstance();
		}
		return null;
	}
}