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
            new WebMotionTokenId("METHODS_HTTP", "keyword", 14),
            new WebMotionTokenId("METHOD_HTTP", "keyword", 15),
            new WebMotionTokenId("ACTION_BASIC_JAVA", "keyword", 16),
            new WebMotionTokenId("ACTION_JAVA", "keyword", 17),
            new WebMotionTokenId("ACTION_VIEW", "keyword", 18),
            new WebMotionTokenId("ACTION_LINK", "keyword", 19),
            new WebMotionTokenId("VARIABLE", "variable", 20),
            new WebMotionTokenId("PATTERN", "variable", 21),
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
            new WebMotionTokenId("CONFIG_KEY", "literal", 34),
            new WebMotionTokenId("CONFIG_EQUALS", "literal", 35),
            new WebMotionTokenId("CONFIG_VALUE", "variable", 36),
            new WebMotionTokenId("CONFIG_END", "character", 37),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_CONFIG", "section", 38),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_ACTIONS", "section", 39),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_ERRORS", "section", 40),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_FILTERS", "section", 41),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_EXTENSIONS", "section", 42),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_PROPERTIES", "section", 43),
            new WebMotionTokenId("ACTION_METHOD", "keyword", 44),
            new WebMotionTokenId("ACTION_SEPARATOR", "character", 45),
            new WebMotionTokenId("ACTION_ACTION_JAVA_BEGIN", "keyword", 46),
            new WebMotionTokenId("ACTION_ACTION_VARIABLE", "variable", 47),
            new WebMotionTokenId("ACTION_ACTION_IDENTIFIER", "literal", 48),
            new WebMotionTokenId("ACTION_ACTION_VIEW", "keyword", 49),
            new WebMotionTokenId("ACTION_ACTION_LINK", "keyword", 50),
            new WebMotionTokenId("ACTION_PATH", "literal", 51),
            new WebMotionTokenId("ACTION_PATH_VALUE", "literal", 52),
            new WebMotionTokenId("ACTION_PATH_VARIABLE", "variable", 53),
            new WebMotionTokenId("ACTION_PARAMETERS_BEGIN", "literal", 54),
            new WebMotionTokenId("ACTION_PATH_END", "character", 55),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_NAME", "literal", 56),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_EQUALS", "literal", 57),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_OTHER", "literal", 58),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_SEPARATOR", "character", 59),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE", "literal", 60),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE_VARIABLE", "variable", 61),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE_OTHER", "literal", 62),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE_SEPARATOR", "literal", 63),
            new WebMotionTokenId("ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER", "literal", 64),
            new WebMotionTokenId("ACTION_ACTION_JAVA_IDENTIFIER", "literal", 65),
            new WebMotionTokenId("ACTION_ACTION_JAVA_VARIABLE", "variable", 66),
            new WebMotionTokenId("ACTION_ACTION_JAVA_SEPARATOR", "literal", 67),
            new WebMotionTokenId("ACTION_ACTION_JAVA_END", "character", 68),
            new WebMotionTokenId("ACTION_ACTION_VIEW_VALUE", "literal", 69),
            new WebMotionTokenId("ACTION_ACTION_VIEW_VARIABLE", "variable", 70),
            new WebMotionTokenId("ACTION_ACTION_VIEW_SEPARATOR", "character", 71),
            new WebMotionTokenId("ACTION_ACTION_VIEW_END", "character", 72),
            new WebMotionTokenId("ACTION_ACTION_LINK_VALUE", "literal", 73),
            new WebMotionTokenId("ACTION_ACTION_LINK_VARIABLE", "variable", 74),
            new WebMotionTokenId("ACTION_ACTION_LINK_SEPARATOR", "character", 75),
            new WebMotionTokenId("ACTION_ACTION_LINK_END", "character", 76),
            new WebMotionTokenId("ACTION_PARAMETERS_SEPARATOR", "character", 77),
            new WebMotionTokenId("ACTION_PARAMETER_NAME", "literal", 78),
            new WebMotionTokenId("ACTION_PARAMETER_EQUALS", "literal", 79),
            new WebMotionTokenId("ACTION_PARAMETER_SEPARATOR", "literal", 80),
            new WebMotionTokenId("ACTION_END", "character", 81),
            new WebMotionTokenId("ACTION_PARAMETER_VALUE", "literal", 82),
            new WebMotionTokenId("ACTION_PARAMETER_VALUE_SEPARATOR", "literal", 83),
            new WebMotionTokenId("ACTION_PARAMETER_VALUE_END", "character", 84),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_CONFIG", "section", 85),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_ACTIONS", "section", 86),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_ERRORS", "section", 87),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_FILTERS", "section", 88),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_EXTENSIONS", "section", 89),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_PROPERTIES", "section", 90),
            new WebMotionTokenId("ERROR_CODE", "keyword", 91),
            new WebMotionTokenId("ERROR_CODE_VALUE", "literal", 92),
            new WebMotionTokenId("ALL", "keyword", 93),
            new WebMotionTokenId("EXCEPTION", "literal", 94),
            new WebMotionTokenId("ERROR_SEPARATOR", "character", 95),
            new WebMotionTokenId("ERROR_ACTION_JAVA_BEGIN", "keyword", 96),
            new WebMotionTokenId("ERROR_ACTION_VIEW_BEGIN", "keyword", 97),
            new WebMotionTokenId("ERROR_ACTION_LINK_BEGIN", "keyword", 98),
            new WebMotionTokenId("ERROR_ACTION_JAVA", "literal", 99),
            new WebMotionTokenId("ERROR_END", "character", 100),
            new WebMotionTokenId("ERROR_ACTION_VALUE", "literal", 101),
            new WebMotionTokenId("ERROR_VALUE_END", "character", 102),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_CONFIG", "section", 103),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_ACTIONS", "section", 104),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_ERRORS", "section", 105),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_FILTERS", "section", 106),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_EXTENSIONS", "section", 107),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_PROPERTIES", "section", 108),
            new WebMotionTokenId("FILTER_METHOD", "keyword", 109),
            new WebMotionTokenId("FILTER_PATH", "literal", 110),
            new WebMotionTokenId("FILTER_PATH_ALL", "literal", 111),
            new WebMotionTokenId("FILTER_SEPARATOR", "character", 112),
            new WebMotionTokenId("FILTER_ACTION_BEGIN", "keyword", 113),
            new WebMotionTokenId("FILTER_ACTION", "literal", 114),
            new WebMotionTokenId("FILTER_PARAMETERS_SEPARATOR", "character", 115),
            new WebMotionTokenId("FILTER_PARAMETER_NAME", "literal", 116),
            new WebMotionTokenId("FILTER_PARAMETER_EQUALS", "literal", 117),
            new WebMotionTokenId("FILTER_PARAMETER_SEPARATOR", "literal", 118),
            new WebMotionTokenId("FILTER_END", "character", 119),
            new WebMotionTokenId("FILTER_PARAMETER_VALUE", "literal", 120),
            new WebMotionTokenId("FILTER_PARAMETER_VALUE_SEPARATOR", "character", 121),
            new WebMotionTokenId("FILTER_PARAMETER_VALUE_END", "character", 122),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_CONFIG", "section", 123),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_ACTIONS", "section", 124),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_ERRORS", "section", 125),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_FILTERS", "section", 126),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_EXTENSIONS", "section", 127),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_PROPERTIES", "section", 128),
            new WebMotionTokenId("EXTENSION_PATH", "literal", 129),
            new WebMotionTokenId("EXTENSION_SEPARATOR", "character", 130),
            new WebMotionTokenId("EXTENSION_FILE", "literal", 131),
            new WebMotionTokenId("EXTENSION_END", "character", 132),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_CONFIG", "section", 133),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_ACTIONS", "section", 134),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_ERRORS", "section", 135),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_FILTERS", "section", 136),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_EXTENSIONS", "section", 137),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_PROPERTIES", "section", 138),
            new WebMotionTokenId("PROPERTIE_NAME", "literal", 139),
            new WebMotionTokenId("PROPERTIE_EQUALS", "literal", 140),
            new WebMotionTokenId("PROPERTIE_VALUE", "variable", 141),
            new WebMotionTokenId("PROPERTIE_END", "character", 142),
            new WebMotionTokenId("CHAR", "character", 143),
            new WebMotionTokenId("CHAR_IN_CONFIG", "character", 144),
            new WebMotionTokenId("CHAR_IN_CONFIG_VALUE", "character", 145),
            new WebMotionTokenId("CHAR_IN_ACTIONS", "character", 146),
            new WebMotionTokenId("CHAR_IN_ACTIONS_PATH", "character", 147),
            new WebMotionTokenId("CHAR_IN_ACTIONS_PATH_PARAMETERS", "character", 148),
            new WebMotionTokenId("CHAR_IN_ACTIONS_PATH_PARAMETER_VALUE", "character", 149),
            new WebMotionTokenId("CHAR_IN_ACTIONS_ACTION_JAVA", "character", 150),
            new WebMotionTokenId("CHAR_IN_ACTIONS_ACTION_VIEW", "character", 151),
            new WebMotionTokenId("CHAR_IN_ACTIONS_ACTION_LINK", "character", 152),
            new WebMotionTokenId("CHAR_IN_ACTIONS_PARAMETERS", "character", 153),
            new WebMotionTokenId("CHAR_IN_ACTIONS_PARAMETER_VALUE", "character", 154),
            new WebMotionTokenId("CHAR_IN_ERRORS", "character", 155),
            new WebMotionTokenId("CHAR_IN_ERRORS_ACTIONS", "character", 156),
            new WebMotionTokenId("CHAR_IN_ERRORS_ACTIONS_VALUE", "character", 157),
            new WebMotionTokenId("CHAR_IN_FILTERS", "character", 158),
            new WebMotionTokenId("CHAR_IN_FILTERS_PARAMETERS", "character", 159),
            new WebMotionTokenId("CHAR_IN_FILTERS_PARAMETER_VALUE", "character", 160),
            new WebMotionTokenId("CHAR_IN_EXTENSIONS", "character", 161),
            new WebMotionTokenId("CHAR_IN_EXTENSIONS_FILE", "character", 162),
            new WebMotionTokenId("CHAR_IN_PROPERTIES", "character", 163),
            new WebMotionTokenId("CHAR_IN_PROPERTIES_VALUE", "character", 164)
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