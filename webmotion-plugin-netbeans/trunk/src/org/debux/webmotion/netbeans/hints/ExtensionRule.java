package org.debux.webmotion.netbeans.hints;

import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.debux.webmotion.netbeans.Utils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.LexerUtils;
import org.debux.webmotion.netbeans.javacc.parser.impl.WebMotionParserImpl.WebMotionParserResult;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.modules.csl.api.Hint;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.RuleContext;
import org.netbeans.modules.parsing.api.Source;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author julien
 */
public class ExtensionRule extends AbstractRule {

    @Override
    public String getDisplayName() {
        return "Test view";
    }

    @Override
    public String getDescription() {
        return "Test if the view exists";
    }

    @Override
    public void run(RuleContext context, List<Hint> hints) {
        WebMotionParserResult parserResult = (WebMotionParserResult) context.parserResult;
        
        Source source = parserResult.getSnapshot().getSource();
        Document document = source.getDocument(false);
        FileObject fileObject = source.getFileObject();
        
        List<OffsetRange> tokens = LexerUtils.getTokens(document, "EXTENSION_FILE");
        for (OffsetRange range : tokens) {
            try {
                String value = LexerUtils.getText(document, range);
                if (!Utils.isPattern(value)) {
                    GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                    FileObject fo = registry.findResource(value);
                    if (fo == null) {
                        hints.add(new Hint(this, "Invalid file", 
                                fileObject, range, WebMotionHintsProvider.asList(new FileFix(fileObject, value)), 100));
                    }
                }
                
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
