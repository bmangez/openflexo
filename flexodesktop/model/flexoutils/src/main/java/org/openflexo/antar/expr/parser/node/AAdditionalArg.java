/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.openflexo.antar.expr.parser.node;

import org.openflexo.antar.expr.parser.analysis.*;

@SuppressWarnings("nls")
public final class AAdditionalArg extends PAdditionalArg {
	private TComma _comma_;
	private PExpr _expr_;

	public AAdditionalArg() {
		// Constructor
	}

	public AAdditionalArg(@SuppressWarnings("hiding") TComma _comma_, @SuppressWarnings("hiding") PExpr _expr_) {
		// Constructor
		setComma(_comma_);

		setExpr(_expr_);

	}

	@Override
	public Object clone() {
		return new AAdditionalArg(cloneNode(this._comma_), cloneNode(this._expr_));
	}

	@Override
	public void apply(Switch sw) {
		((Analysis) sw).caseAAdditionalArg(this);
	}

	public TComma getComma() {
		return this._comma_;
	}

	public void setComma(TComma node) {
		if (this._comma_ != null) {
			this._comma_.parent(null);
		}

		if (node != null) {
			if (node.parent() != null) {
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		this._comma_ = node;
	}

	public PExpr getExpr() {
		return this._expr_;
	}

	public void setExpr(PExpr node) {
		if (this._expr_ != null) {
			this._expr_.parent(null);
		}

		if (node != null) {
			if (node.parent() != null) {
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		this._expr_ = node;
	}

	@Override
	public String toString() {
		return "" + toString(this._comma_) + toString(this._expr_);
	}

	@Override
	void removeChild(@SuppressWarnings("unused") Node child) {
		// Remove child
		if (this._comma_ == child) {
			this._comma_ = null;
			return;
		}

		if (this._expr_ == child) {
			this._expr_ = null;
			return;
		}

		throw new RuntimeException("Not a child.");
	}

	@Override
	void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
		// Replace child
		if (this._comma_ == oldChild) {
			setComma((TComma) newChild);
			return;
		}

		if (this._expr_ == oldChild) {
			setExpr((PExpr) newChild);
			return;
		}

		throw new RuntimeException("Not a child.");
	}
}
