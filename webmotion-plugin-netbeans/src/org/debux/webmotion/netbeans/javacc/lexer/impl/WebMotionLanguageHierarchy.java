package org.debux.webmotion.netbeans.javacc.lexer.impl;

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
            new WebMotionTokenId("EOF", "character", 0),
            new WebMotionTokenId("COMMENT", "comment", 1),
            new WebMotionTokenId("COMMENT_IN_CONFIG", "comment", 2),
            new WebMotionTokenId("COMMENT_IN_ACTIONS", "comment", 3),
            new WebMotionTokenId("COMMENT_IN_ERRORS", "comment", 4),
            new WebMotionTokenId("COMMENT_IN_FILTERS", "comment", 5),
            new WebMotionTokenId("COMMENT_IN_EXTENSIONS", "comment", 6),
            new WebMotionTokenId("COMMENT_IN_PROPERTIES", "comment", 7),
            new WebMotionTokenId("SECTION_CONFIG_NAME", "section", 8),
            new WebMotionTokenId("SECTION_ACTIONS_NAME", "section", 9),
            new WebMotionTokenId("SECTION_ERRORS_NAME", "section", 10),
            new WebMotionTokenId("SECTION_FILTERS_NAME", "section", 11),
            new WebMotionTokenId("SECTION_EXTENSIONS_NAME", "section", 12),
            new WebMotionTokenId("SECTION_PROPERTIES_NAME", "section", 13),
            new WebMotionTokenId("METHODS_HTTP", "character", 14),
            new WebMotionTokenId("METHOD_HTTP", "keyword", 15),
            new WebMotionTokenId("ACTION_BASIC_JAVA", "character", 16),
            new WebMotionTokenId("ACTION_JAVA", "character", 17),
            new WebMotionTokenId("ACTION_VIEW", "character", 18),
            new WebMotionTokenId("ACTION_LINK", "character", 19),
            new WebMotionTokenId("VARIABLE", "character", 20),
            new WebMotionTokenId("PATTERN", "character", 21),
            new WebMotionTokenId("IDENTIFIER", "character", 22),
            new WebMotionTokenId("QUALIFIED_IDENTIFIER", "character", 23),
            new WebMotionTokenId("NEW_LINE", "character", 24),
            new WebMotionTokenId("WHITESPACE", "character", 25),
            new WebMotionTokenId("DIGIT", "character", 26),
            new WebMotionTokenId("LETTER", "character", 27),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_CONFIG", "section", 28),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_ACTIONS", "section", 29),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_ERRORS", "section", 30),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_FILTERS", "section", 31),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_EXTENSIONS", "section", 32),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_PROPERTIES", "section", 33),
            new WebMotionTokenId("CONFIG_KEY", "character", 34),
            new WebMotionTokenId("CONFIG_EQUALS", "character", 35),
            new WebMotionTokenId("CONFIG_VALUE", "character", 36),
            new WebMotionTokenId("CONFIG_END", "character", 37),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_CONFIG", "section", 38),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_ACTIONS", "section", 39),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_ERRORS", "section", 40),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_FILTERS", "section", 41),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_EXTENSIONS", "section", 42),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_PROPERTIES", "section", 43),
            new WebMotionTokenId("ACTION_METHOD", "character", 44),
            new WebMotionTokenId("ACTION_PATH", "character", 45),
            new WebMotionTokenId("ACTION_PATH_VARIABLE", "character", 46),
            new WebMotionTokenId("ACTION_PARAMETERS_BEGIN", "character", 47),
            new WebMotionTokenId("ACTION_ACTION_JAVA", "character", 48),
            new WebMotionTokenId("ACTION_ACTION_VIEW", "character", 49),
            new WebMotionTokenId("ACTION_ACTION_LINK", "character", 50),
            new WebMotionTokenId("ACTION_SEPARATOR", "character", 51),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_NAME", "character", 52),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_EQUALS", "character", 53),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_OTHER", "character", 54),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_SEPARATOR", "character", 55),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE", "character", 56),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE_VARIABLE", "character", 57),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE_OTHER", "character", 58),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE_SEPARATOR", "character", 59),
            new WebMotionTokenId("ACTION_ACTION_JAVA_VALUE", "character", 60),
            new WebMotionTokenId("ACTION_ACTION_JAVA_IDENTIFIER", "character", 61),
            new WebMotionTokenId("ACTION_ACTION_JAVA_SEPARATOR", "character", 62),
            new WebMotionTokenId("ACTION_ACTION_JAVA_END", "character", 63),
            new WebMotionTokenId("ACTION_ACTION_VIEW_VALUE", "character", 64),
            new WebMotionTokenId("ACTION_ACTION_VIEW_SEPARATOR", "character", 65),
            new WebMotionTokenId("ACTION_ACTION_VIEW_END", "character", 66),
            new WebMotionTokenId("ACTION_ACTION_LINK_VALUE", "character", 67),
            new WebMotionTokenId("ACTION_ACTION_LINK_SEPARATOR", "character", 68),
            new WebMotionTokenId("ACTION_ACTION_LINK_END", "character", 69),
            new WebMotionTokenId("ACTION_PARAMETERS_SEPARATOR", "character", 70),
            new WebMotionTokenId("ACTION_PARAMETER_NAME", "character", 71),
            new WebMotionTokenId("ACTION_PARAMETER_EQUALS", "character", 72),
            new WebMotionTokenId("ACTION_PARAMETER_SEPARATOR", "character", 73),
            new WebMotionTokenId("ACTION_END", "character", 74),
            new WebMotionTokenId("ACTION_PARAMETER_VALUE", "character", 75),
            new WebMotionTokenId("ACTION_PARAMETER_VALUE_SEPARATOR", "character", 76),
            new WebMotionTokenId("ACTION_PARAMETER_VALUE_END", "character", 77),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_CONFIG", "section", 78),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_ACTIONS", "section", 79),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_ERRORS", "section", 80),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_FILTERS", "section", 81),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_EXTENSIONS", "section", 82),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_PROPERTIES", "section", 83),
            new WebMotionTokenId("CODE", "character", 84),
            new WebMotionTokenId("ALL", "character", 85),
            new WebMotionTokenId("EXCEPTION", "character", 86),
            new WebMotionTokenId("ERROR_SEPARATOR", "character", 87),
            new WebMotionTokenId("ERROR_ACTION_JAVA", "character", 88),
            new WebMotionTokenId("ERROR_ACTION_VIEW", "character", 89),
            new WebMotionTokenId("ERROR_ACTION_LINK", "character", 90),
            new WebMotionTokenId("ERROR_END", "character", 91),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_CONFIG", "section", 92),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_ACTIONS", "section", 93),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_ERRORS", "section", 94),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_FILTERS", "section", 95),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_EXTENSIONS", "section", 96),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_PROPERTIES", "section", 97),
            new WebMotionTokenId("FILTER_METHOD", "character", 98),
            new WebMotionTokenId("FILTER_PATH", "character", 99),
            new WebMotionTokenId("FILTER_PATH_ALL", "character", 100),
            new WebMotionTokenId("FILTER_SEPARATOR", "character", 101),
            new WebMotionTokenId("FILTER_ACTION", "character", 102),
            new WebMotionTokenId("FILTER_PARAMETERS_SEPARATOR", "character", 103),
            new WebMotionTokenId("FILTER_PARAMETER_NAME", "character", 104),
            new WebMotionTokenId("FILTER_PARAMETER_EQUALS", "character", 105),
            new WebMotionTokenId("FILTER_PARAMETER_SEPARATOR", "character", 106),
            new WebMotionTokenId("FILTER_END", "character", 107),
            new WebMotionTokenId("FILTER_PARAMETER_VALUE", "character", 108),
            new WebMotionTokenId("FILTER_PARAMETER_VALUE_SEPARATOR", "character", 109),
            new WebMotionTokenId("FILTER_PARAMETER_VALUE_END", "character", 110),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_CONFIG", "section", 111),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_ACTIONS", "section", 112),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_ERRORS", "section", 113),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_FILTERS", "section", 114),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_EXTENSIONS", "section", 115),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_PROPERTIES", "section", 116),
            new WebMotionTokenId("EXTENSION_PATH", "character", 117),
            new WebMotionTokenId("EXTENSION_SEPARATOR", "character", 118),
            new WebMotionTokenId("EXTENSION_FILE", "character", 119),
            new WebMotionTokenId("EXTENSION_END", "character", 120),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_CONFIG", "section", 121),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_ACTIONS", "section", 122),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_ERRORS", "section", 123),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_FILTERS", "section", 124),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_EXTENSIONS", "section", 125),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_PROPERTIES", "section", 126),
            new WebMotionTokenId("PROPERTIE_NAME", "character", 127),
            new WebMotionTokenId("PROPERTIE_EQUALS", "character", 128),
            new WebMotionTokenId("PROPERTIE_VALUE", "character", 129),
            new WebMotionTokenId("PROPERTIE_END", "character", 130),
            new WebMotionTokenId("CHAR", "character", 131),
            new WebMotionTokenId("CHAR_IN_CONFIG", "character", 132),
            new WebMotionTokenId("CHAR_IN_ACTIONS", "character", 133),
            new WebMotionTokenId("CHAR_IN_ERRORS", "character", 134),
            new WebMotionTokenId("CHAR_IN_FILTERS", "character", 135),
            new WebMotionTokenId("CHAR_IN_EXTENSIONS", "character", 136),
            new WebMotionTokenId("CHAR_IN_PROPERTIES", "character", 137)
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