/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.openflexo.antar.expr.parser.parser;

import org.openflexo.antar.expr.parser.analysis.AnalysisAdapter;
import org.openflexo.antar.expr.parser.node.EOF;
import org.openflexo.antar.expr.parser.node.TAcos;
import org.openflexo.antar.expr.parser.node.TAnd;
import org.openflexo.antar.expr.parser.node.TAnd2;
import org.openflexo.antar.expr.parser.node.TAsin;
import org.openflexo.antar.expr.parser.node.TAtan;
import org.openflexo.antar.expr.parser.node.TCharsValue;
import org.openflexo.antar.expr.parser.node.TComma;
import org.openflexo.antar.expr.parser.node.TCos;
import org.openflexo.antar.expr.parser.node.TDecimalNumber;
import org.openflexo.antar.expr.parser.node.TDiv;
import org.openflexo.antar.expr.parser.node.TDot;
import org.openflexo.antar.expr.parser.node.TElseToken;
import org.openflexo.antar.expr.parser.node.TEq;
import org.openflexo.antar.expr.parser.node.TEq2;
import org.openflexo.antar.expr.parser.node.TExp;
import org.openflexo.antar.expr.parser.node.TFalse;
import org.openflexo.antar.expr.parser.node.TGt;
import org.openflexo.antar.expr.parser.node.TGte;
import org.openflexo.antar.expr.parser.node.TIdentifier;
import org.openflexo.antar.expr.parser.node.TIfToken;
import org.openflexo.antar.expr.parser.node.TLPar;
import org.openflexo.antar.expr.parser.node.TLog;
import org.openflexo.antar.expr.parser.node.TLt;
import org.openflexo.antar.expr.parser.node.TLte;
import org.openflexo.antar.expr.parser.node.TMinus;
import org.openflexo.antar.expr.parser.node.TMod;
import org.openflexo.antar.expr.parser.node.TMult;
import org.openflexo.antar.expr.parser.node.TNeq;
import org.openflexo.antar.expr.parser.node.TNot;
import org.openflexo.antar.expr.parser.node.TNull;
import org.openflexo.antar.expr.parser.node.TOr;
import org.openflexo.antar.expr.parser.node.TOr2;
import org.openflexo.antar.expr.parser.node.TPi;
import org.openflexo.antar.expr.parser.node.TPlus;
import org.openflexo.antar.expr.parser.node.TPower;
import org.openflexo.antar.expr.parser.node.TPreciseNumber;
import org.openflexo.antar.expr.parser.node.TRPar;
import org.openflexo.antar.expr.parser.node.TScientificNotationNumber;
import org.openflexo.antar.expr.parser.node.TSin;
import org.openflexo.antar.expr.parser.node.TSqrt;
import org.openflexo.antar.expr.parser.node.TStringValue;
import org.openflexo.antar.expr.parser.node.TTan;
import org.openflexo.antar.expr.parser.node.TTrue;

class TokenIndex extends AnalysisAdapter {
	int index;

	@Override
	public void caseTPlus(@SuppressWarnings("unused") TPlus node) {
		this.index = 0;
	}

	@Override
	public void caseTMinus(@SuppressWarnings("unused") TMinus node) {
		this.index = 1;
	}

	@Override
	public void caseTMult(@SuppressWarnings("unused") TMult node) {
		this.index = 2;
	}

	@Override
	public void caseTDiv(@SuppressWarnings("unused") TDiv node) {
		this.index = 3;
	}

	@Override
	public void caseTMod(@SuppressWarnings("unused") TMod node) {
		this.index = 4;
	}

	@Override
	public void caseTPower(@SuppressWarnings("unused") TPower node) {
		this.index = 5;
	}

	@Override
	public void caseTLt(@SuppressWarnings("unused") TLt node) {
		this.index = 6;
	}

	@Override
	public void caseTGt(@SuppressWarnings("unused") TGt node) {
		this.index = 7;
	}

	@Override
	public void caseTLte(@SuppressWarnings("unused") TLte node) {
		this.index = 8;
	}

	@Override
	public void caseTGte(@SuppressWarnings("unused") TGte node) {
		this.index = 9;
	}

