package org.debux.webmotion.netbeans;

import javax.swing.text.Document;
import org.debux.webmotion.netbeans.hints.WebMotionHintsProvider;
import org.debux.webmotion.netbeans.javacc.lexer.impl.WebMotionTokenId;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.api.HintsProvider;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;
import static org.debux.webmotion.netbeans.WebMotionLanguage.MIME_TYPE;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

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
    
    static public FileObject getFO(Document doc) {
        Object sdp = doc.getProperty(Document.StreamDescriptionProperty);
        if (sdp instanceof FileObject) {
            return (FileObject) sdp;
        }
        if (sdp instanceof DataObject) {
            DataObject dobj = (DataObject) sdp;
            return dobj.getPrimaryFile();
        }
        return null;
    }
}
