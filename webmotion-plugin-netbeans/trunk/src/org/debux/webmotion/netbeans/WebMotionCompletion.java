package org.debux.webmotion.netbeans;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JToolTip;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.debux.webmotion.netbeans.javacc.parser.WebMotionParser;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.api.java.source.CancellableTask;
import org.netbeans.api.java.source.ClassIndex;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.ElementHandle;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.support.CancellableTreePathScanner;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.spi.editor.completion.CompletionDocumentation;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

/**
 *
 * @author julien
 */
@MimeRegistration(mimeType = "text/x-wm", service = CompletionProvider.class)
public class WebMotionCompletion implements CompletionProvider {

    public static final String[] KEYWORDS_SECTIONS = {
        "[config]", "[properties]", "[actions]", "[filters]", "[errors]", "[extensions]"
    };
            
    public static final String[] KEYWORDS_CONFIG = {
        "[config]", "[properties]", "[actions]", "[filters]", "[errors]", "[extensions]",
        "package.views", "package.base", "package.filters", "package.actions",
        "package.errors", "javac.debug", "server.async", "server.encoding", "server.error.page",
        "server.controller.scope", "server.listener.class", "server.main.handler.class",
        "server.secret", "server.static.autodetect"
    };
            
    public static final String[] KEYWORDS_METHODS = {
        "[config]", "[properties]", "[actions]", "[filters]", "[errors]", "[extensions]",
        "*", "GET", "POST", "PUT", "DELETE", "HEAD"
    };
            
    public static final String[] KEYWORDS_ACTION = {
        "/"
    };
            
    public static final String[] KEYWORDS_ACTION_ACTION = {
        "action:", "async:", "sync:", "view:", "url:", "redirect:", "forward:"
    };
            
    public static final String[] KEYWORDS_FILTER_ACTION = {
        "action:"
    };
            
    public static final String[] KEYWORDS_ERROR = {
        "[config]", "[properties]", "[actions]", "[filters]", "[errors]", "[extensions]",
        "code:400", "code:401", "code:403", "code:404", "code:408", "code:500", "*"
    };
            
    public static final String[] KEYWORDS_EXTENSION = {
        "[config]", "[properties]", "[actions]", "[filters]", "[errors]", "[extensions]",
        "/"
    };
            
    public static final String[] KEYWORDS_FILTER = {
        "/", "/*"
    };
            
    public static final String[] KEYWORDS_ERROR_ACTION = {
        "action:", "view:", "url:", "redirect:", "forward:"
    };
            
