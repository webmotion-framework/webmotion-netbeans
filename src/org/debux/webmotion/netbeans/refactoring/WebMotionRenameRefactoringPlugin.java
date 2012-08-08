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

import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;
import org.netbeans.api.java.classpath.GlobalPathRegistry;
import org.netbeans.modules.refactoring.api.Problem;
import org.netbeans.modules.refactoring.api.RenameRefactoring;
import org.netbeans.modules.refactoring.spi.RefactoringElementsBag;
import org.netbeans.modules.refactoring.spi.RefactoringPlugin;
import org.netbeans.modules.refactoring.spi.SimpleRefactoringElementImplementation;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.CloneableEditorSupport;
import org.openide.text.PositionBounds;
import org.openide.text.PositionRef;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**
 *
 * @author julien
 */
class WebMotionRenameRefactoringPlugin implements RefactoringPlugin {

    protected RenameRefactoring refactoring;
    
    public WebMotionRenameRefactoringPlugin(RenameRefactoring refactoring) {
        this.refactoring = refactoring;
    }

    @Override
    public Problem preCheck() {
        return null;
    }

    @Override
    public Problem checkParameters() {
        return null;
    }

    @Override
    public Problem fastCheckParameters() {
        return null;
    }

    @Override
    public void cancelRequest() {
    }

    @Override
    public Problem prepare(RefactoringElementsBag refactoringElements) {
        refactoringElements.add(refactoring, new MappingRenameRefactoringElement());
        return null;
    }
    
    public class MappingRenameRefactoringElement extends SimpleRefactoringElementImplementation {

        protected FileObject fo;
        protected DataObject dob;
        
        public MappingRenameRefactoringElement() {
            try {
                GlobalPathRegistry registry = GlobalPathRegistry.getDefault();
                fo = registry.findResource("mapping");
                dob = DataObject.find(fo);
                
            } catch (DataObjectNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
        @Override
        public void performChange() {
            try {
                System.out.println(">>>>> performChange");
                
                EditorCookie lookup = dob.getLookup().lookup(EditorCookie.class);
                StyledDocument doc = lookup.getDocument();
                
                doc.remove(10, 15);
                doc.insertString(10, refactoring.getNewName(), null);
                
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        @Override
        public String getText() {
            return "getText";
        }

        @Override
        public String getDisplayText() {
            return "getDisplayText";
        }

        @Override
        public Lookup getLookup() {
            return Lookup.EMPTY;
        }

        @Override
        public FileObject getParentFile() {
            return fo;
        }

        @Override
        public PositionBounds getPosition() {
            CloneableEditorSupport open = (CloneableEditorSupport) dob.getLookup().lookup(EditorCookie.class);

            PositionRef startPosition = open.createPositionRef(10, Position.Bias.Forward);
            PositionRef endPosition = open.createPositionRef(25, Position.Bias.Backward);
            return new PositionBounds(startPosition, endPosition);
        }
    }
    
}
