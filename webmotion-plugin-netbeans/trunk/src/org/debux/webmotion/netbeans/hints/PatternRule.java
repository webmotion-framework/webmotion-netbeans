package org.debux.webmotion.netbeans.hints;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JComponent;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.debux.webmotion.netbeans.javacc.parser.impl.WebMotionParserImpl.WebMotionParserResult;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.BaseDocument;
import org.netbeans.modules.csl.api.Hint;
import org.netbeans.modules.csl.api.HintSeverity;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.Rule.AstRule;
import org.netbeans.modules.csl.api.RuleContext;
import org.netbeans.modules.parsing.api.Source;
import org.openide.filesystems.FileObject;

/**
 *
 * @author julien
 */
public class PatternRule implements AstRule {

    public enum Kinds {
        DEFAULT;
    }
    
    @Override
    public Set<?> getKinds() {
        return Collections.singleton(Kinds.DEFAULT);
    }

    @Override
    public String getId() {
        return getClass().getSimpleName();
    }

    @Override
    public String getDescription() {
        return "Test valid pattern in variable";
    }

    @Override
    public boolean getDefaultEnabled() {
        return true;
    }

    @Override
    public JComponent getCustomizer(Preferences node) {
        return null;
    }
    
    @Override
    public boolean appliesTo(RuleContext context) {
        return true; //always
    }

    @Override
    public String getDisplayName() {
        return "Test pattern";
    }

    @Override
    public boolean showInTasklist() {
        return true;
    }

    @Override
    public HintSeverity getDefaultSeverity() {
        return HintSeverity.WARNING;
    }
    
    public void run(RuleContext context, List<Hint> hints) {
        BaseDocument doc = context.doc;
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
