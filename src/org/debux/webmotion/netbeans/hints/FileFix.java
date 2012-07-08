package org.debux.webmotion.netbeans.hints;

import org.netbeans.modules.csl.api.HintFix;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 *
 * @author julien
 */
public class FileFix implements HintFix {

    protected FileObject fileObject;
    protected String name;

    public FileFix(FileObject fileObject, String name) {
        this.fileObject = fileObject;
        this.name = name;
    }

    @Override
    public String getDescription() {
        return "Create new file";
    }

    @Override
    public void implement() throws Exception {
        FileObject parent = fileObject.getParent();
        parent.createData(name);
    }

    @Override
    public boolean isSafe() {
        return false;
    }

    @Override
    public boolean isInteractive() {
        return false;
    }
    
}
