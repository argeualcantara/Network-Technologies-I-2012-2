package br.unifor.redes1.layout;

import gnu.io.CommPortIdentifier;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import br.unifor.redes1.examples.TwoWaySerialComm;

public class PopUpConnect extends JFrame{
	private static final long serialVersionUID = -4100219208227595276L;
	
	private JTable tabela;
	private JButton botao;
	
	public PopUpConnect(){
		JPanel painelPrincipal = new JPanel();
		painelPrincipal.setLayout(new GridBagLayout());
		GridBagConstraints pos = new GridBagConstraints();
		
		JLabel label1 = new JLabel("Portas disponíveis: ");
		pos.gridx = 0;
		pos.gridy = 0;
		pos.anchor = pos.NORTH;
		painelPrincipal.add(label1, pos);
		
		tabela = new JTable();
		DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
		modelo.setRowCount(0);
		modelo.setColumnCount(0);
		modelo.addColumn("Porta");
		
		Enumeration<CommPortIdentifier> lista = CommPortIdentifier.getPortIdentifiers();
		while(lista.hasMoreElements()){
			CommPortIdentifier porta = lista.nextElement();
			if(!porta.isCurrentlyOwned()){
				modelo.addRow(new String [] {porta.getName()});
			}
		}
		pos.gridx = 0;
		pos.gridy = 1;
		pos.gridwidth = 2;
		pos.gridheight = 2;
		pos.anchor = pos.NORTH;
		painelPrincipal.add(tabela, pos);
		
		botao = new JButton("Conectar");
		botao.setActionCommand("connect");
		pos.gridx = 0;
		pos.gridy = 4;
		pos.gridwidth = 1;
		pos.gridheight = 2;
		pos.anchor = pos.CENTER;
		painelPrincipal.add(botao, pos);
		
		botao.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(e.getActionCommand().equals("connect")){
					String portName = String.valueOf(
							PopUpConnect.this.tabela.getValueAt(
									tabela.getSelectedRow(), tabela.getSelectedColumn()));
					try {
						PopUpConnect.this.setVisible(false);
						JanelaPrincipal p = JanelaPrincipal.showMainWindow();
						
						TwoWaySerialComm comm = new TwoWaySerialComm(p);
						comm.connect(portName);
						p.comm = comm;
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
			}
			});
		this.add(painelPrincipal);
		this.setBounds(100, 100, 300, 150);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public static void main(String[] args) {
		PopUpConnect main = new PopUpConnect();
		main.setVisible(true);
	}
}
