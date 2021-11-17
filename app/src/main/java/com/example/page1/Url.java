package com.example.page1;

/**
 * Created by melayer on 5/8/16.
 */
public interface Url {
//    String PROTOCOL = "http";
//    String SERVER = "173.234.153.82";
//    String PORT = "7373";
//    String APP_NAME = "vehicleftp";
//    String URL_BASE = PROTOCOL + "://" + SERVER + ":" + PORT + "/" + APP_NAME + "/";
//    String URL_LOGIN = URL_BASE + "userLogin";
//    String URL_REGISTER_VEHICLE  = URL_BASE + "saveVehicle";
//    String URL_MODULE  = URL_BASE + "module";
//    String URL_UPLOAD_MODULE = URL_BASE + "saveModule";


    String BASE_URL = "http://ovotappdev.poshsmetal.com/index.asmx/";

    String URL_Login = BASE_URL+"ValidateLogin";
    String GET_PRODUCT_DETAILS_URL = BASE_URL+"GetProductDetails";
    String INVOICE_INSERTION_URL = BASE_URL+"InsertCustomerInvoice";
    String INCENTIVE_REPORT_URL = BASE_URL+"IncentiveReport";

    String GET_ISD_URL = BASE_URL+"GetISDDetails";
    String UPDATE_ISD_URL = BASE_URL+"UpdateISDDetails";

    String UPDATE_ISD_AGREEMENT_URL = BASE_URL+"UpdateISDDAgreement";
}
