package org.debux.webmotion.netbeans.hints;

import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
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
public class ViewRule extends AbstractRule {

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
        
        String packageViews = Utils.getPackageValue("package.views", null);
        packageViews = packageViews.replaceAll("\\.+", "/");
        
        Source source = parserResult.getSnapshot().getSource();
        Document document = source.getDocument(false);
        FileObject fileObject = source.getFileObject();
        
        List<OffsetRange> tokens = LexerUtils.getTokens(document, 
                "ACTION_ACTION_VIEW", 
                "ACTION_ACTION_VIEW_VALUE", 
                "ACTION_ACTION_VIEW_VARIABLE", 
                "ERROR_ACTION_VIEW_BEGIN", 
                "ERROR_ACTION_VALUE");
        for (OffsetRange range : tokens) {
            try {
                String value = LexerUtils.getText(document, range);
                if (value.startsWith("view:")) {
                    String viewName = StringUtils.substringAfter(value, "view:");
                    if (Utils.isNotVariable(viewName)) {
                        
                        GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                        FileObject fo = registry.findResource(packageViews + viewName);
                        if (fo == null) {
                            int end = range.getStart() + value.length();
                            int start = end - viewName.length();
                            OffsetRange offsetRange = new OffsetRange(start, end);
                            hints.add(new Hint(this, "Invalid view", fileObject, offsetRange, WebMotionHintsProvider.NO_FIXES, 100));
                        }
                    }
                }
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
