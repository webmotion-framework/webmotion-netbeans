package org.debux.webmotion.netbeans.javacc;

import java.util.*;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author julien
 */ 
public class WebMotionLanguageHierarchy extends LanguageHierarchy<WebMotionTokenId> {

    private static List<WebMotionTokenId> tokens;
    private static Map<Integer, WebMotionTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<WebMotionTokenId>asList(new WebMotionTokenId[]{
            new WebMotionTokenId("EOF", "whitespace", 0),
            new WebMotionTokenId("SECTION_CONFIG", "keyword", 1),
            new WebMotionTokenId("SECTION_ACTIONS", "keyword", 2),
            new WebMotionTokenId("SECTION_ERRORS", "keyword", 3),
            new WebMotionTokenId("SECTION_FILTERS", "keyword", 4),
            new WebMotionTokenId("SECTION_PROPERTIES", "keyword", 5),
            new WebMotionTokenId("COMMENT", "comment", 6),
            new WebMotionTokenId("METHOD_HTTP", "literal", 7),
            new WebMotionTokenId("WHITESPACE", "whitespace", 8),
            new WebMotionTokenId("DIGIT", "whitespace", 9),
            new WebMotionTokenId("LETTER", "whitespace", 10)
        });
        idToToken = new HashMap<Integer, WebMotionTokenId>();
        for (WebMotionTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized WebMotionTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<WebMotionTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<WebMotionTokenId> createLexer(LexerRestartInfo<WebMotionTokenId> info) {
        return new WebMotionLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-wm";
    }

}