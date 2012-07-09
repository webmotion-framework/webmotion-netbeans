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

import java.io.File;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.commons.lang.StringUtils;
import org.debux.webmotion.netbeans.Utils;
import org.debux.webmotion.netbeans.javacc.lexer.impl.LexerUtils;
import org.debux.webmotion.netbeans.javacc.parser.impl.WebMotionParserImpl.WebMotionParserResult;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.modules.csl.api.Hint;
import org.netbeans.modules.csl.api.HintFix;
import org.netbeans.modules.csl.api.OffsetRange;
import org.netbeans.modules.csl.api.RuleContext;
import org.netbeans.modules.parsing.api.Source;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;

/**
 *
 * @author julien
 */
public class ViewRule extends AbstractRule {

    @Override
    public String getDisplayName() {
        return "Test view";
    }

    @Override
    public String getDescription() {
        return "Test if the view exists";
    }

    @Override
    public void run(RuleContext context, List<Hint> hints) {
        WebMotionParserResult parserResult = (WebMotionParserResult) context.parserResult;
        
        String packageViews = Utils.getPackageValue("package.views", null);
        packageViews = packageViews.replaceAll("\\.+", "/");
        
        Source source = parserResult.getSnapshot().getSource();
        Document document = source.getDocument(false);
        FileObject fileObject = source.getFileObject();
        
        List<OffsetRange> tokens = LexerUtils.getTokens(document, 
                "ACTION_ACTION_VIEW", 
                "ACTION_ACTION_VIEW_VALUE", 
                "ACTION_ACTION_VIEW_VARIABLE", 
                "ERROR_ACTION_VIEW_BEGIN", 
                "ERROR_ACTION_VALUE");
        for (OffsetRange range : tokens) {
            try {
                String value = LexerUtils.getText(document, range);
                if (value.startsWith("view:")) {
                    String viewName = StringUtils.substringAfter(value, "view:");
                    if (Utils.isNotVariable(viewName)) {
                        
                        GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                        FileObject fo = registry.findResource(packageViews + viewName);
                        if (fo == null) {
                            int end = range.getStart() + value.length();
                            int start = end - viewName.length();
                            OffsetRange offsetRange = new OffsetRange(start, end);
                            hints.add(new Hint(this, "Invalid view", fileObject, offsetRange,
                                    WebMotionHintsProvider.asList(new ViewFix(packageViews, viewName)), 100));
                        }
                    }
                }
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }


    public static class ViewFix implements HintFix {

        protected String packageViews;
        protected String viewName;

        public ViewFix(String packageViews, String viewName) {
            this.packageViews = packageViews;
            this.viewName = viewName;
        }

        @Override
        public String getDescription() {
            return "Create new view file";
        }

        @Override
        public void implement() throws Exception {
            GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
            FileObject fo = registry.findResource(packageViews);
            Utils.createFile(fo, viewName);
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
