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
package org.openflexo.antar.binding;

import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openflexo.antar.binding.AbstractBinding.BindingEvaluationContext;
import org.openflexo.antar.binding.BindingDefinition.BindingDefinitionType;
import org.openflexo.antar.expr.BindingValueAsExpression;
import org.openflexo.antar.expr.Constant;
import org.openflexo.antar.expr.Expression;
import org.openflexo.antar.expr.ExpressionTransformer;
import org.openflexo.antar.expr.ExpressionVisitor;
import org.openflexo.antar.expr.NullReferenceException;
import org.openflexo.antar.expr.TransformException;
import org.openflexo.antar.expr.TypeMismatchException;
import org.openflexo.antar.expr.VisitorException;
import org.openflexo.antar.expr.parser.ExpressionParser;
import org.openflexo.antar.expr.parser.ParseException;
import org.openflexo.xmlcode.StringConvertable;
import org.openflexo.xmlcode.StringEncoder.Converter;

/**
 * Representation of a data binding.<br>
 * A data binding is defined as an symbolic expression defined on a model abstraction (the binding model), which can be evaluated given a
 * {@link BindingEvaluationContext}.
 * 
 * @author sylvain
 * 
 * @param <T>
 */
public class DataBinding<T> implements StringConvertable<DataBinding> {

	private static final Logger logger = Logger.getLogger(DataBinding.class.getPackage().getName());

	public static DataBinding.DataBindingConverter CONVERTER = new DataBindingConverter();

	public static class DataBindingConverter extends Converter<DataBinding> {
		public DataBindingConverter() {
			super(DataBinding.class);
		}

		@Override
		public DataBinding<?> convertFromString(String value) {
			return new DataBinding(value);
		}

		@Override
		public String convertToString(DataBinding value) {
			return value.toString();
		};
	}

	@Override
	public Converter<? extends DataBinding> getConverter() {
		return CONVERTER;
	}

	public static interface BindingAttribute {
		public String name();
	}

	private Bindable owner;
	private BindingAttribute bindingAttribute;
	private String unparsedBinding;
	private BindingDefinition bindingDefinition;
	private Expression expression;

	public DataBinding(Bindable owner, Type type, BindingDefinitionType bdType) {
		super();
		setOwner(owner);
		setBindingDefinition(new BindingDefinition("unnamed", type, bdType, true));
	}

	public DataBinding(Bindable owner, BindingAttribute attribute, Type type, BindingDefinitionType bdType) {
		setOwner(owner);
		setBindingAttribute(attribute);
		setBindingDefinition(new BindingDefinition(attribute.name(), type, bdType, true));
	}

	public DataBinding(String unparsed, Bindable owner, Type type, BindingDefinitionType bdType) {
		this(owner, type, bdType);
		setUnparsedBinding(unparsed);
	}

	public DataBinding(String unparsed, Bindable owner, BindingAttribute attribute, Type type, BindingDefinitionType bdType) {
		this(owner, attribute, type, bdType);
		setUnparsedBinding(unparsed);
	}

	public DataBinding(String unparsed) {
		super();
		setUnparsedBinding(unparsed);
	}

	@Override
	public String toString() {
		if (expression != null) {
			return expression.toString();
		}
		return unparsedBinding;
	}

	public BindingDefinition getBindingDefinition() {
		return bindingDefinition;
	}

	public void setBindingDefinition(BindingDefinition bindingDefinition) {
		this.bindingDefinition = bindingDefinition;
		/*if (bindingDefinition == null) {
			System.out.println("Binding " + this + " has been set with NULL binding definition !!!");
		}*/
	}

	public Expression getExpression() {
		if (expression == null && !isParsingAndAnalysing) {
			parseExpression();
		}
		return expression;
	}

	/*public void setBinding(AbstractBinding binding) 
	{
		this.binding = binding;
	}*/

