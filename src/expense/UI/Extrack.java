package expense.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JCalendar;
import expense.Connection.connectdb;
import expense.module.Tabletransaction;
import expense.module.transaction;
import user.user_class.User;

public class Extrack extends JPanel {

    private static final int PANEL_WIDTH = 1200;
    private static final int PANEL_HEIGHT = 800;
    private static final int BORDER_SPACING = 30;
    private static final int CARD_BORDER_SPACING = 20;
    private static final int BUTTON_SPACING = 20;
    private static final int SHADOW_OFFSET = 5;
    private static final int ROUND_RECT_RADIUS = 15;
    private static final int GRADIENT_CARD_RADIUS = 20;
    private static final int TABLE_ROW_HEIGHT = 60;
    private static final int TABLE_CELL_BORDER = 3;
    private static final int TABLE_HEADER_HEIGHT = 55;
    private static final int INPUT_BORDER_SPACING = 40;

    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private static final Color CARD_TEXT_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR_PRIMARY = new Color(28, 35, 51);
    private static final Color TEXT_COLOR_SECONDARY = new Color(52, 73, 94);
    private static final Color TABLE_HEADER_COLOR = new Color(52, 73, 94);
    private static final Color TABLE_CELL_COLOR = new Color(249, 250, 251);
    private static final Color TABLE_SELECTION_COLOR = new Color(82, 186, 255, 20);
    private static final Color TABLE_SELECTION_FOREGROUND_COLOR = new Color(28, 35, 51);
    private static final Color INCOMING_COLOR = new Color(25, 135, 84);
    private static final Color EXPENSE_COLOR = new Color(185, 28, 28);
    private static final Color SHADOW_COLOR = new Color(28, 35, 51, 20);
    private static final Color INPUT_BACKGROUND_COLOR = new Color(248, 249, 250);

    private static final String ADD_BUTTON_TEXT = "Add";
    private static final String EDIT_BUTTON_TEXT = "Edit";
    private static final String DELETE_BUTTON_TEXT = "Delete";
    private static final String BALANCE_TITLE = "Total Balance";
    private static final String EXPENSE_TITLE = "Expenses";
    private static final String INCOME_TITLE = "Income";
    private static final String TABLE_TITLE = "Recent Transactions";
    private static final String AMOUNT_FORMAT = "%.2f$";
    private static final String ADD_DIALOG_TITLE = "Add";
    private static final String EDIT_DIALOG_TITLE = "Edit";
    private static final String NO_SELECTION_MESSAGE = "Please select a transaction";
    private static final String CONFIRM_DELETE_MESSAGE = "Are you sure you want to delete this transaction?";
    private static final String CONFIRM_DELETE_TITLE = "Confirm Delete";
    private static final String SAVE_BUTTON_TEXT = "Save";
    private static final String CANCEL_BUTTON_TEXT = "Cancel";
    private static final String DATE_FORMAT_DISPLAY = "dd/MM/yyyy";
    private static final String DATE_FORMAT_DB = "yyyy-MM-dd";
    private static final String INVALID_DATE_MESSAGE = "Invalid date format";
    private static final String INVALID_AMOUNT_MESSAGE = "Please enter a valid amount";

    private static final String GET_INCOME_SQL = "SELECT SUM(amount) as total FROM expensetracker WHERE username = ? AND amount > 0";
    private static final String GET_EXPENSE_SQL = "SELECT ABS(SUM(amount)) as total FROM expensetracker WHERE username = ? AND amount < 0";

    private Connection conn = new connectdb().getconnectdb();
    private Tabletransaction tb;
    private User user;
    private JTable table;

