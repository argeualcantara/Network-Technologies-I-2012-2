package br.unifor.redes1.layout;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.unifor.redes1.examples.TwoWaySerialComm;

public class JanelaPrincipal extends JFrame{
	private static final long serialVersionUID = 2921056281458821013L;
	private JTextArea textAreaCom;
	private JTextField textFieldCom;
	public TwoWaySerialComm comm;
	
	public JanelaPrincipal(){
		JPanel painelPrincipal = new JPanel();
		painelPrincipal.setLayout(new GridBagLayout());
		GridBagConstraints pos = new GridBagConstraints();
		textAreaCom = new JTextArea();
		JScrollPane paneltxt1 = new JScrollPane(textAreaCom);
		paneltxt1.setSize(400, 380);
		paneltxt1.setPreferredSize(paneltxt1.getSize());
		textAreaCom.setRows(40);
		pos.gridx = 0;
		pos.gridy = 0;
		pos.gridheight = 1;
		pos.gridwidth = 1;
		pos.anchor = pos.WEST;
		painelPrincipal.add(paneltxt1, pos);
		
		textFieldCom = new JTextField();
		textFieldCom.setSize(315,25);
		textFieldCom.setPreferredSize(textFieldCom.getSize());
		textFieldCom.setVisible(true);
		
		pos.gridx = 0;
		pos.gridy = 1;
		
		painelPrincipal.add(textFieldCom, pos);
		
		JButton button = new JButton("Enviar");
		button.setVisible(true);
		button.setActionCommand("sendMessage");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mandarMensagem();
			}
		});
		pos.gridx = 0;
		pos.gridy = 1;
		pos.anchor = pos.EAST;
		painelPrincipal.add(button, pos);
		
		this.add(painelPrincipal);
		this.setBounds(100, 100, 500, 500);
		this.setResizable(false);
		this.setTitle("Cominucação Serial");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	
	protected void mandarMensagem() {
		this.comm.mandarMensagem();
		
	}

	public static JanelaPrincipal showMainWindow() {
		JanelaPrincipal p = new JanelaPrincipal();
		p.setVisible(true);
		return p;
	}
//	public static void main(String[] args) {
//		JanelaPrincipal p = new JanelaPrincipal();
//		p.setVisible(true);
//	}

	/**
	 * @return the textAreaCom
	 */
	public JTextArea getTextAreaCom() {
		return textAreaCom;
	}

	/**
	 * @param textAreaCom the textAreaCom to set
	 */
	public void setTextAreaCom(JTextArea textAreaCom) {
		this.textAreaCom = textAreaCom;
	}

	/**
	 * @return the textFieldCom
	 */
	public JTextField getTextFieldCom() {
		return textFieldCom;
	}

	/**
	 * @param textFieldCom the textFieldCom to set
	 */
	public void setTextFieldCom(JTextField textFieldCom) {
		this.textFieldCom = textFieldCom;
	}

}
