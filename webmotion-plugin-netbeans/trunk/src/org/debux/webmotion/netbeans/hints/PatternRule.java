package org.debux.webmotion.netbeans.hints;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
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
public class PatternRule extends AbstractRule {

    public enum Kinds {
        DEFAULT;
    }
    
    @Override
    public String getDisplayName() {
        return "Test pattern";
    }

    @Override
    public String getDescription() {
        return "Test valid pattern in variable";
    }

    @Override
    public void run(RuleContext context, List<Hint> hints) {
        WebMotionParserResult parserResult = (WebMotionParserResult) context.parserResult;
        
        Source source = parserResult.getSnapshot().getSource();
        Document document = source.getDocument(false);
        FileObject fileObject = source.getFileObject();
        
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        while (ts.moveNext()) {
            
            Token<WebMotionTokenId> tok = ts.token();
            WebMotionTokenId id = tok.id();
            String name = id.name();
            // TODO jru 20120626 improve syntax to get directly the pattern
            if (name.equals("ACTION_PATH_VARIABLE") || name.equals("ACTION_PATH_PARAMETER_VALUE_VARIABLE")) {
                String value = tok.text().toString();
                    
                String regexp = StringUtils.substringBetween(value, ":", "}");
                if (regexp != null) {
                    
                    try {
                        String pattern = regexp.replaceAll("\\\\\\{", "{");
                        pattern = pattern.replaceAll("\\\\\\}", "}");
                        Pattern.compile(pattern);

                    } catch (PatternSyntaxException pse) {
                        int end = ts.offset() + value.length() -1;
                        int start = end - regexp.length();
                        OffsetRange offsetRange = new OffsetRange(start, end);
                        hints.add(new Hint(this, "Invalid pattern", fileObject, offsetRange, WebMotionHintsProvider.NO_FIXES, 100));
                    }
                }
            }
        }
    }
}
