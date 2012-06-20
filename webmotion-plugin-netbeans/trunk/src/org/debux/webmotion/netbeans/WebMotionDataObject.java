package org.debux.webmotion.netbeans;

import java.io.IOException;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import static org.debux.webmotion.netbeans.WebMotionLanguage.MIME_TYPE;

public class WebMotionDataObject extends MultiDataObject {

    public WebMotionDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor(MIME_TYPE, true);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @MultiViewElement.Registration(displayName = "#editor_view_name",
                                    iconBase = "org/debux/webmotion/netbeans/icon.png",
                                    mimeType = MIME_TYPE,
                                    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
                                    preferredID = "WebMotionDataObject",
                                    position = 1000)
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }
}
