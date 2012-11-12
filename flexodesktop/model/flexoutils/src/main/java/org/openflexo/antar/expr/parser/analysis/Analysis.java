/* This file was generated by SableCC (http://www.sablecc.org/). */

package org.openflexo.antar.expr.parser.analysis;

import org.openflexo.antar.expr.parser.node.*;

public interface Analysis extends Switch
{
    Object getIn(Node node);
    void setIn(Node node, Object o);
    Object getOut(Node node);
    void setOut(Node node, Object o);

    void caseStart(Start node);
    void caseAExpr2Expr(AExpr2Expr node);
    void caseACondExprExpr(ACondExprExpr node);
    void caseAEqExprExpr(AEqExprExpr node);
    void caseAEq2ExprExpr(AEq2ExprExpr node);
    void caseANeqExprExpr(ANeqExprExpr node);
    void caseALtExprExpr(ALtExprExpr node);
    void caseAGtExprExpr(AGtExprExpr node);
    void caseALteExprExpr(ALteExprExpr node);
    void caseAGteExprExpr(AGteExprExpr node);
    void caseAExpr3Expr2(AExpr3Expr2 node);
    void caseAOrExprExpr2(AOrExprExpr2 node);
    void caseAOr2ExprExpr2(AOr2ExprExpr2 node);
    void caseAAddExprExpr2(AAddExprExpr2 node);
    void caseASubExprExpr2(ASubExprExpr2 node);
    void caseATermExpr3(ATermExpr3 node);
    void caseAAndExprExpr3(AAndExprExpr3 node);
    void caseAAnd2ExprExpr3(AAnd2ExprExpr3 node);
    void caseAMultExprExpr3(AMultExprExpr3 node);
    void caseADivExprExpr3(ADivExprExpr3 node);
    void caseAModExprExpr3(AModExprExpr3 node);
    void caseAPowerExprExpr3(APowerExprExpr3 node);
    void caseANotExprExpr3(ANotExprExpr3 node);
    void caseACall(ACall node);
    void caseAEmptyListArgList(AEmptyListArgList node);
    void caseANonEmptyListArgList(ANonEmptyListArgList node);
    void caseAAdditionalArg(AAdditionalArg node);
    void caseAIdentifierBinding(AIdentifierBinding node);
    void caseACallBinding(ACallBinding node);
    void caseATail1Binding(ATail1Binding node);
    void caseATail2Binding(ATail2Binding node);
    void caseACosFuncFunction(ACosFuncFunction node);
    void caseAAcosFuncFunction(AAcosFuncFunction node);
    void caseASinFuncFunction(ASinFuncFunction node);
    void caseAAsinFuncFunction(AAsinFuncFunction node);
    void caseATanFuncFunction(ATanFuncFunction node);
    void caseAAtanFuncFunction(AAtanFuncFunction node);
    void caseAExpFuncFunction(AExpFuncFunction node);
    void caseALogFuncFunction(ALogFuncFunction node);
    void caseASqrtFuncFunction(ASqrtFuncFunction node);
    void caseATrueConstant(ATrueConstant node);
    void caseAFalseConstant(AFalseConstant node);
    void caseANullConstant(ANullConstant node);
    void caseAThisConstant(AThisConstant node);
    void caseAPiConstant(APiConstant node);
    void caseADecimalNumberNumber(ADecimalNumberNumber node);
    void caseAPreciseNumberNumber(APreciseNumberNumber node);
    void caseAScientificNotationNumberNumber(AScientificNotationNumberNumber node);
    void caseAConstantNumber(AConstantNumber node);
    void caseANegativeTerm(ANegativeTerm node);
    void caseANumberTerm(ANumberTerm node);
    void caseAStringValueTerm(AStringValueTerm node);
    void caseACharsValueTerm(ACharsValueTerm node);
    void caseAFunctionTerm(AFunctionTerm node);
    void caseABindingTerm(ABindingTerm node);
    void caseAExprTerm(AExprTerm node);

    void caseTPlus(TPlus node);
    void caseTMinus(TMinus node);
    void caseTMult(TMult node);
    void caseTDiv(TDiv node);
    void caseTMod(TMod node);
    void caseTPower(TPower node);
    void caseTLt(TLt node);
    void caseTGt(TGt node);
    void caseTLte(TLte node);
    void caseTGte(TGte node);
    void caseTAnd(TAnd node);
    void caseTAnd2(TAnd2 node);
    void caseTOr(TOr node);
    void caseTOr2(TOr2 node);
    void caseTEq(TEq node);
    void caseTEq2(TEq2 node);
    void caseTNeq(TNeq node);
    void caseTNot(TNot node);
    void caseTLPar(TLPar node);
    void caseTRPar(TRPar node);
    void caseTDot(TDot node);
    void caseTComma(TComma node);
    void caseTIfToken(TIfToken node);
    void caseTElseToken(TElseToken node);
    void caseTPi(TPi node);
    void caseTCos(TCos node);
    void caseTAcos(TAcos node);
    void caseTSin(TSin node);
    void caseTAsin(TAsin node);
    void caseTTan(TTan node);
    void caseTAtan(TAtan node);
    void caseTLog(TLog node);
    void caseTExp(TExp node);
    void caseTSqrt(TSqrt node);
    void caseTTrue(TTrue node);
    void caseTFalse(TFalse node);
    void caseTNull(TNull node);
    void caseTThis(TThis node);
    void caseTDecimalNumber(TDecimalNumber node);
    void caseTPreciseNumber(TPreciseNumber node);
    void caseTScientificNotationNumber(TScientificNotationNumber node);
    void caseTStringValue(TStringValue node);
    void caseTCharsValue(TCharsValue node);
    void caseTIdentifier(TIdentifier node);
    void caseTBlank(TBlank node);
    void caseEOF(EOF node);
}
