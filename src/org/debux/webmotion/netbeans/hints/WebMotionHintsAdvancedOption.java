package org.debux.webmotion.netbeans.hints;

import org.netbeans.modules.csl.api.HintsProvider;
import org.netbeans.modules.csl.api.HintsProvider.HintsManager;
import org.netbeans.spi.options.AdvancedOption;
import org.netbeans.spi.options.OptionsPanelController;

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
            HintsManager manager = HintsProvider.HintsManager.getManagerForMimeType("text/x-wm");
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
