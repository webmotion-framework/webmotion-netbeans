package org.debux.webmotion.netbeans.hints;

import java.util.Collections;
import java.util.List;
import org.debux.webmotion.netbeans.javacc.parser.impl.WebMotionParserImpl.WebMotionParserResult;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.api.*;
import org.netbeans.modules.csl.api.HintsProvider.HintsManager;
import org.netbeans.modules.csl.api.Rule.AstRule;
import org.openide.filesystems.FileObject;

/**
 *
 * @author julien
 */
public class WebMotionHintsProvider implements HintsProvider {

    private static final List<HintFix> NO_FIXES = Collections.<HintFix>emptyList();

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
    }

    private void apply(RuleContext context, HintsManager manager, List<Hint> hints) {
        WebMotionParserResult parserResult = (WebMotionParserResult) context.parserResult;
        FileObject fo = parserResult.getSnapshot().getSource().getFileObject();
        
        List<? extends AstRule> rules = manager.getHints().get(WebMotionRule.Kinds.DEFAULT);
        for (AstRule rule : rules) {
            hints.add(new Hint(rule, "Test", fo, new OffsetRange(0, 10), NO_FIXES, 100));
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
