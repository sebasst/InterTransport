package com.sagatechs.adminfaces.starter.template;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import static com.sagatechs.adminfaces.starter.util.Assert.has;

@Named
@SessionScoped
public class SkinMB implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String skin;

    @Inject
    private AdminConfig adminConfig;

    @PostConstruct
    public void init() {
        skin = adminConfig.getSkin();
        if(!has(skin)) {
            skin = "skin-blue";
        }
    }


    public void changeSkin(String skin){
        this.skin = skin;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }
}
