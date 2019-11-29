package server;

import hospitalModule.Hospital;
import hospitalModule.HospitalHelper;
import hospitalUtil.HospitalServer;
import org.omg.CORBA.ORB;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class FrontendServer {
    public static void main(String[] args){
        ORB orb=null;
        try{
            String[] args1 = {};
            // create and initialize the ORB //
            orb = ORB.init(args1, null);

            // get reference to rootpoa &amp; activate
            POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            rootpoa.the_POAManager().activate();

            // create servant and register it with the ORB
            Frontend fr = new Frontend();

            // get object reference from the servant
            org.omg.CORBA.Object ref = rootpoa.servant_to_reference(fr);

            // and cast the reference to a CORBA reference
            Hospital href = HospitalHelper.narrow(ref);

            // get the root naming context
            // NameService invokes the transient name service
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");

            // Use NamingContextExt, which is part of the
            // Interoperable Naming Service (INS) specification.
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // bind the Object Reference in Naming
            NameComponent path[] = ncRef.to_name("DHMSFrontend");
            ncRef.rebind(path, href);
            orb.run();
        }catch (Exception e){
            System.out.println("Unable to create server for DHMSFronted");
            e.printStackTrace();
        }
    }
}
