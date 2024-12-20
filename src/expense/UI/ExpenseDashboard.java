package expense.UI;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import expense.Connection.connectdb;
import expense.module.Tabletransaction;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import org.json.JSONArray;
import user.user_class.User;

public class ExpenseDashboard extends JPanel {

    private static final int PANEL_WIDTH = 1200;
    private static final int PANEL_HEIGHT = 800;
    private static final int BORDER_SPACING = 30;
    private static final int CARD_BORDER_SPACING = 20;
    private static final int SHADOW_OFFSET = 5;
    private static final int ROUND_RECT_RADIUS = 15;
    private static final int GRADIENT_CARD_RADIUS = 20;

    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private static final Color CARD_TEXT_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR_PRIMARY = new Color(28, 35, 51);
    private static final Color TEXT_COLOR_SECONDARY = new Color(52, 63, 83);
    private static final Color TABLE_HEADER_COLOR = new Color(52, 73, 94);
    private static final Color TABLE_CELL_COLOR = new Color(249, 250, 251);
    private static final Color TABLE_SELECTION_COLOR = new Color(82, 186, 255, 20);
    private static final Color TABLE_SELECTION_FOREGROUND_COLOR = new Color(28, 35, 51);
    private static final Color INCOMING_COLOR = new Color(25, 135, 84);
    private static final Color EXPENSE_COLOR = new Color(185, 28, 28);
    private static final Color SHADOW_COLOR = new Color(28, 35, 51, 20);

    private static final String BALANCE_TITLE = "Total Balance";
    private static final String EXPENSE_TITLE = "Expenses";
    private static final String INCOME_TITLE = "Income";
    private static final String CHART_TITLE = "Expense Categories";
    private static final String TABLE_TITLE = "Transaction History";
    private static final String AMOUNT_FORMAT = "%.2f$";

    private static final String GET_INCOME_SQL = "SELECT SUM(amount) as total FROM expensetracker WHERE username = ? AND amount > 0";
    private static final String GET_EXPENSE_SQL = "SELECT ABS(SUM(amount)) as total FROM expensetracker WHERE username = ? AND amount < 0";
    private static final String GET_TRANSACTION_SQL = "SELECT DATE(date) as date, " +
            "SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) as income, " +
            "SUM(CASE WHEN amount < 0 THEN ABS(amount) ELSE 0 END) as expense " +
            "FROM expensetracker WHERE username = ? " +
            "GROUP BY DATE(date) ORDER BY date";

    private Connection conn = new connectdb().getconnectdb();
    private Tabletransaction tb;
    private User user;

    public ExpenseDashboard(User user) {
        this.user = user;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(BACKGROUND_COLOR);

        JPanel mainContent = createMainContent();
        add(mainContent, BorderLayout.CENTER);
    }

    private double getIncomeBalance() {
        return executeSumQuery(GET_INCOME_SQL);
    }

    private double getExpenseBalance() {
        return executeSumQuery(GET_EXPENSE_SQL);
    }

    private double executeSumQuery(String sql) {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
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

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.setBorder(new EmptyBorder(BORDER_SPACING, BORDER_SPACING, BORDER_SPACING, BORDER_SPACING));

        JPanel topSection = createTopSection();
        mainContent.add(topSection, BorderLayout.NORTH);

        JPanel centerSection = createCenterSection();
        mainContent.add(centerSection, BorderLayout.CENTER);

        return mainContent;
    }

    private JPanel createTopSection() {
        JPanel topSection = new JPanel(new GridLayout(1, 3, BORDER_SPACING, 0));
        topSection.setBackground(BACKGROUND_COLOR);

        double totalBalance = getIncomeBalance() - getExpenseBalance();

        topSection.add(createGradientCard(BALANCE_TITLE, String.format(AMOUNT_FORMAT, totalBalance),
                new Color(37, 47, 63), new Color(52, 63, 83)));
        topSection.add(createGradientCard(EXPENSE_TITLE, String.format(AMOUNT_FORMAT, getExpenseBalance()),
                new Color(220, 53, 69), new Color(185, 43, 39)));
        topSection.add(createGradientCard(INCOME_TITLE, String.format(AMOUNT_FORMAT, getIncomeBalance()),
                new Color(40, 167, 69), new Color(32, 134, 55)));

        return topSection;
    }

