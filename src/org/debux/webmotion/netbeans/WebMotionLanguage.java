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

    @Override
    public String getLineCommentPrefix() {
        return "#";
    }

}
