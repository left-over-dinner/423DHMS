package hospitalModule;


/**
* hospitalModule/HospitalPOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from hospitalModule.idl
* Sunday, October 27, 2019 2:11:42 o'clock PM EDT
*/

public abstract class HospitalPOA extends org.omg.PortableServer.Servant
 implements hospitalModule.HospitalOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("addAppointment", new java.lang.Integer (0));
    _methods.put ("removeAppointment", new java.lang.Integer (1));
    _methods.put ("listAppointmentAvailability", new java.lang.Integer (2));
    _methods.put ("bookAppointment", new java.lang.Integer (3));
    _methods.put ("getAppointmentSchedule", new java.lang.Integer (4));
    _methods.put ("cancelAppointment", new java.lang.Integer (5));
    _methods.put ("swapAppointment", new java.lang.Integer (6));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    java.lang.Integer __method = (java.lang.Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // hospitalModule/Hospital/addAppointment
       {
         String appointmentID = in.read_string ();
         String appointmentType = in.read_string ();
         int capacity = in.read_long ();
         String $result = null;
         $result = this.addAppointment (appointmentID, appointmentType, capacity);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 1:  // hospitalModule/Hospital/removeAppointment
       {
         String appointmentID = in.read_string ();
         String appointmentType = in.read_string ();
         String $result = null;
         $result = this.removeAppointment (appointmentID, appointmentType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 2:  // hospitalModule/Hospital/listAppointmentAvailability
       {
         String appointmentType = in.read_string ();
         String $result = null;
         $result = this.listAppointmentAvailability (appointmentType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 3:  // hospitalModule/Hospital/bookAppointment
       {
         String patientID = in.read_string ();
         String appointmentID = in.read_string ();
         String appointmentType = in.read_string ();
         String $result = null;
         $result = this.bookAppointment (patientID, appointmentID, appointmentType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 4:  // hospitalModule/Hospital/getAppointmentSchedule
       {
         String patientID = in.read_string ();
         String $result = null;
         $result = this.getAppointmentSchedule (patientID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 5:  // hospitalModule/Hospital/cancelAppointment
       {
         String patientID = in.read_string ();
         String appointmentID = in.read_string ();
         String $result = null;
         $result = this.cancelAppointment (patientID, appointmentID);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       case 6:  // hospitalModule/Hospital/swapAppointment
       {
         String patientID = in.read_string ();
         String oldAppointmentID = in.read_string ();
         String oldAppointmentType = in.read_string ();
         String newAppointmentID = in.read_string ();
         String newAppointmentType = in.read_string ();
         String $result = null;
         $result = this.swapAppointment (patientID, oldAppointmentID, oldAppointmentType, newAppointmentID, newAppointmentType);
         out = $rh.createReply();
         out.write_string ($result);
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:hospitalModule/Hospital:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public Hospital _this() 
  {
    return HospitalHelper.narrow(
    super._this_object());
  }

  public Hospital _this(org.omg.CORBA.ORB orb) 
  {
    return HospitalHelper.narrow(
    super._this_object(orb));
  }


} // class HospitalPOA