    private JPanel createCenterSection() {
        JPanel centerSection = new JPanel(new GridLayout(1, 2, BORDER_SPACING, 0));
        centerSection.setBackground(BACKGROUND_COLOR);
        centerSection.setBorder(new EmptyBorder(BORDER_SPACING, 0, 0, 0));

        JPanel chartWrapper = createPanelWithShadow(createChartPanel());
        JPanel tableWrapper = createPanelWithShadow(createTablePanel());

        centerSection.add(chartWrapper);
        centerSection.add(tableWrapper);

        return centerSection;
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

        JLabel titleLabel = createStyledLabel(title, CARD_TEXT_COLOR, new Font("Inter", Font.PLAIN, 16));
        card.add(titleLabel, gbc);

        JLabel valueLabel = createStyledLabel(value, CARD_TEXT_COLOR, new Font("Inter", Font.BOLD, 28));
        gbc.insets = new Insets(10, 0, 0, 0);
        card.add(valueLabel, gbc);

        return card;
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

    private JPanel createChartPanel() {
        JPanel chartPanel = createPanelWithTitle(CHART_TITLE);
        JFXPanel chartContent = new JFXPanel();

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            try {
                WebView webView = new WebView();
                String htmlContent = generateChartHTML();
                webView.getEngine().loadContent(htmlContent);
                Scene scene = new Scene(webView);
                chartContent.setScene(scene);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        chartPanel.add(chartContent, BorderLayout.CENTER);
        return chartPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = createPanelWithTitle(TABLE_TITLE);

        String[] columns = { "Category", "Description", "Date", "Amount" };
        tb = new Tabletransaction(conn, user);
        Object[][] data = tb.getTransactions();

        JTable table = new JTable(data, columns) {
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
                        break;
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

        table.setRowHeight(60);
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
        header.setForeground(new Color(52, 73, 94));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(52, 152, 219)));
        header.setPreferredSize(new Dimension(0, 55));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private String generateChartHTML() throws SQLException {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html><html><head>");
        htmlContent.append("<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>");
        htmlContent.append(
                "<style>body{display:flex;justify-content:center;align-items:center;height:100vh;margin:0;background:#fff;}</style>");
        htmlContent.append("</head><body><canvas id='myChart'></canvas>");
        htmlContent.append("<script>");
        JSONArray dates = new JSONArray();
        JSONArray totalBalance = new JSONArray();
        JSONArray incomeData = new JSONArray();
        JSONArray expenseData = new JSONArray();

        try (PreparedStatement stmt = conn.prepareStatement(GET_TRANSACTION_SQL)) {
            stmt.setString(1, user.getUsername());
            try (ResultSet rs = stmt.executeQuery()) {
                double runningTotal = 0;
                while (rs.next()) {
                    dates.put(rs.getString("date"));
                    double income = rs.getDouble("income");
                    double expense = rs.getDouble("expense");
                    runningTotal += (income - expense);
                    totalBalance.put(runningTotal);
                    incomeData.put(income);
                    expenseData.put(expense);
                }
            }
        }

        htmlContent.append("new Chart(document.getElementById('myChart'), {")
                .append("type: 'line',")
                .append("data: {")
                .append("labels: ").append(dates.toString()).append(",")
                .append("datasets: [{")
                .append("label: 'Total Balance',")
                .append("data: ").append(totalBalance.toString()).append(",")
                .append("fill: true,")
                .append("backgroundColor: 'rgba(75, 192, 192, 0.1)',")
                .append("borderColor: 'rgb(75, 192, 192)',")
                .append("tension: 0.4")
                .append("}, {")
                .append("label: 'Income',")
                .append("data: ").append(incomeData.toString()).append(",")
                .append("fill: true,")
                .append("backgroundColor: 'rgba(40, 167, 69, 0.1)',")
                .append("borderColor: 'rgb(40, 167, 69)',")
                .append("tension: 0.4")
                .append("}, {")
                .append("label: 'Expense',")
                .append("data: ").append(expenseData.toString()).append(",")
                .append("fill: true,")
                .append("backgroundColor: 'rgba(220, 53, 69, 0.1)',")
                .append("borderColor: 'rgb(220, 53, 69)',")
                .append("tension: 0.4")
                .append("}]")
                .append("},")
                .append("options: {")
                .append("responsive: true,")
                .append("maintainAspectRatio: false,")
                .append("plugins: {")
                .append("legend: {")
                .append("position: 'bottom',")
                .append("labels: {")
                .append("padding: 20,")
                .append("usePointStyle: true,")
                .append("font: {")
                .append("size: 14,")
                .append("family: 'Inter'")
                .append("}}")
                .append("},")
                .append("tooltip: {")
                .append("mode: 'index',")
                .append("intersect: false,")
                .append("padding: 12,")
                .append("backgroundColor: 'rgba(0,0,0,0.8)',")
                .append("titleFont: {size: 14},")
                .append("bodyFont: {size: 13},")
                .append("callbacks: {")
                .append("label: function(context) {")
                .append("let label = context.dataset.label || '';")
                .append("return label + ': $' + context.parsed.y.toFixed(2);")
                .append("}")
                .append("}")
                .append("}")
                .append("},")
                .append("layout: {")
                .append("padding: {")
                .append("bottom: 50")
                .append("}")
                .append("},")
                .append("scales: {")
                .append("y: {")
                .append("grid: {display: true, color: 'rgba(0,0,0,0.05)'},")
                .append("ticks: {")
                .append("font: {size: 12},")
                .append("callback: function(value) {")
                .append("return '$' + value.toFixed(2);")
                .append("}")
                .append("}")
                .append("},")
                .append("x: {")
                .append("grid: {display: false},")
                .append("ticks: {font: {size: 12}}")
                .append("}")
                .append("}")
                .append("}")
                .append("});")
                .append("</script></body></html>");

        return htmlContent.toString();
    }

    private JLabel createStyledLabel(String text, Color color, Font font) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    private JPanel createPanelWithTitle(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(5, 10, 10, 10));

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

    class RoundedBorder extends AbstractBorder {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY);
            g2.fillRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 5, 5, 5);
        }
    }
}