    public Extrack(User user) {
        this.user = user;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BACKGROUND_COLOR);
        JPanel mainContent = createMainContent();
        add(mainContent, BorderLayout.CENTER);
    }

    private double getIncomeBalance() {
        // tính tổng số tiền thu lọc theo username với điều kiện số tiền > 0
        return executeSumQuery(GET_INCOME_SQL);
    }

    private double getExpenseBalance() {
        // tính tổng số tiền chi tiêu lọc theo username với điều kiện số tiền < 0
        return executeSumQuery(GET_EXPENSE_SQL);
    }

    private double executeSumQuery(String sql) { // thực thi câu lệnh sql
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // trả về giá trị của cột total = Sum(amount) or ABS(Sum(amount))
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // panel chính chứa các phần tử giao diện giao dịch
    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.setBorder(new EmptyBorder(BORDER_SPACING, BORDER_SPACING, BORDER_SPACING, BORDER_SPACING));
        table = null;

        JPanel topSection = createTopSection();
        mainContent.add(topSection, BorderLayout.NORTH);

        JPanel centerSection = createCenterSection();
        mainContent.add(centerSection, BorderLayout.CENTER);
        return mainContent;
    }

    private JPanel createTopSection() {
        JPanel topSection = new JPanel(new GridLayout(1, 3, BORDER_SPACING, 0));
        topSection.setBackground(BACKGROUND_COLOR);
        double totalBalance = getIncomeBalance() - getExpenseBalance(); // tổng số tiền = số tiền thu - số tiền chi

        // tạo 3 card hiển thị thông tin số tiền thu, số tiền chi, tổng số tiền
        topSection.add(createGradientCard(BALANCE_TITLE, String.format(AMOUNT_FORMAT, totalBalance),
                new Color(37, 47, 63), new Color(52, 63, 83)));
        topSection.add(createGradientCard(EXPENSE_TITLE, String.format(AMOUNT_FORMAT, getExpenseBalance()),
                new Color(220, 53, 69), new Color(185, 43, 39)));
        topSection.add(createGradientCard(INCOME_TITLE, String.format(AMOUNT_FORMAT, getIncomeBalance()),
                new Color(40, 167, 69), new Color(32, 134, 55)));

        return topSection;
    }

    private JPanel createCenterSection() {
        JPanel centerSection = new JPanel(new GridBagLayout());
        centerSection.setBackground(BACKGROUND_COLOR);
        centerSection.setBorder(new EmptyBorder(BORDER_SPACING, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.95;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 15, 0);
        JPanel tableWrapper = createPanelWithShadow(createTablePanel());
        centerSection.add(tableWrapper, gbc);

        gbc.gridy = 1;
        gbc.weighty = 0.05;
        gbc.insets = new Insets(15, 0, 0, 0);
        JPanel expenseControls = createPanelWithShadow(createExpenseControls());
        centerSection.add(expenseControls, gbc);

        return centerSection;
    }

    private JPanel createExpenseControls() {
        JPanel controls = new JPanel();
        controls.setLayout(new GridLayout(1, 3, BUTTON_SPACING * 2, 0));
        controls.setBackground(Color.WHITE);
        controls.setBorder(BorderFactory.createEmptyBorder(CARD_BORDER_SPACING, INPUT_BORDER_SPACING,
                CARD_BORDER_SPACING, INPUT_BORDER_SPACING));

        JButton addButton = createStyledButton(ADD_BUTTON_TEXT, new Color(37, 47, 63), 250, 60);
        JButton editButton = createStyledButton(EDIT_BUTTON_TEXT, new Color(52, 63, 83), 250, 60);
        JButton deleteButton = createStyledButton(DELETE_BUTTON_TEXT, new Color(220, 53, 69), 250, 60);

        Font buttonFont = new Font("Product Sans", Font.BOLD, 16);
        addButton.setFont(buttonFont);
        editButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);

        controls.add(addButton);
        controls.add(editButton);
        controls.add(deleteButton);

        addButton.addActionListener(_ -> showAddTransactionDialog());
        editButton.addActionListener(_ -> showEditTransactionDialog());
        deleteButton.addActionListener(_ -> showDeleteTransactionDialog());

        addMouseHoverEffect(addButton, new Color(37, 47, 63));
        addMouseHoverEffect(editButton, new Color(52, 63, 83));
        addMouseHoverEffect(deleteButton, new Color(220, 53, 69));

        return controls;
    }

    private void showAddTransactionDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle(ADD_DIALOG_TITLE);
        dialog.setModal(true);
        dialog.add(createInputFields(null));
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditTransactionDialog() {
        table = getTable();
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int modelRow = table.convertRowIndexToModel(selectedRow);
            int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 4).toString());
            transaction t = tb.getTransaction(id);
            JDialog dialog = new JDialog();
            dialog.setTitle(EDIT_DIALOG_TITLE);
            dialog.setModal(true);
            dialog.add(createInputFields(t));
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
        } else {
            showNoSelectionDialog();
        }
    }

    private void showDeleteTransactionDialog() {
        JTable table = getTable();
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            showConfirmDeleteDialog(selectedRow);
        } else {
            showNoSelectionDialog();
        }
    }

    private void showConfirmDeleteDialog(int selectedRow) {
        JDialog confirmDialog = new JDialog();
        confirmDialog.setTitle(CONFIRM_DELETE_TITLE);
        confirmDialog.setModal(true);
        confirmDialog.setLayout(new BorderLayout(0, 5));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        JLabel messageLabel = new JLabel(CONFIRM_DELETE_MESSAGE, JLabel.CENTER);
        messageLabel.setFont(new Font("Product Sans", Font.PLAIN, 16));
        contentPanel.add(messageLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JButton confirmDeleteButton = createStyledButton(SAVE_BUTTON_TEXT, new Color(220, 53, 69), 100, 35);
        JButton cancelButton = createStyledButton(CANCEL_BUTTON_TEXT, new Color(108, 117, 125), 100, 35);

        buttonPanel.add(confirmDeleteButton);
        buttonPanel.add(cancelButton);

        confirmDialog.add(contentPanel, BorderLayout.CENTER);
        confirmDialog.add(buttonPanel, BorderLayout.SOUTH);

        confirmDeleteButton.addActionListener(_ -> {
            deleteSelectedTransaction(selectedRow, confirmDialog);
        });

        cancelButton.addActionListener(_ -> confirmDialog.dispose());

        confirmDialog.pack();
        confirmDialog.setLocationRelativeTo(this);
        confirmDialog.setVisible(true);
    }

    private void deleteSelectedTransaction(int selectedRow, JDialog confirmDialog) {
        try {
            int modelRow = table.convertRowIndexToView(selectedRow);
            int id = Integer.parseInt(table.getModel().getValueAt(modelRow, 4).toString());
            tb.deleteTransaction(id);

            refreshUI();
            confirmDialog.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(confirmDialog,
                    "Error deleting transaction: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showNoSelectionDialog() {
        JOptionPane optionPane = new JOptionPane(NO_SELECTION_MESSAGE, JOptionPane.WARNING_MESSAGE);
        JDialog dialog = optionPane.createDialog(this, "No Selection");

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

    private JPanel createInputFields(transaction transaction) {
        boolean isEditMode = transaction != null;
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_SPACING, INPUT_BORDER_SPACING, BORDER_SPACING,
                INPUT_BORDER_SPACING));
        inputPanel.setBackground(INPUT_BACKGROUND_COLOR);

        JPanel titlePanel = createTitlePanel(isEditMode ? "Edit Transaction" : "Add New Transaction");
        inputPanel.add(titlePanel);
        inputPanel.add(Box.createVerticalStrut(25));

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(5, 1, 0, 20));
        fieldsPanel.setBackground(INPUT_BACKGROUND_COLOR);

        JPanel typePanel = createTypeField(isEditMode ? transaction.getAmount() >= 0 : true);
        fieldsPanel.add(typePanel);

        JPanel categoryPanel = createCategoryField(typePanel, transaction);
        fieldsPanel.add(categoryPanel);

        JPanel descriptionPanel = createDescriptionField(isEditMode ? transaction.getDescription() : "");
        fieldsPanel.add(descriptionPanel);

        JPanel datePanel = createDateField(isEditMode ? transaction.getDate() : null);
        fieldsPanel.add(datePanel);

        JPanel amountPanel = createAmountField(
                isEditMode ? String.format("%.2f", Math.abs(transaction.getAmount())) : "");
        fieldsPanel.add(amountPanel);

        inputPanel.add(fieldsPanel);
        inputPanel.add(Box.createVerticalStrut(30));

        JPanel buttonPanel = createButtonPanel(inputPanel, typePanel, categoryPanel, descriptionPanel, datePanel,
                amountPanel, transaction);
        inputPanel.add(buttonPanel);

        return inputPanel;
    }

    private JPanel createTitlePanel(String title) {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(INPUT_BACKGROUND_COLOR);
        JLabel iconLabel = new JLabel("💰");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Product Sans", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR_PRIMARY);
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    private JPanel createTypeField(boolean isIncome) {
        JPanel typePanel = new JPanel(new BorderLayout(10, 5));
        typePanel.setBackground(INPUT_BACKGROUND_COLOR);
        JLabel typeLabel = new JLabel("Transaction Type");
        JComboBox<String> typeField = new JComboBox<>(new String[] { "Expense", "Income" });
        typeField.setSelectedItem(isIncome ? "Income" : "Expense");
        styleComponent(typeLabel);
        styleComponent(typeField);
        typePanel.add(typeLabel, BorderLayout.NORTH);
        typePanel.add(typeField, BorderLayout.CENTER);
        return typePanel;
    }

    @SuppressWarnings("rawtypes")
    private JPanel createCategoryField(JPanel typePanel, transaction transaction) {
        JPanel categoryPanel = new JPanel(new BorderLayout(10, 5));
        categoryPanel.setBackground(INPUT_BACKGROUND_COLOR);
        JLabel categoryLabel = new JLabel("Category");
        String[] expenseCategories = { "🍔 Food", "🚗 Transport", "🏠 Home", "🎮 Entertainment", "👦 Relatives",
                "💰 Invest", "📚 Study", "🛍️ Shopping", "🏪 Market", "💄 Beautify", "🏥 Health", "🎗️ Charity",
                "💳 Bill", "📦 Others" };
        String[] incomeCategories = { "💼 Salary", "💰 Profit", "🎁 Gift", "💵 Debt Recovery", "🏢 Business",
                "📈 Subsidy", "📦 Others" };
        JComboBox<String> categoryField = new JComboBox<>(
                transaction == null || transaction.getAmount() >= 0 ? incomeCategories : expenseCategories);

        if (transaction != null) {
            categoryField.setSelectedItem(transaction.getCategory());
        }

        ((JComboBox) typePanel.getComponent(1)).addActionListener(_ -> {
            categoryField.removeAllItems();
            String[] categories = ((JComboBox) typePanel.getComponent(1)).getSelectedItem().equals("Income")
                    ? incomeCategories
                    : expenseCategories;
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
        return categoryPanel;
    }

    private JPanel createDescriptionField(String description) {
        JPanel descPanel = new JPanel(new BorderLayout(10, 5));
        descPanel.setBackground(INPUT_BACKGROUND_COLOR);
        JLabel descLabel = new JLabel("Description 📝");
        JTextField descField = new JTextField(description);
        styleComponent(descLabel);
        styleComponent(descField);
        descPanel.add(descLabel, BorderLayout.NORTH);
        descPanel.add(descField, BorderLayout.CENTER);
        return descPanel;
    }

    private JPanel createDateField(String dateString) {
        JPanel datePanel = new JPanel(new BorderLayout(10, 5));
        datePanel.setBackground(INPUT_BACKGROUND_COLOR);
        JLabel dateLabel = new JLabel("Date 📅");
        styleComponent(dateLabel);

        JTextField dateField = new JTextField();
        styleComponent(dateField);
        dateField.setEditable(false);

        SimpleDateFormat displayFormat = new SimpleDateFormat(DATE_FORMAT_DISPLAY);
        SimpleDateFormat dbFormat = new SimpleDateFormat(DATE_FORMAT_DB);
        if (dateString != null) {
            try {
                Date date = dbFormat.parse(dateString);
                dateField.setText(displayFormat.format(date));
            } catch (Exception ex) {
                dateField.setText(displayFormat.format(new Date()));
            }
        } else {
            dateField.setText(displayFormat.format(new Date()));
        }

        JButton calendarButton = new JButton("📅");
        calendarButton.setFocusPainted(false);
        calendarButton.setBorderPainted(false);
        calendarButton.setBackground(Color.WHITE);
        calendarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel datePickerPanel = new JPanel(new BorderLayout());
        datePickerPanel.setBackground(Color.WHITE);
        datePickerPanel.add(dateField, BorderLayout.CENTER);
        datePickerPanel.add(calendarButton, BorderLayout.EAST);

        calendarButton.addActionListener(_ -> showDatePickerDialog(dateField));

        datePanel.add(dateLabel, BorderLayout.NORTH);
        datePanel.add(datePickerPanel, BorderLayout.CENTER);
        return datePanel;
    }

    private void showDatePickerDialog(JTextField dateField) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Select Date");
        dialog.setModal(true);
        dialog.setBackground(INPUT_BACKGROUND_COLOR);

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JCalendar calendar = new JCalendar();
        calendar.setBackground(BACKGROUND_COLOR);
        calendar.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        Color primaryColor = new Color(37, 47, 63);
        Color accentColor = new Color(82, 186, 255);
        Color textColor = new Color(52, 73, 94);

        calendar.getDayChooser().setDecorationBackgroundColor(BACKGROUND_COLOR);
        calendar.getDayChooser().setDecorationBackgroundVisible(true);
        calendar.getDayChooser().setWeekOfYearVisible(false);
        calendar.getDayChooser().setDayBordersVisible(false);
        calendar.getDayChooser().setForeground(textColor);
        calendar.getDayChooser().setWeekdayForeground(primaryColor);
        calendar.getDayChooser().setSundayForeground(accentColor);
        calendar.getDayChooser().setBackground(BACKGROUND_COLOR);
        calendar.getDayChooser().setFont(new Font("Inter", Font.PLAIN, 13));

        calendar.getMonthChooser().setBackground(BACKGROUND_COLOR);
        calendar.getMonthChooser().setFont(new Font("Inter", Font.BOLD, 14));
        calendar.getYearChooser().setBackground(BACKGROUND_COLOR);
        calendar.getYearChooser().setFont(new Font("Inter", Font.BOLD, 14));

        SimpleDateFormat displayFormat = new SimpleDateFormat(DATE_FORMAT_DISPLAY);
        try {
            Date currentDate = displayFormat.parse(dateField.getText());
            calendar.setDate(currentDate);
        } catch (Exception ex) {
            calendar.setDate(new Date());
        }

        calendar.getDayChooser().addPropertyChangeListener("day", _ -> {
            Date selectedDate = calendar.getDate();
            if (selectedDate != null) {
                dateField.setText(displayFormat.format(selectedDate));
            }
            dialog.dispose();
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(37, 47, 63, 20), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        mainPanel.add(calendar, BorderLayout.CENTER);
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(dateField);
        dialog.setVisible(true);
    }

    private JPanel createAmountField(String amount) {
        JPanel amountPanel = new JPanel(new BorderLayout(10, 5));
        amountPanel.setBackground(INPUT_BACKGROUND_COLOR);
        JLabel amountLabel = new JLabel("Amount 💵");
        JTextField amountField = new JTextField(amount);
        styleComponent(amountLabel);
        styleComponent(amountField);
        amountPanel.add(amountLabel, BorderLayout.NORTH);
        amountPanel.add(amountField, BorderLayout.CENTER);
        return amountPanel;
    }

    private JPanel createButtonPanel(JPanel parentPanel, JPanel typePanel, JPanel categoryPanel,
            JPanel descriptionPanel, JPanel datePanel, JPanel amountPanel, transaction transaction) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(INPUT_BACKGROUND_COLOR);

        JButton saveButton = createStyledButton(SAVE_BUTTON_TEXT, new Color(40, 167, 69), 120, 40);
        JButton cancelButton = createStyledButton(CANCEL_BUTTON_TEXT, new Color(220, 53, 69), 120, 40);

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        saveButton.addActionListener(_ -> {
            handleSaveTransaction(parentPanel, typePanel, categoryPanel, descriptionPanel, datePanel, amountPanel,
                    transaction);
        });

        cancelButton.addActionListener(_ -> {
            ((Window) parentPanel.getRootPane().getParent()).dispose();
        });

        return buttonPanel;
    }

    private void handleSaveTransaction(JPanel parentPanel, JPanel typePanel, JPanel categoryPanel,
            JPanel descriptionPanel, JPanel datePanel, JPanel amountPanel, transaction transaction) {
        @SuppressWarnings("rawtypes")
        JComboBox typeComboBox = (JComboBox) typePanel.getComponent(1);
        String type = (String) typeComboBox.getSelectedItem();

        @SuppressWarnings("rawtypes")
        JComboBox categoryComboBox = (JComboBox) categoryPanel.getComponent(1);
        String category = (String) categoryComboBox.getSelectedItem();

        JTextField descriptionField = (JTextField) descriptionPanel.getComponent(1);
        String description = descriptionField.getText();

        JPanel datePickerPanel = (JPanel) datePanel.getComponent(1);
        JTextField dateField = (JTextField) datePickerPanel.getComponent(0);
        String inputDate = dateField.getText();
        SimpleDateFormat inputFormat = new SimpleDateFormat(DATE_FORMAT_DISPLAY);
        SimpleDateFormat outputFormat = new SimpleDateFormat(DATE_FORMAT_DB);
        String date = "";
        try {
            Date parsed = inputFormat.parse(inputDate);
            date = outputFormat.format(parsed);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentPanel, INVALID_DATE_MESSAGE, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(((JTextField) amountPanel.getComponent(1)).getText());
            if (amount == 0 || amount < 0) {
                JOptionPane.showMessageDialog(parentPanel, INVALID_AMOUNT_MESSAGE, "Invalid Input",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (type.equals("Expense")) {
                amount = -amount;
            }

            if (transaction != null) {
                transaction newTransaction = new transaction(transaction.getId(), date, description, category, amount,
                        type);
                newTransaction.setCategory(category, conn);
                tb.updateTransaction(newTransaction);
            } else {
                int nextId = tb.getNextAvailableId();
                transaction newTransaction = new transaction(nextId, date, description, category, amount, type);
                newTransaction.setCategory(category, conn);
                tb.addTransaction(newTransaction);
            }
            refreshUI();
            ((Window) parentPanel.getRootPane().getParent()).dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parentPanel, INVALID_AMOUNT_MESSAGE, "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshUI() {
        removeAll();
        add(createMainContent());
        revalidate();
        repaint();
    }

    private JButton createStyledButton(String text, Color bgColor, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(new Font("Product Sans", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(bgColor);
        return button;
    }

    private void styleComponent(JComponent comp) {
        comp.setFont(new Font("Inter", Font.PLAIN, 14));
        comp.setBackground(Color.WHITE);
        if (comp instanceof JLabel) {
            comp.setForeground(TEXT_COLOR_SECONDARY);
        }
        if (comp instanceof JTextField) {
            ((JTextField) comp).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TEXT_COLOR_SECONDARY),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        }
    }

    private void addMouseHoverEffect(JButton button, Color baseColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(baseColor.brighter());
            }

            @Override
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
                g2d.setColor(SHADOW_COLOR);
                g2d.fillRoundRect(SHADOW_OFFSET, SHADOW_OFFSET, getWidth() - SHADOW_OFFSET * 2,
                        getHeight() - SHADOW_OFFSET * 2, ROUND_RECT_RADIUS, ROUND_RECT_RADIUS);
                g2d.setColor(Color.WHITE);
                g2d.fillRoundRect(0, 0, getWidth() - SHADOW_OFFSET, getHeight() - SHADOW_OFFSET, ROUND_RECT_RADIUS,
                        ROUND_RECT_RADIUS);
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
                GradientPaint gradient = new GradientPaint(0, 0, startColor, getWidth(), getHeight(), endColor);
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), GRADIENT_CARD_RADIUS, GRADIENT_CARD_RADIUS);
                g2d.dispose();
            }
        };

        card.setLayout(new GridBagLayout());
        card.setBorder(BorderFactory.createEmptyBorder(CARD_BORDER_SPACING, CARD_BORDER_SPACING, CARD_BORDER_SPACING,
                CARD_BORDER_SPACING));
        card.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(CARD_TEXT_COLOR);
        titleLabel.setFont(new Font("Inter", Font.PLAIN, 16));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(titleLabel, gbc);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setForeground(CARD_TEXT_COLOR);
        valueLabel.setFont(new Font("Inter", Font.BOLD, 28));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.insets = new Insets(10, 0, 0, 0);
        card.add(valueLabel, gbc);

        return card;
    }

    private JTable getTable() {
        if (table == null) {
            String[] columns = { "Category", "Description", "Date", "Amount" }; // các cột của bảng
            tb = new Tabletransaction(conn, user); // tạo bảng
            Object[][] data = tb.getTransactions(); // lấy dữ liệu từ bảng expensetracker
            table = new JTable(data, columns);
        }
        return table;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(
                new EmptyBorder(CARD_BORDER_SPACING, CARD_BORDER_SPACING, CARD_BORDER_SPACING, CARD_BORDER_SPACING));
        table = getTable();
        table = new JTable(table.getModel()) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                JComponent jc = (JComponent) comp;

                if (comp instanceof JLabel) {
                    ((JLabel) comp).setHorizontalAlignment(JLabel.CENTER);
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
                                header.setHorizontalAlignment(JLabel.CENTER);
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
                if (finalTable.getSelectedRow() == -1 && finalTable.getRowCount() > 0) {
                    finalTable.setRowSelectionInterval(0, 0);
                }
                finalTable.revalidate();
            }
        });

        table.setRowHeight(TABLE_ROW_HEIGHT);
        table.setSelectionBackground(TABLE_SELECTION_COLOR);
        table.setSelectionForeground(TABLE_SELECTION_FOREGROUND_COLOR);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setEnabled(true);

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

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel(TABLE_TITLE);
        titleLabel.setFont(new Font("Product Sans", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR_PRIMARY);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

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

        tablePanel.add(titlePanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        return tablePanel;
    }

    private Border createEmptyBorder() {
        return BorderFactory.createEmptyBorder(12, 20, 12, 20);
    }

    private Border createMatteBorder(Color color) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, TABLE_CELL_BORDER,
                        new Color(color.getRed(), color.getGreen(), color.getBlue(), 80)),
                createEmptyBorder());
    }

    private Border createSelectionBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(82, 186, 255)),
                createEmptyBorder());
    }
}