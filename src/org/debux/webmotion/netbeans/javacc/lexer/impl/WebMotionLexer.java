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