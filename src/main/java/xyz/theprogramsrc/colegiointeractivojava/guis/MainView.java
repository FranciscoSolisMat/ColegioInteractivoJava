/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.guis;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import xyz.theprogramsrc.colegiointeractivojava.Main;
import xyz.theprogramsrc.colegiointeractivojava.guis.dialogs.ErrorDialog;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.utils.ComboItem;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.utils.SwingUtils;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.utils.VirtualBrowser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static xyz.theprogramsrc.colegiointeractivojava.guis.GUIUtil.createItem;
import static xyz.theprogramsrc.colegiointeractivojava.guis.GUIUtil.createMenu;

public class MainView extends JFrame {
    private JPanel panel;
    private JComboBox<ComboItem> profileList;
    private JButton nextBtn;
    private String username, password;
    private HashMap<String, String> profiles = new HashMap<>();

    public MainView() {
        add(panel);
        setTitle("Vista Principal - " + Main.getInstance().APP_INFORMATION);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 450);
        setResizable(Main.getInstance().canResize("MainView", false));

        MenuItem[] appMenubarItems = new MenuItem[]{
                createItem("Borrar Cache y Reiniciar", "clear_cache"),
                createItem("Reiniciar", "restart"),
                createItem("Salir", "exit")
        };

        setMenuBar(GUIUtil.getMenuBar(
                createMenu("Aplicación", e -> {
                    switch (e.getActionCommand()) {
                        case "exit":
                            Main.getInstance().stop();
                            break;
                        case "clear_cache":
                            Main.getInstance().invalidateCache();
                            setVisible(false);
                            new LoginView();
                            break;
                        case "restart":
                            setVisible(false);
                            Main.getInstance().restart();
                            break;
                    }
                }, appMenubarItems)
        ));


        this.username = Main.getInstance().getUsername();
        this.password = Main.getInstance().getPassword();

        nextBtn.addActionListener(e -> {
            Object item = this.profileList.getSelectedItem();
            ComboItem comboItem = ((ComboItem) item);
            if (comboItem != null) {
                Main.getInstance().debug("PROXIMAMENTE");
                setVisible(false);
                new ErrorDialog(null, "PROXIMAMENTE") {
                    @Override
                    public void onOK() {
                        Main.getInstance().stop();
                    }
                };

                // new ProfileManager(comboItem.getKey(), comboItem.getValue());
            } else {
                new ErrorDialog("Error desconocido!", "Por favor envia el archivo logs.txt al soporte") {
                    @Override
                    public void onOK() {
                        System.exit(0);
                    }
                };
            }
        });

        this.load();
    }

    private void load() {
        SwingUtils.buttonToImage(this.nextBtn, Main.getInstance().getImage("Next.png"));
        if (profiles.isEmpty()) this.loadProfiles();
        this.profiles.forEach((p, l) -> this.profileList.addItem(new ComboItem(p, l)));
        setVisible(true);
        if (isVisible()) {
            Main.getInstance().debug("GUI 'MainView' abierta correctamente");
        } else {
            Main.getInstance().error("Error al abrir GUI 'MainView'");
            Main.getInstance().stop(-1);
        }
    }

    private void loadProfiles() {
        String url = "http://app.colegiointeractivo.cl/sae3.0/session/listarPerfiles.php";
        VirtualBrowser browser = Main.getInstance().getBrowser();
        browser.addCookie("app.colegiointeractivo.cl", Main.getInstance().getLoginCookieName(), Main.getInstance().getLoginCookieValue());
        Page request = browser.request(url, HttpMethod.GET);
        Document doc = Jsoup.parseBodyFragment(request.getWebResponse().getContentAsString());
        doc.getElementsByAttributeValueContaining("onclick", "go('setea").forEach(e -> {
            String link = "http://app.colegiointeractivo.cl/sae3.0/session/" + e.attr("onclick").replace("go('", "");
            link = StringUtils.removeEnd(link, "')");
            String profileName = (StringUtils.removeEnd(e.text(), " SAE HABILITADO"));
            this.profiles.put(profileName, link);
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel = new JPanel();
        panel.setLayout(new GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        profileList = new JComboBox();
        panel.add(profileList, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nextBtn = new JButton();
        nextBtn.setText("");
        panel.add(nextBtn, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
