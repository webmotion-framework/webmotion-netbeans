/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.debux.webmotion.netbeans.hints;

import com.sun.source.tree.*;
import java.util.Collections;
import java.util.List;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import org.netbeans.api.java.source.JavaSource.Phase;
import org.netbeans.api.java.source.*;
import org.netbeans.modules.csl.api.HintFix;
import org.openide.filesystems.FileObject;

/**
 *
 * @author julien
 */
public abstract class ContentClassFix implements HintFix {
    protected JavaSource src;
    protected TypeElement classElement;

    public ContentClassFix(JavaSource src, TypeElement classElement) {
        this.src = src;
        this.classElement = classElement;
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
                
                CompilationUnitTree cut = workingCopy.getCompilationUnit();
                for (Tree type : cut.getTypeDecls()) {
                    if (Tree.Kind.CLASS == type.getKind()) {
                        ClassTree clazz = (ClassTree) type;
                        implement(workingCopy, clazz);
                    }
                }
            }

        }).commit();
    }

    protected abstract void implement(WorkingCopy workingCopy, ClassTree clazz);
    
    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public boolean isInteractive() {
        return false;
    }
    
    public static class ExtendsClassFix extends ContentClassFix {
        
        protected String superClass;

        public ExtendsClassFix(JavaSource src, TypeElement classElement, String superClass) {
            super(src, classElement);
            this.superClass = superClass;
        }
        
        @Override
        public String getDescription() {
            return "Add extends " + superClass;
        }

        @Override
        protected void implement(WorkingCopy workingCopy, ClassTree clazz) {
            TreeMaker make = workingCopy.getTreeMaker();
            
            TypeElement element = workingCopy.getElements().getTypeElement(superClass);
            ExpressionTree implementsClause = make.QualIdent(element);
            
            ClassTree modifiedClazz = make.setExtends(clazz, implementsClause);
            workingCopy.rewrite(clazz, modifiedClazz);
        }
    }
    
    public static class PublicModifierClassFix extends ContentClassFix {
        
        public PublicModifierClassFix(JavaSource src, TypeElement classElement) {
            super(src, classElement);
        }
        
        @Override
        public String getDescription() {
            return "Replace class modifier by public";
        }

        @Override
        protected void implement(WorkingCopy workingCopy, ClassTree clazz) {
            TreeMaker make = workingCopy.getTreeMaker();
            
            ModifiersTree modifiers = clazz.getModifiers();
            ModifiersTree removeModifiersModifier = make.addModifiersModifier(modifiers, Modifier.PUBLIC);
            removeModifiersModifier = make.removeModifiersModifier(removeModifiersModifier, Modifier.PROTECTED);
            removeModifiersModifier = make.removeModifiersModifier(removeModifiersModifier, Modifier.PRIVATE);
            workingCopy.rewrite(modifiers, removeModifiersModifier);
        }
    }
    
    public static class AbstractModifierClassFix extends ContentClassFix {
        
        public AbstractModifierClassFix(JavaSource src, TypeElement classElement) {
            super(src, classElement);
        }
        
        @Override
        public String getDescription() {
            return "Remove abstract modifier on class";
        }

        @Override
        protected void implement(WorkingCopy workingCopy, ClassTree clazz) {
            TreeMaker make = workingCopy.getTreeMaker();
            
            ModifiersTree modifiers = clazz.getModifiers();
            ModifiersTree removeModifiersModifier = make.removeModifiersModifier(modifiers, Modifier.ABSTRACT);
            workingCopy.rewrite(modifiers, removeModifiersModifier);
        }
    }
    
    public static class PublicModifierMethodFix extends ContentClassFix {
        
        protected String methodName;

        public PublicModifierMethodFix(JavaSource src, TypeElement classElement, String methodName) {
            super(src, classElement);
            this.methodName = methodName;
        }
        
        @Override
        public String getDescription() {
            return "Replace method modifier by public";
        }

        @Override
        protected void implement(WorkingCopy workingCopy, ClassTree clazz) {
            TreeMaker make = workingCopy.getTreeMaker();
            
            List<? extends Tree> members = clazz.getMembers();
            for (Tree tree : members) {
                if (Tree.Kind.METHOD == tree.getKind()) {
                    MethodTree method = (MethodTree) tree;
                    
                    String name = method.getName().toString();
                    if (name.equals(methodName)) {
                        ModifiersTree modifiers = method.getModifiers();
                        
                        ModifiersTree removeModifiersModifier = make.addModifiersModifier(modifiers, Modifier.PUBLIC);
                        removeModifiersModifier = make.removeModifiersModifier(removeModifiersModifier, Modifier.PROTECTED);
                        removeModifiersModifier = make.removeModifiersModifier(removeModifiersModifier, Modifier.PRIVATE);
                        workingCopy.rewrite(modifiers, removeModifiersModifier);
                        break;
                    }
                }
            }
        }
    }
    
    public static class StaticModifierMethodFix extends ContentClassFix {
        
        protected String methodName;

        public StaticModifierMethodFix(JavaSource src, TypeElement classElement, String methodName) {
            super(src, classElement);
            this.methodName = methodName;
        }
        
        @Override
        public String getDescription() {
            return "Remove static modifier on method";
        }

        @Override
        protected void implement(WorkingCopy workingCopy, ClassTree clazz) {
            TreeMaker make = workingCopy.getTreeMaker();
            
            List<? extends Tree> members = clazz.getMembers();
            for (Tree tree : members) {
                if (Tree.Kind.METHOD == tree.getKind()) {
                    MethodTree method = (MethodTree) tree;
                    
                    String name = method.getName().toString();
                    if (name.equals(methodName)) {
                        ModifiersTree modifiers = method.getModifiers();
                        
                        ModifiersTree removeModifiersModifier = make.removeModifiersModifier(modifiers, Modifier.STATIC);
                        workingCopy.rewrite(modifiers, removeModifiersModifier);
                        break;
                    }
                }
            }
        }
    }
    
    public static class MethodClassFix extends ContentClassFix {
        
        protected String methodName;

        public MethodClassFix(JavaSource src, TypeElement classElement, String methodName) {
            super(src, classElement);
            this.methodName = methodName;
        }
        
        @Override
        public String getDescription() {
            return "Add method " + methodName;
        }

        @Override
        protected void implement(WorkingCopy workingCopy, ClassTree clazz) {
            TreeMaker make = workingCopy.getTreeMaker();
            
            ModifiersTree methodModifiers = make.Modifiers(
                                Collections.<Modifier>singleton(Modifier.PUBLIC),
                                Collections.<AnnotationTree>emptyList());
            
            TypeElement element = workingCopy.getElements().getTypeElement("org.debux.webmotion.server.render.Render");
            ExpressionTree renderClause = make.QualIdent(element);
            
            MethodTree newMethod = make.Method(
                    methodModifiers, // public
                    methodName, // writeExternal
                    renderClause, // return type "render"
                    Collections.<TypeParameterTree>emptyList(), // type parameters - none
                    Collections.<VariableTree>emptyList(), // parameters - none
                    Collections.<ExpressionTree>emptyList(), // throws 
                    "{ throw new UnsupportedOperationException(\"Not supported yet.\") }", // body text
                    null // default value - not applicable here, used by annotations
                );
            
            ClassTree modifiedClazz = make.addClassMember(clazz, newMethod);
            workingCopy.rewrite(clazz, modifiedClazz);
        }
    }
}
