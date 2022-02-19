package org.primefaces.barcelona.view;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class ThemeView implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @PostConstruct
    public void init() {
        this.color = "duran";
    }
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void change(String color) {
        if (color.equals("duran")) {
            this.color = null;
        } else {
            this.color = "-" + color;
        }
    }

}
