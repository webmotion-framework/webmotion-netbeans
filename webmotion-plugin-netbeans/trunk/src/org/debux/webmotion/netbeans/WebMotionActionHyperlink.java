package org.debux.webmotion.netbeans;

import java.util.Map;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
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
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.Line;
import org.openide.text.NbDocument;

/**
 *
 * @author julien
 */
@MimeRegistration(mimeType = "text/x-wm", service = HyperlinkProvider.class)
public class WebMotionActionHyperlink implements HyperlinkProvider {

    @Override
    public boolean isHyperlinkPoint(Document document, int offset) {
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        if (ts != null) {
            ts.move(offset);
            ts.moveNext();
            
            Token<WebMotionTokenId> tok = ts.token();
            return test(tok);
        }
        
        return false;
    }

    @Override
    public int[] getHyperlinkSpan(Document document, int offset) {
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        if (ts != null) {
            int targetStart = 0;
            int targetEnd = 0;
            
            ts.move(offset);
            ts.moveNext();
            Token<WebMotionTokenId> tok = ts.token();

            while (test(tok)) {
                ts.moveNext();
                tok = ts.token();
                
                targetEnd = ts.offset();
            }

            ts.move(offset);
            ts.movePrevious();
            tok = ts.token();

            while (test(tok)) {
                targetStart = ts.offset();

                ts.movePrevious();
                tok = ts.token();
            }

            if (targetStart != 0 && targetEnd != 0) {
                return new int[]{targetStart, targetEnd};
            }
        }
        return null;
    }

    @Override
    // jru 20120605 : TODO create method to get package target
    public void performClickAction(Document document, int offset) {
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        if (ts != null) {
            String target = "";
            
            ts.move(offset);
            ts.moveNext();
            Token<WebMotionTokenId> tok = ts.token();

            // Get Configuration
            // jru 20120605 : Fix NPE, Improve get parser
            Map<String, String> configurations = WebMotionParserFactory.parser.getParser().configurations;
            
            String packageBase = configurations.get("package.base");
            if (packageBase == null) {
                packageBase = "";
            }
            
            String packageActions = configurations.get("package.actions");
            if (packageActions == null) {
                packageActions = "";
            }
            
            String packageFilters = configurations.get("package.filters");
            if (packageFilters == null) {
                packageFilters = "";
            }
            
            String packageErrors = configurations.get("package.errors");
            if (packageErrors == null) {
                packageErrors = "";
            }
            
            // Compute package
            String packageTarget = null;
            
            WebMotionTokenId id = tok.id();
            String name = id.name();
            if ("ACTION_ACTION_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_VARIABLE".equals(name)) {
                
                if (!packageBase.isEmpty() && !packageActions.isEmpty()) {
                    packageTarget = packageBase + "." + packageActions;
                } else if (!packageActions.isEmpty()) {
                    packageTarget = packageActions;
                } else {
                    packageTarget = packageBase;
                }
                
            } else if ("FILTER_ACTION".equals(name)) {
                if (!packageBase.isEmpty() && !packageFilters.isEmpty()) {
                    packageTarget = packageBase + "." + packageFilters;
                } else if (!packageFilters.isEmpty()) {
                    packageTarget = packageFilters;
                } else {
                    packageTarget = packageBase;
                }
                
            } else if ("ERROR_ACTION_JAVA".equals(name)) {
                if (!packageBase.isEmpty() && !packageErrors.isEmpty()) {
                    packageTarget = packageBase + "." + packageErrors;
                } else if (!packageErrors.isEmpty()) {
                    packageTarget = packageErrors;
                } else {
                    packageTarget = packageBase;
                }
                
            } else if ("EXCEPTION".equals(name)) {
                packageTarget = "";
            }
            
            // Search target
            while (test(tok)) {
                target += tok.text().toString();
                
                ts.moveNext();
                tok = ts.token();
            }

            ts.move(offset);
            ts.movePrevious();
            tok = ts.token();

            while (test(tok)) {
                target = tok.text().toString() + target;

                ts.movePrevious();
                tok = ts.token();
            }

            // Open document
            if (!target.isEmpty()) {
                try {
                    target = target.replaceAll("\\.", "/");
                    packageTarget = packageTarget.replaceAll("\\.", "/");
                    if (!packageTarget.isEmpty()) {
                        packageTarget += "/";
                    }
                        
                    String className = StringUtils.substringBeforeLast(target, "/");

                    GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                    FileObject fo = registry.findResource(packageTarget + className + ".java");
                    
                    if (fo != null) {
                        String methodName = StringUtils.substringAfterLast(target, "/");
                        open(fo, methodName);
                        
                    } else {
                        fo = registry.findResource(target + ".java");
                        open(fo, className);
                    }

                } catch (DataObjectNotFoundException e) {
                    NotifyDescriptor.Message msg = new NotifyDescriptor.Message(target);
                    DialogDisplayer.getDefault().notify(msg);
                }
            }
        }
    }

    protected void open(FileObject fo, String mark) throws IndexOutOfBoundsException, DataObjectNotFoundException {
        if (fo != null) {
            DataObject dob = DataObject.find(fo);
            EditorCookie open = dob.getLookup().lookup(EditorCookie.class);
            open.open();
            
            StyledDocument doc = open.getDocument();
            TokenHierarchy<StyledDocument> hi = TokenHierarchy.get(doc);
            TokenSequence<?> ts = hi.tokenSequence();
            while (ts.moveNext()) {
                Token<?> tok = ts.token();
                String matcherText = tok.text().toString();
                if (mark.equals(matcherText)) {
                    
                    int offset = ts.offset();
                    int line = NbDocument.findLineNumber(doc, offset);
                    open.getLineSet().getCurrent(line).show(Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FOCUS);
                    break;
                }
            }
        }
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
