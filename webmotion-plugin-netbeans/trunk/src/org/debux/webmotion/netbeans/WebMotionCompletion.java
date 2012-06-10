package org.debux.webmotion.netbeans;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
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

    public static final String[] keywords = {
                                                "[config]", "[properties]", "[actions]", "[filters]", "[errors]", "[extensions]",
                                                "/", "*", "GET", "POST", "PUT", "DELETE", "HEAD",
                                                "action:", "async:", "sync:",
                                                "view:", "url:", "redirect:", "forward:",
                                                "package.views", "package.base", "package.filters", "package.actions",
                                                "package.errors", "javac.debug", "server.async", "server.encoding", "server.error.page",
                                                "server.controller.scope", "server.listener.class", "server.main.handler.class",
                                                "server.secret", "server.static.autodetect"
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
                    final StyledDocument bDoc = (StyledDocument) document;
                    final int lineStartOffset = getRowFirstNonWhite(bDoc, caretOffset);
                    final char[] line = bDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
                    final int whiteOffset = indexOfWhite(line);
                    filter = new String(line, whiteOffset + 1, line.length - whiteOffset - 1);
                    if (whiteOffset > 0) {
                        startOffset = lineStartOffset + whiteOffset + 1;
                    } else {
                        startOffset = lineStartOffset;
                    }
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
        
                // Keyword
                for (String keyword : keywords) {
                    if (keyword.startsWith(filter)) {
                        completionResultSet.addItem(new KeywordCompletionItem(keyword, startOffset, caretOffset));
                    }
                }
                
                // Java action
                TokenHierarchy<Document> hi = TokenHierarchy.get(document);
                TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
                if (ts != null) {
                    ts.move(caretOffset);
                    ts.movePrevious();
                    
                    Token<WebMotionTokenId> tokken = ts.token();
                    WebMotionTokenId id = tokken.id();
                    String name = id.name();
                    
                    // Get the package in configuration
                    String packageBase = getPackageValue("package.base", null);
                    String packageTarget = null;
                    boolean isJavaFile = true;

                    if ("ACTION_PATH_END".equals(name)
                            || "ACTION_PATH_PARAMETER_SEPARATOR".equals(name)
                            || "ACTION_PATH_PARAMETER_VALUE_SEPARATOR".equals(name)
                            || "ACTION_ACTION_JAVA_BEGIN".equals(name)
                            || "ACTION_ACTION_IDENTIFIER".equals(name)
                            || "ACTION_ACTION_JAVA_IDENTIFIER".equals(name)
                            || "ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER".equals(name)
                            || "ACTION_ACTION_JAVA_VARIABLE".equals(name)) {

                        packageTarget = getPackageValue("package.actions", packageBase);

                    } else if ("FILTER_ACTION".equals(name)
                            || "FILTER_ACTION_BEGIN".equals(name)
                            || "FILTER_SEPARATOR".equals(name)) {
                        packageTarget = getPackageValue("package.filters", packageBase);

                    } else if ("ERROR_ACTION_JAVA".equals(name)
                            || "ERROR_SEPARATOR".equals(name)
                            || "ERROR_ACTION_JAVA_BEGIN".equals(name)) {
                        packageTarget = getPackageValue("package.errors", packageBase);

//                    } else if ("EXTENSION_FILE".equals(name)) {
//                        packageTarget = "";
//                        isJavaFile = false;

//                    } else if ("ACTION_ACTION_VIEW_VALUE".equals(name)
//                            || "ACTION_ACTION_VIEW_VARIABLE".equals(name)
//                            || "ERROR_ACTION_VALUE".equals(name)) {
//                        packageTarget = getPackageValue("package.views", null);
//                        isJavaFile = false;

                    } else if ("EXCEPTION".equals(name)
                            || "ERROR_END".equals(name)
                            || "SECTION_ERRORS_NAME".equals(name)
                            || "SECTION_CONFIG_NEXT_ERRORS".equals(name)
                            || "SECTION_ACTIONS_NEXT_ERRORS".equals(name)
                            || "SECTION_ERRORS_NEXT_ERRORS".equals(name)
                            || "SECTION_FILTERS_NEXT_ERRORS".equals(name)
                            || "SECTION_EXTENSIONS_NEXT_ERRORS".equals(name)
                            || "SECTION_PROPERTIES_NEXT_ERRORS".equals(name)) {
                        packageTarget = "";
                    }
                
                    filter = packageTarget + filter;
                    
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
                        if (!binaryName.equals("") && binaryName.startsWith(filter)) {

                            String value = binaryName.replaceFirst("^" + packageTarget, "");
                            completionResultSet.addItem(new KeywordCompletionItem(value, startOffset, caretOffset));
                        }
                    }
                    
                    // Method
                    if (filter.endsWith(".")) {
                        String className = filter.substring(0, filter.length() - 1).replaceAll("\\.", "/");
                        
                        GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                        FileObject file = registry.findResource(className + ".java");
                        if (file != null) {
                            try {
                                JavaSource javaSource = JavaSource.create(info, file);
                                javaSource.runUserActionTask(new CancellableTask<CompilationController>() {
                                    @Override
                                    public void cancel() {
                                    }

                                    @Override
                                    public void run(CompilationController cu) throws Exception {
                                        cu.toPhase(JavaSource.Phase.PARSED);
                                        CompilationUnitTree tree = cu.getCompilationUnit();
                                        JavaElementFoldVisitor visitor = new JavaElementFoldVisitor();
                                        visitor.scan(tree, null);
                                    }
                                }, false);
                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                            
                        }
                    }
                    
                }
                
                completionResultSet.finish();
            }
        }, component);
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
                if (charAt != ' ' || charAt != ':') {
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
            if (Character.isWhitespace(c) || c == ':') {
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
        return 0;
    }
    
    public static class KeywordCompletionItem implements CompletionItem {

        private String text;
        private static ImageIcon fieldIcon = new ImageIcon(ImageUtilities.loadImage("org/debux/webmotion/netbeans/icon.png"));
        private static Color fieldColor = Color.decode("0x0000B2");
        private int caretOffset;
        private int dotOffset;

        public KeywordCompletionItem(String text, int dotOffset, int caretOffset) {
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
                    completionResultSet.setDocumentation(new KeywordCompletionDocumentation(KeywordCompletionItem.this));
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

        private KeywordCompletionItem item;

        public KeywordCompletionDocumentation(KeywordCompletionItem item) {
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
    
   
    private class JavaElementFoldVisitor extends CancellableTreePathScanner<Object, Object> {
        @Override
        public Object visitMethod(MethodTree node, Object p) {
            System.out.println(node.getName());
            return super.visitMethod(node, p);
        }
    }
    
}
