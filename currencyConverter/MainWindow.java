package currencyConverter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.util.ResourceBundle;

public class MainWindow extends JFrame {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("localization.translation"); 
	private JPanel contentPane;
	private JTextField fieldAmount;
	private ArrayList<Currency> currencies = Currency.init();
	
	
	//Create the mainWindow frame
	 
	public MainWindow() {
		setTitle(BUNDLE.getString("MainWindow.this.title")); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 589, 300);
		setLocationRelativeTo(null);
		setResizable( false );
		
		// Window components
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// Label "Convert"
		JLabel lblConvert = new JLabel(BUNDLE.getString("MainWindow.lblConvert.text")); 
		lblConvert.setHorizontalAlignment(SwingConstants.RIGHT);
		lblConvert.setBounds(0, 14, 92, 15);
		contentPane.add(lblConvert);
	
		// ComboBox of the first currency
		final JComboBox<String> comboBoxCountry1 = new JComboBox<String>();
		comboBoxCountry1.setBounds(147, 7, 240, 28);
		populate(comboBoxCountry1, currencies);
		contentPane.add(comboBoxCountry1);
		
		// Label "To"
		JLabel lblTo = new JLabel(BUNDLE.getString("MainWindow.lblTo.text")); 
		lblTo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTo.setBounds(66, 54, 26, 15);
		contentPane.add(lblTo);
		
		// ComboBox of the second currency
		final JComboBox<String> comboBoxCountry2 = new JComboBox<String>();
		comboBoxCountry2.setBounds(147, 47, 240, 28);
		populate(comboBoxCountry2, currencies);
		contentPane.add(comboBoxCountry2);
		
		// Label "Amount"
		final JLabel lblAmount = new JLabel(BUNDLE.getString("MainWindow.lblAmount.text")); 
		lblAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAmount.setBounds(23, 108, 69, 15);
		contentPane.add(lblAmount);
		
		// Textfield where the user 
		fieldAmount = new JTextField();
		fieldAmount.setText("0.00");
		fieldAmount.setBounds(147, 101, 103, 29);
		contentPane.add(fieldAmount);
		fieldAmount.setColumns(10);
		fieldAmount.setDocument(new JTextFieldLimit(8));
     	
		// Label displaying result of conversion
		final JLabel lblResult = new JLabel("");
		lblResult.setHorizontalAlignment(SwingConstants.LEFT);
		lblResult.setBounds(147, 188, 428, 38);
		contentPane.add(lblResult);
		
		// Button "Convert"
		JButton btnConvert = new JButton(BUNDLE.getString("MainWindow.btnConvert.text")); 
		btnConvert.setBounds(147, 142, 129, 38);
		btnConvert.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent arg0) {
	        	String nameCurrency1 = comboBoxCountry1.getSelectedItem().toString();
	        	String nameCurrency2 = comboBoxCountry2.getSelectedItem().toString();
	        	String result;
	        	String formattedPrice;
	        	String formattedAmount; 
	        	Double price;
	        	Double amount = 0.0;
	        	DecimalFormat format = new DecimalFormat("#0.00");
	        	
	        	try {
	        		amount = Double.parseDouble( fieldAmount.getText() ) ;
	        	} catch (NumberFormatException e) {
	        	    e.printStackTrace();
	        	    amount = 0.0;
	        	}
	        	
	        	price = convert(nameCurrency1, nameCurrency2, currencies, amount);
	        	
	        	// Format numbers to avoid "E7" problem
	        	formattedPrice = format.format(price);
	        	formattedAmount = format.format(amount);
	        	
	        	result = formattedAmount + " " + nameCurrency1 + " = " + formattedPrice + " " + nameCurrency2;
	        	lblResult.setText(result);	        	
	        }
	    });		
		contentPane.add(btnConvert);
	}
	
	// Fill comboBox with currencies name
	public static void populate(JComboBox<String> comboBox, ArrayList<Currency> currencies) {
		for (Integer i = 0; i < currencies.size(); i++) {
			comboBox.addItem( currencies.get(i).getName() );
		}		
	}
	
	// Find the short name and the exchange value of the second currency
	public static Double convert(String currency1, String currency2, ArrayList<Currency> currencies, Double amount) {
		String shortNameCurrency2 = null;
		Double exchangeValue;
		Double price = 0.0;
		
		// Find shortname for the second currency
		for (Integer i = 0; i < currencies.size(); i++) {
			if (currencies.get(i).getName() == currency2) {
				shortNameCurrency2 = currencies.get(i).getShortName();
				break;
			}
		}
		
		// Find exchange value and call convert() to calculate the new price
		if (shortNameCurrency2 != null) {
			for (Integer i = 0; i < currencies.size(); i++) {
				if (currencies.get(i).getName() == currency1) {
					exchangeValue = currencies.get(i).getExchangeValues().get(shortNameCurrency2);
					price = Currency.convert(amount, exchangeValue);
					break;
				}
			}
		}
		
		return price;
	}
}