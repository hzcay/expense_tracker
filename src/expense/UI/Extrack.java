package expense.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import expense.module.Tabletransaction;
import com.toedter.calendar.JCalendar;
import java.util.Date;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import user.User;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class Extrack extends JPanel {
    private Connection conn;
    private Tabletransaction tb;
    private User user = new User("hzcay", "password");

    public Extrack() {
        conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_management",
                    "root",
                    "123456");
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 800));
        setBackground(new Color(240, 242, 245));
        // Main Content
        JPanel mainContent = createMainContent();
        add(mainContent, BorderLayout.CENTER);
    }

    public double getincomebalace() {
        try {
            String sql = "SELECT income FROM financial_tracker WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("income");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double getexpensebalace() {
        try {
            String sql = "SELECT expense FROM financial_tracker WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("expense");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        mainContent.setBackground(new Color(240, 242, 245));
        mainContent.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Top Section with modern cards using gradient panels
        JPanel topSection = new JPanel(new GridLayout(1, 3, 30, 0));
        topSection.setBackground(new Color(240, 242, 245));

        double totalbalance = getincomebalace() - getexpensebalace();
        // Modern gradient cards with matching sidebar theme
        topSection.add(createGradientCard("Total Balance", String.format("%.2f", totalbalance) + "$",
                new Color(37, 47, 63), new Color(52, 63, 83)));
        topSection.add(createGradientCard("Expenses", String.format("%.2f", getexpensebalace()) + "$",
                new Color(220, 53, 69), new Color(185, 43, 39))); // Red gradient for expenses
        topSection.add(createGradientCard("Income", String.format("%.2f", getincomebalace()) + "$",
                new Color(40, 167, 69), new Color(32, 134, 55))); // Green gradient for income

        mainContent.add(topSection, BorderLayout.NORTH);

        // Center Section with improved spacing and shadows
        JPanel centerSection = new JPanel(new GridLayout(1, 2, 30, 0));
        centerSection.setBackground(new Color(240, 242, 245));
        centerSection.setBorder(new EmptyBorder(30, 0, 0, 0));

        // Modified layout to use GridBagLayout for better control
        centerSection.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Table panel taking 80% of the space
        JPanel tableWrapper = createPanelWithShadow(createTablePanel());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.8;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        centerSection.add(tableWrapper, gbc);

        // Control panel taking 20% of the space
        JPanel expenseControls = createPanelWithShadow(createExpenseControls());
        gbc.gridx = 1;
        gbc.weightx = 0.2;
        centerSection.add(expenseControls, gbc);

        mainContent.add(centerSection, BorderLayout.CENTER);
        return mainContent;
    }

    private JPanel createExpenseControls() {
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBackground(Color.WHITE);
        controls.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton addButton = new JButton("Add Expense");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");

        // Style the buttons
        Component[] buttons = { addButton, editButton, deleteButton };
        for (Component btn : buttons) {
            JButton button = (JButton) btn;
            button.setFont(new Font("Product Sans", Font.BOLD, 14));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setForeground(Color.WHITE);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(200, 40));
        }

        // Colors matching sidebar theme
        addButton.setBackground(new Color(37, 47, 63)); // Dark blue from sidebar
        editButton.setBackground(new Color(52, 63, 83)); // Lighter blue from sidebar
        deleteButton.setBackground(new Color(220, 53, 69)); // Keeping red for delete

        addButton.addActionListener(_ -> {
            JDialog dialog = new JDialog();
            dialog.setTitle("Add");
            dialog.setModal(true);
            dialog.add(createInputFields());
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        controls.add(addButton);
        controls.add(Box.createVerticalStrut(15));
        controls.add(editButton);
        controls.add(Box.createVerticalStrut(15));
        controls.add(deleteButton);

        // Add hover effects
        addMouseHoverEffect(addButton, new Color(37, 47, 63));
        addMouseHoverEffect(editButton, new Color(52, 63, 83));
        addMouseHoverEffect(deleteButton, new Color(220, 53, 69));

        return controls;
    }

    private JPanel createInputFields() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        inputPanel.setBackground(new Color(248, 249, 250));

        // Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(248, 249, 250));
        JLabel iconLabel = new JLabel("💰");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JLabel titleLabel = new JLabel("Add New Transaction");
        titleLabel.setFont(new Font("Product Sans", Font.BOLD, 24));
        titleLabel.setForeground(new Color(37, 47, 63));
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        inputPanel.add(titlePanel);
        inputPanel.add(Box.createVerticalStrut(25));

        // Fields container with modern look
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(5, 1, 0, 20));
        fieldsPanel.setBackground(new Color(248, 249, 250));

        // Transaction Type field
        JPanel typePanel = new JPanel(new BorderLayout(10, 5));
        typePanel.setBackground(new Color(248, 249, 250));
        JLabel typeLabel = new JLabel("Transaction Type");
        JComboBox<String> typeField = new JComboBox<>(new String[] { "Expense", "Income" });
        styleComponent(typeLabel);
        styleComponent(typeField);
        typePanel.add(typeLabel, BorderLayout.NORTH);
        typePanel.add(typeField, BorderLayout.CENTER);

        // Category field with custom renderer
        JPanel categoryPanel = new JPanel(new BorderLayout(10, 5));
        categoryPanel.setBackground(new Color(248, 249, 250));
        JLabel categoryLabel = new JLabel("Category");
        String[] expenseCategories = { "🍔 Food", "🚗 Transport", "🏠 Home", "🎮 Entertainment", "👨‍👩‍👦 Relatives",
                "💰 Invest", "📚 Study", "🛍️ Shopping", "🏪 Market", "💄 Beautify", "🏥 Health", "🎗️ Charity",
                "💳 Bill", "📦 Others" };
        String[] incomeCategories = { "💼 Salary", "💰 Profit", "🎁 Gift", "💵 Debt Recovery", "🏢 Business",
                "📈 Subsidy", "📦 Others" };
        JComboBox<String> categoryField = new JComboBox<>(expenseCategories);

        // Update categories when transaction type changes
        typeField.addActionListener(_ -> {
            categoryField.removeAllItems();
            String[] categories = typeField.getSelectedItem().equals("Income") ? incomeCategories : expenseCategories;
            for (String category : categories) {
                categoryField.addItem(category);
            }
        });

        categoryField.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return label;
            }
        });
        styleComponent(categoryLabel);
        styleComponent(categoryField);
        categoryPanel.add(categoryLabel, BorderLayout.NORTH);
        categoryPanel.add(categoryField, BorderLayout.CENTER);

        // Description field with icon
        JPanel descPanel = new JPanel(new BorderLayout(10, 5));
        descPanel.setBackground(new Color(248, 249, 250));
        JLabel descLabel = new JLabel("Description 📝");
        JTextField descField = new JTextField();
        styleComponent(descLabel);
        styleComponent(descField);
        descPanel.add(descLabel, BorderLayout.NORTH);
        descPanel.add(descField, BorderLayout.CENTER);

        // Date field with calendar popup
        JPanel datePanel = new JPanel(new BorderLayout(10, 5));
        datePanel.setBackground(new Color(248, 249, 250));
        JLabel dateLabel = new JLabel("Date 📅");
        styleComponent(dateLabel);

        // Create text field for date display
        JTextField dateField = new JTextField();
        styleComponent(dateField);
        dateField.setEditable(false);

        // Set current date as initial value
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateField.setText(dateFormat.format(new Date()));

        // Create calendar button
        JButton calendarButton = new JButton("📅");
        calendarButton.setFocusPainted(false);
        calendarButton.setBorderPainted(false);
        calendarButton.setBackground(Color.WHITE);
        calendarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Create panel for date field and calendar button
        JPanel datePickerPanel = new JPanel(new BorderLayout());
        datePickerPanel.setBackground(Color.WHITE);
        datePickerPanel.add(dateField, BorderLayout.CENTER);
        datePickerPanel.add(calendarButton, BorderLayout.EAST);

        // Add calendar popup functionality
        calendarButton.addActionListener(e -> {
            JDialog dialog = new JDialog();
            dialog.setModal(true);
            JPanel calendarPanel = new JPanel();

            // Create calendar
            JCalendar calendar = new JCalendar();
            calendar.setPreferredSize(new Dimension(300, 250));

            // Add selection listener
            calendar.addPropertyChangeListener("calendar", evt -> {
                Date selectedDate = calendar.getDate();
                dateField.setText(dateFormat.format(selectedDate));
                dialog.dispose();
            });

            calendarPanel.add(calendar);
            dialog.add(calendarPanel);
            dialog.pack();
            dialog.setLocationRelativeTo(calendarButton);
            dialog.setVisible(true);
        });

        datePanel.add(dateLabel, BorderLayout.NORTH);
        datePanel.add(datePickerPanel, BorderLayout.CENTER);

        // Amount field with icon
        JPanel amountPanel = new JPanel(new BorderLayout(10, 5));
        amountPanel.setBackground(new Color(248, 249, 250));
        JLabel amountLabel = new JLabel("Amount 💵");
        JTextField amountField = new JTextField();
        styleComponent(amountLabel);
        styleComponent(amountField);
        amountPanel.add(amountLabel, BorderLayout.NORTH);
        amountPanel.add(amountField, BorderLayout.CENTER);

        fieldsPanel.add(typePanel);
        fieldsPanel.add(categoryPanel);
        fieldsPanel.add(descPanel);
        fieldsPanel.add(datePanel);
        fieldsPanel.add(amountPanel);

        inputPanel.add(fieldsPanel);
        inputPanel.add(Box.createVerticalStrut(30));

        // Modern buttons with hover effect
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(new Color(248, 249, 250));

        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(120, 40));
        saveButton.setBackground(new Color(40, 167, 69));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Product Sans", Font.BOLD, 14));
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        addMouseHoverEffect(saveButton, new Color(40, 167, 69));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setBackground(new Color(220, 53, 69));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Product Sans", Font.BOLD, 14));
        cancelButton.setBorderPainted(false);
        cancelButton.setFocusPainted(false);
        addMouseHoverEffect(cancelButton, new Color(220, 53, 69));

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        inputPanel.add(buttonPanel);

        return inputPanel;
    }

    private void styleComponent(JComponent comp) {
        comp.setFont(new Font("Inter", Font.PLAIN, 14));
        comp.setBackground(Color.WHITE);
        if (comp instanceof JLabel) {
            comp.setForeground(new Color(52, 73, 94));
        }
        if (comp instanceof JTextField) {
            ((JTextField) comp).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 73, 94)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        }
    }

    private void addMouseHoverEffect(JButton button, Color baseColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor);
            }
        });
    }

    private JPanel createPanelWithShadow(JPanel content) {
        JPanel wrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw shadow
                g2d.setColor(new Color(28, 35, 51, 20));
                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);

                // Draw panel background
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 15, 15);
                g2d.dispose();
            }
        };

        wrapper.setLayout(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(content);
        return wrapper;
    }

    private JPanel createGradientCard(String title, String value, Color startColor, Color endColor) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, startColor,
                        getWidth(), getHeight(), endColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
            }
        };

        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        card.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel, gbc);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(new Color(255, 255, 255));
        valueLabel.setFont(new Font("Inter", Font.BOLD, 28));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(10, 0, 0, 0);
        card.add(valueLabel, gbc);

        return card;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        String[] columns = { "Category", "Description", "Date", "Amount" };
        tb = new Tabletransaction(conn, user);
        Object[][] data = tb.getTransactions();

        JTable table = new JTable(data, columns) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent) comp;

                // Enhanced row colors with subtle gradient
                if (!isRowSelected(row)) {
                    Color baseColor = row % 2 == 0 ? new Color(249, 250, 251) : new Color(255, 255, 255);
                    comp.setBackground(baseColor);
                }

                // Enhanced styling for different columns
                switch (column) {
                    case 0: // Category
                        comp.setFont(new Font("Inter", Font.BOLD, 14));
                        comp.setForeground(new Color(28, 35, 51));
                        break;
                    case 3: // Amount
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
                            comp.setForeground(new Color(40, 167, 69));
                            jc.setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(40, 167, 69, 50)),
                                    BorderFactory.createEmptyBorder(12, 20, 12, 20)));
                        } else {
                            comp.setForeground(new Color(220, 53, 69));
                            jc.setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(220, 53, 69, 50)),
                                    BorderFactory.createEmptyBorder(12, 20, 12, 20)));
                        }
                        break;
                    default:
                        comp.setFont(new Font("Inter", Font.PLAIN, 14));
                        comp.setForeground(new Color(52, 63, 83));
                        jc.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
                }

                // Enhanced selection styling
                if (isRowSelected(row)) {
                    comp.setBackground(new Color(82, 186, 255, 20));
                    comp.setForeground(new Color(28, 35, 51));
                    jc.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(82, 186, 255)),
                            BorderFactory.createEmptyBorder(12, 17, 12, 20)));
                }

                return comp;
            }

            // Custom header renderer
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
                                header.setForeground(new Color(52, 73, 94));
                                header.setBackground(new Color(240, 243, 247));
                                header.setBorder(BorderFactory.createCompoundBorder(
                                        BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(82, 186, 255)),
                                        BorderFactory.createEmptyBorder(10, 20, 10, 20)));
                                header.setHorizontalAlignment(JLabel.LEFT);
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

        // Enhanced table styling
        table.setRowHeight(60);
        table.setSelectionBackground(new Color(82, 186, 255, 15));
        table.setSelectionForeground(new Color(28, 35, 51));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Smooth hover effect
        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            private int lastRow = -1;

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

        // Add mouse listener for click effect
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent e) {
                table.clearSelection();
            }
        });

        // Enhanced header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Product Sans", Font.BOLD, 14));
        header.setBackground(new Color(240, 243, 247)); // Subtle header background
        header.setForeground(new Color(52, 73, 94)); // Dark slate for header text
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));
        header.setPreferredSize(new Dimension(0, 55));

        // Enhanced scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerPanel.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel("💼");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setForeground(new Color(28, 35, 51));

        JLabel titleLabel = new JLabel("Recent Transactions");
        titleLabel.setFont(new Font("Product Sans", Font.BOLD, 24));
        titleLabel.setForeground(new Color(28, 35, 51));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        headerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                titleLabel.setForeground(new Color(82, 186, 255));
                iconLabel.setForeground(new Color(82, 186, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                titleLabel.setForeground(new Color(28, 35, 51));
                iconLabel.setForeground(new Color(28, 35, 51));
            }
        });

        headerPanel.add(iconLabel);
        headerPanel.add(titleLabel);

        JSeparator separator = new JSeparator() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(28, 35, 51),
                        getWidth(), 0, new Color(82, 186, 255));
                g2.setPaint(gp);
                g2.fill(new Rectangle(0, 0, getWidth(), 2));
                g2.dispose();
            }
        };
        separator.setPreferredSize(new Dimension(0, 2));

        titlePanel.add(headerPanel, BorderLayout.NORTH);
        titlePanel.add(separator, BorderLayout.SOUTH);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        tablePanel.add(titlePanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        return tablePanel;
    }

}
