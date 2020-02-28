/**********************************************************************************************************************
 * Copyright (c) 2020.                                                                                                *
 * This project was created by FranciscoSolis                                                                         *
 **********************************************************************************************************************/

package xyz.theprogramsrc.colegiointeractivojava.superjcore.utils;


import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import xyz.theprogramsrc.colegiointeractivojava.Main;

import java.net.URL;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VirtualBrowser {
    private WebClient webClient;

    public VirtualBrowser() {
        this.webClient = new WebClient(BrowserVersion.CHROME);
        this.webClient.getOptions().setJavaScriptEnabled(true);
        this.webClient.getOptions().setTimeout(15000);
        this.webClient.getOptions().setCssEnabled(false);
        this.webClient.getOptions().setRedirectEnabled(true);
        this.webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        this.webClient.getOptions().setThrowExceptionOnScriptError(false);
        this.webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
    }

    public Page request(String var1, HttpMethod var2, NameValuePair... var3) {
        try{
            WebRequest var4 = new WebRequest(new URL(var1), var2);
            var4.setRequestParameters(Arrays.asList(var3));
            Page var5 = this.webClient.getPage(var4);
            if (var5 instanceof HtmlPage) {
                HtmlPage var6 = (HtmlPage)var5;
                if (var6.asText().contains("DDoS protection by Cloudflare")) {
                    Main.getInstance().debug("Bypassing Cloudflare");
                    try {
                        Thread.sleep(9000L);
                    } catch (InterruptedException var8) {
                        var8.printStackTrace();
                    }

                    return this.request(var1, var2, var3);
                } else {
                    return var6;
                }
            } else {
                return var5;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Page request(String var1, NameValuePair... var2) {
        try{
            WebRequest var4 = new WebRequest(new URL(var1));
            var4.setRequestParameters(Arrays.asList(var2));
            Page var5 = this.webClient.getPage(var4);
            if (var5 instanceof HtmlPage) {
                HtmlPage var6 = (HtmlPage)var5;
                if (var6.asText().contains("DDoS protection by Cloudflare")) {
                    try {
                        Thread.sleep(9000L);
                    } catch (InterruptedException var8) {
                        var8.printStackTrace();
                    }

                    return this.request(var1, var2);
                } else {
                    return var6;
                }
            } else {
                return var5;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void addCookie(String domain, String name, String value) {
        this.webClient.getCookieManager().addCookie(new Cookie(domain, name, value));
    }

    public void clearCookies() {
        this.webClient.getCookieManager().clearCookies();
    }

    public Set<Cookie> getCookies() {
        return this.webClient.getCookieManager().getCookies();
    }

    public void close() {
        this.webClient.close();
    }
}
