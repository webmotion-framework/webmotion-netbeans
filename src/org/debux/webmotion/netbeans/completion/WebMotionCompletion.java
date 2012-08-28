/*
 * #%L
 * WebMotion plugin netbeans
 * 
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2012 Debux
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.debux.webmotion.netbeans.completion;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.Utils;
import org.debux.webmotion.netbeans.WebMotionLanguage;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.api.java.source.CancellableTask;
import org.netbeans.api.java.source.ClasspathInfo;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import static org.debux.webmotion.netbeans.WebMotionLanguage.MIME_TYPE;
import org.debux.webmotion.netbeans.javacc.lexer.impl.LexerUtils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId.Section;

/**
 *
 * @author julien
 */
@MimeRegistration(mimeType = MIME_TYPE, service = CompletionProvider.class)
public class WebMotionCompletion implements CompletionProvider {

    public static final String[] KEYWORDS_EMPTY = {
    };
    
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
            
    public static final String[] KEYWORDS_CONFIG_EQUAL = {
        "="
    };
    
    public static final String[] KEYWORDS_CONFIG_BOOLEAN = {
        "true", "false"
    };
            
    public static final String[] KEYWORDS_CONFIG_ERROR = {
        "enabled", "disabled", "forced"
    };
            
    public static final String[] KEYWORDS_CONFIG_SCOPE = {
        "singleton", "request", "session"
    };
            
    public static final String[] KEYWORDS_CONFIG_ENCODING = Charset.availableCharsets().keySet().toArray(new String[]{});
            
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
                    StyledDocument doc = (StyledDocument) document;
                    int lineStartOffset = Utils.getRowFirstNonWhite(doc, caretOffset);
                    char[] line = doc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
                    int whiteOffset = Utils.indexOfWhite(line);
                    filter = new String(line, whiteOffset + 1, line.length - whiteOffset - 1);
                    if (whiteOffset > 0) {
                        startOffset = lineStartOffset + whiteOffset + 1;
                    } else {
                        startOffset = lineStartOffset;
                    }
                    
                    // Get position
                    Element lineElement = doc.getParagraphElement(caretOffset);
                    String lineValue = doc.getText(lineElement.getStartOffset(), caretOffset - lineElement.getStartOffset());
                    
                    Pattern pattern = Pattern.compile("\\s+");
                    Matcher matcher = pattern.matcher(lineValue);
                    while (matcher.find()) {
                        column ++;
                    }
                    
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
        
                // Get section
                Section section = LexerUtils.getSection(document, caretOffset);
                
                // Get the package in configuration
                String packageBase = Utils.getPackageValue("package.base", null);
                String packageTarget = null;
                String filterSuperClass = null;
                String separator = "/";
                boolean onlyFolder = false;
                String onlyMimeType = null;
                
