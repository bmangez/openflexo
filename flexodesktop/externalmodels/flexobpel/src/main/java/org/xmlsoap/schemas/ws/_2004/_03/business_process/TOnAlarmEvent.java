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
 * Java class for tOnAlarmEvent complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tOnAlarmEvent">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}tExtensibleElements">
 *       &lt;sequence>
 *         &lt;group ref="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}forOrUntilGroup" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}repeatEvery" minOccurs="0"/>
 *         &lt;element ref="{http://schemas.xmlsoap.org/ws/2004/03/business-process/}scope" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tOnAlarmEvent", propOrder = { "_for", "until", "repeatEvery", "scope" })
public class TOnAlarmEvent extends TExtensibleElements {

	@XmlElement(name = "for")
	protected TDurationExpr _for;
	protected TDeadlineExpr until;
	protected TDurationExpr repeatEvery;
	protected TScope scope;

	/**
	 * Gets the value of the for property.
	 * 
	 * @return possible object is {@link TDurationExpr }
	 * 
	 */
	public TDurationExpr getFor() {
		return _for;
	}

	/**
	 * Sets the value of the for property.
	 * 
	 * @param value
	 *            allowed object is {@link TDurationExpr }
	 * 
	 */
	public void setFor(TDurationExpr value) {
		this._for = value;
	}

	/**
	 * Gets the value of the until property.
	 * 
	 * @return possible object is {@link TDeadlineExpr }
	 * 
	 */
	public TDeadlineExpr getUntil() {
		return until;
	}

	/**
	 * Sets the value of the until property.
	 * 
	 * @param value
	 *            allowed object is {@link TDeadlineExpr }
	 * 
	 */
	public void setUntil(TDeadlineExpr value) {
		this.until = value;
	}

	/**
	 * Gets the value of the repeatEvery property.
	 * 
	 * @return possible object is {@link TDurationExpr }
	 * 
	 */
	public TDurationExpr getRepeatEvery() {
		return repeatEvery;
	}

	/**
	 * Sets the value of the repeatEvery property.
	 * 
	 * @param value
	 *            allowed object is {@link TDurationExpr }
	 * 
	 */
	public void setRepeatEvery(TDurationExpr value) {
		this.repeatEvery = value;
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

}
