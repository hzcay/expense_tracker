package expense.UI;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import expense.module.Tabletransaction;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.embed.swing.JFXPanel;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import user.User;

import org.json.JSONArray;
import expense.Connection.connectdb;

public class ExpenseDashboard extends JPanel {
    private Connection conn = new connectdb().getconnectdb();
    private Tabletransaction tb;
    private User user;

    public ExpenseDashboard(User u) {
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

        JPanel centerSection = new JPanel(new GridLayout(1, 2, 30, 0));
        centerSection.setBackground(new Color(240, 242, 245));
        centerSection.setBorder(new EmptyBorder(30, 0, 0, 0));

        JPanel chartWrapper = createPanelWithShadow(createTotPanel());
        JPanel tableWrapper = createPanelWithShadow(createTablePanel());

        centerSection.add(chartWrapper);
        centerSection.add(tableWrapper);
        mainContent.add(centerSection, BorderLayout.CENTER);

        return mainContent;
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

                // Center align all cells
                if (comp instanceof JLabel) {
                    ((JLabel) comp).setHorizontalAlignment(SwingConstants.CENTER);
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

            // Custom header renderer with centered text
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
        table.setSelectionBackground(new Color(82, 186, 255, 15));
        table.setSelectionForeground(new Color(28, 35, 51));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(new Font("Product Sans", Font.BOLD, 24));
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

        tablePanel.add(titlePanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        return tablePanel;
    }

    // RoundedBorder class for rounded edges
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
