package expense.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.json.JSONArray;

import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;

import expense.Connection.connectdb;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import user.user_class.User;

public class Analyticspanel extends JPanel {
    private Connection conn = new connectdb().getconnectdb();
    private User user;

    public Analyticspanel(User u) {
        user = u;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 800));
        setBackground(new Color(240, 242, 245));

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

        JPanel topSection = new JPanel(new GridLayout(1, 3, 30, 0));
        topSection.setBackground(new Color(240, 242, 245));

        double totalbalance = getincomebalace() - getexpensebalace();

        topSection.add(createGradientCard("Total Balance", String.format("%.2f", totalbalance) + "$",
                new Color(37, 47, 63), new Color(52, 63, 83)));
        topSection.add(createGradientCard("Expenses", String.format("%.2f", getexpensebalace()) + "$",
                new Color(220, 53, 69), new Color(185, 43, 39)));
        topSection.add(createGradientCard("Income", String.format("%.2f", getincomebalace()) + "$",
                new Color(40, 167, 69), new Color(32, 134, 55)));
        mainContent.add(topSection, BorderLayout.NORTH);

        JPanel centerSection = new JPanel(new GridLayout(1, 2, 30, 0));
        centerSection.setBackground(new Color(240, 242, 245));
        centerSection.setBorder(new EmptyBorder(30, 0, 0, 0));

        JPanel expensechart = createPanelWithShadow(expensechart());
        JPanel incomechart = createPanelWithShadow(incomechart());
        JPanel totalchart = createPanelWithShadow(createTotPanel());

        JPanel expenseTitlePanel = createTitlePanel("Expense");
        JPanel incomeTitlePanel = createTitlePanel("Income");

        expensechart.add(expenseTitlePanel, BorderLayout.NORTH);
        incomechart.add(incomeTitlePanel, BorderLayout.NORTH);

        JPanel rightPanel = new JPanel(new GridLayout(2, 1, 0, 30));
        rightPanel.setBackground(new Color(240, 242, 245));
        rightPanel.add(expensechart);
        rightPanel.add(incomechart);

        centerSection.removeAll();
        centerSection.setLayout(new GridLayout(1, 2, 30, 0));
        centerSection.add(totalchart);
        centerSection.add(rightPanel);
        mainContent.add(centerSection, BorderLayout.CENTER);

        return mainContent;
    }

    private JPanel createTitlePanel(String title) {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setForeground(new Color(28, 35, 51));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                titleLabel.setForeground(new Color(82, 186, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                titleLabel.setForeground(new Color(28, 35, 51));
            }
        });
        headerPanel.add(titleLabel);

        titlePanel.add(headerPanel, BorderLayout.NORTH);

        return titlePanel;
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

    private JPanel expensechart() {
        String sql = "SELECT SUBSTRING(c.name, 3) as category_id, ABS(SUM(e.Amount)) as total FROM expensetracker e JOIN category c ON e.category_id = c.category_id WHERE e.username = ? AND e.Amount < 0 GROUP BY c.name ";

        return createChartPanel(sql);
    }

    private JPanel incomechart() {
        String sql = "SELECT SUBSTRING(c.name, 3) as category_id, ABS(SUM(e.Amount)) as total FROM expensetracker e JOIN category c ON e.category_id = c.category_id WHERE e.username = ? AND e.Amount > 0 GROUP BY c.name";
        return createChartPanel(sql);
    }

    private JPanel createChartPanel(String q) {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JFXPanel chartContent = new JFXPanel();

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            try {
                if (chartContent.getScene() != null) {
                    ((WebView) chartContent.getScene().getRoot()).getEngine().load(null);
                }
                WebView webView = new WebView();
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
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, user.getUsername());
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    labels.put(rs.getString("category_id"));
                    data.put(rs.getDouble("total"));
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
                htmlContent.append("'rgba(255, 160, 122, 0.8)','rgba(221, 160, 221, 0.8)'");
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

                webView.getEngine().loadContent(htmlContent.toString());
                Scene scene = new Scene(webView);
                chartContent.setScene(scene);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        chartPanel.add(chartContent, BorderLayout.CENTER);

        return chartPanel;
    }

    private JPanel createTotPanel() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Expense Categories");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setForeground(new Color(28, 35, 51));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        headerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                titleLabel.setForeground(new Color(82, 186, 255));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                titleLabel.setForeground(new Color(28, 35, 51));
            }
        });

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

        JFXPanel chartContent = new JFXPanel();

        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            try {
                WebView webView = new WebView();
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

                String sql = "SELECT DATE(date) as date, " +
                        "SUM(CASE WHEN amount > 0 THEN amount ELSE 0 END) as income, " +
                        "SUM(CASE WHEN amount < 0 THEN ABS(amount) ELSE 0 END) as expense " +
                        "FROM expensetracker WHERE username = ? " +
                        "GROUP BY DATE(date) ORDER BY date";

                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, user.getUsername());
                ResultSet rs = stmt.executeQuery();

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
                        .append("position: 'bottom',") // Changed from 'top' to 'bottom'
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
                        .append("layout: {") // Added layout configuration
                        .append("padding: {")
                        .append("bottom: 50") // Added padding at bottom for legend
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

                webView.getEngine().loadContent(htmlContent.toString());
                Scene scene = new Scene(webView);
                chartContent.setScene(scene);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        chartPanel.add(titlePanel, BorderLayout.NORTH);
        chartPanel.add(chartContent, BorderLayout.CENTER);

        return chartPanel;
    }

    private JPanel createPanelWithShadow(JPanel content) {
        JPanel wrapper = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(28, 35, 51, 20));
                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);

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
}
