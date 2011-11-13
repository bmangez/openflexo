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
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.5-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.11.06 at 03:26:51 PM CET 
//

package org.xmlsoap.schemas.ws._2004._03.business_process;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tElseif complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tElseif">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}tExtensibleElements">
 *       &lt;sequence>
 *         &lt;element ref="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}condition" minOccurs="0"/>
 *         &lt;group ref="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}activity" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tElseif", propOrder = { "condition", "assign", "compensate", "compensateScope", "empty", "exit", "extensionActivity",
		"flow", "forEach", "_if", "invoke", "pick", "receive", "repeatUntil", "reply", "rethrow", "scope", "sequence", "_throw",
		"validate", "wait", "_while", "opaqueActivity" })
public class TElseif extends TExtensibleElements {

	protected TBooleanExpr condition;
	protected TAssign assign;
	protected TCompensate compensate;
	protected TCompensateScope compensateScope;
	protected TEmpty empty;
	protected TExit exit;
	protected TExtensionActivity extensionActivity;
	protected TFlow flow;
	protected TForEach forEach;
	@XmlElement(name = "if")
	protected TIf _if;
	protected TInvoke invoke;
	protected TPick pick;
	protected TReceive receive;
	protected TRepeatUntil repeatUntil;
	protected TReply reply;
	protected TRethrow rethrow;
	protected TScope scope;
	protected TSequence sequence;
	@XmlElement(name = "throw")
	protected TThrow _throw;
	protected TValidate validate;
	protected TWait wait;
	@XmlElement(name = "while")
	protected TWhile _while;
	protected TOpaqueActivity opaqueActivity;

	/**
	 * Gets the value of the condition property.
	 * 
	 * @return possible object is {@link TBooleanExpr }
	 * 
	 */
	public TBooleanExpr getCondition() {
		return condition;
	}

	/**
	 * Sets the value of the condition property.
	 * 
	 * @param value
	 *            allowed object is {@link TBooleanExpr }
	 * 
	 */
	public void setCondition(TBooleanExpr value) {
		this.condition = value;
	}

	/**
	 * Gets the value of the assign property.
	 * 
	 * @return possible object is {@link TAssign }
	 * 
	 */
	public TAssign getAssign() {
		return assign;
	}

	/**
	 * Sets the value of the assign property.
	 * 
	 * @param value
	 *            allowed object is {@link TAssign }
	 * 
	 */
	public void setAssign(TAssign value) {
		this.assign = value;
	}

	/**
	 * Gets the value of the compensate property.
	 * 
	 * @return possible object is {@link TCompensate }
	 * 
	 */
	public TCompensate getCompensate() {
		return compensate;
	}

	/**
	 * Sets the value of the compensate property.
	 * 
	 * @param value
	 *            allowed object is {@link TCompensate }
	 * 
	 */
	public void setCompensate(TCompensate value) {
		this.compensate = value;
	}

	/**
	 * Gets the value of the compensateScope property.
	 * 
	 * @return possible object is {@link TCompensateScope }
	 * 
	 */
	public TCompensateScope getCompensateScope() {
		return compensateScope;
	}

	/**
	 * Sets the value of the compensateScope property.
	 * 
	 * @param value
	 *            allowed object is {@link TCompensateScope }
	 * 
	 */
	public void setCompensateScope(TCompensateScope value) {
		this.compensateScope = value;
	}

	/**
	 * Gets the value of the empty property.
	 * 
	 * @return possible object is {@link TEmpty }
	 * 
	 */
	public TEmpty getEmpty() {
		return empty;
	}

	/**
	 * Sets the value of the empty property.
	 * 
	 * @param value
	 *            allowed object is {@link TEmpty }
	 * 
	 */
	public void setEmpty(TEmpty value) {
		this.empty = value;
	}

	/**
	 * Gets the value of the exit property.
	 * 
	 * @return possible object is {@link TExit }
	 * 
	 */
	public TExit getExit() {
		return exit;
	}

	/**
	 * Sets the value of the exit property.
	 * 
	 * @param value
	 *            allowed object is {@link TExit }
	 * 
	 */
	public void setExit(TExit value) {
		this.exit = value;
	}

	/**
	 * Gets the value of the extensionActivity property.
	 * 
	 * @return possible object is {@link TExtensionActivity }
	 * 
	 */
	public TExtensionActivity getExtensionActivity() {
		return extensionActivity;
	}

	/**
	 * Sets the value of the extensionActivity property.
	 * 
	 * @param value
	 *            allowed object is {@link TExtensionActivity }
	 * 
	 */
	public void setExtensionActivity(TExtensionActivity value) {
		this.extensionActivity = value;
	}

	/**
	 * Gets the value of the flow property.
	 * 
	 * @return possible object is {@link TFlow }
	 * 
	 */
	public TFlow getFlow() {
		return flow;
	}

	/**
	 * Sets the value of the flow property.
	 * 
	 * @param value
	 *            allowed object is {@link TFlow }
	 * 
	 */
	public void setFlow(TFlow value) {
		this.flow = value;
	}

	/**
	 * Gets the value of the forEach property.
	 * 
	 * @return possible object is {@link TForEach }
	 * 
	 */
	public TForEach getForEach() {
		return forEach;
	}

	/**
	 * Sets the value of the forEach property.
	 * 
	 * @param value
	 *            allowed object is {@link TForEach }
	 * 
	 */
	public void setForEach(TForEach value) {
		this.forEach = value;
	}

	/**
	 * Gets the value of the if property.
	 * 
	 * @return possible object is {@link TIf }
	 * 
	 */
	public TIf getIf() {
		return _if;
	}

