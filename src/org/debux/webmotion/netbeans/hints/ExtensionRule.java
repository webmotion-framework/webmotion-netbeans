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
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;

/**
 *
 * @author julien
 */
public class ExtensionRule extends AbstractRule {

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
        
        Source source = parserResult.getSnapshot().getSource();
        Document document = source.getDocument(false);
        FileObject fileObject = source.getFileObject();
        
        List<OffsetRange> tokens = LexerUtils.getTokens(document, "EXTENSION_FILE");
        for (OffsetRange range : tokens) {
            try {
                String value = LexerUtils.getText(document, range);
                if (!Utils.isPattern(value)) {
                    GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                    FileObject fo = registry.findResource(value);
                    if (fo == null) {
                        hints.add(new Hint(this, "Invalid file", 
                                fileObject, range, WebMotionHintsProvider.asList(new CreateFileFix(fileObject, value)), 100));
                    }
                }
                
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public static class CreateFileFix implements HintFix {

        protected FileObject fileObject;
        protected String name;

        public CreateFileFix(FileObject fileObject, String name) {
            this.fileObject = fileObject;
            this.name = name;
        }

        @Override
        public String getDescription() {
            return "Create new extension file";
        }

        @Override
        public void implement() throws Exception {
            FileObject classTemplate = null;
            if (name.endsWith(".wm")) {
                classTemplate = FileUtil.getConfigFile("Templates/WebMotion/mapping.wm");
                name = StringUtils.substringBeforeLast(name, ".wm");
            }

            FileObject pack = fileObject.getParent();
            FileObject target;
            if (classTemplate != null) {
                DataObject classTemplateDO = DataObject.find(classTemplate);
                DataObject od = classTemplateDO.createFromTemplate(DataFolder.findFolder(pack), name);

                target = od.getPrimaryFile();
            } else {
                target = FileUtil.createData(pack, name);
            }
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
