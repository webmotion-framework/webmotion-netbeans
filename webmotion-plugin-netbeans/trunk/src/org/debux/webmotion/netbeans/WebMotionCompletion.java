package org.debux.webmotion.netbeans;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import org.openide.util.NbBundle;
import static org.debux.webmotion.netbeans.WebMotionLanguage.MIME_TYPE;

/**
 *
 * @author julien
 */
@MimeRegistration(mimeType = MIME_TYPE, service = CompletionProvider.class)
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
            
    public static final String[] KEYWORDS_METHOD = {
        "GET", "POST", "PUT", "DELETE", "HEAD"
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
        "*", "code:"
    };
            
    public static final String[] KEYWORDS_ERROR_CODE = {
        "400", "401", "403", "404", "408", "500"
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
                int column = 0;
                
                try {
                    // Get filter
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
                    
                    // Get position
                    Element lineElement = bDoc.getParagraphElement(caretOffset);
                    String lineValue = bDoc.getText(lineElement.getStartOffset(), caretOffset - lineElement.getStartOffset());
                    
                    Pattern pattern = Pattern.compile("\\s+");
                    Matcher matcher = pattern.matcher(lineValue);
                    while (matcher.find()) {
                        column ++;
                    }
                    
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
        
                // Get section
                TokenHierarchy<Document> hi = TokenHierarchy.get(document);
                TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
                
                ts.move(caretOffset);
                ts.moveNext();
                
                String sectionName = null;
                
                do {
                    
                    Token<WebMotionTokenId> tok = ts.token();
                    WebMotionTokenId id = tok.id();
                    String name = id.name();

                    if (name.startsWith("SECTION_")) {
                        sectionName = tok.text().toString();
                    }
                } while(ts.movePrevious() && sectionName == null);
                
                // Get the package in configuration
                String packageBase = getPackageValue("package.base", null);
                String packageTarget = null;
                boolean packageTarget4View = false;
                String[] keywords = {};
                if (sectionName != null) {
                    
                    if (sectionName.startsWith("[config]")) {
                        keywords = KEYWORDS_CONFIG;
                        
                    } else if (sectionName.startsWith("[errors]") && column % 2 == 0) {
                        keywords = KEYWORDS_ERROR;
                        packageTarget = "";
                        
                    } else if (sectionName.startsWith("[errors]") && column % 2 == 1) {
                        keywords = KEYWORDS_ERROR_ACTION;
                        
                        if (filter.startsWith("view:")) {
                            packageTarget = getPackageValue("package.views", null);
                            packageTarget4View = true;
                            
                        } else if (filter.startsWith("action:") || !filter.contains(":")) {
                            packageTarget = getPackageValue("package.errors", packageBase);
                        }
                        
                    } else if (sectionName.startsWith("[extensions]") && column % 2 == 0) {
                        keywords = KEYWORDS_EXTENSION;
                        
                    } else if (sectionName.startsWith("[extensions]") && column % 2 == 1) {
                        packageTarget = "";
                        packageTarget4View = true;
                        
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
                        
                        if (filter.startsWith("view:")) {
                            packageTarget = getPackageValue("package.views", null);
                            packageTarget4View = true;

                        } else if (filter.startsWith("action:") || !filter.contains(":")) {
                            packageTarget = getPackageValue("package.actions", packageBase);
                        }
                        
                    } else if (sectionName.contains("properties]")) {
                        keywords = KEYWORDS_SECTIONS;
                    }
                    
                } else {
                    keywords = KEYWORDS_SECTIONS;
                }
                
                // Keywords
                if (keywords == KEYWORDS_METHODS && filter.contains(",")) {
                    keywords = KEYWORDS_METHOD;
                    startOffset += StringUtils.substringBeforeLast(filter, ",").length();
                    filter = StringUtils.substringAfterLast(filter, ",");
                    
                } else if (keywords == KEYWORDS_ERROR && filter.contains("code:")) {
                    keywords = KEYWORDS_ERROR_CODE;
                    startOffset += "code:".length();
                    filter = filter.substring("code:".length());
                    packageTarget = null;
                }
                
                for (String keyword : keywords) {
                    if (keyword.startsWith(filter)) {
                        completionResultSet.addItem(new WebMotionCompletionItem(keyword, startOffset, caretOffset));
                    }
                }
                
                if (packageTarget != null) {
                    
                    // File
                    if (packageTarget4View) {
                        String path = packageTarget.replaceAll("\\.", "/");
                        String filterFile = filter;
                        int startOffsetFile = startOffset;

                        if (filter.startsWith("view:")) {
                            filterFile = filter.replaceFirst("view:", "");
                            startOffsetFile += "view:".length();
                        }

                        if (filterFile.contains("/")) {
                            path += StringUtils.substringBeforeLast(filterFile, "/");
                            filterFile = StringUtils.substringAfterLast(filterFile, "/");
                            startOffsetFile += path.length() + 1;
                        }

                        GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                        List<ClassPath> paths = new ArrayList<ClassPath>();
                        paths.addAll(registry.getPaths(ClassPath.BOOT));
                        paths.addAll(registry.getPaths(ClassPath.COMPILE));
                        paths.addAll(registry.getPaths(ClassPath.SOURCE));

                        for (ClassPath classPath : paths) {
                            FileObject resource = classPath.findResource(path);

                            if (resource != null) {
                                FileObject[] children = resource.getChildren();
                                for (FileObject child : children) {
                                    String nameExt = child.getNameExt();
                                    if (child.isFolder()) {
                                        nameExt += "/";
                                    }

                                    if (nameExt.startsWith(filterFile)) {
                                        completionResultSet.addItem(new WebMotionCompletionItem(nameExt, startOffsetFile, caretOffset));
                                    }
                                }
                            }
                        }
                        
                        completionResultSet.finish();
                        
                    } else {
                        
                        // Class
                        String filterClass = packageTarget + filter;
                        int startOffsetClass = startOffset;

                        if (filter.contains(":") && !filter.startsWith("code:")) {
                            filterClass = packageTarget + StringUtils.substringAfter(filter, ":");
                            startOffsetClass += StringUtils.substringBefore(filter, ":").length() + 1;
                        }

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
                                completionResultSet.addItem(new WebMotionCompletionItem(value, startOffsetClass, caretOffset));
                            }
                        }

                        // Method
                        String className = StringUtils.substringBeforeLast(filterClass, ".").replaceAll("\\.", "/");
                        final String filterMethod = StringUtils.substringAfterLast(filter, ".");

                        JavaSource javaSource = null;
                        final CompletionResultSet completionResultSetJavaSource = completionResultSet;
                        final int startOffsetJavaSource = startOffsetClass + StringUtils.substringBeforeLast(filter, ".").length() + 1;
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
        return 0;
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
            try {
                return NbBundle.getMessage(KeywordCompletionDocumentation.class, "completion_" + item.text);
            } catch (MissingResourceException ex) {
                return "Information about " + item.text;
            }
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
