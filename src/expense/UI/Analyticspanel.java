package expense.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.json.JSONArray;

import expense.Connection.connectdb;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import user.user_class.User;

public class Analyticspanel extends JPanel {

    private static final int PANEL_WIDTH = 1200;
    private static final int PANEL_HEIGHT = 800;
    private static final int BORDER_SPACING = 30;
    private static final int CARD_BORDER_SPACING = 20;
    private static final int SHADOW_OFFSET = 5;
    private static final int ROUND_RECT_RADIUS = 15;
    private static final int GRADIENT_CARD_RADIUS = 20;
    private static final int CHART_PADDING = 10;

    private static final Color BACKGROUND_COLOR = new Color(240, 242, 245);
    private static final Color CARD_TEXT_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_COLOR_PRIMARY = new Color(28, 35, 51);
    private static final Color SHADOW_COLOR = new Color(28, 35, 51, 20);

    private static final String BALANCE_TITLE = "Total Balance";
    private static final String EXPENSE_TITLE = "Expenses";
    private static final String INCOME_TITLE = "Income";
    private static final String EXPENSE_CHART_TITLE = "Expense";
    private static final String INCOME_CHART_TITLE = "Income";
    private static final String TOTAL_CHART_TITLE = "Expense Categories";
    private static final String AMOUNT_FORMAT = "%.2f$";

    private static final String GET_INCOME_SQL = "SELECT SUM(amount) as total FROM expensetracker WHERE username = ? AND amount > 0";
    private static final String GET_EXPENSE_SQL = "SELECT ABS(SUM(amount)) as total FROM expensetracker WHERE username = ? AND amount < 0";

    private static final String GET_EXPENSE_CATEGORY_SQL = "SELECT SUBSTRING(c.name, 3) as category_id, ABS(SUM(e.Amount)) as total FROM expensetracker e JOIN category c ON e.category_id = c.category_id WHERE e.username = ? AND e.Amount < 0 GROUP BY c.name ";
    private static final String GET_INCOME_CATEGORY_SQL = "SELECT SUBSTRING(c.name, 3) as category_id, ABS(SUM(e.Amount)) as total FROM expensetracker e JOIN category c ON e.category_id = c.category_id WHERE e.username = ? AND e.Amount > 0 GROUP BY c.name";
    private static final String GET_TRANSACTION_SQL = "SELECT DATE(date) as date, " +
            "SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) as income, " +
            "SUM(CASE WHEN amount < 0 THEN ABS(amount) ELSE 0 END) as expense " +
            "FROM expensetracker WHERE username = ? " +
            "GROUP BY DATE(date) ORDER BY date";

    private Connection conn = new connectdb().getconnectdb();
    private User user;

