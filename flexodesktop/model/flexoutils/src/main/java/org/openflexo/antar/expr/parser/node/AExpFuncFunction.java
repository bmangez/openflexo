/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.openflexo.antar.expr.parser.node;

import org.openflexo.antar.expr.parser.analysis.*;

@SuppressWarnings("nls")
public final class AExpFuncFunction extends PFunction
{
    private TExp _exp_;
    private TLPar _lPar_;
    private PExpr2 _expr2_;
    private TRPar _rPar_;

    public AExpFuncFunction()
    {
        // Constructor
    }

    public AExpFuncFunction(
        @SuppressWarnings("hiding") TExp _exp_,
        @SuppressWarnings("hiding") TLPar _lPar_,
        @SuppressWarnings("hiding") PExpr2 _expr2_,
        @SuppressWarnings("hiding") TRPar _rPar_)
    {
        // Constructor
        setExp(_exp_);

        setLPar(_lPar_);

        setExpr2(_expr2_);

        setRPar(_rPar_);

    }

    @Override
    public Object clone()
    {
        return new AExpFuncFunction(
            cloneNode(this._exp_),
            cloneNode(this._lPar_),
            cloneNode(this._expr2_),
            cloneNode(this._rPar_));
    }

    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAExpFuncFunction(this);
    }

    public TExp getExp()
    {
        return this._exp_;
    }

    public void setExp(TExp node)
    {
        if(this._exp_ != null)
        {
            this._exp_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._exp_ = node;
    }

    public TLPar getLPar()
    {
        return this._lPar_;
    }

    public void setLPar(TLPar node)
    {
        if(this._lPar_ != null)
        {
            this._lPar_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._lPar_ = node;
    }

    public PExpr2 getExpr2()
    {
        return this._expr2_;
    }

    public void setExpr2(PExpr2 node)
    {
        if(this._expr2_ != null)
        {
            this._expr2_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._expr2_ = node;
    }

    public TRPar getRPar()
    {
        return this._rPar_;
    }

    public void setRPar(TRPar node)
    {
        if(this._rPar_ != null)
        {
            this._rPar_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._rPar_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._exp_)
            + toString(this._lPar_)
            + toString(this._expr2_)
            + toString(this._rPar_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._exp_ == child)
        {
            this._exp_ = null;
            return;
        }

        if(this._lPar_ == child)
        {
            this._lPar_ = null;
            return;
        }

        if(this._expr2_ == child)
        {
            this._expr2_ = null;
            return;
        }

        if(this._rPar_ == child)
        {
            this._rPar_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._exp_ == oldChild)
        {
            setExp((TExp) newChild);
            return;
        }

        if(this._lPar_ == oldChild)
        {
            setLPar((TLPar) newChild);
            return;
        }

        if(this._expr2_ == oldChild)
        {
            setExpr2((PExpr2) newChild);
            return;
        }

        if(this._rPar_ == oldChild)
        {
            setRPar((TRPar) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
