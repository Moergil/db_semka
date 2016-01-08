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

        addMenuItem(listingsMenu, "Obdobia platcov", () -> new ListTaxPayingPeriodsView(application));
        addMenuItem(listingsMenu, "Zloženie príjmov", () -> new ListIncomeCompositionsView(application));
        addMenuItem(listingsMenu, "Právnické osoby - neplatiči", () -> new ListTaxPayersWhoDidntPayedView(application));
        addMenuItem(listingsMenu, "Spoločnosti", () -> new ListCompanies(application));
        addMenuItem(listingsMenu, "Vlastníci firiem", () -> new ListCompaniesOwners(application));
        addMenuItem(listingsMenu, "Prehľad daní", () -> new ListTaxesOverview(application));
        addMenuItem(listingsMenu, "Daň zo zisku - pokles", () -> new ListTaxPaymentsWithDecline(application));
        addMenuItem(listingsMenu, "Rozpis platieb", () -> new ListPaymentsSchedules(application));
        addMenuItem(listingsMenu, "Výpis zmien", () -> new ListChanges(application));
        addMenuItem(listingsMenu, "Preddavky na daň", () -> new ListTaxAdvances(application));
        addMenuItem(listingsMenu, "Top platci", () -> new ListTopPayers(application));

        menuBar.add(listingsMenu);

        JMenuItem addsMenu = new JMenu("Pridať");

        addMenuItem(addsMenu, "Nová platba", () -> new NewPaymentView(application));
        addMenuItem(addsMenu, "Nový typ dane", () -> new NewTaxTypeView(application));
        addMenuItem(addsMenu, "Nastavenie dane", () -> new TaxSettingView(application));
        addMenuItem(addsMenu, "Príslušnosť k dani", () -> new TaxAffiliationView(application));

        menuBar.add(addsMenu);

        window.setJMenuBar(menuBar);
    }

    private void addMenuItem(JMenuItem parent, String name, Supplier<JPanel> supplier) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.addActionListener((e) -> setContent(supplier.get()));
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