    public Analyticspanel(User user) {
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

        JPanel expenseChart = createPanelWithShadow(createChartPanel(GET_EXPENSE_CATEGORY_SQL, EXPENSE_CHART_TITLE));
        JPanel incomeChart = createPanelWithShadow(createChartPanel(GET_INCOME_CATEGORY_SQL, INCOME_CHART_TITLE));
        JPanel totalChart = createPanelWithShadow(createTotalChartPanel());

        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, BORDER_SPACING));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.add(expenseChart);
        rightPanel.add(incomeChart);

        centerSection.removeAll();
        centerSection.setLayout(new GridLayout(1, 2, BORDER_SPACING, 0));
        centerSection.add(totalChart);
        centerSection.add(rightPanel);
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

    private JLabel createStyledLabel(String text, Color color, Font font) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
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

    private JPanel createChartPanel(String sql, String title) {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(new EmptyBorder(CHART_PADDING, CHART_PADDING, CHART_PADDING, CHART_PADDING));

        JPanel titlePanel = createTitlePanel(title);
        chartPanel.add(titlePanel, BorderLayout.NORTH);

        JFXPanel chartContent = new JFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            try {
                if (chartContent.getScene() != null) {
                    ((WebView) chartContent.getScene().getRoot()).getEngine().load(null);
                }
                WebView webView = new WebView();
                String htmlContent = generateChartHTML(sql);
                webView.getEngine().loadContent(htmlContent);
                Scene scene = new Scene(webView);
                chartContent.setScene(scene);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        chartPanel.add(chartContent, BorderLayout.CENTER);
        return chartPanel;
    }

    private JPanel createTotalChartPanel() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JPanel titlePanel = createTitlePanel(TOTAL_CHART_TITLE);
        chartPanel.add(titlePanel, BorderLayout.NORTH);

        JFXPanel chartContent = new JFXPanel();
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            try {
                if (chartContent.getScene() != null) {
                    ((WebView) chartContent.getScene().getRoot()).getEngine().load(null);
                }
                WebView webView = new WebView();
                String htmlContent = generateLineChartHTML();
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

    private String generateChartHTML(String q) throws SQLException {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html><html><head>");
        htmlContent.append(
                "<script src='https://cdn.jsdelivr.net/npm/chart.js'></script>");
        htmlContent.append(
                "<style>body{display:flex;justify-content:center;align-items:center;height:100vh;margin:0;background:#fff;} canvas{max-width:100%;}</style>");
        htmlContent
                .append("</head><body><div style='width:95%;height:95%;'><canvas id='myChart'></canvas></div>");
        htmlContent.append("<script>");

        JSONArray data = new JSONArray();
        JSONArray labels = new JSONArray();
        String sql = q;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    labels.put(rs.getString("category_id"));
                    data.put(rs.getDouble("total"));
                }
            }
        }

        htmlContent.append("const ctx = document.getElementById('myChart');");
        htmlContent.append("new Chart(ctx, {type: 'doughnut',data: {");
        htmlContent.append("labels: " + labels.toString() + ",");
        htmlContent.append("datasets: [{");
        htmlContent.append("data: " + data.toString() + ",");
        htmlContent.append("backgroundColor: [");
        htmlContent.append("'rgba(255, 99, 132, 0.8)','rgba(54, 162, 235, 0.8)',");
        htmlContent.append("'rgba(255, 206, 86, 0.8)','rgba(75, 192, 192, 0.8)',");
        htmlContent.append("'rgba(153, 102, 255, 0.8)','rgba(255, 159, 64, 0.8)',");
        htmlContent.append("'rgba(69, 183, 209, 0.8)','rgba(150, 201, 61, 0.8)',");
        htmlContent.append("'rgba(255, 160, 122, 0.8)','rgba(221, 160, 221, 0.8)',");
        htmlContent.append("'rgba(106, 90, 205, 0.8)','rgba(64, 224, 208, 0.8)',");
        htmlContent.append("'rgba(255, 105, 180, 0.8)','rgba(154, 205, 50, 0.8)',");
        htmlContent.append("'rgba(218, 112, 214, 0.8)','rgba(30, 144, 255, 0.8)'");
        htmlContent.append("],");
        htmlContent.append("borderWidth: 2,");
        htmlContent.append("borderColor: '#ffffff',");
        htmlContent.append("hoverOffset: 4");
        htmlContent.append("}]},");
        htmlContent.append("options: {");
        htmlContent.append("layout: {");
        htmlContent.append("padding: { left: 0, right: 10 }");
        htmlContent.append("},");
        htmlContent.append("cutout: '65%',");
        htmlContent.append("responsive: true,");
        htmlContent.append("maintainAspectRatio: false,");
        htmlContent.append("plugins: {");
        htmlContent.append("legend: {");
        htmlContent.append("position: 'right',");
        htmlContent.append("labels: {");
        htmlContent.append("padding: 15,");
        htmlContent.append("usePointStyle: true,");
        htmlContent.append("font: {size: 12,family: 'Inter'}");
        htmlContent.append("}},");
        htmlContent.append("tooltip: {");
        htmlContent.append("callbacks: {");
        htmlContent.append("label: function(context) {");
        htmlContent.append("let label = context.label || '';");
        htmlContent.append("let value = context.parsed;");
        htmlContent.append("let total = context.dataset.data.reduce((a,b) => a+b, 0);");
        htmlContent.append("let percentage = ((value * 100) / total).toFixed(1);");
        htmlContent.append("return `${label}: $${value} (${percentage}%)`;");
        htmlContent.append("}},");
        htmlContent.append("padding: 12,");
        htmlContent.append("backgroundColor: 'rgba(0,0,0,0.8)',");
        htmlContent.append("titleFont: {size: 14},");
        htmlContent.append("bodyFont: {size: 13}");
        htmlContent.append("}");
        htmlContent.append("}");
        htmlContent.append("}});");

        htmlContent.append("</script></body></html>");
        return htmlContent.toString();
    }

    private String generateLineChartHTML() throws SQLException {
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
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
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
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        panel.add(titlePanel, BorderLayout.NORTH);
        return panel;
    }
}