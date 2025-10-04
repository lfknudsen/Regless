package com.falkknudsen.regless;

public class Controller {
    private View view;
    private Model model;

    public Controller(View v, Model m) {
        this.view = v;
        this.model = m;
    }
}
