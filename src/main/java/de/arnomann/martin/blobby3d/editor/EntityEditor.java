package de.arnomann.martin.blobby3d.editor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class BlockEditor {

    public static final int WIDTH = 400, HEIGHT = 320;

    private long blockId;

    JFrame window;
    private JTable parametersTable;

    BlockEditor(long blockId) {
        this.blockId = blockId;

        window = new JFrame("Block Editor");
        window.setResizable(true);
        window.setSize(WIDTH, HEIGHT);

        window.getContentPane().setBackground(new Color(40, 44, 52));
        window.getContentPane().setLayout(null);

        parametersTable = new JTable(new DefaultTableModel(new String[]{"Parameter Name", "Value"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        });
        
    }

}