                String[] keywords = {};
                if (section != null) {
                    
                    if (section == Section.CONFIG) {
                        keywords = KEYWORDS_CONFIG;
                        
                    } else if (section == Section.ERRORS && column % 2 == 0) {
                        keywords = KEYWORDS_ERROR;
                        packageTarget = "";
                        filterSuperClass = "java.lang.Exception";
                        
                    } else if (section == Section.ERRORS && column % 2 == 1) {
                        keywords = KEYWORDS_ERROR_ACTION;
                        
                        if (filter.startsWith("view:")) {
                            packageTarget = Utils.getPackageValue("package.views", null);
                            
                        } else if (filter.startsWith("action:") || !filter.contains(":")) {
                            packageTarget = Utils.getPackageValue("package.errors", packageBase);
                            filterSuperClass = "org.debux.webmotion.server.WebMotionController";
                        }
                        
                    } else if (section == Section.EXTENSIONS && column % 2 == 0) {
                        keywords = KEYWORDS_EXTENSION;
                        
                    } else if (section == Section.EXTENSIONS && column % 2 == 1) {
                        packageTarget = "";
                        onlyMimeType = WebMotionLanguage.MIME_TYPE;
                        
                    } else if (section == Section.FILTERS && column % 3 == 0) {
                        keywords = KEYWORDS_METHODS;
                        
                    } else if (section == Section.FILTERS && column % 3 == 1) {
                        keywords = KEYWORDS_FILTER;
                        
                    } else if (section == Section.FILTERS && column % 3 == 2) {
                        keywords = KEYWORDS_FILTER_ACTION;
                        packageTarget = Utils.getPackageValue("package.filters", packageBase);
                        filterSuperClass = "org.debux.webmotion.server.WebMotionFilter";
                        
                    } else if (section == Section.ACTIONS && column % 3 == 0) {
                        keywords = KEYWORDS_METHODS;
                        
                    } else if (section == Section.ACTIONS && column % 3 == 1) {
                        keywords = KEYWORDS_ACTION;
                        
                    } else if (section == Section.ACTIONS && column % 3 == 2) {
                        keywords = KEYWORDS_ACTION_ACTION;
                        
                        if (filter.startsWith("view:")) {
                            packageTarget = Utils.getPackageValue("package.views", null);

                        } else if (filter.startsWith("action:") || !filter.contains(":")) {
                            packageTarget = Utils.getPackageValue("package.actions", packageBase);
                            filterSuperClass = "org.debux.webmotion.server.WebMotionController";
                        }
                        
                    } else if (section == Section.PROPERTIES) {
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
                    
                } else if (filter.startsWith("javac.debug=") || filter.startsWith("server.async=") || filter.startsWith("server.static.autodetect=")) {
                    keywords = KEYWORDS_CONFIG_BOOLEAN;
                    startOffset += StringUtils.substringBefore(filter, "=").length() + 1;
                    filter = StringUtils.substringAfter(filter, "=");
                    
                } else if (filter.startsWith("server.error.page=")) {
                    keywords = KEYWORDS_CONFIG_ERROR;
                    startOffset += StringUtils.substringBefore(filter, "=").length() + 1;
                    filter = StringUtils.substringAfter(filter, "=");
                    
                } else if (filter.startsWith("server.controller.scope=")) {
                    keywords = KEYWORDS_CONFIG_SCOPE;
                    startOffset += StringUtils.substringBefore(filter, "=").length() + 1;
                    filter = StringUtils.substringAfter(filter, "=");
                    
                } else if (filter.startsWith("package.base=")) {
                    keywords = KEYWORDS_EMPTY;
                    startOffset += StringUtils.substringBefore(filter, "=").length() + 1;
                    filter = StringUtils.substringAfter(filter, "=").replaceAll("\\.", "/");
                    
                    packageTarget = "";
                    filterSuperClass = null;
                    separator = ".";
                    onlyFolder = true;
                    
                } else if (filter.startsWith("package.views=")) {
                    keywords = KEYWORDS_EMPTY;
                    startOffset += StringUtils.substringBefore(filter, "=").length() + 1;
                    filter = StringUtils.substringAfter(filter, "=").replaceAll("\\.", "/");
                    
                    packageTarget = "";
                    filterSuperClass = null;
                    separator = "/";
                    onlyFolder = true;
                    
                } else if (filter.startsWith("server.listener.class=")) {
                    keywords = KEYWORDS_EMPTY;
                    startOffset += StringUtils.substringBefore(filter, "=").length() + 1;
                    filter = StringUtils.substringAfter(filter, "=");
                    
                    packageTarget = "";
                    filterSuperClass = "org.debux.webmotion.server.WebMotionServerListener";
                    
                } else if (filter.startsWith("server.main.handler.class=")) {
                    keywords = KEYWORDS_EMPTY;
                    startOffset += StringUtils.substringBefore(filter, "=").length() + 1;
                    filter = StringUtils.substringAfter(filter, "=");
                    
                    packageTarget = "";
                    filterSuperClass = "org.debux.webmotion.server.WebMotionHandler";
                    
                } else if (filter.startsWith("package.actions=") || filter.startsWith("package.filters=") || filter.startsWith("package.errors=")) {
                    keywords = KEYWORDS_EMPTY;
                    startOffset += StringUtils.substringBefore(filter, "=").length() + 1;
                    filter = StringUtils.substringAfter(filter, "=");
                    
                    packageTarget = Utils.getPackageValue("package.base", null);
                    filterSuperClass = null;
                    separator = ".";
                    onlyFolder = true;
                    
                } else if (filter.startsWith("server.encoding=")) {
                    keywords = KEYWORDS_CONFIG_ENCODING;
                    startOffset += StringUtils.substringBefore(filter, "=").length() + 1;
                    filter = StringUtils.substringAfter(filter, "=");
                    
                } else if (filter.startsWith("server.secret=")) {
                    keywords = new String[] {RandomStringUtils.random(31, true, true)};
                    startOffset += StringUtils.substringBefore(filter, "=").length() + 1;
                    filter = StringUtils.substringAfter(filter, "=");
                    
                } else if (filter.startsWith("javac.debug") || filter.startsWith("server.async") || filter.startsWith("server.static.autodetect")
                            || filter.startsWith("server.error.page")
                            || filter.startsWith("server.controller.scope")
                            || filter.startsWith("package.base") 
                            || filter.startsWith("package.views")
                            || filter.startsWith("server.listener.class")
                            || filter.startsWith("server.main.handler.class")
                            || filter.startsWith("package.actions") || filter.startsWith("package.filters") || filter.startsWith("package.errors")
                            || filter.startsWith("server.encoding")
                            || filter.startsWith("server.secret")) {
                    
                    keywords = KEYWORDS_CONFIG_EQUAL;
                    startOffset += StringUtils.substringBefore(filter, "=").length();
                    filter = "";
                }
                
                for (String keyword : keywords) {
                    if (keyword.startsWith(filter)) {
                        completionResultSet.addItem(new WebMotionCompletionItem(keyword, startOffset, caretOffset));
                    }
                }
                
                if (packageTarget != null) {
                    
                    // File
                    if (filterSuperClass == null) {
                        String path = packageTarget.replaceAll("\\.", "/");
                        String filterFile = filter;
                        int startOffsetFile = startOffset;

                        if (filter.startsWith("view:")) {
                            filterFile = filter.replaceFirst("view:", "");
                            startOffsetFile += "view:".length();
                        }

                        if (filterFile.contains("/")) {
                            String current = StringUtils.substringBeforeLast(filterFile, "/");
                            path += current;
                            startOffsetFile += current.length() + 1;
                            filterFile = StringUtils.substringAfterLast(filterFile, "/");
                            
                        } else if (packageTarget.isEmpty() && separator.equals("/")) {
                            String fileName = "WEB-INF";
                            if (fileName.startsWith(filterFile)) {
                                if (!fileName.equals(filterFile)) {
                                    completionResultSet.addItem(new WebMotionCompletionItem(fileName, startOffsetFile, caretOffset));
                                } else {
                                    completionResultSet.addItem(new WebMotionCompletionItem(separator, startOffsetFile + fileName.length(), caretOffset));
                                }
                            }
                        }

                        GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                        List<ClassPath> paths = new ArrayList<ClassPath>();
                        paths.addAll(registry.getPaths(ClassPath.BOOT));
                        paths.addAll(registry.getPaths(ClassPath.COMPILE));
                        paths.addAll(registry.getPaths(ClassPath.SOURCE));

                        Set<String> names = new HashSet<String>();
                        for (ClassPath classPath : paths) {
                            FileObject resource = classPath.findResource(path);

                            if (resource != null) {
                                FileObject[] children = resource.getChildren();
                                for (FileObject child : children) {
                                    String fileName = child.getNameExt();

                                    if ((!onlyFolder || onlyFolder && child.isFolder())
                                        && (onlyMimeType == null || child.isFolder() || onlyMimeType != null && child.getMIMEType().equals(onlyMimeType))
                                        && fileName.startsWith(filterFile) && !fileName.startsWith(".")) {
                                        
                                        if (!names.contains(fileName)) {
                                            if (!fileName.equals(filterFile)) {
                                                completionResultSet.addItem(new WebMotionCompletionItem(fileName, startOffsetFile, caretOffset));
                                            } else {
                                                completionResultSet.addItem(new WebMotionCompletionItem(separator, startOffsetFile + fileName.length(), caretOffset));
                                            }
                                            names.add(fileName);
                                        }
                                    }
                                }
                            }
                        }
                        completionResultSet.finish();
                        
                    } else {
                        
                        // Class
                        if (filter.contains(":") && !filter.startsWith("code:")) {
                            startOffset += StringUtils.substringBefore(filter, ":").length() + 1;
                            filter = StringUtils.substringAfter(filter, ":");
                        }
                        
                        int startOffsetClass = startOffset;
                        if (filter.contains(".")) {
                            startOffsetClass += StringUtils.substringBeforeLast(filter, ".").length() + 1;
                        }
                        
                        String fullName = packageTarget + filter;
                        String rootPackage = "";
                        String filerPackage = fullName;
                        if (fullName.contains(".")) {
                            rootPackage = StringUtils.substringBeforeLast(fullName, ".");
                            filerPackage = StringUtils.substringAfterLast(fullName, ".");
                        }
                        
                        String folderPackage = rootPackage.replaceAll("\\.", "/");

                        FileObject fo = Utils.getFO(document);
                        ClassPath bootCp = ClassPath.getClassPath(fo, ClassPath.BOOT);
                        ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);
                        ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);
                        ClasspathInfo info = ClasspathInfo.create(bootCp, compileCp, sourcePath);
                        JavaSource src = JavaSource.create(info);
                        
                        final String filterSuperJavaClass = filterSuperClass;
                        final CompletionResultSet completionResultSetClass = completionResultSet;
                        final int startOffsetJavaClass = startOffsetClass;
                        final int caretOffsetClass = caretOffset;
                        
                        // Package names
                        List<FileObject> folders = new ArrayList<FileObject>();
                        folders.addAll(bootCp.findAllResources(folderPackage));
                        folders.addAll(compileCp.findAllResources(folderPackage));
                        folders.addAll(sourcePath.findAllResources(folderPackage));
                        
                        final String rootPackageFilter = rootPackage;
                        final String filterPackageFilter = filerPackage;
                        final Set<String> names = new HashSet<String>();
                        for (final FileObject folder : folders) {
                            
                            try {
                                src.runUserActionTask(new CancellableTask<CompilationController>() {
                                    @Override
                                    public void cancel() {
                                    }

                                    @Override
                                    public void run(CompilationController cu) throws Exception {
                                        Types types = cu.getTypes();
                                        Elements elements = cu.getElements();

                                        TypeElement superElement = elements.getTypeElement(filterSuperJavaClass);
                                        TypeMirror superType = null;
                                        if (superElement != null) {
                                            superType = superElement.asType();
                                        }
                                        
                                        FileObject[] children = folder.getChildren();
                                        for (FileObject child : children) {
                                            String name = child.getName();
                                            if (!name.startsWith(".") && name.startsWith(filterPackageFilter)) {

                                                if (!names.contains(name)) {
                                                    names.add(name);
                                                    
                                                    TypeElement element;
                                                    if (rootPackageFilter.isEmpty()) {
                                                        element = elements.getTypeElement(name);
                                                    } else {
                                                        element = elements.getTypeElement(rootPackageFilter + "." + name);
                                                    }

                                                    if (name.equals(filterPackageFilter)) {
                                                        
                                                        if (element == null 
                                                                || element.getKind() != ElementKind.CLASS 
                                                                || element.getKind() == ElementKind.CLASS 
                                                                    && (filterSuperJavaClass.equals("org.debux.webmotion.server.WebMotionFilter")
                                                                     || filterSuperJavaClass.equals("org.debux.webmotion.server.WebMotionController"))) {
                                                            
                                                            WebMotionCompletionItem item = new WebMotionCompletionItem(".", startOffsetJavaClass + name.length(), caretOffsetClass);
                                                            completionResultSetClass.addItem(item);
                                                        }
                                                        
                                                        
                                                    } else if (child.isFolder()) {
                                                        WebMotionCompletionItem item = new WebMotionCompletionItem(name, startOffsetJavaClass, caretOffsetClass);
                                                        completionResultSetClass.addItem(item);

                                                    } else if (element != null) {
                                                        Set<Modifier> modifiers = element.getModifiers();
                                                        ElementKind kind = element.getKind();
                                                        TypeMirror resolveType = element.asType();

                                                        if (kind == ElementKind.CLASS
                                                                && modifiers.contains(Modifier.PUBLIC)
                                                                && !modifiers.contains(Modifier.ABSTRACT)
                                                                && superType != null && types.isSubtype(resolveType, superType)) {

                                                            WebMotionCompletionItem item = new WebMotionCompletionItem(name, cu, element, startOffsetJavaClass, caretOffsetClass);
                                                            completionResultSetClass.addItem(item);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }, false);

                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
                        
                        // Method
                        if (filter.contains(".") && 
                                (filterSuperJavaClass.equals("org.debux.webmotion.server.WebMotionFilter")
                                    || filterSuperJavaClass.equals("org.debux.webmotion.server.WebMotionController"))) {
                            
                            final String className = StringUtils.substringBeforeLast(filter, ".");
                            final String fullClassName = packageTarget + className;
                            final String filterMethod = StringUtils.substringAfterLast(filter, ".");

                            final CompletionResultSet completionResultSetJavaSource = completionResultSet;
                            final int startOffsetJavaSource = startOffset + className.length() + 1;
                            final int caretOffesetJavaSource = caretOffset;

                            try {
                                src.runUserActionTask(new CancellableTask<CompilationController>() {

                                    @Override
                                    public void cancel() {
                                    }

                                    @Override
                                    public void run(CompilationController cu) throws Exception {
                                        cu.toPhase(JavaSource.Phase.PARSED);

                                        Elements elements = cu.getElements();
                                        TypeElement classElement = elements.getTypeElement(fullClassName);
                                        if (classElement != null) {

                                            List<? extends javax.lang.model.element.Element> members = elements.getAllMembers(classElement);
                                            for (javax.lang.model.element.Element member : members) {
                                                if (member.getKind() == ElementKind.METHOD) {

                                                    Set<Modifier> modifiers = member.getModifiers();
                                                    String methodName = member.getSimpleName().toString();
                                                    String className = member.getEnclosingElement().getSimpleName().toString();

                                                    if (!"Object".equals(className)
                                                        && !"WebMotionController".equals(className)
                                                        && !"WebMotionFilter".equals(className)
                                                        &&  modifiers.contains(Modifier.PUBLIC)
                                                        && !modifiers.contains(Modifier.STATIC)
                                                        && methodName.startsWith(filterMethod)) {

                                                        WebMotionCompletionItem item = new WebMotionCompletionItem(methodName, cu, member, startOffsetJavaSource, caretOffesetJavaSource);
                                                        completionResultSetJavaSource.addItem(item);
                                                    }
                                                }
                                            }
                                        }

                                        completionResultSetJavaSource.finish();
                                    }
                                }, false);
                            } catch (IOException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        } else {
                            completionResultSet.finish();
                        }
                    }
                    
                } else {
                    completionResultSet.finish();
                }
            }
        }, component);
    }
    
    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        return 0;
    }

}
