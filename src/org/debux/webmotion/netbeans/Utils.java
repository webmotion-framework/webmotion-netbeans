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

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.javacc.parser.WebMotionParser;
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
    
    public static void createFile(FileObject fo, String fileName) throws IOException {
        File base = FileUtil.toFile(fo);
        File file = new File(base, fileName);

        File parent = file.getParentFile();
        parent.mkdirs();
        file.createNewFile();
    }
    
}
