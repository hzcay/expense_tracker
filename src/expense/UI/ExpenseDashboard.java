package expense.UI;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

import expense.module.Tabletransaction;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import user.User;

public class ExpenseDashboard extends JPanel {
    private Connection conn;
    private Tabletransaction tb;
    private User user = new User("hzcay", "password");

    public ExpenseDashboard() {
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

        // Center Section with improved spacing and shadows
        JPanel centerSection = new JPanel(new GridLayout(1, 2, 30, 0));
        centerSection.setBackground(new Color(240, 242, 245));
        centerSection.setBorder(new EmptyBorder(30, 0, 0, 0));

        // Add shadow borders to panels
        JPanel chartWrapper = createPanelWithShadow(createChartPanel());
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

    private JPanel createChartPanel() {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        headerPanel.setBackground(Color.WHITE);

        JLabel iconLabel = new JLabel("📊");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setForeground(new Color(28, 35, 51)); // Darker blue matching sidebar

        JLabel titleLabel = new JLabel("Expense Categories");
        titleLabel.setFont(new Font("Inter", Font.BOLD, 24));
        titleLabel.setForeground(new Color(28, 35, 51)); // Darker blue matching sidebar
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        headerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                titleLabel.setForeground(new Color(82, 186, 255)); // Lighter blue on hover
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

        JPanel chartContent = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                Color[] colors = {
                        new Color(255, 99, 132),
                        new Color(54, 162, 235),
                        new Color(255, 206, 86),
                        new Color(75, 192, 192),
                        new Color(153, 102, 255)
                };

                int[] values = { 30, 20, 15, 25, 10 };
                String[] labels = { "Food", "Housing", "Transport", "Shopping", "Others" };
                int total = 0;
                for (int value : values)
                    total += value;

                int padding = 25;
                int diameter = (int) ((Math.min(getWidth(), getHeight() - 60) - padding * 2) * 0.8);
                int centerX = getWidth() / 2;
                // Adjusted centerY to move chart up by 30 pixels
                int centerY = ((getHeight() - 120) / 2) - 30;
                int x = centerX - diameter / 2;
                int y = centerY - diameter / 2;

                // Rest of the code remains the same
                for (int i = 0; i < 5; i++) {
                    g2d.setColor(new Color(0, 0, 0, 3));
                    g2d.fillOval(x + i, y + i, diameter, diameter);
                }

                int startAngle = 0;
                for (int i = 0; i < values.length; i++) {
                    int arcAngle = (int) Math.round(360.0 * values[i] / total);
                    Color baseColor = colors[i];

                    Point2D center = new Point2D.Float(x + diameter / 2, y + diameter / 2);
                    float radius = diameter / 2;
                    float[] dist = { 0.0f, 0.7f, 1.0f };
                    Color[] gradientColors = {
                            baseColor.brighter(),
                            baseColor,
                            baseColor.darker()
                    };
                    RadialGradientPaint paint = new RadialGradientPaint(
                            center, radius, dist, gradientColors);

                    g2d.setPaint(paint);
                    g2d.fillArc(x, y, diameter, diameter, startAngle, arcAngle);

                    double midAngle = Math.toRadians(startAngle + arcAngle / 2);
                    int labelRadius = diameter / 3;
                    int labelX = centerX + (int) (labelRadius * Math.cos(midAngle));
                    int labelY = centerY - (int) (labelRadius * Math.sin(midAngle));

                    String percentage = String.format("%.1f%%", (values[i] * 100.0 / total));
                    g2d.setFont(new Font("Inter", Font.BOLD, 14));
                    FontMetrics fm = g2d.getFontMetrics();

                    g2d.setColor(new Color(0, 0, 0, 60));
                    g2d.drawString(percentage, labelX - fm.stringWidth(percentage) / 2 + 1,
                            labelY + fm.getAscent() / 2 + 1);
                    g2d.setColor(Color.WHITE);
                    g2d.drawString(percentage, labelX - fm.stringWidth(percentage) / 2,
                            labelY + fm.getAscent() / 2);

                    startAngle += arcAngle;
                }

                int legendY = y + diameter + 15;
                g2d.setFont(new Font("Inter", Font.PLAIN, 12));

                int itemWidth = 100;
                int itemHeight = 20;
                int itemsPerRow = 3;
                int horizontalGap = 15;

                for (int i = 0; i < values.length; i++) {
                    int row = i / itemsPerRow;
                    int col = i % itemsPerRow;

                    int startX = centerX - ((itemsPerRow * itemWidth + (itemsPerRow - 1) * horizontalGap) / 2);
                    int legendX = startX + (col * (itemWidth + horizontalGap));
                    int itemY = legendY + (row * (itemHeight + 5));

                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fillRoundRect(legendX + 1, itemY + 1, 16, 16, 6, 6);
                    g2d.setColor(colors[i]);
                    g2d.fillRoundRect(legendX, itemY, 16, 16, 6, 6);

                    g2d.setColor(new Color(33, 37, 41));
                    g2d.drawString(labels[i], legendX + 25, itemY + 12);
                }
            }
        };
        chartContent.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int centerX = chartContent.getWidth() / 2;
                int centerY = ((chartContent.getHeight() - 120) / 2) - 30;
                int diameter = (int) ((Math.min(chartContent.getWidth(), chartContent.getHeight() - 60) - 25 * 2)
                        * 0.8);
                int radius = diameter / 2;

                double dx = x - centerX;
                double dy = y - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= radius) {
                    double angle = Math.toDegrees(Math.atan2(dy, dx)) + 360;
                    angle = (angle + 90) % 360;

                    int startAngle = 0;
                    int[] values = { 30, 20, 15, 25, 10 };
                    String[] labels = { "Food", "Housing", "Transport", "Shopping", "Others" };
                    int total = 0;
                    for (int value : values)
                        total += value;

                    for (int i = 0; i < values.length; i++) {
                        int arcAngle = (int) Math.round(360.0 * values[i] / total);
                        if (angle >= startAngle && angle < startAngle + arcAngle) {
                            JOptionPane.showMessageDialog(chartContent,
                                    "Category: " + labels[i] + "\nValue: " + values[i] + "%");
                            break;
                        }
                        startAngle += arcAngle;
                    }
                }
            }
        });
        chartContent.setBackground(Color.WHITE);
        chartContent.setPreferredSize(new Dimension(0, 350));

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
                            comp.setForeground(new Color(25, 135, 84)); // Darker green for income
                            comp.setBackground(new Color(40, 167, 69, 40)); // More opaque green background
                            jc.setBorder(BorderFactory.createCompoundBorder(
                                    BorderFactory.createMatteBorder(0, 0, 0, 3, new Color(25, 135, 84, 80)),
                                    BorderFactory.createEmptyBorder(12, 20, 12, 20)));
                        } else {
                            comp.setForeground(new Color(185, 28, 28)); // Darker red for expense
                            comp.setBackground(new Color(220, 53, 69, 40)); // More opaque red background
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ExpenseDashboard::new);
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
