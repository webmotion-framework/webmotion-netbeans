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
import java.util.List;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.debux.webmotion.netbeans.javacc.parser.impl.WebMotionParserImpl.WebMotionParserResult;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.csl.api.Hint;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.RuleContext;
import org.netbeans.modules.parsing.api.Source;
import org.openide.filesystems.FileObject;

/**
 *
 * @author julien
 */
public class VariableRule extends AbstractRule {

    @Override
    public String getDisplayName() {
        return "Test variable";
    }

    @Override
    public String getDescription() {
        return "Test if the variable is defined";
    }

    @Override
    public void run(RuleContext context, List<Hint> hints) {
        WebMotionParserResult parserResult = (WebMotionParserResult) context.parserResult;
        
        Source source = parserResult.getSnapshot().getSource();
        Document document = source.getDocument(false);
        FileObject fileObject = source.getFileObject();
        
        List<String> variables = new ArrayList<String>();
        
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        while (ts.moveNext()) {
            
            Token<WebMotionTokenId> token = ts.token();
            WebMotionTokenId id = token.id();
            String name = id.name();
            
            if ("ACTION_METHOD".equals(name)) {
                variables = new ArrayList<String>();
            }
            
            if ("ACTION_PATH_PARAMETER_NAME".equals(name)) {
                String variable = token.text().toString();
                variables.add(variable);
            }
            
            if ("ACTION_PATH_VARIABLE".equals(name) 
                    || "".equals(name)
                    || "ACTION_PATH_PARAMETER_VALUE_VARIABLE".equals(name)) {
                String variable = token.text().toString();
                if (variable.contains(":")) {
                    variables.add(StringUtils.substringBetween(variable, "{", ":"));
                } else {
                    variables.add(StringUtils.substringBetween(variable, "{", "}"));
                }
            }
            
            if ("ACTION_ACTION_JAVA_VARIABLE".equals(name) 
                    || "ACTION_ACTION_VIEW_VARIABLE".equals(name)
                    || "ACTION_ACTION_LINK_VARIABLE".equals(name)) {
                
                String variable = token.text().toString();
                variable = StringUtils.substringBetween(variable, "{", "}");
                
                if (!variables.contains(variable)) {
                    int start = ts.offset() + 1;
                    int end = start + variable.length();
                    OffsetRange range = new OffsetRange(start, end);
                    
                    hints.add(new Hint(this, "Variable not defined", fileObject, range, WebMotionHintsProvider.NO_FIXES, 100));
                }
            }
        }
    }
}
