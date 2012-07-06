package org.debux.webmotion.netbeans.hints;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.Utils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.LexerUtils;
import org.debux.webmotion.netbeans.javacc.parser.impl.WebMotionParserImpl.WebMotionParserResult;
import org.netbeans.api.java.classpath.ClassPath;
import org.netbeans.api.java.source.*;
import org.netbeans.modules.csl.api.Hint;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.RuleContext;
import org.netbeans.modules.parsing.api.Source;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 *
 * @author julien
 */
public class ActionRule extends AbstractRule {

    @Override
    public String getDisplayName() {
        return "Test action";
    }

    @Override
    public String getDescription() {
        return "Test if the class and the method exists";
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
        JavaSource src = JavaSource.create(info);
        
        try {
            src.runUserActionTask(new CancellableTask<CompilationController>() {
                @Override
                public void cancel() {
                }

                @Override
                public void run(CompilationController cu) throws Exception {
                    String packageBase = Utils.getPackageValue("package.base", null);
                    
                    List<OffsetRange> tokens = LexerUtils.getTokens(document, "FILTER_ACTION");
                    String packageTarget = Utils.getPackageValue("package.filters", packageBase);
                    String superClass = "org.debux.webmotion.server.WebMotionFilter";
                    
                    runAction(cu, tokens, packageTarget, superClass);
                    
                    tokens = LexerUtils.getTokens(document, "ERROR_ACTION_JAVA");
                    packageTarget = Utils.getPackageValue("package.errors", packageBase);
                    superClass = "org.debux.webmotion.server.WebMotionController";
                    
                    runAction(cu, tokens, packageTarget, superClass);
                    
                    tokens = LexerUtils.getTokens(document, 
                            "ACTION_ACTION_IDENTIFIER", "ACTION_ACTION_JAVA_IDENTIFIER",
                            "ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER", "ACTION_ACTION_JAVA_VARIABLE");
                    packageTarget = Utils.getPackageValue("package.actions", packageBase);
                    superClass = "org.debux.webmotion.server.WebMotionController";
                    
                    runAction(cu, tokens, packageTarget, superClass);
                }

                protected void runAction(CompilationController cu, List<OffsetRange> tokens, String packageTarget, String superClass) {
                    Elements elements = cu.getElements();
                    TypeUtilities typeUtilities = cu.getTypeUtilities();
                    for (OffsetRange range : tokens) {
                        try {
                            String value = LexerUtils.getText(document, range);
                            String className = StringUtils.substringBeforeLast(value, ".");
                            String methodName = StringUtils.substringAfterLast(value, ".");
                            
                            if (Utils.isNotVariable(className)) {
                                TypeElement classElement = elements.getTypeElement(packageTarget + className);
                                if (classElement != null) {

                                    // Check class
                                    TypeElement controllerElement = elements.getTypeElement(superClass);
                                    if (controllerElement != null) {
                                        TypeMirror controllerType = controllerElement.asType();

                                        Set<Modifier> modifiers = classElement.getModifiers();
                                        ElementKind kind = classElement.getKind();
                                        TypeMirror resolveType = classElement.asType();

                                        if (kind == ElementKind.CLASS) {
                                            if (!modifiers.contains(Modifier.PUBLIC)) {
                                                hints.add(new Hint(ActionRule.this, "The class is not public", fileObject, range, WebMotionHintsProvider.NO_FIXES, 100));
                                            }
                                            if (modifiers.contains(Modifier.ABSTRACT)) {
                                                hints.add(new Hint(ActionRule.this, "The class is abstract", fileObject, range, WebMotionHintsProvider.NO_FIXES, 100));
                                            }
                                            if (!typeUtilities.isCastable(resolveType, controllerType)) {
                                                hints.add(new Hint(ActionRule.this, "Requires super class " + superClass, fileObject, range, WebMotionHintsProvider.NO_FIXES, 100));
                                            }
                                        }
                                    }

                                    // Check method
                                    if (Utils.isNotVariable(methodName)) {
                                        List<? extends Element> members = elements.getAllMembers(classElement);
                                        Element method = null;
                                        for (Element member : members) {
                                            ElementKind kind = member.getKind();
                                            String name = member.getSimpleName().toString();
                                            if (kind == ElementKind.METHOD && name.equals(methodName)) {
                                                method = member;
                                                break;
                                            }
                                        }

                                        if (method != null) {
                                            Set<Modifier> modifiers = method.getModifiers();
                                            String currentClass = method.getEnclosingElement().getSimpleName().toString();

                                            if ("Object".equals(currentClass)
                                                || "WebMotionController".equals(currentClass)
                                                || "WebMotionFilter".equals(currentClass)) {
                                                hints.add(new Hint(ActionRule.this, "Invalid method", fileObject, range, WebMotionHintsProvider.NO_FIXES, 100));
                                            }
                                            if (!modifiers.contains(Modifier.PUBLIC)) {
                                                hints.add(new Hint(ActionRule.this, "The method is not public", fileObject, range, WebMotionHintsProvider.NO_FIXES, 100));
                                            }
                                            if (modifiers.contains(Modifier.STATIC)) {
                                                hints.add(new Hint(ActionRule.this, "The method is static", fileObject, range, WebMotionHintsProvider.NO_FIXES, 100));
                                            }

                                        } else {
                                            hints.add(new Hint(ActionRule.this, "Invalid method", fileObject, range, WebMotionHintsProvider.NO_FIXES, 100));
                                        }
                                    }

                                } else {
                                    hints.add(new Hint(ActionRule.this, "Invalid class", fileObject, range, WebMotionHintsProvider.NO_FIXES, 100));
                                }
                            }
                        } catch (BadLocationException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }, false);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }   
    }
}
