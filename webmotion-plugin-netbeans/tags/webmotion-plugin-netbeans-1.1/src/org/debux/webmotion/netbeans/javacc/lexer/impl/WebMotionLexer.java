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

import org.debux.webmotion.netbeans.javacc.lexer.Token;
import org.debux.webmotion.netbeans.javacc.lexer.WebMotionParserTokenManager;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author julien
 */
public class WebMotionLexer implements Lexer<WebMotionTokenId> {

    protected LexerRestartInfo<WebMotionTokenId> info;
    protected WebMotionParserTokenManager tokenManager;
    protected int lexerState;
    
    WebMotionLexer(LexerRestartInfo<WebMotionTokenId> info) {
        SimpleCharStream stream = new SimpleCharStream(info.input());
        if (info.state() != null) {
            tokenManager = new WebMotionParserTokenManager(stream, (Integer) info.state());
        } else {
            tokenManager = new WebMotionParserTokenManager(stream);
        }
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<WebMotionTokenId> nextToken() {
        Token token = tokenManager.getNextToken();
        lexerState = tokenManager.curLexState;
        if (info.input().readLength() < 1) {
            return null;
        }
        return info.tokenFactory().createToken(WebMotionLanguageHierarchy.getToken(token.kind));
    }

    @Override
    public Object state() {
        return lexerState;
    }

    @Override
    public void release() {
    }

}