package expense.UI;

import com.formdev.flatlaf.FlatLightLaf;
import expense.Connection.connectdb;
import expense.module.Tabletransaction;
import user.user_class.User;

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
import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarPanel extends JPanel {

    private static final int PANEL_WIDTH = 1200;
    private static final int PANEL_HEIGHT = 800;
    private static final int BORDER_SPACING = 10;
    private static final int CELL_SPACING = 5;
    private static final int TABLE_ROW_HEIGHT = 60;
    private static final int TABLE_HEADER_HEIGHT = 55;

    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private static final Color TEXT_COLOR_PRIMARY = new Color(28, 35, 51);
    private static final Color TEXT_COLOR_SECONDARY = new Color(52, 73, 94);
    private static final Color TABLE_HEADER_COLOR = new Color(52, 73, 94);
    private static final Color TABLE_CELL_COLOR = new Color(249, 250, 251);
    private static final Color TABLE_SELECTION_COLOR = new Color(82, 186, 255, 20);
    private static final Color TABLE_SELECTION_FOREGROUND_COLOR = new Color(28, 35, 51);
    private static final Color INCOMING_COLOR = new Color(25, 135, 84);
    private static final Color EXPENSE_COLOR = new Color(185, 28, 28);
    private static final Color NAVIGATION_HOVER_COLOR = new Color(204, 229, 255);
    private static final Color DAY_TEXT_COLOR = new Color(31, 41, 55);

    private static final String GET_DAILY_INCOME_SQL = "SELECT SUM(amount) as total FROM expensetracker WHERE username = ? AND amount > 0 AND DATE(date) = ?";
    private static final String GET_DAILY_EXPENSE_SQL = "SELECT ABS(SUM(amount)) as total FROM expensetracker WHERE username = ? AND amount < 0 AND DATE(date) = ?";
    private static final String CALENDAR_ICON = "📅";
    private static final String TRANSACTION_HISTORY = "Transaction History";

    private Connection conn = new connectdb().getconnectdb();
    private Tabletransaction tb;
    private User user;
    private LocalDate currentDate = LocalDate.now();
    private JPanel calendarPanel = new JPanel(new GridLayout(0, 7, CELL_SPACING, CELL_SPACING));
    private JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
    private JTable table;

    public CalendarPanel(User user) {
        this.user = user;
        setupLookAndFeel();
        setupLayout();
        renderCalendar(currentDate);
    }

    private void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        calendarPanel.setBackground(BACKGROUND_COLOR);
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(0, BORDER_SPACING, BORDER_SPACING, BORDER_SPACING));
        add(calendarPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(BORDER_SPACING, 0));
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(BORDER_SPACING, BORDER_SPACING, 0, BORDER_SPACING));

        JButton prevButton = createNavigationButton("←");
        JButton nextButton = createNavigationButton("→");

        prevButton.addActionListener(_ -> changeMonth(-1));
        nextButton.addActionListener(_ -> changeMonth(1));
        monthLabel.setFont(new Font("Arial", Font.BOLD, 20));
        monthLabel.setForeground(TEXT_COLOR_PRIMARY);
        monthLabel.setHorizontalAlignment(SwingConstants.CENTER);
        monthLabel.setVerticalAlignment(SwingConstants.CENTER);

        JLabel iconLabel = new JLabel(CALENDAR_ICON);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setVerticalAlignment(SwingConstants.CENTER);
        iconLabel.setForeground(TEXT_COLOR_PRIMARY);

        headerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                monthLabel.setForeground(new Color(82, 186, 255));
                iconLabel.setForeground(new Color(82, 186, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                monthLabel.setForeground(TEXT_COLOR_PRIMARY);
                iconLabel.setForeground(TEXT_COLOR_PRIMARY);
            }
        });

        headerPanel.add(prevButton, BorderLayout.WEST);
        JPanel centerPanel = createCenterHeaderPanel(iconLabel);
        headerPanel.add(centerPanel, BorderLayout.CENTER);
        headerPanel.add(nextButton, BorderLayout.EAST);
        return headerPanel;
    }

    private JPanel createCenterHeaderPanel(JLabel iconLabel) {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(BACKGROUND_COLOR);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.X_AXIS));
        innerPanel.setBackground(BACKGROUND_COLOR);
        innerPanel.add(iconLabel);
        innerPanel.add(Box.createHorizontalStrut(10));
        innerPanel.add(monthLabel);
        innerPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        centerPanel.add(innerPanel);
        return centerPanel;
    }

    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setForeground(new Color(0, 102, 204));
        button.setBackground(BACKGROUND_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(NAVIGATION_HOVER_COLOR);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BACKGROUND_COLOR);
            }
        });
        button.setPreferredSize(new Dimension(40, 40));
        return button;
    }

    private void updateMonthLabel() {
        YearMonth yearMonth = YearMonth.from(currentDate);
        monthLabel.setText(yearMonth.getMonth().name() + " " + yearMonth.getYear());
    }

    private void changeMonth(int offset) {
        currentDate = currentDate.plusMonths(offset);
        renderCalendar(currentDate);
    }

    private void renderCalendar(LocalDate date) {
        calendarPanel.removeAll();
        updateMonthLabel();
        calendarPanel.setLayout(new GridLayout(0, 7, CELL_SPACING, CELL_SPACING));

        String[] daysOfWeek = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
        for (String day : daysOfWeek) {
            JLabel label = createDayOfWeekLabel(day);
            calendarPanel.add(label);
        }
        YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonthValue());
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        for (int i = 0; i < dayOfWeek; i++) {
            calendarPanel.add(new JLabel());
        }

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            JButton dayButton = createDayButton(yearMonth, day);
            calendarPanel.add(dayButton);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private JLabel createDayOfWeekLabel(String day) {
        JLabel label = new JLabel(day, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR_PRIMARY);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        label.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                label.setForeground(new Color(82, 186, 255));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                label.setForeground(TEXT_COLOR_PRIMARY);
            }
        });
        return label;
    }

    private JButton createDayButton(YearMonth yearMonth, int day) {
        JButton dayButton = new JButton();
        dayButton.setFont(new Font("Inter", Font.PLAIN, 14));
        dayButton.setFocusPainted(false);
        dayButton.setBorderPainted(false);
        dayButton.setLayout(new BorderLayout(4, 4));
        dayButton.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        LocalDate date = yearMonth.atDay(day);
        double income = getDailyIncome(date);
        double expense = getDailyExpense(date);

        JLabel dayLabel = createStyledDayLabel(String.valueOf(day));
        dayButton.add(dayLabel, BorderLayout.NORTH);
        JPanel amountPanel = createAmountPanel(income, expense);
        JPanel wrapper = createDayButtonWrapper(amountPanel);
        dayButton.add(wrapper, BorderLayout.SOUTH);

        Color originalColor = getOriginalDayButtonColor(income, expense);
        dayButton.setBackground(originalColor);
        dayButton.putClientProperty("JButton.buttonType", "roundRect");

        Color activeColor = getActiveDayButtonColor(income, expense);

        dayButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                dayButton.setBackground(activeColor);
                dayButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                dayButton.setBackground(originalColor);
            }
        });

        dayButton.addActionListener(_ -> showTransactionsDialog(yearMonth, day));
        return dayButton;
    }

    private JLabel createStyledDayLabel(String day) {
        JLabel dayLabel = new JLabel(day);
        dayLabel.setFont(new Font("Inter", Font.BOLD, 16));
        dayLabel.setForeground(DAY_TEXT_COLOR);
        return dayLabel;
    }

    private JPanel createAmountPanel(double income, double expense) {
        JPanel amountPanel = new JPanel();
        amountPanel.setLayout(new BoxLayout(amountPanel, BoxLayout.Y_AXIS));
        amountPanel.setOpaque(false);
        amountPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        if (income > 0) {
            JLabel incomeLabel = new JLabel("+" + String.format("%.0f", income));
            incomeLabel.setFont(new Font("Inter", Font.BOLD, 15));
            incomeLabel.setForeground(new Color(34, 197, 94));
            incomeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            incomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
            amountPanel.add(incomeLabel);
        }

        if (expense > 0) {
            JLabel expenseLabel = new JLabel("- " + String.format("%.0f", expense));
            expenseLabel.setFont(new Font("Inter", Font.BOLD, 15));
            expenseLabel.setForeground(new Color(239, 68, 68));
            expenseLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            amountPanel.add(expenseLabel);
        }
        return amountPanel;
    }

    private JPanel createDayButtonWrapper(JPanel amountPanel) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(amountPanel, BorderLayout.EAST);
        return wrapper;
    }

    private Color getOriginalDayButtonColor(double income, double expense) {
        Color profitColor = new Color(180, 252, 200, 200);
        Color lossColor = new Color(254, 226, 226, 200);
        Color neutralColor = new Color(255, 253, 230, 200);

        if (income > expense) {
            return profitColor;
        } else if (expense > income) {
            return lossColor;
        } else if (income == expense && income > 0) {
            return neutralColor;
        }
        return new Color(255, 255, 255);
    }

    private Color getActiveDayButtonColor(double income, double expense) {
        Color activeprofitColor = new Color(150, 240, 150);
        Color activelossColor = new Color(255, 180, 180);
        Color activeneutralColor = new Color(255, 235, 180);
        Color normalHoverColor = new Color(229, 231, 235);
        if (income > expense) {
            return activeprofitColor;
        } else if (expense > income) {
            return activelossColor;
        } else if (income == expense && income > 0) {
            return activeneutralColor;
        } else {
            return normalHoverColor;
        }
    }

    private void showTransactionsDialog(YearMonth yearMonth, int day) {
        LocalDate selectedDate = yearMonth.atDay(day);
        tb = new Tabletransaction(conn, user);
        Object[][] transactions = tb.getTransactionwithDATE(selectedDate.toString());

        if (table != null) {
            table.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][] {},
                    new String[] { "Category", "Description", "Date", "Amount" }));
        }
        if (transactions != null && transactions.length > 0) {
            JDialog dialog = new JDialog();
            dialog.setTitle("Transactions for " + selectedDate);
            dialog.setModal(true);
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(null);
            table = null;
            dialog.add(createTablePanel(selectedDate.toString()));
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null,
                    "No transactions for " + selectedDate,
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private double getDailyIncome(LocalDate date) {
        try (PreparedStatement stmt = conn.prepareStatement(GET_DAILY_INCOME_SQL)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, date.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private double getDailyExpense(LocalDate date) {
        try (PreparedStatement stmt = conn.prepareStatement(GET_DAILY_EXPENSE_SQL)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, date.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private JTable getTable(String date) {
        if (table == null) {
            String[] columns = { "Category", "Description", "Date", "Amount" };
            tb = new Tabletransaction(conn, user);
            Object[][] data = tb.getTransactionwithDATE(date);
            table = new JTable(data, columns);
        }
        return table;
    }

    private JPanel createTablePanel(String date) {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        table = getTable(date);
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

}