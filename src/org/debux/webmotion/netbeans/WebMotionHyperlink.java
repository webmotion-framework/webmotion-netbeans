package org.debux.webmotion.netbeans;

import java.io.IOException;
import java.util.Map;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.debux.webmotion.netbeans.javacc.parser.WebMotionParser;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProvider;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.Line;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import static org.debux.webmotion.netbeans.WebMotionLanguage.MIME_TYPE;

/**
 *
 * @author julien
 */
@MimeRegistration(mimeType = MIME_TYPE, service = HyperlinkProvider.class)
public class WebMotionHyperlink implements HyperlinkProvider {

    @Override
    public boolean isHyperlinkPoint(Document document, int offset) {
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        if (ts != null) {
            ts.move(offset);
            ts.moveNext();
            
            Token<WebMotionTokenId> tok = ts.token();
            return verifyToken(tok);
        }
        
        return false;
    }

    @Override
    public int[] getHyperlinkSpan(Document document, int offset) {
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        if (ts != null) {
            
            ts.move(offset);
            ts.moveNext();
            Token<WebMotionTokenId> tok = ts.token();
            
            int targetEnd = ts.offset();
            int targetStart = ts.offset();

            while (verifyToken(tok)) {
                ts.moveNext();
                tok = ts.token();
                
                targetEnd = ts.offset();
            }

            ts.move(offset);
            ts.movePrevious();
            tok = ts.token();

            while (verifyToken(tok)) {
                targetStart = ts.offset();

                ts.movePrevious();
                tok = ts.token();
            }

            if (targetStart != targetEnd) {
                return new int[]{targetStart, targetEnd};
            }
        }
        return null;
    }

    @Override
    public void performClickAction(Document document, int offset) {
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        if (ts != null) {
            String target = "";
            
            ts.move(offset);
            ts.moveNext();

            // Get the package in configuration
            String packageBase = getPackageValue("package.base", null);
            String packageTarget = null;
            boolean isJavaFile = true;
            
            Token<WebMotionTokenId> tok = ts.token();
            WebMotionTokenId id = tok.id();
            String name = id.name();
            if ("ACTION_ACTION_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_VARIABLE".equals(name)) {
                
                packageTarget = getPackageValue("package.actions", packageBase);
                
            } else if ("FILTER_ACTION".equals(name)) {
                packageTarget = getPackageValue("package.filters", packageBase);
                
            } else if ("ERROR_ACTION_JAVA".equals(name)) {
                packageTarget = getPackageValue("package.errors", packageBase);
                
            } else if ("EXTENSION_FILE".equals(name)) {
                packageTarget = "";
                isJavaFile = false;
                
            } else if ("ACTION_ACTION_VIEW_VALUE".equals(name) ||
                       "ACTION_ACTION_VIEW_VARIABLE".equals(name) ||
                       "ERROR_ACTION_VALUE".equals(name)) {
                packageTarget = getPackageValue("package.views", null);
                isJavaFile = false;
                
            } else if ("EXCEPTION".equals(name)) {
                packageTarget = "";
            }
            
            // Search target
            while (verifyToken(tok)) {
                target += tok.text().toString();
                
                ts.moveNext();
                tok = ts.token();
            }

            ts.move(offset);
            ts.movePrevious();
            tok = ts.token();

            while (verifyToken(tok)) {
                target = tok.text().toString() + target;

                ts.movePrevious();
                tok = ts.token();
            }

            // Open document
            if (!target.isEmpty()) {
                try {
                    GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                    
                    if (isJavaFile) {
                        target = target.replaceAll("\\.", "/");
                        String className = StringUtils.substringBeforeLast(target, "/");
                        String mark = StringUtils.substringAfterLast(target, "/");

                        FileObject fo = registry.findResource(packageTarget + className + ".java");
                        if (fo == null) {
                            fo = registry.findResource(target + ".java");
                        }

                        open(fo, mark);
                            
                    } else {
                        FileObject fo = registry.findResource(packageTarget + target);
                        open(fo, null);
                    }
                    
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
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
            if (mark != null && doc != null) {
                TokenHierarchy<StyledDocument> hi = TokenHierarchy.get(doc);
                TokenSequence<?> ts = hi.tokenSequence();
                
                while (ts.moveNext()) {
                    Token<?> token = ts.token();
                    String matcherText = token.text().toString();
                    if (mark.equals(matcherText)) {

                        int offset = ts.offset();
                        int line = NbDocument.findLineNumber(doc, offset);
                        open.getLineSet().getCurrent(line).show(Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FOCUS);
                        break;
                    }
                }
            }
        }
    }
    
    protected String getPackageValue(String name, String packageBase) {
        Map<String, String> configurations = WebMotionParser.configurations;
        String packageValue = configurations.get(name);
        
        String packageTarget = "";
        
        if (StringUtils.isNotEmpty(packageBase) && StringUtils.isNotEmpty(packageValue)) {
            packageTarget = packageBase.replaceFirst("\\.$", "") + "." + packageValue.replaceFirst("^\\.", "");
            
        } else if (StringUtils.isNotEmpty(packageBase)) {
            packageTarget = packageBase.replaceFirst("\\.$", "");
            
        } else if (StringUtils.isNotEmpty(packageValue)) {
            packageTarget = packageValue.replaceFirst("\\.$", "");
        }
       
        packageTarget = packageTarget.replaceAll("\\.", "/");
        
        if (!packageTarget.isEmpty()) {
            packageTarget += "/";
        }

        return packageTarget;
    }
    
    public boolean verifyToken(Token<WebMotionTokenId> token){
        WebMotionTokenId id = token.id();
        String name = id.name();
        
        return "ACTION_ACTION_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_VARIABLE".equals(name) ||
                
                "ACTION_ACTION_VIEW_VALUE".equals(name) ||
                "ACTION_ACTION_VIEW_VARIABLE".equals(name) ||
                
                "FILTER_ACTION".equals(name) ||
                
                "EXTENSION_FILE".equals(name) ||
                
                "ERROR_ACTION_JAVA".equals(name) ||
                "EXCEPTION".equals(name) ||
                "ERROR_ACTION_VALUE".equals(name);
    }

}
