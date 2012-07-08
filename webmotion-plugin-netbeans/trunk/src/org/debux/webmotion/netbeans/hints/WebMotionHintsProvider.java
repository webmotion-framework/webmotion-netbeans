/*
 * #%L
 * WebMotion plugin netbeans
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2012 Debux
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.debux.webmotion.netbeans.hints;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.debux.webmotion.netbeans.javacc.parser.impl.WebMotionParserImpl.WebMotionParserResult;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.api.*;
import org.netbeans.modules.csl.api.HintsProvider.HintsManager;

/**
 *
 * @author julien
 */
public class WebMotionHintsProvider implements HintsProvider {

    public static final List<HintFix> NO_FIXES = Collections.<HintFix>emptyList();

    public static List<HintFix> asList(HintFix fix) {
        List<HintFix> fixes = new ArrayList<HintFix>();
        fixes.add(fix);
        return fixes;
    }
    
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
        List<AbstractRule> rules = (List<AbstractRule>) manager.getHints().get(AbstractRule.Kinds.DEFAULT);
        for (AbstractRule rule : rules) {
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