    @Override
    public CompletionTask createTask(int queryType, JTextComponent component) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
           return null;
        }
        
        return new AsyncCompletionTask(new AsyncCompletionQuery() {
            @Override
            protected void query(CompletionResultSet completionResultSet, Document document, int caretOffset) {
                
                String filter = null;
                int startOffset = caretOffset - 1;

                try {
                    StyledDocument bDoc = (StyledDocument) document;
                    int lineStartOffset = getRowFirstNonWhite(bDoc, caretOffset);
                    char[] line = bDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
                    int whiteOffset = indexOfWhite(line);
                    filter = new String(line, whiteOffset + 1, line.length - whiteOffset - 1);
                    if (whiteOffset > 0) {
                        startOffset = lineStartOffset + whiteOffset + 1;
                    } else {
                        startOffset = lineStartOffset;
                    }
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
        
                TokenHierarchy<Document> hi = TokenHierarchy.get(document);
                TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
                
                ts.move(caretOffset);
                ts.moveNext();
                
                String sectionName = null;
                int column = 0;
                do {
                    
                    Token<WebMotionTokenId> tok = ts.token();
                    WebMotionTokenId id = tok.id();
                    String name = id.name();
                    
                    if (name.startsWith("SECTION_")) {
                        sectionName = tok.text().toString();
                    }
                    
                    if ("ACTION_SEPARATOR".equals(name)
                            || "ACTION_PATH_END".equals(name)
                            || "ACTION_PATH_PARAMETER_SEPARATOR".equals(name)
                            || "ACTION_PATH_PARAMETER_VALUE_SEPARATOR".equals(name)
                            || "ACTION_PARAMETERS_SEPARATOR".equals(name)
                            
                            || "ACTION_ACTION_JAVA_END".equals(name)
                            || "ACTION_ACTION_VIEW_END".equals(name)
                            || "ACTION_ACTION_LINK_END".equals(name)
                            || "ACTION_PARAMETER_VALUE_END".equals(name)
                            || "ACTION_END".equals(name)
                            
                            || "ERROR_SEPARATOR".equals(name)
                            
                            || "ERROR_END".equals(name)
                            || "ERROR_VALUE_END".equals(name)
                            
                            || "FILTER_SEPARATOR".equals(name)
                            || "FILTER_END".equals(name)
                            || "FILTER_PARAMETER_VALUE_END".equals(name)
                            
                            || "EXTENSION_SEPARATOR".equals(name)
                            || "EXTENSION_END".equals(name)) {
                        
                        column ++;
                    }
                } while(ts.movePrevious() && sectionName == null);
                
                // Get the package in configuration
                String packageBase = getPackageValue("package.base", null);
                String packageTarget = null;
                String[] keywords = {};
                if (sectionName != null) {
                    
                    if (sectionName.startsWith("[config]")) {
                        keywords = KEYWORDS_CONFIG;
                        
                    } else if (sectionName.startsWith("[errors]") && column % 2 == 0) {
                        keywords = KEYWORDS_ERROR;
                        packageTarget = "";
                        
                    } else if (sectionName.startsWith("[errors]") && column % 2 == 1) {
                        keywords = KEYWORDS_ERROR_ACTION;
                        packageTarget = getPackageValue("package.errors", packageBase);
                        
                    } else if (sectionName.startsWith("[extensions]") && column % 2 == 1) {
                        keywords = KEYWORDS_EXTENSION;
                        
                    } else if (sectionName.startsWith("[filters]") && column % 3 == 0) {
                        keywords = KEYWORDS_METHODS;
                        
                    } else if (sectionName.startsWith("[filters]") && column % 3 == 1) {
                        keywords = KEYWORDS_FILTER;
                        
                    } else if (sectionName.startsWith("[filters]") && column % 3 == 2) {
                        keywords = KEYWORDS_FILTER_ACTION;
                        packageTarget = getPackageValue("package.filters", packageBase);
                        
                    } else if (sectionName.startsWith("[actions]") && column % 3 == 0) {
                        keywords = KEYWORDS_METHODS;
                        
                    } else if (sectionName.startsWith("[actions]") && column % 3 == 1) {
                        keywords = KEYWORDS_ACTION;
                        
                    } else if (sectionName.startsWith("[actions]") && column % 3 == 2) {
                        keywords = KEYWORDS_ACTION_ACTION;
                        packageTarget = getPackageValue("package.actions", packageBase);
                        
                    } else if (sectionName.contains("properties]")) {
                        keywords = KEYWORDS_SECTIONS;
                    }
                    
                } else {
                    keywords = KEYWORDS_SECTIONS;
                }
                
                // Keywords
                for (String keyword : keywords) {
                    if (keyword.startsWith(filter)) {
                        completionResultSet.addItem(new WebMotionCompletionItem(keyword, startOffset, caretOffset));
                    }
                }
                
                if (packageTarget != null) {
                    String filterClass = packageTarget + filter;
                    if (filter.contains(":") && !filter.startsWith("code:")) {
                        filterClass = packageTarget + StringUtils.substringAfter(":", filter);
                        startOffset += StringUtils.substringBefore(":", filter).length() + 1;
                    }
                    
                    // Class
                    FileObject fo = getFO(document);
                    ClassPath bootCp = ClassPath.getClassPath(fo, ClassPath.BOOT);
                    ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);
                    ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);
                    ClasspathInfo info = ClasspathInfo.create(bootCp, compileCp, sourcePath);
                    
                    Set<ElementHandle<TypeElement>> result = info.getClassIndex()
                            .getDeclaredTypes("", ClassIndex.NameKind.PREFIX,
                                                EnumSet.of(ClassIndex.SearchScope.SOURCE, ClassIndex.SearchScope.DEPENDENCIES));
                    
                    for (ElementHandle<TypeElement> type : result) {
                        String binaryName = type.getBinaryName();
                        if (!binaryName.equals("") && binaryName.startsWith(filterClass)) {

                            String value = binaryName.replaceFirst("^" + packageTarget, "");
                            completionResultSet.addItem(new WebMotionCompletionItem(value, startOffset, caretOffset));
                        }
                    }
                    
                    // Method
                    String className = StringUtils.substringBeforeLast(filterClass, ".").replaceAll("\\.", "/");
                    final String filterMethod = StringUtils.substringAfterLast(filter, ".");

                    JavaSource javaSource = null;
                    final CompletionResultSet completionResultSetJavaSource = completionResultSet;
                    final int startOffsetJavaSource = startOffset + StringUtils.substringBeforeLast(filter, ".").length() + 1;
                    final int caretOffesetJavaSource = caretOffset;
                        
                    GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                    FileObject file = registry.findResource(className + ".java");
                    if (file != null) {
                        try {
                            javaSource = JavaSource.create(info, file);
                            javaSource.runUserActionTask(new CancellableTask<CompilationController>() {
                                @Override
                                public void cancel() {
                                }

                                @Override
                                public void run(CompilationController cu) throws Exception {
                                    cu.toPhase(JavaSource.Phase.PARSED);
                                    CompilationUnitTree tree = cu.getCompilationUnit();

                                    List<String> methods = new ArrayList<String>();
                                    JavaMethodVisitor visitor = new JavaMethodVisitor();
                                    visitor.scan(tree, methods);

                                    for (String name : methods) {
                                        if (name.startsWith(filterMethod)) {
                                            completionResultSetJavaSource.addItem(new WebMotionCompletionItem(name, startOffsetJavaSource, caretOffesetJavaSource));
                                        }
                                    }
                                    completionResultSetJavaSource.finish();
                                }
                            }, false);
                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                    
                    if (javaSource == null) {
                        completionResultSet.finish();
                    }
                    
                } else {
                    completionResultSet.finish();
                }
            }
        }, component);
    }
   
    public static class JavaMethodVisitor extends CancellableTreePathScanner<Object, List<String>> {
        @Override
        public Object visitMethod(MethodTree method, List<String> p) {
            Name methodName = method.getName();
            p.add(methodName.toString());
            return super.visitMethod(method, p);
        }
    }
    
    static String getPackageValue(String name, String packageBase) {
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
       
        if (!packageTarget.isEmpty()) {
            packageTarget += ".";
        }

        return packageTarget;
    }
    
    static int getRowFirstNonWhite(StyledDocument doc, int offset) throws BadLocationException {
        Element lineElement = doc.getParagraphElement(offset);
        int start = lineElement.getStartOffset();
        while (start + 1 < lineElement.getEndOffset()) {
            try {
                char charAt = doc.getText(start, 1).charAt(0);
                if (charAt != ' ') {
                    break;
                }
            } catch (BadLocationException ex) {
                throw (BadLocationException) new BadLocationException(
                        "calling getText(" + start + ", " + (start + 1)
                        + ") on doc of length: " + doc.getLength(), start).initCause(ex);
            }
            start++;
        }
        return start;
    }

    static int indexOfWhite(char[] line) {
        int i = line.length;
        while (--i > -1) {
            final char c = line[i];
            if (Character.isWhitespace(c)) {
                return i;
            }
        }
        return -1;
    }
             
    static FileObject getFO(Document doc) {
        Object sdp = doc.getProperty(Document.StreamDescriptionProperty);
        if (sdp instanceof FileObject) {
            return (FileObject) sdp;
        }
        if (sdp instanceof DataObject) {
            DataObject dobj = (DataObject) sdp;
            return dobj.getPrimaryFile();
        }
        return null;
    }
    
    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        return 1;
    }
    
    public static class WebMotionCompletionItem implements CompletionItem {

        private String text;
        private static ImageIcon fieldIcon = new ImageIcon(ImageUtilities.loadImage("org/debux/webmotion/netbeans/icon.png"));
        private static Color fieldColor = Color.decode("0x0000B2");
        private int caretOffset;
        private int dotOffset;

        public WebMotionCompletionItem(String text, int dotOffset, int caretOffset) {
            this.text = text;
            this.dotOffset = dotOffset;
            this.caretOffset = caretOffset;
        }

        @Override
        public void defaultAction(JTextComponent component) {
            StyledDocument doc = (StyledDocument) component.getDocument();
            try {
                doc.remove(dotOffset, caretOffset - dotOffset);
                doc.insertString(dotOffset, text, null);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
            //This statement will close the code completion box:
            Completion.get().hideAll();
        }

        @Override
        public void processKeyEvent(KeyEvent ke) {
        }

        @Override
        public int getPreferredWidth(Graphics graphics, Font font) {
            return CompletionUtilities.getPreferredWidth(text, null, graphics, font);
        }

        @Override
        public void render(Graphics g, Font defaultFont, Color defaultColor,
                Color backgroundColor, int width, int height, boolean selected) {
            CompletionUtilities.renderHtml(fieldIcon, text, null, g, defaultFont,
                    (selected ? Color.white : fieldColor), width, height, selected);
        }

        @Override
        public CompletionTask createDocumentationTask() {
            return new AsyncCompletionTask(new AsyncCompletionQuery() {
                @Override
                protected void query(CompletionResultSet completionResultSet, Document document, int i) {
                    completionResultSet.setDocumentation(new KeywordCompletionDocumentation(WebMotionCompletionItem.this));
                    completionResultSet.finish();
                }
            });
        }

        @Override
        public CompletionTask createToolTipTask() {
            return new AsyncCompletionTask(new AsyncCompletionQuery() {
                @Override
                protected void query(CompletionResultSet completionResultSet, Document document, int i) {
                    JToolTip toolTip = new JToolTip();
                    toolTip.setTipText("Press Enter to insert \"" + text + "\"");
                    completionResultSet.setToolTip(toolTip);
                    completionResultSet.finish();
                }
            });
        }

        @Override
        public boolean instantSubstitution(JTextComponent jtc) {
            return false;
        }

        @Override
        public int getSortPriority() {
            return 0;
        }

        @Override
        public CharSequence getSortText() {
            return text;
        }

        @Override
        public CharSequence getInsertPrefix() {
            return text;
        }
        
    }
    
    public static class KeywordCompletionDocumentation implements CompletionDocumentation {

        private WebMotionCompletionItem item;

        public KeywordCompletionDocumentation(WebMotionCompletionItem item) {
            this.item = item;
        }

        @Override
        public String getText() {
            return "Information about " + item.text;
        }

        @Override
        public URL getURL() {
            return null;
        }

        @Override
        public CompletionDocumentation resolveLink(String string) {
            return null;
        }

        @Override
        public Action getGotoSourceAction() {
            return null;
        }
    }
    
}
