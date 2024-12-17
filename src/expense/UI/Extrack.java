package expense.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import expense.module.Tabletransaction;
import expense.module.transaction;

import com.formdev.flatlaf.FlatLightLaf;
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

import expense.Connection.connectdb;

public class Extrack extends JPanel {
    private Connection conn = new connectdb().getconnectdb();
    private Tabletransaction tb;
    private User user = new User("hzcay", "password");
    private JTable table;

    public Extrack(User u) {
        user = u;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 800));
        setBackground(new Color(240, 242, 245));
        // Main Content
        JPanel mainContent = createMainContent();
        add(mainContent, BorderLayout.CENTER);
    }

    public double getincomebalace() {
        try {
            String sql = "SELECT SUM(amount) as total FROM expensetracker WHERE username = ? AND amount > 0";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public double getexpensebalace() {
        try {
            String sql = "SELECT ABS(SUM(amount)) as total FROM expensetracker WHERE username = ? AND amount < 0";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUsername());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total");
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

        // Reset table to null to force recreation
        table = null;

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

        // Use a single column for both components
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.95;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 15, 0); // Add bottom margin
        JPanel tableWrapper = createPanelWithShadow(createTablePanel());
        centerSection.add(tableWrapper, gbc);

        // Controls panel goes below the table
        gbc.gridy = 1;
        gbc.weighty = 0.05;
        gbc.insets = new Insets(15, 0, 0, 0); // Add top margin
        JPanel expenseControls = createPanelWithShadow(createExpenseControls());
        centerSection.add(expenseControls, gbc);

        mainContent.add(centerSection, BorderLayout.CENTER);
        return mainContent;
    }

    private JPanel createExpenseControls() {
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS)); // Changed to horizontal layout
        controls.setBackground(Color.WHITE);
        controls.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton addButton = new JButton("Add");
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
            button.setPreferredSize(new Dimension(180, 45));
        }

        // Colors matching sidebar theme
        addButton.setBackground(new Color(37, 47, 63));
        editButton.setBackground(new Color(52, 63, 83));
        deleteButton.setBackground(new Color(220, 53, 69));

        // Add horizontal spacing between buttons
        controls.add(addButton);
        controls.add(Box.createHorizontalStrut(20)); // Add space between buttons
        controls.add(editButton);
        controls.add(Box.createHorizontalStrut(20)); // Add space between buttons
        controls.add(deleteButton);

        addButton.addActionListener(_ -> {
            JDialog dialog = new JDialog();
            dialog.setTitle("Add");
            dialog.setModal(true);
            dialog.add(createInputFields());
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        });

        editButton.addActionListener(_ -> {
            table = getTable();
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = table.convertRowIndexToModel(selectedRow);
                int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 4).toString());
                transaction t = tb.getTransaction(id);
                JDialog dialog = new JDialog();
                dialog.setTitle("Edit");
                dialog.setModal(true);
                dialog.add(updateInputField(t));
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            } else {
                JOptionPane optionPane = new JOptionPane(
                        "Please select a transaction to edit",
                        JOptionPane.WARNING_MESSAGE);
                JDialog dialog = optionPane.createDialog(this, "No Selection");

                // Get the OK button from the option pane
                JButton okButton = null;
                for (Component comp : optionPane.getComponents()) {
                    if (comp instanceof JPanel) {
                        for (Component btn : ((JPanel) comp).getComponents()) {
                            if (btn instanceof JButton) {
                                okButton = (JButton) btn;
                                okButton.setPreferredSize(new Dimension(80, 30)); // Reduced button size
                                okButton.setBackground(new Color(37, 47, 63)); // Changed to dark blue to match UI
                                okButton.setForeground(Color.WHITE);
                                okButton.setFont(new Font("Inter", Font.BOLD, 12)); // Smaller font
                                okButton.setBorderPainted(false);
                                okButton.setFocusPainted(false);
                            }
                        }
                    }
                }

                dialog.setVisible(true);
            }
        });

        deleteButton.addActionListener(e -> {
            JTable table = getTable();
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                // Create and configure the confirmation dialog
                JDialog confirmDialog = new JDialog();
                confirmDialog.setTitle("Confirm Delete");
                confirmDialog.setModal(true);
                confirmDialog.setLayout(new BorderLayout(0, 5)); // Reduced spacing from 15 to 5

                // Content panel with message
                JPanel contentPanel = new JPanel(new BorderLayout());
                contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30)); // Reduced bottom padding
                JLabel messageLabel = new JLabel("Are you sure you want to delete this transaction?", JLabel.CENTER);
                messageLabel.setFont(new Font("Product Sans", Font.PLAIN, 16));
                contentPanel.add(messageLabel);

                // Button panel with centered buttons
                JPanel buttonPanel = new JPanel();
                buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5)); // Reduced vertical padding
                buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Reduced bottom padding

                // Configure delete button
                JButton confirmDeleteButton = new JButton("Delete");
                confirmDeleteButton.setPreferredSize(new Dimension(100, 35));
                confirmDeleteButton.setBackground(new Color(220, 53, 69));
                confirmDeleteButton.setForeground(Color.WHITE);
                confirmDeleteButton.setFont(new Font("Product Sans", Font.BOLD, 14));
                confirmDeleteButton.setBorderPainted(false);
                confirmDeleteButton.setFocusPainted(false);

                // Configure cancel button
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setPreferredSize(new Dimension(100, 35));
                cancelButton.setBackground(new Color(108, 117, 125));
                cancelButton.setForeground(Color.WHITE);
                cancelButton.setFont(new Font("Product Sans", Font.BOLD, 14));
                cancelButton.setBorderPainted(false);
                cancelButton.setFocusPainted(false);

                // Add buttons to panel
                buttonPanel.add(confirmDeleteButton);
                buttonPanel.add(cancelButton);

                // Add panels to dialog
                confirmDialog.add(contentPanel, BorderLayout.CENTER);
                confirmDialog.add(buttonPanel, BorderLayout.SOUTH);

                confirmDeleteButton.addActionListener(_ -> {
                    try {
                        int modelRow = table.convertRowIndexToView(selectedRow);
                        int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 4).toString());
                        tb.deleteTransaction(id);
                        // Refresh UI
                        removeAll();
                        add(createMainContent());
                        revalidate();
                        repaint();
                        confirmDialog.dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(confirmDialog,
                                "Error deleting transaction: " + ex.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });

                cancelButton.addActionListener(_ -> confirmDialog.dispose());

                confirmDialog.pack();
                confirmDialog.setLocationRelativeTo(this);
                confirmDialog.setVisible(true);
            } else {
                JOptionPane optionPane = new JOptionPane(
                        "Please select a transaction to delete",
                        JOptionPane.WARNING_MESSAGE);
                JDialog dialog = optionPane.createDialog(this, "No Selection");

                // Get the OK button from the option pane
                JButton okButton = null;
                for (Component comp : optionPane.getComponents()) {
                    if (comp instanceof JPanel) {
                        for (Component btn : ((JPanel) comp).getComponents()) {
                            if (btn instanceof JButton) {
                                okButton = (JButton) btn;
                                okButton.setPreferredSize(new Dimension(80, 30));
                                okButton.setBackground(new Color(37, 47, 63));
                                okButton.setForeground(Color.WHITE);
                                okButton.setFont(new Font("Inter", Font.BOLD, 12));
                                okButton.setBorderPainted(false);
                                okButton.setFocusPainted(false);
                            }
                        }
                    }
                }

                dialog.setVisible(true);
            }
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

    private JPanel updateInputField(transaction t) {
        JPanel updatePanel = new JPanel();
        updatePanel.setLayout(new BoxLayout(updatePanel, BoxLayout.Y_AXIS));
        updatePanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        updatePanel.setBackground(new Color(248, 249, 250));

        // Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(248, 249, 250));
        JLabel iconLabel = new JLabel("💰");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JLabel titleLabel = new JLabel("Edit Transaction"); // Changed to Edit
        titleLabel.setFont(new Font("Product Sans", Font.BOLD, 24));
        titleLabel.setForeground(new Color(37, 47, 63));
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        updatePanel.add(titlePanel);
        updatePanel.add(Box.createVerticalStrut(25));

        // Fields container with modern look
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(5, 1, 0, 20));
        fieldsPanel.setBackground(new Color(248, 249, 250));

        // Transaction Type field - initialize with transaction type
        JPanel typePanel = new JPanel(new BorderLayout(10, 5));
        typePanel.setBackground(new Color(248, 249, 250));
        JLabel typeLabel = new JLabel("Transaction Type");
        JComboBox<String> typeField = new JComboBox<>(new String[] { "Expense", "Income" });
        typeField.setSelectedItem(t.getAmount() >= 0 ? "Income" : "Expense");
        styleComponent(typeLabel);
        styleComponent(typeField);
        typePanel.add(typeLabel, BorderLayout.NORTH);
        typePanel.add(typeField, BorderLayout.CENTER);

        // Category field
        JPanel categoryPanel = new JPanel(new BorderLayout(10, 5));
        categoryPanel.setBackground(new Color(248, 249, 250));
        JLabel categoryLabel = new JLabel("Category");
        String[] expenseCategories = { "🍔 Food", "🚗 Transport", "🏠 Home", "🎮 Entertainment", "👨‍👩‍👦 Relatives",
                "💰 Invest", "📚 Study", "🛍️ Shopping", "🏪 Market", "💄 Beautify", "🏥 Health", "🎗️ Charity",
                "💳 Bill", "📦 Others" };
        String[] incomeCategories = { "💼 Salary", "💰 Profit", "🎁 Gift", "💵 Debt Recovery", "🏢 Business",
                "📈 Subsidy", "📦 Others" };

        JComboBox<String> categoryField = new JComboBox<>(t.getAmount() >= 0 ? incomeCategories : expenseCategories);

        // Set the selected category from the transaction
        categoryField.setSelectedItem(t.getCategory());

        // Update categories when transaction type changes
        typeField.addActionListener(_ -> {
            categoryField.removeAllItems();
            String[] categories = typeField.getSelectedItem().equals("Income") ? incomeCategories : expenseCategories;
            for (String category : categories) {
                categoryField.addItem(category);
            }
        });

        styleComponent(categoryLabel);
        styleComponent(categoryField);
        categoryPanel.add(categoryLabel, BorderLayout.NORTH);
        categoryPanel.add(categoryField, BorderLayout.CENTER);

        // Description field - initialize with transaction description
        JPanel descPanel = new JPanel(new BorderLayout(10, 5));
        descPanel.setBackground(new Color(248, 249, 250));
        JLabel descLabel = new JLabel("Description 📝");
        JTextField descField = new JTextField(t.getDescription());
        styleComponent(descLabel);
        styleComponent(descField);
        descPanel.add(descLabel, BorderLayout.NORTH);
        descPanel.add(descField, BorderLayout.CENTER);

        // Date field - initialize with transaction date
        JPanel datePanel = new JPanel(new BorderLayout(10, 5));
        datePanel.setBackground(new Color(248, 249, 250));
        JLabel dateLabel = new JLabel("Date 📅");
        styleComponent(dateLabel);

        JTextField dateField = new JTextField();
        styleComponent(dateField);
        dateField.setEditable(false);

        // Convert date from yyyy-MM-dd to dd/MM/yyyy
        SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = dbFormat.parse(t.getDate());
            dateField.setText(displayFormat.format(date));
        } catch (Exception ex) {
            dateField.setText(displayFormat.format(new Date()));
        }

        // Calendar button and picker code remains the same
        JButton calendarButton = new JButton("📅");
        calendarButton.setFocusPainted(false);
        calendarButton.setBorderPainted(false);
        calendarButton.setBackground(Color.WHITE);
        calendarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel datePickerPanel = new JPanel(new BorderLayout());
        datePickerPanel.setBackground(Color.WHITE);
        datePickerPanel.add(dateField, BorderLayout.CENTER);
        datePickerPanel.add(calendarButton, BorderLayout.EAST);

        calendarButton.addActionListener(event -> {
            // Calendar dialog code remains the same
            JDialog dialog = new JDialog();
            dialog.setTitle("Select Date");
            dialog.setModal(true);
            dialog.setBackground(new Color(248, 249, 250));

            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JCalendar calendar = new JCalendar();
            calendar.setBackground(new Color(240, 242, 245));
            calendar.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

            Color primaryColor = new Color(37, 47, 63);
            Color accentColor = new Color(82, 186, 255);
            Color textColor = new Color(52, 73, 94);

            calendar.getDayChooser().setDecorationBackgroundColor(new Color(240, 242, 245));
            calendar.getDayChooser().setDecorationBackgroundVisible(true);
            calendar.getDayChooser().setWeekOfYearVisible(false);
            calendar.getDayChooser().setDayBordersVisible(false);
            calendar.getDayChooser().setForeground(textColor);
            calendar.getDayChooser().setWeekdayForeground(primaryColor);
            calendar.getDayChooser().setSundayForeground(accentColor);
            calendar.getDayChooser().setBackground(new Color(240, 242, 245));
            calendar.getDayChooser().setFont(new Font("Inter", Font.PLAIN, 13));

            calendar.getMonthChooser().setBackground(new Color(240, 242, 245));
            calendar.getMonthChooser().setFont(new Font("Inter", Font.BOLD, 14));
            calendar.getYearChooser().setBackground(new Color(240, 242, 245));
            calendar.getYearChooser().setFont(new Font("Inter", Font.BOLD, 14));

            try {
                Date currentDate = displayFormat.parse(dateField.getText());
                calendar.setDate(currentDate);
            } catch (Exception ex) {
                calendar.setDate(new Date());
            }

            calendar.getDayChooser().addPropertyChangeListener("day", evt -> {
                Date selectedDate = calendar.getDate();
                if (selectedDate != null) {
                    dateField.setText(displayFormat.format(selectedDate));
                }
                dialog.dispose();
            });

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(240, 242, 245));
            mainPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(37, 47, 63, 20), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            mainPanel.add(calendar, BorderLayout.CENTER);

            dialog.add(mainPanel);
            dialog.pack();
            dialog.setLocationRelativeTo(calendarButton);
            dialog.setVisible(true);
        });

        datePanel.add(dateLabel, BorderLayout.NORTH);
        datePanel.add(datePickerPanel, BorderLayout.CENTER);

        // Amount field - initialize with absolute transaction amount
        JPanel amountPanel = new JPanel(new BorderLayout(10, 5));
        amountPanel.setBackground(new Color(248, 249, 250));
        JLabel amountLabel = new JLabel("Amount 💵");
        JTextField amountField = new JTextField(String.format("%.2f", Math.abs(t.getAmount())));
        styleComponent(amountLabel);
        styleComponent(amountField);
        amountPanel.add(amountLabel, BorderLayout.NORTH);
        amountPanel.add(amountField, BorderLayout.CENTER);

        fieldsPanel.add(typePanel);
        fieldsPanel.add(categoryPanel);
        fieldsPanel.add(descPanel);
        fieldsPanel.add(datePanel);
        fieldsPanel.add(amountPanel); // Changed from amountField to amountPanel

        updatePanel.add(fieldsPanel);
        updatePanel.add(Box.createVerticalStrut(30));

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
        updatePanel.add(buttonPanel);

        saveButton.addActionListener(e -> {
            String type = (String) typeField.getSelectedItem();
            String category = (String) categoryField.getSelectedItem();
            String description = descField.getText();
            // Convert date from dd/MM/yyyy to yyyy-MM-dd for database
            String inputDate = dateField.getText();
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = "";
            try {
                Date parsed = inputFormat.parse(inputDate);
                date = outputFormat.format(parsed);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(updatePanel, "Invalid date format", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
                if (type.equals("Expense")) {
                    amount = -amount;
                }

                transaction newTransaction = new transaction(t.getId(), date, description, category, amount, type);
                newTransaction.setCategory(category, conn);
                tb.updateTransaction(newTransaction);

                // Refresh the UI
                removeAll();
                add(createMainContent());
                revalidate();
                repaint();

                ((Window) updatePanel.getRootPane().getParent()).dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(updatePanel, "Please enter a valid amount", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            ((Window) updatePanel.getRootPane().getParent()).dispose();
        });

        return updatePanel;
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
        calendarButton.addActionListener(event -> {
            // Create and configure modern date picker dialog
            JDialog dialog = new JDialog();
            dialog.setTitle("Select Date");
            dialog.setModal(true);
            dialog.setBackground(new Color(248, 249, 250));

            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JCalendar calendar = new JCalendar();
            calendar.setBackground(new Color(240, 242, 245));
            calendar.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

            // Enhanced modern styling for calendar
            Color primaryColor = new Color(37, 47, 63);
            Color accentColor = new Color(82, 186, 255);
            Color textColor = new Color(52, 73, 94);

            // Style the day chooser
            calendar.getDayChooser().setDecorationBackgroundColor(new Color(240, 242, 245));
            calendar.getDayChooser().setDecorationBackgroundVisible(true);
            calendar.getDayChooser().setWeekOfYearVisible(false);
            calendar.getDayChooser().setDayBordersVisible(false);
            calendar.getDayChooser().setForeground(textColor);
            calendar.getDayChooser().setWeekdayForeground(primaryColor);
            calendar.getDayChooser().setSundayForeground(accentColor);
            calendar.getDayChooser().setBackground(new Color(240, 242, 245));
            calendar.getDayChooser().setFont(new Font("Inter", Font.PLAIN, 13));

            // Style month/year choosers
            calendar.getMonthChooser().setBackground(new Color(240, 242, 245));
            calendar.getMonthChooser().setFont(new Font("Inter", Font.BOLD, 14));
            calendar.getYearChooser().setBackground(new Color(240, 242, 245));
            calendar.getYearChooser().setFont(new Font("Inter", Font.BOLD, 14));

            // Set initial date
            try {
                Date currentDate = dateFormat.parse(dateField.getText());
                calendar.setDate(currentDate);
            } catch (Exception ex) {
                calendar.setDate(new Date());
            }

            // Add property change listener to handle date selection
            calendar.getDayChooser().addPropertyChangeListener("day", evt -> {
                Date selectedDate = calendar.getDate();
                if (selectedDate != null) {
                    dateField.setText(dateFormat.format(selectedDate));
                }
                dialog.dispose();
            });

            // Main panel
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBackground(new Color(240, 242, 245));
            mainPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(37, 47, 63, 20), 1),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));

            mainPanel.add(calendar, BorderLayout.CENTER);

            dialog.add(mainPanel);
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

        saveButton.addActionListener(e -> {
            String type = (String) typeField.getSelectedItem();
            String category = (String) categoryField.getSelectedItem();
            String description = descField.getText();
            // Convert date from dd/MM/yyyy to yyyy-MM-dd for database
            String inputDate = dateField.getText();
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = "";
            try {
                Date parsed = inputFormat.parse(inputDate);
                date = outputFormat.format(parsed);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(inputPanel, "Invalid date format", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText());
                if (type.equals("Expense")) {
                    amount = -amount;
                }
                // Get next available ID
                int nextId = tb.getNextAvailableId();
                transaction t = new transaction(nextId, date, description, category, amount, type);
                t.setCategory(category, conn);
                tb.addTransaction(t);

                // Refresh the UI
                removeAll();
                add(createMainContent());
                revalidate();
                repaint();

                ((Window) inputPanel.getRootPane().getParent()).dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(inputPanel, "Please enter a valid amount", "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> {
            ((Window) inputPanel.getRootPane().getParent()).dispose();
        });

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

    private JTable getTable() {
        if (table == null) { // Create table only once
            String[] columns = { "Category", "Description", "Date", "Amount" };
            tb = new Tabletransaction(conn, user);
            Object[][] data = tb.getTransactions();
            table = new JTable(data, columns);
        }
        return table;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        table = getTable();
        table = new JTable(table.getModel()) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent) comp;

                // Center align all cell content
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
                }

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
                            comp.setForeground(new Color(25, 135, 84));
                            comp.setBackground(new Color(40, 167, 69, 40));
                            jc.setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(25, 135, 84, 80)),
                                    BorderFactory.createEmptyBorder(12, 20, 12, 20)));
                        } else {
                            comp.setForeground(new Color(185, 28, 28));
                            comp.setBackground(new Color(220, 53, 69, 40));
                            jc.setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(185, 28, 28, 80)),
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
                                header.setHorizontalAlignment(JLabel.CENTER); // Center align headers
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

        final JTable finalTable = table;
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                // Ensure a row stays selected
                if (finalTable.getSelectedRow() == -1 && finalTable.getRowCount() > 0) {
                    finalTable.setRowSelectionInterval(0, 0);
                }
                finalTable.revalidate();
            }
        });

        // Enhanced table styling
        table.setRowHeight(60);
        table.setSelectionBackground(new Color(82, 186, 255, 15));
        table.setSelectionForeground(new Color(28, 35, 51));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setEnabled(true); // Make sure table is enabled
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
