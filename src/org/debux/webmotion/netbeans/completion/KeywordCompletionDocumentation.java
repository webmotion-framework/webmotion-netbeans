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
