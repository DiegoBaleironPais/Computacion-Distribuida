package GUI;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Buyer extends Agent{
	BuyerGUI gui;

	 protected void setup() {
		 //Despliege de la GUI
		 gui = new BuyerGUI();
		 gui.setVisible(true);

		 registerIntoYellowPages();
		 addBehaviour(new Subasta());
		 addBehaviour(new aceptarCompra());
		 addBehaviour(new subastaPerdida());
	 }

	 protected void takeDown() {
		 try {
			 DFService.deregister(this);
			 gui.dispose();
		 } catch (FIPAException e) {
			 System.err.println(e.getMessage());
		 }
	 }

	private void registerIntoYellowPages () {
		try {
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setName(getAID().getName());
			sd.setType("GUI.Buyer");
			dfd.addServices(sd);
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	private class Subasta extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				Float prize = Float.parseFloat(msg.getContent());
				String title = msg.getConversationId();
				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.PROPOSE);
				reply.setConversationId(title);
				gui.borrarLibro(title, true);
				if (gui.quiereLibro(title)) {
					if (gui.puedeComprarLibro(title, prize)) {
						myAgent.send(reply);
						gui.anadirLibro(title,"EN SUBASTA", prize);
					} else {
						gui.anadirLibro(title,"SP Demasiado caro", prize);
					}
				}
			} else {
				block();
			}
		}
	}

	private class aceptarCompra extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				Float prize = Float.parseFloat(msg.getContent());
				String title = msg.getConversationId();
				gui.borrarLibro(title, true);
				gui.borrarLibro(title, false);
				gui.anadirLibro(title,"EN PROPIEDAD", prize);
			} else {
				block();
			}
		}
	}

	private class subastaPerdida extends CyclicBehaviour {
		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REJECT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				Float prize = Float.parseFloat(msg.getContent());
				String title = msg.getConversationId();
				gui.borrarLibro(title, true);
				gui.anadirLibro(title,"SP por empate", prize);
			} else {
				block();
			}
		}
	}
	 
}
