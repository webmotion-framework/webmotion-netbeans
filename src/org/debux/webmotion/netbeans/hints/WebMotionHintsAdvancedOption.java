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

import org.netbeans.modules.csl.api.HintsProvider;
import org.netbeans.modules.csl.api.HintsProvider.HintsManager;
import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;
import static org.debux.webmotion.netbeans.WebMotionLanguage.MIME_TYPE;

/**
 *
 * @author julien
 */
public class WebMotionHintsAdvancedOption extends AdvancedOption {

    OptionsPanelController panelController;
    
    @Override
    public String getDisplayName() {
        return "Hints";
    }

    @Override
    public String getTooltip() {
        return "Hints And Warnings for WebMotion";
    }

    @Override
    public OptionsPanelController create() {
        if ( panelController == null ) {
            HintsManager manager = HintsProvider.HintsManager.getManagerForMimeType(MIME_TYPE);
            assert manager != null;
            panelController = manager.getOptionsController();
        }

        return panelController;
    }
    
    //TODO: temporary solution, this should be solved on GSF level
    public static  OptionsPanelController createStatic(){
        return new WebMotionHintsAdvancedOption().create();
    }
}
