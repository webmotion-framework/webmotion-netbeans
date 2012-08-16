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

import java.util.Collection;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.debux.webmotion.netbeans.javacc.lexer.impl.LexerUtils;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.refactoring.spi.ui.ActionsImplementationProvider;
import org.netbeans.modules.refactoring.spi.ui.UI;
import org.openide.cookies.EditorCookie;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author julien
 */
@ServiceProvider(service = ActionsImplementationProvider.class)
public class WebMotionRefactoringActions extends ActionsImplementationProvider {

    @Override
    public boolean canRename(Lookup lookup) {
        Collection<? extends Node> nodes = lookup.lookupAll(Node.class);
        return true;
    }

    @Override
    public void doRename(Lookup lookup) {
        EditorCookie ec = lookup.lookup(EditorCookie.class);
        JTextComponent textComponent = ec.getOpenedPanes()[0];
        Document document = textComponent.getDocument();
        int caretPosition = textComponent.getCaretPosition();
        
        final RefactoringContext context = new RefactoringContext(document, caretPosition);
        
        new Runnable() {
            @Override
            public void run() {
                UI.openRefactoringUI(new WebMotionRenameRefactoringUI(context));
            }
        }.run();
    }
    
    
    public static class RefactoringContext {
        protected int caret;
        protected Document document;

        public RefactoringContext(Document document, int caret) {
            this.document = document;
            this.caret = caret;
        }
        
        public String getValue() {
            TokenSequence<? extends TokenId> ts = LexerUtils.getMostEmbeddedTokenSequence(document, caret, true);
            Token<? extends TokenId> token = ts.token();
            String value = token.text().toString();
            return value;
        }
    }
    
}
