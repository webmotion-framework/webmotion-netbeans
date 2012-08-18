package org.debux.webmotion.netbeans.refactoring;

import java.io.IOException;
import javax.swing.event.ChangeListener;
import org.netbeans.modules.csl.api.ElementKind;
import org.netbeans.modules.refactoring.api.AbstractRefactoring;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.api.RenameRefactoring;
import org.netbeans.modules.refactoring.spi.ui.CustomRefactoringPanel;
import org.netbeans.modules.refactoring.spi.ui.RefactoringUI;
import org.netbeans.modules.refactoring.spi.ui.RefactoringUIBypass;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;

/**
 * 
 * @author julien
 */
public class WebMotionRenameRefactoringUI implements RefactoringUI, RefactoringUIBypass {

    private final AbstractRefactoring refactoring;
    private final String name;
    private RenamePanel panel;

    public WebMotionRenameRefactoringUI(WebMotionRefactoringActions.RefactoringContext handle) {
        this.name = handle.getValue();
        this.refactoring = new RenameRefactoring(Lookups.singleton(handle));
//        this.refactoring.getContext().add(UI.Constants.REQUEST_PREVIEW);
    }

    static String getElementName(final String name, final ElementKind kind) {
        String retval = name;
        if (kind.equals(ElementKind.VARIABLE) || kind.equals(ElementKind.FIELD)) {
            while (retval.length() > 1 && retval.startsWith("$")) {//NOI18N
                retval = retval.substring(1);
            }
        }
        return retval;
    }

    @Override
    public String getName() {
        return NbBundle.getMessage(WebMotionRenameRefactoringUI.class, "LBL_Rename"); //NOI18N
    }

    @Override
    public String getDescription() {
        return NbBundle.getMessage(WebMotionRenameRefactoringUI.class, "LBL_Rename_Descr"); //NOI18N
    }

    @Override
    public boolean isQuery() {
        return false;
    }

    @Override
    public CustomRefactoringPanel getPanel(ChangeListener parent) {
        if (panel == null) {
            panel = new RenamePanel(name, parent, NbBundle.getMessage(RenamePanel.class, "LBL_Rename"), true, true); //NOI18N
        }
        return panel;
    }

    @Override
    public Problem setParameters() {
        String newName = panel.getNameValue();
        if (refactoring instanceof RenameRefactoring) {
            ((RenameRefactoring) refactoring).setNewName(newName);
        }
        return refactoring.checkParameters();
    }

    @Override
    public Problem checkParameters() {
        if (!panel.isUpdateReferences()) {
            return null;
        }
        if (refactoring instanceof RenameRefactoring) {
            ((RenameRefactoring) refactoring).setNewName(panel.getNameValue());
        }
        return refactoring.checkParameters();
    }

    @Override
    public boolean hasParameters() {
        return true;
    }

    @Override
    public AbstractRefactoring getRefactoring() {
        return this.refactoring;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return new HelpCtx(WebMotionRenameRefactoringUI.class);
    }

    @Override
    public boolean isRefactoringBypassRequired() {
        return !panel.isUpdateReferences();
    }
    
    @Override
    public void doRefactoringBypass() throws IOException {
        DataObject dob = DataObject.find(refactoring.getRefactoringSource().lookup(FileObject.class));
        dob.rename(panel.getNameValue());
    }

}
