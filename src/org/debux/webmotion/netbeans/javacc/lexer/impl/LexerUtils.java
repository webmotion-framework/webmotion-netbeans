package org.debux.webmotion.netbeans.javacc.lexer.impl;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.apache.commons.lang.ArrayUtils;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
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
    
    public static List<OffsetRange> getTokens(Document document, String ... extractName) {
        List<OffsetRange> result = new ArrayList<OffsetRange>();
        OffsetRange current = null;
        
        TokenHierarchy<Document> hi = TokenHierarchy.get(document);
        TokenSequence<WebMotionTokenId> ts = (TokenSequence<WebMotionTokenId>) hi.tokenSequence();
        while (ts.moveNext()) {
            
            Token<WebMotionTokenId> tok = ts.token();
            WebMotionTokenId id = tok.id();
            String name = id.name();
            
            if (ArrayUtils.contains(extractName, name)) {
                int start = ts.offset();
                int end = start + tok.text().toString().length();
                
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
            ts.moveNext();
            Token<WebMotionTokenId> tok = ts.token();
            WebMotionTokenId id = tok.id();
            String name = id.name();
            
            int targetEnd = ts.offset();
            int targetStart = ts.offset();

            while (ArrayUtils.contains(extractName, name)) {
                ts.moveNext();
                tok = ts.token();
                id = tok.id();
                name = id.name();
                
                targetEnd = ts.offset();
            }

            ts.move(offset);
            ts.movePrevious();
            tok = ts.token();
            id = tok.id();
            name = id.name();

            while (ArrayUtils.contains(extractName, name)) {
                targetStart = ts.offset();

                ts.movePrevious();
                tok = ts.token();
                id = tok.id();
                name = id.name();
            }

            if (targetStart != targetEnd) {
                return new OffsetRange(targetStart, targetEnd);
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
    
}
