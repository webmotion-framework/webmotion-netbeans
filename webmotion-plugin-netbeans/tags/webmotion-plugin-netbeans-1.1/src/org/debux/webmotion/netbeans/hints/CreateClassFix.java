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
import javax.lang.model.element.TypeElement;
import org.apache.commons.lang.StringUtils;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.*;
import org.netbeans.modules.csl.api.HintFix;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;

/**
 *
 * @author julien
 */
public class CreateClassFix implements HintFix {
    protected JavaSource src;
    protected String packageTarget;
    protected String superClass;
    protected String fullClassName;

    public CreateClassFix(JavaSource src, String packageTarget, String superClass, String fullClassName) {
        this.src = src;
        this.packageTarget = packageTarget;
        this.superClass = superClass;
        this.fullClassName = fullClassName;
    }

    @Override
    public String getDescription() {
        return "Create class";
    }

    @Override
    public void implement() throws Exception {
        ClasspathInfo classpathInfo = src.getClasspathInfo();
        ClassPath classPath = classpathInfo.getClassPath(ClasspathInfo.PathKind.SOURCE);
        FileObject targetSourceRoot = classPath.findResource(packageTarget.replaceAll("\\.", "/"));
        
        String packageName = "";
        String simpleName = fullClassName;
        if (fullClassName.contains(".")) {
            packageName = StringUtils.substringBeforeLast(fullClassName, ".");
            simpleName = StringUtils.substringAfterLast(fullClassName, ".");
        }
        
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
                workingCopy.toPhase(JavaSource.Phase.RESOLVED);
                TreeMaker make = workingCopy.getTreeMaker();
                CompilationUnitTree cut = workingCopy.getCompilationUnit();
                for (Tree type : cut.getTypeDecls()) {
                    if (Tree.Kind.CLASS == type.getKind()) {
                        ClassTree clazz = (ClassTree) type;
                        TypeElement element = workingCopy.getElements().getTypeElement(superClass);
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
