package org.debux.webmotion.netbeans.javacc;

import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author julien
 */
public class WebMotionLexer implements Lexer<WebMotionTokenId> {

    private LexerRestartInfo<WebMotionTokenId> info;
    private WebMotionParserTokenManager tokenManager;

    WebMotionLexer(LexerRestartInfo<WebMotionTokenId> info) {
        this.info = info;
        SimpleCharStream stream = new SimpleCharStream(info.input());
        tokenManager = new WebMotionParserTokenManager(stream);
    }

    @Override
    public org.netbeans.api.lexer.Token<WebMotionTokenId> nextToken() {
        Token token = tokenManager.getNextToken();
        if (info.input().readLength() < 1) {
            return null;
        }
        return info.tokenFactory().createToken(WebMotionLanguageHierarchy.getToken(token.kind));
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }

}