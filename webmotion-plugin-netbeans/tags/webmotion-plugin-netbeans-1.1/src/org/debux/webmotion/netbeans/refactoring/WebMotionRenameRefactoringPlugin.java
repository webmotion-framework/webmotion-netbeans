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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;
import org.apache.commons.io.IOUtils;
import org.debux.webmotion.netbeans.Utils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.LexerUtils;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.*;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.refactoring.api.Problem;
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
        
        FileObject fileObject = source.lookup(FileObject.class);
        if (fileObject != null) {
            
            Set<FileObject> findAllMappings = Utils.findAllMappings();
            for (FileObject mapping : findAllMappings) {
                prepare(refactoringElements, fileObject, mapping);
            }
        }
        
        return null;
    }
        
    protected void prepare(final RefactoringElementsBag refactoringElements,
            final TreePathHandle treePathHandle, final FileObject mapping) throws IllegalArgumentException {
        try {
            
            ClassPath bootCp = ClassPath.getClassPath(mapping, ClassPath.BOOT);
            ClassPath compileCp = ClassPath.getClassPath(mapping, ClassPath.COMPILE);
            ClassPath sourcePath = ClassPath.getClassPath(mapping, ClassPath.SOURCE);
            ClasspathInfo info = ClasspathInfo.create(bootCp, compileCp, sourcePath);
            JavaSource src = JavaSource.create(info);
            
            src.runUserActionTask(new CancellableTask<CompilationController>() {
                @Override
                public void cancel() {
                }

                @Override
                public void run(CompilationController cu) throws Exception {
                    DataObject dob = DataObject.find(mapping);
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
    
    protected void prepare(final RefactoringElementsBag refactoringElements,
            final FileObject fileObject, final FileObject mapping) throws IllegalArgumentException {
        try {
                DataObject dob = DataObject.find(mapping);
                EditorCookie editor = dob.getLookup().lookup(EditorCookie.class);
                StyledDocument doc = editor.openDocument();

                String oldName = fileObject.getName().toString();
                
                String filter = oldName;
                String ext = fileObject.getExt();
                if (ext != null && !ext.isEmpty()) {
                    filter += "." + ext;
                }

                List<OffsetRange> tokens = LexerUtils.getTokens(doc, Utils.getAccessibleToken());

                for (OffsetRange offsetRange : tokens) {
                    String text = LexerUtils.getText(doc, offsetRange);

                    if (text.contains(filter)) {
                        int start = offsetRange.getStart() + text.indexOf(oldName);
                        int end = start + oldName.length();
                        refactoringElements.add(refactoring, new MappingRenameRefactoringElement(dob, start, end, refactoring.getNewName()));
                    }
                }
                
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
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