	@Override
	public void caseTAnd(@SuppressWarnings("unused") TAnd node) {
		this.index = 10;
	}

	@Override
	public void caseTAnd2(@SuppressWarnings("unused") TAnd2 node) {
		this.index = 11;
	}

	@Override
	public void caseTOr(@SuppressWarnings("unused") TOr node) {
		this.index = 12;
	}

	@Override
	public void caseTOr2(@SuppressWarnings("unused") TOr2 node) {
		this.index = 13;
	}

	@Override
	public void caseTEq(@SuppressWarnings("unused") TEq node) {
		this.index = 14;
	}

	@Override
	public void caseTEq2(@SuppressWarnings("unused") TEq2 node) {
		this.index = 15;
	}

	@Override
	public void caseTNeq(@SuppressWarnings("unused") TNeq node) {
		this.index = 16;
	}

	@Override
	public void caseTNot(@SuppressWarnings("unused") TNot node) {
		this.index = 17;
	}

	@Override
	public void caseTLPar(@SuppressWarnings("unused") TLPar node) {
		this.index = 18;
	}

	@Override
	public void caseTRPar(@SuppressWarnings("unused") TRPar node) {
		this.index = 19;
	}

	@Override
	public void caseTDot(@SuppressWarnings("unused") TDot node) {
		this.index = 20;
	}

	@Override
	public void caseTComma(@SuppressWarnings("unused") TComma node) {
		this.index = 21;
	}

	@Override
	public void caseTIfToken(@SuppressWarnings("unused") TIfToken node) {
		this.index = 22;
	}

	@Override
	public void caseTElseToken(@SuppressWarnings("unused") TElseToken node) {
		this.index = 23;
	}

	@Override
	public void caseTPi(@SuppressWarnings("unused") TPi node) {
		this.index = 24;
	}

	@Override
	public void caseTCos(@SuppressWarnings("unused") TCos node) {
		this.index = 25;
	}

	@Override
	public void caseTAcos(@SuppressWarnings("unused") TAcos node) {
		this.index = 26;
	}

	@Override
	public void caseTSin(@SuppressWarnings("unused") TSin node) {
		this.index = 27;
	}

	@Override
	public void caseTAsin(@SuppressWarnings("unused") TAsin node) {
		this.index = 28;
	}

	@Override
	public void caseTTan(@SuppressWarnings("unused") TTan node) {
		this.index = 29;
	}

	@Override
	public void caseTAtan(@SuppressWarnings("unused") TAtan node) {
		this.index = 30;
	}

	@Override
	public void caseTLog(@SuppressWarnings("unused") TLog node) {
		this.index = 31;
	}

	@Override
	public void caseTExp(@SuppressWarnings("unused") TExp node) {
		this.index = 32;
	}

	@Override
	public void caseTSqrt(@SuppressWarnings("unused") TSqrt node) {
		this.index = 33;
	}

	@Override
	public void caseTTrue(@SuppressWarnings("unused") TTrue node) {
		this.index = 34;
	}

	@Override
	public void caseTFalse(@SuppressWarnings("unused") TFalse node) {
		this.index = 35;
	}

	@Override
	public void caseTNull(@SuppressWarnings("unused") TNull node) {
		this.index = 36;
	}

	@Override
	public void caseTDecimalNumber(@SuppressWarnings("unused") TDecimalNumber node) {
		this.index = 37;
	}

	@Override
	public void caseTPreciseNumber(@SuppressWarnings("unused") TPreciseNumber node) {
		this.index = 38;
	}

	@Override
	public void caseTScientificNotationNumber(@SuppressWarnings("unused") TScientificNotationNumber node) {
		this.index = 39;
	}

	@Override
	public void caseTStringValue(@SuppressWarnings("unused") TStringValue node) {
		this.index = 40;
	}

	@Override
	public void caseTCharsValue(@SuppressWarnings("unused") TCharsValue node) {
		this.index = 41;
	}

	@Override
	public void caseTIdentifier(@SuppressWarnings("unused") TIdentifier node) {
		this.index = 42;
	}

	@Override
	public void caseEOF(@SuppressWarnings("unused") EOF node) {
		this.index = 43;
	}
}
