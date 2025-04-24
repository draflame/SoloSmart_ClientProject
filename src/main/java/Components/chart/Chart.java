package Components.chart;

import Components.chart.blankchart.BlankPlotChart;
import Components.chart.blankchart.BlankPlotChatRender;
import Components.chart.blankchart.SeriesSize;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Chart extends javax.swing.JPanel {

    private ModelLegend legend;
    private List<ModelChart> model = new ArrayList<>();
    private final int seriesSize = 12;
    private final int seriesSpace = 6;

    public Chart() {
        initComponents();
        blankPlotChart.setBlankPlotChatRender(new BlankPlotChatRender() {
            @Override
            public String getLabelText(int index) {
                return model.get(index).getLabel();
            }

            @Override
            public void renderSeries(BlankPlotChart chart, Graphics2D g2, SeriesSize size, int index) {
                if (model.size() > index && legend != null) {
                    ModelChart currentModel = model.get(index);
                    if (currentModel.getValues().length > 0) {
                        g2.setColor(legend.getColor());
                        double seriesValues = chart.getSeriesValuesOf(currentModel.getValues()[0], size.getHeight());
                        double x = size.getX() + (size.getWidth() / model.size() * index) + (size.getWidth() / model.size() / 2) - (seriesSize / 2);
                        g2.fillRect((int) x, (int) (size.getY() + size.getHeight() - seriesValues), seriesSize, (int) seriesValues);
                    }
                }
            }
        });
    }
    
    public Chart(int maxValue) {
        initComponents(maxValue);
        blankPlotChart.setBlankPlotChatRender(new BlankPlotChatRender() {
            @Override
            public String getLabelText(int index) {
                return model.get(index).getLabel();
            }

            @Override
            public void renderSeries(BlankPlotChart chart, Graphics2D g2, SeriesSize size, int index) {
                if (model.size() > index && legend != null) {
                    ModelChart currentModel = model.get(index);
                    if (currentModel.getValues().length > 0) {
                        g2.setColor(legend.getColor());
                        double seriesValues = chart.getSeriesValuesOf(currentModel.getValues()[0], size.getHeight());
                        double x = size.getX() + (size.getWidth() / model.size() * index) + (size.getWidth() / model.size() / 2) - (seriesSize / 2);
                        g2.fillRect((int) x, (int) (size.getY() + size.getHeight() - seriesValues), seriesSize, (int) seriesValues);
                    }
                }
            }
        });
    }

    public void addLegend(String name, Color color) {
        this.legend = new ModelLegend(name, color);
        panelLegend.removeAll(); // Xóa các legend item cũ (nếu có)
        panelLegend.add(new LegendItem(this.legend)); // Thêm legend item mới
        panelLegend.repaint();
        panelLegend.revalidate();
    }

    public void addData(ModelChart data) {
        model.add(data);
        blankPlotChart.setLabelCount(model.size());
        double max = data.getMaxValues();
        if (max > blankPlotChart.getMaxValues()) {
            blankPlotChart.setMaxValues(max);
        }
    }
    public void clearData() {
        model.clear();
        blankPlotChart.setLabelCount(0); // Reset số lượng nhãn
        blankPlotChart.repaint(); // Yêu cầu vẽ lại biểu đồ
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        blankPlotChart = new BlankPlotChart();
        panelLegend = new javax.swing.JPanel();

        setBackground(new Color(255, 255, 255));

        panelLegend.setOpaque(false);
        panelLegend.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelLegend, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                    .addComponent(blankPlotChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(blankPlotChart, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelLegend, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void initComponents(int maxValue) {

        blankPlotChart = new BlankPlotChart(maxValue);
        panelLegend = new javax.swing.JPanel();

        setBackground(new Color(255, 255, 255));

        panelLegend.setOpaque(false);
        panelLegend.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelLegend, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
                    .addComponent(blankPlotChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(blankPlotChart, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelLegend, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private BlankPlotChart blankPlotChart;
    private javax.swing.JPanel panelLegend;
    // End of variables declaration//GEN-END:variables
}
