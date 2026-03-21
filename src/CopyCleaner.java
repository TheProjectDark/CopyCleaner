/*
 * CopyCleaner
 * Copyright (C) 2026 TheProjectDark
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.io.FileWriter;
import java.io.IOException;

public class CopyCleaner {
    public static String getClipboardText() { //method for getting the text from the clipboard
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

    public static void setClipboardText(String text) { //procedure for setting the text
        StringSelection sel = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
    }

    public static String fastGetAndSet() {  //method for fastClean button, which gets the clipboard, sanitizes and returns;
        String fC;
        fC = sanitize(getClipboardText());
        return fC;
    }

    public static void saveToTxt(JTextArea txtArea) {
        JFileChooser chooser = new JFileChooser();

        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
                writer.write(txtArea.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String sanitize(String s) { //sanitizing text from bad unicode
        return s
                .replace("\u200B","")
                .replace("\u200C","")
                .replace("\u200D","")
                .replace("ⓘ/?", "");
    }

    public static void main(String[] args) {
        //creating frame and panel
        JFrame frame = new JFrame();
        frame.setSize(480, 640);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        //elements
        JTextArea txtArea = new JTextArea();
        JButton pasteBtn = new JButton("PASTE");
        JButton copyBtn = new JButton("COPY");
        JButton fastClean = new JButton("FASTCLEAN");
        JButton save = new JButton("Save to txt");
        txtArea.setBounds(5, 30, 470, 580);
        pasteBtn.setBounds(5, 5, 80, 20);
        copyBtn.setBounds(90, 5, 70, 20);
        fastClean.setBounds(165, 5, 120, 20);
        save.setBounds(290, 5, 100, 20);
        panel.add(txtArea);
        panel.add(pasteBtn);
        panel.add(copyBtn);
        panel.add(fastClean);
        panel.add(save);

        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);

        //binds for buttons
        pasteBtn.addActionListener(e -> txtArea.setText(sanitize(getClipboardText())));
        copyBtn.addActionListener(e -> setClipboardText(sanitize(txtArea.getText())));
        fastClean.addActionListener(e -> {
            String sanitized = sanitize(fastGetAndSet());
            setClipboardText(sanitized);
            txtArea.setText(sanitized);
        });
        save.addActionListener(e -> saveToTxt(txtArea));

        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}