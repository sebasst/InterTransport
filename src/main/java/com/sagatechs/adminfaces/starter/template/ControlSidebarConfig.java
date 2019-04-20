package com.sagatechs.adminfaces.starter.template;

import java.io.Serializable;

public class ControlSidebarConfig implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Boolean showOnMobile;
    private final Boolean fixedLayout;
    private final Boolean boxedLayout;
    private final Boolean expandOnHover;
    private final Boolean sidebarCollapsed;
    private final Boolean fixed;
    private final Boolean darkSkin;


    public ControlSidebarConfig(boolean showOnMobile, boolean fixedLayout, boolean boxedLayout, boolean expandOnHover, boolean sidebarCollapsed, boolean fixed, boolean darkSkin) {
         this.showOnMobile = showOnMobile;
         this.fixedLayout =fixedLayout;
         this.boxedLayout = boxedLayout;
         this.expandOnHover = expandOnHover;
         this.sidebarCollapsed = sidebarCollapsed;
         this.fixed = fixed;
         this.darkSkin = darkSkin;
    }

    public Boolean getShowOnMobile() {
        return showOnMobile;
    }

    public Boolean getFixedLayout() {
        return fixedLayout;
    }

    public Boolean getBoxedLayout() {
        return boxedLayout;
    }

    public Boolean getExpandOnHover() {
        return expandOnHover;
    }

    public Boolean getSidebarCollapsed() {
        return sidebarCollapsed;
    }

    public Boolean getFixed() {
        return fixed;
    }

    public Boolean getDarkSkin() {
        return darkSkin;
    }
    
}
