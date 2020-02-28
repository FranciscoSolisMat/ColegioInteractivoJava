/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.guis;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import xyz.theprogramsrc.colegiointeractivojava.Main;
import xyz.theprogramsrc.colegiointeractivojava.guis.dialogs.ErrorDialog;
import xyz.theprogramsrc.colegiointeractivojava.superjcore.utils.*;

import javax.swing.*;

import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static xyz.theprogramsrc.colegiointeractivojava.guis.GUIUtil.createMenu;
import static xyz.theprogramsrc.colegiointeractivojava.guis.GUIUtil.createItem;

public class LoginView extends JFrame {
    private JPanel panel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberCheckbox;
    private JButton loginBtn;
    private JProgressBar progress;
    private boolean load = false;


    public LoginView() {
        add(panel);
        setTitle("Iniciar sesion - " + Main.getInstance().APP_INFORMATION);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(500, 450);
        setResizable(Main.getInstance().canResize("LoginView", false));

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


        loginBtn.addActionListener(e -> {
            if (this.load) return;
            this.load = true;
            Main.getInstance().debug("Cargando...");
            String username = new String(this.usernameField.getText().getBytes());
            String password = new String(this.passwordField.getPassword());
            if (username == null || password == null) {
                this.invalidCredentials();
            } else {
                if (username.equals(" ") || username.equals("") || password.equals("") || password.equals(" ")) {
                    this.invalidCredentials();
                } else {
                    Connection con = Jsoup.connect(Main.LOGIN_URL.replace("{USERNAME}", username).replace("{PASSWORD}", password)).followRedirects(true);
                    try {
                        if (con.get().title().toLowerCase().contains("error")) {
                            new ErrorDialog("Error", "Error desconocido.") {
                                @Override
                                public void onOK() {
                                    LoginView.this.errorProgress();
                                    setVisible(false);
                                }
                            };
                        } else {
                            boolean remember = this.rememberCheckbox.isSelected();
                            if (remember) {
                                Main.getInstance().getSystemSettings().set("username", StringUtils.encode(username));
                                Main.getInstance().getSystemSettings().set("password", StringUtils.encode(password));
                            } else {
                                Main.getInstance().getCacheFile().set("username", StringUtils.encode(username));
                                Main.getInstance().getCacheFile().set("password", StringUtils.encode(password));
                            }
                            Map<String, String> resCookies = con.response().cookies();
                            Set<Cookie> cookies = resCookies.keySet().stream().map(s -> new Cookie(s, resCookies.get(s))).collect(Collectors.toSet());
                            cookies.stream().filter(c -> c.getName().equals("PHPSESSID")).findFirst().ifPresent(sessionId -> Main.getInstance().getSystemSettings().set("LoginCookie", StringUtils.encode(StringUtils.encode(sessionId.getValue()))));
                            Main.getInstance().log("Logueado correctamente como '" + username + "'");
                            final Timer timer = new Timer();
                            this.progress.setForeground(Utils.fromHex("#0768b7"));
                            this.progress.setValue(0);
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    LoginView.this.progress.setMaximum(100);
                                    LoginView.this.progress.setMinimum(0);
                                    int i = LoginView.this.progress.getValue();
                                    if (i != 100) {
                                        LoginView.this.progress.setForeground(Utils.fromHex("#0768b7"));
                                        LoginView.this.progress.setValue(i + 1);
                                    } else {
                                        LoginView.this.progress.setForeground(Color.green);
                                        LoginView.this.load = false;
                                        LoginView.this.setVisible(false);
                                        new MainView();
                                        this.cancel();
                                        timer.cancel();
                                        timer.cancel();
                                    }
                                }
                            };
                            timer.schedule(task, 0L, 5L);
                        }
                    } catch (Exception ex) {
                        Main.getInstance().debug(ex);
                    }
                }
            }
        });
        this.load();
    }

    /*

     */

    private void invalidCredentials() {
        new ErrorDialog("Error en credenciales!", "Por favor revisa que tus credenciales sean válidas!") {
            @Override
            public void onOK() {
                setVisible(false);
                LoginView.this.errorProgress();
            }
        };
    }

    private void errorProgress() {
        this.load = false;
        this.progress.setForeground(Color.red);
        this.progress.setValue(50);
    }

    private void load() {
        SwingUtils.buttonToImage(this.loginBtn, Main.getInstance().getImage("Login.png"));

        setVisible(true);
        if (isVisible()) {
            Main.getInstance().debug("GUI 'LoginView' abierta correctamente");
        } else {
            Main.getInstance().error("Error al abrir GUI 'LoginView'");
            Main.getInstance().stop(-1);
        }
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
        panel.setLayout(new GridLayoutManager(9, 4, new Insets(0, 0, 0, 0), -1, -1));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$("Verdana", -1, 18, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Ingresa tu Usuario:");
        panel.add(label1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameField = new JTextField();
        usernameField.setHorizontalAlignment(10);
        panel.add(usernameField, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel.add(spacer1, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel.add(spacer2, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel.add(spacer3, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel.add(spacer4, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        passwordField = new JPasswordField();
        panel.add(passwordField, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$("Verdana", -1, 18, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setText("Ingresa tu Clave:");
        panel.add(label2, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        rememberCheckbox = new JCheckBox();
        rememberCheckbox.setText("Recordar Credenciales");
        panel.add(rememberCheckbox, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel.add(spacer5, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        loginBtn = new JButton();
        loginBtn.setText("");
        panel.add(loginBtn, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        progress = new JProgressBar();
        progress.setForeground(new Color(-16291657));
        panel.add(progress, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel.add(spacer6, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel;
    }

}
