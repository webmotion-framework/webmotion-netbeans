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
package org.debux.webmotion.netbeans.refactoring;

import com.sun.source.tree.Tree.Kind;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.Utils;
import org.debux.webmotion.netbeans.refactoring.WebMotionRefactoringActions.RefactoringContext;
import org.debux.webmotion.netbeans.javacc.lexer.impl.LexerUtils;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.api.java.source.*;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.api.RefactoringSession;
import org.netbeans.modules.refactoring.api.RenameRefactoring;
import org.netbeans.modules.refactoring.spi.RefactoringElementsBag;
import org.netbeans.modules.refactoring.spi.RefactoringPlugin;
import org.netbeans.modules.refactoring.spi.SimpleRefactoringElementImplementation;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.text.*;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author julien
 */
class WebMotionRenameRefactoringPlugin implements RefactoringPlugin {

    protected RenameRefactoring refactoring;
    
    public WebMotionRenameRefactoringPlugin(RenameRefactoring refactoring) {
        this.refactoring = refactoring;
    }

    @Override
    public Problem preCheck() {
        return null;
    }

    @Override
    public Problem checkParameters() {
        String newName = refactoring.getNewName();
        if (newName.length() == 0) {
            return new Problem(true, "The element name cannot be empty.");
        }
        return null;
    }

    @Override
    public Problem fastCheckParameters() {
        return checkParameters();
    }

    @Override
    public void cancelRequest() {
    }

    @Override
    public Problem prepare(final RefactoringElementsBag refactoringElements) {
        Lookup source = refactoring.getRefactoringSource();
        TreePathHandle treePathHandle = source.lookup(TreePathHandle.class);
        
        if (treePathHandle != null && (
                TreeUtilities.CLASS_TREE_KINDS.contains(treePathHandle.getKind())
                || Kind.METHOD == treePathHandle.getKind())) {
            
            Set<FileObject> findAllMappings = Utils.findAllMappings();
            for (FileObject mapping : findAllMappings) {
                prepare(refactoringElements, treePathHandle, mapping);
            }
        }
        
        RefactoringContext refactoringContext = source.lookup(RefactoringContext.class);
        if (refactoringContext != null) {
            
            Document document = refactoringContext.getDocument();
            int caret = refactoringContext.getCaret();
            prepare(refactoringElements, refactoringContext, document, caret);
            
            Set<FileObject> findAllMappings = Utils.findAllMappings();
            for (FileObject mapping : findAllMappings) {
                prepare(refactoringElements, refactoringContext, mapping);
            }
        }
        
        return null;
    }

    protected void prepare(final RefactoringElementsBag refactoringElements,
            final RefactoringContext refactoringContext,
            Document document, int offset) {
        
        final String name = refactoring.getNewName();
        String packageTarget = Utils.getPackage(document, offset);
        
        String target = refactoringContext.getValue();
        if (target != null) {
            if (LexerUtils.isJavaToken(document, offset)) {

                FileObject srcFile = Utils.getFO(document);
                ClassPath bootCp = ClassPath.getClassPath(srcFile, ClassPath.BOOT);
                ClassPath compileCp = ClassPath.getClassPath(srcFile, ClassPath.COMPILE);
                ClassPath sourcePath = ClassPath.getClassPath(srcFile, ClassPath.SOURCE);

                final ClasspathInfo info = ClasspathInfo.create(bootCp, compileCp, sourcePath);
                JavaSource src = JavaSource.create(info);

                final String fullClassName = target.replaceAll("/+", ".");
                final String packageClassName = packageTarget.replaceAll("/+", ".");
                final String className = StringUtils.substringBeforeLast(target, ".");
                final String methodName = StringUtils.substringAfterLast(target, ".");

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

                                RenameRefactoring delegate = new RenameRefactoring(Lookups.singleton(fo));
                                delegate.setNewName(StringUtils.substringBeforeLast(name, "."));
                                RefactoringSession currentSession = refactoringElements.getSession();
                                delegate.prepare(currentSession);
                                
                                List<? extends Element> members = classElement.getEnclosedElements();
                                for (Element element : members) {
                                    if (element.getKind() == ElementKind.METHOD &&
                                            element.getSimpleName().toString().equals(methodName)) {
                                        
                                        TreePathHandle treePathHandle = TreePathHandle.create(element, cu);
                                        
                                        delegate = new RenameRefactoring(Lookups.singleton(treePathHandle));
                                        delegate.setNewName(StringUtils.substringAfterLast(name, "."));
                                        currentSession = refactoringElements.getSession();
                                        delegate.prepare(currentSession);
                                        
                                        break;
                                    }
                                }
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

                refactoringElements.add(refactoring, new RenameFileRefactoringElement(fo, name));
            }
        }
    }
    
