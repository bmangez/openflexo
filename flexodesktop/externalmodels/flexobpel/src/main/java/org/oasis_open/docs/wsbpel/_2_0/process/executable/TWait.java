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
// Generated on: 2008.02.08 at 10:43:57 AM CET 
//

package org.oasis_open.docs.wsbpel._2_0.process.executable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for tWait complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tWait">
 *   &lt;complexContent>
 *     &lt;extension base="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}tActivity">
 *       &lt;choice>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}for"/>
 *         &lt;element ref="{http://docs.oasis-open.org/wsbpel/2.0/process/executable}until"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tWait", propOrder = { "_for", "until" })
public class TWait extends TActivity {

	@XmlElement(name = "for")
	protected TDurationExpr _for;
	protected TDeadlineExpr until;

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

}
