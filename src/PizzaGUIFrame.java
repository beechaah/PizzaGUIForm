import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class PizzaGUIFrame extends JFrame
{
    private JRadioButton thinCrust, regularCrust, deepDishCrust;
    private JComboBox<String> sizeComboBox;
    private JCheckBox[] toppingsCheckBoxes;
    private JTextArea orderTextArea;
    private JButton orderButton, clearButton, quitButton;
    private ButtonGroup crustGroup;

    public PizzaGUIFrame()
    {
        setTitle("Pizza Order Form");
        setSize(1100, 425);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        JPanel crustPanel = new JPanel();
        crustPanel.setBorder(BorderFactory.createTitledBorder("Type of Crust"));
        thinCrust = new JRadioButton("Thin");
        regularCrust = new JRadioButton("Regular");
        deepDishCrust = new JRadioButton("Deep-dish");
        crustGroup = new ButtonGroup();
        crustGroup.add(thinCrust);
        crustGroup.add(regularCrust);
        crustGroup.add(deepDishCrust);
        crustPanel.add(thinCrust);
        crustPanel.add(regularCrust);
        crustPanel.add(deepDishCrust);
        thinCrust.setFont(new Font("Arial", Font.PLAIN, 20));
        regularCrust.setFont(new Font("Arial", Font.PLAIN, 20));
        deepDishCrust.setFont(new Font("Arial", Font.PLAIN, 20));


        JPanel sizePanel = new JPanel();
        sizePanel.setBorder(BorderFactory.createTitledBorder("Size"));
        String[] sizes = {"Small - $8.00", "Medium - $12.00", "Large - $16.00", "Super - $20.00"};
        sizeComboBox = new JComboBox<>(sizes);
        sizePanel.add(sizeComboBox);
        sizeComboBox.setFont(new Font("Arial", Font.PLAIN, 20));


        JPanel toppingsPanel = new JPanel();
        toppingsPanel.setBorder(BorderFactory.createTitledBorder("Toppings"));
        String[] toppings = {"Teeth", "Iris's", "Olives", "Pencil Lead", "Metal Shreddings", "Clownfish"};
        toppingsCheckBoxes = new JCheckBox[toppings.length];
        for (int i = 0; i < toppings.length; i++)
        {
            toppingsCheckBoxes[i] = new JCheckBox(toppings[i]);
            toppingsPanel.add(toppingsCheckBoxes[i]);
            toppingsCheckBoxes[i].setFont(new Font("Arial", Font.PLAIN, 20));
        }


        JPanel orderPanel = new JPanel();
        orderPanel.setBorder(BorderFactory.createTitledBorder("Order Summary"));
        orderTextArea = new JTextArea(10, 40);
        orderTextArea.setEditable(false);
        orderTextArea.setFont(new Font("Courier New", Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(orderTextArea);
        orderPanel.add(scrollPane);


        JPanel buttonPanel = new JPanel();
        orderButton = new JButton("Order");
        clearButton = new JButton("Clear");
        quitButton = new JButton("Quit");
        buttonPanel.add(orderButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(quitButton);
        orderButton.setFont(new Font("Arial", Font.BOLD, 20));
        clearButton.setFont(new Font("Arial", Font.BOLD, 20));
        quitButton.setFont(new Font("Arial", Font.BOLD, 20));


        add(crustPanel, BorderLayout.NORTH);
        add(sizePanel, BorderLayout.WEST);
        add(toppingsPanel, BorderLayout.CENTER);
        add(orderPanel, BorderLayout.EAST);
        add(buttonPanel, BorderLayout.SOUTH);


        orderButton.addActionListener(new OrderButtonListener());
        clearButton.addActionListener(new ClearButtonListener());
        quitButton.addActionListener(new QuitButtonListener());
    }


    private class OrderButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            StringBuilder orderSummary = new StringBuilder();
            DecimalFormat df = new DecimalFormat("0.00");
            double subtotal = 0;


            String crustType = "";
            if (thinCrust.isSelected())
            {
                crustType = "Thin";
            } else if (regularCrust.isSelected())
            {
                crustType = "Regular";
            } else if (deepDishCrust.isSelected())
            {
                crustType = "Deep-dish";
            } else
            {
                JOptionPane.showMessageDialog(null, "Please select a crust.");
                return;
            }


            String sizeSelection = (String) sizeComboBox.getSelectedItem();
            double sizeCost = 0;
            if (sizeSelection != null)
            {
                if (sizeSelection.contains("Small")) sizeCost = 8.00;
                else if (sizeSelection.contains("Medium")) sizeCost = 12.00;
                else if (sizeSelection.contains("Large")) sizeCost = 16.00;
                else if (sizeSelection.contains("Super")) sizeCost = 20.00;
            }

            subtotal += sizeCost;


            orderSummary.append("=========================================\n");
            orderSummary.append(String.format("%-25s %10s\n", crustType + " Crust & " + sizeSelection, "$" + df.format(sizeCost)));
            orderSummary.append("=========================================\n");
            orderSummary.append("Ingredients:\n");
            int toppingsCount = 0;
            for (JCheckBox checkBox : toppingsCheckBoxes)
            {
                if (checkBox.isSelected())
                {
                    orderSummary.append(String.format("%-25s %10s\n", checkBox.getText(), "$1.00"));
                    toppingsCount++;
                }
            }
            if (toppingsCount == 0)
            {
                JOptionPane.showMessageDialog(null, "Please select at least one topping.");
                return;
            }

            subtotal += toppingsCount;


            double tax = subtotal * 0.07;
            double total = subtotal + tax;


            orderSummary.append("\n-----------------------------------------\n");
            orderSummary.append(String.format("%-25s %10s\n", "Sub-total:", "$" + df.format(subtotal)));
            orderSummary.append(String.format("%-25s %10s\n", "Tax (7%):", "$" + df.format(tax)));
            orderSummary.append("=========================================\n");
            orderSummary.append(String.format("%-25s %10s\n", "Total:", "$" + df.format(total)));

            orderTextArea.setText(orderSummary.toString());
        }
    }


    private class ClearButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            crustGroup.clearSelection();
            sizeComboBox.setSelectedIndex(0);
            for (JCheckBox checkBox : toppingsCheckBoxes)
            {
                checkBox.setSelected(false);
            }
            orderTextArea.setText("");
        }
    }


    private class QuitButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Quit Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmed == JOptionPane.YES_OPTION)
            {
                dispose();
            }
        }
    }
}
