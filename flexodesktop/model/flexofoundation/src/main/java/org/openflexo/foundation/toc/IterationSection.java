package org.openflexo.foundation.toc;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import org.openflexo.antar.binding.AbstractBinding.BindingEvaluationContext;
import org.openflexo.antar.binding.BindingDefinition;
import org.openflexo.antar.binding.BindingDefinition.BindingDefinitionType;
import org.openflexo.antar.binding.BindingModel;
import org.openflexo.antar.binding.BindingVariableImpl;
import org.openflexo.antar.binding.ParameterizedTypeImpl;
import org.openflexo.antar.binding.WilcardTypeImpl;
import org.openflexo.foundation.xml.FlexoTOCBuilder;

public class IterationSection extends ControlSection {

	private String iteratorName;

	public IterationSection(FlexoTOCBuilder builder) {
		this(builder.tocData);
		initializeDeserialization(builder);
	}

	public IterationSection(TOCData data) {
		super(data);
	}

	private TOCDataBinding iteration;

	private Type LIST_BINDING_TYPE = new ParameterizedTypeImpl(List.class, new WilcardTypeImpl(Object.class));;

	private BindingDefinition ITERATION = new BindingDefinition("iteration", LIST_BINDING_TYPE, BindingDefinitionType.GET, false);

	public BindingDefinition getIterationBindingDefinition() {
		return ITERATION;
	}

	@Override
	public boolean isIteration() {
		return true;
	}

	public TOCDataBinding getIteration() {
		if (iteration == null) {
			iteration = new TOCDataBinding(this, ControlSectionBindingAttribute.iteration, getIterationBindingDefinition());
		}
		return iteration;
	}

	public void setIteration(TOCDataBinding iteration) {
		iteration.setOwner(this);
		iteration.setBindingAttribute(ControlSectionBindingAttribute.iteration);
		iteration.setBindingDefinition(getIterationBindingDefinition());
		this.iteration = iteration;
		rebuildBindingModel();
	}

	private TOCDataBinding condition;

	private BindingDefinition CONDITION = new BindingDefinition("condition", Boolean.class, BindingDefinitionType.GET, false);

	public BindingDefinition getConditionBindingDefinition() {
		return CONDITION;
	}

	public TOCDataBinding getCondition() {
		if (condition == null) {
			condition = new TOCDataBinding(this, ControlSectionBindingAttribute.condition, getConditionBindingDefinition());
		}
		return condition;
	}

	public void setCondition(TOCDataBinding condition) {
		if (condition != null) {
			condition.setOwner(this);
			condition.setBindingAttribute(ControlSectionBindingAttribute.condition);
			condition.setBindingDefinition(getConditionBindingDefinition());
			this.condition = condition;
		}
	}

	public String getIteratorName() {
		return iteratorName;
	}

	public void setIteratorName(String iteratorName) {
		this.iteratorName = iteratorName;
	}

	public Type getItemType() {
		if (getIteration() != null && getIteration().hasBinding()) {
			Type accessedType = getIteration().getBinding().getAccessedType();
			if (accessedType instanceof ParameterizedType && ((ParameterizedType) accessedType).getActualTypeArguments().length > 0) {
				return ((ParameterizedType) accessedType).getActualTypeArguments()[0];
			}
		}
		return Object.class;
	}

	@Override
	protected BindingModel buildBindingModel() {
		BindingModel returned = super.buildBindingModel();
		returned.addToBindingVariables(new BindingVariableImpl(this, getIteratorName(), getItemType()) {
			@Override
			public Object getBindingValue(Object target, BindingEvaluationContext context) {
				logger.info("What should i return for " + getIteratorName() + " ? target " + target + " context=" + context);
				return super.getBindingValue(target, context);
			}

			@Override
			public Type getType() {
				return getItemType();
			}
		});
		return returned;
	}

	@Override
	public void finalizeDeserialization(Object builder) {
		super.finalizeDeserialization(builder);
		getIteration().finalizeDeserialization();
		getCondition().finalizeDeserialization();
	}

}
