package org.debux.webmotion.netbeans.hints;

import java.util.Collections;
import java.util.List;
import org.debux.webmotion.netbeans.javacc.parser.impl.WebMotionParserImpl.WebMotionParserResult;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.api.*;
import org.netbeans.modules.csl.api.HintsProvider.HintsManager;
import org.openide.filesystems.FileObject;

/**
 *
 * @author julien
 */
public class WebMotionHintsProvider implements HintsProvider {

    public static final List<HintFix> NO_FIXES = Collections.<HintFix>emptyList();

    public WebMotionHintsProvider() {
    }
    
    @Override
    public void computeHints(HintsManager manager, RuleContext context, List<Hint> hints) {
        apply(context, manager, hints);
    }

    @Override
    public void computeSuggestions(HintsManager manager, RuleContext context, List<Hint> suggestions, int caretOffset) {
    } 

    @Override
    public void computeSelectionHints(HintsManager manager, RuleContext context, List<Hint> suggestions, int start, int end) {
    }

    @Override
    public void computeErrors(HintsManager manager, RuleContext context, List<Hint> hints, List<Error> unhandled) {
        WebMotionParserResult parserResult = (WebMotionParserResult) context.parserResult;
        List<? extends Error> diagnostics = parserResult.getDiagnostics();
        unhandled.addAll(diagnostics);
    }

    private void apply(RuleContext context, HintsManager manager, List<Hint> hints) {
        List<PatternRule> rules = (List<PatternRule>) manager.getHints().get(PatternRule.Kinds.DEFAULT);
        for (PatternRule rule : rules) {
            rule.run(context, hints);
        }
    }

    @Override
    public void cancel() {
    }

    @Override
    public List<Rule> getBuiltinRules() {
        return null;
    }

    @Override
    public RuleContext createRuleContext() {
        return new RuleContext();
    }
    
}
