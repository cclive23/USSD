package com.example.ussd.service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UssdService {


    public static final Map<String, List<String>> menu = new HashMap<>();


    static {
        menu.put("main", List.of(
                "YOLO Voice",
                "YOLO Internet",
                "Other Bundles",
                "YOLO Star",
                "Balance check"
        ));


//
        menu.put("voice", List.of(
                "100RWF = 30M + 30SMS/24HRS",
                "200RWF = 90M + 10SMS/24HRS",
                "500RWF = 220M + 50SMS/7DAYS",
                "3000RWF = 3500M + 200SMS/30DAYS",
                "0.Exit"
        ));

        menu.put("voice1", List.of(
                "Pay with airtime",
                "  Pay with MoMo  "
        ));


        menu.put("Exit", List.of(
                "Thank you for using our services."

        ));
    }
}
//        menu.put("Error", List.of(
//                "Input error has occured."
//
//        ));
//
//
//
//        menu.put("Send", List.of(
//                "1. Enter receiver number: ",
//                "0.Exit"
//
//        ));
//        menu.put("Buy",List.of(
//                "1. Airtime and Bundles",
//                "2. Electricity",
//                "3. Phones deals",
//                "0. Back"
//        ));
//        menu.put("Airtime", List.of(
//                "1. Buy Airtime",
//                "2. Daily",
//                "3. Monthly",
//                "0. Back"
//        ));
//    }
//
//
//}