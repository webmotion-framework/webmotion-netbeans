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

import java.util.*;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import static org.debux.webmotion.netbeans.WebMotionLanguage.MIME_TYPE;
import static org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId.Section.*;

/**
 *
 * @author julien
 */ 
public class WebMotionLanguageHierarchy extends LanguageHierarchy<WebMotionTokenId> {

    private static List<WebMotionTokenId> tokens;
    private static Map<Integer, WebMotionTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<WebMotionTokenId>asList(new WebMotionTokenId[]{
            new WebMotionTokenId("EOF", "character", 0, NONE),
            new WebMotionTokenId("COMMENT", "comment", 1, NONE),
            new WebMotionTokenId("COMMENT_IN_CONFIG", "comment", 2, CONFIG),
            new WebMotionTokenId("COMMENT_IN_ACTIONS", "comment", 3, ACTIONS),
            new WebMotionTokenId("COMMENT_IN_ERRORS", "comment", 4, ERRORS),
            new WebMotionTokenId("COMMENT_IN_FILTERS", "comment", 5, FILTERS),
            new WebMotionTokenId("COMMENT_IN_EXTENSIONS", "comment", 6, EXTENSIONS),
            new WebMotionTokenId("COMMENT_IN_PROPERTIES", "comment", 7, PROPERTIES),
            new WebMotionTokenId("SECTION_CONFIG_NAME", "section", 8, CONFIG),
            new WebMotionTokenId("SECTION_ACTIONS_NAME", "section", 9, ACTIONS),
            new WebMotionTokenId("SECTION_ERRORS_NAME", "section", 10, ERRORS),
            new WebMotionTokenId("SECTION_FILTERS_NAME", "section", 11, FILTERS),
            new WebMotionTokenId("SECTION_EXTENSIONS_NAME", "section", 12, EXTENSIONS),
            new WebMotionTokenId("SECTION_PROPERTIES_NAME", "section", 13, PROPERTIES),
            new WebMotionTokenId("METHODS_HTTP", "keyword", 14, NONE),
            new WebMotionTokenId("METHOD_HTTP", "keyword", 15, NONE),
            new WebMotionTokenId("ACTION_BASIC_JAVA", "keyword", 16, NONE),
            new WebMotionTokenId("ACTION_JAVA", "keyword", 17, NONE),
            new WebMotionTokenId("ACTION_VIEW", "keyword", 18, NONE),
            new WebMotionTokenId("ACTION_LINK", "keyword", 19, NONE),
            new WebMotionTokenId("VARIABLE", "variable", 20, NONE),
            new WebMotionTokenId("PATTERN", "variable", 21, NONE),
            new WebMotionTokenId("IDENTIFIER", "character", 22, NONE),
            new WebMotionTokenId("QUALIFIED_IDENTIFIER", "character", 23, NONE),
            new WebMotionTokenId("NEW_LINE", "character", 24, NONE),
            new WebMotionTokenId("WHITESPACE", "character", 25, NONE),
            new WebMotionTokenId("DIGIT", "character", 26, NONE),
            new WebMotionTokenId("LETTER", "character", 27, NONE),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_CONFIG", "section", 28, CONFIG),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_ACTIONS", "section", 29, ACTIONS),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_ERRORS", "section", 30, ERRORS),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_FILTERS", "section", 31, FILTERS),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_EXTENSIONS", "section", 32, EXTENSIONS),
            new WebMotionTokenId("SECTION_CONFIG_NEXT_PROPERTIES", "section", 33, ERRORS),
            new WebMotionTokenId("CONFIG_KEY", "literal", 34, CONFIG),
            new WebMotionTokenId("CONFIG_EQUALS", "literal", 35, CONFIG),
            new WebMotionTokenId("CONFIG_VALUE", "variable", 36, CONFIG),
            new WebMotionTokenId("CONFIG_END", "character", 37, CONFIG),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_CONFIG", "section", 38, CONFIG),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_ACTIONS", "section", 39, ACTIONS),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_ERRORS", "section", 40, ERRORS),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_FILTERS", "section", 41, FILTERS),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_EXTENSIONS", "section", 42, EXTENSIONS),
            new WebMotionTokenId("SECTION_ACTIONS_NEXT_PROPERTIES", "section", 43, PROPERTIES),
            new WebMotionTokenId("ACTION_METHOD", "keyword", 44, ACTIONS),
            new WebMotionTokenId("ACTION_SEPARATOR", "character", 45, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_JAVA_BEGIN", "keyword", 46, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_VARIABLE", "variable", 47, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_IDENTIFIER", "literal", 48, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_VIEW", "keyword", 49, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_LINK", "keyword", 50, ACTIONS),
            new WebMotionTokenId("ACTION_PATH", "literal", 51, ACTIONS),
            new WebMotionTokenId("ACTION_PATH_VALUE", "literal", 52, ACTIONS),
            new WebMotionTokenId("ACTION_PATH_VARIABLE", "variable", 53, ACTIONS),
            new WebMotionTokenId("ACTION_PARAMETERS_BEGIN", "literal", 54, ACTIONS),
            new WebMotionTokenId("ACTION_PATH_END", "character", 55, ACTIONS),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_NAME", "literal", 56, ACTIONS),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_EQUALS", "literal", 57, ACTIONS),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_OTHER", "literal", 58, ACTIONS),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_SEPARATOR", "character", 59, ACTIONS),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE", "literal", 60, ACTIONS),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE_VARIABLE", "variable", 61, ACTIONS),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE_OTHER", "literal", 62, ACTIONS),
            new WebMotionTokenId("ACTION_PATH_PARAMETER_VALUE_SEPARATOR", "literal", 63, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_JAVA_QUALIFIED_IDENTIFIER", "literal", 64, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_JAVA_IDENTIFIER", "literal", 65, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_JAVA_VARIABLE", "variable", 66, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_JAVA_SEPARATOR", "literal", 67, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_JAVA_END", "character", 68, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_VIEW_VALUE", "literal", 69, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_VIEW_VARIABLE", "variable", 70, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_VIEW_SEPARATOR", "character", 71, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_VIEW_END", "character", 72, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_LINK_VALUE", "literal", 73, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_LINK_VARIABLE", "variable", 74, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_LINK_SEPARATOR", "character", 75, ACTIONS),
            new WebMotionTokenId("ACTION_ACTION_LINK_END", "character", 76, ACTIONS),
            new WebMotionTokenId("ACTION_PARAMETERS_SEPARATOR", "character", 77, ACTIONS),
            new WebMotionTokenId("ACTION_PARAMETER_NAME", "literal", 78, ACTIONS),
            new WebMotionTokenId("ACTION_PARAMETER_EQUALS", "literal", 79, ACTIONS),
            new WebMotionTokenId("ACTION_PARAMETER_SEPARATOR", "literal", 80, ACTIONS),
            new WebMotionTokenId("ACTION_END", "character", 81, ACTIONS),
            new WebMotionTokenId("ACTION_PARAMETER_VALUE", "literal", 82, ACTIONS),
            new WebMotionTokenId("ACTION_PARAMETER_VALUE_SEPARATOR", "literal", 83, ACTIONS),
            new WebMotionTokenId("ACTION_PARAMETER_VALUE_END", "character", 84, ACTIONS),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_CONFIG", "section", 85, CONFIG),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_ACTIONS", "section", 86, ACTIONS),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_ERRORS", "section", 87, ERRORS),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_FILTERS", "section", 88, FILTERS),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_EXTENSIONS", "section", 89, EXTENSIONS),
            new WebMotionTokenId("SECTION_ERRORS_NEXT_PROPERTIES", "section", 90, PROPERTIES),
            new WebMotionTokenId("ERROR_CODE", "keyword", 91, ERRORS),
            new WebMotionTokenId("ERROR_CODE_VALUE", "literal", 92, ERRORS),
            new WebMotionTokenId("ALL", "keyword", 93, ERRORS),
            new WebMotionTokenId("EXCEPTION", "literal", 94, ERRORS),
            new WebMotionTokenId("ERROR_SEPARATOR", "character", 95, ERRORS),
            new WebMotionTokenId("ERROR_ACTION_JAVA_BEGIN", "keyword", 96, ERRORS),
            new WebMotionTokenId("ERROR_ACTION_VIEW_BEGIN", "keyword", 97, ERRORS),
            new WebMotionTokenId("ERROR_ACTION_LINK_BEGIN", "keyword", 98, ERRORS),
            new WebMotionTokenId("ERROR_ACTION_JAVA", "literal", 99, ERRORS),
            new WebMotionTokenId("ERROR_END", "character", 100, ERRORS),
            new WebMotionTokenId("ERROR_ACTION_VALUE", "literal", 101, ERRORS),
            new WebMotionTokenId("ERROR_VALUE_END", "character", 102, ERRORS),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_CONFIG", "section", 103, CONFIG),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_ACTIONS", "section", 104, ACTIONS),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_ERRORS", "section", 105, ERRORS),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_FILTERS", "section", 106, FILTERS),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_EXTENSIONS", "section", 107, EXTENSIONS),
            new WebMotionTokenId("SECTION_FILTERS_NEXT_PROPERTIES", "section", 108, PROPERTIES),
            new WebMotionTokenId("FILTER_METHOD", "keyword", 109, FILTERS),
            new WebMotionTokenId("FILTER_PATH", "literal", 110, FILTERS),
            new WebMotionTokenId("FILTER_PATH_ALL", "literal", 111, FILTERS),
            new WebMotionTokenId("FILTER_SEPARATOR", "character", 112, FILTERS),
            new WebMotionTokenId("FILTER_ACTION_BEGIN", "keyword", 113, FILTERS),
            new WebMotionTokenId("FILTER_ACTION", "literal", 114, FILTERS),
            new WebMotionTokenId("FILTER_PARAMETERS_SEPARATOR", "character", 115, FILTERS),
            new WebMotionTokenId("FILTER_PARAMETER_NAME", "literal", 116, FILTERS),
            new WebMotionTokenId("FILTER_PARAMETER_EQUALS", "literal", 117, FILTERS),
            new WebMotionTokenId("FILTER_PARAMETER_SEPARATOR", "literal", 118, FILTERS),
            new WebMotionTokenId("FILTER_END", "character", 119, FILTERS),
            new WebMotionTokenId("FILTER_PARAMETER_VALUE", "literal", 120, FILTERS),
            new WebMotionTokenId("FILTER_PARAMETER_VALUE_SEPARATOR", "character", 121, FILTERS),
            new WebMotionTokenId("FILTER_PARAMETER_VALUE_END", "character", 122, FILTERS),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_CONFIG", "section", 123, CONFIG),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_ACTIONS", "section", 124, ACTIONS),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_ERRORS", "section", 125, ERRORS),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_FILTERS", "section", 126, FILTERS),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_EXTENSIONS", "section", 127, EXTENSIONS),
            new WebMotionTokenId("SECTION_EXTENSIONS_NEXT_PROPERTIES", "section", 128, PROPERTIES),
            new WebMotionTokenId("EXTENSION_PATH", "literal", 129, EXTENSIONS),
            new WebMotionTokenId("EXTENSION_SEPARATOR", "character", 130, EXTENSIONS),
            new WebMotionTokenId("EXTENSION_FILE", "literal", 131, EXTENSIONS),
            new WebMotionTokenId("EXTENSION_END", "character", 132, EXTENSIONS),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_CONFIG", "section", 133, CONFIG),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_ACTIONS", "section", 134, ACTIONS),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_ERRORS", "section", 135, ERRORS),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_FILTERS", "section", 136, FILTERS),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_EXTENSIONS", "section", 137, EXTENSIONS),
            new WebMotionTokenId("SECTION_PROPERTIES_NEXT_PROPERTIES", "section", 138, PROPERTIES),
            new WebMotionTokenId("PROPERTIE_NAME", "literal", 139, PROPERTIES),
            new WebMotionTokenId("PROPERTIE_EQUALS", "literal", 140, PROPERTIES),
            new WebMotionTokenId("PROPERTIE_VALUE", "variable", 141, PROPERTIES),
            new WebMotionTokenId("PROPERTIE_END", "character", 142, PROPERTIES),
            new WebMotionTokenId("CHAR", "character", 143, NONE),
            new WebMotionTokenId("CHAR_IN_CONFIG", "character", 144, CONFIG),
            new WebMotionTokenId("CHAR_IN_CONFIG_VALUE", "character", 145, CONFIG),
            new WebMotionTokenId("CHAR_IN_ACTIONS", "character", 146, ACTIONS),
            new WebMotionTokenId("CHAR_IN_ACTIONS_PATH", "character", 147, ACTIONS),
            new WebMotionTokenId("CHAR_IN_ACTIONS_PATH_PARAMETERS", "character", 148, ACTIONS),
            new WebMotionTokenId("CHAR_IN_ACTIONS_PATH_PARAMETER_VALUE", "character", 149, ACTIONS),
            new WebMotionTokenId("CHAR_IN_ACTIONS_ACTION_JAVA", "character", 150, ACTIONS),
            new WebMotionTokenId("CHAR_IN_ACTIONS_ACTION_VIEW", "character", 151, ACTIONS),
            new WebMotionTokenId("CHAR_IN_ACTIONS_ACTION_LINK", "character", 152, ACTIONS),
            new WebMotionTokenId("CHAR_IN_ACTIONS_PARAMETERS", "character", 153, ACTIONS),
            new WebMotionTokenId("CHAR_IN_ACTIONS_PARAMETER_VALUE", "character", 154, ACTIONS),
            new WebMotionTokenId("CHAR_IN_ERRORS", "character", 155, ERRORS),
            new WebMotionTokenId("CHAR_IN_ERRORS_ACTIONS", "character", 156, ERRORS),
            new WebMotionTokenId("CHAR_IN_ERRORS_ACTIONS_VALUE", "character", 157, ERRORS),
            new WebMotionTokenId("CHAR_IN_FILTERS", "character", 158, FILTERS),
            new WebMotionTokenId("CHAR_IN_FILTERS_PARAMETERS", "character", 159, FILTERS),
            new WebMotionTokenId("CHAR_IN_FILTERS_PARAMETER_VALUE", "character", 160, FILTERS),
            new WebMotionTokenId("CHAR_IN_EXTENSIONS", "character", 161, EXTENSIONS),
            new WebMotionTokenId("CHAR_IN_EXTENSIONS_FILE", "character", 162, EXTENSIONS),
            new WebMotionTokenId("CHAR_IN_PROPERTIES", "character", 163, PROPERTIES),
            new WebMotionTokenId("CHAR_IN_PROPERTIES_VALUE", "character", 164, PROPERTIES)
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
        return MIME_TYPE;
    }

}