/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.openflexo.antar.expr.parser.node;

import org.openflexo.antar.expr.parser.analysis.*;

@SuppressWarnings("nls")
public final class TElseToken extends Token {
	public TElseToken() {
		super.setText(":");
	}

	public TElseToken(int line, int pos) {
		super.setText(":");
		setLine(line);
		setPos(pos);
	}

	@Override
	public Object clone() {
		return new TElseToken(getLine(), getPos());
	}

	@Override
	public void apply(Switch sw) {
		((Analysis) sw).caseTElseToken(this);
	}

	@Override
	public void setText(@SuppressWarnings("unused") String text) {
		throw new RuntimeException("Cannot change TElseToken text.");
	}
}