	/**
	 * Sets the value of the if property.
	 * 
	 * @param value
	 *            allowed object is {@link TIf }
	 * 
	 */
	public void setIf(TIf value) {
		this._if = value;
	}

	/**
	 * Gets the value of the invoke property.
	 * 
	 * @return possible object is {@link TInvoke }
	 * 
	 */
	public TInvoke getInvoke() {
		return invoke;
	}

	/**
	 * Sets the value of the invoke property.
	 * 
	 * @param value
	 *            allowed object is {@link TInvoke }
	 * 
	 */
	public void setInvoke(TInvoke value) {
		this.invoke = value;
	}

	/**
	 * Gets the value of the pick property.
	 * 
	 * @return possible object is {@link TPick }
	 * 
	 */
	public TPick getPick() {
		return pick;
	}

	/**
	 * Sets the value of the pick property.
	 * 
	 * @param value
	 *            allowed object is {@link TPick }
	 * 
	 */
	public void setPick(TPick value) {
		this.pick = value;
	}

	/**
	 * Gets the value of the receive property.
	 * 
	 * @return possible object is {@link TReceive }
	 * 
	 */
	public TReceive getReceive() {
		return receive;
	}

	/**
	 * Sets the value of the receive property.
	 * 
	 * @param value
	 *            allowed object is {@link TReceive }
	 * 
	 */
	public void setReceive(TReceive value) {
		this.receive = value;
	}

	/**
	 * Gets the value of the repeatUntil property.
	 * 
	 * @return possible object is {@link TRepeatUntil }
	 * 
	 */
	public TRepeatUntil getRepeatUntil() {
		return repeatUntil;
	}

	/**
	 * Sets the value of the repeatUntil property.
	 * 
	 * @param value
	 *            allowed object is {@link TRepeatUntil }
	 * 
	 */
	public void setRepeatUntil(TRepeatUntil value) {
		this.repeatUntil = value;
	}

	/**
	 * Gets the value of the reply property.
	 * 
	 * @return possible object is {@link TReply }
	 * 
	 */
	public TReply getReply() {
		return reply;
	}

	/**
	 * Sets the value of the reply property.
	 * 
	 * @param value
	 *            allowed object is {@link TReply }
	 * 
	 */
	public void setReply(TReply value) {
		this.reply = value;
	}

	/**
	 * Gets the value of the rethrow property.
	 * 
	 * @return possible object is {@link TRethrow }
	 * 
	 */
	public TRethrow getRethrow() {
		return rethrow;
	}

	/**
	 * Sets the value of the rethrow property.
	 * 
	 * @param value
	 *            allowed object is {@link TRethrow }
	 * 
	 */
	public void setRethrow(TRethrow value) {
		this.rethrow = value;
	}

	/**
	 * Gets the value of the scope property.
	 * 
	 * @return possible object is {@link TScope }
	 * 
	 */
	public TScope getScope() {
		return scope;
	}

	/**
	 * Sets the value of the scope property.
	 * 
	 * @param value
	 *            allowed object is {@link TScope }
	 * 
	 */
	public void setScope(TScope value) {
		this.scope = value;
	}

	/**
	 * Gets the value of the sequence property.
	 * 
	 * @return possible object is {@link TSequence }
	 * 
	 */
	public TSequence getSequence() {
		return sequence;
	}

	/**
	 * Sets the value of the sequence property.
	 * 
	 * @param value
	 *            allowed object is {@link TSequence }
	 * 
	 */
	public void setSequence(TSequence value) {
		this.sequence = value;
	}

	/**
	 * Gets the value of the throw property.
	 * 
	 * @return possible object is {@link TThrow }
	 * 
	 */
	public TThrow getThrow() {
		return _throw;
	}

	/**
	 * Sets the value of the throw property.
	 * 
	 * @param value
	 *            allowed object is {@link TThrow }
	 * 
	 */
	public void setThrow(TThrow value) {
		this._throw = value;
	}

	/**
	 * Gets the value of the validate property.
	 * 
	 * @return possible object is {@link TValidate }
	 * 
	 */
	public TValidate getValidate() {
		return validate;
	}

	/**
	 * Sets the value of the validate property.
	 * 
	 * @param value
	 *            allowed object is {@link TValidate }
	 * 
	 */
	public void setValidate(TValidate value) {
		this.validate = value;
	}

	/**
	 * Gets the value of the wait property.
	 * 
	 * @return possible object is {@link TWait }
	 * 
	 */
	public TWait getWait() {
		return wait;
	}

	/**
	 * Sets the value of the wait property.
	 * 
	 * @param value
	 *            allowed object is {@link TWait }
	 * 
	 */
	public void setWait(TWait value) {
		this.wait = value;
	}

	/**
	 * Gets the value of the while property.
	 * 
	 * @return possible object is {@link TWhile }
	 * 
	 */
	public TWhile getWhile() {
		return _while;
	}

	/**
	 * Sets the value of the while property.
	 * 
	 * @param value
	 *            allowed object is {@link TWhile }
	 * 
	 */
	public void setWhile(TWhile value) {
		this._while = value;
	}

	/**
	 * Gets the value of the opaqueActivity property.
	 * 
	 * @return possible object is {@link TOpaqueActivity }
	 * 
	 */
	public TOpaqueActivity getOpaqueActivity() {
		return opaqueActivity;
	}

	/**
	 * Sets the value of the opaqueActivity property.
	 * 
	 * @param value
	 *            allowed object is {@link TOpaqueActivity }
	 * 
	 */
	public void setOpaqueActivity(TOpaqueActivity value) {
		this.opaqueActivity = value;
	}

}
