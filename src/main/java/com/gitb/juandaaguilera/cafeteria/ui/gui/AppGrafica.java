package com.gitb.juandaaguilera.cafeteria.ui.gui;

import com.gitb.juandaaguilera.cafeteria.fachada.CafeteriaFacade;
import javax.swing.SwingUtilities;

public class AppGrafica {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal(new CafeteriaFacade()).setVisible(true));
    }
}
