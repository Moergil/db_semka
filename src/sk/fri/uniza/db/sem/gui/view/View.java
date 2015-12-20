package sk.fri.uniza.db.sem.gui.view;

import sk.fri.uniza.db.sem.Application;

import javax.swing.*;

public interface View {

    Application getApplication();

    String getTitle();

    void onShow();
    void onHide();

}
