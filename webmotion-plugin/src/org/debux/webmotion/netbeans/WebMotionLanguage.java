package org.debux.webmotion.netbeans;

import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;

/**
 *
 * @author julien
 */
@LanguageRegistration(mimeType = "text/x-wm")
public class WebMotionLanguage extends DefaultLanguageConfig {

    @Override
    public Language getLexerLanguage() {
        return WebMotionTokenId.getLanguage();
    }

    @Override
    public String getDisplayName() {
        return "wm";
    }

}
