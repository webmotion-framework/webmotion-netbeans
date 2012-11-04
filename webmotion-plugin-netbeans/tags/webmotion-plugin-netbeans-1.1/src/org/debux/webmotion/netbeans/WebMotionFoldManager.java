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

import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.netbeans.api.editor.fold.Fold;
import org.netbeans.api.editor.fold.FoldHierarchy;
import org.netbeans.api.editor.fold.FoldType;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.spi.editor.fold.FoldHierarchyTransaction;
import org.netbeans.spi.editor.fold.FoldManager;
import org.netbeans.spi.editor.fold.FoldManagerFactory;
import org.netbeans.spi.editor.fold.FoldOperation;
import org.openide.util.Exceptions;

/**
 *
 * @author julien
 */
public class WebMotionFoldManager implements FoldManager {
    
    public static final FoldType COMMENT_FOLD_TYPE = new FoldType("# ...");
    public static final FoldType SECTION_FOLD_TYPE = new FoldType("...");

    private FoldOperation operation;
    
    public static class Factory implements FoldManagerFactory {
        @Override
        public FoldManager createFoldManager() {
            return new WebMotionFoldManager();
        }
    }
    
    @Override
    public void init(FoldOperation operation) {
        this.operation = operation;
    }

    public FoldOperation getOperation() {
        return operation;
    }
    
    @Override
    public void initFolds(FoldHierarchyTransaction transaction) {
        FoldHierarchy hierarchy = operation.getHierarchy();
        Document document = hierarchy.getComponent().getDocument();
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        
        FoldType type = null;
        int start = 0;
        int offset = 0;
        while (ts.moveNext()) {
            offset = ts.offset();
            
            Token<WebMotionTokenId> token = ts.token();
            String matcherText = token.text().toString();
            
            WebMotionTokenId id = token.id();
            String name = id.name();
            if (name.equals("COMMENT") && type == null) {
                type = COMMENT_FOLD_TYPE;
                start = offset;
                
            } else if (name.startsWith("SECTION")) {
                if (type != null) {
                    try {
                        operation.addToHierarchy(type, type.toString(), false, start, offset - 1, 0, 0, hierarchy, transaction);
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
                
                start = offset + matcherText.length() - 1;
                type = SECTION_FOLD_TYPE;
                
            }
        }
        
        if (type != null) {
            try {
                operation.addToHierarchy(type, type.toString(), false, start, offset + 1, 0, 0, hierarchy, transaction);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

    }

    @Override
    public void insertUpdate(DocumentEvent evt, FoldHierarchyTransaction transaction) {
    }

    @Override
    public void removeUpdate(DocumentEvent evt, FoldHierarchyTransaction transaction) {
    }
    
    @Override
    public void changedUpdate(DocumentEvent evt, FoldHierarchyTransaction transaction) {
    }
    
    @Override
    public void removeEmptyNotify(Fold emptyFold) {
    }
    
    @Override
    public void removeDamagedNotify(Fold damagedFold) {
    }
    
    @Override
    public void expandNotify(Fold expandedFold) {
    }

    @Override
    public void release() {
    }
}
