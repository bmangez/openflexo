/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.openflexo.antar.expr.parser.node;

import org.openflexo.antar.expr.parser.analysis.Analysis;

@SuppressWarnings("nls")
public final class TLte extends Token {
	public TLte() {
		super.setText("<=");
	}

	public TLte(int line, int pos) {
		super.setText("<=");
		setLine(line);
		setPos(pos);
	}

	@Override
	public Object clone() {
		return new TLte(getLine(), getPos());
	}

	@Override
	public void apply(Switch sw) {
		((Analysis) sw).caseTLte(this);
	}

	@Override
	public void setText(@SuppressWarnings("unused") String text) {
		throw new RuntimeException("Cannot change TLte text.");
	}
}
