package org.debux.webmotion.netbeans;

import java.util.Map;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.debux.webmotion.netbeans.javacc.parser.impl.WebMotionParserFactory;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProvider;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.cookies.OpenCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;

/**
 *
 * @author julien
 */
@MimeRegistration(mimeType = "text/x-wm", service = HyperlinkProvider.class)
public class WebMotionActionHyperlink implements HyperlinkProvider {

    private String target;
    private int targetStart;
    private int targetEnd;

    @Override
    public boolean isHyperlinkPoint(Document doc, int offset) {
        return verifyState(doc, offset);
    }

    @Override
    public int[] getHyperlinkSpan(Document document, int offset) {
        if (verifyState(document, offset)) {
            return new int[]{targetStart, targetEnd};
        } else {
            return null;
        }
    }

    @Override
    public void performClickAction(Document document, int offset) {
        if (verifyState(document, offset)) {
            try {
                GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                target = target.replaceAll("\\.", "/");

                Map<String, String> configurations = WebMotionParserFactory.parser.getParser().configurations;
                String packageBase = configurations.get("package.base").replaceAll("\\.", "/");
//                String packageActions = configurations.get("package.actions");
//                String packageFilters = configurations.get("package.filters");
//                String packageErrors = configurations.get("package.errors");
                
                FileObject fo = registry.findResource(packageBase + "/" + StringUtils.substringBeforeLast(target, "/") + ".java");
                if (fo == null) {
                    fo = registry.findResource(target + ".java");
                }
                
                if (fo != null) {
                    DataObject dob = DataObject.find(fo);
                    OpenCookie open = dob.getLookup().lookup(OpenCookie.class);
                    open.open();
                }
                
            } catch (DataObjectNotFoundException e) {
                NotifyDescriptor.Message msg = new NotifyDescriptor.Message(target);
                DialogDisplayer.getDefault().notify(msg);
            }
        }
    }
    
    public boolean verifyState(Document doc, int offset) {
        TokenHierarchy<Document> hi = TokenHierarchy.get(doc);
        TokenSequence<WebMotionTokenId> ts = hi.tokenSequence(WebMotionTokenId.getLanguage());
        if (ts != null) {
            targetStart = 0;
            targetEnd = 0;
            target = "";
            
            ts.move(offset);
            ts.moveNext();
            
            Token<WebMotionTokenId> tok = ts.token();
            if (test(tok)) {
                
                ts.move(offset);
                ts.moveNext();
                tok = ts.token();
                
                 while (test(tok)) {
                    String matcherText = tok.text().toString();
                    target += matcherText;
                    
                    ts.moveNext();
                    tok = ts.token();
                }
                
                ts.move(offset);
                ts.movePrevious();
                tok = ts.token();
                
                while (test(tok)) {
                    String matcherText = tok.text().toString();
                    target = matcherText + target;
                    
                    int newOffset = ts.offset();
                    targetStart = newOffset;
                    
                    ts.movePrevious();
                    tok = ts.token();
                }
                
                targetEnd = targetStart + target.length();
                return true;
            }
        }
        
        return false;
    }
    
    public boolean test(Token<WebMotionTokenId> tok) {
        WebMotionTokenId id = tok.id();
        String name = id.name();
            
        return "ACTION_ACTION_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_VARIABLE".equals(name) ||
                
                "FILTER_ACTION".equals(name) ||
                
                "ERROR_ACTION_JAVA".equals(name) ||
                "EXCEPTION".equals(name);
    }

}
