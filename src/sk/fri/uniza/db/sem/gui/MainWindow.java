package sk.fri.uniza.db.sem.gui;

import sk.fri.uniza.db.sem.Application;
import sk.fri.uniza.db.sem.gui.view.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.Supplier;

public class MainWindow {

    private static final Dimension MIN_SIZE = new Dimension(800, 400);

    private static final String TITLE = "Daňový úrad";

    private final Application application;

    private final JFrame window;

    private final Container contentPane;

    public MainWindow(Application application) {
        this.application = application;

        window = new JFrame(TITLE);

        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                application.getDataProvider().close();
            }
        });

        contentPane = window.getContentPane();
        contentPane.setLayout(new BorderLayout());

        window.setMinimumSize(MIN_SIZE);
        window.pack();

        window.setLocationRelativeTo(null);

        setupMenu();
    }

    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu listingsMenu = new JMenu("Výpisy");

        JMenuItem menuItem;

        addMenuItem(listingsMenu, () -> new ListTaxPayingPeriodsView(application, "Úrad / Dátum / Počet"));
        addMenuItem(listingsMenu, () -> new ListIncomeCompositionsView(application, "Zloženie príjmov"));
        addMenuItem(listingsMenu, () -> new ListTaxPayersWhoDidntPayedView(application, "Právnické osoby - neplatiči"));
        addMenuItem(listingsMenu, () -> new ListCompanies(application, "Spoločnosti"));
        addMenuItem(listingsMenu, () -> new ListCompaniesOwners(application, "Vlastníci firiem"));
        addMenuItem(listingsMenu, () -> new ListTaxesOverview(application, "Prehľad daní"));
        addMenuItem(listingsMenu, () -> new ListTaxPaymentsWithDecline(application, "Daň zo zisku - pokles"));
        addMenuItem(listingsMenu, () -> new ListPaymentsSchedules(application, "Rozpis platieb"));
        addMenuItem(listingsMenu, () -> new ListChanges(application, "Výpis zmien"));
        addMenuItem(listingsMenu, () -> new ListTaxAdvances(application, "Preddavky na daň"));
        addMenuItem(listingsMenu, () -> new ListTopPayers(application, "Top platci"));

        menuBar.add(listingsMenu);

        JMenuItem addsMenu = new JMenu("Pridať");

        addMenuItem(addsMenu, () -> new NewPaymentView(application, "Nová platba"));
        addMenuItem(addsMenu, () -> new NewTaxTypeView(application, "Nový typ dane"));
        addMenuItem(addsMenu, () -> new TaxSettingView(application, "Nastavenie dane"));
        addMenuItem(addsMenu, () -> new TaxAffiliationView(application, "Príslušnosť k dani"));

        menuBar.add(addsMenu);

        window.setJMenuBar(menuBar);
    }

    private void addMenuItem(JMenuItem parent, Supplier<View> supplier) {
        View view = supplier.get();

        String name = view.getTitle();
        JMenuItem menuItem = new JMenuItem(name);

        menuItem.addActionListener((e) -> setContent((JComponent) view));
        parent.add(menuItem);
    }

    public void show() {
        window.setVisible(true);
    }

    public void setContent(Container content) {
        String layoutPart = BorderLayout.CENTER;

        BorderLayout layout = (BorderLayout) contentPane.getLayout();
        Component actualComponent = layout.getLayoutComponent(layoutPart);

        if (actualComponent != null) {
            if (actualComponent instanceof View) {
                View previousView = (View) actualComponent;
                previousView.onHide();
            }

            contentPane.remove(actualComponent);
        }

        View view = (View) content;
        window.setTitle(TITLE + " - " + view.getTitle());

        contentPane.add(layoutPart, content);
        view.onShow();

        window.validate();
        window.repaint();
    }

}
