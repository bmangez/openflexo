/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.openflexo.antar.expr.parser.node;

import org.openflexo.antar.expr.parser.analysis.*;

@SuppressWarnings("nls")
public final class AParameteredTypeReference extends PTypeReference {
	private TDollar _dollar_;
	private PTypeReferencePath _typeReferencePath_;
	private PTypeReferenceArgList _typeReferenceArgList_;

	public AParameteredTypeReference() {
		// Constructor
	}

	public AParameteredTypeReference(@SuppressWarnings("hiding") TDollar _dollar_,
			@SuppressWarnings("hiding") PTypeReferencePath _typeReferencePath_,
			@SuppressWarnings("hiding") PTypeReferenceArgList _typeReferenceArgList_) {
		// Constructor
		setDollar(_dollar_);

		setTypeReferencePath(_typeReferencePath_);

		setTypeReferenceArgList(_typeReferenceArgList_);

	}

	@Override
	public Object clone() {
		return new AParameteredTypeReference(cloneNode(this._dollar_), cloneNode(this._typeReferencePath_),
				cloneNode(this._typeReferenceArgList_));
	}

	@Override
	public void apply(Switch sw) {
		((Analysis) sw).caseAParameteredTypeReference(this);
	}

	public TDollar getDollar() {
		return this._dollar_;
	}

	public void setDollar(TDollar node) {
		if (this._dollar_ != null) {
			this._dollar_.parent(null);
		}

		if (node != null) {
			if (node.parent() != null) {
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		this._dollar_ = node;
	}

	public PTypeReferencePath getTypeReferencePath() {
		return this._typeReferencePath_;
	}

	public void setTypeReferencePath(PTypeReferencePath node) {
		if (this._typeReferencePath_ != null) {
			this._typeReferencePath_.parent(null);
		}

		if (node != null) {
			if (node.parent() != null) {
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		this._typeReferencePath_ = node;
	}

	public PTypeReferenceArgList getTypeReferenceArgList() {
		return this._typeReferenceArgList_;
	}

	public void setTypeReferenceArgList(PTypeReferenceArgList node) {
		if (this._typeReferenceArgList_ != null) {
			this._typeReferenceArgList_.parent(null);
		}

		if (node != null) {
			if (node.parent() != null) {
				node.parent().removeChild(node);
			}

			node.parent(this);
		}

		this._typeReferenceArgList_ = node;
	}

	@Override
	public String toString() {
		return "" + toString(this._dollar_) + toString(this._typeReferencePath_) + toString(this._typeReferenceArgList_);
	}

	@Override
	void removeChild(@SuppressWarnings("unused") Node child) {
		// Remove child
		if (this._dollar_ == child) {
			this._dollar_ = null;
			return;
		}

		if (this._typeReferencePath_ == child) {
			this._typeReferencePath_ = null;
			return;
		}

		if (this._typeReferenceArgList_ == child) {
			this._typeReferenceArgList_ = null;
			return;
		}

		throw new RuntimeException("Not a child.");
	}

	@Override
	void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild) {
		// Replace child
		if (this._dollar_ == oldChild) {
			setDollar((TDollar) newChild);
			return;
		}

		if (this._typeReferencePath_ == oldChild) {
			setTypeReferencePath((PTypeReferencePath) newChild);
			return;
		}

		if (this._typeReferenceArgList_ == oldChild) {
			setTypeReferenceArgList((PTypeReferenceArgList) newChild);
			return;
		}

		throw new RuntimeException("Not a child.");
	}
}