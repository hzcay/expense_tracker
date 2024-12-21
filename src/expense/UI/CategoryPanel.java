package expense.UI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import expense.Connection.connectdb;
import expense.module.Tabletransaction;
import user.user_class.User;

import java.awt.*;
import java.sql.Connection;
import java.util.Arrays;
import java.util.function.BiConsumer;

public class CategoryPanel extends JPanel {
    private static final int PANEL_WIDTH = 1200;
    private static final int PANEL_HEIGHT = 800;
    private static final int TABLE_ROW_HEIGHT = 60;
    private static final int TABLE_HEADER_HEIGHT = 55;

    private static final Color BACKGROUND_COLOR = new Color(240, 240, 255);
    private static final Color TEXT_COLOR_PRIMARY = new Color(28, 35, 51);
    private static final Color TEXT_COLOR_SECONDARY = new Color(52, 73, 94);
    private static final Color TABLE_HEADER_COLOR = new Color(52, 73, 94);
    private static final Color TABLE_CELL_COLOR = new Color(249, 250, 251);
    private static final Color TABLE_SELECTION_COLOR = new Color(82, 186, 255, 20);
    private static final Color TABLE_SELECTION_FOREGROUND_COLOR = new Color(28, 35, 51);
    private static final Color INCOMING_COLOR = new Color(25, 135, 84);
    private static final Color EXPENSE_COLOR = new Color(185, 28, 28);
    private static final String[] expenseCategories = { "🍔 Food", "🚗 Transport", "🏠 Home", "🎮 Entertainment",
            "👦 Relatives",
            "💰 Invest", "📚 Study", "🛍️ Shopping", "🏪 Market", "💄 Beautify", "🏥 Health", "🎗️ Charity",
            "💳 Bill", "📦 Others" };
    private static final String[] incomeCategories = { "💼 Salary", "💰 Profit", "🎁 Gift", "💵 Debt Recovery",
            "🏢 Business",
            "📈 Subsidy", "📦 Others" };
    private static final String TRANSACTION_HISTORY = "Transaction History";
    private boolean isAscending = true;

    private Connection conn = new connectdb().getconnectdb();
    private Tabletransaction tb;
    private User user;
    private JTable table;

