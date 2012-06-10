package org.debux.webmotion.netbeans;

import java.io.IOException;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject.Registration;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;

@Registration(displayName = "#editor_name", iconBase = "org/debux/webmotion/netbeans/icon.png", mimeType = "text/x-wm")
public class WebMotionDataObject extends MultiDataObject {

    public WebMotionDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        registerEditor("text/x-wm", true);
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

    @MultiViewElement.Registration(displayName = "#editor_view_name",
                                    iconBase = "org/debux/webmotion/netbeans/icon.png",
                                    mimeType = "text/x-wm",
                                    persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
                                    preferredID = "WebMotionDataObject",
                                    position = 1000)
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
    }
}
