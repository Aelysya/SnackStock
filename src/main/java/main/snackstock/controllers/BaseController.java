package main.snackstock.controllers;

import main.snackstock.MainController;

public class BaseController {

    protected MainController mainController;

    public void setMainController(MainController mc){
        this.mainController = mc;
    }
}