    protected void prepare(final RefactoringElementsBag refactoringElements,
            final RefactoringContext refactoringContext, final FileObject fo) throws IllegalArgumentException {
        try {
            String name = refactoringContext.getValue();
            String beginName = StringUtils.substringBeforeLast(name, ".");
            String endName = StringUtils.substringAfterLast(name, ".");
                    
            String target = refactoring.getNewName();
            String beginTarget = StringUtils.substringBeforeLast(target, ".");
            String endTarget = StringUtils.substringAfterLast(target, ".");
            
            DataObject dob = DataObject.find(fo);
            EditorCookie editor = dob.getLookup().lookup(EditorCookie.class);
            StyledDocument doc = editor.openDocument();
            
            List<OffsetRange> tokens = LexerUtils.getTokens(doc, Utils.getAccessibleToken());
            for (OffsetRange offsetRange : tokens) {
                String text = LexerUtils.getText(doc, offsetRange);

                if (text.startsWith(beginName)) {
                    int start = offsetRange.getStart();
                    
                    if (text.endsWith(endName)) {
                        int end = start + name.length();
                        refactoringElements.add(refactoring, new MappingRenameRefactoringElement(dob, start, end, target));
                        
                    } else if (!beginTarget.equals(beginName)) {
                        int end = start + beginName.length();
                        refactoringElements.add(refactoring, new MappingRenameRefactoringElement(dob, start, end, beginTarget));
                    }
                }
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
        
    protected void prepare(final RefactoringElementsBag refactoringElements,
            final TreePathHandle treePathHandle, final FileObject fo) throws IllegalArgumentException {
        try {
            
            ClassPath bootCp = ClassPath.getClassPath(fo, ClassPath.BOOT);
            ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);
            ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);
            ClasspathInfo info = ClasspathInfo.create(bootCp, compileCp, sourcePath);
            JavaSource src = JavaSource.create(info);
            
            src.runUserActionTask(new CancellableTask<CompilationController>() {
                @Override
                public void cancel() {
                }

                @Override
                public void run(CompilationController cu) throws Exception {
                    DataObject dob = DataObject.find(fo);
                    EditorCookie editor = dob.getLookup().lookup(EditorCookie.class);
                    StyledDocument doc = editor.openDocument();

                    FileObject fileObject = treePathHandle.getFileObject();
                    String className = fileObject.getName().toString();
            
                    String oldName = className;
                    String filter = className;
                    
                    if (Kind.METHOD == treePathHandle.getKind()) {
                        Element element = treePathHandle.resolveElement(cu);
                        oldName = element.getSimpleName().toString();
                        filter = className + "." + oldName;
                    }
                    
                    List<OffsetRange> tokens = LexerUtils.getTokens(doc, "FILTER_ACTION",
                            "ERROR_ACTION_JAVA", "ACTION_ACTION_IDENTIFIER", "ACTION_ACTION_JAVA_IDENTIFIER",
                            "ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER", "ACTION_ACTION_JAVA_VARIABLE");

                    for (OffsetRange offsetRange : tokens) {
                        String text = LexerUtils.getText(doc, offsetRange);

                        if (text.contains(filter)) {
                            int start = offsetRange.getStart() + text.indexOf(oldName);
                            int end = start + oldName.length();
                            refactoringElements.add(refactoring, new MappingRenameRefactoringElement(dob, start, end, refactoring.getNewName()));
                        }
                    }
                }
            }, false);
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    public class RenameFileRefactoringElement extends SimpleRefactoringElementImplementation {
        
        protected FileObject fo;
        protected String name;

        public RenameFileRefactoringElement(FileObject fo, String name) {
            this.fo = fo;
            this.name = name;
        }

        @Override
        public String getText() {
            return "Rename file " + name;
        }

        @Override
        public String getDisplayText() {
            return "Rename file " + name;
        }

        @Override
        public void performChange() {
            try {
                Utils.renameFileObject(fo, name);
                
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        public Lookup getLookup() {
            return refactoring.getRefactoringSource();
        }

        @Override
        public FileObject getParentFile() {
            return fo;
        }

        @Override
        public PositionBounds getPosition() {
            return null;
        }
        
    }
    
    public class MappingRenameRefactoringElement extends SimpleRefactoringElementImplementation {

        protected DataObject dob;
        protected String value;
        protected PositionBounds bounds;

        public MappingRenameRefactoringElement(DataObject dob, int start, int end, String value) {
            this.dob = dob;
            this.value = value;
            
            CloneableEditorSupport open = (CloneableEditorSupport) dob.getLookup().lookup(EditorCookie.class);
            
            PositionRef startPosition = open.createPositionRef(start, Position.Bias.Forward);
            PositionRef endPosition = open.createPositionRef(end, Position.Bias.Backward);
            bounds = new PositionBounds(startPosition, endPosition);
        }

        @Override
        public void performChange() {
            try {
                bounds.setText(value);
                
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        public String getText() {
            return "Rename in mapping file";
        }

        @Override
        public String getDisplayText() {
            return "Rename in mapping file";
        }

        @Override
        protected String getNewFileContent() {
            InputStream inputStream = null;
            try {
                int begin = bounds.getBegin().getOffset();
                int end = bounds.getEnd().getOffset();
                inputStream = dob.getPrimaryFile().getInputStream();
                String content = IOUtils.toString(inputStream);
                
                return content.substring(0, begin) + value + content.substring(end);
                
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            } finally {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
            return null;
        }

        @Override
        public Lookup getLookup() {
            return refactoring.getRefactoringSource();
        }

        @Override
        public FileObject getParentFile() {
            return dob.getPrimaryFile();
        }

        @Override
        public PositionBounds getPosition() {
            return bounds;
        }
    }
    
}
