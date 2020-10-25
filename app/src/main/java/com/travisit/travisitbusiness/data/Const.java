package com.travisit.travisitbusiness.data;

public class Const {

    public static String DEFAULT_SERVER_ADDRESS = "http://ec2-54-214-89-166.us-west-2.compute.amazonaws.com/api/";
    public static String IMAGES_SERVER_ADDRESS = "http://ec2-54-214-89-166.us-west-2.compute.amazonaws.com/";
    public static Long  REQUEST_TIMEOUT = 60L;
    public static Long  WRITE_TIMEOUT = 60L;
    public static Long  CONNECT_TIMEOUT = 60L;
    //for Location
    public static final String PACKAGE_NAME="com.travisit.travisitbusiness";
    public static final String RESULT_ADDRESS_KEY=PACKAGE_NAME+".ADDRESS_RESULT_DATA_KEY";
    public static final String ADDRESS_RECEIVR =PACKAGE_NAME+".ADDRESS_RECEIVER";
    public static final String LOCATION_DATA_EXTRA=PACKAGE_NAME+".LOCATION_DATA_EXTRA";
    public static final int SUCCESS_RESULT=1;
    public static final int FAILURE_RESULT=0;
    //Internet Access State
    public static final String BroadCast_String_For_Action="checkInternet";


}