	public void setExpression(Expression value) {
		Expression oldValue = this.expression;
		if (oldValue == null) {
			if (value == null) {
				return; // No change
			} else {
				this.expression = value;
				unparsedBinding = value != null ? value.toString() : null;
				analyseExpression();
				notifyBindingChanged(oldValue, value);
				return;
			}
		} else {
			if (oldValue.equals(value)) {
				return; // No change
			} else {
				this.expression = value;
				unparsedBinding = value != null ? expression.toString() : null;
				logger.info("Binding takes now value " + value);
				analyseExpression();
				notifyBindingChanged(oldValue, value);
				return;
			}
		}
	}

	public Type getDeclaredType() {
		return getBindingDefinition().getType();
	}

	public void setDeclaredType(Type aType) {
		getBindingDefinition().setType(aType);
	}

	public Type getAnalyzedType() {
		if (getExpression() != null) {
			if (getExpression() instanceof BindingValueAsExpression) {
				return ((BindingValueAsExpression) getExpression()).getAccessedType();
			} else {
				try {
					/*System.out.println("****** expression=" + getExpression());
					System.out.println("****** eval type=" + getExpression().getEvaluationType());
					if (getExpression() instanceof BinaryOperatorExpression) {
						BinaryOperatorExpression bope = (BinaryOperatorExpression) getExpression();
						System.out.println("**** left=" + bope.getLeftArgument() + " of " + bope.getLeftArgument().getEvaluationType());
						System.out.println("**** right=" + bope.getRightArgument() + " of " + bope.getRightArgument().getEvaluationType());
						BindingValueAsExpression left = (BindingValueAsExpression) bope.getLeftArgument();
						BindingValueAsExpression right = (BindingValueAsExpression) bope.getRightArgument();
						// left.isValid(this);
						// right.isValid(this);
						System.out.println("**** a gauche, " + left.getAccessedType());
						System.out.println("**** a droite, " + right.getAccessedType());
					}*/

					return getExpression().getEvaluationType().getType();
				} catch (TypeMismatchException e) {
					return Object.class;
				}
			}
		}
		return Object.class;
	}

	public static class InvalidBindingValue extends VisitorException {
		private BindingValueAsExpression bindingValue;

		public InvalidBindingValue(BindingValueAsExpression e) {
			bindingValue = e;
		}

		public BindingValueAsExpression getBindingValue() {
			return bindingValue;
		}
	}

	private String invalidBindingReason;

	public boolean isValid() {

		invalidBindingReason = "unknown";

		if (getExpression() == null) {
			invalidBindingReason = "null expression";
			return false;
		}

		if (getOwner() != null) {
			try {
				expression.visit(new ExpressionVisitor() {
					@Override
					public void visit(Expression e) throws InvalidBindingValue {
						if (e instanceof BindingValueAsExpression) {
							if (!((BindingValueAsExpression) e).isValid(DataBinding.this)) {
								throw new InvalidBindingValue((BindingValueAsExpression) e);
							}
						}
					}
				});
			} catch (InvalidBindingValue e) {
				invalidBindingReason = "Invalid binding value: " + e.getBindingValue();
				return false;
			} catch (VisitorException e) {
				invalidBindingReason = "Unexpected visitor exception: " + e.getMessage();
				logger.warning("TransformException while transforming " + expression);
				return false;
			}
		}

		if (getAnalyzedType() == null) {
			invalidBindingReason = "Invalid binding because accessed type is null";
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("Invalid binding because accessed type is null");
			}
			return false;
		}

		if (getBindingDefinition() != null && getBindingDefinition().getIsSettable()) {
			// Only BindingValue may be settable
			if (!(getExpression() instanceof BindingValueAsExpression) || !((BindingValueAsExpression) getExpression()).isSettable()) {
				invalidBindingReason = "Invalid binding because binding definition declared as settable and definition cannot satisfy it";
				if (logger.isLoggable(Level.FINE)) {
					logger.fine("Invalid binding because binding definition declared as settable and definition cannot satisfy it (binding variable not settable)");
				}
				return false;
			}
		}

		if (getBindingDefinition().getType() != null
				&& TypeUtils.isTypeAssignableFrom(getBindingDefinition().getType(), getAnalyzedType(), true)) {
			// System.out.println("getBindingDefinition().getType()="+getBindingDefinition().getType());
			// System.out.println("getAccessedType()="+getAccessedType());
			invalidBindingReason = "valid binding";
			return true;
		}

