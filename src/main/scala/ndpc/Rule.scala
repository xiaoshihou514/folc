package ndpc
import ndpc.Formula.LFormula

object Rule {
    type LF[A] = LFormula[A]
    type LF_ = LFormula[_]
    enum Rule:
        case Intro(i: Introduction)
        case Elim(e: Elimination)
        case Derived(d: Derived)

    enum Introduction:
        // ¬¬introduction, ¬¬I: From 𝝓, deduce ¬¬𝝓
        // 1 𝝓      proved this somehow
        // 2 ¬¬𝝓    ¬¬I(1)
        case DoubleNeg(orig: LF_)
        // ⊥-introduction, or ⊥I: To prove ⊥, you must prove 𝝓 and ¬𝝓 (for any 𝝓 you like).
        // 1 𝝓 got this somehow
        // 2 ...
        // 3 ¬𝝓 and this
        // 4 ⊥ ⊥I(1, 3)
        case Falsity(orig: LF_, negated: LF[LFormula.Not[_]])
        // ⊤-introduction, You can introduce ⊤ anywhere (for all the good it does you).
        case Truth
        // ↔-introduction, or ↔I: To prove 𝝓 ↔ φ, prove both 𝝓 → φ and φ → 𝝓.
        case Equiv(
            leftImp: LF[LFormula.Implies[_, _]],
            rightImp: LF[LFormula.Implies[_, _]]
        )
        case Exists(orig: LF[LFormula.Exists[_]])
        // To introduce the sentence ∀x 𝝓 for some 𝝓(x), you introduce a new
        // constant, say c, not used in the proof so far, and prove 𝝓[c/x].
        case Forall(orig: LF[LFormula.Forall[_]])

    enum Elimination:
        // ¬¬Elimination, ¬¬E: From ¬¬𝝓, deduce 𝝓
        // 1 ¬¬𝝓    proved this somehow
        // 2 𝝓      ¬¬E(1)
        case DoubleNeg(orig: LF[LFormula.Not[LF[LFormula.Not[_]]]])
        // ⊥-elimination, ⊥E: This encode the fact that a contradiction can prove anything.
        // 1 ⊥ we got this
        // 2 𝝓 ⊥E(1)
        case Falsity(orig: LF[false])
        // same as ⊥I
        case Not(orig: LF[false])
        // ↔-elimination, ↔E: From 𝝓 ↔ φ and 𝝓, you can prove φ. From 𝝓 ↔ φ and φ, you can prove 𝝓.
        case Equiv(
            leftImp: LF[LFormula.Equiv[_, _]],
            rightImp: LF_
        )
        // ∃-elimination, or ∃E: Let 𝝓 be a formula. If you have managed to write down ∃x 𝝓,
        // you can prove a sentence φ from it by
        // • assuming 𝝓[c/x], where c is a new constant not used in Â or in
        // the proof so far,
        // • proving φ from this assumption.
        case Exists(orig: LF[LFormula.Exists[_]])
        // ∀-elimination, or ∀E: Let 𝝓(x) be a formula. If you have managed to
        // write down ∀x 𝝓, you can go on to write down ∀[t/x] for any closed
        // term t. (It’s your choice which t!)
        case Forall(orig: LF[LFormula.Forall[_]])

    enum Special:
        // Law of excluded middle (p ∨ ¬p)
        case LEM
        // Modus Tollens: From 𝝓 → φ and ¬φ, derive ¬𝝓.
        case MT(imp: LF[LFormula.Implies[_, _]], not: LF[LFormula.Not[_]])
        case PC[A](orig: LF_, neg: LF[LFormula.Not[LF_]])
        case Refl
        case EqSub(orig: LF_, eq: LF[LFormula.Eq])
        case Sym(orig: LF[LFormula.Eq])
}
