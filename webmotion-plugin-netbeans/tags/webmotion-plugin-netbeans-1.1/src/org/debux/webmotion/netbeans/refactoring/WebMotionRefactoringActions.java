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
package org.debux.webmotion.netbeans.refactoring;

import org.netbeans.modules.refactoring.spi.ui.ActionsImplementationProvider;
import org.netbeans.modules.refactoring.spi.ui.UI;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

/**
 *
 * @author julien
 */
//@ServiceProvider(service = ActionsImplementationProvider.class)
public class WebMotionRefactoringActions extends ActionsImplementationProvider {

    @Override
    public boolean canRename(Lookup lookup) {
        FileObject fo = lookup.lookup(FileObject.class);
        return fo != null;
    }

    @Override
    public void doRename(Lookup lookup) {
        final FileObject fo = lookup.lookup(FileObject.class);
        
        new Runnable() {
            @Override
            public void run() {
                UI.openRefactoringUI(new WebMotionRenameRefactoringUI(fo));
            }
        }.run();
    }
    
}