		invalidBindingReason = "Invalid binding because types are not matching searched " + getBindingDefinition().getType() + " having "
				+ getAnalyzedType();
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Invalid binding because types are not matching searched " + getBindingDefinition().getType() + " having "
					+ getAnalyzedType());
		}
		return false;
	}

	public String invalidBindingReason() {
		return invalidBindingReason;
	}

	public boolean isSet() {
		return unparsedBinding != null || getExpression() != null;
	}

	public boolean isUnset() {
		return unparsedBinding == null && getExpression() == null;
	}

	public String getUnparsedBinding() {
		return unparsedBinding;
	}

	public void setUnparsedBinding(String unparsedBinding) {
		this.unparsedBinding = unparsedBinding;
	}

	public Bindable getOwner() {
		return owner;
	}

	public void setOwner(Bindable owner) {
		this.owner = owner;
	}

	private Expression parseExpression() {
		if (getUnparsedBinding() == null) {
			return expression = null;
		}

		isParsingAndAnalysing = true;
		if (getOwner() != null) {
			try {
				expression = ExpressionParser.parse(getUnparsedBinding());
			} catch (ParseException e1) {
				// parse error
				e1.printStackTrace();
				return expression = null;
			}
			analyseExpression();
		}
		isParsingAndAnalysing = false;

		return expression;
	}

	private boolean isParsingAndAnalysing = false;

	private Expression analyseExpression() {
		isParsingAndAnalysing = true;
		if (getOwner() != null) {
			try {
				expression.visit(new ExpressionVisitor() {
					@Override
					public void visit(Expression e) {
						if (e instanceof BindingValueAsExpression) {
							((BindingValueAsExpression) e).performSemanticAnalysis(DataBinding.this);
						}
					}
				});
			} catch (VisitorException e) {
				logger.warning("Unexpected " + e);
			}
		}

		isParsingAndAnalysing = false;
		return expression;
	}

	public BindingAttribute getBindingAttribute() {
		return bindingAttribute;
	}

	public void setBindingAttribute(BindingAttribute bindingAttribute) {
		this.bindingAttribute = bindingAttribute;
	}

	private void notifyBindingChanged(Expression oldValue, Expression newValue) {
		// TODO
		logger.warning("Please implement me");
		/*if (bindingAttribute != null) {
			owner.notifyChange(bindingAttribute, oldValue, value);
		}
		owner.notifyBindingChanged(this);*/
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DataBinding) {
			if (toString() == null) {
				return false;
			}
			return toString().equals(obj.toString());
		} else {
			return super.equals(obj);
		}
	}

	public T getBindingValue(final BindingEvaluationContext context) throws TypeMismatchException, NullReferenceException {
		if (isValid()) {
			try {
				Expression resolvedExpression = expression.transform(new ExpressionTransformer() {
					@Override
					public Expression performTransformation(Expression e) throws TransformException {
						if (e instanceof BindingValueAsExpression) {
							((BindingValueAsExpression) e).setDataBinding(DataBinding.this);
							Object o = ((BindingValueAsExpression) e).getBindingValue(context);
							return Constant.makeConstant(o);
						}
						return e;
					}
				});
				Expression evaluatedExpression = resolvedExpression.evaluate();

				if (evaluatedExpression instanceof Constant) {
					return (T) ((Constant) evaluatedExpression).getValue();
				}

				logger.warning("Cannot evaluate " + expression + " max reduction is " + evaluatedExpression + " resolvedExpression="
						+ resolvedExpression);
				return null;

			} catch (NullReferenceException e1) {
				throw e1;
			} catch (TypeMismatchException e1) {
				throw e1;
			} catch (TransformException e1) {
				logger.warning("Unexpected TransformException while evaluating " + expression + " " + e1.getMessage());
				e1.printStackTrace();
				return null;
			}
		}
		return null;
	}

	public void setBindingValue(Object value, BindingEvaluationContext context) {
		// TODO
	}

}