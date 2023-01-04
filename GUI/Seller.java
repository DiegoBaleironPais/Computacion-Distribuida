package GUI;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.Iterator;

public class Seller extends Agent {
	 private ArrayList<SellerBooks> sellerBooks;
	 private SellerGUI myGui;
     private DFAgentDescription[] results;

	 // Put agent initializations here
	 protected void setup() {
	     sellerBooks = new ArrayList<SellerBooks>();
		 myGui = new SellerGUI(this);
		 myGui.setVisible(true);

		 addBehaviour(new TickerBehaviour(this, 10000) {
			 protected void onTick() {
                 try {
                     // Build the description used as template for the search
                     DFAgentDescription template = new DFAgentDescription();
                     ServiceDescription templateSd = new ServiceDescription();
                     templateSd.setType("GUI.Buyer");
                     template.addServices(templateSd);

                     results = DFService.search(myAgent, template);
                     if (results.length > 0) {
                         for (int i = 0; i < results.length; ++i) {
                             DFAgentDescription dfd = results[i];
                         }
                     }

                 } catch (FIPAException fe) {
                     System.err.println(fe.getMessage());
                 }
             }
		 } );
	 }

	 // Put agent clean-up operations here
	 protected void takeDown() {
	 	System.out.println("GUI.Seller-agent "+getAID().getName()+" terminating.");
	 }

     public void anadirLibro(SellerBooks libro) {
        addBehaviour(new Subasta(libro));
     }

     private class Subasta extends Behaviour {
        private SellerBooks libro;
        private AID bestBuyer;
        private MessageTemplate mt;
        private int test = 0;
        private int numeroCompradores;
        private ArrayList<AID> subastadoresEmpatadores;

        public Subasta (SellerBooks libro) {
             this.libro = libro;
        }

         @Override
         public void action() {
             switch (test) {
                 case 0:
                     ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                     for (int i = 0; i < results.length; ++i) {
                         cfp.addReceiver(results[i].getName());
                     }
                     cfp.setContent(String.valueOf(libro.getActualPrize()));
                     cfp.setConversationId(libro.getName());
                     cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
                     myAgent.send(cfp);
                     // Prepare the template to get proposals
                     mt = MessageTemplate.and(MessageTemplate.MatchConversationId(libro.getName()),
                             MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                     break;
                 case 1:
                     numeroCompradores = 0;
                     long t= System.currentTimeMillis();
                     long end = t+10000;
                     while(System.currentTimeMillis() < end) {
                         ACLMessage reply = myAgent.receive(mt);
                         if (reply != null) {
                             if (reply.getPerformative() == ACLMessage.PROPOSE) {
                                 if (numeroCompradores == 0) {
                                     subastadoresEmpatadores = new ArrayList<AID>();
                                     bestBuyer = reply.getSender();
                                 } else {
                                     System.out.println("Añadiendo empatador");
                                     subastadoresEmpatadores.add(reply.getSender());
                                 }
                                 numeroCompradores++;
                                 myGui.anhadirLibro(libro.getName(),"EN SUBASTA", String.valueOf(libro.getActualPrize()),
                                         String.valueOf(numeroCompradores));
                             }
                         }
                         else {
                             block();
                         }
                     }

             }
         }

         @Override
         public boolean done() {
             if ((numeroCompradores == 0 || numeroCompradores == 1) && bestBuyer != null) {
                 System.out.println(numeroCompradores);
                 if (numeroCompradores==0) {
                     libro.setActualPrize(libro.getActualPrize() - libro.getAumento());
                     ACLMessage rechazo = new ACLMessage(ACLMessage.REJECT_PROPOSAL);
                     rechazo.setContent(String.valueOf(libro.getActualPrize()));
                     rechazo.setConversationId(libro.getName());
                     Iterator<AID> it = subastadoresEmpatadores.iterator();
                     while (it.hasNext()) {
                         System.out.println("Entrando");
                         rechazo.addReceiver(it.next());
                     }
                     myAgent.send(rechazo);
                 }
                 ACLMessage confimacionVenta = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                 confimacionVenta.setContent(String.valueOf(libro.getActualPrize()));
                 confimacionVenta.setConversationId(libro.getName());
                 confimacionVenta.addReceiver(bestBuyer);
                 myAgent.send(confimacionVenta);
                 myGui.borrarLibro(libro.getName());
                 myGui.anhadirLibro(libro.getName(),"SUBASTADO", String.valueOf(libro.getActualPrize()),
                         bestBuyer.toString());
                 return true;
             } else {
                 if (test == 0) {
                     test = 1;
                 } else {
                     if (numeroCompradores >= 2) {
                         libro.setActualPrize(libro.getActualPrize()+ libro.getAumento());
                     }
                     test = 0;
                 }
                 return false;
             }
         }
     }

}


