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
