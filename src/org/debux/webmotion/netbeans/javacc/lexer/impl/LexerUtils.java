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
package org.debux.webmotion.netbeans.javacc.lexer.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.commons.lang.ArrayUtils;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.modules.csl.api.OffsetRange;

/**
 *
 * @author julien
 */
public class LexerUtils {
    
    public static WebMotionTokenId.Section getSection(Document document, int offset) {
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        if (ts != null) {
            ts.move(offset);
            ts.moveNext();
            
            Token<WebMotionTokenId> tok = ts.token();
            WebMotionTokenId id = tok.id();
            
            return id.getSection();
        }
        
        return WebMotionTokenId.Section.NONE;
    }
    
    public static List<OffsetRange> getSeparatedTokens(Document document, String ... extractName) {
        List<OffsetRange> result = new ArrayList<OffsetRange>();
        
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        while (ts.moveNext()) {
            
            Token<WebMotionTokenId> token = ts.token();
            WebMotionTokenId id = token.id();
            String name = id.name();
            
            if (ArrayUtils.contains(extractName, name)) {
                int start = ts.offset();
                int end = start + token.text().toString().length();
                
                OffsetRange offsetRange = new OffsetRange(start, end);
                result.add(offsetRange);
            }
        }
        
        return result;
    }
    
    public static TokenSequence<? extends TokenId> getMostEmbeddedTokenSequence(final Document doc, final int offset, boolean runUnderLock) {
        final AtomicReference<TokenSequence<? extends TokenId>> ref = new AtomicReference<TokenSequence<? extends TokenId>>();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                TokenHierarchy th = TokenHierarchy.get(doc);
                List<TokenSequence<? extends TokenId>> sequences = th.embeddedTokenSequences(offset, false);
                if(sequences.isEmpty()) {
                    //no embedding, return top level sequence;
                    ref.set(th.tokenSequence());
                } else {
                    ref.set(sequences.get(sequences.size() - 1)); //return the most embedded one
                }
            }
        };

        if(runUnderLock) {
            doc.render(r);
        } else {
            r.run();
        }

        return ref.get();
    }
        
    public static List<OffsetRange> getTokens(Document document, String ... extractName) {
        List<OffsetRange> result = new ArrayList<OffsetRange>();
        OffsetRange current = null;
        
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        while (ts.moveNext()) {
            
            Token<WebMotionTokenId> token = ts.token();
            WebMotionTokenId id = token.id();
            String name = id.name();
            
            if (ArrayUtils.contains(extractName, name)) {
                int start = ts.offset();
                int end = start + token.text().toString().length();
                
                if (current == null) {
                    current = new OffsetRange(start, end);
                } else {
                    current = new OffsetRange(current.getStart(), end);
                }

            } else {
                if(current != null) {
                    result.add(current);
                }
                current = null;
            }
        }
        
        return result;
    }

    public static OffsetRange getTokens(Document document, int offset, String ... extractName) {
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        if (ts != null) {
            
            ts.move(offset);
            if (ts.moveNext()) {
                Token<WebMotionTokenId> token = ts.token();
                WebMotionTokenId id = token.id();
                String name = id.name();

                int targetEnd = ts.offset();
                int targetStart = ts.offset();

                while (ArrayUtils.contains(extractName, name)) {
                    ts.moveNext();
                    token = ts.token();
                    id = token.id();
                    name = id.name();

                    targetEnd = ts.offset();
                }

                ts.move(offset);
                if (ts.movePrevious()) {
                    
                    token = ts.token();
                    id = token.id();
                    name = id.name();

                    while (ArrayUtils.contains(extractName, name)) {
                        targetStart = ts.offset();

                        ts.movePrevious();
                        token = ts.token();
                        id = token.id();
                        name = id.name();
                    }

                    if (targetStart != targetEnd) {
                        return new OffsetRange(targetStart, targetEnd);
                    }
                }
            }
        }
        return null;
    }
    
    public static String getText(Document document, OffsetRange range) throws BadLocationException {
        int start = range.getStart();
        int length = range.getEnd() - start;
        String target = document.getText(start, length);
        return target;
    }
    
    
    public static boolean isJavaToken(Document document, int offset) {
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) LexerUtils.getMostEmbeddedTokenSequence(document, offset, true);
        
        Token<WebMotionTokenId> tok = ts.token();
        WebMotionTokenId id = tok.id();
        
        String name = id.name();
        if ("EXTENSION_FILE".equals(name) ||
                "ACTION_ACTION_VIEW_VALUE".equals(name) ||
                "ACTION_ACTION_VIEW_VARIABLE".equals(name) ||
                "ERROR_ACTION_VALUE".equals(name)) {
            
            return false;
        }
        
        return true;
    }
}