    public CategoryPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BACKGROUND_COLOR);
        JPanel mainContent = createMainContent();
        add(mainContent, BorderLayout.CENTER);
    }

    public JPanel createMainContent() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel categoryPanel = createcategoryPanel();
        mainContent.add(categoryPanel, BorderLayout.CENTER);

        return mainContent;
    }

    private JPanel createcategoryPanel() {

        String[] colors = {
                "rgba(255, 99, 71, 0.85)",
                "rgba(65, 105, 225, 0.85)",
                "rgba(60, 179, 113, 0.85)",
                "rgba(147, 112, 219, 0.85)",
                "rgba(255, 165, 0, 0.85)",
                "rgba(30, 144, 255, 0.85)",
                "rgba(218, 165, 32, 0.85)",
                "rgba(219, 112, 147, 0.85)",
                "rgba(46, 139, 87, 0.85)",
                "rgba(255, 20, 147, 0.85)",
                "rgba(220, 20, 60, 0.85)",
                "rgba(106, 90, 205, 0.85)",
                "rgba(0, 139, 139, 0.85)",
                "rgba(128, 128, 128, 0.85)"
        };
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 40));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        class ShadowPanel extends JPanel {
            public ShadowPanel(LayoutManager layout) {
                super(layout);
                setOpaque(false);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 20));
                g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 20, 20);

                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 20, 20);
                g2.dispose();
            }
        }

        JPanel expensePanel = new ShadowPanel(new BorderLayout(0, 10));
        expensePanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JPanel expenseLabel = createTitlePanel("Expense");
        expensePanel.add(expenseLabel, BorderLayout.NORTH);

        JPanel expenseGrid = new JPanel(new GridLayout(0, 4, 25, 25));
        expenseGrid.setOpaque(false);

        JPanel incomePanel = new ShadowPanel(new BorderLayout(0, 10));
        incomePanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        JPanel incomeLabel = createTitlePanel("Income");
        incomePanel.add(incomeLabel, BorderLayout.NORTH);

        JPanel incomeGrid = new JPanel(new GridLayout(0, 4, 25, 25));
        incomeGrid.setOpaque(false);

        BiConsumer<JButton, Color> styleButton = (button, buttonColor) -> {
            button.setFont(new Font("SF Pro Text", Font.BOLD, 16));
            button.setBackground(buttonColor);
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(18, 18, 18, 18),
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)));
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setFocusPainted(false);

            button.putClientProperty("JButton.buttonType", "roundRect");
            button.putClientProperty("JButton.borderWidth", 0);
        };

        for (int i = 0; i < expenseCategories.length; i++) {
            JButton button = new JButton(expenseCategories[i]) {
                {
                    setContentAreaFilled(false);
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    super.paintComponent(g);
                }
            };
            Color buttonColor = parseRGBA(colors[i % colors.length]);
            styleButton.accept(button, buttonColor);

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(buttonColor.brighter());
                    button.setFont(new Font("SF Pro Text", Font.BOLD, 17));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(buttonColor);
                    button.setFont(new Font("SF Pro Text", Font.BOLD, 16));
                }
            });
            expenseGrid.add(button);
        }

        for (int i = 0; i < incomeCategories.length; i++) {
            JButton button = new JButton(incomeCategories[i]) {
                {
                    setContentAreaFilled(false);
                }

                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    super.paintComponent(g);
                }
            };
            Color buttonColor = parseRGBA(colors[i % colors.length]);
            styleButton.accept(button, buttonColor);

            button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(buttonColor.brighter());
                    button.setFont(new Font("SF Pro Text", Font.BOLD, 17));
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(buttonColor);
                    button.setFont(new Font("SF Pro Text", Font.BOLD, 16));
                }
            });
            incomeGrid.add(button);
        }

        expensePanel.add(expenseGrid, BorderLayout.CENTER);
        incomePanel.add(incomeGrid, BorderLayout.CENTER);

        panel.add(expensePanel);
        panel.add(incomePanel);

        for (Component c : expenseGrid.getComponents()) {
            if (c instanceof JButton) {
                JButton button = (JButton) c;
                button.addActionListener(_ -> {
                    String category = button.getText();
                    if (category != "📦 Others") {
                        showTransactionsDialog(category);
                    } else {
                        showTransactionsDialog("EX_0");
                    }
                });
            }
        }

        for (Component c : incomeGrid.getComponents()) {
            if (c instanceof JButton) {
                JButton button = (JButton) c;
                button.addActionListener(_ -> {
                    String category = button.getText();
                    if (category != "📦 Others") {
                        showTransactionsDialog(category);
                    } else {
                        showTransactionsDialog("IC_0");
                    }
                });
            }
        }

        return panel;
    }

    private Color parseRGBA(String rgba) {
        String[] values = rgba.substring(rgba.indexOf('(') + 1, rgba.indexOf(')')).split(",");
        return new Color(
                Integer.parseInt(values[0].trim()),
                Integer.parseInt(values[1].trim()),
                Integer.parseInt(values[2].trim()),
                (int) (Float.parseFloat(values[3].trim()) * 255));
    }

    private JTable getCategory(String category) {
        if (table == null) {
            String[] columns = { "Category", "Description", "Date", "Amount" };
            tb = new Tabletransaction(conn, user);
            Object[][] data = tb.getTransactionwithCategory(category);
            table = new JTable(data, columns);
        }
        return table;
    }

    private void showTransactionsDialog(String category) {
        tb = new Tabletransaction(conn, user);
        Object[][] transactions = tb.getTransactionwithCategory(category);

        if (table != null) {
            table.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][] {},
                    new String[] { "Category", "Description", "Date", "Amount" }));
        }
        if (transactions != null && transactions.length > 0) {
            JDialog dialog = new JDialog();
            if (category == "IC_0") {
                dialog.setTitle("Income Transactions for: other");
            } else if (category == "EX_0") {
                dialog.setTitle("Expense Transactions for: other");
            } else if (category.startsWith("IC_")) {
                dialog.setTitle("Income Transactions for: " + category.substring(3));
            } else {
                dialog.setTitle("Expense Transactions for: " + category.substring(3));
            }
            dialog.setModal(true);
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(null);
            table = null;
            dialog.add(createTablePanel(category));
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "No transactions found for this category", "No Transactions",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel createTablePanel(String category) {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        table = getCategory(category);
        table = new JTable(table.getModel()) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent) comp;

                if (comp instanceof JLabel) {
                    ((JLabel) comp).setHorizontalAlignment(SwingConstants.CENTER);
                }
                if (!isRowSelected(row)) {
                    Color baseColor = row % 2 == 0 ? TABLE_CELL_COLOR : Color.WHITE;
                    comp.setBackground(baseColor);
                }
                switch (column) {
                    case 0:
                        comp.setFont(new Font("Inter", Font.BOLD, 14));
                        comp.setForeground(TEXT_COLOR_PRIMARY);
                        break;
                    case 3:
                        comp.setFont(new Font("Inter", Font.BOLD, 14));
                        Object value = getValueAt(row, column);
                        double amount = 0;
                        if (value != null) {
                            try {
                                amount = Double.parseDouble(value.toString().replace("$", ""));
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        if (amount >= 0) {
                            comp.setForeground(INCOMING_COLOR);
                            comp.setBackground(new Color(40, 167, 69, 40));
                            jc.setBorder(createMatteBorder(INCOMING_COLOR));
                        } else {
                            comp.setForeground(EXPENSE_COLOR);
                            comp.setBackground(new Color(220, 53, 69, 40));
                            jc.setBorder(createMatteBorder(EXPENSE_COLOR));
                        }
                        break;
                    default:
                        comp.setFont(new Font("Inter", Font.PLAIN, 14));
                        comp.setForeground(TEXT_COLOR_SECONDARY);
                        jc.setBorder(createEmptyBorder());
                }
                if (isRowSelected(row)) {
                    comp.setBackground(TABLE_SELECTION_COLOR);
                    comp.setForeground(TABLE_SELECTION_FOREGROUND_COLOR);
                    jc.setBorder(createSelectionBorder());
                }
                return comp;
            }

            @Override
            public JTableHeader createDefaultTableHeader() {
                return new JTableHeader(columnModel) {
                    @Override
                    public TableCellRenderer getDefaultRenderer() {
                        return new DefaultTableCellRenderer() {
                            @Override
                            public Component getTableCellRendererComponent(JTable table, Object value,
                                    boolean isSelected, boolean hasFocus, int row, int column) {
                                JLabel header = (JLabel) super.getTableCellRendererComponent(table, value,
                                        isSelected, hasFocus, row, column);
                                header.setFont(new Font("Product Sans", Font.BOLD, 14));
                                header.setForeground(TABLE_HEADER_COLOR);
                                header.setBackground(new Color(240, 243, 247));
                                header.setBorder(BorderFactory.createCompoundBorder(
                                        BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(82, 186, 255)),
                                        BorderFactory.createEmptyBorder(10, 20, 10, 20)));
                                header.setHorizontalAlignment(SwingConstants.CENTER);
                                return header;
                            }
                        };
                    }
                };
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return String.class;
            }
        };

        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setSelectionBackground(TABLE_SELECTION_COLOR);
        table.setSelectionForeground(TABLE_SELECTION_FOREGROUND_COLOR);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            private int lastRow = -1;

            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != lastRow) {
                    if (row > -1) {
                        table.clearSelection();
                        table.setRowSelectionInterval(row, row);
                    } else {
                        table.clearSelection();
                    }
                    lastRow = row;
                }
            }
        });

        table.getTableHeader().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int column = table.columnAtPoint(e.getPoint());
                if (column == 3) {
                    tb = new Tabletransaction(conn, user);
                    Object[][] data = tb.getTransactionwithCategory(category);
                    Sorttable(data);

                    table.setModel(new javax.swing.table.DefaultTableModel(
                            data,
                            new String[] { "Category", "Description", "Date", "Amount" }));
                    table.revalidate();
                    table.repaint();
                }
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                table.clearSelection();
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Product Sans", Font.BOLD, 14));
        header.setBackground(new Color(240, 243, 247));
        header.setForeground(TEXT_COLOR_SECONDARY);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));
        header.setPreferredSize(new Dimension(0, TABLE_HEADER_HEIGHT));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel titlePanel = createPanelWithTitle(TRANSACTION_HISTORY);

        tablePanel.add(titlePanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        return tablePanel;
    }

    private JPanel createPanelWithTitle(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = createStyledLabel(title, TEXT_COLOR_PRIMARY, new Font("Inter", Font.BOLD, 24));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        headerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                titleLabel.setForeground(new Color(82, 186, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                titleLabel.setForeground(TEXT_COLOR_PRIMARY);
            }
        });

        headerPanel.add(titleLabel);

        JSeparator separator = new JSeparator() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, TEXT_COLOR_PRIMARY, getWidth(), 0, new Color(82, 186, 255));
                g2.setPaint(gp);
                g2.fill(new Rectangle(0, 0, getWidth(), 2));
                g2.dispose();
            }
        };
        separator.setPreferredSize(new Dimension(0, 2));
        titlePanel.add(headerPanel, BorderLayout.NORTH);
        titlePanel.add(separator, BorderLayout.SOUTH);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panel.add(titlePanel, BorderLayout.NORTH);
        return panel;
    }

    private JLabel createStyledLabel(String text, Color color, Font font) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private Border createEmptyBorder() {
        return BorderFactory.createEmptyBorder(12, 20, 12, 20);
    }

    private Border createMatteBorder(Color color) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 3,
                        new Color(color.getRed(), color.getGreen(), color.getBlue(), 80)),
                createEmptyBorder());
    }

    private Border createSelectionBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(82, 186, 255)),
                createEmptyBorder());
    }

    private JPanel createTitlePanel(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(0, 10, 10, 10));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = createStyledLabel(title, TEXT_COLOR_PRIMARY, new Font("Inter", Font.BOLD, 24));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Reduced vertical spacing to 0
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        headerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                titleLabel.setForeground(new Color(82, 186, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                titleLabel.setForeground(TEXT_COLOR_PRIMARY);
            }
        });

        headerPanel.add(titleLabel);

        JSeparator separator = new JSeparator() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, TEXT_COLOR_PRIMARY, getWidth(), 0, new Color(82, 186, 255));
                g2.setPaint(gp);
                g2.fill(new Rectangle(0, 0, getWidth(), 2));
                g2.dispose();
            }
        };
        separator.setPreferredSize(new Dimension(0, 2));
        titlePanel.add(headerPanel, BorderLayout.CENTER);
        titlePanel.add(separator, BorderLayout.SOUTH);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Removed bottom padding

        panel.add(titlePanel, BorderLayout.NORTH);
        return panel;
    }

    public void Sorttabledown(Object[][] data) {
        Arrays.sort(data, (a, b) -> {
            double amountA = Double.parseDouble(a[3].toString().replace("$", ""));
            double amountB = Double.parseDouble(b[3].toString().replace("$", ""));
            return Double.compare(amountB, amountA);
        });
        tb.setData(data);
    }

    public void Sorttableup(Object[][] data) {
        Arrays.sort(data, (a, b) -> {
            double amountA = Double.parseDouble(a[3].toString().replace("$", ""));
            double amountB = Double.parseDouble(b[3].toString().replace("$", ""));
            return Double.compare(amountA, amountB);
        });
        tb.setData(data);
    }

    public void Sorttable(Object[][] data) {
        if (isAscending) {
            Sorttableup(data);
        } else {
            Sorttabledown(data);
        }
        isAscending = !isAscending;
    }
}
