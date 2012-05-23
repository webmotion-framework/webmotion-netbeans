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
            new WebMotionTokenId("SECTION_CONFIG", "section", 1),
            new WebMotionTokenId("SECTION_ACTIONS", "section", 2),
            new WebMotionTokenId("SECTION_ERRORS", "section", 3),
            new WebMotionTokenId("SECTION_FILTERS", "section", 4),
            new WebMotionTokenId("SECTION_EXTENSIONS", "section", 5),
            new WebMotionTokenId("SECTION_PROPERTIES", "section", 6),
            new WebMotionTokenId("CONFIG_NAMES", "keyword", 7),
            new WebMotionTokenId("ACTION_JAVA", "keyword", 8),
            new WebMotionTokenId("ACTION_URL", "keyword", 9),
            new WebMotionTokenId("ACTION_VIEW", "keyword", 10),
            new WebMotionTokenId("COMMENT", "comment", 11),
            new WebMotionTokenId("METHODS_HTTP", "keyword", 12),
            new WebMotionTokenId("METHOD_HTTP", "character", 13),
            new WebMotionTokenId("WHITESPACE", "character", 14),
            new WebMotionTokenId("DIGIT", "character", 15),
            new WebMotionTokenId("LETTER", "character", 16)
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