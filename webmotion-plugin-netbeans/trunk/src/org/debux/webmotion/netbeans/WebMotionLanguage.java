package org.debux.webmotion.netbeans;

import org.debux.webmotion.netbeans.hints.WebMotionHintsProvider;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.api.HintsProvider;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import static org.debux.webmotion.netbeans.WebMotionLanguage.MIME_TYPE;

/**
 *
 * @author julien
 */
@LanguageRegistration(mimeType = MIME_TYPE)
public class WebMotionLanguage extends DefaultLanguageConfig {

    public static final String MIME_TYPE = "text/x-wm";
    
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
