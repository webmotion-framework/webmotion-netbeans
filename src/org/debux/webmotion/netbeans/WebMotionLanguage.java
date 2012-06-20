package org.debux.webmotion.netbeans;

import org.debux.webmotion.netbeans.hints.WebMotionHintsProvider;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.api.HintsProvider;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;

/**
 *
 * @author julien
 */
@LanguageRegistration(mimeType = "text/x-wm")
public class WebMotionLanguage extends DefaultLanguageConfig {

    @Override
    public Language<WebMotionTokenId> getLexerLanguage() {
        return WebMotionTokenId.getLanguage();
    }

    @Override
    public String getDisplayName() {
        return "WebMotion";
    }

    @Override
    public boolean hasHintsProvider() {
        return true;
    }

    @Override
    public HintsProvider getHintsProvider() {
        return new WebMotionHintsProvider();
    }
}
