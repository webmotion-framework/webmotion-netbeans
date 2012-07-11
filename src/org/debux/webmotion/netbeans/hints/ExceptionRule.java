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
package org.debux.webmotion.netbeans.hints;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.Tree;
import java.io.IOException;
import java.util.List;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.Utils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.LexerUtils;
import org.debux.webmotion.netbeans.javacc.parser.impl.WebMotionParserImpl.WebMotionParserResult;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.*;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.modules.csl.api.Hint;
import org.netbeans.modules.csl.api.HintFix;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.RuleContext;
import org.netbeans.modules.parsing.api.Source;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

/**
 *
 * @author julien
 */
public class ExceptionRule extends AbstractRule {

    @Override
    public String getDisplayName() {
        return "Test exception";
    }

    @Override
    public String getDescription() {
        return "Test if the exception is defined";
    }

    @Override
    public void run(RuleContext context, final List<Hint> hints) {
        WebMotionParserResult parserResult = (WebMotionParserResult) context.parserResult;
        
        Source source = parserResult.getSnapshot().getSource();
        final Document document = source.getDocument(false);
        final FileObject fileObject = source.getFileObject();
        
        FileObject fo = Utils.getFO(document);
        ClassPath bootCp = ClassPath.getClassPath(fo, ClassPath.BOOT);
        ClassPath compileCp = ClassPath.getClassPath(fo, ClassPath.COMPILE);
        ClassPath sourcePath = ClassPath.getClassPath(fo, ClassPath.SOURCE);
        ClasspathInfo info = ClasspathInfo.create(bootCp, compileCp, sourcePath);
        final JavaSource src = JavaSource.create(info);
        
        try {
            src.runUserActionTask(new CancellableTask<CompilationController>() {
                @Override
                public void cancel() {
                }

                @Override
                public void run(CompilationController cu) throws Exception {
                    Elements elements = cu.getElements();
                    TypeUtilities typeUtilities = cu.getTypeUtilities();
                    
                    List<OffsetRange> tokens = LexerUtils.getTokens(document, "EXCEPTION");
                    for (OffsetRange range : tokens) {
                        String value = LexerUtils.getText(document, range);
                        
                        TypeElement classElement = elements.getTypeElement(value);
                        if (classElement != null) {
                            // Check class
                            TypeElement controllerElement = elements.getTypeElement("java.lang.Exception");
                            if (controllerElement != null) {
                                TypeMirror controllerType = controllerElement.asType();
                                ElementKind kind = classElement.getKind();
                                TypeMirror resolveType = classElement.asType();

                                if (kind == ElementKind.CLASS && !typeUtilities.isCastable(resolveType, controllerType)) {
                                    hints.add(new Hint(ExceptionRule.this, "Requires super class java.lang.Exception", fileObject, range, 
                                            WebMotionHintsProvider.asList(new ExtendsExceptionFix(src, classElement)), 100));
                                }
                            }
                        } else {
                            hints.add(new Hint(ExceptionRule.this, "Invalid class", fileObject, range, 
                                    WebMotionHintsProvider.asList(new CreateClassFix(src, value)), 100));
                        }
                    }
                }
            }, false);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }   
    }

    public static class CreateClassFix implements HintFix {

        protected JavaSource src;
        protected String fullClassName;

        public CreateClassFix(JavaSource src, String fullClassName) {
            this.src = src;
            this.fullClassName = fullClassName;
        }

        @Override
        public String getDescription() {
            return "Create new exception class";
        }

        @Override
        public void implement() throws Exception {
            ClasspathInfo classpathInfo = src.getClasspathInfo();
            ClassPath classPath = classpathInfo.getClassPath(ClasspathInfo.PathKind.SOURCE);
            FileObject targetSourceRoot = classPath.findResource("");

            String packageName = StringUtils.substringBeforeLast(fullClassName, ".");
            String simpleName = StringUtils.substringAfterLast(fullClassName, ".");

            FileObject pack = FileUtil.createFolder(targetSourceRoot, packageName.replace('.', '/')); // NOI18N
            FileObject classTemplate = FileUtil.getConfigFile("Templates/Classes/Class.java");
            FileObject target;

            if (classTemplate != null) {
                DataObject classTemplateDO = DataObject.find(classTemplate);
                DataObject od = classTemplateDO.createFromTemplate(DataFolder.findFolder(pack), simpleName);

                target = od.getPrimaryFile();
            } else {
                target = FileUtil.createData(pack, simpleName + ".java");
            }
            
            JavaSource.forFileObject(target).runModificationTask(new Task<WorkingCopy>() {
                @Override
                public void run(WorkingCopy workingCopy) throws Exception {
                    workingCopy.toPhase(Phase.RESOLVED);

                    TreeMaker make = workingCopy.getTreeMaker();
                    CompilationUnitTree cut = workingCopy.getCompilationUnit();
                    
                    for (Tree type : cut.getTypeDecls()) {
                        if (Tree.Kind.CLASS == type.getKind()) {
                            ClassTree clazz = (ClassTree) type;
                            
                            TypeElement element = workingCopy.getElements().getTypeElement("java.lang.Exception");
                            ExpressionTree implementsClause = make.QualIdent(element);

                            ClassTree modifiedClazz = make.setExtends(clazz, implementsClause);
                            workingCopy.rewrite(clazz, modifiedClazz);
                        }
                    }
                }
            }).commit();
        }

        @Override
        public boolean isSafe() {
            return false;
        }

        @Override
        public boolean isInteractive() {
            return false;
        }

    }
    
    public static class ExtendsExceptionFix implements HintFix {

        protected JavaSource src;
        protected TypeElement classElement;

        public ExtendsExceptionFix(JavaSource src, TypeElement classElement) {
            this.src = src;
            this.classElement = classElement;
        }

        @Override
        public String getDescription() {
            return "Add extends java.lang.Exception";
        }

        @Override
        public void implement() throws Exception {
            ClasspathInfo classpathInfo = src.getClasspathInfo();
            ElementHandle<TypeElement> eh = ElementHandle.create(classElement);
            FileObject file = SourceUtils.getFile(eh, classpathInfo);
            
            JavaSource.forFileObject(file).runModificationTask(new Task<WorkingCopy>() {
                @Override
                public void run(WorkingCopy workingCopy) throws Exception {
                    workingCopy.toPhase(Phase.RESOLVED);

                    TreeMaker make = workingCopy.getTreeMaker();
                    CompilationUnitTree cut = workingCopy.getCompilationUnit();
                    
                    for (Tree type : cut.getTypeDecls()) {
                        if (Tree.Kind.CLASS == type.getKind()) {
                            ClassTree clazz = (ClassTree) type;
                            
                            TypeElement element = workingCopy.getElements().getTypeElement("java.lang.Exception");
                            ExpressionTree implementsClause = make.QualIdent(element);

                            ClassTree modifiedClazz = make.setExtends(clazz, implementsClause);
                            workingCopy.rewrite(clazz, modifiedClazz);
                        }
                    }
                }
            }).commit();
        }

        @Override
        public boolean isSafe() {
            return false;
        }

        @Override
        public boolean isInteractive() {
            return false;
        }

    }
}
