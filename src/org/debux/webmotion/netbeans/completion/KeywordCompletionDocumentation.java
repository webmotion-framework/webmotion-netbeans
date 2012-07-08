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
package org.debux.webmotion.netbeans.completion;

import java.net.URL;
import java.util.MissingResourceException;
import javax.swing.Action;
import org.debux.webmotion.netbeans.WebMotionLanguage;
import org.netbeans.spi.editor.completion.CompletionDocumentation;
import org.openide.util.NbBundle;

/**
 *
 * @author julien
 */
public class KeywordCompletionDocumentation implements CompletionDocumentation {
    private WebMotionCompletionItem item;

    public KeywordCompletionDocumentation(WebMotionCompletionItem item) {
        this.item = item;
    }

    @Override
    public String getText() {
        try {
            return NbBundle.getMessage(WebMotionLanguage.class, "completion_" + item.getSortText());
        } catch (MissingResourceException ex) {
            return "No information about " + item.getSortText();
        }
    }

    @Override
    public URL getURL() {
        return null;
    }

    @Override
    public CompletionDocumentation resolveLink(String string) {
        return null;
    }

    @Override
    public Action getGotoSourceAction() {
        return null;
    }
    
}
