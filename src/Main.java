/*
 * CopyCleaner
 * Copyright (C) 2026 TheProjectDark
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.datatransfer.*;

public class Main {
    public static String getClipboardText() {
        try {
            Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();

            if (cb.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                return (String) cb.getData(DataFlavor.stringFlavor);
            }

            if (cb.isDataFlavorAvailable(DataFlavor.selectionHtmlFlavor)) {
                String html = (String) cb.getData(DataFlavor.selectionHtmlFlavor);
                return html.replaceAll("<[^>]*>", "");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void setClipboardText(String text) {
        StringSelection sel = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
    }

    private static String sanitize(String s) {
        return s
                .replace("\u200B","")
                .replace("\u200C","")
                .replace("\u200D","")
                .replace("ⓘ/?", "");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(480, 640);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JTextArea txtArea = new JTextArea();
        JButton pasteBtn = new JButton("PASTE");
        JButton copyBtn = new JButton("COPY");
        txtArea.setBounds(5, 30, 470, 580);
        pasteBtn.setBounds(5, 5, 50, 20);
        copyBtn.setBounds(60, 5, 50, 20);
        panel.add(txtArea);
        panel.add(pasteBtn);
        panel.add(copyBtn);

        pasteBtn.addActionListener(e -> txtArea.setText(sanitize(getClipboardText())));
        copyBtn.addActionListener(e -> setClipboardText(sanitize(txtArea.getText())));


        frame.add(panel);
        frame.show();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}