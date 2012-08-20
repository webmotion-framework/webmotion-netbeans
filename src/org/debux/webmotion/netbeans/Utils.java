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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.LexerUtils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.debux.webmotion.netbeans.javacc.parser.WebMotionParser;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenSequence;
import org.openide.filesystems.FileAlreadyLockedException;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 *
 * @author julien
 */
public class Utils {
    
    /** Pattern to get all variable like {var} in String */
    public static Pattern VARIABLE_PATTERN = Pattern.compile("(^|[^\\\\])\\{((\\p{Alnum}|\\.)+)\\}");
    
    /**
     * Test if the value not contains a variable like "{var"}.
     * @param value value to test
     * @return true if the string not contains a variable otherwise false
     */
    public static boolean isNotVariable(String value) {
        return !isVariable(value);
    }
    
    /**
     * Test if the value contains a variable like "{var"}.
     * @param value value to test
     * @return true if the string contains a variable otherwise false
     */
    public static boolean isVariable(String value) {
        Matcher matcher = VARIABLE_PATTERN.matcher(value);
        return matcher.find();
    }
    
    public static FileObject getFO(Document doc) {
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
    
    public static String getPackageValue(String name, String packageBase) {
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
    
    
    // Get the package in configuration
    public static String getPackage(Document document, int offset) {
        String packageBase = Utils.getPackageValue("package.base", null);
        String packageTarget = null;
        
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) LexerUtils.getMostEmbeddedTokenSequence(document, offset, true);
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
            
        } else if ("ACTION_ACTION_VIEW_VALUE".equals(name) ||
                   "ACTION_ACTION_VIEW_VARIABLE".equals(name) ||
                   "ERROR_ACTION_VALUE".equals(name)) {
            packageTarget = Utils.getPackageValue("package.views", null);
            
            
        } else if ("EXCEPTION".equals(name)) {
            packageTarget = "";
        }
        
        return packageTarget;
    }
    
    public static int getRowFirstNonWhite(StyledDocument doc, int offset) throws BadLocationException {
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

    public static int indexOfWhite(char[] line) {
        int i = line.length;
        while (--i > -1) {
            final char c = line[i];
            if (Character.isWhitespace(c)) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * Return true if {@code str} is a pattern (contains * or ?).
     *
     * @param str str to test
     * @return {@code true} if {@code str} is a pattern, {@code false} otherwise
     * @since 2.2
     */
    public static boolean isPattern(String str) {
        return str.indexOf('*') != -1 || str.indexOf('?') != -1;
    }
    
    public static Set<FileObject> findAllMappings() {
        GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
        Set<FileObject> sourceRoots = registry.getSourceRoots();
        return findAllMappings(sourceRoots.toArray(new FileObject[0]));
    }
    
    public static Set<FileObject> findAllMappings(FileObject[] fileObjects) {
        Set<FileObject> result = new HashSet<FileObject>();
        
        for (FileObject fileObject : fileObjects) {
            if (fileObject.isFolder()) {
                FileObject[] children = fileObject.getChildren();
                result.addAll(findAllMappings(children));
                
            } else if (fileObject.getMIMEType().equals(WebMotionLanguage.MIME_TYPE)) {
                result.add(fileObject);
            }
        }
        
        return result;
    }

    public static String[] getAccessibleToken() {
        return new String[]{
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
    
    public static void renameFileObject(FileObject fo, String newName) throws IOException {
        FileLock lock = null;
        try {
            lock = fo.lock();
        } catch (FileAlreadyLockedException e) {
            // Try again later; perhaps display a warning dialog.
            return;
        }
        try {
            String extension = StringUtils.substringAfterLast(newName, ".");
            String name = StringUtils.substringBeforeLast(newName, ".");
            fo.rename(lock, name, extension);
        } finally {
            // Always put this in a finally block!
            lock.releaseLock();
        }
    }

}
