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
package org.debux.webmotion.netbeans;

import java.io.IOException;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
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
import org.debux.webmotion.netbeans.javacc.lexer.impl.LexerUtils;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.*;
import org.netbeans.modules.csl.api.OffsetRange;

/**
 *
 * @author julien
 */
@MimeRegistration(mimeType = MIME_TYPE, service = HyperlinkProvider.class)
public class WebMotionHyperlink implements HyperlinkProvider {

    @Override
    public boolean isHyperlinkPoint(Document document, int offset) {
        OffsetRange range = LexerUtils.getTokens(document, offset, getVerifyToken());
        return range != null;
    }

    @Override
    public int[] getHyperlinkSpan(Document document, int offset) {
        OffsetRange range = LexerUtils.getTokens(document, offset, getVerifyToken());
        if (range != null) {
            int start = range.getStart();
            int end = range.getEnd();
            return new int[]{start, end};
        }
        return null;
    }

    @Override
    public void performClickAction(Document document, int offset) {
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        if (ts != null) {
            ts.move(offset);
            ts.moveNext();

            // Get the package in configuration
            String packageBase = Utils.getPackageValue("package.base", null);
            String packageTarget = null;
            boolean isJavaFile = true;
            
            Token<WebMotionTokenId> tok = ts.token();
            WebMotionTokenId id = tok.id();
            String name = id.name();
            if ("ACTION_ACTION_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER".equals(name) ||
                "ACTION_ACTION_JAVA_VARIABLE".equals(name)) {
                
                packageTarget = Utils.getPackageValue("package.actions", packageBase);
                
            } else if ("FILTER_ACTION".equals(name)) {
                packageTarget = Utils.getPackageValue("package.filters", packageBase);
                
            } else if ("ERROR_ACTION_JAVA".equals(name)) {
                packageTarget = Utils.getPackageValue("package.errors", packageBase);
                
            } else if ("EXTENSION_FILE".equals(name)) {
                packageTarget = "";
                isJavaFile = false;
                
            } else if ("ACTION_ACTION_VIEW_VALUE".equals(name) ||
                       "ACTION_ACTION_VIEW_VARIABLE".equals(name) ||
                       "ERROR_ACTION_VALUE".equals(name)) {
                packageTarget = Utils.getPackageValue("package.views", null);
                isJavaFile = false;
                
            } else if ("EXCEPTION".equals(name)) {
                packageTarget = "";
            }
            
            // Open document
            OffsetRange range = LexerUtils.getTokens(document, offset, getVerifyToken());
            if (range != null) {
                try {
                    String target = LexerUtils.getText(document, range);

                    if (isJavaFile) {
                        
                        FileObject fo = Utils.getFO(document);
                        ClassPath bootCp = ClassPath.getClassPath(fo, ClassPath.BOOT);
                        ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);
                        ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);

                        final ClasspathInfo info = ClasspathInfo.create(bootCp, compileCp, sourcePath);
                        JavaSource src = JavaSource.create(info);
                        
                        final String fullClassName = target.replaceAll("/+", ".");
                        final String packageClassName = packageTarget.replaceAll("/+", ".");
                        final String className = StringUtils.substringBeforeLast(target, ".");
                        final String mark = StringUtils.substringAfterLast(target, ".");

                        try {
                            src.runUserActionTask(new CancellableTask<CompilationController>() {
                                @Override
                                public void cancel() {
                                }

                                @Override
                                public void run(CompilationController cu) throws Exception {
                                    Elements elements = cu.getElements();
                                    
                                    TypeElement classElement = elements.getTypeElement(packageClassName + className);
                                    if (classElement == null) {
                                        classElement = elements.getTypeElement(fullClassName);
                                    }
                                    
                                    if (classElement != null) {
                                        ElementHandle<TypeElement> create = ElementHandle.create(classElement);
                                        FileObject fo = SourceUtils.getFile(create, info);
                                        open(fo, mark);
                                    }
                                }
                            }, false);

                        } catch (IOException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                                                
                    } else {
                        packageTarget = packageTarget.replaceAll("\\.+", "/");
                        GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                        FileObject fo = registry.findResource(packageTarget + target);
                        open(fo, null);
                    }
                    
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
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
    
    public String[] getVerifyToken(){
        return new String[] {
                "ACTION_ACTION_IDENTIFIER",
                "ACTION_ACTION_JAVA_IDENTIFIER",
                "ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER",
                "ACTION_ACTION_JAVA_VARIABLE",
                
                "ACTION_ACTION_VIEW_VALUE",
                "ACTION_ACTION_VIEW_VARIABLE",
                
                "FILTER_ACTION",
                
                "EXTENSION_FILE",
                
                "ERROR_ACTION_JAVA",
                "EXCEPTION",
                "ERROR_ACTION_VALUE"
        };
    }

}
