package org.debux.webmotion.netbeans.javacc;

import org.debux.webmotion.netbeans.javacc.WebMotionTokenManager;
import org.debux.webmotion.netbeans.javacc.WebMotionTokenManager;
import org.debux.webmotion.netbeans.javacc.WebMotionTokenManager;
import org.debux.webmotion.netbeans.javacc.WebMotionTokenManager;
import org.debux.webmotion.netbeans.javacc.WebMotionTokenManager;
import org.debux.webmotion.netbeans.javacc.WebMotionTokenManager;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 *
 * @author julien
 */
public class WebMotionLexer implements Lexer<WebMotionTokenId> {

    private LexerRestartInfo<WebMotionTokenId> info;
    private WebMotionTokenManager webmotionTokenManager;

    WebMotionLexer(LexerRestartInfo<WebMotionTokenId> info) {
        this.info = info;
        SimpleCharStream stream = new SimpleCharStream(info.input());
        webmotionTokenManager = new WebMotionTokenManager(stream);
    }

    @Override
    public org.netbeans.api.lexer.Token<WebMotionTokenId> nextToken() {
        Token token = webmotionTokenManager.getNextToken();
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