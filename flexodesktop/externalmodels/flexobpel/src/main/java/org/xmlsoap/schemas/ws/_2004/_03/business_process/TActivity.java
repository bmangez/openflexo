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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tActivity complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tActivity">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}tExtensibleElements">
 *       &lt;sequence>
 *         &lt;element ref="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}targets" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}sources" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}NCName" />
 *       &lt;attribute name="suppressJoinFailure" type="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}tBoolean" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tActivity", propOrder = { "targets", "sources" })
@XmlSeeAlso({ TSequence.class, TForEach.class, TValidate.class, TCompensate.class, TEmpty.class, TThrow.class, TPick.class, TInvoke.class,
		TReply.class, TAssign.class, TWait.class, TCompensateScope.class, TScope.class, TRepeatUntil.class, TExit.class, TWhile.class,
		TFlow.class, TRethrow.class, TReceive.class, TOpaqueActivity.class, TIf.class })
public class TActivity extends TExtensibleElements {

	protected TTargets targets;
	protected TSources sources;
	@XmlAttribute(namespace = "http://schemas.xmlsoap.org/ws/2004/03/business-process/")
	protected String name;
	@XmlAttribute(namespace = "http://schemas.xmlsoap.org/ws/2004/03/business-process/")
	protected TBoolean suppressJoinFailure;

	/**
	 * Gets the value of the targets property.
	 * 
	 * @return possible object is {@link TTargets }
	 * 
	 */
	public TTargets getTargets() {
		return targets;
	}

	/**
	 * Sets the value of the targets property.
	 * 
	 * @param value
	 *            allowed object is {@link TTargets }
	 * 
	 */
	public void setTargets(TTargets value) {
		this.targets = value;
	}

	/**
	 * Gets the value of the sources property.
	 * 
	 * @return possible object is {@link TSources }
	 * 
	 */
	public TSources getSources() {
		return sources;
	}

	/**
	 * Sets the value of the sources property.
	 * 
	 * @param value
	 *            allowed object is {@link TSources }
	 * 
	 */
	public void setSources(TSources value) {
		this.sources = value;
	}

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the suppressJoinFailure property.
	 * 
	 * @return possible object is {@link TBoolean }
	 * 
	 */
	public TBoolean getSuppressJoinFailure() {
		return suppressJoinFailure;
	}

	/**
	 * Sets the value of the suppressJoinFailure property.
	 * 
	 * @param value
	 *            allowed object is {@link TBoolean }
	 * 
	 */
	public void setSuppressJoinFailure(TBoolean value) {
		this.suppressJoinFailure = value;
	}

}
