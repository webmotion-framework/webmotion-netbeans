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
