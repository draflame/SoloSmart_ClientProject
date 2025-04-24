package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeChooser extends JPanel {
    private YearMonth currentMonth;
    private JPanel daysPanel;
    private JLabel monthLabel;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private JTextField textReference;
    private final JPopupMenu popup = new JPopupMenu();
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;

    public CustomDateTimeChooser() {
        setLayout(new BorderLayout());
        selectedDate = LocalDate.now();
        selectedTime = LocalTime.now();
        currentMonth = YearMonth.from(selectedDate);

        // Header: tháng/năm và nút điều hướng
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.decode("#3a8a7d"));
        JButton prev = createNavButton("<");
        JButton next = createNavButton(">");
        monthLabel = new JLabel("", JLabel.CENTER);
        monthLabel.setForeground(Color.WHITE);
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.add(prev, BorderLayout.WEST);
        header.add(monthLabel, BorderLayout.CENTER);
        header.add(next, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Panel hiển thị ngày
        daysPanel = new JPanel(new GridLayout(0, 7));
        add(daysPanel, BorderLayout.CENTER);

        // Panel chọn giờ và phút
        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timePanel.setBackground(Color.WHITE);
        SpinnerModel hourModel = new SpinnerNumberModel(selectedTime.getHour(), 0, 23, 1);
        hourSpinner = new JSpinner(hourModel);
        JSpinner.NumberEditor hourEditor = new JSpinner.NumberEditor(hourSpinner, "00");
        hourSpinner.setEditor(hourEditor);
        timePanel.add(new JLabel("Giờ:"));
        timePanel.add(hourSpinner);

        SpinnerModel minuteModel = new SpinnerNumberModel(selectedTime.getMinute(), 0, 59, 1);
        minuteSpinner = new JSpinner(minuteModel);
        JSpinner.NumberEditor minuteEditor = new JSpinner.NumberEditor(minuteSpinner, "00");
        minuteSpinner.setEditor(minuteEditor);
        timePanel.add(new JLabel("Phút:"));
        timePanel.add(minuteSpinner);
        add(timePanel, BorderLayout.SOUTH);

        prev.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            updateCalendar();
        });

        next.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            updateCalendar();
        });

        updateCalendar();
        popup.setLayout(new BorderLayout());
        popup.add(this, BorderLayout.CENTER);
    }

    private void updateCalendar() {
        daysPanel.removeAll();
        LocalDate first = currentMonth.atDay(1);
        int firstDayOfWeek = first.getDayOfWeek().getValue(); // 1 = Monday
        int daysInMonth = currentMonth.lengthOfMonth();

        // Tiêu đề các thứ
        String[] headers = {"Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"};
        for (String s : headers) {
            JLabel l = new JLabel(s, JLabel.CENTER);
            l.setForeground(Color.DARK_GRAY);
            l.setFont(new Font("SansSerif", Font.BOLD, 12));
            daysPanel.add(l);
        }

        int day = 1;
        for (int i = 1; i < firstDayOfWeek; i++) {
            daysPanel.add(new JLabel());
        }

        for (; day <= daysInMonth; day++) {
            final int d = day;
            boolean isSelected = selectedDate != null &&
                    selectedDate.getYear() == currentMonth.getYear() &&
                    selectedDate.getMonthValue() == currentMonth.getMonthValue() &&
                    selectedDate.getDayOfMonth() == d;

            DayButton btn = new DayButton(String.valueOf(d), isSelected);
            btn.addActionListener(e -> {
                selectedDate = LocalDate.of(currentMonth.getYear(), currentMonth.getMonth(), d);
                updateCalendar(); // Cập nhật lại highlight
                updateTextReference();
                popup.setVisible(false);
            });
            daysPanel.add(btn);
        }

        daysPanel.revalidate();
        daysPanel.repaint();
        monthLabel.setText(currentMonth.getMonth() + " - " + currentMonth.getYear());
    }

    public void setTextReference(JTextField textField) {
        this.textReference = textField;
        this.textReference.setEditable(false);
        this.textReference.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.textReference.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopup();
            }
        });
        // Mặc định hiện ngày và giờ hôm nay
        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }
        if (selectedTime == null) {
            selectedTime = LocalTime.now();
            ((SpinnerNumberModel) hourSpinner.getModel()).setValue(selectedTime.getHour());
            ((SpinnerNumberModel) minuteSpinner.getModel()).setValue(selectedTime.getMinute());
        }
        updateTextReference();

        hourSpinner.addChangeListener(e -> {
            selectedTime = LocalTime.of((Integer) hourSpinner.getValue(), selectedTime.getMinute());
            updateTextReference();
        });

        minuteSpinner.addChangeListener(e -> {
            selectedTime = LocalTime.of(selectedTime.getHour(), (Integer) minuteSpinner.getValue());
            updateTextReference();
        });
    }
private void updateTextReference() {
        if (textReference != null) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            textReference.setText( timeFormatter.format(selectedTime)+ " " +dateFormatter.format(selectedDate) );
        }
    }
    private void showPopup() {
        if (popup.isVisible()) {
            popup.setVisible(false);
        } else {
            popup.show(textReference, 0, textReference.getHeight());
        }
    }

    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(false);
        return btn;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }
     public void setDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            selectedDate = dateTime.toLocalDate();
            selectedTime = dateTime.toLocalTime();
            currentMonth = YearMonth.from(selectedDate);

            // Cập nhật giao diện
            ((SpinnerNumberModel) hourSpinner.getModel()).setValue(selectedTime.getHour());
            ((SpinnerNumberModel) minuteSpinner.getModel()).setValue(selectedTime.getMinute());
            updateCalendar();
            updateTextReference();
        }
    }
     public LocalDateTime getSelectedDateTime() {
        return LocalDateTime.of(selectedDate, selectedTime);
    }
    // Nút ngày với highlight nếu được chọn
     private static class DayButton extends JButton {
        public DayButton(String text, boolean isSelected) {
            super(text);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(true);
            setBackground(isSelected ? Color.decode("#3a8a7d") : Color.WHITE);
            setForeground(isSelected ? Color.WHITE : Color.BLACK);
            setFont(new Font("SansSerif", Font.PLAIN, 12));
        }
    }
}
