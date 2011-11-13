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
package cb.petal;

import java.util.Collection;

/**
 * Represents State_Transition object
 * 
 * @version $Id: StateTransition.java,v 1.3 2011/09/12 11:46:49 gpolet Exp $
 * @author <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public class StateTransition extends QuiduObject implements Labeled, HasSupplier {
	public StateTransition(PetalNode parent, Collection params) {
		super(parent, "State_Transition", params);
	}

	public StateTransition() {
		super("State_Transition");
	}

	@Override
	public String getLabel() {
		return getPropertyAsString("label");
	}

	@Override
	public void setLabel(String o) {
		defineProperty("label", o);
	}

	@Override
	public String getSupplier() {
		return getPropertyAsString("supplier");
	}

	@Override
	public void setSupplier(String o) {
		defineProperty("supplier", o);
	}

	public Event getEvent() {
		return (Event) getProperty("Event");
	}

	public void setEvent(Event o) {
		defineProperty("Event", o);
	}

	public Action getAction() {
		return (Action) getProperty("action");
	}

	public void setAction(Action o) {
		defineProperty("action", o);
	}

	public SendEvent getSendEvent() {
		return (SendEvent) getProperty("sendEvent");
	}

	public void setSendEvent(SendEvent o) {
		defineProperty("sendEvent", o);
	}

	public String getCondition() {
		return getPropertyAsString("condition");
	}

	public void setCondition(String o) {
		defineProperty("condition", o);
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
