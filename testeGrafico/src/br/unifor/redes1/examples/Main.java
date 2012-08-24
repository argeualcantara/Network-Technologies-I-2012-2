package br.unifor.redes1.examples;

import gnu.io.CommPortIdentifier;

import java.util.HashSet;

public class Main {
	public static void main(String[] args) {
//		DiscoverCommPorts.listPorts();
		HashSet<CommPortIdentifier> hash = DiscoverCommPorts.getAvailableSerialPorts();
		for (CommPortIdentifier commPortIdentifier : hash) {
			System.out.println("Nome da porta:"+ commPortIdentifier.getName() + " - Esta sendo Utilizada? " + (commPortIdentifier.isCurrentlyOwned()));
		}
	}
}
